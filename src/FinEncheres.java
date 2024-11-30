import java.sql.*;

public class FinEncheres {
    public static void terminerEnchere(Connection connection, int idVente) {
        try {
            // Vérifier si la vente est montante ou descendante
            String queryVente = "SELECT SalleDeVente.EstMontante FROM Vente " +
                                "JOIN SalleDeVente ON Vente.IdSalle = SalleDeVente.IdSalle " +
                                "WHERE Vente.IdVente = ?";
            PreparedStatement stmt = connection.prepareStatement(queryVente);
            stmt.setInt(1, idVente);
            ResultSet rs = stmt.executeQuery();

            if (!rs.next()) {
                System.out.println("Vente introuvable.");
                return;
            }

            boolean estMontante = rs.getInt("EstMontante") == 1;

            // Trouver le ou les gagnants
            String queryGagnants = "SELECT Email, PrixOffre, Quantite FROM Offre WHERE IdVente = ? " +
                                   (estMontante ?
                                       "ORDER BY PrixOffre DESC, DateOffre ASC, HeureOffre ASC " :
                                       "ORDER BY DateOffre ASC, HeureOffre ASC") +
                                   " FETCH FIRST 1 ROWS ONLY";

            PreparedStatement gagnantsStmt = connection.prepareStatement(queryGagnants);
            gagnantsStmt.setInt(1, idVente);
            ResultSet gagnantsRs = gagnantsStmt.executeQuery();

            if (gagnantsRs.next()) {
                String gagnantEmail = gagnantsRs.getString("Email");
                int prixGagnant = gagnantsRs.getInt("PrixOffre");
                int quantiteGagnante = gagnantsRs.getInt("Quantite");

                System.out.println("Le gagnant est : " + gagnantEmail);
                System.out.println("Prix : " + prixGagnant + ", Quantité : " + quantiteGagnante);
                
                // Clôture de la vente et mise à jour du stock du produit
                String queryStock = "SELECT Produit.Stock, Produit.IdProduit FROM Vente " +
                                     "JOIN Produit ON Produit.IdProduit = Vente.IdProduit " +
                                     "WHERE Vente.IdVente = ?";
                PreparedStatement stockStmt = connection.prepareStatement(queryStock);
                stockStmt.setInt(1, idVente);
                ResultSet stockResult = stockStmt.executeQuery();
                
                if (stockResult.next()) {
                    int stockProduit = stockResult.getInt("Stock");
                    int idProduit = stockResult.getInt("IdProduit");
                    
                    
                    int quantiteRestante = stockProduit - quantiteGagnante;
                    
                    if (quantiteRestante > 0) {
                        // Mise à jour du stock
                        String updateStock = "UPDATE Produit SET Stock = ? WHERE IdProduit = ?";
                        PreparedStatement updateStockStmt = connection.prepareStatement(updateStock);
                        updateStockStmt.setInt(1, quantiteRestante);
                        updateStockStmt.setInt(2, idProduit);
                        updateStockStmt.executeUpdate();
                        
                        // Mettre à jour la disponibilité du produit
                        String updateDispoProduit = "UPDATE Produit SET DispoProduit = 1 WHERE IdProduit = ?";
                        PreparedStatement updateDispoStmt = connection.prepareStatement(updateDispoProduit);
                        updateDispoStmt.setInt(1, idProduit);
                        updateDispoStmt.executeUpdate();
                        
                        // Réouverture de la vente
                        String reouvertureVente = "INSERT INTO Vente (IdProduit, IdSalle, DateVente, EstMontante) " +
                                                  "VALUES (?, (SELECT IdSalle FROM Vente WHERE IdVente = ?), NOW(), ?)";
                        PreparedStatement reouvertureVenteStmt = connection.prepareStatement(reouvertureVente);
                        reouvertureVenteStmt.setInt(1, idProduit);
                        reouvertureVenteStmt.setInt(2, idVente);
                        reouvertureVenteStmt.setBoolean(3, estMontante);
                        reouvertureVenteStmt.executeUpdate();
                        
                        System.out.println("Vente réouverte avec le produit ID : " + idProduit);
                    } else {
                        // Si la quantité est épuisée, mettre à jour la disponibilité du produit
                        String updateDispoProduitEpuisé = "UPDATE Produit SET DispoProduit = 0 WHERE IdProduit = ?";
                        PreparedStatement updateDispoEpuiséStmt = connection.prepareStatement(updateDispoProduitEpuisé);
                        updateDispoEpuiséStmt.setInt(1, idProduit);
                        updateDispoEpuiséStmt.executeUpdate();
                        
                        System.out.println("Produit épuisé, la vente est terminée et le produit est retiré.");
                    }
                }
                
                System.out.println("La vente est terminée.");
            } else {
                System.out.println("Aucun gagnant trouvé pour cette vente.");
            }
        } 
        catch (SQLException e) 
        {
            System.err.println("Erreur lors de la fin de l'enchère.");
        }
    }
}
