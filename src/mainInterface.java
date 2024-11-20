import java.sql.*;
import java.util.Scanner;
import static java.lang.System.exit;


public class mainInterface {
    private Connection connection;

    public mainInterface(Connection connection) {
        this.connection = connection;
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


    public void choisirAction(Scanner scanner) {
        
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
                        etablirUtilisateur.afficherTousLesUtilisateurs(connection,scanner);
                        break;
                    case 2:
                        etablirUtilisateur.afficherUtilisateurSpecifique(connection, scanner);
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
                System.out.print("Votre choix : ");
                int choix3 = scanner.nextInt();
                scanner.nextLine(); // Consommer la nouvelle ligne

                switch (choix3){
                    case 1:
                        etablirSalleDeVente.afficherToutesLesSalles(connection, scanner);
                        break;
                    case 2:
                        etablirSalleDeVente.afficherToutesLesSallesDisponibles(connection, scanner);
                        break;
                    case 3:
                        etablirSalleDeVente.creerNouvelleSalleDeVente(connection, scanner);
                        break;
                    default:
                        System.out.println("Choix non valide.");
                }
                break;


            case 3:
                // Section vente
                System.out.println("1. Afficher les ventes");
                System.out.println("2. Déclarer une nouvelle vente");
                System.out.print("Votre choix : ");
                int choix4 = scanner.nextInt();
                scanner.nextLine(); // Consommer la nouvelle ligne

                switch (choix4){
                    case 1:
                        etablirVente.afficherToutesLesVentes(connection,scanner);
                        break;
                    case 2:
                        etablirVente.creerNouvelleVente(connection,scanner);
                        break;
                    default:
                        System.out.println("Choix non valide.");
                }
                break;
            case 4:
                // Section Catégorie
                System.out.println("1. Afficher les catégories");
                System.out.println("2. Afficher une catégorie spécifique");
                System.out.print("Votre choix : ");
                int choix5 = scanner.nextInt();
                scanner.nextLine(); // Consommer la nouvelle ligne

                switch (choix5){
                    case 1:
                        etablirCategorie.afficherToutesCategories(connection, scanner);
                        break;
                    case 2:
                        etablirCategorie.afficherCategorieSpecifique(connection, scanner);
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
