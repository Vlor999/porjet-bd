BEGIN TRANSACTION;

DECLARE
    v_id_vente NUMBER;
BEGIN
    -- 1. Récupérer l'ID de la vente associée au produit
    SELECT IdVente INTO v_id_vente
    FROM Vente
    WHERE IdProduit = :id_produit;

    -- 2. Insérer l'offre
    INSERT INTO Offre (PrixOffre, DateOffre, HeureOffre, Quantite, Email, IdVente, IdProduit)
    VALUES (:montant_offre, CURRENT_DATE, CURRENT_TIMESTAMP, :quantite, :email_utilisateur, v_id_vente, :id_produit);

    COMMIT;
EXCEPTION
    WHEN OTHERS THEN
        ROLLBACK;
        RAISE;
END;
/