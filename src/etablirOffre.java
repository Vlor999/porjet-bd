import java.sql.*;
import java.util.Scanner;

public class etablirOffre {

    public static void afficherToutesLesOffres(Connection connection, Scanner scanner) {
        try {
            Statement stmt = connection.createStatement();
            ResultSet res = stmt.executeQuery("SELECT * FROM Offre");

            // Afficher l'en-tête
            String header = String.format("| %-10s | %-30s | %-10s | %-35s | %-15s |", 
                "Prix Offre","Heure Offre", "Quantité", "Email Utilisateur", "ID Vente");
            System.out.println("-".repeat(header.length()));
            System.out.println(header);
            System.out.println("-".repeat(header.length()));
        
            // Afficher les résultats
            while (res.next()) {
                String row = String.format("| %-10s | %-30s | %-10d | %-35s | %-15d |",
                        res.getDouble("PRIXOFFRE"),
                        res.getTimestamp("HEUREOFFRE"), // Récupère le timestamp
                        res.getInt("QUANTITE"),
                        res.getString("EMAIL"),
                        res.getInt("IDVENTE")
                );
                System.out.println(row);
            }
            System.out.println("-".repeat(header.length()));
        } catch (SQLException e) {
            System.err.println("Erreur lors de l'affichage des offres");
            e.printStackTrace();
        }
    }

    public static void afficherOffreSpecifique(Connection connection, Scanner scanner) {
        ResultSet res = null;
        try {
            // Requête paramétrée pour récupérer les offres d'une vente spécifique
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM Offre WHERE idvente = ?");
            System.out.print("ID de la vente : ");
            int idvente = scanner.nextInt();
            scanner.nextLine(); // Consommer la nouvelle ligne
            statement.setInt(1, idvente);
            res = statement.executeQuery();
        
            // Afficher l'en-tête
            String header = String.format("| %-10s | %-30s | %-10s | %-35s | %-15s |", 
                "Prix Offre", "Heure Offre", "Quantité", "Email Utilisateur", "ID Vente");
            System.out.println("-".repeat(header.length()));
            System.out.println(header);
            System.out.println("-".repeat(header.length()));
        
            // Afficher les résultats
            boolean found = false;
            while (res.next()) {
                found = true;
                String row = String.format("| %-10s | %-30s | %-10d | %-35s | %-15s |",
                        res.getDouble("PRIXOFFRE"),
                        res.getTimestamp("HEUREOFFRE"), // Récupère le timestamp
                        res.getInt("QUANTITE"),
                        res.getString("EMAIL"),
                        res.getInt("IDVENTE")
                );
                System.out.println(row);
            }
            if (!found) {
                System.out.println("Aucune offre trouvée pour l'ID de vente spécifié.");
            }
            System.out.println("-".repeat(header.length()));
        } catch (SQLException e) {
            System.err.println("Erreur lors de la recherche des offres");
            e.printStackTrace();
        }
    }
    
    
}
