package main;
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

/**
 *
 * @author topto
 */
public class gerAffectation extends javax.swing.JFrame {
private String cin;    


    /**
     * Creates new form gerAffectation
     */
    public gerAffectation() {
        this.cin=cin;
        initComponents();
        loadAffectations();
        jPanel1.setVisible(false);
    
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
        return DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/salle_tp", "root", "hendsql131004");
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
            case 3: jour = newValue; break;
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
// 5. Vérification du quota d'heures hebdomadaires de la salle
try {
    // Calculer la durée de la nouvelle affectation
    Duration nouvelleDuree = Duration.between(LocalTime.parse(heureDeb), LocalTime.parse(heureFin));

    // Récupérer les affectations existantes pour la salle (hors celle en cours de modif)
    String sqlTotalDuree = """
        SELECT heure_deb, heure_fin FROM affectation
        WHERE id_salle = ? AND id_affect != ?
    """;
    PreparedStatement pst = con.prepareStatement(sqlTotalDuree);
    pst.setString(1, idSalle);
    pst.setString(2, idAffectation);
    ResultSet rsDurees = pst.executeQuery();

    long totalMinutes = 0;
    while (rsDurees.next()) {
        LocalTime deb = LocalTime.parse(rsDurees.getString("heure_deb"));
        LocalTime fin = LocalTime.parse(rsDurees.getString("heure_fin"));
        totalMinutes += Duration.between(deb, fin).toMinutes();
    }

    long nouvelleDureeMinutes = nouvelleDuree.toMinutes();
    long totalAvecNouvelle = totalMinutes + nouvelleDureeMinutes;

    // Récupérer le quota max de la salle
    String sqlQuota = "SELECT nbr_max_aff FROM salle WHERE id_salle = ?";
    PreparedStatement pstQuota = con.prepareStatement(sqlQuota);
    pstQuota.setString(1, idSalle);
    ResultSet rsQuota = pstQuota.executeQuery();

    if (rsQuota.next()) {
        int maxMinutes = rsQuota.getInt("nbr_max_aff") * 60; // Si stocké en heures
        if (totalAvecNouvelle > maxMinutes) {
            JOptionPane.showMessageDialog(this,
                "Erreur : Ce créneau dépasse le quota hebdomadaire de la salle (" +
                (maxMinutes / 60) + " heures maximum)",
                "Quota dépassé", JOptionPane.ERROR_MESSAGE);
            return;
        }
    }
} catch (Exception e) {
    JOptionPane.showMessageDialog(this, "Erreur durant la vérification du quota : " + e.getMessage(),
        "Erreur", JOptionPane.ERROR_MESSAGE);
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
    // <editor-fold defaultstate="collapsed" desc="Generated Code">                          
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        btnadd = new javax.swing.JButton();
        btnedit = new javax.swing.JButton();
        btndelete = new javax.swing.JButton();
        btnretour = new javax.swing.JButton();
        btndisconnect = new javax.swing.JButton();
        btnexit = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        btnok = new javax.swing.JButton();
        jTextField1 = new javax.swing.JTextField();
        jTextField2 = new javax.swing.JTextField();
        jTextField5 = new javax.swing.JTextField();
        jTextField6 = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jComboBox1 = new javax.swing.JComboBox<>();
        jTextField3 = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

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
        jScrollPane1.setViewportView(jTable1);

        getContentPane().add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 30, -1, 164));

        btnadd.setText("add");
        btnadd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnaddActionPerformed(evt);
            }
        });
        getContentPane().add(btnadd, new org.netbeans.lib.awtextra.AbsoluteConstraints(400, 210, -1, -1));

        btnedit.setText("edit");
        btnedit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btneditActionPerformed(evt);
            }
        });
        getContentPane().add(btnedit, new org.netbeans.lib.awtextra.AbsoluteConstraints(530, 210, -1, -1));

        btndelete.setText("delete");
        getContentPane().add(btndelete, new org.netbeans.lib.awtextra.AbsoluteConstraints(650, 210, -1, -1));

        btnretour.setText("retour");
        btnretour.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnretourActionPerformed(evt);
            }
        });
        getContentPane().add(btnretour, new org.netbeans.lib.awtextra.AbsoluteConstraints(380, 310, -1, -1));

        btndisconnect.setText("disconnect");
        btndisconnect.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btndisconnectActionPerformed(evt);
            }
        });
        getContentPane().add(btndisconnect, new org.netbeans.lib.awtextra.AbsoluteConstraints(550, 310, -1, -1));

        btnexit.setText("exit");
        btnexit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnexitActionPerformed(evt);
            }
        });
        getContentPane().add(btnexit, new org.netbeans.lib.awtextra.AbsoluteConstraints(740, 310, -1, -1));

        btnok.setText("ok");
        btnok.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnokActionPerformed(evt);
            }
        });

        jTextField1.setText("jTextField1");

        jTextField2.setText("jTextField2");

        jTextField5.setText("jTextField5");

        jTextField6.setText("jTextField6");

        jLabel3.setText("jour");

        jLabel2.setText("id_salle");

        jLabel1.setText("id_ens");

        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "lundi", "mardi", "mercredi", "jeudi","vendredi","samedi" }));
        jComboBox1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox1ActionPerformed(evt);
            }
        });

        jTextField3.setText("jTextField3");

        jLabel4.setText("heure_deb");

        jLabel5.setText("heure_fin");

        jLabel6.setText("nbre_etud");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jLabel4, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jLabel2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 64, Short.MAX_VALUE)))
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addComponent(jLabel5, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel6, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 57, Short.MAX_VALUE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, 156, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addComponent(jTextField6, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 155, Short.MAX_VALUE)
                                .addComponent(jTextField5, javax.swing.GroupLayout.Alignment.LEADING))
                            .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 143, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(6, 6, 6)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, 128, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(39, 39, 39)
                        .addComponent(btnok)))
                .addGap(17, 20, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGap(33, 33, 33)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(1, 1, 1)))
                .addGap(11, 11, 11)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(6, 6, 6)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jTextField5, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextField6, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(btnok)
                .addGap(18, 18, 18))
        );

        getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 0, 270, -1));

        pack();
    }// </editor-fold>                        

    private void btnretourActionPerformed(java.awt.event.ActionEvent evt) {                                          
try (Connection con = connectToDatabase()) {
        // Vérifier le rôle de l'utilisateur
        String query = "SELECT role FROM user1 WHERE cin = ?";
        PreparedStatement pst = con.prepareStatement(query);
        pst.setString(1, this.cin);
        ResultSet rs = pst.executeQuery();
        
        if (rs.next()) {
            String role = rs.getString("role");
            
            if ("admin".equalsIgnoreCase(role)) {
                new admin_interface(this.cin).setVisible(true);
            } else {
                new user_interface(this.cin).setVisible(true);
            }
            this.dispose(); // Fermer la fenêtre actuelle
        }
    } catch (SQLException ex) {
        JOptionPane.showMessageDialog(this, "Erreur de connexion: " + ex.getMessage());
    }        // TODO add your handling code here:
    }                                         

    private void btnexitActionPerformed(java.awt.event.ActionEvent evt) {                                        
     System.exit(0);        // TODO add your handling code here:
    }                                       

    private void btndisconnectActionPerformed(java.awt.event.ActionEvent evt) {                                              
         this.dispose();
        new Sign_in().setVisible(true);
    }                                             

    private void btnaddActionPerformed(java.awt.event.ActionEvent evt) {                                       
    jPanel1.setVisible(true); // Affiche le formulaire
    jTable1.clearSelection(); // DÃ©sÃ©lectionne toute ligne
    jTextField1.setText("00000000");
    jTextField2.setText("");
    jComboBox1.setSelectedItem("");
    jTextField3.setText("hh:mm:ss");
    jTextField5.setText("hh:mm:ss");
    jTextField6.setText("");
    // DÃ©sactive modifier/supprimer si on passe en ajout
    btnadd.setEnabled(false);
    btnedit.setEnabled(false);
    btndelete.setEnabled(false);
    
     
    
    
        
    

    
               
    }                                      

    private void btnokActionPerformed(java.awt.event.ActionEvent evt) {                                      
                                                 
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
int totalMinutesAffectees = 0;
int dureeNouvelleAffectation = (int) ChronoUnit.MINUTES.between(debut, fin);

// 6.1 Récupérer le quota max d'affectation pour la salle
String queryQuotaSalle = "SELECT nbr_max_aff FROM salle WHERE id_salle = ?";
try (PreparedStatement pstQuota = con.prepareStatement(queryQuotaSalle)) {
    pstQuota.setString(1, idSalle);
    ResultSet rsQuota = pstQuota.executeQuery();
    if (rsQuota.next()) {
        quotaSalle = rsQuota.getInt("nbr_max_aff") ; 
    }
}

// 6.2 Calculer la somme des affectations existantes (en minutes) pour cette salle et cette semaine
String queryTotalAffectation = """
    SELECT SUM(TIMESTAMPDIFF(MINUTE, heure_deb, heure_fin)) AS total 
    FROM affectation 
    WHERE id_salle = ? AND jour IN ('Lundi', 'Mardi', 'Mercredi', 'Jeudi', 'Vendredi', 'Samedi')
""";
try (PreparedStatement pstTotal = con.prepareStatement(queryTotalAffectation)) {
    pstTotal.setString(1, idSalle);
    ResultSet rsTotal = pstTotal.executeQuery();
    if (rsTotal.next()) {
        totalMinutesAffectees = rsTotal.getInt("total");
    }
}

if (totalMinutesAffectees + dureeNouvelleAffectation > quotaSalle) {
    JOptionPane.showMessageDialog(this, String.format(
        "Erreur: La salle a atteint son quota hebdomadaire d'affectation (%d heures max).",
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
             jPanel1.setVisible(false);
            loadAffectations(); // recharge le tableau
        }

    } catch (DateTimeParseException ex) {
        JOptionPane.showMessageDialog(this, "Format d'heure invalide (HH:mm:ss attendu)");
    } catch (NumberFormatException ex) {
        JOptionPane.showMessageDialog(this, "Erreur dans les champs numériques");
    } catch (SQLException ex) {
        JOptionPane.showMessageDialog(this, "Erreur SQL : " + ex.getMessage());
    }

    }                                     

    private void btneditActionPerformed(java.awt.event.ActionEvent evt) {                                        
        // TODO add your handling code here:
    }                                       

    private void jComboBox1ActionPerformed(java.awt.event.ActionEvent evt) {                                           
          String selectedjour = (String) jComboBox1.getSelectedItem();

    if (null == selectedjour) 
        System.out.println("Unknown day selected");
 
    }                                          

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(gerAffectation.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(gerAffectation.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(gerAffectation.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(gerAffectation.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new gerAffectation().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify                     
    private javax.swing.JButton btnadd;
    private javax.swing.JButton btndelete;
    private javax.swing.JButton btndisconnect;
    private javax.swing.JButton btnedit;
    private javax.swing.JButton btnexit;
    private javax.swing.JButton btnok;
    private javax.swing.JButton btnretour;
    private javax.swing.JComboBox<String> jComboBox1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JTextField jTextField3;
    private javax.swing.JTextField jTextField5;
    private javax.swing.JTextField jTextField6;
    // End of variables declaration                   
}
