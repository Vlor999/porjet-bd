import java.sql.*;
import java.util.Scanner;

public class lecteur {
    public static void main(String[] args)
    {
        // Pilote JDBC
        try
        {
            DriverManager.registerDriver(
                new oracle.jdbc.driver.OracleDriver()
            );
        }
        catch (SQLException e)
        {
        e.printStackTrace();
        }


        Connection connection = null; // Déclarer connection ici
        //  Etablir connexion
        try{
            String url = "jdbc:oracle:thin:@oracle1.ensimag.fr:1521:oracle1";
            String user = "adnetw";
            String mdp = "adnetw";

            connection = DriverManager.getConnection(url, user, mdp);
        }
        catch(SQLException e){
        e.printStackTrace();
        }


        // Premiere Requete Simple
        try {
            Statement stmt = connection.createStatement();
            stmt.executeQuery("SELECT * FROM Utilisateur;");
            }
        catch ( SQLException e ) {
        e.printStackTrace ();
        }

        ResultSet res = null;
        // Requete parametree "?"
        try {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM Utilisateur WHERE prenom LIKE ?;");
            System.out.print("prenom de l ’ utilisateur : ");
            Scanner scan = new Scanner(System.in);
            String nomUtilisateur = scan.next();
            scan.nextLine();
            statement.setString(1,nomUtilisateur);
            res = statement.executeQuery();
        }
        catch ( SQLException e ) {
        e.printStackTrace ();
        }


        // Traitement des resultats
        try {
            while(res.next()){
                System.out.println("Utilisateur " + res.getString("nom" )+ " -> email : " + res.getString("email"));
            }
        } catch ( SQLException e ) {
        e.printStackTrace ();
        }


        // Fermeture de la cnx
        try {
            connection.close();
        } catch ( SQLException e ) {
        e.printStackTrace();
        }
    }
}
