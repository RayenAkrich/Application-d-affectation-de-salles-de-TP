package main;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.JOptionPane;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;

public class gerMaint extends javax.swing.JFrame {
    private String cin;

    public gerMaint() {
        this.cin=cin;
        initComponents();
         loadMaintenance();
        jPanel1.setVisible(false);
        // Ajouter le listener pour détecter les modifications
        jTable1.getModel().addTableModelListener(new TableModelListener() {
            public void tableChanged(TableModelEvent e) {
                int row = e.getFirstRow();
                int column = e.getColumn();
                
                // Détecter toute modification
                if (row != -1 && column != -1) {
                    Object newValue = jTable1.getValueAt(row, column);  // Nouvelle valeur dans la cellule modifiée
                    String idMaintenance= jTable1.getValueAt(row, 0).toString();  // ID de l'affectation (colonne 0)

                    // Appeler la méthode pour mettre à jour la base de données
                    updateMaintenance(idMaintenance, column, newValue.toString());
                }
            }
        });
        btnedit.addActionListener(e -> editMaintenance());
        btndelete.addActionListener(e -> deleteMaintenance());}
private Connection connectToDatabase() throws SQLException {
        return DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/salle_tp", "root", "hendsql131004");
    }
private void editMaintenance() {
    int selectedRow = jTable1.getSelectedRow();
    if (selectedRow == -1) {
        JOptionPane.showMessageDialog(this, "Sélectionnez une ligne !");
        return;
    }

    String idMaintenance = jTable1.getValueAt(selectedRow, 0).toString();
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
        updateMaintenance(idMaintenance, column, newValue);
    }
}
private void updateMaintenance(String idMaintenance, int column, String newValue) {
    try (Connection con = connectToDatabase()) {
        // 1. Récupérer les valeurs actuelles
        String currentSql = "SELECT * FROM maintenance WHERE id_maint = ?";
        PreparedStatement currentPst = con.prepareStatement(currentSql);
        currentPst.setString(1, idMaintenance);
        ResultSet rs = currentPst.executeQuery();

        if (!rs.next()) {
            JOptionPane.showMessageDialog(this, "Maintenance introuvable", "Erreur", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String jour = rs.getString("jour");
        String numSalle = rs.getString("num_salle");

        // Mettre à jour la valeur modifiée
        switch (column) {
            case 1: jour = newValue; break;
            case 2: numSalle = newValue; break;
            default: throw new SQLException("Colonne invalide");
        }

        // 2. Vérifier qu'il n'y a pas d'affectation ce jour-là dans cette salle
        String checkAffectation = "SELECT COUNT(*) FROM affectation WHERE id_salle = ? AND jour = ?";
        try (PreparedStatement pst = con.prepareStatement(checkAffectation)) {
            pst.setString(1, numSalle);
            pst.setString(2, jour);
            ResultSet rsAffect = pst.executeQuery();
            if (rsAffect.next() && rsAffect.getInt(1) > 0) {
                JOptionPane.showMessageDialog(this, "Erreur : Il existe déjà une affectation pour cette salle ce jour-là.");
                return;
            }
        }

        // 3. Vérifier unicité dans maintenance (sauf la ligne actuelle)
        String checkUnique = "SELECT COUNT(*) FROM maintenance WHERE num_salle = ? AND jour = ? AND id_maint != ?";
        try (PreparedStatement pst = con.prepareStatement(checkUnique)) {
            pst.setString(1, numSalle);
            pst.setString(2, jour);
            pst.setString(3, idMaintenance);
            ResultSet rsUnique = pst.executeQuery();
            if (rsUnique.next() && rsUnique.getInt(1) > 0) {
                JOptionPane.showMessageDialog(this, "Erreur : Cette salle est déjà déclarée en maintenance ce jour-là.");
                return;
            }
        }
        //verifier salle dans la base
           String checkSalle = "SELECT COUNT(*) FROM salle WHERE id_salle = ?";
try (PreparedStatement pstSalle = con.prepareStatement(checkSalle)) {
    pstSalle.setString(1, numSalle);
    try (ResultSet rsSalle = pstSalle.executeQuery()) {
        rsSalle.next();
        if (rsSalle.getInt(1) == 0) {
            JOptionPane.showMessageDialog(this, "Erreur : La salle avec l'ID spécifié n'existe pas.");
            return;
        }
    }
}
        // 4. Effectuer la mise à jour
        String updateSql = "";
        switch (column) {
            case 1: updateSql = "UPDATE maintenance SET jour = ? WHERE id_maint = ?"; break;
            case 2: updateSql = "UPDATE maintenance SET num_salle = ? WHERE id_maint = ?"; break;
        }

        try (PreparedStatement pst = con.prepareStatement(updateSql)) {
            pst.setString(1, newValue);
            pst.setString(2, idMaintenance);
            pst.executeUpdate();
            loadMaintenance(); // Recharge ton JTable maintenance si tu en as un
            JOptionPane.showMessageDialog(this, "Maintenance mise à jour avec succès !");
        }
    } catch (SQLException ex) {
        JOptionPane.showMessageDialog(this, "Erreur SQL : " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
    }
}
private void loadMaintenance() {
        try (Connection con = connectToDatabase()) {
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM maintenance");

            DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
            model.setRowCount(0); // Vider d'abord le tableau

            while (rs.next()) {
                Object[] row = {
                    rs.getInt("id_maint"),
                    rs.getString("jour"),
                    rs.getInt("num_salle"),
                };
                model.addRow(row);
            }

            rs.close();
            st.close();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erreur lors du chargement des maintenances : " + e.getMessage());
        }
    }

    
    private void deleteMaintenance() {
        int selected = jTable1.getSelectedRow();
        if (selected == -1) {
            JOptionPane.showMessageDialog(this, "Sélectionnez une ligne !");
            return;
        }
        String idMaintenance = jTable1.getValueAt(selected, 0).toString();

        try (Connection con = connectToDatabase()) {
            PreparedStatement pst = con.prepareStatement("DELETE FROM maintenance WHERE id_maint = ?");
            pst.setString(1, idMaintenance);
            pst.executeUpdate();
            loadMaintenance();  // Recharger les affectations après la suppression
              JOptionPane.showMessageDialog(this, "Affectation supprimée avec succès !");
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Erreur lors de la suppression : " + ex.getMessage());
        }
    }
    


    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">                          
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        btnadd = new javax.swing.JButton();
        btnedit = new javax.swing.JButton();
        btndelete = new javax.swing.JButton();
        btnexit = new javax.swing.JButton();
        bndisconnect = new javax.swing.JButton();
        btnretour = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        jTextField1 = new javax.swing.JTextField();
        btnok = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jComboBox1 = new javax.swing.JComboBox<>();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null}
            },
            new String [] {
                "id_maint", "jour", "num_salle"
            }
        ));
        jScrollPane1.setViewportView(jTable1);

        btnadd.setText("add");
        btnadd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnaddActionPerformed(evt);
            }
        });

        btnedit.setText("modify");

        btndelete.setText("delete");

        btnexit.setText("exit");
        btnexit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnexitActionPerformed(evt);
            }
        });

        bndisconnect.setText("disconnect");
        bndisconnect.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bndisconnectActionPerformed(evt);
            }
        });

        btnretour.setText("retour");
        btnretour.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnretourActionPerformed(evt);
            }
        });

        jTextField1.setText("jTextField1");

        btnok.setText("ok");
        btnok.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnokActionPerformed(evt);
            }
        });

        jLabel1.setText("jour");

        jLabel2.setText("num_salle");

        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "lundi", "mardi", "mercredi", "jeudi","vendredi","samedi" }));
        jComboBox1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGap(26, 26, 26)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 69, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 28, Short.MAX_VALUE)
                        .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 103, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnok)
                        .addGap(31, 31, 31))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(28, 28, 28)
                        .addComponent(jComboBox1, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addGap(25, 25, 25))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(61, 61, 61)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(28, 28, 28)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(btnok)
                .addContainerGap(63, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(86, 86, 86)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(btnexit)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(37, 37, 37)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 311, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(18, 18, 18)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(btndelete)
                                    .addComponent(btnedit)
                                    .addComponent(btnadd)))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(43, 43, 43)
                                .addComponent(btnretour)
                                .addGap(26, 26, 26)
                                .addComponent(bndisconnect)))))
                .addContainerGap(139, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(19, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(26, 26, 26))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 279, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap())))
            .addGroup(layout.createSequentialGroup()
                .addGap(52, 52, 52)
                .addComponent(btnadd)
                .addGap(29, 29, 29)
                .addComponent(btnedit)
                .addGap(34, 34, 34)
                .addComponent(btndelete)
                .addGap(19, 19, 19)
                .addComponent(btnexit)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnretour)
                    .addComponent(bndisconnect))
                .addGap(24, 24, 24))
        );

        pack();
    }// </editor-fold>                        

    private void btnaddActionPerformed(java.awt.event.ActionEvent evt) {                                       
    jPanel1.setVisible(true); // Affiche le formulaire
    jTable1.clearSelection(); // DÃ©sÃ©lectionne toute ligne
    jComboBox1.setSelectedItem("");
    jTextField1.setText("");
     btnadd.setEnabled(false);
    btnedit.setEnabled(false);
    btndelete.setEnabled(false);
    
    
    
        // TODO add your handling code here:
    }                                      

    private void btnexitActionPerformed(java.awt.event.ActionEvent evt) {                                        
    System.exit(0);        
    }                                       

    private void bndisconnectActionPerformed(java.awt.event.ActionEvent evt) {                                             
 this.dispose();
        new Sign_in().setVisible(true);        // TODO add your handling code here:
    }                                            

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
    }        // TODO add your handling code here:        // TODO add your handling code here:
    }                                         

    private void btnokActionPerformed(java.awt.event.ActionEvent evt) {                                      
      String jour = (String) jComboBox1.getSelectedItem();
    int numSalle;

    try {
        numSalle = Integer.parseInt(jTextField1.getText());
    } catch (NumberFormatException ex) {
        JOptionPane.showMessageDialog(this, "Erreur : Le numéro de salle doit être un entier.", "Erreur", JOptionPane.ERROR_MESSAGE);
        return;
    }

    try (Connection con = connectToDatabase()) {

        // 1. Vérifier que la salle existe
        String checkSalle = "SELECT COUNT(*) FROM salle WHERE id_salle = ?";
        try (PreparedStatement pstSalle = con.prepareStatement(checkSalle)) {
            pstSalle.setInt(1, numSalle);
            try (ResultSet rsSalle = pstSalle.executeQuery()) {
                rsSalle.next();
                if (rsSalle.getInt(1) == 0) {
                    JOptionPane.showMessageDialog(this, "Erreur : La salle avec l'ID spécifié n'existe pas.");
                    return;
                }
            }
        }

        // 2. Vérifier qu'il n'y a pas d'affectation ce jour-là dans cette salle
        String checkAffectation = "SELECT COUNT(*) FROM affectation WHERE id_salle = ? AND jour = ?";
        try (PreparedStatement pst = con.prepareStatement(checkAffectation)) {
            pst.setInt(1, numSalle);
            pst.setString(2, jour);
            ResultSet rsAffect = pst.executeQuery();
            if (rsAffect.next() && rsAffect.getInt(1) > 0) {
                JOptionPane.showMessageDialog(this, "Erreur : Il existe déjà une affectation pour cette salle ce jour-là.");
                return;
            }
        }

        // 3. Vérifier unicité dans maintenance
        String checkUnique = "SELECT COUNT(*) FROM maintenance WHERE num_salle = ? AND jour = ?";
        try (PreparedStatement pst = con.prepareStatement(checkUnique)) {
            pst.setInt(1, numSalle);
            pst.setString(2, jour);
            ResultSet rsUnique = pst.executeQuery();
            if (rsUnique.next() && rsUnique.getInt(1) > 0) {
                JOptionPane.showMessageDialog(this, "Erreur : Cette salle est déjà déclarée en maintenance ce jour-là.");
                return;
            }
        }

        // 4. Insertion dans la table maintenance
        String insertSql = "INSERT INTO maintenance (num_salle, jour) VALUES (?, ?)";
        try (PreparedStatement pst = con.prepareStatement(insertSql)) {
            pst.setInt(1, numSalle);
            pst.setString(2, jour);
            pst.executeUpdate();
            JOptionPane.showMessageDialog(this, "Maintenance ajoutée avec succès !");
            jPanel1.setVisible(false);
            loadMaintenance(); // Recharge le tableau des maintenances
        }

    } catch (SQLException ex) {
        JOptionPane.showMessageDialog(this, "Erreur SQL : " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
    }
    }                                     

    private void jComboBox1ActionPerformed(java.awt.event.ActionEvent evt) {                                           
   String selectedjour = (String) jComboBox1.getSelectedItem();

    if (null == selectedjour) 
        System.out.println("Unknown day selected");
         // TODO add your handling code here:
    }                                          

    public static void main(String args[]) {
        
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new gerMaint().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify                     
    private javax.swing.JButton bndisconnect;
    private javax.swing.JButton btnadd;
    private javax.swing.JButton btndelete;
    private javax.swing.JButton btnedit;
    private javax.swing.JButton btnexit;
    private javax.swing.JButton btnok;
    private javax.swing.JButton btnretour;
    private javax.swing.JComboBox<String> jComboBox1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JTextField jTextField1;
    // End of variables declaration                   
}
