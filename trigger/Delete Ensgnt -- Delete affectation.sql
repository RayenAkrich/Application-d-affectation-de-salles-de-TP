-- Ajouter ceci après toutes les créations de tables et insertions
DELIMITER //

CREATE TRIGGER before_delete_enseignant
BEFORE DELETE ON enseignant
FOR EACH ROW
BEGIN
    -- Supprimer toutes les affectations liées à cet enseignant
    DELETE FROM affectation WHERE id_ens = OLD.cin;
END//

DELIMITER ;