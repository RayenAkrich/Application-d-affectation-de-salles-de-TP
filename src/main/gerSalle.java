package main;
import java.awt.Dimension;
import java.awt.Font;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
public class gerSalle extends javax.swing.JFrame {
    private String currentCin;
    Connection con;
    Statement stm;
    ResultSet rs;
    PreparedStatement pst;
    public void connect() {
        try {
            con = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/salle_tp", "root", "123456");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "impoo");
        }
    }
    public gerSalle(String cin) {
        this.currentCin=cin;
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        initComponents();
        loadEnseignants();
        // Configure table header
        JTableHeader header = jTable2.getTableHeader();
        header.setPreferredSize(new Dimension(header.getWidth(), 50)); // Set header height (e.g., 50px)
        header.setFont(new Font("Segoe UI", Font.BOLD, 24)); // Larger font
        
        jTable2.getSelectionModel().addListSelectionListener(e -> {
        if (!e.getValueIsAdjusting() && jTable2.getSelectedRow() != -1) {
            supp.setEnabled(true); // Supprimer
            edit.setEnabled(true); // Modifier
            id1.setEditable(false);
            int selectedRow = jTable2.getSelectedRow();
            id1.setText(jTable2.getValueAt(selectedRow, 0).toString());
            nom1.setText(jTable2.getValueAt(selectedRow, 1).toString());
            capacite1.setText(jTable2.getValueAt(selectedRow, 2).toString());
            nb_max_aff.setText(jTable2.getValueAt(selectedRow, 3).toString());
            
        }
});
    }
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        lblErreur = new javax.swing.JLabel();
        add = new javax.swing.JButton();
        edit = new javax.swing.JButton();
        id1 = new javax.swing.JTextField();
        nom1 = new javax.swing.JTextField();
        capacite1 = new javax.swing.JTextField();
        nb_max_aff = new javax.swing.JTextField();
        resetButton = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable2 = new javax.swing.JTable();
        supp = new javax.swing.JButton();
        exitButton1 = new javax.swing.JButton();
        goBackButton = new javax.swing.JButton();
        deconnecterButton = new javax.swing.JButton();
        Background = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setBackground(new java.awt.Color(204, 204, 204));
        setForeground(new java.awt.Color(204, 204, 204));
        setUndecorated(true);
        setResizable(false);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        lblErreur.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        lblErreur.setForeground(new java.awt.Color(255, 255, 255));
        getContentPane().add(lblErreur, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 580, 390, 100));

        add.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        add.setForeground(java.awt.Color.white);
        add.setBorderPainted(false);
        add.setContentAreaFilled(false);
        add.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        add.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addActionPerformed(evt);
            }
        });
        getContentPane().add(add, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 180, 140, 70));

        edit.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        edit.setForeground(java.awt.Color.white);
        edit.setBorderPainted(false);
        edit.setContentAreaFilled(false);
        edit.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        edit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                editActionPerformed(evt);
            }
        });
        getContentPane().add(edit, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 180, 140, 70));
        getContentPane().add(id1, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 280, 210, 30));
        getContentPane().add(nom1, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 340, 210, 30));
        getContentPane().add(capacite1, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 400, 210, 30));
        getContentPane().add(nb_max_aff, new org.netbeans.lib.awtextra.AbsoluteConstraints(330, 460, 80, 30));

        resetButton.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        resetButton.setForeground(new java.awt.Color(255, 255, 255));
        resetButton.setToolTipText("");
        resetButton.setBorderPainted(false);
        resetButton.setContentAreaFilled(false);
        resetButton.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        resetButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        resetButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                resetButtonActionPerformed(evt);
            }
        });
        getContentPane().add(resetButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(1000, 580, 190, 60));

        jTable2.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        jTable2.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        jTable2.setRowHeight(90);
        jTable2.setShowHorizontalLines(true);
        jTable2.setShowVerticalLines(true);
        jTable2.getTableHeader().setReorderingAllowed(false);
        jTable2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                jTable2MouseReleased(evt);
            }
        });
        jScrollPane1.setViewportView(jTable2);

        getContentPane().add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(480, 140, 720, 430));

        supp.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        supp.setForeground(new java.awt.Color(255, 255, 255));
        supp.setBorderPainted(false);
        supp.setContentAreaFilled(false);
        supp.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        supp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                suppActionPerformed(evt);
            }
        });
        getContentPane().add(supp, new org.netbeans.lib.awtextra.AbsoluteConstraints(780, 580, 170, 60));

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

        Background.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/gerSalles.jpg"))); // NOI18N
        getContentPane().add(Background, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1280, 800));

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    
    private void loadEnseignants() {
        String query = "";
    try{
        connect();
        query = "SELECT * FROM salle";
    String[] columns = {"ID Salle", "Nom", "Capacité","Nbr aff max"};
    DefaultTableModel model = new DefaultTableModel(null, columns);

    Statement sta = con.createStatement();
    rs = sta.executeQuery(query);
    
    while (rs.next()) {
        String[] row = new String[4];
        row[0] = rs.getString("id_salle");
        row[1] = rs.getString("nom");
        row[2] = rs.getString("capacite");
        row[3] =rs.getString("nbr_max_aff");
        
        model.addRow(row);
    }
    jTable2.setModel(model);
            con.close();
            
    // Désactive les boutons tant qu'aucune ligne n'est sélectionnée
    supp.setEnabled(false);
    edit.setEnabled(false);}catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Erreur : " + e.getMessage());
        }
    }
    private void suppActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_suppActionPerformed
        // TODO add your handling code here:
        try {
            connect();
            int selec = jTable2.getSelectedRow();
            if (selec != -1) {
                String selle = jTable2.getValueAt(selec, 0).toString();

                int confirm = JOptionPane.showConfirmDialog(null,
                        "Voulez-vous vraiment supprimer cette salle ?", "Confirmation", JOptionPane.YES_NO_OPTION);

                if (confirm == JOptionPane.YES_OPTION) {

                    // Supprimer d'abord les dépendances dans maintenance
                    PreparedStatement pst1 = con.prepareStatement("DELETE FROM affectation WHERE id_salle = ?");
                    pst1.setString(1, selle); // id est l’id_salle que tu veux supprimer
                    pst1.executeUpdate();
// Puis supprimer la salle elle-même
                    PreparedStatement pst2 = con.prepareStatement("DELETE FROM salle WHERE id_salle = ?");
                    pst2.setString(1, selle);
                    int rowsDeleted = pst2.executeUpdate();

                    if (rowsDeleted > 0) {
                        // Supprimer du JTable
                        DefaultTableModel model = (DefaultTableModel) jTable2.getModel();
                        model.removeRow(selec);

                        JOptionPane.showMessageDialog(null, "Salle supprimé avec succès !");
                    } else {
                        JOptionPane.showMessageDialog(null, "Aucune suppression effectuée !");
                    }
                }
            } else {
                JOptionPane.showMessageDialog(null, "Veuillez sélectionner une salle à supprimer.");
            }

            con.close(); // Fermer la connexion
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Erreur lors de la suppression !");
        }
    }//GEN-LAST:event_suppActionPerformed

    private void jTable2MouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable2MouseReleased
        // TODO add your handling code here:
        int i = jTable2.getSelectedRow();
        DefaultTableModel model = (DefaultTableModel) jTable2.getModel();
    }//GEN-LAST:event_jTable2MouseReleased

    private void resetButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_resetButtonActionPerformed
        // TODO add your handling code here:
        id1.setEditable(true);
        id1.setText("");
        nom1.setText("");
        capacite1.setText("");
        nb_max_aff.setText("");
    }//GEN-LAST:event_resetButtonActionPerformed

    private void editActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_editActionPerformed
        // TODO add your handling code here:
        int selectedRow = jTable2.getSelectedRow();

        if (selectedRow != -1) {
            try {

                int id = Integer.parseInt(id1.getText());
                String nom = nom1.getText();
                int capacite = Integer.parseInt(capacite1.getText());
                int specialite = Integer.parseInt(nb_max_aff.getText());
                

                try{
                    connect();
                    String sql = "UPDATE salle SET nom = ?, capacite = ?, nbr_max_aff = ? WHERE id_salle = ?";
                    PreparedStatement pstmt = con.prepareStatement(sql);
                    pstmt.setString(1, nom); 
                    pstmt.setInt(2, capacite); // Should be capacite (integer)
                    pstmt.setInt(3, specialite); // Should be maxAff (integer)
                    pstmt.setInt(4, id);
                    
                    int rowsAffected = pstmt.executeUpdate();

                    if (rowsAffected > 0) {
                        JOptionPane.showMessageDialog(this, "Salle modifiée avec succès !");
                        loadEnseignants(); // recharge le tableau
                    } else {
                        JOptionPane.showMessageDialog(this, "Aucune modification effectuée.");
                    }
                }

                catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this, "Erreur de format. Veuillez vérifier les champs numériques.");
                } }catch (SQLException e) {
                    e.printStackTrace();
                    JOptionPane.showMessageDialog(this, "Erreur lors de la mise à jour.");
                }
            } else {
                JOptionPane.showMessageDialog(this, "Veuillez sélectionner un enseignant à modifier.");
            }
    }//GEN-LAST:event_editActionPerformed

    private void addActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addActionPerformed
        // TODO add your handling code here:
        try {
            int cin = Integer.parseInt(id1.getText());
            String nom = nom1.getText();
            String prenom = capacite1.getText();
            String specialite = nb_max_aff.getText();

            try{
                connect();
                String sql = "INSERT INTO salle VALUES (?, ?, ?, ?)";
                PreparedStatement pstmt = con.prepareStatement(sql);
                pstmt.setInt(1, Integer.parseInt(id1.getText())); // id_salle
                pstmt.setString(2,nom1.getText()); // nom
                pstmt.setInt(3, Integer.parseInt(capacite1.getText())); // capacite
                pstmt.setInt(4, Integer.parseInt(nb_max_aff.getText())); // nbr_max_aff

                pstmt.executeUpdate();
                JOptionPane.showMessageDialog(this, "salle ajouté avec succès !");
                loadEnseignants(); // Recharge le tableau

            } catch (SQLException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Erreur lors de l'ajout.");
            }

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Erreur : Veuillez vérifier les champs numériques.");
        }

    }//GEN-LAST:event_addActionPerformed

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
    private javax.swing.JLabel Background;
    private javax.swing.JButton add;
    private javax.swing.JTextField capacite1;
    private javax.swing.JButton deconnecterButton;
    private javax.swing.JButton edit;
    private javax.swing.JButton exitButton1;
    private javax.swing.JButton goBackButton;
    private javax.swing.JTextField id1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable2;
    private javax.swing.JLabel lblErreur;
    private javax.swing.JTextField nb_max_aff;
    private javax.swing.JTextField nom1;
    private javax.swing.JButton resetButton;
    private javax.swing.JButton supp;
    // End of variables declaration//GEN-END:variables

}