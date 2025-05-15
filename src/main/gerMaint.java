package main;

import java.awt.Dimension;
import java.awt.Font;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

public class gerMaint extends javax.swing.JFrame {
    private String currentCin;

    public gerMaint(String Cin) {
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        this.currentCin = Cin;
        initComponents();
        loadMaintenance();
        JTableHeader header = jTable1.getTableHeader();
        header.setPreferredSize(new Dimension(header.getWidth(), 50)); // Set header height (e.g., 50px)
        header.setFont(new Font("Segoe UI", Font.BOLD, 24)); // Larger font
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
        btndelete.addActionListener(e -> deleteMaintenance());}
private Connection connectToDatabase() throws SQLException {
        return DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/salle_tp", "root", "123456");
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
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        btndelete = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        jTextField1 = new javax.swing.JTextField();
        btnok = new javax.swing.JButton();
        jComboBox1 = new javax.swing.JComboBox<>();
        exitButton1 = new javax.swing.JButton();
        goBackButton = new javax.swing.JButton();
        deconnecterButton = new javax.swing.JButton();
        background = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setUndecorated(true);
        setResizable(false);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jTable1.setFont(new java.awt.Font("Segoe UI", 0, 36)); // NOI18N
        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null}
            },
            new String [] {
                "Id Maintenance", "Jour", "Id Salle"
            }
        ));
        jTable1.setRowHeight(90);
        jTable1.setShowHorizontalLines(true);
        jTable1.setShowVerticalLines(true);
        jTable1.getTableHeader().setReorderingAllowed(false);
        jScrollPane1.setViewportView(jTable1);

        getContentPane().add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(490, 140, 710, 430));

        btndelete.setBorderPainted(false);
        btndelete.setContentAreaFilled(false);
        btndelete.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        getContentPane().add(btndelete, new org.netbeans.lib.awtextra.AbsoluteConstraints(1010, 580, 190, 70));

        jPanel1.setOpaque(false);
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jTextField1.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        jPanel1.add(jTextField1, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 170, 280, 50));

        btnok.setBorderPainted(false);
        btnok.setContentAreaFilled(false);
        btnok.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnok.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnokActionPerformed(evt);
            }
        });
        jPanel1.add(btnok, new org.netbeans.lib.awtextra.AbsoluteConstraints(242, 343, 140, 60));

        jComboBox1.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "lundi", "mardi", "mercredi", "jeudi","vendredi","samedi" }));
        jComboBox1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox1ActionPerformed(evt);
            }
        });
        jPanel1.add(jComboBox1, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 270, 280, 50));

        getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 120, 410, 420));

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
        getContentPane().add(deconnecterButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(1040, 700, 200, 80));

        background.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/gerMaint.jpg"))); // NOI18N
        getContentPane().add(background, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1280, 800));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnokActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnokActionPerformed
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
    }//GEN-LAST:event_btnokActionPerformed

    private void jComboBox1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox1ActionPerformed
   String selectedjour = (String) jComboBox1.getSelectedItem();

    if (null == selectedjour) 
        System.out.println("Unknown day selected");
         // TODO add your handling code here:
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
    private javax.swing.JLabel background;
    private javax.swing.JButton btndelete;
    private javax.swing.JButton btnok;
    private javax.swing.JButton deconnecterButton;
    private javax.swing.JButton exitButton1;
    private javax.swing.JButton goBackButton;
    private javax.swing.JComboBox<String> jComboBox1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JTextField jTextField1;
    // End of variables declaration//GEN-END:variables
}
