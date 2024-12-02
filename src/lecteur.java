import java.sql.*;
import java.util.Scanner;
import java.sql.Timestamp;
public class lecteur 
{
    private static String idUser = "";
    public static Timestamp precTime = new Timestamp(System.currentTimeMillis());


    public static boolean authentifierUtilisateur(Connection connection, Scanner scanner) 
    {
        System.out.print("Êtes-vous déjà membre ? (oui/non/exit) : ");
        String reponse = scanner.nextLine().trim().toLowerCase();

        try 
        {
            if (reponse.equals("oui")) 
            {
                // Cas où l'utilisateur est déjà membre
                System.out.print("Veuillez entrer votre email : ");
                String email = scanner.nextLine();

                PreparedStatement statement = connection.prepareStatement("SELECT * FROM Utilisateur WHERE email = ?");
                statement.setString(1, email);
                ResultSet res = statement.executeQuery();

                if (res.next()) 
                {
                    System.out.println("Connexion réussie. Bienvenue " + res.getString("prenom") + " " + res.getString("nom") + " !");
                    idUser = res.getString("EMAIL");
                    return true;
                } 
                else 
                {
                    System.out.println("Email non trouvé dans la base. Voulez-vous réessayer ? (oui/non) : ");
                    String retry = scanner.nextLine().trim().toLowerCase();
                    if (retry.equals("oui")) 
                    {
                        return authentifierUtilisateur(connection, scanner); // Appel récursif pour réessayer
                    } 
                    else 
                    {
                        System.out.println("Fin de la procédure.");
                        return authentifierUtilisateur(connection, scanner);
                    }
                }
            } 
            else if (reponse.equals("non")) 
            {
                // Cas où l'utilisateur n'est pas membre
                System.out.print("Veuillez entrer votre email : ");
                String email = scanner.nextLine();

                PreparedStatement checkStatement = connection.prepareStatement("SELECT * FROM Utilisateur WHERE email = ?");
                checkStatement.setString(1, email);
                ResultSet res = checkStatement.executeQuery();

                if (res.next()) 
                {
                    System.out.println("Cet email est déjà utilisé. Voulez-vous réessayer ? (oui/non) : ");
                    String retry = scanner.nextLine().trim().toLowerCase();
                    if (retry.equals("oui")) 
                    {
                        return authentifierUtilisateur(connection, scanner); // Appel récursif pour réessayer
                    } 
                    else 
                    {
                        System.out.println("Fin de la procédure.");
                        return authentifierUtilisateur(connection, scanner);
                    }
                } 
                else 
                {
                    System.out.print("Nom : ");
                    String nom = scanner.nextLine();
                    System.out.print("Prénom : ");
                    String prenom = scanner.nextLine();
                    System.out.print("Adresse postale : ");
                    String adressePostale = scanner.nextLine();

                    PreparedStatement insertStatement = connection.prepareStatement(
                            "INSERT INTO Utilisateur (email, nom, prenom, ADRESSEPOSTALE) VALUES (?, ?, ?, ?)");
                    insertStatement.setString(1, email);
                    insertStatement.setString(2, nom);
                    insertStatement.setString(3, prenom);
                    insertStatement.setString(4, adressePostale);

                    insertStatement.executeUpdate();
                    System.out.println("Inscription réussie. Vous êtes maintenant membre !");
                    return true;
                }
            }
            else if (reponse.equals("exit"))
            {
                System.out.println("Fin de la procédure.");
                return false;
            } 
            else 
            {
                System.out.println("Réponse non valide.");
                return authentifierUtilisateur(connection, scanner); // Appel récursif pour réessayer
            }
        } 
        catch (SQLException e) 
        {
            System.err.println("Erreur lors de l'authentification de l'utilisateur.");
        }
        return false;
    }

    public static void main(String[] args) 
    {
        // Pilote JDBC
        Scanner scanner = new Scanner(System.in);
        etablirConnexion.cnxPilote();
        Connection connection = etablirConnexion.cnxBaseDonnees();
    
        if (args.length > 0 && args[0].equalsIgnoreCase("new")) 
        {
            // Réinitialisation de la base
            ajoutData ajoutData = new ajoutData(connection);
            try 
            {
                ajoutData.deleteAny("Offre");
                ajoutData.deleteAny("Vente");
                ajoutData.deleteAny("Caracteristiques");
                ajoutData.deleteAny("Produit");
                ajoutData.deleteAny("SalleDeVente");
                ajoutData.deleteAny("Categorie");
                ajoutData.deleteAny("Utilisateur");
                ajoutData.ajoutUser();
                ajoutData.ajoutCat();
                ajoutData.ajoutSalleDeVente();
                ajoutData.ajoutProduit();
                ajoutData.ajoutCarac();
                ajoutData.ajoutVente();
                ajoutData.ajoutOffre();
                System.out.println("Base de données réinitialisée avec succès.");
            } 
            catch (Exception e) 
            {
                System.err.println("Erreur lors de la réinitialisation de la base de données.");
            }
        }
        else if (args.length > 0 && args[0].equalsIgnoreCase("clean")) 
        {
            // Nettoyage de la base
            ajoutData ajoutData = new ajoutData(connection);
            try 
            {
                ajoutData.deleteAny("Vente");
                ajoutData.deleteAny("Caracteristiques");
                ajoutData.deleteAny("Produit");
                ajoutData.deleteAny("SalleDeVente");
                ajoutData.deleteAny("Categorie");
                ajoutData.deleteAny("Utilisateur");
                System.out.println("Base de données nettoyée avec succès.");
            } 
            catch (Exception e) 
            {
                System.err.println("Erreur lors du nettoyage de la base de données.");
            }
        } 
        else if (args.length > 0 && args[0].equalsIgnoreCase("test")) 
        {
            // Mode test : pas d'authentification
            System.out.println("Mode test activé. Saut de l'authentification.");
            mainInterface mainInterface = new mainInterface(connection);
            Timestamp precTime = new Timestamp(System.currentTimeMillis());
            boolean continuer = true;
            while (continuer) 
            {
                mainInterface.choisirAction(scanner);
    
                // Vérification si la connexion est fermée, si oui, quitter la boucle
                try 
                {
                    if (connection.isClosed()) 
                    {
                        continuer = false;
                    }
                } 
                catch (SQLException e) 
                {
                    System.err.println("Erreur lors de la vérification de la connexion.");
                }
            }
        } 
        else 
        {
            // Authentification utilisateur requise
            System.out.println("Aucune réinitialisation de la base de données. Connexion à la base existante...");
        }
        // Test de la classe gererUtilisateur
        mainInterface mainInterface = new mainInterface(connection);
        
        System.out.println("Bienvenue ! Veuillez vous authentifier pour accéder à toutes les fonctionnalités.");
        boolean result_auth = authentifierUtilisateur(connection, scanner);
        mainInterface.setIdUser(idUser);

        // Boucle pour afficher le menu tant que l'option 4 (fermer la connexion) n'est pas choisie
        if (result_auth)
        {
            boolean continuer = true;
            while (continuer) 
            {
                mainInterface.choisirAction(scanner);

                // Vérification si la connexion est fermée, si oui, quitter la boucle
                try 
                {
                    if (connection.isClosed()) 
                    {
                        continuer = false; // Arrêter la boucle si la connexion est fermée
                    }
                } 
                catch (SQLException e) 
                {
                    System.err.println("Erreur lors de la vérification de la connexion.");
                }
            }
        }
        
    
        scanner.close();
    }    
}
