-- Commencer la transaction
BEGIN TRANSACTION;

-- 1. Vérifier les produits disponibles dans la salle de vente
DECLARE @IdSalle INT = ?; -- A Remplace par l'ID de la salle de vente
DECLARE @IdProduit INT;
DECLARE @PrixActuel FLOAT;
DECLARE @IdVente INT;
DECLARE @PrixOffre FLOAT = ?; -- Montant de l'offre à vérifier
DECLARE @Quantite INT = ?; -- Quantité à insérer
DECLARE @Email VARCHAR(100) = ?; -- Email de l'utilisateur
DECLARE @DateOffre DATE = CURRENT_DATE;
DECLARE @HeureOffre TIMESTAMP = CURRENT_TIMESTAMP;

-- Récupérer les produits disponibles
SELECT P.IdProduit, P.NomProduit, V.PrixActuel, V.IdVente 
INTO #ProduitsDisponibles
FROM Vente V 
JOIN Produit P ON V.IdProduit = P.IdProduit 
WHERE P.DispoProduit = 1 AND V.IdSalle = @IdSalle
AND V.DateVente <= CURRENT_DATE 
AND (V.Duree = -1 OR SYSTIMESTAMP <= V.DateVente + NUMTODSINTERVAL(V.Duree, 'MINUTE'));

-- Vérifier si des produits sont disponibles
IF NOT EXISTS (SELECT 1 FROM #ProduitsDisponibles)
BEGIN
    ROLLBACK TRANSACTION;
    RAISERROR('Aucun produit disponible dans cette salle de vente.', 16, 1);
    RETURN;
END

-- 2. Vérifier si le produit existe et obtenir le prix actuel
SELECT @IdProduit = P.IdProduit, @PrixActuel = V.PrixActuel, @IdVente = V.IdVente
FROM #ProduitsDisponibles
WHERE IdProduit = ?; -- A Remplacer par l'ID du produit saisi

IF @IdProduit IS NULL
BEGIN
    ROLLBACK TRANSACTION;
    RAISERROR('Le produit spécifié n\'existe pas.', 16, 1);
    RETURN;
END

-- Vérifier si l'offre est valide
IF @PrixOffre <= @PrixActuel
BEGIN
    ROLLBACK TRANSACTION;
    RAISERROR('L\'offre doit être supérieure au prix actuel.', 16, 1);
    RETURN;
END

-- 3. Insérer l'offre dans la table Offre
INSERT INTO Offre (PrixOffre, DateOffre, HeureOffre, Quantite, Email, IdVente)
VALUES (@PrixOffre, @DateOffre, @HeureOffre, @Quantite, @Email, @IdVente);

-- Valider la transaction
COMMIT TRANSACTION;