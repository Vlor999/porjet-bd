import java.math.BigDecimal;
import java.sql.*;
import java.util.Scanner;

public class EnchereService {
    private static final String URL = "jdbc:oracle:thin:@oracle1.ensimag.fr:1521:oracle1";

    // Méthode pour placer une enchère (offre)
    public void placerEnchere(String emailUtilisateur, int idProduit, BigDecimal montantOffre, int quantite) throws Exception {
        Connection connection = null;
        PreparedStatement pstmt = null;
        try {
            connection = DriverManager.getConnection(URL, "adnetw", "adnetw");
            connection.setAutoCommit(false); 

            // Vérifier la validité de l'offre avec un JOIN
            String sqlVerif = """
                SELECT V.PrixActuel, V.IdVente 
                FROM Vente V 
                JOIN Produit P ON V.IdProduit = P.IdProduit 
                WHERE P.IdProduit = ? AND P.DispoProduit = 1 
                AND V.DateVente <= CURRENT_DATE AND 
                (V.Duree = -1 OR SYSTIMESTAMP <= V.DateVente + NUMTODSINTERVAL(V.Duree, 'MINUTE'))
            """;
            pstmt = connection.prepareStatement(sqlVerif);
            pstmt.setInt(1, idProduit);

            ResultSet rs = pstmt.executeQuery();
            if (!rs.next()) {
                throw new Exception("Produit non trouvé ou vente terminée.");
            }

            BigDecimal prixActuel = new BigDecimal(rs.getInt("PrixActuel"));
            int idVente = rs.getInt("IdVente");
            if (!verifierOffre(montantOffre, prixActuel)) {
                throw new Exception("L'offre doit être supérieure au prix actuel.");
            }

            // Insérer l'offre
            String sqlInsert = """
                INSERT INTO Offre (PrixOffre, DateOffre, HeureOffre, Quantite, Email, IdVente)
                VALUES (?, CURRENT_DATE, CURRENT_TIMESTAMP, ?, ?, ?)
            """;
            pstmt = connection.prepareStatement(sqlInsert);
            pstmt.setBigDecimal(1, montantOffre);
            pstmt.setInt(2, quantite);
            pstmt.setString(3, emailUtilisateur);
            pstmt.setInt(4, idVente);

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
        Scanner scanner = new Scanner(System.in);

        try {
            System.out.print("Entrez votre email : ");
            String emailUtilisateur = scanner.nextLine();

            System.out.print("Entrez l'ID du produit : ");
            int idProduit = Integer.parseInt(scanner.nextLine());

            System.out.print("Entrez le montant de votre offre : ");
            BigDecimal montantOffre = new BigDecimal(scanner.nextLine());

            System.out.print("Entrez la quantité : ");
            int quantite = Integer.parseInt(scanner.nextLine());

            service.placerEnchere(emailUtilisateur, idProduit, montantOffre, quantite);
            System.out.println("Votre offre a été validée.");

        } catch (Exception e) {
            System.err.println("Erreur lors du placement de l'enchère: " + e.getMessage());
        } finally {
            scanner.close(); 
        }
    }
}