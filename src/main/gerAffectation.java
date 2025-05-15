
package main;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.Set;
import javax.swing.JFrame;
import javax.swing.table.JTableHeader;


public class gerAffectation extends javax.swing.JFrame {
   
    private String currentCin;

    public gerAffectation(String cin) {
        this.currentCin=cin;
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        initComponents();
        // Configure table header
        JTableHeader header = jTable1.getTableHeader();
        header.setPreferredSize(new Dimension(header.getWidth(), 50)); // Set header height (e.g., 50px)
        header.setFont(new Font("Segoe UI", Font.BOLD, 24)); // Larger font
        loadAffectations();
        // Ajouter le listener pour détecter les modifications
        jTable1.getModel().addTableModelListener(new TableModelListener() {
            public void tableChanged(TableModelEvent e) {
                int row = e.getFirstRow();
                int column = e.getColumn();
                
                // Détecter toute modification
                if (row != -1 && column != -1) {
                    Object newValue = jTable1.getValueAt(row, column);  // Nouvelle valeur dans la cellule modifiée
                    String idAffectation = jTable1.getValueAt(row, 0).toString();  // ID de l'affectation (colonne 0)

                    // Appeler la méthode pour mettre à jour la base de données
                    updateAffectation(idAffectation, column, newValue.toString());
                }
            }
        });

        // Gérer les actions des boutons
        
        btnedit.addActionListener(e -> editAffectation());
        btndelete.addActionListener(e -> deleteAffectation());}
    private Connection connectToDatabase() throws SQLException {
        return DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/salle_tp", "root", "123456");
    }

    private void updateAffectation(String idAffectation, int column, String newValue) {
    try (Connection con = connectToDatabase()) {
     System.out.println("DEBUG - Début update - ID: " + idAffectation + 
                         ", Colonne: " + column + ", Valeur: " + newValue);    
        // 1. Récupérer les valeurs actuelles
        String currentSql = "SELECT * FROM affectation WHERE id_affect = ?";
        PreparedStatement currentPst = con.prepareStatement(currentSql);
        currentPst.setString(1, idAffectation);
        ResultSet rs = currentPst.executeQuery();
        
        if (!rs.next()) {
            JOptionPane.showMessageDialog(this, "Affectation introuvable", "Erreur", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // 2. Préparer les nouvelles valeurs
        String idEns = rs.getString("id_ens");
        String idSalle = rs.getString("id_salle");
        String jour = rs.getString("jour");
        String heureDeb = rs.getString("heure_deb");
        String heureFin = rs.getString("heure_fin");
        String nbreEtud = rs.getString("nbre_etud");
        System.out.println("DEBUG - Valeurs actuelles: " + 
                         String.join("|", idEns, idSalle, jour, heureDeb, heureFin, nbreEtud));
        
        // Mettre à jour la valeur modifiée
        switch (column) {
            case 1: idEns = newValue; break;
            case 2: idSalle = newValue; break;
            case 3: jour = newValue.trim().toLowerCase(); // Trim whitespace
                    // Validate jour against allowed days
                    Set<String> allowedDays = Set.of("lundi", "mardi", "mercredi", "jeudi", "vendredi", "samedi");
                     if (!allowedDays.contains(jour)) {
                        JOptionPane.showMessageDialog(this, 
                        "Jour invalide. Les jours valides sont du lundi au samedi.", 
                        "Erreur", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                break;
            case 4: heureDeb = newValue; break;
            case 5: heureFin = newValue; break;
            case 6: nbreEtud = newValue; break;
            default: throw new SQLException("Colonne invalide");
        }
  System.out.println("DEBUG - Nouvelles valeurs: " + 
                         String.join("|", idEns, idSalle, jour, heureDeb, heureFin, nbreEtud));
        // 3. Vérifier maintenance
        String checkMaintenance = "SELECT COUNT(*) FROM maintenance WHERE num_salle = ? AND jour = ?";
        try (PreparedStatement pst = con.prepareStatement(checkMaintenance)) {
            pst.setString(1, idSalle);
            pst.setString(2, jour);
            ResultSet rsMaint = pst.executeQuery();
            if (rsMaint.next() && rsMaint.getInt(1) > 0) {
                  JOptionPane.showMessageDialog(this, "Erreur: Salle en maintenance ce jour", "Erreur", JOptionPane.ERROR_MESSAGE);
                return;
            }
        }
// 5. Vérification du nbre max d aff de la salle
    int totalaff = 0;
    int maxaff=0;
    String queryQuotaSalle = "SELECT nbr_max_aff FROM salle WHERE id_salle = ?";
try (PreparedStatement pstQuota = con.prepareStatement(queryQuotaSalle)) {
    pstQuota.setString(1, idSalle);
    ResultSet rsQuota = pstQuota.executeQuery();
    if (rsQuota.next()) {
        maxaff = rsQuota.getInt("nbr_max_aff") ; 
    }
}

// 6.2 Calculer la somme des affectations existantes (en minutes) pour cette salle et cette semaine
String queryTotalAffectation = """
    SELECT count(*) as total
    FROM affectation 
    WHERE id_salle = ?
""";
try (PreparedStatement pstTotal = con.prepareStatement(queryTotalAffectation)) {
    pstTotal.setString(1, idSalle);
    ResultSet rsTotal = pstTotal.executeQuery();
    if (rsTotal.next()) {
        totalaff = rsTotal.getInt("total");
    }
}




if ( totalaff+ 1 > (maxaff)) {
    JOptionPane.showMessageDialog(this, String.format(
        "Erreur: La salle a atteint son nombre max d'affectations (%d aff max).",
        maxaff));
    return;
}
        // 4. Vérifier occupation salle (exclure l'affectation actuelle)
        String checkOccupation = """
            SELECT COUNT(*) FROM affectation 
            WHERE id_salle = ? AND jour = ? AND id_affect != ?
            AND (
                (? BETWEEN heure_deb AND heure_fin)
                OR (? BETWEEN heure_deb AND heure_fin)
                OR (heure_deb BETWEEN ? AND ?)
            )""";
        try (PreparedStatement pst = con.prepareStatement(checkOccupation)) {
            pst.setString(1, idSalle);
            pst.setString(2, jour);
            pst.setString(3, idAffectation);
            pst.setString(4, heureDeb);
            pst.setString(5, heureFin);
            pst.setString(6, heureDeb);
            pst.setString(7, heureFin);
            ResultSet rsOcc = pst.executeQuery();
            if (rsOcc.next() && rsOcc.getInt(1) > 0) {
                  JOptionPane.showMessageDialog(this, 
                    "salle est déja occupée", 
                    "Erreur de saisie", JOptionPane.ERROR_MESSAGE);
                
                return;
            }
        }
        // 5. Vérifier quota horaire hebdomadaire (si modification concerne horaire ou jour)
    // 3. Validation des heures
 
        try {
            LocalTime debut, fin;
            debut = LocalTime.parse(heureDeb);
            fin = LocalTime.parse(heureFin);
            
           LocalTime heureMin = LocalTime.of(8, 0);
LocalTime heureMax = LocalTime.of(18, 0);

if (fin.isBefore(debut)) {
    JOptionPane.showMessageDialog(this, 
        "L'heure de fin doit être après l'heure de début", 
        "Erreur de saisie", JOptionPane.ERROR_MESSAGE);
    return;
}

if (debut.isBefore(heureMin) || fin.isAfter(heureMax)) {
    JOptionPane.showMessageDialog(this, 
        "Les horaires doivent être compris entre 08:00 et 18:00", 
        "Heure invalide", JOptionPane.ERROR_MESSAGE);
    return;
}
        } catch (DateTimeParseException e) {
            JOptionPane.showMessageDialog(this, 
                "Format d'heure invalide (HH:MM requis)", 
                "Erreur de format", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
       
// 5. Vérification de la capacité de la salle
        String checkCapaciteSalle = "SELECT capacite FROM salle WHERE id_salle = ?";
        try (PreparedStatement pst = con.prepareStatement(checkCapaciteSalle)) {
            pst.setString(1, idSalle);
            ResultSet rsCapacite = pst.executeQuery();
            if (rsCapacite.next()) {
                int capaciteSalle = rsCapacite.getInt("capacite");
                if (Integer.parseInt(nbreEtud) > capaciteSalle) {
                    JOptionPane.showMessageDialog(this, 
                        "Erreur: Le nombre d'étudiants dépasse la capacité de la salle (" + capaciteSalle + " étudiants max)", 
                        "Erreur", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }
        }
        //heure
           try {
            LocalTime debut, fin;
            debut = LocalTime.parse(heureDeb);
            fin = LocalTime.parse(heureFin);
            
           LocalTime heureMin = LocalTime.of(8, 0);
LocalTime heureMax = LocalTime.of(18, 0);

if (fin.isBefore(debut)) {
    JOptionPane.showMessageDialog(this, 
        "L'heure de fin doit être après l'heure de début", 
        "Erreur de saisie", JOptionPane.ERROR_MESSAGE);
    return;
}

if (debut.isBefore(heureMin) || fin.isAfter(heureMax)) {
    JOptionPane.showMessageDialog(this, 
        "Les horaires doivent être compris entre 08:00 et 18:00", 
        "Heure invalide", JOptionPane.ERROR_MESSAGE);
    return;
}
        } catch (DateTimeParseException e) {
            JOptionPane.showMessageDialog(this, 
                "Format d'heure invalide (HH:MM requis)", 
                "Erreur de format", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // 4. Vérification quota horaire hebdomadaire (si modification horaire/jour)
       
        // 6. Effectuer la mise à jour
        String updateSql = "";
        switch (column) {
            case 1: updateSql = "UPDATE affectation SET id_ens = ? WHERE id_affect = ?"; break;
            case 2: updateSql = "UPDATE affectation SET id_salle = ? WHERE id_affect = ?"; break;
            case 3: updateSql = "UPDATE affectation SET jour = ? WHERE id_affect = ?"; break;
            case 4: updateSql = "UPDATE affectation SET heure_deb = ? WHERE id_affect = ?"; break;
            case 5: updateSql = "UPDATE affectation SET heure_fin = ? WHERE id_affect = ?"; break;
            case 6: updateSql = "UPDATE affectation SET nbre_etud = ? WHERE id_affect = ?"; break;
        }

        try (PreparedStatement pst = con.prepareStatement(updateSql)) {
            pst.setString(1, newValue);
            pst.setString(2, idAffectation);
            pst.executeUpdate();
            loadAffectations();
            JOptionPane.showMessageDialog(this, "Affectation mise à jour avec succès", "Succès", JOptionPane.INFORMATION_MESSAGE);
        }
    } catch (SQLException ex) {
          if (ex.getMessage().contains("foreign key constraint fails") && ex.getMessage().contains("fk_affectation_enseignant")){
              JOptionPane.showMessageDialog(this,"l'enseignant n'existe pas");
              return ;}
         if (ex.getMessage().contains("foreign key constraint fails")&& ex.getMessage().contains("id_salle")){
              JOptionPane.showMessageDialog(this,"la salle n'existe pas");
              return ;
             
         }
          

          else{
              JOptionPane.showMessageDialog(this, "Erreur : " + ex.getMessage(), "Exception", JOptionPane.ERROR_MESSAGE);}
       
    } catch (DateTimeParseException ex) {
        JOptionPane.showMessageDialog(this, 
        "Format d'heure invalide (HH:MM requis)", 
        "Erreur de format", JOptionPane.ERROR_MESSAGE);
        
    } catch (NumberFormatException ex) { 
          JOptionPane.showMessageDialog(this, 
        "Nombre d'étudiants invalide", 
        "Erreur de saisie", JOptionPane.ERROR_MESSAGE);
    }
}

private void editAffectation() {
    int selectedRow = jTable1.getSelectedRow();
    if (selectedRow == -1) {
        JOptionPane.showMessageDialog(this, "Sélectionnez une ligne !");
        return;
    }

    String idAffectation = jTable1.getValueAt(selectedRow, 0).toString();
    int column = jTable1.getSelectedColumn();
    
    if (column == 0) {
        JOptionPane.showMessageDialog(this, "L'ID ne peut pas être modifié.");
        return;
    }

    String columnName = jTable1.getColumnName(column);
    String currentValue = jTable1.getValueAt(selectedRow, column).toString();
    String newValue = JOptionPane.showInputDialog(this, 
        "Modifier " + columnName + ":", currentValue);

    if (newValue != null && !newValue.equals(currentValue)) {
        updateAffectation(idAffectation, column, newValue);
    }
}


    

    
    private void deleteAffectation() {
        int selected = jTable1.getSelectedRow();
        if (selected == -1) {
            JOptionPane.showMessageDialog(this, "Sélectionnez une ligne !");
            return;
        }
        String idAffectation = jTable1.getValueAt(selected, 0).toString();

        try (Connection con = connectToDatabase()) {
            PreparedStatement pst = con.prepareStatement("DELETE FROM affectation WHERE id_affect = ?");
            pst.setString(1, idAffectation);
            pst.executeUpdate();
            loadAffectations();  // Recharger les affectations après la suppression
              JOptionPane.showMessageDialog(this, "Affectation supprimée avec succès !");
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Erreur lors de la suppression : " + ex.getMessage());
        }
    }
    public int getNbMinutesEnseignees(String cin) {
    int nbreMinutes = 0;

    String query = """
        SELECT 
            SUM(TIMESTAMPDIFF(MINUTE, heure_deb, heure_fin)) AS diff_minutes
        FROM affectation
        WHERE id_ens = ?
    """;

    try (Connection conn = connectToDatabase();
         PreparedStatement stmt = conn.prepareStatement(query)) {

        // Remplacer le ? par le CIN de l'enseignant
        stmt.setString(1, cin);

        // Exécuter la requête
        ResultSet rs = stmt.executeQuery();

        // Récupérer le résultat
        if (rs.next()) {
            nbreMinutes = rs.getInt("diff_minutes"); // Récupère le total des minutes
        }

    } catch (SQLException e) {
        e.printStackTrace();
    }

    return nbreMinutes; // Retourne le nombre total de minutes
}

    private void loadAffectations() {
        try (Connection con = connectToDatabase()) {
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM affectation");

            DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
            model.setRowCount(0); // Vider d'abord le tableau

            while (rs.next()) {
                Object[] row = {
                    rs.getInt("id_affect"),
                    rs.getString("id_ens"),
                    rs.getString("id_salle"),
                    rs.getString("jour"),
                    rs.getTime("heure_deb"),
                    rs.getTime("heure_fin"),
                    rs.getInt("nbre_etud")
                };
                model.addRow(row);
            }

            rs.close();
            st.close();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erreur lors du chargement des affectations : " + e.getMessage());
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        btnedit = new javax.swing.JButton();
        btndelete = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        btnok = new javax.swing.JButton();
        jTextField1 = new javax.swing.JTextField();
        jTextField2 = new javax.swing.JTextField();
        jTextField5 = new javax.swing.JTextField();
        jTextField6 = new javax.swing.JTextField();
        jComboBox1 = new javax.swing.JComboBox<>();
        jTextField3 = new javax.swing.JTextField();
        exitButton1 = new javax.swing.JButton();
        goBackButton = new javax.swing.JButton();
        deconnecterButton = new javax.swing.JButton();
        jLabel7 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setUndecorated(true);
        setResizable(false);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jTable1.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null}
            },
            new String [] {
                "id_aff", "id_ens", "id_salle", "jour", "heure_deb", "heure_fin", "nbre_etud"
            }
        ));
        jTable1.setRowHeight(90);
        jTable1.setShowHorizontalLines(true);
        jTable1.setShowVerticalLines(true);
        jTable1.getTableHeader().setReorderingAllowed(false);
        jScrollPane1.setViewportView(jTable1);

        getContentPane().add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(400, 140, 800, 450));

        btnedit.setBorderPainted(false);
        btnedit.setContentAreaFilled(false);
        btnedit.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnedit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btneditActionPerformed(evt);
            }
        });
        getContentPane().add(btnedit, new org.netbeans.lib.awtextra.AbsoluteConstraints(900, 610, 150, 70));

        btndelete.setBorderPainted(false);
        btndelete.setContentAreaFilled(false);
        btndelete.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        getContentPane().add(btndelete, new org.netbeans.lib.awtextra.AbsoluteConstraints(1070, 610, 140, 70));

        jPanel1.setOpaque(false);
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        btnok.setBorderPainted(false);
        btnok.setContentAreaFilled(false);
        btnok.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnok.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnokActionPerformed(evt);
            }
        });
        jPanel1.add(btnok, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 490, 140, 70));
        jPanel1.add(jTextField1, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 120, 200, 40));
        jPanel1.add(jTextField2, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 180, 200, 40));
        jPanel1.add(jTextField5, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 370, 140, 40));
        jPanel1.add(jTextField6, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 430, 140, 40));

        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "lundi", "mardi", "mercredi", "jeudi","vendredi","samedi" }));
        jComboBox1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox1ActionPerformed(evt);
            }
        });
        jPanel1.add(jComboBox1, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 240, 200, 40));
        jPanel1.add(jTextField3, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 310, 140, 40));

        getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 120, 340, 570));

        exitButton1.setForeground(new java.awt.Color(255, 255, 255));
        exitButton1.setBorder(null);
        exitButton1.setContentAreaFilled(false);
        exitButton1.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        exitButton1.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        exitButton1.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        exitButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                exitButton1ActionPerformed(evt);
            }
        });
        getContentPane().add(exitButton1, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 720, 160, 60));

        goBackButton.setFont(new java.awt.Font("Segoe UI", 1, 30)); // NOI18N
        goBackButton.setForeground(new java.awt.Color(255, 255, 255));
        goBackButton.setToolTipText("");
        goBackButton.setBorder(null);
        goBackButton.setBorderPainted(false);
        goBackButton.setContentAreaFilled(false);
        goBackButton.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        goBackButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        goBackButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                goBackButtonActionPerformed(evt);
            }
        });
        getContentPane().add(goBackButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(540, 710, 200, 80));

        deconnecterButton.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        deconnecterButton.setForeground(new java.awt.Color(255, 255, 255));
        deconnecterButton.setBorder(null);
        deconnecterButton.setBorderPainted(false);
        deconnecterButton.setContentAreaFilled(false);
        deconnecterButton.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        deconnecterButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deconnecterButtonActionPerformed(evt);
            }
        });
        getContentPane().add(deconnecterButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(1050, 700, 140, 70));

        jLabel7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/gerAffectation.jpg"))); // NOI18N
        getContentPane().add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1280, 800));

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void btnokActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnokActionPerformed
                                                 
    try {
        String idEns = jTextField1.getText();
        String idSalle = jTextField2.getText();
        String jour = (String) jComboBox1.getSelectedItem();
        String heureDeb = jTextField3.getText();
        String heureFin = jTextField5.getText();
        int nbreEtud = Integer.parseInt(jTextField6.getText());

        LocalTime debut = LocalTime.parse(heureDeb);
        LocalTime fin = LocalTime.parse(heureFin);
        

        try (Connection con = connectToDatabase()) {
            //  Vérifier la maintenance
            String checkMaintenance = "SELECT COUNT(*) FROM maintenance WHERE num_salle = ? AND jour = ?";
            PreparedStatement pstMaint = con.prepareStatement(checkMaintenance);
            pstMaint.setString(1, idSalle);
            pstMaint.setString(2, jour);
            ResultSet rsMaint = pstMaint.executeQuery();
            rsMaint.next();
            if (rsMaint.getInt(1) > 0) {
                JOptionPane.showMessageDialog(this, "Erreur: La salle est en maintenance ce jour");
                return;
            }
            // verifier que l enseignant existe dans la base
            String checkEnseignant = "SELECT COUNT(*) FROM enseignant WHERE cin = ?";
            try (PreparedStatement pstEns = con.prepareStatement(checkEnseignant)) {
                pstEns.setString(1, idEns);
                try (ResultSet rsEns = pstEns.executeQuery()) {
                         rsEns.next();
                         if (rsEns.getInt(1) == 0) {
                                JOptionPane.showMessageDialog(this, "Erreur : L'enseignant avec le CIN spécifié n'existe pas.");
                                    return;
                        }
    }
}      
            // 5. Vérification du quota d'heures hebdomadaires de la salle

            // heure_deb et heure_fin
               try {
            
            
           LocalTime heureMin = LocalTime.of(8, 0);
LocalTime heureMax = LocalTime.of(18, 0);

if (fin.isBefore(debut)) {
    JOptionPane.showMessageDialog(this, 
        "L'heure de fin doit être après l'heure de début", 
        "Erreur de saisie", JOptionPane.ERROR_MESSAGE);
    return;
}

if (debut.isBefore(heureMin) || fin.isAfter(heureMax)) {
    JOptionPane.showMessageDialog(this, 
        "Les horaires doivent être compris entre 08:00 et 18:00", 
        "Heure invalide", JOptionPane.ERROR_MESSAGE);
    return;
}
        } catch (DateTimeParseException e) {
            JOptionPane.showMessageDialog(this, 
                "Format d'heure invalide (HH:MM requis)", 
                "Erreur de format", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
            // verifier salle existe dans la base:
            String checkSalle = "SELECT COUNT(*) FROM salle WHERE id_salle = ?";
try (PreparedStatement pstSalle = con.prepareStatement(checkSalle)) {
    pstSalle.setString(1, idSalle);
    try (ResultSet rsSalle = pstSalle.executeQuery()) {
        rsSalle.next();
        if (rsSalle.getInt(1) == 0) {
            JOptionPane.showMessageDialog(this, "Erreur : La salle avec l'ID spécifié n'existe pas.");
            return;
        }
    }
}
            //  Vérifier l'occupation
            String checkOccupation = """
                SELECT COUNT(*) FROM affectation 
                WHERE id_salle = ? AND jour = ? 
                AND (
                    (? BETWEEN heure_deb AND heure_fin) 
                    OR (? BETWEEN heure_deb AND heure_fin)
                    OR (heure_deb BETWEEN ? AND ?)
                )""";
            PreparedStatement pstOcc = con.prepareStatement(checkOccupation);
            pstOcc.setString(1, idSalle);
            pstOcc.setString(2, jour);
            pstOcc.setString(3, heureDeb);
            pstOcc.setString(4, heureFin);
            pstOcc.setString(5, heureDeb);
            pstOcc.setString(6, heureFin);
            ResultSet rsOcc = pstOcc.executeQuery();
            rsOcc.next();
            if (rsOcc.getInt(1) > 0) {
                JOptionPane.showMessageDialog(this, "Erreur: La salle est déjà occupée pendant ce créneau");
                return;
            }

            // 6. Vérifier quota d'affectation de la salle
int quotaSalle = 0;
int totalaff=0;

String queryQuotaSalle = "SELECT nbr_max_aff FROM salle WHERE id_salle = ?";
try (PreparedStatement pstQuota = con.prepareStatement(queryQuotaSalle)) {
    pstQuota.setString(1, idSalle);
    ResultSet rsQuota = pstQuota.executeQuery();
    if (rsQuota.next()) {
        quotaSalle = rsQuota.getInt("nbr_max_aff") ; 
    }
}

String queryTotalAffectation = """
    SELECT count(*) as total
    FROM affectation 
    WHERE id_salle = ?
""";
try (PreparedStatement pstTotal = con.prepareStatement(queryTotalAffectation)) {
    pstTotal.setString(1, idSalle);
    ResultSet rsTotal = pstTotal.executeQuery();
    if (rsTotal.next()) {
        totalaff = rsTotal.getInt("total");
    }
}




if ( totalaff+ 1 > (quotaSalle)) {
    JOptionPane.showMessageDialog(this, String.format(
        "Erreur: La salle a atteint son nombre max d'affectations (%d aff max).",
        quotaSalle));
    return;
}
        
            // 3. Vérifier capacité salle
            String checkCapaciteSalle = "SELECT capacite FROM salle WHERE id_salle = ?";
            PreparedStatement pstCapacite = con.prepareStatement(checkCapaciteSalle);
            pstCapacite.setString(1, idSalle);
            ResultSet rsCapacite = pstCapacite.executeQuery();
            if (rsCapacite.next()) {
                int capaciteSalle = rsCapacite.getInt("capacite");
                if (nbreEtud > capaciteSalle) {
                    JOptionPane.showMessageDialog(this, "Erreur: Le nombre d'étudiants dépasse la capacité de la salle (" + capaciteSalle + " étudiants max)");
                    return;
                }
            }

            // 4. Vérifier quota horaire enseignant
            int nbre_max = 0;
            int nbre_sem=getNbMinutesEnseignees(idEns)+(int)ChronoUnit.MINUTES.between(debut, fin);
            System.out.println(nbre_sem);
    String query = """
        SELECT 
            nbre_max_heure as nbre_max
        FROM enseignant
        WHERE cin = ?
    """;

    try (Connection conn = connectToDatabase();
         PreparedStatement stmt = conn.prepareStatement(query)) {

        // Remplacer le ? par le CIN de l'enseignant
        stmt.setString(1,idEns);

        // Exécuter la requête
        ResultSet rs = stmt.executeQuery();

        // Récupérer le résultat
        if (rs.next()) {
            nbre_max = (rs.getInt("nbre_max"))*60;
        }

    } catch (SQLException e) {
        e.printStackTrace();
    }
     if (nbre_sem > nbre_max) {
                    JOptionPane.showMessageDialog(this, String.format(
                        "Erreur: Quota horaire dépassé (%d cette semaine)", nbre_max));
                            return;
                }
     
           
            // 5. Insertion
            String sql = "INSERT INTO affectation (id_ens, id_salle, jour, heure_deb, heure_fin, nbre_etud) VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setString(1, idEns);
            pst.setString(2, idSalle);
            pst.setString(3, jour);
            pst.setString(4, heureDeb);
            pst.setString(5, heureFin);
            pst.setInt(6, nbreEtud);
            pst.executeUpdate();

            JOptionPane.showMessageDialog(this, "Affectation ajoutée avec succès !");
            loadAffectations();
        }

    } catch (DateTimeParseException ex) {
        JOptionPane.showMessageDialog(this, "Format d'heure invalide (HH:mm:ss attendu)");
    } catch (NumberFormatException ex) {
        JOptionPane.showMessageDialog(this, "Erreur dans les champs numériques");
    } catch (SQLException ex) {
        JOptionPane.showMessageDialog(this, "Erreur SQL : " + ex.getMessage());
    }

    }//GEN-LAST:event_btnokActionPerformed

    private void btneditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btneditActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btneditActionPerformed

    private void jComboBox1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox1ActionPerformed
          String selectedjour = (String) jComboBox1.getSelectedItem();

    if (null == selectedjour) 
        System.out.println("Unknown day selected");
 
    }//GEN-LAST:event_jComboBox1ActionPerformed

    private void exitButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_exitButton1ActionPerformed
        System.exit(0);
    }//GEN-LAST:event_exitButton1ActionPerformed

    private void goBackButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_goBackButtonActionPerformed
        new admin_interface(currentCin).setVisible(true);
        dispose();
    }//GEN-LAST:event_goBackButtonActionPerformed

    private void deconnecterButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deconnecterButtonActionPerformed
        new Sign_in().setVisible(true);
        dispose();
    }//GEN-LAST:event_deconnecterButtonActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btndelete;
    private javax.swing.JButton btnedit;
    private javax.swing.JButton btnok;
    private javax.swing.JButton deconnecterButton;
    private javax.swing.JButton exitButton1;
    private javax.swing.JButton goBackButton;
    private javax.swing.JComboBox<String> jComboBox1;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JTextField jTextField3;
    private javax.swing.JTextField jTextField5;
    private javax.swing.JTextField jTextField6;
    // End of variables declaration//GEN-END:variables
}
