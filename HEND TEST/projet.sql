-- create
use salle_tp;
CREATE TABLE enseignant (
cin VARCHAR(8) PRIMARY KEY,
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
id_affect integer PRIMARY KEY AUTO_INCREMENT,
id_ens VARCHAR(8) ,
id_salle integer,
jour varchar(10),
heure_deb integer ,
heure_fin integer ,
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
INSERT INTO affectation (id_ens,id_salle,jour,heure_deb,heure_fin,nbre_etud) VALUES (1,2,'mercredi',8,10,15);
-- insert users
INSERT INTO users VALUES ('15328795','yacoub','hend','hend.yacoub@etudiant-fst.utm.tn','10100123','admin','2025-04-04');
INSERT INTO users VALUES ('06538247','hamam','mootaz','mootaz.hamam@etudiant-fst.utm.tn','10100145','admin','2025-04-04');
INSERT INTO users VALUES ('11542354','hajji','mohamed amine','amine.hajji@etudiant-fst.utm.tn','1010015458','admin','2025-04-04');
INSERT INTO users VALUES ('06587235','akrich','rayen','rayen.akrich@etudiant-fst.utm.tn','101005468','admin','2025-04-04');
INSERT INTO users VALUES ('12345678','normal','user','normaluser@etudiant-fst.utm.tn','123456789','user','2025-04-06');
