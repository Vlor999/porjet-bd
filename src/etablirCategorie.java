import java.sql.*;
import java.util.Scanner;

public class etablirCategorie 
{
    public static void afficherToutesCategories(Connection connection, Scanner scanner) {
        try {
            Statement stmt = connection.createStatement();
            ResultSet res = stmt.executeQuery("SELECT * FROM Categorie");
    
            // Afficher l'en-tête avec largeurs ajustées
            String header = String.format("| %-30s | %-120s |", "Nom de la Catégorie", "Description");
            System.out.println("-".repeat(header.length()));
            System.out.println(header);
            System.out.println("-".repeat(header.length()));
    
            // Afficher les données
            while (res.next()) {
                String row = String.format("| %-30s | %-120s |",
                        res.getString("NomCat"),
                        res.getString("DescrCat"));
                System.out.println(row);
            }
            System.out.println("-".repeat(header.length()));
        } 
        catch (SQLException e) 
        {
            System.err.println("Erreur lors de l'affichage des catégories");
        }
    }
    

    public static void afficherCategorieSpecifique(Connection connection, Scanner scanner) {
        ResultSet res = null;
    
        try {
            // Requête paramétrée
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM Categorie WHERE NomCat LIKE ?");
            System.out.print("Nom de la catégorie : ");
            String nomcat = scanner.nextLine();
            statement.setString(1, nomcat);
            res = statement.executeQuery();
    
            // Afficher l'en-tête avec largeurs ajustées
            String header = String.format("| %-30s | %-120s |", "Nom de la Catégorie", "Description");
            System.out.println("-".repeat(header.length()));
            System.out.println(header);
            System.out.println("-".repeat(header.length()));
    
            // Afficher les données
            while (res.next()) {
                String row = String.format("| %-30s | %-120s |",
                        res.getString("NomCat"),
                        res.getString("DescrCat"));
                System.out.println(row);
            }
            System.out.println("-".repeat(header.length()));
        } 
        catch (SQLException e) 
        {
            System.err.println("Erreur lors de l'affichage de la catégorie");
        }
    }
    

    public static void creerNouvelleCategorie(Connection connection, Scanner scanner)
    {
        try
        {
            System.out.print("Veuillez entrer le nom de la catégorie : ");
            String nom = scanner.nextLine();

            PreparedStatement checkStatement = connection.prepareStatement("SELECT * FROM Categorie WHERE NomCat LIKE ?");
            checkStatement.setString(1, nom);
            ResultSet res = checkStatement.executeQuery();

            if (res.next()) 
            {
                System.out.println("Ce nom est déjà utilisé !");
            } 
            else 
            {
                System.out.print("Description de la catégorie : ");
                String descr = scanner.nextLine();
                
                PreparedStatement insertStatement = connection.prepareStatement(
                        "INSERT INTO Categorie (NomCat, DescrCat) VALUES (?, ?)");
                insertStatement.setString(1, nom);
                insertStatement.setString(2,descr);

                insertStatement.executeUpdate();
                System.out.println("Création de la catégorie réussie !");
            }
        }
        catch (SQLException e) 
        {
            System.err.println("Erreur lors de la création de la catégorie");
        }

    }
}
