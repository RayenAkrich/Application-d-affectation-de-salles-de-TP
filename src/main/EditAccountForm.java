package main;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class EditAccountForm extends javax.swing.JFrame {
    
    private String currentCin; // The current user's CIN, passed from the caller.
    
    public EditAccountForm(String currentCin) {
        initComponents();
        this.currentCin = currentCin;
        loadUserData();
        setExtendedState(JFrame.MAXIMIZED_BOTH);
    }
    
        // Load current user's data from the database into the text fields.
    private void loadUserData() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/salle_tp", "root", "123456");
            String query = "SELECT cin, nom, prenom, email FROM users WHERE cin = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, currentCin);
            ResultSet rs = stmt.executeQuery();
            if(rs.next()){
                cinLabel.setText(rs.getString("cin"));
                nomTextField.setText(rs.getString("nom"));
                prenomTextField.setText(rs.getString("prenom"));
                emailTextField.setText(rs.getString("email"));
            } else {
                JOptionPane.showMessageDialog(null, "User not found!");
            }
            rs.close();
            stmt.close();
            conn.close();
        } catch(Exception ex){
            JOptionPane.showMessageDialog(null, "Error loading user data: " + ex.getMessage());
        }
    }
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        cinLabel = new javax.swing.JLabel();
        nomTextField = new javax.swing.JTextField();
        prenomTextField = new javax.swing.JTextField();
        emailTextField = new javax.swing.JTextField();
        doneButton = new javax.swing.JButton();
        backButton = new javax.swing.JButton();
        exitButton = new javax.swing.JButton();
        editMdp = new javax.swing.JButton();
        Background = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setUndecorated(true);
        setResizable(false);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        cinLabel.setForeground(new java.awt.Color(255, 51, 51));
        cinLabel.setOpaque(true);
        getContentPane().add(cinLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 250, 500, 40));

        nomTextField.setBorder(null);
        nomTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                nomTextFieldActionPerformed(evt);
            }
        });
        getContentPane().add(nomTextField, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 350, 500, 40));

        prenomTextField.setBorder(null);
        prenomTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                prenomTextFieldActionPerformed(evt);
            }
        });
        getContentPane().add(prenomTextField, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 460, 500, 40));

        emailTextField.setBorder(null);
        emailTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                emailTextFieldActionPerformed(evt);
            }
        });
        getContentPane().add(emailTextField, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 560, 500, 40));

        doneButton.setFont(new java.awt.Font("Baskerville Old Face", 1, 36)); // NOI18N
        doneButton.setBorder(null);
        doneButton.setBorderPainted(false);
        doneButton.setContentAreaFilled(false);
        doneButton.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        doneButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                doneButtonActionPerformed(evt);
            }
        });
        getContentPane().add(doneButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(720, 650, 190, 50));

        backButton.setFont(new java.awt.Font("Baskerville Old Face", 1, 20)); // NOI18N
        backButton.setBorder(null);
        backButton.setBorderPainted(false);
        backButton.setContentAreaFilled(false);
        backButton.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        backButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                backButtonActionPerformed(evt);
            }
        });
        getContentPane().add(backButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(1060, 730, 190, 60));

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

        editMdp.setBorderPainted(false);
        editMdp.setContentAreaFilled(false);
        editMdp.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        editMdp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                editMdpActionPerformed(evt);
            }
        });
        getContentPane().add(editMdp, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 650, 180, 50));

        Background.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/EditACCOUNT.jpg"))); // NOI18N
        getContentPane().add(Background, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1280, 800));

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void doneButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_doneButtonActionPerformed
        // Retrieve new values from fields
        String newNom = nomTextField.getText().trim();
        String newPrenom = prenomTextField.getText().trim();
        String newEmail = emailTextField.getText().trim();

        // ---- Validations ----
        // Vérification des champs vides
        if(newNom.isEmpty() || newPrenom.isEmpty() || newEmail.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Tous les champs doivent être remplis.");
            return;
        }

        if(!newNom.matches("^[A-Za-z\\s\\-']+$")){
            JOptionPane.showMessageDialog(null, "Nom must contain only alphabetic letters.");
            return;
        }
        if(!newPrenom.matches("^[A-Za-z\\s\\-']+$")){
            JOptionPane.showMessageDialog(null, "Prenom must contain only alphabetic letters.");
            return;
        }
        if(!newEmail.matches("^[\\w+.-]+@[\\w.-]+\\.[A-Za-z]{2,}$")){
            JOptionPane.showMessageDialog(null, "Email must be valid, e.g., example@domain.com");
            return;
        }

        // ---- Database Update ----
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/salle_tp", "root", "123456")) {

            // 1. Check for duplicate email
            String emailCheckQuery = "SELECT * FROM users WHERE email = ? AND cin != ?";
            try (PreparedStatement emailCheckStmt = conn.prepareStatement(emailCheckQuery)) {
                emailCheckStmt.setString(1, newEmail);
                emailCheckStmt.setString(2, currentCin);
                try (ResultSet emailRs = emailCheckStmt.executeQuery()) {
                    if (emailRs.next()) {
                        JOptionPane.showMessageDialog(null, "Email is already in use by another account!");
                        return;
                    }
                }
            }

            // 2. Update user data
            String updateQuery = "UPDATE users SET nom = ?, prenom = ?, email = ? WHERE cin = ?";
            try (PreparedStatement updateStmt = conn.prepareStatement(updateQuery)) {
                updateStmt.setString(1, newNom);
                updateStmt.setString(2, newPrenom);
                updateStmt.setString(3, newEmail);
                updateStmt.setString(4, currentCin); // Correction: index 4 au lieu de 5

                int rowsUpdated = updateStmt.executeUpdate();
                if (rowsUpdated > 0) {
                    JOptionPane.showMessageDialog(null, "Account updated successfully!");

                    // Vérification du rôle avant redirection
                    String roleQuery = "SELECT role FROM users WHERE cin = ?";
                    try (PreparedStatement roleStmt = conn.prepareStatement(roleQuery)) {
                        roleStmt.setString(1, currentCin);
                        try (ResultSet roleRs = roleStmt.executeQuery()) {
                            if (roleRs.next()) {
                                String role = roleRs.getString("role");
                                this.dispose();
                                if ("admin".equalsIgnoreCase(role)) {
                                    new admin_interface(currentCin).setVisible(true);
                                } else {
                                    new user_interface(currentCin).setVisible(true);
                                }
                            }
                        }
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Update failed. Please try again.");
                }
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Error: " + ex.getMessage());
        }
    }//GEN-LAST:event_doneButtonActionPerformed

    private void nomTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_nomTextFieldActionPerformed

    }//GEN-LAST:event_nomTextFieldActionPerformed

    private void prenomTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_prenomTextFieldActionPerformed

    }//GEN-LAST:event_prenomTextFieldActionPerformed

    private void emailTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_emailTextFieldActionPerformed

    }//GEN-LAST:event_emailTextFieldActionPerformed

    private void backButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_backButtonActionPerformed
        
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/salle_tp", "root", "123456")) {
            String roleQuery = "SELECT role FROM users WHERE cin = ?";
            try (PreparedStatement pst = conn.prepareStatement(roleQuery)) {
                pst.setString(1, currentCin);
                try (ResultSet rs = pst.executeQuery()) {
                    if (rs.next()) {
                        String role = rs.getString("role");

                        if ("admin".equalsIgnoreCase(role)) {
                            new admin_interface(currentCin).setVisible(true); // Pass to admin interface
                        } else {
                            new user_interface(currentCin).setVisible(true);
                        }
                        this.dispose();
                    }
                }
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Error retrieving user role: " + ex.getMessage());
            // Fallback to regular user interface
            new user_interface(currentCin).setVisible(true);
            this.dispose();
        }
        
    }//GEN-LAST:event_backButtonActionPerformed

    private void exitButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_exitButtonActionPerformed
        System.exit(0);
    }//GEN-LAST:event_exitButtonActionPerformed

    private void editMdpActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_editMdpActionPerformed
        new editMdp(currentCin).setVisible(true);
            this.dispose();
    }//GEN-LAST:event_editMdpActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel Background;
    private javax.swing.JButton backButton;
    private javax.swing.JLabel cinLabel;
    private javax.swing.JButton doneButton;
    private javax.swing.JButton editMdp;
    private javax.swing.JTextField emailTextField;
    private javax.swing.JButton exitButton;
    private javax.swing.JTextField nomTextField;
    private javax.swing.JTextField prenomTextField;
    // End of variables declaration//GEN-END:variables
}
