-- create
use salle_tp;
CREATE TABLE enseignant (
cin VARCHAR(8) PRIMARY KEY,
nom varchar(10),
prenom varchar(10),
specialite varchar(20),
nbre_max_heure integer
);
CREATE TABLE salle (
id_salle integer PRIMARY KEY AUTO_INCREMENT,
nom varchar(5) unique,
capacite integer,
nbr_max_aff integer
);
CREATE TABLE maintenance (
id_maint integer PRIMARY KEY AUTO_INCREMENT,
jour varchar(10),
num_salle integer,
FOREIGN KEY (num_salle) REFERENCES salle(id_salle)
);
CREATE TABLE affectation(
id_affect integer PRIMARY KEY AUTO_INCREMENT,
id_ens VARCHAR(8) ,
id_salle integer,
jour varchar(10),
heure_deb TIME ,
heure_fin TIME ,
nbre_etud integer,
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


-- insert enseignant
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
-- insert salle
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
-- insert maintenance
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

-- insert affectation 
INSERT INTO affectation (id_ens, id_salle, jour, heure_deb, heure_fin, nbre_etud) VALUES 
('09876543', 5, 'lundi', '08:00:00', '10:00:00', 30),
('11223344', 6, 'mardi', '10:00:00', '12:00:00', 25),
('02345678', 7, 'mercredi', '13:00:00', '15:00:00', 40),
('14567892', 8, 'jeudi', '09:00:00', '11:00:00', 12),
('08765432', 9, 'vendredi', '14:00:00', '16:00:00', 70),
('13245768', 10, 'lundi', '08:00:00', '11:00:00', 18),
('01234587', 11, 'mardi', '10:00:00', '12:00:00', 35),
('18765439', 12, 'mercredi', '15:00:00', '17:00:00', 15),
('07654321', 13, 'jeudi', '08:00:00', '10:00:00', 8),
('15432678', 14, 'vendredi', '13:00:00', '15:00:00', 40),
('08765432', 9, 'lundi', '10:00:00', '12:00:00', 65),
('15432678', 9, 'mardi', '08:00:00', '10:00:00', 75),
('09876543', 9, 'mercredi', '14:00:00', '16:00:00', 70),
('11223344', 5, 'jeudi', '14:00:00', '16:00:00', 30), 
('02345678', 7, 'vendredi', '10:00:00', '12:00:00', 45),
('13245768', 9, 'jeudi', '08:00:00', '10:00:00', 60),   
('01234587', 11, 'lundi', '13:00:00', '15:00:00', 30), 
('18765439', 9, 'vendredi', '09:00:00', '11:00:00', 80), 
('07654321', 13, 'mardi', '15:00:00', '17:00:00', 10),
('14567892', 9, 'mercredi', '10:00:00', '12:00:00', 68);
-- insert users
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
select * from affectation ;
select * from maintenance;
select * from salle;
select * from enseignant;