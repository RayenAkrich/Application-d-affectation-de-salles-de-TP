CREATE TRIGGER after_salle_nom_update
AFTER UPDATE ON salle
FOR EACH ROW
BEGIN
    -- Vérifier si le nom a été modifié
    IF OLD.nom != NEW.nom THEN
        -- Mettre à jour les références dans la table affectation
        UPDATE affectation 
        SET id_salle = NEW.id_salle 
        WHERE id_salle = :OLD.id_salle;
        
        -- Mettre à jour les références dans la table maintenance
        UPDATE maintenance 
        SET num_salle = NEW.id_salle 
        WHERE num_salle = OLD.id_salle;
    END IF;
END//
