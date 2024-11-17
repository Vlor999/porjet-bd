BEGIN
   -- Création de la salle de vente
   INSERT INTO SALLE_DE_VENTE (IDSALLE, NOMCAT, DESCRIPTION)
   VALUES (:idSalle, :nomCat, :description);
   
   -- Création de la vente pour un produit
   INSERT INTO VENTE (IDVENTE, TYPEVENTE, PRIXDEPART, REVOCABLE, LIMITEOFFRES, DUREE, IDPRODUIT, IDSALLE)
   VALUES (:idVente, :typeVente, :prixDepart, :revocable, :limiteOffres, :duree, :idProduit, :idSalle);
EXCEPTION
   WHEN OTHERS THEN
      ROLLBACK;
      RAISE;
END;


DECLARE
   gagnantEmail VARCHAR(255);
   gagnantPrix INT;
   gagnantQuantite INT;
BEGIN
   -- Identifier le gagnant pour une vente descendante
   SELECT EMAIL, PRIXOFFRE, QUANTITE
   INTO gagnantEmail, gagnantPrix, gagnantQuantite
   FROM OFFRE
   WHERE IDVENTE = :idVente
   AND PRIXOFFRE >= :prixDepart
   ORDER BY DATEOFFRE ASC, HEUREOFFRE ASC -- de la plus ancienne à la plus récente
   FETCH FIRST 1 ROWS ONLY;
   
--    -- Clôture de la vente : passage à une vente descendante
--    UPDATE VENTE
--    SET TYPEVENTE = -1  -- Marquer la vente comme  clôturée (type à prendre en compte dans la table alors)
--    WHERE IDVENTE = :idVente;
END;
