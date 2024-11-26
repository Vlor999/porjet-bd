import java.sql.*;
import java.util.Scanner;

public class etablirUtilisateur 
{
    public static void afficherTousLesUtilisateurs(Connection connection, Scanner scanner) {
        try {
            Statement stmt = connection.createStatement();
            ResultSet res = stmt.executeQuery("SELECT * FROM Utilisateur");
    
            // Afficher l'en-tête
            String header = String.format("%-40s %-20s %-20s %-50s", "Email", "Nom", "Prénom", "Adresse Postale");
            System.out.println("-".repeat(header.length()));
            System.out.println(header);
            System.out.println("-".repeat(header.length()));
    
            // Afficher les données
            while (res.next()) {
                String row = String.format("%-40s %-20s %-20s %-50s",
                        res.getString("email"),
                        res.getString("nom"),
                        res.getString("prenom"),
                        res.getString("ADRESSEPOSTALE"));
                System.out.println(row);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    

    public static void afficherUtilisateurSpecifique(Connection connection, Scanner scanner) 
    {
        ResultSet res = null;
        // Requete parametree "?"
        try 
        {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM Utilisateur WHERE email LIKE ?");
            System.out.print("email de l'utilisateur : ");
            String emailUtilisateur = scanner.next();
            scanner.nextLine();
            statement.setString(1, emailUtilisateur);
            res = statement.executeQuery();
        }
        catch (SQLException e) 
        {
            e.printStackTrace ();
        }
        // Traitement des resultats
        try 
        {
            // Afficher l'en-tête
            String header = String.format("%-40s %-20s %-20s %-50s", "Email", "Nom", "Prénom", "Adresse Postale");
            System.out.println("-".repeat(header.length()));
            System.out.println(header);
            System.out.println("-".repeat(header.length()));

            while (res.next()) {
                String row = String.format("%-40s %-20s %-20s %-50s",
                        res.getString("email"),
                        res.getString("nom"),
                        res.getString("prenom"),
                        res.getString("ADRESSEPOSTALE"));
                System.out.println(row);
            }
        } 
        catch ( SQLException e ) 
        {
            e.printStackTrace ();
        }
        
    }
}