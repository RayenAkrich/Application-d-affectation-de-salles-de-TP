package main;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class Sign_up extends javax.swing.JFrame {

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
    
    public Sign_up() {
        initComponents();
        setExtendedState(JFrame.MAXIMIZED_BOTH);
    }
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        goToSignInButton = new javax.swing.JButton();
        signUpButton = new javax.swing.JButton();
        cinTextField = new javax.swing.JTextField();
        nomTextField = new javax.swing.JTextField();
        prenomTextField = new javax.swing.JTextField();
        emailTextField = new javax.swing.JTextField();
        passwordField = new javax.swing.JPasswordField();
        exitButton = new javax.swing.JButton();
        Background = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setUndecorated(true);
        setResizable(false);
        setSize(new java.awt.Dimension(0, 0));
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        goToSignInButton.setBackground(new java.awt.Color(0, 0, 102));
        goToSignInButton.setFont(new java.awt.Font("Segoe UI", 2, 24)); // NOI18N
        goToSignInButton.setBorderPainted(false);
        goToSignInButton.setContentAreaFilled(false);
        goToSignInButton.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        goToSignInButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                goToSignInButtonActionPerformed(evt);
            }
        });
        getContentPane().add(goToSignInButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(1050, 720, 200, 70));

        signUpButton.setBackground(new java.awt.Color(0, 0, 102));
        signUpButton.setFont(new java.awt.Font("Baskerville Old Face", 1, 36)); // NOI18N
        signUpButton.setBorderPainted(false);
        signUpButton.setContentAreaFilled(false);
        signUpButton.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        signUpButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                signUpButtonActionPerformed(evt);
            }
        });
        getContentPane().add(signUpButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(720, 640, 200, 60));

        cinTextField.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        cinTextField.setBorder(null);
        cinTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cinTextFieldActionPerformed(evt);
            }
        });
        getContentPane().add(cinTextField, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 240, 490, 30));

        nomTextField.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        nomTextField.setBorder(null);
        getContentPane().add(nomTextField, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 320, 490, 30));

        prenomTextField.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        prenomTextField.setBorder(null);
        prenomTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                prenomTextFieldActionPerformed(evt);
            }
        });
        getContentPane().add(prenomTextField, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 410, 490, 30));

        emailTextField.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        emailTextField.setBorder(null);
        emailTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                emailTextFieldActionPerformed(evt);
            }
        });
        getContentPane().add(emailTextField, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 500, 490, 30));

        passwordField.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        passwordField.setBorder(null);
        getContentPane().add(passwordField, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 590, 490, -1));

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

        Background.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/InscriptionBGD.jpg"))); // NOI18N
        getContentPane().add(Background, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1280, 800));

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void signUpButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_signUpButtonActionPerformed
                                                   
        // Get input values from text fields
        String cinStr = cinTextField.getText().trim();
        String nom = nomTextField.getText().trim();
        String prenom = prenomTextField.getText().trim();
        String email = emailTextField.getText().trim();
        String password = new String(passwordField.getPassword());
        
        // ---- Validations ----
        // 1. CIN must be exactly 8 digits and start with 0 or 1.
        if (!cinStr.matches("^[01]\\d{7}$")) {
            JOptionPane.showMessageDialog(null, "CIN must be exactly 8 digits and start with 0 or 1.");
            return;
        }
        // 2. NOM must contain only alphabetic letters.
        if (!nom.matches("^[A-Za-z\\s\\-']+$")) {
            JOptionPane.showMessageDialog(null, "Nom must contain only alphabetic letters.");
            return;
        }
        // 3. PRENOM must contain only alphabetic letters.
        if (!prenom.matches("^[A-Za-z\\s\\-']+$")) {
            JOptionPane.showMessageDialog(null, "Prenom must contain only alphabetic letters.");
            return;
        }
        // 4. EMAIL must contain an '@' character.
        if (!email.matches("^[\\w+.-]+@[\\w.-]+\\.[A-Za-z]{2,}$")) {
            JOptionPane.showMessageDialog(null, "Email must contain a valid format like 'example@domain.com'.");
            return;
        }
        // 5. MOT DE PASSE must be more than 5 characters.
        if (password.length() <= 5) {
            JOptionPane.showMessageDialog(null, "Mot de passe must be more than 5 characters.");
            return;
        }
        
        // ---- Check if user already exists and register the user ----
        connect();
        if (conn == null) {
            JOptionPane.showMessageDialog(null, "Cannot connect to the database.");
            return;
        }
        
        try {
            // Check if a user with the same CIN or email already exists
            String checkQuery = "SELECT * FROM users WHERE cin = ? OR email = ?";
            PreparedStatement checkStmt = conn.prepareStatement(checkQuery);
            checkStmt.setString(1, cinStr);
            checkStmt.setString(2, email);
            ResultSet rs = checkStmt.executeQuery();
            if (rs.next()) {
                JOptionPane.showMessageDialog(null, "User with this CIN or email already exists.");
                return;
            }
            
            // Insert the new user; role is set to 'user' by default, and date_inscription is auto-set.
            String insertQuery = "INSERT INTO users (cin, nom, prenom, email, mot_de_passe, role) VALUES (?, ?, ?, ?, ?, 'user')";
            PreparedStatement insertStmt = conn.prepareStatement(insertQuery);
            insertStmt.setString(1, cinStr);
            insertStmt.setString(2, nom);
            insertStmt.setString(3, prenom);
            insertStmt.setString(4, email);
            insertStmt.setString(5, password);
            
            int rowsInserted = insertStmt.executeUpdate();
            if (rowsInserted > 0) {
                JOptionPane.showMessageDialog(null, "Registration successful!");
                // Optionally, redirect to Sign_in page:
                new Sign_in().setVisible(true);
                dispose();
            } else {
                JOptionPane.showMessageDialog(null, "Registration failed, please try again.");
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error: " + ex.getMessage());
        }                                   
    }//GEN-LAST:event_signUpButtonActionPerformed

    private void cinTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cinTextFieldActionPerformed

    }//GEN-LAST:event_cinTextFieldActionPerformed

    private void goToSignInButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_goToSignInButtonActionPerformed
       new Sign_in().setVisible(true);
        dispose();
    }//GEN-LAST:event_goToSignInButtonActionPerformed

    private void emailTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_emailTextFieldActionPerformed

    }//GEN-LAST:event_emailTextFieldActionPerformed

    private void prenomTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_prenomTextFieldActionPerformed

    }//GEN-LAST:event_prenomTextFieldActionPerformed

    private void exitButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_exitButtonActionPerformed
        System.exit(0);
    }//GEN-LAST:event_exitButtonActionPerformed

    public static void main(String args[]) {
        
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Sign_up().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel Background;
    private javax.swing.JTextField cinTextField;
    private javax.swing.JTextField emailTextField;
    private javax.swing.JButton exitButton;
    private javax.swing.JButton goToSignInButton;
    private javax.swing.JTextField nomTextField;
    private javax.swing.JPasswordField passwordField;
    private javax.swing.JTextField prenomTextField;
    private javax.swing.JButton signUpButton;
    // End of variables declaration//GEN-END:variables
}
