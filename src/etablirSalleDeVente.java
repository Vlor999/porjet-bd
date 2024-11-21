import java.util.Scanner;
import java.sql.*;


public class etablirSalleDeVente 
{
    public static void afficherToutesLesSalles(Connection connection, Scanner scanner)
    {
        try 
        {
            Statement stmt = connection.createStatement();
            ResultSet res = stmt.executeQuery("SELECT * FROM SALLEDEVENTE");
            while (res.next()) 
            { // IDSALLE /, ESTREVOCABLE, ESTOCCUPEE/, ESTMONTANTE/, LIMITEOFFRES/, TYPEDUREE/, CATEGORIE/
                System.out.println(affiche(res));
            }
            res.close();
            stmt.close();
        } 
        catch (SQLException e) 
        {
            e.printStackTrace();
        }
    }

    public static void afficherSalleId(Connection connection, Scanner scanner)

    {
        try
        {
            System.out.print("Veuillez entrer l'identifiant de la salle : ");
            int id = scanner.nextInt();
            scanner.nextLine();
            PreparedStatement stmt = connection.prepareStatement("SELECT * FROM SALLEDEVENTE WHERE IDSALLE = ?");
            stmt.setInt(1, id);
            ResultSet res = stmt.executeQuery();
            if (res.next()) 
            {
                System.out.println(affiche(res));
            } 
            else 
            {
                System.out.println("Aucune salle avec cet identifiant n'a été trouvée !");
            }
            res.close();
            stmt.close();
        } 
        catch (SQLException e) 
        {
            e.printStackTrace();
        }
    }

    public static void afficherToutesLesSallesDisponibles(Connection connection, Scanner scanner)
    {
        try 
        {
            Statement stmt = connection.createStatement();
            ResultSet res = stmt.executeQuery("SELECT * FROM SALLEDEVENTE WHERE \"ESTOCCUPEE\" = 0");
            while (res.next()) 
            { // IDSALLE, ESTREVOCABLE, ESTOCCUPEE, ESTMONTANTE, LIMITEOFFRES, TYPEDUREE, CATEGORIE
                System.out.println(affiche(res));
            }
            res.close();
            stmt.close();
        } 
        catch (SQLException e) 
        {
            e.printStackTrace();
        }
    }

    public static void creerNouvelleSalleDeVente(Connection connection, Scanner scanner)
    {
        try
        {
            System.out.print("Veuillez entrer l'identifiant de la salle : ");
            int id = scanner.nextInt();
            scanner.nextLine();
            PreparedStatement checkStatement = connection.prepareStatement("SELECT * FROM SalleDeVente WHERE IDSALLE = ?");
            checkStatement.setInt(1, id);
            ResultSet res = checkStatement.executeQuery();
            if (res.next()) 
            {
                System.out.println("Cet identifiant est déjà utilisé !");
            } 
            else 
            {
                System.out.print("Est-ce que la salle est révocable (1 si oui, 0 sinon) : ");
                int revocable = scanner.nextInt();
                scanner.nextLine();
                System.out.print("Est-elle occupée (1 si oui, 0 sinon) : ");
                int occupe = scanner.nextInt();
                scanner.nextLine();
                System.out.print("Est-elle montante (1 si oui, 0 sinon) : ");
                int montante = scanner.nextInt();
                scanner.nextLine();
                System.out.print("Nombre d'offres possibles (-1 s'il n'y a pas de limite) : ");
                int nombreOffres = scanner.nextInt();
                scanner.nextLine();
                System.out.print("Type de durée : (limitee ou illimitee) ");
                String typeDuree = scanner.nextLine();
                System.out.print("Nom de la catégorie : ");
                String nomCategorie = scanner.nextLine();

                PreparedStatement insertStatement = connection.prepareStatement(
                        "INSERT INTO SalleDeVente (IDSALLE, ESTMONTANTE, ESTOCCUPEE, ESTREVOCABLE, LIMITEOFFRES, TYPEDUREE, CATEGORIE) VALUES (?, ?, ?, ?, ?, ?, ?)");
                insertStatement.setInt(1, id);
                insertStatement.setInt(2, montante);
                insertStatement.setInt(3, occupe);
                insertStatement.setInt(4, revocable);
                insertStatement.setInt(5, nombreOffres);
                insertStatement.setString(6, typeDuree);
                insertStatement.setString(7, nomCategorie);

                insertStatement.executeUpdate();
                System.out.println("Création de la salle de vente réussie !");
                insertStatement.close();
            }
            res.close();
            checkStatement.close();
        }
        catch (SQLException e) 
        {
            e.printStackTrace();
        }
    }

    private static String affiche(ResultSet res)
    {
        try
        {
            if(res.next())
            {
                return "Identifiant de la salle : " + res.getString("IDSALLE") 
                    + ", Est Révocable : " + res.getString("ESTREVOCABLE")
                    + ", Est occupée : " + res.getString("ESTOCCUPEE") 
                    + ", Est montante : " + res.getString("ESTMONTANTE") 
                    + ", Nombre d'offres possibles : " + res.getString("LIMITEOFFRES") 
                    + ", Durée : " + res.getString("TYPEDUREE")
                    + ", Catégorie : " + res.getString("CATEGORIE");
            }
        }
        catch (SQLException e) 
        {
            e.printStackTrace();
        }
        return "";
    }
}
