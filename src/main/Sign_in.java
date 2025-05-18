package main;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.swing.JOptionPane;
import javax.swing.JFrame;

public class Sign_in extends javax.swing.JFrame {
    
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

    public Sign_in() {
        initComponents();
        setExtendedState(JFrame.MAXIMIZED_BOTH);
    }
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel6 = new javax.swing.JLabel();
        signInButton = new javax.swing.JButton();
        goToSignUpButton = new javax.swing.JButton();
        passwordField = new javax.swing.JPasswordField();
        emailTextField = new javax.swing.JTextField();
        exitButton = new javax.swing.JButton();
        Background = new javax.swing.JLabel();

        jLabel6.setText("jLabel6");

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("SIGN IN");
        setUndecorated(true);
        setResizable(false);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        signInButton.setBackground(new java.awt.Color(0, 0, 102));
        signInButton.setFont(new java.awt.Font("Baskerville Old Face", 1, 24)); // NOI18N
        signInButton.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        signInButton.setBorderPainted(false);
        signInButton.setContentAreaFilled(false);
        signInButton.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        signInButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                signInButtonActionPerformed(evt);
            }
        });
        getContentPane().add(signInButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(990, 560, 190, 60));

        goToSignUpButton.setBackground(new java.awt.Color(0, 51, 102));
        goToSignUpButton.setFont(new java.awt.Font("Segoe UI", 3, 24)); // NOI18N
        goToSignUpButton.setToolTipText("");
        goToSignUpButton.setBorderPainted(false);
        goToSignUpButton.setContentAreaFilled(false);
        goToSignUpButton.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        goToSignUpButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                goToSignUpButtonActionPerformed(evt);
            }
        });
        getContentPane().add(goToSignUpButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(970, 660, 180, 60));

        passwordField.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        passwordField.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        passwordField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                passwordFieldActionPerformed(evt);
            }
        });
        getContentPane().add(passwordField, new org.netbeans.lib.awtextra.AbsoluteConstraints(800, 450, 330, 60));

        emailTextField.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        emailTextField.setBorder(null);
        emailTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                emailTextFieldActionPerformed(evt);
            }
        });
        getContentPane().add(emailTextField, new org.netbeans.lib.awtextra.AbsoluteConstraints(800, 300, 340, 60));

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

        Background.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/ConnexionBGD.jpg"))); // NOI18N
        getContentPane().add(Background, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, -1));

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents
    private String[] authenticateUser(String email, String password) {  
        connect();
        String query = "SELECT role, cin FROM users WHERE email = ? AND mot_de_passe = ?";
        try {
            PreparedStatement pst = conn.prepareStatement(query);
            pst.setString(1, email);
            pst.setString(2, password);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                return new String[]{rs.getString("role"), rs.getString("cin")};
            }
        } catch (SQLException ex) {
            System.out.println("Authentication failed: " + ex.getMessage());
        }
        return null; 
    }
    private void signInButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_signInButtonActionPerformed
                                           
        String email = emailTextField.getText();
        String password = new String(passwordField.getPassword());

        // Get role AND cin
        String[] authResult = authenticateUser(email, password);
        
        if (authResult != null) {
            String role = authResult[0];
            String cin = authResult[1];

            JOptionPane.showMessageDialog(null, "Sign-in successful as " + role + "!");
        
            if (role.equalsIgnoreCase("admin")) {
                new admin_interface(cin).setVisible(true);
            } else {
                // Pass cin to user_interface
                new user_interface(cin).setVisible(true);
            }
            dispose();
        } else {
            JOptionPane.showMessageDialog(null, "Invalid email or password.");
        }
    }//GEN-LAST:event_signInButtonActionPerformed

    private void goToSignUpButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_goToSignUpButtonActionPerformed
        Sign_up signUpForm = new Sign_up();
        signUpForm.setVisible(true);
        dispose();
    }//GEN-LAST:event_goToSignUpButtonActionPerformed

    private void passwordFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_passwordFieldActionPerformed

    }//GEN-LAST:event_passwordFieldActionPerformed

    private void emailTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_emailTextFieldActionPerformed

    }//GEN-LAST:event_emailTextFieldActionPerformed

    private void exitButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_exitButtonActionPerformed
        System.exit(0);
    }//GEN-LAST:event_exitButtonActionPerformed

    public static void main(String args[]) {
        
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Sign_in().setVisible(true);
            }
        });
        
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel Background;
    private javax.swing.JTextField emailTextField;
    private javax.swing.JButton exitButton;
    private javax.swing.JButton goToSignUpButton;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPasswordField passwordField;
    private javax.swing.JButton signInButton;
    // End of variables declaration//GEN-END:variables
}
