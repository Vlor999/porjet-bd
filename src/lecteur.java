import java.sql.*;
import java.util.Scanner;
import static java.lang.System.exit;

public class lecteur {
    public static void main(String[] args)
    {
        // Pilote JDBC
        System.out.print("Connexion au pilote ...");
        try
        {
            DriverManager.registerDriver(
                new oracle.jdbc.OracleDriver()
            );
            System.out.println("\nConnexion établie !");

        }
        catch (SQLException e)
        {
            e.printStackTrace();
            System.out.println("\n=== Erreur lors du chargement du pilote ===");
            exit(1);
        }


        System.out.print("Connexion à la base de données ...");
        Connection connection = null; // Déclarer connection ici
        //  Etablir connexion
        try{
            String url = "jdbc:oracle:thin:@oracle1.ensimag.fr:1521:oracle1";
            String user = "adnetw";
            String mdp = "adnetw";

            connection = DriverManager.getConnection(url, user, mdp);
        }
        catch(SQLException e)
        {
            e.printStackTrace();
            System.out.println("\n=== Erreur lors de la connexion à la base de données ===");
            exit(1);
        }
        System.out.println("\nConnexion établie");

        // Premiere Requete Simple
        try {
            Statement stmt = connection.createStatement();
            ResultSet res = stmt.executeQuery("SELECT * FROM Utilisateur");
            while(res.next()){
                System.out.println("Utilisateur " + "email : " + res.getString("email") + "-> nom : "+ res.getString("nom") + ", prenom : " + res.getString("prenom"));
            }
        }
        catch ( SQLException e ) 
        {
            e.printStackTrace ();
            exit(1);
        }

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
            while(res.next()){
                System.out.println("Utilisateur " + "email : " + res.getString("email") + "-> nom : "+ res.getString("nom") + ", prenom : " + res.getString("prenom"));
            }
        } catch ( SQLException e ) {
            e.printStackTrace ();
        }


        // Fermeture de la cnx
        try {
            connection.close();
            scan.close();
        } catch ( SQLException e ) {
        e.printStackTrace();
        }
    }
}
