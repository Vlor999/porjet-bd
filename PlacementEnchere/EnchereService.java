import java.math.BigDecimal;
import java.sql.*;

public class EnchereService {
    private static final String URL = "jdbc:oracle:thin:@oracle1.ensimag.fr:1521:oracle1";

    // Méthode pour placer une enchère (offre)
    public void placerEnchere(String emailUtilisateur, int idProduit, BigDecimal montantOffre, int quantite) throws Exception {
        Connection connection = null;
        PreparedStatement pstmt = null;
        try {
            connection = DriverManager.getConnection(URL, "adnetw", "adnetw");
            connection.setAutoCommit(false);

            // Vérifier la validité de l'offre
            String sqlVerif = """
                SELECT V.PrixActuel 
                FROM Vente V 
                JOIN Produit P ON V.IdProduit = P.IdProduit 
                WHERE P.IdProduit = ? AND P.DispoProduit = 1
                """;
            pstmt.setInt(1, idProduit);

            ResultSet rs = pstmt.executeQuery();
            if (!rs.next()) {
                throw new Exception("Produit non trouvé.");
            }

            BigDecimal prixActuel = rs.getBigDecimal("PrixActuel");
            if (!verifierOffre(montantOffre, prixActuel)) {
                throw new Exception("L'offre doit être supérieure au prix actuel.");
            }

            // Insérer l'offre 
            String sqlInsert = """
                INSERT INTO Offre (PrixOffre, DateOffre, HeureOffre, Quantite, Email, IdVente, IdProduit)
                VALUES (?, CURRENT_DATE, CURRENT_TIMESTAMP, ?, ?, 
                (SELECT IdVente FROM Vente WHERE IdProduit = ?), ?)
                """;
            pstmt = connection.prepareStatement(sqlInsert);
            pstmt.setBigDecimal(1, montantOffre);
            pstmt.setInt(2, quantite);
            pstmt.setString(3, emailUtilisateur);
            pstmt.setInt(4, idProduit);

            pstmt.executeUpdate();
            connection.commit(); 

            System.out.println("Offre enregistrée avec succès.");

        } catch (Exception e) {
            if (connection != null) {
                connection.rollback(); 
            }
            throw e;
        } finally {
            if (pstmt != null) pstmt.close();
            if (connection != null) connection.close();
        }
    }

    // Méthode pour vérifier si l'offre est valide
    private boolean verifierOffre(BigDecimal montantOffre, BigDecimal prixActuel) {
        return montantOffre.compareTo(prixActuel) > 0; // L'offre doit être supérieure au prix actuel
    }

    public static void main(String[] args) {
        EnchereService service = new EnchereService();
        try {
            service.placerEnchere(
                "walid.barkatou@bdd.com",
                95, // ID du produit
                new BigDecimal("50.00"), // Montant de l'offre
                1 // Quantité
            );
        } catch (Exception e) {
            System.err.println("Erreur lors du placement de l'enchère: " + e.getMessage());
        }
    }
}