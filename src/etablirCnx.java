import java.sql.*;
import static java.lang.System.exit;

public class etablirCnx{
    public void cnxPilote(){
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
