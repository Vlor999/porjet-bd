import java.sql.*;
import java.util.Scanner;

public class etablirCaracteristiques {

    public static void afficherToutesLesCaracteristiques(Connection connection, Scanner scanner) {
        try {
            Statement stmt = connection.createStatement();
            ResultSet res = stmt.executeQuery(
                "SELECT NOMPRODUIT, NOMCAR, VALEURCAR " +
                "FROM CARACTERISTIQUES " +
                "JOIN PRODUIT ON PRODUIT.IDPRODUIT = CARACTERISTIQUES.IDPRODUIT " +
                "ORDER BY PRODUIT.IDPRODUIT"
            );

            // Affichage de l'en-tête
            String header = String.format(
                "| %-30s | %-30s | %-30s |", 
                "Nom du produit", "Nom de la caractéristique", "Valeur de la caractéristique"
            );
            System.out.println(header);
            System.out.println("-".repeat(header.length()));

            // Parcourir et afficher les résultats
            while (res.next()) {
                System.out.println(String.format(
                    "| %-30s | %-30s | %-30s |",
                    res.getString("NomProduit"),
                    res.getString("NomCar"),
                    res.getString("ValeurCar")
                ));
            }
            System.out.println("-".repeat(header.length()));
            res.close();
            stmt.close();
        } 
        catch (SQLException e) 
        {
            System.err.println("Erreur lors de l'affichage des caractéristiques");
        }
    }

    public static void afficherCaracteristiquesSpecifique(Connection connection, Scanner scanner) {
        try {
            System.out.print("Nom du produit : ");
            String nomproduit = scanner.nextLine();

            PreparedStatement statement = connection.prepareStatement(
                "SELECT NOMPRODUIT, NOMCAR, VALEURCAR " +
                "FROM CARACTERISTIQUES " +
                "JOIN PRODUIT ON PRODUIT.IDPRODUIT = CARACTERISTIQUES.IDPRODUIT " +
                "WHERE NOMPRODUIT LIKE ?"
            );
            statement.setString(1, "%" + nomproduit + "%");

            ResultSet res = statement.executeQuery();

            // Affichage de l'en-tête
            String header = String.format(
                "| %-30s | %-30s | %-30s |", 
                "Nom du produit", "Nom de la caractéristique", "Valeur de la caractéristique"
            );
            System.out.println("-".repeat(header.length()));
            System.out.println(header);
            System.out.println("-".repeat(header.length()));

            // Parcourir et afficher les résultats
            boolean hasResults = false;
            while (res.next()) {
                hasResults = true;
                System.out.println(String.format(
                    "| %-30s | %-30s | %-30s |",
                    res.getString("NomProduit"),
                    res.getString("NomCar"),
                    res.getString("ValeurCar")
                ));
            }
            System.out.println("-".repeat(header.length()));

            if (!hasResults) {
                System.out.println("Aucune caractéristique trouvée pour le produit : " + nomproduit);
            }

            res.close();
            statement.close();
        } 
        catch (SQLException e) 
        {
            System.err.println("Erreur lors de l'affichage des caractéristiques");
        }
    }
}
