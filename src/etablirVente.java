import java.sql.*;
import java.util.Scanner;

public class etablirVente {

    public static void afficherToutesLesVentes(Connection connection, Scanner scanner) {
        try {
            Statement stmt = connection.createStatement();
            ResultSet res = stmt.executeQuery("SELECT * FROM Vente");

            // Afficher l'en-tête
            String header = String.format(
                "%-15s %-15s %-10s %-15s %-10s",
                "ID Vente", "Prix Départ", "Durée", "ID Salle", "Prix Actuel"
            );
            System.out.println("-".repeat(header.length()));
            System.out.println(header);
            System.out.println("-".repeat(header.length()));

            // Afficher les données
            while (res.next()) {
                System.out.println(String.format(
                    "%-15s %-15s %-10s %-15s %-10s",
                    res.getString("IdVente"),
                    res.getString("PrixDepart"),
                    res.getString("Duree"),
                    res.getString("IdSalle"),
                    res.getString("PrixActuel")
                ));
            }

            res.close();
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void creerNouvelleVente(Connection connection, Scanner scanner) {
        try {
            System.out.print("Veuillez entrer l'identifiant de la vente : ");
            int id = scanner.nextInt();
            scanner.nextLine();

            PreparedStatement checkStatement = connection.prepareStatement("SELECT * FROM Vente WHERE IdVente = ?");
            checkStatement.setInt(1, id);
            ResultSet res = checkStatement.executeQuery();

            if (res.next()) {
                System.out.println("Cet identifiant est déjà utilisé !");
            } else {
                System.out.print("Identifiant de la salle : ");
                int idsalle = scanner.nextInt();
                scanner.nextLine();

                System.out.print("Identifiant du produit : ");
                int idproduit = scanner.nextInt();
                scanner.nextLine();

                System.out.print("Prix de départ : ");
                int prixdepart = scanner.nextInt();
                scanner.nextLine();

                System.out.print("Prix actuel de la vente : ");
                int prixactuel = scanner.nextInt();
                scanner.nextLine();

                System.out.print("Durée de la vente (en minutes, -1 s'il n'y a pas de limite) : ");
                int duree = scanner.nextInt();
                scanner.nextLine();

                PreparedStatement insertStatement = connection.prepareStatement(
                    "INSERT INTO Vente (IdVente, IdSalle, IdProduit, PrixDepart, PrixActuel, Duree) VALUES (?, ?, ?, ?, ?, ?)"
                );
                insertStatement.setInt(1, id);
                insertStatement.setInt(2, idsalle);
                insertStatement.setInt(3, idproduit);
                insertStatement.setInt(3, prixdepart);
                insertStatement.setInt(4, prixactuel);
                insertStatement.setInt(5, duree);
                insertStatement.executeUpdate();
                System.out.println("Création de la vente réussie !");
                insertStatement.close();
            }

            res.close();
            checkStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
