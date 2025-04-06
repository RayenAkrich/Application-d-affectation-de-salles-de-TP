-- create
CREATE TABLE enseignant (
cin integer PRIMARY KEY,
nom varchar(10),
prenom varchar(10),
specialite varchar(20),
nbre_max integer
);
CREATE TABLE salle (
id_salle integer PRIMARY KEY,
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
id_affect integer PRIMARY KEY,
id_ens integer ,
id_salle integer,
jour varchar(10),
heure_deb integer ,
heure_fin integer ,
nbre_etud integer,
FOREIGN KEY (id_ens) REFERENCES enseignant(cin)
FOREIGN KEY (id_salle) REFERENCES salle(id_salle)
);
CREATE TABLE user (
cin integer PRIMARY KEY,
nom VARCHAR(20),
prenom VARCHAR(20),
email VARCHAR(100) UNIQUE,
mot_de_passe VARCHAR(255),
role ENUM('admin', 'user') NOT NULL,
date_inscription DATETIME DEFAULT CURRENT_TIMESTAMP
);


-- insert enseignant
INSERT INTO enseignant VALUES (1, 'hamam', 'maha','maths',8);
INSERT INTO enseignant VALUES (2, 'aisaoui', 'karim','electrostatique',10);
INSERT INTO enseignant VALUES (3, 'ayari', 'mariem','electronique',10);
INSERT INTO enseignant VALUES (4,'fathalah','imen','programmation',9);
INSERT INTO enseignant VALUES (5,'hajji','fethi','français',7);
-- insert salle
INSERT INTO salle VALUES (1,'S1',30,20);
INSERT INTO salle VALUES (2,'S2',40,15);
INSERT INTO salle VALUES (3,'SE2B',45,20);
INSERT INTO salle VALUES (4,'SE4B',40,18);
-- insert maintenance
INSERT INTO maintenance (jour, num_salle) VALUES ('lundi',1);
INSERT INTO maintenance (jour, num_salle) VALUES ('mardi',1);
INSERT INTO maintenance (jour, num_salle) VALUES ('mercredi',2);
INSERT INTO maintenance (jour, num_salle) VALUES ('jeudi',3);
INSERT INTO maintenance (jour, num_salle) VALUES ('vendredi',3);

-- insert affectation 
INSERT INTO affectation VALUES (1,1,2,'mercredi',8,10,30);
-- insert users
INSERT INTO user VALUES (1,'yacoub','hend','hend.yacoub@etudiant-fst.utm.tn','10100123','admin','04-04-2025');
INSERT INTO user VALUES (2,'hamam','mootaz','mootaz.hamam@etudiant-fst.utm.tn','10100145','admin','04-04-2025');
INSERT INTO user VALUES (3,'hajji','mohamed amine','amine.hajji@etudiant-fst.utm.tn','1010015458','admin','04-04-2025');
INSERT INTO user VALUES (4,'akrich','rayen','rayen.akrich@etudiant-fst.utm.tn','101005468','admin','04-04-2025');




