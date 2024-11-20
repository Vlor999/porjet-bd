import java.sql.*;
import java.util.Scanner;

public class etablirUtilisateur {

    public static void afficherTousLesUtilisateurs(Connection connection, Scanner scanner) {
        try {
            Statement stmt = connection.createStatement();
            ResultSet res = stmt.executeQuery("SELECT * FROM Utilisateur");
            while (res.next()) {
                System.out.println("Email : " + res.getString("email") + ", Nom : " + res.getString("nom") +
                        ", Prénom : " + res.getString("prenom") + ", Adresse : " + res.getString("ADRESSEPOSTALE"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void afficherUtilisateurSpecifique(Connection connection, Scanner scanner) {
        ResultSet res = null;
        // Requete parametree "?"
        try {
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
        try {
            while(res.next())
            {
                System.out.println("Email : " + res.getString("email") + ", Nom : "+ res.getString("nom") + ", Prénom : " + res.getString("prenom")+ ", Adresse : " + res.getString("ADRESSEPOSTALE"));
            }
        } catch ( SQLException e ) {
            e.printStackTrace ();
        }
        
    }

}