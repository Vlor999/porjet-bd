import java.sql.*;

public class FinEncheres {
    public static void terminerEnchere(Connection connection, int idVente) {
        try {
            // Vérifier si la vente est montante ou descendante
            String queryVente = "SELECT Vente.PrixDepart, SalleDeVente.EstMontante,SalleDeVente.IdSalle FROM Vente " +
                                "JOIN SalleDeVente ON Vente.IdSalle = SalleDeVente.IdSalle " +
                                "WHERE Vente.IdVente = ?";
            PreparedStatement stmt = connection.prepareStatement(queryVente);
            stmt.setInt(1, idVente);
            ResultSet rs = stmt.executeQuery();

            if (!rs.next()) {
                System.out.println("Vente introuvable.");
                return;
            }
            int prixdepart = rs.getInt("PrixDepart");
            int idsalle = rs.getInt("IdSalle");
            boolean estMontante = rs.getInt("EstMontante") == 1;

            // Trouver le ou les gagnants
            String queryGagnants = "SELECT Email, PrixOffre, Quantite FROM Offre WHERE IdVente = ? " +
                                   (estMontante ?
                                       "ORDER BY PrixOffre DESC,  HeureOffre ASC " :
                                       "ORDER BY HeureOffre ASC") +
                                   " FETCH FIRST 1 ROWS ONLY";

            PreparedStatement gagnantsStmt = connection.prepareStatement(queryGagnants);
            gagnantsStmt.setInt(1, idVente);
            ResultSet gagnantsRs = gagnantsStmt.executeQuery();

            if (gagnantsRs.next()) {
                String gagnantEmail = gagnantsRs.getString("Email");
                int prixGagnant = gagnantsRs.getInt("PrixOffre");
                int quantiteGagnante = gagnantsRs.getInt("Quantite");

                System.out.println("\033[0;31mLe gagnant est : " + gagnantEmail+"\033[0m");
                System.out.println("\033[0;31mPrix : " + prixGagnant + ", Quantité : " + quantiteGagnante+"\033[0m");
                
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
                        String reouvertureVente = "UPDATE Vente SET PrixActuel = ?, Quantite = ?, Duree = -1 WHERE IdVente = ?";
                        PreparedStatement reouvertureVenteStmt = connection.prepareStatement(reouvertureVente);
                        reouvertureVenteStmt.setInt(1, prixdepart);
                        reouvertureVenteStmt.setInt(2,quantiteRestante);
                        reouvertureVenteStmt.setInt(3,idVente);
                        reouvertureVenteStmt.executeUpdate();
                        System.out.println("rguirhgiruegjr\n\n");
                        
                        System.out.println("Vente réouverte avec l'identifiant produit : " + idProduit + " et l'identifiant vente : "+ idVente);
                        
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
