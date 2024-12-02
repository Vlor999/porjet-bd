BEGIN;

-- Affichage des salles de vente disponibles (non occupées)
SELECT * 
FROM SalleDeVente 
WHERE EstOccupee = 0;

COMMIT;


BEGIN;

-- Affichage de toutes les salles de vente
SELECT * 
FROM SalleDeVente;

COMMIT;


BEGIN;

-- Affichage de toutes les salles de vente
SELECT * 
FROM SalleDeVente;

COMMIT;

BEGIN;
-- Vérification si la vente est montante ou descendante
SELECT SalleDeVente.EstMontante 
FROM Vente 
JOIN SalleDeVente ON Vente.IdSalle = SalleDeVente.IdSalle 
WHERE Vente.IdVente = :idVente;

COMMIT;

--=====================
BEGIN;

--Afficher tous les produits dispo dans la salle de vente :
SELECT P.IdProduit, P.NomProduit, V.Quantite, V.PrixActuel, V.IdVente, S.EstMontante, V.Duree, V.HeureVente
                FROM Vente V 
                JOIN Produit P ON V.IdProduit = P.IdProduit
                JOIN SalledeVente S ON V.IdSalle = S.IdSalle 
                WHERE DispoProduit = 1 AND S.IdSalle = ? AND (DUREE = -1 OR (DUREE > 0 AND ? < HeureVente AND 
                ? > HeureVente - NUMTODSINTERVAL(DUREE, 'MINUTE'))) 

COMMIT;
--=======================
BEGIN;

-- Vérification du prix actuel du produit et insertion de l'offre
SELECT P.IdProduit, P.NomProduit, V.PrixActuel, V.IdVente 
FROM Vente V 
JOIN Produit P ON V.IdProduit = P.IdProduit 
WHERE P.IdProduit = :idProduit;

-- Insertion de l'offre
INSERT INTO Offre (PrixOffre, DateOffre, HeureOffre, Quantite, Email, IdVente)
VALUES (:prixOffre, CURRENT_DATE, CURRENT_TIMESTAMP, :quantite, :emailUtilisateur, :idVente);

-- Mise à jour du prix actuel de la vente si l'offre est valide
UPDATE Vente
SET PrixActuel = :prixOffre
WHERE IdVente = :idVente;

COMMIT;
--==================================

BEGIN;

-- Récupération du gagnant pour une vente montante ou descendante
SELECT Email, PrixOffre, Quantite 
FROM Offre 
WHERE IdVente = :idVente 
ORDER BY 
    CASE 
        WHEN (SELECT EstMontante 
              FROM SalleDeVente 
              WHERE IdSalle = (SELECT IdSalle FROM Vente WHERE IdVente = :idVente)) = 1 
        THEN PrixOffre DESC, DateOffre ASC, HeureOffre ASC 
        ELSE DateOffre ASC, HeureOffre ASC 
    END 
FETCH FIRST 1 ROWS ONLY;

COMMIT;


BEGIN;

-- Récupération du stock et du produit lié à la vente
SELECT Produit.Stock, Produit.IdProduit 
FROM Vente 
JOIN Produit ON Produit.IdProduit = Vente.IdProduit 
WHERE Vente.IdVente = :idVente;

-- Mise à jour du stock après la vente
UPDATE Produit 
SET Stock = Stock - :quantiteGagnante 
WHERE IdProduit = :idProduit;

-- Mettre à jour la disponibilité du produit en fonction du stock restant
UPDATE Produit 
SET DispoProduit = CASE 
    WHEN Stock <= 0 THEN 0 
    ELSE 1 
END 
WHERE IdProduit = :idProduit;

-- -- Clôture de la vente terminée
-- DELETE FROM Vente 
-- WHERE IdVente = :idVente;

-- COMMIT;

BEGIN;

-- Si le stock restant est suffisant, on met à jour la vente pour faire en sorte d'en créer une nouvelle, illimitée
UPDATE Vente SET PrixActuel = :PrixDepart, Quantite = :QuantiteRestante, Duree = -1 WHERE IdVente = :idVente;

-- Si le stock est épuisé, retirer le produit de la disponibilité
UPDATE Produit 
SET DispoProduit = 0 
WHERE IdProduit = :idProduit;

COMMIT;



