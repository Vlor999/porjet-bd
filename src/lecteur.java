import java.sql.*;
import java.util.Scanner;

public class lecteur {

    public static boolean authentifierUtilisateur(Connection connection, Scanner scanner) {
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
                    return true;
                } 
                else 
                {
                    System.out.println("Email non trouvé dans la base. Voulez-vous réessayer ? (oui/non) : ");
                    String retry = scanner.nextLine().trim().toLowerCase();
                    if (retry.equals("oui")) {
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
                    System.out.println("Cet email est déjà utilisé.");
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
            e.printStackTrace();
        }
        return false;
    }


    public static void main(String[] args) 
    {
        // Pilote JDBC
        Scanner scanner = new Scanner(System.in); //IDEE UN SEUL SCNANER POUR TOUT LE CODE
        etablirConnexion.cnxPilote();
        Connection connection = etablirConnexion.cnxBaseDonnees();

        ajoutData ajoutData = new ajoutData(connection);
        try
        {
            ajoutData.deleteVente();
            ajoutData.deleteProduit();
            ajoutData.deleteSalleDeVente();
            ajoutData.deleteCat();
            ajoutData.deleteUser();
            ajoutData.ajoutUser();
            ajoutData.ajoutCat();
            ajoutData.ajoutProduit();
            // ajoutData.ajoutSalleDeVente();
            // ajoutData.ajoutVente();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        // Test de la classe gererUtilisateur
        mainInterface mainInterface = new mainInterface(connection);

        System.out.println("Bienvenue ! Veuillez vous authentifier pour accéder à toutes les fonctionnalités.");
        boolean result_auth = authentifierUtilisateur(connection, scanner);

        // Boucle pour afficher le menu tant que l'option 4 (fermer la connexion) n'est pas choisie
        if (result_auth)
        {
            boolean continuer = true;
            while (continuer) 
            {
                mainInterface.choisirAction(scanner);

                // Vérification si la connexion est fermée, si oui, quitter la boucle
                try {
                    if (connection.isClosed()) {
                        continuer = false; // Arrêter la boucle si la connexion est fermée
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    scanner.close();
    }
}
