import static java.lang.System.currentTimeMillis;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.Scanner;

public class etablirVente {

    public static void afficherToutesLesVentes(Connection connection, Scanner scanner) {
        try {
            Statement stmt = connection.createStatement();
            ResultSet res = stmt.executeQuery("SELECT * FROM Vente");
    
            String header = String.format(
                "| %-10s | %-10s | %-15s | %-10s | %-10s | %-15s | %-10s | %-10s | %-10s |",
                "ID Vente", "ID Produit", "Prix Départ", "Durée", "ID Salle", "Prix Actuel", "Quantité", "Date Vente", "Heure Vente"
            );
            System.out.println("-".repeat(header.length()));
            System.out.println(header);
            System.out.println("-".repeat(header.length()));
    
            while (res.next()) {
                System.out.println(String.format(
                    "| %-10s | %-10s | %-15s | %-10s | %-10s | %-15s | %-10s | %-10s | %-10s |",
                    res.getInt("IdVente"),
                    res.getInt("IdProduit"),
                    res.getDouble("PrixDepart"),
                    res.getInt("Duree"),
                    res.getInt("IdSalle"),
                    res.getDouble("PrixActuel"),
                    res.getInt("Quantite"),
                    res.getDate("DateVente"),
                    res.getTimestamp("HeureVente")
                ));
            }
            System.out.println("-".repeat(header.length()));
    
            res.close();
            stmt.close();
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération des ventes !");
        }
    }
    
    public static void afficherVentesEnCours(Connection connection, Scanner scanner) {
        try {
            // Obtenir la date et l'heure actuelles du système
            Timestamp currentTimestamp = new Timestamp(System.currentTimeMillis());
            System.out.println(currentTimestamp);
            // Préparer la requête
            PreparedStatement stmt = connection.prepareStatement(
                "SELECT * FROM Vente " +
                "WHERE DUREE = -1 OR (DUREE > 0 AND ? < HeureVente AND ? > HeureVente - NUMTODSINTERVAL(DUREE, 'MINUTE')) " 
            );
            stmt.setTimestamp(1, currentTimestamp);
            stmt.setTimestamp(2, currentTimestamp);
            stmt.setTimestamp(3, currentTimestamp);

            ResultSet res = stmt.executeQuery();
        
            // Préparer l'en-tête
            String header = String.format(
                "| %-10s | %-10s | %-15s | %-10s | %-10s | %-15s | %-10s | %-10s | %-10s |",
                "ID Vente", "ID Produit", "Prix Départ", "Durée", "ID Salle", "Prix Actuel", "Quantité", "Date Vente", "Heure Vente"
            );
            System.out.println("-".repeat(header.length()));
            System.out.println(header);
            System.out.println("-".repeat(header.length()));
        
            // Afficher les résultats
            while (res.next()) {
                System.out.println(String.format(
                    "| %-10s | %-10s | %-15s | %-10s | %-10s | %-15s | %-10s | %-10s | %-10s |",
                    res.getInt("IdVente"),
                    res.getInt("IdProduit"),
                    res.getDouble("PrixDepart"),
                    res.getInt("Duree"),
                    res.getInt("IdSalle"),
                    res.getDouble("PrixActuel"),
                    res.getInt("Quantite"),
                    res.getDate("DateVente"), // Type DATE
                    res.getTimestamp("HeureVente") // Type TIMESTAMP
                ));
            }
            System.out.println("-".repeat(header.length()));
        
            res.close();
            stmt.close();
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération des ventes en cours !");
            e.printStackTrace();
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
                double prixdepart = scanner.nextDouble();
                scanner.nextLine();

                System.out.print("Prix actuel de la vente : ");
                double prixactuel = scanner.nextDouble();
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
                insertStatement.setDate(7, Date.valueOf(dateVente));
                insertStatement.setTimestamp(8, Timestamp.valueOf(dateVente + " " + heureVente));

                insertStatement.executeUpdate();
                System.out.println("Création de la vente réussie !");
                insertStatement.close();
            }
            res.close();
            checkStatement.close();
        } catch (SQLException e) {
            System.err.println("Erreur lors de la création de la vente : " + e.getMessage());
        }
    }
    
    public static void afficherEtatVente(Connection connection, int idVente) {
        try {
            String queryVente = "SELECT Duree, heureVente FROM Vente WHERE IdVente = ?";
            PreparedStatement stmtVente = connection.prepareStatement(queryVente);
            stmtVente.setInt(1, idVente);
            ResultSet resVente = stmtVente.executeQuery();

            if (!resVente.next()) {
                System.out.println("Vente introuvable.");
                return;
            }

            int duree = resVente.getInt("Duree");
            Timestamp heureDebutVente = resVente.getTimestamp("heureVente");
            Timestamp now = new Timestamp(System.currentTimeMillis());

            LocalDateTime heure = heureDebutVente.toLocalDateTime();
            LocalDateTime updatedHeure = heure.plusMinutes(duree);
            Timestamp heureFinVente = Timestamp.valueOf(updatedHeure);
           
        
            // System.out.println("Heure actuelle; "+now);
            // System.out.println("Heure du debut de la vente: "+heureDebutVente);
            // System.out.println("Duree de la vente: "+duree);
            // System.out.println("Heure du debut de la vente: "+heureFinVente);


            if (duree == -1 || now.before(heureFinVente)) {
                String queryMeilleureOffre = "SELECT Email, PrixOffre FROM Offre WHERE IdVente = ? ORDER BY PrixOffre DESC, DateOffre ASC, HeureOffre ASC FETCH FIRST 1 ROWS ONLY";
                PreparedStatement stmtOffre = connection.prepareStatement(queryMeilleureOffre);
                stmtOffre.setInt(1, idVente);
                ResultSet resOffre = stmtOffre.executeQuery();

                if (resOffre.next()) {
                    System.out.println("Meilleure offre actuelle :");
                    System.out.println("Utilisateur : " + resOffre.getString("Email"));
                    System.out.println("Prix offert : " + resOffre.getDouble("PrixOffre"));
                } else {
                    System.out.println("Aucune offre pour cette vente en cours.");
                }
                resOffre.close();
            } else {
                System.out.println("La vente est terminée.");
                FinEncheres.terminerEnchere(connection, idVente);
            }

            resVente.close();
            stmtVente.close();
        } catch (SQLException e) {
            System.err.println("Erreur lors de l'affichage de l'état de la vente : " + e.getMessage());
        }
    }
}
