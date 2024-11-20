import java.sql.*;
import java.util.Scanner;

public class etablirCategorie {
    public static void afficherToutesCategories(Connection connection, Scanner scanner){
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

    public static void afficherCategorieSpecifique(Connection connection, Scanner scanner){

        ResultSet res = null;
        // Requete parametree "?"
        try {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM Categorie WHERE NomCat LIKE ?");
            System.out.print("Nom de la catégorie : ");
            String nomcat = scanner.nextLine();
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

    public static void creerNouvelleCategorie(Connection connection, Scanner scanner){
        try{
            System.out.print("Veuillez entrer le nom de la catégorie : ");
            String nom = scanner.nextLine();

            PreparedStatement checkStatement = connection.prepareStatement("SELECT * FROM Categorie WHERE NomCat LIKE ?");
            checkStatement.setString(1, nom);
            ResultSet res = checkStatement.executeQuery();

            if (res.next()) {
                System.out.println("Ce nom est déjà utilisé !");
            } else {
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
        catch (SQLException e) {
            e.printStackTrace();
        }

    }
}
