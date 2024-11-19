import java.sql.*;
import java.util.Scanner;
import static java.lang.System.exit;


public class mainInterface {
    private Connection connection;

    public mainInterface(Connection connection) {
        this.connection = connection;
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
        System.out.println("Bienvenue ! Veuillez vous authentifier pour accéder à toutes les fonctionnalités.");
        boolean auth = false;
        if (!auth){
            authentifierUtilisateur();
            auth = true;
        }
        
        System.out.println("\nQue voulez-vous faire ?");
        System.out.println("1. Requête Utilisateur");
        System.out.println("2. Requête Salle de vente");
        System.out.println("3. Requete Vente");
        System.out.println("4. Requête Catégorie");
        System.out.println("5. Requête Caractéristiques");
        System.out.println("6. Requête Produit");
        System.out.println("7. Fermer la connexion");

        System.out.print("Votre choix : ");
        int choix = scanner.nextInt();
        scanner.nextLine(); // Consommer la nouvelle ligne
        
        switch (choix) {
            case 1:
                // Section utilisateur 
                System.out.println("1. Afficher tous les utilisateurs");
                System.out.println("2. Afficher les informations d'un utilisateur spécifique");
                System.out.print("Votre choix : ");
                int choix2 = scanner.nextInt();
                scanner.nextLine(); // Consommer la nouvelle ligne

                switch (choix2){
                    case 1:
                        etablirUtilisateur.afficherTousLesUtilisateurs(connection);
                        break;
                    case 2:
                        etablirUtilisateur.afficherUtilisateurSpecifique(connection);
                        break;
                    default:
                        System.out.println("Choix non valide.");
                }
                break;

            case 2:
                // Section salle de vente
                System.out.println("1. Afficher les salles de vente");
                System.out.println("2. Afficher les salles de vente disponibles");
                System.out.println("3. Déclarer une nouvelle salle de vente ");
                int choix3 = scanner.nextInt();
                scanner.nextLine(); // Consommer la nouvelle ligne

                switch (choix3){
                    case 1:
                        etablirSalleDeVente.afficherToutesLesSalles(connection);
                        break;
                    case 2:
                        etablirSalleDeVente.afficherToutesLesSallesDisponibles(connection);
                        break;
                    case 3:
                        etablirSalleDeVente.creerNouvelleSalleDeVente(connection);
                        break;
                    default:
                        System.out.println("Choix non valide.");
                }
                break;


            case 3:
                // Section vente
                System.out.println("1. Afficher les ventes");
                System.out.println("2. Déclarer une nouvelle vente");
                int choix4 = scanner.nextInt();
                scanner.nextLine(); // Consommer la nouvelle ligne

                switch (choix4){
                    case 1:
                        etablirVente.afficherToutesLesVentes(connection);
                        break;
                    case 2:
                        etablirVente.creerNouvelleVente(connection);
                        break;
                    default:
                        System.out.println("Choix non valide.");
                }
                break;
            case 4:
                // Section Catégorie
                System.out.println("1. Afficher les catégories");
                System.out.println("2. Afficher une catégorie spécifique");
                int choix5 = scanner.nextInt();
                scanner.nextLine(); // Consommer la nouvelle ligne

                switch (choix5){
                    case 1:
                        etablirCategorie.afficherToutesCategories(connection);
                        break;
                    case 2:
                        etablirCategorie.afficherCategorieSpecifique(connection);
                        break;
                    default:
                        System.out.println("Choix non valide.");
                }
                break;
            case 5:
                break;
            case 6: 
                break;
            case 7:
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
