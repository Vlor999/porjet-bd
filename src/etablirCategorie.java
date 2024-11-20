import java.sql.*;
import java.util.Scanner;

public class etablirCategorie {
    public static void afficherToutesCategories(Connection connection){
        try {
            Statement stmt = connection.createStatement();
            ResultSet res = stmt.executeQuery("SELECT * FROM Categorie");
            while (res.next()) {
                System.out.println("Nom de la catégorie : " + res.getString("NomCat") + ", Description : " +  res.getString("DescrCat"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void afficherCategorieSpecifique(Connection connection){

        ResultSet res = null;
        Scanner scanner = new Scanner(System.in);
        // Requete parametree "?"
        try {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM Categorie WHERE NomCat LIKE ?");
            System.out.print("Nom de la catégorie : ");
            String nomcat = scanner.next();
            scanner.nextLine();
            statement.setString(1, nomcat);
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
                System.out.println("Nom de la catégorie : " + res.getString("NomCat") + ", Description : " +  res.getString("DescrCat"));
            }
        } catch ( SQLException e ) {
            e.printStackTrace ();
        }
                
    }
}
