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
   meilleurPrix INT;
BEGIN
   -- Récupérer le meilleur prix actuel
   SELECT MAX(PRIXOFFRE)
   INTO meilleurPrix
   FROM OFFRE
   WHERE IDVENTE = :idVente;

   -- Vérifier si l'offre est supérieure au meilleur prix actuel
   IF :prixOffre > meilleurPrix AND SYSDATE < (:heureDepart + INTERVAL :duree MINUTE) THEN
      INSERT INTO OFFRE (IDOFFRE, EMAIL, IDVENTE, PRIXOFFRE, DATEOFFRE, HEUREOFFRE, QUANTITE)
      VALUES (:idOffre, :email, :idVente, :prixOffre, SYSDATE, SYSTIMESTAMP, :quantite);
--    ELSE
--       RAISE_APPLICATION_ERROR(-20001, 'L''offre doit être supérieure au meilleur prix actuel.');
   END IF;
END;

DECLARE
   gagnantEmail VARCHAR(255);
   gagnantPrix INT;
   gagnantQuantite INT;
BEGIN
   -- Identifier le gagnant pour une vente montante
   SELECT EMAIL, PRIXOFFRE, QUANTITE
   INTO gagnantEmail, gagnantPrix, gagnantQuantite
   FROM OFFRE
   WHERE IDVENTE = :idVente
   ORDER BY PRIXOFFRE DESC-- (DATEOFFRE ASC , HEUREOFFRE ASC)
   FETCH FIRST 1 ROWS ONLY;-- dans le cas où un seul gangnant possible (??)

--dans le cas où plusieurs gagnants possibles on selectionne toous les gagnants
    -- SELECT EMAIL, PRIXOFFRE, QUANTITE
    -- INTO gagnantEmail, gagnantPrix, gagnantQuantite
    -- FROM OFFRE
    -- WHERE IDVENTE = :idVente
    -- AND PRIXOFFRE = (SELECT MAX(PRIXOFFRE) FROM OFFRE WHERE IDVENTE = :idVente)
    -- ORDER BY DATEOFFRE DESC, HEUREOFFRE DESC;


   -- -- Clôture de la vente
   -- UPDATE VENTE
   -- SET TYPEVENTE = -1
   -- WHERE IDVENTE = :idVente;

END;
