package main;

import javax.swing.JFrame;

public class admin_interface extends javax.swing.JFrame {

    private String loggedInCin;
    
    public admin_interface(String cin) {
        initComponents();
        this.loggedInCin = cin;
        setExtendedState(JFrame.MAXIMIZED_BOTH);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        gererSalleButton = new javax.swing.JButton();
        gererEnseignantButton = new javax.swing.JButton();
        gererMaintenanceButton = new javax.swing.JButton();
        gererAffectationButton = new javax.swing.JButton();
        gererUserButton = new javax.swing.JButton();
        deconnecterButton = new javax.swing.JButton();
        EditAccount = new javax.swing.JButton();
        PressICON = new javax.swing.JLabel();
        exitButton = new javax.swing.JButton();
        background = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setUndecorated(true);
        setResizable(false);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        gererSalleButton.setFont(new java.awt.Font("Yu Gothic", 1, 30)); // NOI18N
        gererSalleButton.setForeground(new java.awt.Color(255, 255, 255));
        gererSalleButton.setBorder(null);
        gererSalleButton.setBorderPainted(false);
        gererSalleButton.setContentAreaFilled(false);
        gererSalleButton.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        gererSalleButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                gererSalleButtonActionPerformed(evt);
            }
        });
        getContentPane().add(gererSalleButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 270, 450, 90));

        gererEnseignantButton.setFont(new java.awt.Font("Yu Gothic", 1, 30)); // NOI18N
        gererEnseignantButton.setForeground(new java.awt.Color(255, 255, 255));
        gererEnseignantButton.setBorder(null);
        gererEnseignantButton.setBorderPainted(false);
        gererEnseignantButton.setContentAreaFilled(false);
        gererEnseignantButton.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        gererEnseignantButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                gererEnseignantButtonActionPerformed(evt);
            }
        });
        getContentPane().add(gererEnseignantButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(690, 270, 450, 100));

        gererMaintenanceButton.setFont(new java.awt.Font("Yu Gothic", 1, 30)); // NOI18N
        gererMaintenanceButton.setForeground(new java.awt.Color(255, 255, 255));
        gererMaintenanceButton.setBorder(null);
        gererMaintenanceButton.setBorderPainted(false);
        gererMaintenanceButton.setContentAreaFilled(false);
        gererMaintenanceButton.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        gererMaintenanceButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                gererMaintenanceButtonActionPerformed(evt);
            }
        });
        getContentPane().add(gererMaintenanceButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 430, 450, 100));

        gererAffectationButton.setFont(new java.awt.Font("Yu Gothic", 1, 30)); // NOI18N
        gererAffectationButton.setForeground(new java.awt.Color(255, 255, 255));
        gererAffectationButton.setBorder(null);
        gererAffectationButton.setBorderPainted(false);
        gererAffectationButton.setContentAreaFilled(false);
        gererAffectationButton.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        gererAffectationButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                gererAffectationButtonActionPerformed(evt);
            }
        });
        getContentPane().add(gererAffectationButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(690, 430, 450, 100));

        gererUserButton.setFont(new java.awt.Font("Yu Gothic", 1, 30)); // NOI18N
        gererUserButton.setForeground(new java.awt.Color(255, 255, 255));
        gererUserButton.setBorder(null);
        gererUserButton.setBorderPainted(false);
        gererUserButton.setContentAreaFilled(false);
        gererUserButton.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        gererUserButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                gererUserButtonActionPerformed(evt);
            }
        });
        getContentPane().add(gererUserButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(400, 590, 450, 100));

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

        EditAccount.setBorder(null);
        EditAccount.setBorderPainted(false);
        EditAccount.setContentAreaFilled(false);
        EditAccount.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        EditAccount.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                EditAccountActionPerformed(evt);
            }
        });
        getContentPane().add(EditAccount, new org.netbeans.lib.awtextra.AbsoluteConstraints(1050, 20, 180, 190));

        PressICON.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        PressICON.setForeground(new java.awt.Color(255, 255, 255));
        PressICON.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        PressICON.setText("Press the icon to edit your account");
        PressICON.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        getContentPane().add(PressICON, new org.netbeans.lib.awtextra.AbsoluteConstraints(1040, 220, 200, -1));

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

        background.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/InterfaceADMIN.jpg"))); // NOI18N
        getContentPane().add(background, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1280, 800));

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void gererSalleButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_gererSalleButtonActionPerformed
        new gerSalle(loggedInCin).setVisible(true);
        dispose();
    }//GEN-LAST:event_gererSalleButtonActionPerformed

    private void gererEnseignantButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_gererEnseignantButtonActionPerformed
        new gerEnseignant(loggedInCin).setVisible(true);
        dispose();
    }//GEN-LAST:event_gererEnseignantButtonActionPerformed

    private void gererAffectationButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_gererAffectationButtonActionPerformed
        new gerAffectation(loggedInCin).setVisible(true);
        dispose();
    }//GEN-LAST:event_gererAffectationButtonActionPerformed

    private void gererUserButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_gererUserButtonActionPerformed
        new gerUser(loggedInCin).setVisible(true);
        dispose();
    }//GEN-LAST:event_gererUserButtonActionPerformed

    private void gererMaintenanceButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_gererMaintenanceButtonActionPerformed
        new gerMaint(loggedInCin).setVisible(true);
        dispose();
    }//GEN-LAST:event_gererMaintenanceButtonActionPerformed

    private void deconnecterButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deconnecterButtonActionPerformed
        new Sign_in().setVisible(true);
        dispose();
    }//GEN-LAST:event_deconnecterButtonActionPerformed

    private void EditAccountActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_EditAccountActionPerformed
        // Pass the logged-in user's CIN to the edit form
        new EditAccountForm(loggedInCin).setVisible(true);
        dispose(); // Close the current window
    }//GEN-LAST:event_EditAccountActionPerformed

    private void exitButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_exitButtonActionPerformed
        System.exit(0);
    }//GEN-LAST:event_exitButtonActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton EditAccount;
    private javax.swing.JLabel PressICON;
    private javax.swing.JLabel background;
    private javax.swing.JButton deconnecterButton;
    private javax.swing.JButton exitButton;
    private javax.swing.JButton gererAffectationButton;
    private javax.swing.JButton gererEnseignantButton;
    private javax.swing.JButton gererMaintenanceButton;
    private javax.swing.JButton gererSalleButton;
    private javax.swing.JButton gererUserButton;
    // End of variables declaration//GEN-END:variables
}
