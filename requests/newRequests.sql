
-- ============================
-- Insertion des données
-- ============================
BEGIN;

-- Insertion dans la table CARACTERISTIQUES
INSERT INTO CARACTERISTIQUES (NOMCAR, VALEURCAR, IDPRODUIT) 
VALUES ('Variété', 'Cavendish', '95');

-- Insertion dans la table Categorie
INSERT INTO Categorie (NOMCAT, DESCRCAT) 
VALUES ('Électronique', 'Appareils électroniques variés tels que les smartphones téléviseurs ordinateurs etc');

-- Insertion dans la table Offre
INSERT INTO Offre (PrixOffre, HeureOffre, Quantite, Email, IdVente) 
VALUES ('95', TIMESTAMP '2024-12-06 10:30:00', '1', 'jennifer.wilson@bdd.com', '6');

-- Insertion dans la table PRODUIT
INSERT INTO PRODUIT (IDPRODUIT, NOMPRODUIT, PRIXREVIENT, STOCK, DISPOPRODUIT, NOMCAT) 
VALUES ('7', 'Banane Fei', '16', '4300', '1', 'Fruit');

-- Insertion dans la table SALLEDEVENTE
INSERT INTO SALLEDEVENTE (IDSALLE, ESTMONTANTE, ESTOCCUPEE, ESTREVOCABLE, LIMITEOFFRES, TYPEDUREE, CATEGORIE) 
VALUES ('7', '1', '0', '1', '12', 'illimitee', 'Livres');

-- Insertion dans la table UTILISATEUR
INSERT INTO UTILISATEUR (Email, Nom, Prenom, AdressePostale) 
VALUES ('john.doe@bdd.com', 'Doe', 'John', '7 rue de la paix');

-- Insertion dans la table Vente
INSERT INTO Vente (IdVente, Quantite, IdProduit, IdSalle, PrixDepart, PrixActuel, Duree, HeureVente) 
VALUES ('22', '7', '78', '5', '60', '65', '5000', TIMESTAMP '2024-12-14 12:45:00');

COMMIT;

-- ============================
-- Affichage des ventes
-- ============================

-- Affichage des ventes en fonction d'une idvente spécifique.
BEGIN;
SELECT * FROM VENTE WHERE vente.IDPRODUIT = ?;
COMMIT;

-- Affichage des salles de vente disponibles (non occupées)
BEGIN;
SELECT * FROM SalleDeVente WHERE EstOccupee = 0;
COMMIT;

-- Affichage de toutes les salles de vente
BEGIN;
SELECT * FROM SalleDeVente;
COMMIT;

-- Vérification si la vente est montante ou descendante
BEGIN;
SELECT SalleDeVente.EstMontante 
FROM Vente 
JOIN SalleDeVente ON Vente.IdSalle = SalleDeVente.IdSalle 
WHERE Vente.IdVente = :idVente;
COMMIT;

-- =========================
-- Affichage des produits disponibles dans une salle de vente
BEGIN;
SELECT P.IdProduit, P.NomProduit, V.Quantite, V.PrixActuel, V.IdVente, S.EstMontante, V.Duree, V.HeureVente
FROM Vente V 
JOIN Produit P ON V.IdProduit = P.IdProduit
JOIN SalledeVente S ON V.IdSalle = S.IdSalle 
WHERE DispoProduit = 1 AND S.IdSalle = ? AND (DUREE = -1 OR (DUREE > 0 AND ? < HeureVente AND 
? > HeureVente - NUMTODSINTERVAL(DUREE, 'MINUTE')));
COMMIT;

-- =========================
-- Vérification du prix actuel et insertion de l'offre
BEGIN;
SELECT P.IdProduit, P.NomProduit, V.PrixActuel, V.IdVente 
FROM Vente V 
JOIN Produit P ON V.IdProduit = P.IdProduit 
WHERE P.IdProduit = :idProduit;

INSERT INTO Offre (PrixOffre, DateOffre, HeureOffre, Quantite, Email, IdVente)
VALUES (:prixOffre, CURRENT_DATE, CURRENT_TIMESTAMP, :quantite, :emailUtilisateur, :idVente);

UPDATE Vente
SET PrixActuel = :prixOffre
WHERE IdVente = :idVente;
COMMIT;

-- =========================
-- Récupération du gagnant pour une vente
BEGIN;
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

-- =========================
-- Mise à jour des stocks après une vente
BEGIN;
SELECT Produit.Stock, Produit.IdProduit 
FROM Vente 
JOIN Produit ON Produit.IdProduit = Vente.IdProduit 
WHERE Vente.IdVente = :idVente;

UPDATE Produit 
SET Stock = Stock - :quantiteGagnante 
WHERE IdProduit = :idProduit;

UPDATE Produit 
SET DispoProduit = CASE 
    WHEN Stock <= 0 THEN 0 
    ELSE 1 
END 
WHERE IdProduit = :idProduit;

UPDATE Vente SET PrixActuel = :PrixDepart, Quantite = :QuantiteRestante, Duree = -1 WHERE IdVente = :idVente;

UPDATE Produit 
SET DispoProduit = 0 
WHERE IdProduit = :idProduit;
COMMIT;