import java.sql.*;
import java.util.Scanner;
import static java.lang.System.exit;


public class gererUtilisateur {
    public Connection connection;

    public gererUtilisateur(Connection connection) {
        this.connection = connection;
    }

    public void afficherTousUtilisateurs() {
        try {
            Statement stmt = connection.createStatement();
            ResultSet res = stmt.executeQuery("SELECT * FROM Utilisateur");
            while (res.next()) {
                System.out.println("Utilisateur " + "email : " + res.getString("email") + " -> nom : " + res.getString("nom") +
                        ", prenom : " + res.getString("prenom") + ", adresse : " + res.getString("ADRESSEPOSTALE"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void authentifierUtilisateur() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Êtes-vous déjà membre ? (oui/non) : ");
        String reponse = scanner.nextLine().trim().toLowerCase();

        try {
            if (reponse.equals("oui")) {
                // Cas où l'utilisateur est déjà membre
                System.out.print("Veuillez entrer votre email : ");
                String email = scanner.nextLine();

                PreparedStatement statement = connection.prepareStatement("SELECT * FROM Utilisateur WHERE email = ?");
                statement.setString(1, email);
                ResultSet res = statement.executeQuery();

                if (res.next()) {
                    System.out.println("Connexion réussie. Bienvenue " + res.getString("prenom") + " " + res.getString("nom") + " !");
                } else {
                    System.out.println("Email non trouvé dans la base. Voulez-vous réessayer ? (oui/non) : ");
                    String retry = scanner.nextLine().trim().toLowerCase();
                    if (retry.equals("oui")) {
                        authentifierUtilisateur(); // Appel récursif pour réessayer
                    } else {
                        System.out.println("Fin de la procédure.");
                    }
                }
            } else if (reponse.equals("non")) {
                // Cas où l'utilisateur n'est pas membre
                System.out.print("Veuillez entrer votre email : ");
                String email = scanner.nextLine();

                PreparedStatement checkStatement = connection.prepareStatement("SELECT * FROM Utilisateur WHERE email = ?");
                checkStatement.setString(1, email);
                ResultSet res = checkStatement.executeQuery();

                if (res.next()) {
                    System.out.println("Cet email est déjà utilisé.");
                } else {
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
                }
            } else {
                System.out.println("Réponse non valide.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public void afficherUtilisateur() {
        ResultSet res = null;
        Scanner scanner = new Scanner(System.in);
        // Requete parametree "?"
        try {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM Utilisateur WHERE email LIKE ?");
            System.out.print("email de l'utilisateur : ");
            String emailUtilisateur = scanner.next();
            scanner.nextLine();
            statement.setString(1, emailUtilisateur);
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
                System.out.println("Utilisateur " + "email : " + res.getString("email") + "-> nom : "+ res.getString("nom") + ", prenom : " + res.getString("prenom"));
            }
        } catch ( SQLException e ) {
            e.printStackTrace ();
        }
    }


    public void fermerConnexion() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("Connexion fermée avec succès.");
            } else {
                System.out.println("La connexion est déjà fermée.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public void choisirAction() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("\nQue voulez-vous faire ?");
        System.out.println("1. Afficher tous les utilisateurs");
        System.out.println("2. Afficher les informations d'un utilisateur spécifique");
        System.out.println("3. Authentification / Inscription");
        System.out.println("4. Fermer la connexion");

        System.out.print("Votre choix (1, 2, 3 ou 4) : ");
        int choix = scanner.nextInt();
        scanner.nextLine(); // Consommer la nouvelle ligne
        
        switch (choix) {
            case 1:
                afficherTousUtilisateurs();
                break;
            case 2:
                afficherUtilisateur();
                break;
            case 3:
                authentifierUtilisateur();
                break;
            case 4:
                fermerConnexion();
                break;
            default:
                System.out.println("Choix non valide.");
        }
    }

    public Connection cnxBaseDonnees() {
        System.out.print("Connexion à la base de données ...");
        Connection connection = null;
        try {
            String url = "jdbc:oracle:thin:@oracle1.ensimag.fr:1521:oracle1";
            String user = "adnetw";
            String mdp = "adnetw";

            connection = DriverManager.getConnection(url, user, mdp);
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("\n=== Erreur lors de la connexion à la base de données ===");
            exit(1);
        }
        System.out.println("\nConnexion établie");
        return connection;
    }
}
