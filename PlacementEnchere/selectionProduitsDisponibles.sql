SELECT P.IdProduit, P.NomProduit, V.PrixDepart, V.PrixActuel
FROM Produit P
JOIN Vente V ON P.IdProduit = V.IdVente
JOIN SalleDeVente S ON V.IdSalle = S.IdSalle
WHERE P.DispoProduit = 1
AND V.PrixActuel < P.PrixRevient; -- Critère pour afficher les produits