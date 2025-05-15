package main;
import java.awt.Dimension;
import java.awt.Font;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import java.sql.Statement;
import javax.swing.JFrame;
import javax.swing.table.JTableHeader;

public class gerEnseignant extends javax.swing.JFrame {
    
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
        
    public gerEnseignant(String Cin) {
        initComponents();
        // Configure table header
        JTableHeader header = EnsTable.getTableHeader();
        header.setPreferredSize(new Dimension(header.getWidth(), 50)); // Set header height (e.g., 50px)
        header.setFont(new Font("Segoe UI", Font.BOLD, 24)); // Larger font
        loadEnseignants();
        this.currentCin = Cin;
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        EnsTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                if (EnsTable.getSelectedRow() != -1) {
                    // Mode ÉDITION
                    supp.setEnabled(true);
                    edit.setEnabled(true);
                    int selectedRow = EnsTable.getSelectedRow();
                    cin1.setText(EnsTable.getValueAt(selectedRow, 0).toString());
                    nom1.setText(EnsTable.getValueAt(selectedRow, 1).toString());
                    prenom1.setText(EnsTable.getValueAt(selectedRow, 2).toString());
                    specialite1.setText(EnsTable.getValueAt(selectedRow, 3).toString());
                    nbremax1.setText(EnsTable.getValueAt(selectedRow, 4).toString());
                    cin1.setEditable(false); // CIN verrouillé
                } else {
                    // Mode ajout
                    cin1.setEditable(true); // CIN modifiable
                    cin1.setText("");
                    nom1.setText("");
                    prenom1.setText("");
                    specialite1.setText("");
                    nbremax1.setText("");
                }
            }
        });
    }

    private void loadEnseignants() {
        DefaultTableModel model = (DefaultTableModel) EnsTable.getModel();
        model.setRowCount(0); // Clear table first

        try {
            connect();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM enseignant");

            while (rs.next()) {
                model.addRow(new Object[]{
                        rs.getString("cin"),
                        rs.getString("nom"),
                        rs.getString("prenom"),
                        rs.getString("specialite"),
                        rs.getInt("nbre_max_heure")
                });
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Erreur de chargement des enseignants.");
        }

        // Désactive les boutons tant qu'aucune ligne n'est sélectionnée
        supp.setEnabled(false);
        edit.setEnabled(false);
    }

    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        exitButton = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        specialite1 = new javax.swing.JTextField();
        prenom1 = new javax.swing.JTextField();
        nom1 = new javax.swing.JTextField();
        cin1 = new javax.swing.JTextField();
        edit = new javax.swing.JButton();
        add = new javax.swing.JButton();
        nbremax1 = new javax.swing.JTextField();
        supp = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        EnsTable = new javax.swing.JTable();
        resetButton = new javax.swing.JButton();
        deconnecterButton = new javax.swing.JButton();
        goBackButton = new javax.swing.JButton();
        Background = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setUndecorated(true);
        setResizable(false);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        exitButton.setForeground(new java.awt.Color(255, 255, 255));
        exitButton.setToolTipText("");
        exitButton.setBorder(null);
        exitButton.setBorderPainted(false);
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

        jPanel1.setOpaque(false);
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        specialite1.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jPanel1.add(specialite1, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 300, 240, 30));

        prenom1.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jPanel1.add(prenom1, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 250, 240, 30));

        nom1.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jPanel1.add(nom1, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 200, 240, 30));

        cin1.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        cin1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cin1ActionPerformed(evt);
            }
        });
        jPanel1.add(cin1, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 150, 240, 30));

        edit.setFont(new java.awt.Font("Segoe UI", 1, 21)); // NOI18N
        edit.setForeground(new java.awt.Color(255, 255, 255));
        edit.setToolTipText("");
        edit.setBorderPainted(false);
        edit.setContentAreaFilled(false);
        edit.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        edit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                editActionPerformed(evt);
            }
        });
        jPanel1.add(edit, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 60, 140, 70));

        add.setFont(new java.awt.Font("Segoe UI", 1, 21)); // NOI18N
        add.setForeground(new java.awt.Color(255, 255, 255));
        add.setToolTipText("");
        add.setBorderPainted(false);
        add.setContentAreaFilled(false);
        add.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        add.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addActionPerformed(evt);
            }
        });
        jPanel1.add(add, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 60, 150, 70));

        nbremax1.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jPanel1.add(nbremax1, new org.netbeans.lib.awtextra.AbsoluteConstraints(280, 342, 100, 30));

        getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 120, 410, 430));

        supp.setFont(new java.awt.Font("Segoe UI", 1, 21)); // NOI18N
        supp.setForeground(new java.awt.Color(255, 255, 255));
        supp.setToolTipText("");
        supp.setBorderPainted(false);
        supp.setContentAreaFilled(false);
        supp.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        supp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                suppActionPerformed(evt);
            }
        });
        getContentPane().add(supp, new org.netbeans.lib.awtextra.AbsoluteConstraints(770, 583, 190, 60));

        EnsTable.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        EnsTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "CIN", "Nom", "Prénom", "Spécialité", "HeuresMax/Semaine"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class
            };
            boolean[] canEdit = new boolean [] {
                false, true, true, true, true
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        EnsTable.setRowHeight(90);
        EnsTable.getTableHeader().setReorderingAllowed(false);
        jScrollPane1.setViewportView(EnsTable);

        getContentPane().add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(490, 140, 710, 420));

        resetButton.setFont(new java.awt.Font("Segoe UI", 1, 21)); // NOI18N
        resetButton.setForeground(new java.awt.Color(255, 255, 255));
        resetButton.setToolTipText("");
        resetButton.setBorderPainted(false);
        resetButton.setContentAreaFilled(false);
        resetButton.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        resetButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                resetButtonActionPerformed(evt);
            }
        });
        getContentPane().add(resetButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(1000, 583, 180, 60));

        deconnecterButton.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        deconnecterButton.setForeground(new java.awt.Color(255, 255, 255));
        deconnecterButton.setToolTipText("");
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

        Background.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/GererEnseignant.jpg"))); // NOI18N
        getContentPane().add(Background, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1280, 800));

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void addActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addActionPerformed
                                  
        try {
        String cinStr = cin1.getText().trim();
        String nom = nom1.getText().trim();
        String prenom = prenom1.getText().trim();
        String specialite = specialite1.getText().trim();
        String nbreMaxStr = nbremax1.getText().trim();

        // 1. CIN must be exactly 8 digits and start with 0 or 1.
        if (!cinStr.matches("^[01]\\d{7}$")) {
            JOptionPane.showMessageDialog(this, "CIN must be exactly 8 digits and start with 0 or 1.");
            return;
        }

        // 2. NOM must contain only alphabetic letters.
        if (!nom.matches("^[A-Za-z\\s\\-']+$")) {
            JOptionPane.showMessageDialog(this, "Nom must contain only alphabetic letters.");
            return;
        }

        // 3. PRENOM must contain only alphabetic letters.
        if (!prenom.matches("^[A-Za-z\\s\\-']+$")) {
            JOptionPane.showMessageDialog(this, "Prenom must contain only alphabetic letters.");
            return;
        }

        // 4. nbreMax doit être un entier positif
        int nbreMax;
        try {
            nbreMax = Integer.parseInt(nbreMaxStr);
            if (nbreMax <= 0) {
                JOptionPane.showMessageDialog(this, "Le nombre d'heure maximal doit être supérieur à zéro.");
                return;
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Le nombre maximal d'heure doit être un nombre entier.");
            return;
        }

        try {
            connect();
            if (conn == null) {
                JOptionPane.showMessageDialog(this, "Connexion à la base de données échouée.");
                return;
            }

            // Vérifie si un enseignant avec ce CIN existe déjà
            String checkQuery = "SELECT * FROM enseignant WHERE cin = ?";
            PreparedStatement checkStmt = conn.prepareStatement(checkQuery);
            checkStmt.setString(1, cinStr);
            ResultSet rs = checkStmt.executeQuery();
            if (rs.next()) {
                JOptionPane.showMessageDialog(this, "Un enseignant avec ce CIN existe déjà.");
                return;
            }

            // Insertion
            String sql = "INSERT INTO enseignant (cin, nom, prenom, specialite, nbre_max_heure) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, cinStr);
            pstmt.setString(2, nom);
            pstmt.setString(3, prenom);
            pstmt.setString(4, specialite);
            pstmt.setInt(5, nbreMax);

            pstmt.executeUpdate();
            JOptionPane.showMessageDialog(this, "Enseignant ajouté avec succès !");
            loadEnseignants();
            cin1.setText("");
            nom1.setText("");
            prenom1.setText("");
            specialite1.setText("");
            nbremax1.setText("");
            cin1.setEditable(true); // Restaurer l'édition du CIN

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Erreur SQL : " + e.getMessage());
            e.printStackTrace();
        }

    } catch (NumberFormatException ex) {
        JOptionPane.showMessageDialog(this, "Erreur : Veuillez vérifier les champs numériques.");
    }
    }//GEN-LAST:event_addActionPerformed

    private void editActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_editActionPerformed
// TODO add your handling code here:
int selectedRow = EnsTable.getSelectedRow();

if (selectedRow != -1) {
    String cinStr = cin1.getText().trim();
    String nom = nom1.getText().trim();
    String prenom = prenom1.getText().trim();
    String specialite = specialite1.getText().trim();
    String nbreMaxStr = nbremax1.getText().trim();

    // 1. CIN must be exactly 8 digits and start with 0 or 1.
    if (!cinStr.matches("^[01]\\d{7}$")) {
        JOptionPane.showMessageDialog(this, "CIN must be exactly 8 digits and start with 0 or 1.");
        return;
    }

    // 2. NOM must contain only alphabetic letters.
    if (!nom.matches("^[A-Za-z\\s\\-']+$")) {
        JOptionPane.showMessageDialog(this, "Nom must contain only alphabetic letters.");
        return;
    }

    // 3. PRENOM must contain only alphabetic letters.
    if (!prenom.matches("^[A-Za-z\\s\\-']+$")) {
        JOptionPane.showMessageDialog(this, "Prenom must contain only alphabetic letters.");
        return;
    }

    // 4. nbreMax doit être un entier positif
    int nbreMax;
    try {
        nbreMax = Integer.parseInt(nbreMaxStr);
        if (nbreMax <= 0) {
            JOptionPane.showMessageDialog(this, "Le nombre maximal doit être supérieur à zéro.");
            return;
        }
    } catch (NumberFormatException e) {
        JOptionPane.showMessageDialog(this, "Le nombre maximal doit être un nombre entier.");
        return;
    }

    try {
            connect();
            String sql = "UPDATE enseignant SET nom = ?, prenom = ?, specialite = ?, nbre_max_heure = ? WHERE cin = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, nom);
            pstmt.setString(2, prenom);
            pstmt.setString(3, specialite);
            pstmt.setInt(4, nbreMax);
            pstmt.setString(5, cinStr);

            int rowsAffected = pstmt.executeUpdate();

            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(this, "Enseignant de CIN: "+cinStr +" modifié avec succès !");
                loadEnseignants(); // recharge le tableau
            } else {
                JOptionPane.showMessageDialog(this, "Aucune modification effectuée.");
            }
        

    } catch (SQLException e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(this, "Erreur lors de la mise à jour.");
    }

} else {
    JOptionPane.showMessageDialog(this, "Veuillez sélectionner un enseignant à modifier.");
}

    }//GEN-LAST:event_editActionPerformed

    private void suppActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_suppActionPerformed
    int selectedRow = EnsTable.getSelectedRow();
    if (selectedRow != -1) {
        int confirm = JOptionPane.showConfirmDialog(this, "Voulez-vous vraiment supprimer cet enseignant ?", "Confirmation", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            String cin = cin1.getText().trim();
            try {
                connect();
                conn.setAutoCommit(false); // Désactive l'auto-commit

                // 1. Supprime les affectations liées
                String deleteAffectationQuery = "DELETE FROM Affectation WHERE id_ens = ?";
                try (PreparedStatement pstAffectation = conn.prepareStatement(deleteAffectationQuery)) {
                    pstAffectation.setString(1, cin);
                    pstAffectation.executeUpdate();
                }

                // 2. Supprime l'enseignant
                String deleteEnseignantQuery = "DELETE FROM Enseignant WHERE cin = ?";
                try (PreparedStatement pstEnseignant = conn.prepareStatement(deleteEnseignantQuery)) {
                    pstEnseignant.setString(1, cin);
                    int rowsDeleted = pstEnseignant.executeUpdate();
                    if (rowsDeleted > 0) {
                        conn.commit(); // Valide la transaction
                        JOptionPane.showMessageDialog(this, "Enseignant supprimé !");
                        loadEnseignants(); // Recharge la table
                    }
                }
            } catch (SQLException e) {
                try {
                    conn.rollback(); // Annule en cas d'erreur
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
                JOptionPane.showMessageDialog(this, "Erreur : " + e.getMessage());
            } finally {
                try {
                    conn.setAutoCommit(true); // Réactive l'auto-commit
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        }
    } else {
        JOptionPane.showMessageDialog(this, "Veuillez sélectionner un enseignant.");
    }
    }//GEN-LAST:event_suppActionPerformed

    private void cin1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cin1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cin1ActionPerformed

    private void resetButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_resetButtonActionPerformed
        // Réinitialise le formulaire
        EnsTable.clearSelection();
        cin1.setText("");
        nom1.setText("");
        prenom1.setText("");
        specialite1.setText("");
        nbremax1.setText("");
        cin1.setEditable(true);
        supp.setEnabled(false);
        edit.setEnabled(false);
    }//GEN-LAST:event_resetButtonActionPerformed

    private void exitButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_exitButtonActionPerformed
        System.exit(0);
    }//GEN-LAST:event_exitButtonActionPerformed

    private void deconnecterButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deconnecterButtonActionPerformed
        new Sign_in().setVisible(true);
        dispose();
    }//GEN-LAST:event_deconnecterButtonActionPerformed

    private void goBackButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_goBackButtonActionPerformed
        new admin_interface(currentCin).setVisible(true);
        dispose();
    }//GEN-LAST:event_goBackButtonActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel Background;
    private javax.swing.JTable EnsTable;
    private javax.swing.JButton add;
    private javax.swing.JTextField cin1;
    private javax.swing.JButton deconnecterButton;
    private javax.swing.JButton edit;
    private javax.swing.JButton exitButton;
    private javax.swing.JButton goBackButton;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextField nbremax1;
    private javax.swing.JTextField nom1;
    private javax.swing.JTextField prenom1;
    private javax.swing.JButton resetButton;
    private javax.swing.JTextField specialite1;
    private javax.swing.JButton supp;
    // End of variables declaration//GEN-END:variables
}
