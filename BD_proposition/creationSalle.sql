-- Début de la transaction
BEGIN TRANSACTION;

-- 1. Création d'une nouvelle salle de vente
INSERT INTO SalleVente (
    NomSalle,
    Description,
    DateCreation
) VALUES (
    'Salle Antiquités',
    'Vente aux enchères d''antiquités rares',
    CURRENT_DATE
) RETURNING IdSalle;  -- Récupère l'ID de la salle créée

-- 2. Sélection des produits disponibles (non associés à une vente active)
SELECT 
    P.IdProduit,
    P.NomProduit,
    P.Description,
    P.Stock,
    P.PrixRevient
FROM Produit P
LEFT JOIN Vente V ON P.IdProduit = V.IdProduit 
    AND V.DateFin >= CURRENT_DATE
WHERE V.IdVente IS NULL
    AND P.Stock > 0;

-- 3. Création d'une nouvelle vente pour un produit sélectionné
INSERT INTO Vente (
    DateDebut,
    DateFin,
    HeureDebut,
    HeureFin,
    PrixDepart,
    TypeVente,
    Revocable,
    IdProduit,
    IdSalle
) VALUES (
    '2024-11-20',  -- Date de début
    '2024-11-25',  -- Date de fin
    '09:00:00',    -- Heure de début
    '18:00:00',    -- Heure de fin
    1000.00,       -- Prix de départ
    'montante',    -- Type de vente (montante/descendante)
    TRUE,          -- Revocable ou non
    1,             -- IdProduit sélectionné
    1             -- IdSalle créé précédemment
);

-- 4. Vérification de la disponibilité du produit
SELECT 
    CASE 
        WHEN EXISTS (
            SELECT 1 
            FROM Vente 
            WHERE IdProduit = 1  -- IdProduit (1) a modifier au cas ou
            AND DateFin >= CURRENT_DATE
        ) THEN 0
        ELSE 1
    END as ProduitDisponible;

-- Si tout est OK, on valide la transaction
COMMIT;

-- En cas d'erreur
ROLLBACK;