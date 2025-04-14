-- Création de la base de données
CREATE DATABASE IF NOT EXISTS salle_tp;
USE salle_tp;

-- Supprime la table `enseignant` si elle existe déjà
DROP TABLE IF EXISTS `enseignant`;
-- Crée une nouvelle table `enseignant`
CREATE TABLE `enseignant` (
    -- Colonne pour le CIN de l'enseignant (8 caractères)
    `cin` VARCHAR(8) NOT NULL,
    -- Colonne pour le nom de l'enseignant, ne peut pas être vide
    `nom` VARCHAR(10) NOT NULL,
    -- Colonne pour le prénom de l'enseignant, ne peut pas être vide
    `prenom` VARCHAR(10) NOT NULL,
    -- Colonne pour la spécialité de l'enseignant
    `specialite` VARCHAR(20),
    -- Colonne pour le nombre maximum d'heures d'enseignement par semaine
    `nbre_max_heure` INTEGER,
    -- Définit la clé primaire de la table sur la colonne `cin`
    PRIMARY KEY (`cin`)
);

-- Supprime la table `salle` si elle existe déjà
DROP TABLE IF EXISTS `salle`;
-- Crée une nouvelle table `salle`
CREATE TABLE `salle` (
    -- Colonne pour l'ID de la salle, auto-incrémentée
    `id_salle` INTEGER NOT NULL AUTO_INCREMENT,
    -- Colonne pour le nom de la salle (5 caractères max), doit être unique
    `nom` VARCHAR(5) UNIQUE,
    -- Colonne pour la capacité d'accueil de la salle
    `capacite` INTEGER,
    -- Colonne pour le nombre maximum d'affectations par semaine
    `nbr_max_aff` INTEGER,
    -- Définit la clé primaire de la table sur la colonne `id_salle`
    PRIMARY KEY (`id_salle`)
);

-- Supprime la table `maintenance` si elle existe déjà
DROP TABLE IF EXISTS `maintenance`;
-- Crée une nouvelle table `maintenance`
CREATE TABLE `maintenance` (
    -- Colonne pour l'ID de maintenance, auto-incrémentée
    `id_maint` INTEGER NOT NULL AUTO_INCREMENT,
    -- Colonne pour le jour de maintenance (ex: "Lundi", "Mardi")
    `jour` VARCHAR(10),
    -- Colonne pour le numéro de salle en maintenance
    `num_salle` INTEGER,
    -- Définit la clé primaire de la table sur la colonne `id_maint`
    PRIMARY KEY (`id_maint`),
    -- Crée un index sur la colonne `num_salle`
    KEY `num_salle_idx` (`num_salle`),
    -- Définit une contrainte de clé étrangère vers la table `salle`
    CONSTRAINT `num_salle` FOREIGN KEY (`num_salle`) 
    REFERENCES `salle` (`id_salle`)
);

-- Supprime la table `affectation` si elle existe déjà
DROP TABLE IF EXISTS `affectation`;
-- Crée une nouvelle table `affectation`
CREATE TABLE `affectation` (
    -- Colonne pour l'ID d'affectation, auto-incrémentée
    `id_affect` INTEGER NOT NULL AUTO_INCREMENT,
    -- Colonne pour le CIN de l'enseignant affecté
    `id_ens` VARCHAR(8),
    -- Colonne pour l'ID de la salle affectée
    `id_salle` INTEGER,
    -- Colonne pour le jour de l'affectation (ex: "Lundi")
    `jour` VARCHAR(10),
    -- Colonne pour l'heure de début de la séance
    `heure_deb` TIME,
    -- Colonne pour l'heure de fin de la séance
    `heure_fin` TIME,
    -- Colonne pour le nombre d'étudiants prévus
    `nbre_etud` INTEGER,
    -- Définit la clé primaire de la table sur la colonne `id_affect`
    PRIMARY KEY (`id_affect`),
    -- Crée un index sur la colonne `id_ens`
    KEY `id_ens_idx` (`id_ens`),
    -- Crée un index sur la colonne `id_salle`
    KEY `id_salle_idx` (`id_salle`),
    -- Contrainte de clé étrangère vers la table `enseignant`
    CONSTRAINT `id_ens` FOREIGN KEY (`id_ens`) 
    REFERENCES `enseignant` (`cin`),
    -- Contrainte de clé étrangère vers la table `salle`
    CONSTRAINT `id_salle` FOREIGN KEY (`id_salle`) 
    REFERENCES `salle` (`id_salle`)
);

-- Supprime la table `users` si elle existe déjà
DROP TABLE IF EXISTS `users`;
-- Crée une nouvelle table `users`
CREATE TABLE `users` (
    -- Colonne pour le CIN de l'utilisateur (8 caractères)
    `cin` VARCHAR(8) NOT NULL,
    -- Colonne pour le nom de l'utilisateur
    `nom` VARCHAR(20),
    -- Colonne pour le prénom de l'utilisateur
    `prenom` VARCHAR(20),
    -- Colonne pour l'email de l'utilisateur, doit être unique
    `email` VARCHAR(100) UNIQUE,
    -- Colonne pour le mot de passe (stocké en hash)
    `mot_de_passe` VARCHAR(255),
    -- Colonne pour le rôle (admin ou user)
    `role` ENUM('admin','user') NOT NULL,
    -- Colonne pour la date d'inscription (remplie automatiquement)
    `date_inscription` DATETIME DEFAULT CURRENT_TIMESTAMP,
    -- Définit la clé primaire de la table sur la colonne `cin`
    PRIMARY KEY (`cin`)
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

-- Insertion des exemples
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

INSERT INTO affectation (id_ens, id_salle, jour, heure_deb, heure_fin, nbre_etud) VALUES 
    ('09876543', 5, 'mardi', '08:00:00', '10:00:00', 30),
    ('11223344', 6, 'mercredi', '10:00:00', '12:00:00', 25),
    ('02345678', 7, 'jeudi', '13:00:00', '15:00:00', 40),
    ('14567892', 8, 'lundi', '09:00:00', '11:00:00', 12),
    ('08765432', 9, 'mardi', '14:00:00', '16:00:00', 70),
    ('13245768', 10, 'mercredi', '08:00:00', '11:00:00', 18),
    ('01234587', 11, 'mercredi', '10:00:00', '12:00:00', 35),
    ('18765439', 12, 'jeudi', '15:00:00', '17:00:00', 15),
    ('07654321', 13, 'mardi', '08:00:00', '10:00:00', 8),
    ('15432678', 14, 'jeudi', '13:00:00', '15:00:00', 40),
    ('08765432', 9, 'lundi', '10:00:00', '12:00:00', 65),
    ('15432678', 9, 'mercredi', '08:00:00', '10:00:00', 75),
    ('09876543', 9, 'jeudi', '14:00:00', '16:00:00', 70),
    ('11223344', 5, 'jeudi', '14:00:00', '16:00:00', 30),
    ('02345678', 7, 'lundi', '10:00:00', '12:00:00', 45),
    ('13245768', 9, 'mercredi', '08:00:00', '10:00:00', 60),
    ('01234587', 11, 'jeudi', '13:00:00', '15:00:00', 30),
    ('18765439', 9, 'lundi', '09:00:00', '11:00:00', 80),
    ('07654321', 13, 'mardi', '15:00:00', '17:00:00', 10),
    ('14567892', 9, 'mercredi', '10:00:00', '12:00:00', 68);

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
