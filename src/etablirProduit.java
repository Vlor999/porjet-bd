import java.sql.*;
import java.util.Scanner;

public class etablirProduit {
    
    public static void afficherTousLesProduits(Connection connection, Scanner scanner) {
        try {
            Statement stmt = connection.createStatement();
            ResultSet res = stmt.executeQuery("SELECT * FROM Produit");

            // Afficher l'en-tête
            String header = String.format(
                "| %-15s | %-30s | %-15s | %-10s | %-20s | %-30s |", 
                "ID Produit", "Nom du Produit", "Prix Revient", "Stock", "Disponibilité", "Nom Catégorie"
            );
            System.out.println("-".repeat(header.length()));
            System.out.println(header);
            System.out.println("-".repeat(header.length()));

            // Afficher les données
            while (res.next()) {
                String row = String.format(
                    "| %-15s | %-30s | %-15s | %-10s | %-20s | %-30s |",
                    res.getString("IdProduit"),
                    res.getString("NomProduit"),
                    res.getString("PrixRevient"),
                    res.getString("Stock"),
                    res.getString("DispoProduit"),
                    res.getString("NomCat")
                );
                System.out.println(row);
            }
            System.out.println("-".repeat(header.length()));
        } 
        catch (SQLException e) 
        {
            System.err.println("Erreur lors de l'affichage des produits : Bad SQL Request");   
        }
    }
    public static void afficherProduitSpécifiqueId(Connection connection, Scanner scanner){
        ResultSet res = null;
        try {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM Produit WHERE IDPRODUIT = ?");
            System.out.print("Identifiant du produit : ");
            int idproduit = scanner.nextInt();
            scanner.nextLine();
            statement.setInt(1, idproduit);
            res = statement.executeQuery();

            // Afficher l'en-tête
            String header = String.format(
                "| %-15s | %-30s | %-15s | %-10s | %-20s | %-30s |", 
                "ID Produit", "Nom du Produit", "Prix Revient", "Stock", "Disponibilité", "Nom Catégorie"
            );
            System.out.println("-".repeat(header.length()));
            System.out.println(header);
            System.out.println("-".repeat(header.length()));

            // Afficher les données
            while (res.next()) {
                String row = String.format(
                    "| %-15s | %-30s | %-15s | %-10s | %-20s | %-30s |",
                    res.getString("IdProduit"),
                    res.getString("NomProduit"),
                    res.getString("PrixRevient"),
                    res.getString("Stock"),
                    res.getString("DispoProduit"),
                    res.getString("NomCat")
                );
                System.out.println(row);
            }
            System.out.println("-".repeat(header.length()));
        } 
        catch (SQLException e) 
        {
            System.err.println("Erreur lors de l'affichage d'un produit spécifique : Bad SQL Request or Bad Product Name");   
        }
    }
    public static void afficherProduitSpécifiqueNom(Connection connection, Scanner scanner) {
        ResultSet res = null;
        try {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM Produit WHERE NomProduit LIKE ?");
            System.out.print("Nom du produit : ");
            String nomProduit = scanner.nextLine();
            statement.setString(1, "%" + nomProduit + "%");
            res = statement.executeQuery();

            // Afficher l'en-tête
            String header = String.format(
                "| %-15s | %-30s | %-15s | %-10s | %-20s | %-30s |", 
                "ID Produit", "Nom du Produit", "Prix Revient", "Stock", "Disponibilité", "Nom Catégorie"
            );
            System.out.println("-".repeat(header.length()));
            System.out.println(header);
            System.out.println("-".repeat(header.length()));

            // Afficher les données
            while (res.next()) {
                String row = String.format(
                    "| %-15s | %-30s | %-15s | %-10s | %-20s | %-30s |",
                    res.getString("IdProduit"),
                    res.getString("NomProduit"),
                    res.getString("PrixRevient"),
                    res.getString("Stock"),
                    res.getString("DispoProduit"),
                    res.getString("NomCat")
                );
                System.out.println(row);
            }
            System.out.println("-".repeat(header.length()));
        } 
        catch (SQLException e) 
        {
            System.err.println("Erreur lors de l'affichage d'un produit spécifique : Bad SQL Request or Bad Product Name");   
        }
    }

    public static void afficherProduitsDispnibles(Connection connection, Scanner scanner) {
        try {
            Statement stmt = connection.createStatement();
            ResultSet res = stmt.executeQuery("SELECT * FROM Produit WHERE DISPOPRODUIT = 1");

            // Afficher l'en-tête
            String header = String.format(
               "| %-15s | %-30s | %-15s | %-10s | %-20s | %-30s |", 
                "ID Produit", "Nom du Produit", "Prix Revient", "Stock", "Disponibilité", "Nom Catégorie"
            );
            System.out.println("-".repeat(header.length()));
            System.out.println(header);
            System.out.println("-".repeat(header.length()));

            // Afficher les données
            while (res.next()) {
                String row = String.format(
                    "| %-15s | %-30s | %-15s | %-10s | %-20s | %-30s |",
                    res.getString("IdProduit"),
                    res.getString("NomProduit"),
                    res.getString("PrixRevient"),
                    res.getString("Stock"),
                    res.getString("DispoProduit"),
                    res.getString("NomCat")
                );
                System.out.println(row);
            }
            System.out.println("-".repeat(header.length()));
        } 
        catch (SQLException e) 
        {
            System.err.println("Erreur lors de l'affichage des produits disponibles : Bad SQL Request");
        }
    }
}
