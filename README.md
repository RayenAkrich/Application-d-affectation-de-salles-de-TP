Projet universitaire en Java et PL/SQL pour la gestion des affectations de salles de cours : <br/>
CRUD pour les salles/enseignants, limites d'affectation hebdomadaires, jours de maintenance et disponibilité des salles.<br/>
Permet aux utilisateurs de consulter les plannings, l'utilisation des salles par enseignant, les salles libres <br/>
par jour et la salle la plus utilisée. Comprend une procédure pour vérifier la disponibilité des salles<br/>
par date, heure et nombre d'étudiants.<br/>
Cette application permet : <br/>
1. L’ajout, la modification ou la suppression de salles et d’enseignants.<br/>
2. La gestion d’affectations, avec un nombre d’affectations maximum (par semaine) et des jours de<br/>
maintenance et de nettoyage.<br/>
3. L’interrogation de la base de données pour afficher :<br/>
a. Les salles existantes et leurs affectations de la semaine.<br/>
b. Les salles dans lesquelles travaille un enseignant donné.<br/>
c. Les jours de la semaine où une salle est libre.<br/>
d. La salle la plus affectée.<br/>
4. La vérification la disponibilité des salles de travaux pratiques (TP)<br/>
pour une certaine date et heure donnée. Elle prendre en entrée la date et l'heure souhaitées<br/>
ainsi que le nombre d'étudiants. Ensuite, elle <br/> vérifie quelles salles sont disponibles pour<br/>
accueillir le nombre d'étudiants spécifié à cette date et heure. Enfin, elle renvoit la liste <br/>
des salles disponibles ou un message indiquant qu'aucune salle n'est disponible pour les critères donnés.<br/>
