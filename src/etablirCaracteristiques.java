import java.sql.*;
import java.util.Scanner;

public class etablirCaracteristiques {
    

    public static void afficherToutesLesCaracteristiques(Connection connection, Scanner scanner){
        try {
            Statement stmt = connection.createStatement();
            ResultSet res = stmt.executeQuery("SELECT NOMPRODUIT,NOMCAR,VALEURCAR FROM CARACTERISTIQUES JOIN PRODUIT ON PRODUIT.IDPRODUIT = CARACTERISTIQUES.IDPRODUIT ORDER BY PRODUIT.IDPRODUIT");
            while (res.next()) {
                System.out.println("Nom du produit : " + res.getString("NomProduit") + ", Nom de la caractéristique : " +  res.getString("NomCar") +
                ", Valeur de la caractéristique : " + res.getString("ValeurCar"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void afficherCaracteristiquesSpecifique(Connection connection, Scanner scanner){
        ResultSet res = null;
        try {
            PreparedStatement statement = connection.prepareStatement("SELECT NOMPRODUIT,NOMCAR,VALEURCAR FROM CARACTERISTIQUES JOIN PRODUIT ON PRODUIT.IDPRODUIT = CARACTERISTIQUES.IDPRODUIT AND NOMPRODUIT LIKE ? ");
            System.out.print("Nom du produit : ");
            String nomproduit = scanner.next();
            scanner.nextLine();
            statement.setString(1, nomproduit);
            res = statement.executeQuery();
            while (res.next()) {
                System.out.println("Nom du produit : " + res.getString("NomProduit") + ", Nom de la caractéristique : " +  res.getString("NomCar") +
                ", Valeur de la caractéristique : " + res.getString("ValeurCar"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
