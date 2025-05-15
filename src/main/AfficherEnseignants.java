package main;

import java.awt.Color;
import java.sql.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.Dimension;
import java.awt.Font;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.SwingConstants;

public class AfficherEnseignants extends javax.swing.JFrame {
    
    private String currentCin;

    
    public AfficherEnseignants(String cin) {
        initComponents();
         
        // Configure table header
        JTableHeader header = enseignantsTable.getTableHeader();
        header.setPreferredSize(new Dimension(header.getWidth(), 50)); // Set header height (e.g., 50px)
        header.setFont(new Font("Segoe UI", Font.BOLD, 24)); // Larger font
        
        this.currentCin = cin;
        loadEnseignantsData();
        setExtendedState(JFrame.MAXIMIZED_BOTH);
    }
    
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
    
    
    private void loadEnseignantsData() {
        connect();
        try {
            String query = "SELECT cin, nom, prenom, specialite, nbre_max_heure FROM enseignant";
            PreparedStatement pst = conn.prepareStatement(query);
            ResultSet rs = pst.executeQuery();
            
            DefaultTableModel model = (DefaultTableModel) enseignantsTable.getModel();
            model.setRowCount(0); // Clear existing data
            
            // Add column names
            model.setColumnIdentifiers(new String[]{"CIN", "Nom", "Prénom", "Spécialité", "Heures max/semaine"});
            
            while(rs.next()) {
                model.addRow(new Object[]{
                    rs.getString("cin"),
                    rs.getString("nom"),
                    rs.getString("prenom"),
                    rs.getString("specialite"),
                    rs.getInt("nbre_max_heure")
                });
            }
            
            rs.close();
            pst.close();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error loading data: " + ex.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        goBackButton = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        enseignantsTable = new javax.swing.JTable();
        exitButton = new javax.swing.JButton();
        deconnecterButton = new javax.swing.JButton();
        Background = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setUndecorated(true);
        setResizable(false);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

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

        jScrollPane1.setFont(new java.awt.Font("Segoe UI", 0, 36)); // NOI18N
        jScrollPane1.setOpaque(false);
        jScrollPane1.setPreferredSize(new java.awt.Dimension(500, 500));

        enseignantsTable.setFont(new java.awt.Font("Segoe UI", 0, 36)); // NOI18N
        enseignantsTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "CIN", "Nom", "Prénom", "Spécialité", "Nb Heure/Semaine"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Integer.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        enseignantsTable.setRowHeight(90);
        enseignantsTable.setShowHorizontalLines(true);
        enseignantsTable.setShowVerticalLines(true);
        enseignantsTable.getTableHeader().setReorderingAllowed(false);
        jScrollPane1.setViewportView(enseignantsTable);
        if (enseignantsTable.getColumnModel().getColumnCount() > 0) {
            enseignantsTable.getColumnModel().getColumn(0).setResizable(false);
            enseignantsTable.getColumnModel().getColumn(1).setResizable(false);
            enseignantsTable.getColumnModel().getColumn(2).setResizable(false);
            enseignantsTable.getColumnModel().getColumn(3).setResizable(false);
            enseignantsTable.getColumnModel().getColumn(4).setResizable(false);
        }

        getContentPane().add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 140, 1100, 510));

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
        getContentPane().add(deconnecterButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(1030, 700, 220, 80));

        Background.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/AffEnsgnt.jpg"))); // NOI18N
        getContentPane().add(Background, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1280, 800));

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void goBackButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_goBackButtonActionPerformed
        new user_interface(currentCin).setVisible(true);
        dispose();
    }//GEN-LAST:event_goBackButtonActionPerformed

    private void exitButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_exitButtonActionPerformed
        System.exit(0);
    }//GEN-LAST:event_exitButtonActionPerformed

    private void deconnecterButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deconnecterButtonActionPerformed
        new Sign_in().setVisible(true);
        dispose();
    }//GEN-LAST:event_deconnecterButtonActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel Background;
    private javax.swing.JButton deconnecterButton;
    private javax.swing.JTable enseignantsTable;
    private javax.swing.JButton exitButton;
    private javax.swing.JButton goBackButton;
    private javax.swing.JScrollPane jScrollPane1;
    // End of variables declaration//GEN-END:variables
}
