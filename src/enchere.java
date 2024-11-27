import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;


public class enchere
{
    public static void afficherSallesDeVenteDisponibles(Connection connection, Scanner scanner) 
    {
        ResultSet res;
        try
        {
            Statement statement = connection.createStatement();
            res = statement.executeQuery("SELECT * FROM SALLEDEVENTE WHERE ESTOCCUPEE = 0");
            String header = String.format("|%-10s | %-10s | %-10s | %-10s | %-10s | %-10s | %-20s |", "Id", "Montante", "Occupee", "Revocable", "Limite", "Duree", "Catégorie");
            
            System.out.println("-".repeat(header.length()));
            System.out.println(header);
            System.out.println("-".repeat(header.length()));
            while(res.next())
            {
                String row = String.format("|%-10s | %-10s | %-10s | %-10s | %-10s | %-10s | %-20s |",
                        res.getString("IDSALLE"),
                        res.getString("ESTMONTANTE"),
                        res.getString("ESTOCCUPEE"),
                        res.getString("ESTREVOCABLE"),
                        res.getString("LIMITEOFFRES"),
                        res.getString("TYPEDUREE"),
                        res.getString("CATEGORIE")
                        );
                System.out.println(row);
            }
            System.out.println("-".repeat(header.length()));
        } catch (SQLException e){
            System.out.println("Erreur lors de la récupération des salles de vente disponibles.");
            e.printStackTrace();
        }
    }

    public static void afficherSallesDeVente(Connection connection, Scanner scanner) 
    {
        ResultSet res;
        try 
        {
            Statement statement = connection.createStatement();
            res = statement.executeQuery("SELECT * FROM SalleDeVente");

            String header = String.format("|%-10s | %-10s | %-10s | %-10s | %-10s | %-10s | %-20s |", "Id", "Montante", "Occupee", "Revocable", "Limite", "Duree", "Catégorie");
            
            System.out.println("-".repeat(header.length()));
            System.out.println(header);
            System.out.println("-".repeat(header.length()));
            while(res.next())
            {
                String row = String.format("|%-10s | %-10s | %-10s | %-10s | %-10s | %-10s | %-20s |",
                        res.getString("IDSALLE"),
                        res.getString("ESTMONTANTE"),
                        res.getString("ESTOCCUPEE"),
                        res.getString("ESTREVOCABLE"),
                        res.getString("LIMITEOFFRES"),
                        res.getString("TYPEDUREE"),
                        res.getString("CATEGORIE")
                        );
                System.out.println(row);
            }
            System.out.println("-".repeat(header.length()));
        } 
        catch (SQLException e) 
        {
            System.out.println("Erreur lors de la récupération des salles de vente.");
            e.printStackTrace();
        }
    }

    public static void entrerDansSalleDeVente(Connection connection, Scanner scanner, mainInterface user)
    {
        // Affichage des salles de vente disponibles pour l'utilisateur
        afficherSallesDeVenteDisponibles(connection, scanner);
        // Demande de l'id de la salle de vente
        System.out.print("Id de la salle de vente : ");
        int idSalleDeVente = scanner.nextInt();
        scanner.nextLine();
        // Demande de vérfication de l'existence de la salle de vente
        ResultSet res;
        try {
            Statement statement = connection.createStatement();
            res = statement.executeQuery("SELECT * FROM SalleDeVente WHERE IDSALLE = " + idSalleDeVente);
            if (!res.next()) 
            {
                System.out.println("La salle de vente n'existe pas.");
                return;
            }
            user.setIdSalleDeVente(idSalleDeVente);
        }
        catch (SQLException e) 
        {
            System.out.println("Erreur lors de la récupération de la salle de vente. Mauvaise ID");
            entrerDansSalleDeVente(connection, scanner, user);
            return;
        }

        // On est connecté à la salle de vente
        System.out.println("Vous êtes maintenant connecté à la salle de vente " + idSalleDeVente + ".");
    }
}

