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

/*dans le code java , vérifier bien si une offre faite est égale (ou supérieure) au prix actuel de la vente et, 
 *Ainsi le gagnant sera simplement le premier à faire une offre valide */


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
   FETCH FIRST 1 ROWS ONLY;-- sélectionner le premier à avoir fait une offre valide.
   
--    -- Clôture de la vente :
--    UPDATE VENTE
--    SET TYPEVENTE = -1  -- Marquer la vente comme  clôturée (type à prendre en compte dans la table alors)
--    WHERE IDVENTE = :idVente;
END;
