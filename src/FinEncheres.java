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
