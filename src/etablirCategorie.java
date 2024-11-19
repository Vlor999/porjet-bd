import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.*;
import java.sql.SQLException;

public class etablirCategorie {
    public static void afficherToutesCategories(Connection connection){
        try {
            Statement stmt = connection.createStatement();
            ResultSet res = stmt.executeQuery("SELECT * FROM Categorie");
            while (res.next()) {
                System.out.println("Nom de la catégorie : " + res.getString("NomCat") + " -> Description : " +  res.getString("DescrCat"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    
}
