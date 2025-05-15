package main;

import java.awt.Dimension;
import java.awt.Font;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.PreparedStatement;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

public class gerUser extends javax.swing.JFrame {

        private String currentCin;
        Connection conn;
        public void connect() {
            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
                conn = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/salle_tp", "root", "123456");
                System.out.println("Connected to the database.");
            } catch (ClassNotFoundException | SQLException e) {
                System.out.println("Database connection failed: " + e.getMessage());
            }
        }
        
    public gerUser(String Cin) {
        this.currentCin = Cin;
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        initComponents();
        loaduser();
        // Configure table header
        JTableHeader header = jTable1.getTableHeader();
        header.setPreferredSize(new Dimension(header.getWidth(), 50)); // Set header height (e.g., 50px)
        header.setFont(new Font("Segoe UI", Font.BOLD, 24)); // Larger font
        jTable1.getSelectionModel().addListSelectionListener(e -> {
    if (!e.getValueIsAdjusting() && jTable1.getSelectedRow() != -1) {
        supp.setEnabled(true); // Supprimer
        edit.setEnabled(true); // Modifier
        int selectedRow = jTable1.getSelectedRow();
        cin1.setText(jTable1.getValueAt(selectedRow, 0).toString());
        cin1.setEditable(false);
        nom1.setText(jTable1.getValueAt(selectedRow, 1).toString());
        prenom1.setText(jTable1.getValueAt(selectedRow, 2).toString());
        email1.setText(jTable1.getValueAt(selectedRow, 3).toString());
        mdp1.setText(jTable1.getValueAt(selectedRow, 4).toString());
        jComboBox1.setSelectedItem(jTable1.getValueAt(selectedRow, 5).toString());
    }
});
    }
        private void loaduser() {
    DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
    model.setRowCount(0); // Clear table first

    try {
        connect();
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM users");

        while (rs.next()) {
            model.addRow(new Object[]{
                    rs.getString("cin"),
                    rs.getString("nom"),
                    rs.getString("prenom"),
                    rs.getString("email"),
                    rs.getString("mot_de_passe"),
                    rs.getString("role"),
                    rs.getString("date_inscription")
            });
        }
    } catch (SQLException e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(this, "Erreur de chargement des utilisateurs.");
    }

    // Désactive les boutons tant qu'aucune ligne n'est sélectionnée
    supp.setEnabled(false);
    edit.setEnabled(false);
}

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jComboBox1 = new javax.swing.JComboBox<>();
        edit = new javax.swing.JButton();
        cin1 = new javax.swing.JTextField();
        nom1 = new javax.swing.JTextField();
        prenom1 = new javax.swing.JTextField();
        email1 = new javax.swing.JTextField();
        mdp1 = new javax.swing.JTextField();
        add = new javax.swing.JToggleButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        supp = new javax.swing.JButton();
        goBackButton = new javax.swing.JButton();
        deconnecterButton = new javax.swing.JButton();
        exitButton1 = new javax.swing.JButton();
        resetButton = new javax.swing.JButton();
        background = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setUndecorated(true);
        setResizable(false);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel1.setOpaque(false);
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "admin", "user"}));
        jComboBox1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox1ActionPerformed(evt);
            }
        });
        jPanel1.add(jComboBox1, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 460, 220, 40));

        edit.setBorderPainted(false);
        edit.setContentAreaFilled(false);
        edit.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        edit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                editActionPerformed(evt);
            }
        });
        jPanel1.add(edit, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 60, 140, 70));

        cin1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cin1ActionPerformed(evt);
            }
        });
        jPanel1.add(cin1, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 160, 220, 40));
        jPanel1.add(nom1, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 222, 220, 40));
        jPanel1.add(prenom1, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 280, 220, 40));
        jPanel1.add(email1, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 340, 220, 40));
        jPanel1.add(mdp1, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 400, 220, 40));

        add.setBorderPainted(false);
        add.setContentAreaFilled(false);
        add.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        add.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addActionPerformed(evt);
            }
        });
        jPanel1.add(add, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 60, 130, 70));

        getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 120, 400, 540));

        jTable1.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null}
            },
            new String [] {
                "Cin", "Nom", "Prénom", "Email", "MDP", "Role", "Date inscription"
            }
        ));
        jTable1.setRowHeight(90);
        jTable1.setShowHorizontalLines(true);
        jTable1.setShowVerticalLines(true);
        jTable1.getTableHeader().setReorderingAllowed(false);
        jScrollPane1.setViewportView(jTable1);

        getContentPane().add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(490, 150, 710, 410));

        supp.setBorderPainted(false);
        supp.setContentAreaFilled(false);
        supp.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        supp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                suppActionPerformed(evt);
            }
        });
        getContentPane().add(supp, new org.netbeans.lib.awtextra.AbsoluteConstraints(770, 580, 200, 60));

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

        resetButton.setBorderPainted(false);
        resetButton.setContentAreaFilled(false);
        resetButton.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        resetButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                resetButtonActionPerformed(evt);
            }
        });
        getContentPane().add(resetButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(1000, 580, 190, 60));

        background.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/gerUser.jpg"))); // NOI18N
        getContentPane().add(background, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1280, 800));

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void jComboBox1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox1ActionPerformed
    String selectedRole = (String) jComboBox1.getSelectedItem();

    if (null == selectedRole) {
        System.out.println("Unknown role selected");
    } else // You can now use this selectedRole value for further logic
        switch (selectedRole) {
            case "admin" -> System.out.println("Admin role selected");
        // Perform actions specific to admin role
            case "user" -> System.out.println("User role selected");
        // Perform actions specific to user role
            default -> System.out.println("Unknown role selected");
        }


    }//GEN-LAST:event_jComboBox1ActionPerformed

    private void editActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_editActionPerformed
    int selectedRow = jTable1.getSelectedRow();

    if (selectedRow != -1) {
        String cinStr = cin1.getText().trim();
        String nom = nom1.getText().trim();
        String prenom = prenom1.getText().trim();
        String email = email1.getText().trim();
        String motDePasse = mdp1.getText().trim();
        String role = jComboBox1.getSelectedItem().toString().trim();

        // Validation 1: CIN doit être exactement 8 chiffres et commencer par 0 ou 1
        if (!cinStr.matches("^[01]\\d{7}$")) {
            JOptionPane.showMessageDialog(this, "CIN doit comporter 8 chiffres et commencer par 0 ou 1.");
            return;
        }

        // Validation 2: NOM doit contenir uniquement des lettres
        if (!nom.matches("^[A-Za-z\\s\\-']+$")) {
            JOptionPane.showMessageDialog(this, "Le nom ne doit contenir que des lettres.");
            return;
        }

        // Validation 3: PRENOM aussi
        if (!prenom.matches("^[A-Za-z\\s\\-']+$")) {
            JOptionPane.showMessageDialog(this, "Le prénom ne doit contenir que des lettres.");
            return;
        }

        // Validation 4: Email format simple
        if (!email.matches("^[\\w.-]+@[\\w.-]+\\.[A-Za-z]{2,}$")) {
            JOptionPane.showMessageDialog(this, "Adresse email invalide.");
            return;
        }

        // Validation 5: Rôle doit être "admin" ou "user"
        if (!role.equals("admin") && !role.equals("user")) {
            JOptionPane.showMessageDialog(this, "Le rôle doit être 'admin' ou 'user'.");
            return;
        }

        try {
            connect();
            String sql = "UPDATE users SET nom = ?, prenom = ?, email = ?, mot_de_passe = ?, role = ? WHERE cin = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, nom);
            pstmt.setString(2, prenom);
            pstmt.setString(3, email);
            pstmt.setString(4, motDePasse);
            pstmt.setString(5, role);
            pstmt.setString(6, cinStr);

            int rowsAffected = pstmt.executeUpdate();

            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(this, "Utilisateur CIN " + cinStr + " modifié avec succès !");
                loaduser(); // méthode à définir pour recharger le tableau
            } else {
                JOptionPane.showMessageDialog(this, "Aucune modification effectuée.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Erreur lors de la mise à jour.");
        }

    } else {
        JOptionPane.showMessageDialog(this, "Veuillez sélectionner un utilisateur à modifier.");
    }        // TODO add your handling code here:
    }//GEN-LAST:event_editActionPerformed

    private void suppActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_suppActionPerformed
    int selectedRow = jTable1.getSelectedRow();

    if (selectedRow != -1) {
        String cinStr = jTable1.getValueAt(selectedRow, 0).toString(); // colonne 0 = CIN (clé primaire)

        int confirm = JOptionPane.showConfirmDialog(this,
                "Êtes-vous sûr de vouloir supprimer l'utilisateur avec CIN " + cinStr + " ?",
                "Confirmation de suppression", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            try {
                connect();
                String sql = "DELETE FROM users WHERE cin = ?";
                PreparedStatement pstmt = conn.prepareStatement(sql);
                pstmt.setString(1, cinStr);

                int rowsDeleted = pstmt.executeUpdate();

                if (rowsDeleted > 0) {
                    JOptionPane.showMessageDialog(this, "Utilisateur supprimé avec succès !");
                    loaduser(); // Recharge le tableau après suppression
                } else {
                    JOptionPane.showMessageDialog(this, "Aucune suppression effectuée.");
                }
            } catch (SQLException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Erreur lors de la suppression.");
            }
        }

    } else {
        JOptionPane.showMessageDialog(this, "Veuillez sélectionner un utilisateur à supprimer.");
    }        // TODO add your handling code here:
    }//GEN-LAST:event_suppActionPerformed

    private void cin1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cin1ActionPerformed

    }//GEN-LAST:event_cin1ActionPerformed

    private void addActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addActionPerformed
    String cinStr = cin1.getText().trim();
    String nom = nom1.getText().trim();
    String prenom = prenom1.getText().trim();
    String email = email1.getText().trim();
    String motDePasse = mdp1.getText().trim();
    String role = jComboBox1.getSelectedItem().toString().trim();
    
    
    // Validation 1: CIN doit être exactement 8 chiffres et commencer par 0 ou 1
    if (!cinStr.matches("^[01]\\d{7}$")) {
        JOptionPane.showMessageDialog(this, "CIN doit comporter 8 chiffres et commencer par 0 ou 1.");
        return;
    }

    // Validation 2: NOM doit contenir uniquement des lettres
    if (!nom.matches("^[A-Za-z\\s\\-']+$")) {
        JOptionPane.showMessageDialog(this, "Le nom ne doit contenir que des lettres.");
        return;
    }

    // Validation 3: PRENOM aussi
    if (!prenom.matches("^[A-Za-z\\s\\-']+$")) {
        JOptionPane.showMessageDialog(this, "Le prénom ne doit contenir que des lettres.");
        return;
    }

    // Validation 4: Email format simple
    if (!email.matches("^[\\w.-]+@[\\w.-]+\\.[A-Za-z]{2,}$")) {
        JOptionPane.showMessageDialog(this, "Adresse email invalide.");
        return;
    }

    // Validation 5: Rôle doit être "admin" ou "user"
    if (!role.equals("admin") && !role.equals("user")) {
        JOptionPane.showMessageDialog(this, "Le rôle doit être 'admin' ou 'user'.");
        return;
    }
    

    
    try {
        connect();
        String checkQuery = "SELECT * FROM users WHERE cin = ? OR email = ?";
        PreparedStatement checkStmt = conn.prepareStatement(checkQuery);
        checkStmt.setString(1, cinStr);
        checkStmt.setString(2, email);
        ResultSet rs = checkStmt.executeQuery();
        if (rs.next()) {
            JOptionPane.showMessageDialog(this, "CIN or email already exists!");
            return;
        }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Erreur lors de l'ajout de l'utilisateur.");
        }
        
    try {
        connect();
        String sql = "INSERT INTO users (cin, nom, prenom, email, mot_de_passe, role) VALUES (?, ?, ?, ?, ?, ?)";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setString(1, cinStr);
        pstmt.setString(2, nom);
        pstmt.setString(3, prenom);
        pstmt.setString(4, email);
        pstmt.setString(5, motDePasse);
        pstmt.setString(6, role);

        int rowsInserted = pstmt.executeUpdate();

        if (rowsInserted > 0) {
            JOptionPane.showMessageDialog(this, "Nouvel utilisateur ajouté avec succès !");
            loaduser(); // Recharge le tableau après l'ajout
        } else {
            JOptionPane.showMessageDialog(this, "Aucun utilisateur ajouté.");
        }
    } catch (SQLException e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(this, "Erreur lors de l'ajout de l'utilisateur.");
    }     
    }//GEN-LAST:event_addActionPerformed

    private void goBackButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_goBackButtonActionPerformed
        new admin_interface(currentCin).setVisible(true);
        dispose();
    }//GEN-LAST:event_goBackButtonActionPerformed

    private void deconnecterButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deconnecterButtonActionPerformed
        new Sign_in().setVisible(true);
        dispose();
    }//GEN-LAST:event_deconnecterButtonActionPerformed

    private void exitButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_exitButton1ActionPerformed
        System.exit(0);
    }//GEN-LAST:event_exitButton1ActionPerformed

    private void resetButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_resetButtonActionPerformed
        cin1.setEditable(true);
        cin1.setText("");
        nom1.setText("");
        prenom1.setText("");
        email1.setText("");
        mdp1.setText("");
        jComboBox1.setSelectedIndex(0); 
    }//GEN-LAST:event_resetButtonActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JToggleButton add;
    private javax.swing.JLabel background;
    private javax.swing.JTextField cin1;
    private javax.swing.JButton deconnecterButton;
    private javax.swing.JButton edit;
    private javax.swing.JTextField email1;
    private javax.swing.JButton exitButton1;
    private javax.swing.JButton goBackButton;
    private javax.swing.JComboBox<String> jComboBox1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JTextField mdp1;
    private javax.swing.JTextField nom1;
    private javax.swing.JTextField prenom1;
    private javax.swing.JButton resetButton;
    private javax.swing.JButton supp;
    // End of variables declaration//GEN-END:variables
}
