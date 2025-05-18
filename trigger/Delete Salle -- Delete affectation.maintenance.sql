-- Ajouter ceci après toutes les créations de tables et insertions
DELIMITER //

CREATE TRIGGER before_delete_salle
BEFORE DELETE ON salle
FOR EACH ROW
BEGIN
    -- Supprimer toutes les affectations liées à cette salle
    DELETE FROM affectation WHERE id_salle = OLD.id_salle;
    
    -- Supprimer toutes les maintenances liées à cette salle
    DELETE FROM maintenance WHERE num_salle = OLD.id_salle;
END//

DELIMITER ;