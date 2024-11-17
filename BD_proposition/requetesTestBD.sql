--Récupérer le meilleur prix offert pour une vente montante

SELECT MAX(O.PrixOffre) AS MeilleurPrixOffert
FROM Offre O
JOIN Vente V ON O.IdVente = V.IdVente
WHERE V.TypeVente = 'montante'
AND V.IdVente = ?;  -- Remplacer ? par l'ID de la vente concernée


--Mettre à jour la valeur courante pour une vente descendante

-- Suppression de l'ancienne offre
DELETE FROM Offre
WHERE IdVente = ?;  -- on remplace ? par l'ID de la vente concernée

-- Insertion de la nouvelle offre
INSERT INTO Offre (IdOffre, PrixOffre, DateOffre, HeureOffre, Quantite, Email, IdVente)
VALUES (?, ?, ?, ?, ?, ?, ?);  -- on remplace les ? par les valeurs de la nouvelle offre

--Demander la description et les caractéristiques d'un produit

SELECT P.NomProduit, P.PrixRevient, P.Stock, C.DescrCat, CAR.NomCar, CAR.ValeurCar
FROM Produit P
JOIN Categorie C ON P.NomCat = C.NomCat
LEFT JOIN Caracteristiques CAR ON P.IdProduit = CAR.IdProduit
WHERE P.IdProduit = ?;  -- on remplace ? par l'ID du produit concerné