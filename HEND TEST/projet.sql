-- Use the database
USE salle_tp;

-- Table definitions (unchanged)
CREATE TABLE enseignant (
    cin VARCHAR(8) PRIMARY KEY,
    nom VARCHAR(10),
    prenom VARCHAR(10),
    specialite VARCHAR(20),
    nbre_max_heure INTEGER
);

CREATE TABLE salle (
    id_salle INTEGER PRIMARY KEY AUTO_INCREMENT,
    nom VARCHAR(5) UNIQUE,
    capacite INTEGER,
    nbr_max_aff INTEGER
);

CREATE TABLE maintenance (
    id_maint INTEGER PRIMARY KEY AUTO_INCREMENT,
    jour VARCHAR(10),
    num_salle INTEGER,
    FOREIGN KEY (num_salle) REFERENCES salle(id_salle)
);

CREATE TABLE affectation (
    id_affect INTEGER PRIMARY KEY AUTO_INCREMENT,
    id_ens VARCHAR(8),
    id_salle INTEGER,
    jour VARCHAR(10),
    heure_deb TIME,
    heure_fin TIME,
    nbre_etud INTEGER,
    FOREIGN KEY (id_ens) REFERENCES enseignant(cin),
    FOREIGN KEY (id_salle) REFERENCES salle(id_salle)
);

CREATE TABLE users (
    cin VARCHAR(8) PRIMARY KEY,
    nom VARCHAR(20),
    prenom VARCHAR(20),
    email VARCHAR(100) UNIQUE,
    mot_de_passe VARCHAR(255),
    role ENUM('admin', 'user') NOT NULL,
    date_inscription DATETIME DEFAULT CURRENT_TIMESTAMP
);

-- Inserting data into enseignant
INSERT INTO enseignant VALUES 
    ('09876543', 'Dupont', 'Émilie', 'Physique', 9),
    ('11223344', 'Martin', 'Luc', 'Chimie', 7),
    ('02345678', 'Leroy', 'Sophie', 'Biologie', 10),
    ('14567892', 'Garcia', 'Carlos', 'Informatique', 8),
    ('08765432', 'Dubois', 'Annie', 'Mathématiques', 12),
    ('13245768', 'Karam', 'Nour', 'Électronique', 6),
    ('01234587', 'O''Connor', 'Sean', 'Mécanique', 11),
    ('18765439', 'Al-Farsi', 'Layla', 'Thermodynamique', 9),
    ('07654321', 'Van-Der', 'Maarten', 'Optique', 10),
    ('15432678', 'Ben Ahmed', 'Youssef', 'Génie Civil', 7);

-- Inserting data into salle
INSERT INTO salle VALUES 
    (5, 'S3', 35, 25),
    (6, 'S4', 40, 30),
    (7, 'SE5A', 50, 35),
    (8, 'LAB1', 20, 15),
    (9, 'AMPHI', 100, 80),
    (10, 'S6', 30, 20),
    (11, 'SE7C', 45, 30),
    (12, 'LAB2', 25, 18),
    (13, 'VIDEO', 15, 10),
    (14, 'S8', 60, 45);

-- Inserting data into maintenance  
INSERT INTO maintenance (jour, num_salle) VALUES 
    ('lundi', 5),
    ('mardi', 6),
    ('mercredi', 7),
    ('jeudi', 8),
    ('vendredi', 9),
    ('lundi', 10),
    ('mardi', 11),
    ('mercredi', 12),
    ('jeudi', 13),
    ('vendredi', 14);

-- Modified affectation examples
INSERT INTO affectation (id_ens, id_salle, jour, heure_deb, heure_fin, nbre_etud) VALUES 
    -- For salle 5 (maintenance day: lundi), assign on mardi.
    ('09876543', 5, 'mardi', '08:00:00', '10:00:00', 30),
    -- For salle 6 (maintenance day: mardi), assign on mercredi.
    ('11223344', 6, 'mercredi', '10:00:00', '12:00:00', 25),
    -- For salle 7 (maintenance day: mercredi), assign on jeudi.
    ('02345678', 7, 'jeudi', '13:00:00', '15:00:00', 40),
    -- For salle 8 (maintenance day: jeudi), assign on lundi.
    ('14567892', 8, 'lundi', '09:00:00', '11:00:00', 12),
    -- For salle 9 (maintenance day: vendredi), assign on mardi.
    ('08765432', 9, 'mardi', '14:00:00', '16:00:00', 70),
    -- For salle 10 (maintenance day: lundi), assign on mercredi.
    ('13245768', 10, 'mercredi', '08:00:00', '11:00:00', 18),
    -- For salle 11 (maintenance day: mardi), assign on mercredi.
    ('01234587', 11, 'mercredi', '10:00:00', '12:00:00', 35),
    -- For salle 12 (maintenance day: mercredi), assign on jeudi.
    ('18765439', 12, 'jeudi', '15:00:00', '17:00:00', 15),
    -- For salle 13 (maintenance day: jeudi), assign on mardi.
    ('07654321', 13, 'mardi', '08:00:00', '10:00:00', 8),
    -- For salle 14 (maintenance day: vendredi), assign on jeudi.
    ('15432678', 14, 'jeudi', '13:00:00', '15:00:00', 40),
    -- Additional affectations for salle 9 on non‐maintenance days:
    ('08765432', 9, 'lundi', '10:00:00', '12:00:00', 65),
    ('15432678', 9, 'mercredi', '08:00:00', '10:00:00', 75),
    ('09876543', 9, 'jeudi', '14:00:00', '16:00:00', 70),
    -- Additional affectation for salle 5 (maintenance day: lundi) on jeudi.
    ('11223344', 5, 'jeudi', '14:00:00', '16:00:00', 30),
    -- For salle 7 (maintenance day: mercredi), assign on lundi.
    ('02345678', 7, 'lundi', '10:00:00', '12:00:00', 45),
    -- Another affectation for salle 9 on a safe day.
    ('13245768', 9, 'mercredi', '08:00:00', '10:00:00', 60),
    -- For salle 11 (maintenance day: mardi), assign on jeudi.
    ('01234587', 11, 'jeudi', '13:00:00', '15:00:00', 30),
    -- Additional affectation for salle 9 on a safe day.
    ('18765439', 9, 'lundi', '09:00:00', '11:00:00', 80),
    -- For salle 13, we keep the original day since mardi does not conflict with its maintenance (jeudi).
    ('07654321', 13, 'mardi', '15:00:00', '17:00:00', 10),
    -- One more affectation for salle 9 on a safe day.
    ('14567892', 9, 'mercredi', '10:00:00', '12:00:00', 68);

-- Inserting data into users (unchanged)
INSERT INTO users (cin, nom, prenom, email, mot_de_passe, role) VALUES 
    ('09871234', 'yacoub', 'hend', 'hend.yacoub@etudiant-fst.utm.tn', 'SecurePass123', 'admin'),
    ('11234567', 'hamam', 'mootaz', 'mootaz.hamam@etudiant-fst.utm.tn', '10100145', 'admin'),
    ('02349876', 'hajji', 'mohamed amine', 'amine.hajji@etudiant-fst.utm.tn', 'Vietnam2024', 'admin'),
    ('13456789', 'akriche', 'rayen', 'rayen.akrich@etudiant-fst.utm.tn', 'JS*456789', 'admin'),
    ('08765432', 'Gómez', 'Ana', 'ana.gomez@example.com', 'Fl0w3rP0w3r', 'user'),
    ('14567987', 'Kim', 'Min-ji', 'minji.k@example.com', 'K0re@2023', 'user'),
    ('01237654', 'Müller', 'Hans', 'h.muller@example.com', 'B3rlin2025', 'user'),
    ('18765439', 'Ivanova', 'Svetlana', 's.ivanova@example.com', 'Sib3ria!', 'user'),
    ('07654328', 'Dubois', 'Pierre', 'p.dubois@example.com', 'Paris1234', 'user'),
    ('15432679', 'Rossi', 'Giulia', 'g.rossi@example.com', 'R0ma2024#', 'user');

select * from users;
select * from enseignant;
select * from maintenance;
select * from salle;
select * from affectation;
