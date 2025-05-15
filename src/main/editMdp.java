
package main;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class editMdp extends javax.swing.JFrame {
    
    private String currentCin; // The current user's CIN, passed from the caller.
    
    public editMdp(String currentCin) {
        initComponents();
        this.currentCin = currentCin;
        setExtendedState(JFrame.MAXIMIZED_BOTH);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        exitButton = new javax.swing.JButton();
        backButton = new javax.swing.JButton();
        currentMdpField = new javax.swing.JTextField();
        newMdpField = new javax.swing.JPasswordField();
        okButton = new javax.swing.JButton();
        background = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setUndecorated(true);
        setResizable(false);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

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

        currentMdpField.setFont(new java.awt.Font("Segoe UI", 0, 36)); // NOI18N
        getContentPane().add(currentMdpField, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 220, 370, 90));

        newMdpField.setFont(new java.awt.Font("Segoe UI", 0, 36)); // NOI18N
        getContentPane().add(newMdpField, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 400, 370, 90));

        okButton.setBorder(null);
        okButton.setContentAreaFilled(false);
        okButton.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        okButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                okButtonActionPerformed(evt);
            }
        });
        getContentPane().add(okButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(970, 590, 200, 70));

        background.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/editMdp.jpg"))); // NOI18N
        getContentPane().add(background, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1280, 800));

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void exitButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_exitButtonActionPerformed
        System.exit(0);
    }//GEN-LAST:event_exitButtonActionPerformed

    private void backButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_backButtonActionPerformed
        new EditAccountForm(currentCin).setVisible(true);
        this.dispose();
    }//GEN-LAST:event_backButtonActionPerformed

    private void okButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_okButtonActionPerformed
        String currentMdp = currentMdpField.getText().trim();
        String newMdp = new String(newMdpField.getPassword()).trim();

        if (currentMdp.isEmpty() || newMdp.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Fields cannot be empty!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try (Connection con = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/salle_tp", "root", "123456")) {
            String query = "SELECT mot_de_passe FROM users WHERE cin = ?";
            try (PreparedStatement pst = con.prepareStatement(query)) {
                pst.setString(1, currentCin);
                try (ResultSet rs = pst.executeQuery()) {
                    if (rs.next()) {
                        String storedMdp = rs.getString("mot_de_passe");
                        if (!storedMdp.equals(currentMdp)) {
                            JOptionPane.showMessageDialog(this, "Current password is incorrect!", "Error", JOptionPane.ERROR_MESSAGE);
                            return;
                        }

                        if (newMdp.length() < 6) {
                            JOptionPane.showMessageDialog(this, "New password must be at least 6 characters!", "Error", JOptionPane.ERROR_MESSAGE);
                            return;
                        }

                        // Update password
                        String updateQuery = "UPDATE users SET mot_de_passe = ? WHERE cin = ?";
                        try (PreparedStatement updatePst = con.prepareStatement(updateQuery)) {
                            updatePst.setString(1, newMdp);
                            updatePst.setString(2, currentCin);
                            updatePst.executeUpdate();
                        }

                        JOptionPane.showMessageDialog(this, "Password updated successfully!");
                        new EditAccountForm(currentCin).setVisible(true);
                        this.dispose();
                    } else {
                        JOptionPane.showMessageDialog(this, "User not found!", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Database error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }//GEN-LAST:event_okButtonActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton backButton;
    private javax.swing.JLabel background;
    private javax.swing.JTextField currentMdpField;
    private javax.swing.JButton exitButton;
    private javax.swing.JPasswordField newMdpField;
    private javax.swing.JButton okButton;
    // End of variables declaration//GEN-END:variables
}
