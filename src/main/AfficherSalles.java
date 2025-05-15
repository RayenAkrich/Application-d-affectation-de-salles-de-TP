package main;
import java.awt.Dimension;
import java.awt.Font;
import java.sql.SQLException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.sql.Time;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import java.sql.CallableStatement;
import javax.swing.JFrame;
import javax.swing.table.JTableHeader;


public class AfficherSalles extends javax.swing.JFrame {
    
    Connection con;
    Statement stm;
    ResultSet rs;
    PreparedStatement pst;
    private String currentCin;
    
    public void connect() {
        try {
            con = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/salle_tp", "root", "123456");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "impoo");
        }
    }
    
    void verifierDisponibiliteSalles() {
    // 1. Récupération des valeurs depuis l'interface
    String nbEtudiantsStr = nbEtud.getText(); // "Entrer nbr Etudiant(opt)"
    String dateStr = Jour.getSelectedItem() != null ? Jour.getSelectedItem().toString() : ""; // "Entrez Date(Opt)"
    String heureDebStr = HeureDeb.getText(); // "Heure deb"
    String heureFinStr = HeureFin.getText(); // "Heure fin"
    System.out.println(heureDebStr);
    System.out.println("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa");
    System.out.println(heureFinStr);

    // Initialisation des variables heure
    LocalTime heureDeb = null;
    LocalTime heureFin = null;

    // 3. Traitement des heures si fournies
    if (!heureDebStr.isEmpty() && !heureFinStr.isEmpty()) {
        try {
            // Validation format avec regex plus précise
            
            String timeRegex = "^(0?[0-9]|1[0-9]|2[0-3]):([0-5][0-9])$";
            if (!heureDebStr.matches(timeRegex) || !heureFinStr.matches(timeRegex)) {
                throw new DateTimeParseException("Format invalide", heureDebStr, 0);
            }
            if (dateStr.isEmpty()){
                showError("Veuillez sélectionner un jour !");
            }
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("H:mm");
            heureDeb = LocalTime.parse(heureDebStr, formatter);
            heureFin = LocalTime.parse(heureFinStr, formatter);

            if (!heureDeb.isBefore(heureFin)) {
                showError("L'heure de début doit être avant l'heure de fin !");
                return;
            }
        } catch (DateTimeParseException e) {
            showError("Format d'heure invalide !\nUtilisez HH:MM (ex: 09:00 ou 9:00)");
            return;
        }
    }
    

    try {
        // 3. Conversion des valeurs
        int nbEtudiants = nbEtudiantsStr.isEmpty() ? 0 : Integer.parseInt(nbEtudiantsStr);
        if (nbEtudiants<=0){
            showError("Veuillez entrer un nombre d'étudiants valide !");
        }
        // 4. Requête SQL adaptée
        String query = "SELECT id_salle, nom, capacite, nbr_max_aff " +
                      "FROM salle " +
                      "WHERE (? = 0 OR capacite >= ?) " + // Filtre capacité si renseigné
                      "AND id_salle NOT IN (" +
                      "   SELECT id_salle FROM affectation " +
                      "   WHERE jour = ? " +
                      "   AND ((? >= heure_deb ) " +
                      "   AND (? <= heure_fin) " +
                      "   OR (heure_deb BETWEEN ? AND ?)))";

        // 5. Exécution
        connect();
        PreparedStatement pst = con.prepareStatement(query);
        pst.setInt(1, nbEtudiants);
        pst.setInt(2, nbEtudiants);
        pst.setString(3, dateStr);
        pst.setTime(4, Time.valueOf(heureDeb));
        pst.setTime(5, Time.valueOf(heureFin));
        pst.setTime(6, Time.valueOf(heureDeb));
        pst.setTime(7, Time.valueOf(heureFin));

        ResultSet rs = pst.executeQuery();

        // 6. Affichage dans le tableau existant
        DefaultTableModel model = (DefaultTableModel) tableSalle.getModel();
        model.setRowCount(0); // Vide le tableau

        while (rs.next()) {
            model.addRow(new Object[]{
                rs.getString("id_salle"),
                rs.getString("nom"),
                rs.getString("capacite"),
                rs.getString("nbr_max_aff"),
                "-" // Colonne Détails
            });
        }

        if (model.getRowCount() == 0) {
            JOptionPane.showMessageDialog(null, "Aucune salle disponible pour ces critères.");
        }

    } catch (NumberFormatException e) {
        JOptionPane.showMessageDialog(null, "Nombre d'étudiants invalide");
    } catch (DateTimeParseException e) {
        JOptionPane.showMessageDialog(null, "Format d'heure invalide (HH:mm)");
    } catch (SQLException e) {
        JOptionPane.showMessageDialog(null, "Erreur base de données: " + e.getMessage());
    } finally {
        try { if (con != null) con.close(); } catch (SQLException e) {}
    }
}
    private void showError(String message) {
    JOptionPane.showMessageDialog(null, message, "Erreur", JOptionPane.ERROR_MESSAGE);
}

    public void Table() {
        try {
            connect();
            String selection = Choix.getSelectedItem().toString();
    String query = "";
    
    // Récupérer le texte saisi dans le JTextField
    String champ = nbEtud.getText().trim(); 
    
    jLabel1.setText("");
    nbEtud.setVisible(false);
    HeureDeb.setVisible(false);
    HeureFin.setVisible(false);
    Jour.setVisible(false);
    jLabel2.setText("");
    jLabel3.setText("");
    jLabel4.setText("");
    if (selection.equals("Toutes les salles")) {
        System.out.println(champ);
        query = "SELECT id_salle, nom, capacite, nbr_max_aff FROM salle";
        nbEtud.setVisible(true);
        HeureFin.setVisible(true);
        HeureDeb.setVisible(true);
        
        Jour.setVisible(true);
        jLabel1.setText("Entrez Date: ");
        jLabel2.setText("Heure deb:");
        jLabel4.setText("Heure fin:");
        jLabel3.setText("Entrer nbr Etudiant:");
        
        boolean auMoinsUnChampRempli = nbEtud.getText().isEmpty() 
                                || HeureDeb.getText().isEmpty() 
                                || HeureFin.getText().isEmpty() 
                                || Jour.getSelectedItem() == null;
        System.out.println(auMoinsUnChampRempli);
        if (!auMoinsUnChampRempli) {
            System.out.println("Au moins un champ rempli - vérification disponibilité:"+nbEtud.getText()+HeureDeb.getText()+HeureFin.getText()+Jour.getSelectedItem());
            verifierDisponibiliteSalles();
        return;
}
    }else if (selection.equals("Salles par enseignant")) {
        nbEtud.setVisible(true);
        nbEtud.setText("");
        jLabel3.setText("Entrer le nom de l'enseignant");
        

        // Réinitialiser le message d'erreur
        
        try {
            
            
            query = "SELECT s.id_salle, s.nom, s.capacite, s.nbr_max_aff, e.nom AS Enseignant "
              + "FROM salle s "
              + "JOIN affectation a ON s.id_salle = a.id_salle "
              + "JOIN enseignant e ON a.id_ens = cin AND e.nom LIKE '%" + champ + "%'";
        } catch (Exception e) {
            showError("Veuillez entrer un enseignant existant.");
            return;
        }
        
        


        // Ajouter la condition de recherche par étudiant si un nom est saisi   
    } else if (selection.equals("Jours libres par salle")) {
        nbEtud.setVisible(true);
        nbEtud.setText("");
        jLabel3.setText("Entrer l' ID de la salle");
        // Réinitialiser le message d'erreur
        int a=1;
        if(!champ.isEmpty()){
        try {
            int idSalle = Integer.parseInt(champ); // Vérifier si c'est un nombre
            query = "SELECT s.id_salle, s.nom, s.capacite,s.nbr_max_aff, j.jour "
              + "FROM salle s "
              + "CROSS JOIN (SELECT 'lundi' AS jour UNION SELECT 'mardi' UNION SELECT 'mercredi' "
              + "UNION SELECT 'jeudi' UNION SELECT 'vendredi' UNION SELECT 'samedi' UNION SELECT 'dimanche') j "
              + "LEFT JOIN affectation a ON a.id_salle= s.id_salle "+" AND j.jour = a.jour "
              + "WHERE a.id_salle IS NULL AND s.id_salle=" + idSalle;
        } catch (NumberFormatException e) {
            showError("Veuillez entrer un ID de salle valide !");
            return;
        }}else{
             query = "SELECT s.id_salle, s.nom, s.capacite,s.nbr_max_aff, j.jour "
              + "FROM salle s "
              + "CROSS JOIN (SELECT 'lundi' AS jour UNION SELECT 'mardi' UNION SELECT 'mercredi' "
              + "UNION SELECT 'jeudi' UNION SELECT 'vendredi' UNION SELECT 'samedi' UNION SELECT 'dimanche') j ";
             a=0;
        }
        

    }else if (selection.equals("Salle la plus affectée")) {
        
        nbEtud.setText("");
           query = "SELECT s.id_salle, s.nom, s.capacite,s.nbr_max_aff, COUNT(a.id_affect) AS Nbr_Affectations "
                + "FROM salle s "
                + "LEFT JOIN affectation a ON s.id_salle = a.id_salle "
                + "GROUP BY s.id_salle "
                + "ORDER BY Nbr_Affectations DESC LIMIT 1";
    }
    String dateStr = Jour.getSelectedItem() != null ? Jour.getSelectedItem().toString().trim() : "";

    
    if(selection.equals("Toutes les salles")&&(!HeureDeb.getText().isEmpty() || !HeureFin.getText().isEmpty()||!nbEtud.getText().isEmpty() || !dateStr.isEmpty())){
        showError("veuillez completez les autres champs");
        return;
    }
   
    String[] columns = {"ID Salle", "Nom", "Capacité","Nbr aff max", "Détails"};
    DefaultTableModel model = new DefaultTableModel(null, columns);

    Statement sta = con.createStatement();
    rs = sta.executeQuery(query);
    if (!rs.isBeforeFirst() && selection.equals("Salles par enseignant")) { // Vérifie si le ResultSet est vide
            showError("Veuillez entrer un enseignant existant !");  
            return;}
    if (!rs.isBeforeFirst() && selection.equals("Jours libres par salle")) { // Vérifie si le ResultSet est vide
               JOptionPane.showMessageDialog(null,"cette ID est inexistant ");
            return;}
    while (rs.next()) {
        String[] row = new String[5];
        row[0] = rs.getString("id_salle");
        row[1] = rs.getString("nom");
        row[2] = rs.getString("capacite");
        row[3] =rs.getString("nbr_max_aff");
        if (selection.equals("Salles par enseignant")) {
            row[4] = rs.getString("Enseignant");
        } else if (selection.equals("Jours libres par salle")) {
            row[4] = rs.getString("jour");
            
        } else if (selection.equals("Salle la plus affectée")) {
            row[4] = rs.getString("Nbr_Affectations");
        } else {
            row[4] = "-";
        }
        model.addRow(row);
    }
    tableSalle.setModel(model);
            con.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Erreurr : " + e.getMessage());
        }
    }

    public AfficherSalles(String cin) {
        initComponents();
        Table();
        this.currentCin = cin;
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        JTableHeader header = tableSalle.getTableHeader();
        header.setPreferredSize(new Dimension(header.getWidth(), 50)); // Set header height (e.g., 50px)
        header.setFont(new Font("Segoe UI", Font.BOLD, 24)); // Larger font
    }
    


    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        tableSalle = new javax.swing.JTable();
        Choix = new javax.swing.JComboBox<>();
        nbEtud = new javax.swing.JTextField();
        ConfirmButton = new javax.swing.JButton();
        Jour = new javax.swing.JComboBox<>();
        jLabel1 = new javax.swing.JLabel();
        HeureDeb = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        HeureFin = new javax.swing.JTextField();
        exitButton = new javax.swing.JButton();
        goBackButton = new javax.swing.JButton();
        deconnecterButton = new javax.swing.JButton();
        background = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setUndecorated(true);
        setResizable(false);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        tableSalle.setFont(new java.awt.Font("Segoe UI", 0, 36)); // NOI18N
        tableSalle.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        tableSalle.setEditingColumn(0);
        tableSalle.setEditingRow(0);
        tableSalle.setEnabled(false);
        tableSalle.setFocusable(false);
        tableSalle.setRowHeight(90);
        tableSalle.setShowHorizontalLines(true);
        tableSalle.setShowVerticalLines(true);
        tableSalle.getTableHeader().setReorderingAllowed(false);
        jScrollPane1.setViewportView(tableSalle);

        getContentPane().add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(400, 140, 790, 530));

        Choix.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        Choix.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Toutes les salles", "Salles par enseignant", "Jours libres par salle", "Salle la plus affectée" }));
        Choix.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ChoixActionPerformed(evt);
            }
        });
        getContentPane().add(Choix, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 230, 280, 70));

        nbEtud.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        nbEtud.setToolTipText("");
        nbEtud.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                nbEtudActionPerformed(evt);
            }
        });
        getContentPane().add(nbEtud, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 360, 170, 50));

        ConfirmButton.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        ConfirmButton.setForeground(new java.awt.Color(255, 255, 255));
        ConfirmButton.setBorderPainted(false);
        ConfirmButton.setContentAreaFilled(false);
        ConfirmButton.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        ConfirmButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ConfirmButtonActionPerformed(evt);
            }
        });
        getContentPane().add(ConfirmButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(46, 150, 140, 70));

        Jour.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        Jour.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { " ", "lundi", "mardi", "mercredi", "jeudi", "vendredi", "samedi ", "dimanche" }));
        Jour.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                JourActionPerformed(evt);
            }
        });
        getContentPane().add(Jour, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 440, 170, 60));

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 20)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("Jour:");
        getContentPane().add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 410, 127, 30));

        HeureDeb.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        HeureDeb.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                HeureDebActionPerformed(evt);
            }
        });
        getContentPane().add(HeureDeb, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 540, 170, 50));

        jLabel3.setFont(new java.awt.Font("Segoe UI", 1, 20)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("Nombre d'étudiants:");
        getContentPane().add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 320, 320, 40));

        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 20)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("Heure Début:");
        getContentPane().add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 510, 170, 30));

        jLabel4.setFont(new java.awt.Font("Segoe UI", 1, 20)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setText("Heure Fin:");
        getContentPane().add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 600, 120, 30));

        HeureFin.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        HeureFin.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                HeureFinActionPerformed(evt);
            }
        });
        getContentPane().add(HeureFin, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 630, 170, 50));

        exitButton.setForeground(new java.awt.Color(255, 255, 255));
        exitButton.setBorder(null);
        exitButton.setContentAreaFilled(false);
        exitButton.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        exitButton.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        exitButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        exitButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                exitButtonActionPerformed(evt);
            }
        });
        getContentPane().add(exitButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 720, 160, 60));

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
        getContentPane().add(deconnecterButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(1030, 700, 220, 90));

        background.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/AfficherSalles.jpg"))); // NOI18N
        getContentPane().add(background, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1280, 800));

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void ConfirmButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ConfirmButtonActionPerformed
        Choix.setSelectedItem(Choix.getSelectedItem());
        
        
    }//GEN-LAST:event_ConfirmButtonActionPerformed

    private void ChoixActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ChoixActionPerformed
        Table();
    }//GEN-LAST:event_ChoixActionPerformed

    private void JourActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_JourActionPerformed
  
    }//GEN-LAST:event_JourActionPerformed

    private void HeureDebActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_HeureDebActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_HeureDebActionPerformed

    private void HeureFinActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_HeureFinActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_HeureFinActionPerformed

    private void nbEtudActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_nbEtudActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_nbEtudActionPerformed

    private void exitButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_exitButtonActionPerformed
        System.exit(0);
    }//GEN-LAST:event_exitButtonActionPerformed

    private void goBackButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_goBackButtonActionPerformed
        new user_interface(currentCin).setVisible(true);
        dispose();
    }//GEN-LAST:event_goBackButtonActionPerformed

    private void deconnecterButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deconnecterButtonActionPerformed
        new Sign_in().setVisible(true);
        dispose();
    }//GEN-LAST:event_deconnecterButtonActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox<String> Choix;
    private javax.swing.JButton ConfirmButton;
    private javax.swing.JTextField HeureDeb;
    private javax.swing.JTextField HeureFin;
    private javax.swing.JComboBox<String> Jour;
    private javax.swing.JLabel background;
    private javax.swing.JButton deconnecterButton;
    private javax.swing.JButton exitButton;
    private javax.swing.JButton goBackButton;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextField nbEtud;
    private javax.swing.JTable tableSalle;
    // End of variables declaration//GEN-END:variables
}
