
package main;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.sql.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.SwingConstants;
import javax.swing.JFrame;

public class AfficherAffectations extends javax.swing.JFrame {

    private String currentCin;
    private Connection conn;
    
    public AfficherAffectations(String Cin) {
        initComponents();
        
        // Configure table header
        JTableHeader header = affectationTable.getTableHeader();
        header.setPreferredSize(new Dimension(header.getWidth(), 50)); // Set header height (e.g., 50px)
        header.setFont(new Font("Segoe UI", Font.BOLD, 24)); // Larger font
        
        this.currentCin = Cin;
        loadAffectationData();
        setExtendedState(JFrame.MAXIMIZED_BOTH);
    }
    
    private void connect() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/salle_tp", "root", "123456");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Database connection failed: " + e.getMessage());
        }
    }

    private void loadAffectationData() {
        connect();
        try {
            String query = "SELECT id_affect, id_salle, jour, heure_deb, heure_fin, nbre_etud FROM affectation";
            PreparedStatement pst = conn.prepareStatement(query);
            ResultSet rs = pst.executeQuery();
            
            DefaultTableModel model = (DefaultTableModel) affectationTable.getModel();
            model.setRowCount(0);
            
            model.setColumnIdentifiers(new String[]{"Num Affectation", "Num Salle", "Jour", "Heure Déb", "Heure Fin", "Nb Étudiants"});
            
            while(rs.next()) {
                model.addRow(new Object[]{
                    rs.getInt("id_affect"),
                    rs.getInt("id_salle"),
                    rs.getString("jour"),
                    rs.getTime("heure_deb"),
                    rs.getTime("heure_fin"),
                    rs.getInt("nbre_etud")
                });
            }
            
            rs.close();
            pst.close();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error loading data: " + ex.getMessage());
        }
    }
    
    private void showMostAffectedSalle() {
        connect();
        try {
            String query = "SELECT s.id_salle, s.nom, s.capacite, s.nbr_max_aff, COUNT(*) AS total_affectations " +
                           "FROM affectation a " +
                           "JOIN salle s ON a.id_salle = s.id_salle " +
                           "GROUP BY s.id_salle " +
                           "ORDER BY total_affectations DESC LIMIT 1";

            PreparedStatement pst = conn.prepareStatement(query);
            ResultSet rs = pst.executeQuery();

            if(rs.next()) {
                String message = "<html>" +
                                 "Salle la plus affectée :<br>" +
                                 "ID: " + rs.getInt("id_salle") + "<br>" +
                                 "Nom: " + rs.getString("nom") + "<br>" +
                                 "Capacité: " + rs.getInt("capacite") + "<br>" +
                                 "Max affectations/semaine: " + rs.getInt("nbr_max_aff") + "<br>" +
                                 "Total affectations: " + rs.getInt("total_affectations") +
                                 "</html>";

                JOptionPane.showMessageDialog(null, message);
            }

            rs.close();
            pst.close();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Erreur : " + ex.getMessage());
        }
    }
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        affectationTable = new javax.swing.JTable();
        exitButton = new javax.swing.JButton();
        deconnecterButton = new javax.swing.JButton();
        goBackButton = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setUndecorated(true);
        setResizable(false);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        affectationTable.setFont(new java.awt.Font("Segoe UI", 0, 36)); // NOI18N
        affectationTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null}
            },
            new String [] {
                "Id affectation", "Id enseignant", "Id salle", "Jour", "Heure deb", "Heure fin", "Nb étudiants max"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.Integer.class, java.lang.Integer.class, java.lang.String.class, java.lang.Integer.class, java.lang.Integer.class, java.lang.Integer.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        affectationTable.setRowHeight(90);
        affectationTable.setShowHorizontalLines(true);
        affectationTable.setShowVerticalLines(true);
        affectationTable.getTableHeader().setReorderingAllowed(false);
        jScrollPane1.setViewportView(affectationTable);

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

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/AffAffect.jpg"))); // NOI18N
        getContentPane().add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1280, 800));

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void exitButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_exitButtonActionPerformed
        System.exit(0);
    }//GEN-LAST:event_exitButtonActionPerformed

    private void deconnecterButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deconnecterButtonActionPerformed
        new Sign_in().setVisible(true);
        dispose();
    }//GEN-LAST:event_deconnecterButtonActionPerformed

    private void goBackButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_goBackButtonActionPerformed
        new user_interface(currentCin).setVisible(true);
        dispose();
    }//GEN-LAST:event_goBackButtonActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTable affectationTable;
    private javax.swing.JButton deconnecterButton;
    private javax.swing.JButton exitButton;
    private javax.swing.JButton goBackButton;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane1;
    // End of variables declaration//GEN-END:variables
}
