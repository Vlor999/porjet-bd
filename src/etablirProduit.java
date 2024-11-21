import java.sql.*;
import java.util.Scanner;

public class etablirProduit 
{

    public static void afficherTousLesProduits(Connection connection, Scanner scanner)
    {
        try 
        {
            Statement stmt = connection.createStatement();
            ResultSet res = stmt.executeQuery("SELECT * FROM Produit");
            while (res.next()) 
            {
                System.out.println("Identifiant du produit : " + res.getString("IdProduit") + ", Nom du produit : " +  res.getString("NomProduit")
                + ", Prix Revient : " + res.getString("PrixRevient") + ", Stock : " + res.getString("Stock") + ", Disponibilité du produit " + res.getString("DispoProduit")
                + ", Nom de la catégorie : " + res.getString("NomCat"));
            }
        } 
        catch (SQLException e) 
        {
            e.printStackTrace();
        }
    }

    public static void afficherProduitSpécifique(Connection connection, Scanner scanner)
    {
        ResultSet res = null;
        try 
        {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM Produit WHERE NomProduit LIKE ?");
            System.out.print("Nom du produit : ");
            String nomcat = scanner.nextLine();
            statement.setString(1, "%" + nomcat + "%");
            res = statement.executeQuery();
            while (res.next()) 
            {
                System.out.println("Identifiant du produit : " + res.getString("IdProduit") + ", Nom du produit : " +  res.getString("NomProduit")
                + ", Prix Revient : " + res.getString("PrixRevient") + ", Stock : " + res.getString("Stock") + ", Disponibilité du produit " + res.getString("DispoProduit")
                + ", Nom de la catégorie : " + res.getString("NomCat"));
            }
        } 
        catch (SQLException e) 
        {
            e.printStackTrace();
        }
    }
    
    public static void afficherProduitsDispnibles(Connection connection, Scanner scanner)
    {
        try 
        {
            Statement stmt = connection.createStatement();
            ResultSet res = stmt.executeQuery("SELECT * FROM Produit WHERE DISPOPRODUIT = 1");
            while (res.next()) 
            {
                System.out.println("Identifiant du produit : " + res.getString("IdProduit") + ", Nom du produit : " +  res.getString("NomProduit")
                + ", Prix Revient : " + res.getString("PrixRevient") + ", Stock : " + res.getString("Stock") + ", Disponibilité du produit " + res.getString("DispoProduit")
                + ", Nom de la catégorie : " + res.getString("NomCat"));
            }
        } 
        catch (SQLException e) 
        {
            e.printStackTrace();
        }
    }
}
