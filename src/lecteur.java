import java.sql.*;
import java.util.Scanner;
import static java.lang.System.exit;

public class lecteur {
    public static void main(String[] args)
    {
        // Pilote JDBC
        etablirConnexion.cnxPilote();
        Connection connection = etablirConnexion.cnxBaseDonnees();

        gererUtilisateur gererUtilisateur = new gererUtilisateur(connection);
        gererUtilisateur.printUtilisateurs();

        ResultSet res = null;
        Scanner scan = new Scanner(System.in);
        // Requete parametree "?"
        try {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM Utilisateur WHERE prenom LIKE ?");
            System.out.print("prenom de l'utilisateur : ");
            String nomUtilisateur = scan.next();
            scan.nextLine();
            statement.setString(1, nomUtilisateur);
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
                System.out.println("Utilisateur " + "email : " + res.getString("email") + "-> nom : "+ res.getString("nom") + ", prenom : " + res.getString("prenom"));
            }
        } catch ( SQLException e ) {
            e.printStackTrace ();
        }


        // Fermeture de la cnx
        try {
            connection.close();
            scan.close();
        } 
        catch (SQLException e) 
        {
            e.printStackTrace();
        }
    }
}
