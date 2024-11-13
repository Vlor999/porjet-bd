import java.sql.*;
import static java.lang.System.exit;

public class gererUtilisateur{
    public Connection connection;

    public gererUtilisateur(Connection connection){
        this.connection = connection;
    }

    public void printUtilisateurs(){
        try {
            Statement stmt = connection.createStatement();
            ResultSet res = stmt.executeQuery("SELECT * FROM Utilisateur");
            while(res.next()){
                System.out.println("Utilisateur " + "email : " + res.getString("email") + "-> nom : "+ res.getString("nom") + ", prenom : " + res.getString("prenom") + " adresse : " + res.getString("ADRESSEPOSTALE"));
            }
        }
        catch ( SQLException e ) 
        {
            e.printStackTrace ();
        }
    }


    public Connection cnxBaseDonnees(){
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
        return connection;
    }
}
