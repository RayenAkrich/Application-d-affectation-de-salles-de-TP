package main;

import javax.swing.JFrame;

public class user_interface extends javax.swing.JFrame {
    
    private String loggedInCin;
    
    public user_interface(String cin) {
        initComponents();
        this.loggedInCin = cin;
        setExtendedState(JFrame.MAXIMIZED_BOTH);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        deconnecterButton = new javax.swing.JButton();
        afficherSallesButton = new javax.swing.JButton();
        afficherEnseignantsButton = new javax.swing.JButton();
        afficherAffectationsButton = new javax.swing.JButton();
        EditAccount = new javax.swing.JButton();
        PressICON = new javax.swing.JLabel();
        exitButton = new javax.swing.JButton();
        background = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setUndecorated(true);
        setResizable(false);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

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

        afficherSallesButton.setFont(new java.awt.Font("Yu Gothic", 1, 36)); // NOI18N
        afficherSallesButton.setForeground(new java.awt.Color(255, 255, 255));
        afficherSallesButton.setActionCommand("null");
        afficherSallesButton.setBorderPainted(false);
        afficherSallesButton.setContentAreaFilled(false);
        afficherSallesButton.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        afficherSallesButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                afficherSallesButtonActionPerformed(evt);
            }
        });
        getContentPane().add(afficherSallesButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 300, 560, 120));

        afficherEnseignantsButton.setFont(new java.awt.Font("Yu Gothic", 1, 36)); // NOI18N
        afficherEnseignantsButton.setForeground(new java.awt.Color(255, 255, 255));
        afficherEnseignantsButton.setActionCommand("null");
        afficherEnseignantsButton.setBorderPainted(false);
        afficherEnseignantsButton.setContentAreaFilled(false);
        afficherEnseignantsButton.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        afficherEnseignantsButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                afficherEnseignantsButtonActionPerformed(evt);
            }
        });
        getContentPane().add(afficherEnseignantsButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(675, 303, 570, 120));

        afficherAffectationsButton.setFont(new java.awt.Font("Yu Gothic", 1, 36)); // NOI18N
        afficherAffectationsButton.setForeground(new java.awt.Color(255, 255, 255));
        afficherAffectationsButton.setBorderPainted(false);
        afficherAffectationsButton.setContentAreaFilled(false);
        afficherAffectationsButton.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        afficherAffectationsButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                afficherAffectationsButtonActionPerformed(evt);
            }
        });
        getContentPane().add(afficherAffectationsButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(335, 553, 610, 130));

        EditAccount.setBorder(null);
        EditAccount.setBorderPainted(false);
        EditAccount.setContentAreaFilled(false);
        EditAccount.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        EditAccount.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                EditAccountActionPerformed(evt);
            }
        });
        getContentPane().add(EditAccount, new org.netbeans.lib.awtextra.AbsoluteConstraints(1040, 23, 200, 200));

        PressICON.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        PressICON.setForeground(new java.awt.Color(255, 255, 255));
        PressICON.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        PressICON.setText("Press the icon to edit your account");
        PressICON.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        getContentPane().add(PressICON, new org.netbeans.lib.awtextra.AbsoluteConstraints(1040, 230, 200, -1));

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

        background.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/InterfaceUSER.jpg"))); // NOI18N
        getContentPane().add(background, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1280, 800));

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void deconnecterButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deconnecterButtonActionPerformed
        new Sign_in().setVisible(true);
        dispose();
    }//GEN-LAST:event_deconnecterButtonActionPerformed

    private void afficherSallesButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_afficherSallesButtonActionPerformed
        new AfficherSalles(loggedInCin).setVisible(true);
        dispose();
    }//GEN-LAST:event_afficherSallesButtonActionPerformed

    private void afficherAffectationsButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_afficherAffectationsButtonActionPerformed
        new AfficherAffectations(loggedInCin).setVisible(true);
        dispose();
    }//GEN-LAST:event_afficherAffectationsButtonActionPerformed

    private void afficherEnseignantsButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_afficherEnseignantsButtonActionPerformed
        new AfficherEnseignants(loggedInCin).setVisible(true);
        dispose();
    }//GEN-LAST:event_afficherEnseignantsButtonActionPerformed

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
    private javax.swing.JButton afficherAffectationsButton;
    private javax.swing.JButton afficherEnseignantsButton;
    private javax.swing.JButton afficherSallesButton;
    private javax.swing.JLabel background;
    private javax.swing.JButton deconnecterButton;
    private javax.swing.JButton exitButton;
    // End of variables declaration//GEN-END:variables
}
