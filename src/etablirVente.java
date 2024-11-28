import java.sql.*;
import java.util.Scanner;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.lang.Thread;

public class etablirVente {

    public static void afficherToutesLesVentes(Connection connection, Scanner scanner) {
        try {
            Statement stmt = connection.createStatement();
            ResultSet res = stmt.executeQuery("SELECT * FROM Vente");

            // Afficher l'en-tête
            String header = String.format(
                "| %-15s | %-15s | %-15s | %-10s | %-15s | %-15s | %-15s | %-15s |",
                "ID Vente","ID Produit", "Prix Départ", "Durée", "ID Salle", "Prix Actuel", "Date Vente", "Heure Vente"
            );
            System.out.println("-".repeat(header.length()));
            System.out.println(header);
            System.out.println("-".repeat(header.length()));

            // Afficher les données
            while (res.next()) {
                System.out.println(String.format(
                    "| %-15s | %-15s | %-15s | %-10s | %-15s | %-15s | %-15s | %-15s |",
                    res.getString("IdVente"),
                    res.getString("IdProduit"),
                    res.getString("PrixDepart"),
                    res.getString("Duree"),
                    res.getString("IdSalle"),
                    res.getString("PrixActuel"),
                    res.getString("DateVente"),
                    res.getString("HeureVente")
                ));
            }
            System.out.println("-".repeat(header.length()));

            res.close();
            stmt.close();
        } 
        catch (SQLException e) 
        {
            System.err.println("Erreur lors de la récupération des ventes !");
        }
    }

    public static void afficherVentesEnCours(Connection connection,Scanner scanner){
        
        // UTILISATION DE HEURE POUR SAVOIR SI C EN COURS 
        try {
            HeureDate hd = new HeureDate();
            PreparedStatement stmt = connection.prepareStatement("SELECT * FROM Vente WHERE DUREE <> -1 AND DATEVENTE > ? OR (DATEVENTE = ? AND HEUREVENTE >= ?)  ");
            stmt.setString(1, hd.getDate());
            stmt.setString(2, hd.getDate());
            stmt.setString(3, hd.getHeure());
            ResultSet res = stmt.executeQuery();

            // Afficher l'en-tête
            String header = String.format(
                "| %-15s | %-15s | %-15s | %-10s | %-15s | %-15s | %-15s | %-15s |",
                "ID Vente","ID Produit", "Prix Départ", "Durée", "ID Salle", "Prix Actuel", "Date Vente", "Heure Vente"
            );
            System.out.println("-".repeat(header.length()));
            System.out.println(header);
            System.out.println("-".repeat(header.length()));

            // Afficher les données
            while (res.next()) {
                System.out.println(String.format(
                    "| %-15s | %-15s | %-15s | %-10s | %-15s | %-15s | %-15s | %-15s |",
                    res.getString("IdVente"),
                    res.getString("IdProduit"),
                    res.getString("PrixDepart"),
                    res.getString("Duree"),
                    res.getString("IdSalle"),
                    res.getString("PrixActuel"),
                    res.getString("DateVente"),
                    res.getString("HeureVente")
                ));
            }
            System.out.println("-".repeat(header.length()));

            res.close();
            stmt.close();
        } 
        catch (SQLException e) 
        {
            System.err.println("Erreur lors de la récupération des ventes !");
        }
    }


    public static void creerNouvelleVente(Connection connection, Scanner scanner) {
        try {
            System.out.print("Veuillez entrer l'identifiant de la vente : ");
            int id = scanner.nextInt();
            scanner.nextLine();

            PreparedStatement checkStatement = connection.prepareStatement("SELECT * FROM Vente WHERE IdVente = ?");
            checkStatement.setInt(1, id);
            ResultSet res = checkStatement.executeQuery();

            if (res.next()) {
                System.out.println("Cet identifiant est déjà utilisé !");
            } else {
                System.out.print("Identifiant de la salle : ");
                int idsalle = scanner.nextInt();
                scanner.nextLine();

                System.out.print("Identifiant du produit : ");
                int idproduit = scanner.nextInt();
                scanner.nextLine();            
                

                System.out.print("Prix de départ : ");
                int prixdepart = scanner.nextInt();
                scanner.nextLine();

                System.out.print("Prix actuel de la vente : ");
                int prixactuel = scanner.nextInt();
                scanner.nextLine();

                System.out.print("Durée de la vente (en minutes, -1 s'il n'y a pas de limite) : ");
                int duree = scanner.nextInt();
                scanner.nextLine();

                System.out.print("Date de la vente (format YYYY-MM-DD) : ");
                String dateVente = scanner.nextLine();

                System.out.print("Heure de la vente (format HH:MM:SS) : ");
                String heureVente = scanner.nextLine();

                PreparedStatement insertStatement = connection.prepareStatement(
                    "INSERT INTO Vente (IdVente, IdSalle, IdProduit, PrixDepart, PrixActuel, Duree, DateVente, HeureVente) VALUES (?, ?, ?, ?, ?, ?, ?, ?)"
                );
                insertStatement.setInt(1, id);
                insertStatement.setInt(2, idsalle);
                insertStatement.setInt(3, idproduit);
                insertStatement.setDouble(4, prixdepart);
                insertStatement.setDouble(5, prixactuel);
                insertStatement.setInt(6, duree);
                insertStatement.setString(7, dateVente);
                insertStatement.setString(8, heureVente);

                insertStatement.executeUpdate();
                
                PreparedStatement checkStatement2 = connection.prepareStatement("SELECT PRODUIT.NOMCAT FROM PRODUIT JOIN VENTE ON VENTE.IDPRODUIT = PRODUIT.IDPRODUIT WHERE PRODUIT.IDPRODUIT = ?");
                checkStatement2.setInt(1,idproduit);
                ResultSet res2 = checkStatement2.executeQuery();
                PreparedStatement checkStatement3 = connection.prepareStatement("SELECT SALLEDEVENTE.CATEGORIE FROM SALLEDEVENTE JOIN VENTE ON SALLEDEVENTE.IDSALLE = VENTE.IDSALLE WHERE SALLEDEVENTE.IDSALLE = ?");
                checkStatement3.setInt(1, idsalle);
                ResultSet res3 = checkStatement3.executeQuery();
                if (res2.next() && res3.next()) 
                { // Assurez-vous qu'il y a des résultats
                    if (!res2.getString("NOMCAT").equals(res3.getString("CATEGORIE"))) 
                    {
                        System.out.println("Erreur : les catégories de la salle de vente et du produit ne correspondent pas !");
                        PreparedStatement smt = connection.prepareStatement("DELETE FROM Vente WHERE IDPRODUIT = ? AND IDSALLE = ?");
                        smt.setInt(1,idproduit);
                        smt.setInt(2,idsalle);
                        smt.executeQuery();                        
                    } 
                    else 
                    {
                        System.out.println("Création de la vente réussie !");
                    }
                }
                res2.close();
                res3.close();
                checkStatement2.close();
                checkStatement3.close();
                insertStatement.close();
            }
            res.close();
            checkStatement.close();
        } 
        catch (SQLException e) 
        {
            System.err.println("Erreur lors de la creation de la vente!");
        }
    }
}
