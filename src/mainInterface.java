import java.sql.*;
import java.util.Scanner;
import java.util.function.BiConsumer;

import static java.lang.System.exit;


public class mainInterface 
{
    private Connection connection;

    public mainInterface(Connection connection) 
    {
        this.connection = connection;
    }

    public void fermerConnexion() {
        try 
        {
            if (connection != null && !connection.isClosed()) 
            {
                connection.close();
                System.out.println("Connexion fermée avec succès.");
            } 
            else 
            {
                System.out.println("La connexion est déjà fermée.");
            }
        } 
        catch (SQLException e) 
        {
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
        int choix;
        try
        {
            choix = scanner.nextInt();
            scanner.nextLine(); // Consommer la nouvelle ligne
        }
        catch (Exception e)
        {
            System.out.println("Choix non valide.");
            scanner.nextLine(); // Consommer la nouvelle ligne
            this.choisirAction(scanner);
            return;
        }
        
        switch (choix) 
        {
            case 1:
                // Section utilisateur
                this.choix1(scanner);
                break;
            case 2:
                // Section salle de vente
                this.choix2(scanner);
                break;
            case 3:
                // Section vente
                this.choix3(scanner);
                break;
            case 4:
                // Section Catégorie
                this.choix4(scanner);
                break;
            case 5:
                // Section Caractéristiques
                this.choix5(scanner);
                break;
            case 6: 
                // Section Produit
                this.choix6(scanner);
                break;
            case 7:
                // Fermer la connexion
                fermerConnexion();
                break;
            default:
                System.out.println("Choix non valide.");
                this.choisirAction(scanner);
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

    public void afficherMenuEtGererChoix(String[] options, BiConsumer<Integer, Scanner> gestionChoix, Scanner scanner) 
    // BiConsumer <=> a une fonction qui prend 2 arguments -> sorte de tuple qui prend la bonne sortie
    {
        System.out.println();
        for (int i = 0; i < options.length; i++) 
        {
            System.out.println((i + 1) + ". " + options[i]);
        }
        System.out.print("Votre choix : ");
        int choix;
        try 
        {
            choix = scanner.nextInt();
            scanner.nextLine(); // Consommer la nouvelle ligne
        } 
        catch (Exception e) 
        {
            System.out.println("Choix non valide.");
            scanner.nextLine(); // Consommer la nouvelle ligne
            afficherMenuEtGererChoix(options, gestionChoix, scanner);
            return;
        }
        if (choix < 1 || choix > options.length) 
        {
            System.out.println("Choix non valide.");
            afficherMenuEtGererChoix(options, gestionChoix, scanner);
        } 
        else if (choix != options.length) 
        {
            gestionChoix.accept(choix, scanner);
        }
    }

    public void choix1(Scanner scanner) 
    {
        String[] options = 
        {
            "Afficher tous les utilisateurs",
            "Afficher les informations d'un utilisateur spécifique",
            "retour"
        };
    
        afficherMenuEtGererChoix(options, (choix, sc) -> 
        {
            switch (choix) 
            {
                case 1 -> etablirUtilisateur.afficherTousLesUtilisateurs(connection, sc);
                case 2 -> etablirUtilisateur.afficherUtilisateurSpecifique(connection, sc);
            }
        }, scanner);
    }

    public void choix2(Scanner scanner) 
    {
        String[] options = 
        {
            "Afficher les salles de vente",
            "Afficher les salles de vente disponibles",
            "Déclarer une nouvelle salle de vente",
            "retour"
        };
    
        afficherMenuEtGererChoix(options, (choix, sc) -> 
        {
            switch (choix) 
            {
                case 1 -> etablirSalleDeVente.afficherToutesLesSalles(connection, sc);
                case 2 -> etablirSalleDeVente.afficherToutesLesSallesDisponibles(connection, sc);
                case 3 -> etablirSalleDeVente.creerNouvelleSalleDeVente(connection, sc);
            }
        }, scanner);
    }

    public void choix3(Scanner scanner) 
    {
        String[] options = 
        {
            "Afficher les ventes",
            "Déclarer une nouvelle vente",
            "retour"
        };
    
        afficherMenuEtGererChoix(options, (choix, sc) -> 
        {
            switch (choix) 
            {
                case 1 -> etablirVente.afficherToutesLesVentes(connection, sc);
                case 2 -> etablirVente.creerNouvelleVente(connection, sc);
            }
        }, scanner);
    }

    public void choix4(Scanner scanner)
    {
        String[] options = 
        {
            "Afficher les catégories",
            "Afficher une catégorie spécifique",
            "Créer une nouvelle catégorie",
            "retour"
        };
    
        afficherMenuEtGererChoix(options, (choix, sc) -> 
        {
            switch (choix) 
            {
                case 1 -> etablirCategorie.afficherToutesCategories(connection, sc);
                case 2 -> etablirCategorie.afficherCategorieSpecifique(connection, sc);
                case 3 -> etablirCategorie.creerNouvelleCategorie(connection, sc);
            }
        }, scanner);
    }

    public void choix5(Scanner scanner)
    {
        String[] options = 
        {
            "Afficher les caractéristiques des produits",
            "Afficher des caractéristiques spécifiques à un produit",
            "retour"
        };
    
        afficherMenuEtGererChoix(options, (choix, sc) -> 
        {
            switch (choix) 
            {
                case 1 -> etablirCaracteristiques.afficherToutesLesCaracteristiques(connection, sc);
                case 2 -> etablirCaracteristiques.afficherCaracteristiquesSpecifique(connection, sc);
            }
        }, scanner);
    }

    public void choix6(Scanner scanner)
    {
        String[] options = 
        {
            "Afficher les produits",
            "Afficher des produits spécifiques",
            "Afficher les produits disponibles",
            "retour"
        };
    
        afficherMenuEtGererChoix(options, (choix, sc) -> 
        {
            switch (choix) 
            {
                case 1 -> etablirProduit.afficherTousLesProduits(connection, sc);
                case 2 -> etablirProduit.afficherProduitSpécifique(connection, sc);
                case 3 -> etablirProduit.afficherProduitsDispnibles(connection, sc);
            }
        }, scanner);
    }
}
