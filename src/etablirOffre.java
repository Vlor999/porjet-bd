import java.sql.*;
import java.util.Scanner;

public class etablirOffre {

    public static void afficherToutesLesOffres(Connection connection, Scanner scanner) {
        try {
            Statement stmt = connection.createStatement();
            ResultSet res = stmt.executeQuery("SELECT * FROM Offre ORDER BY idVente ASC, prixOffre DESC");
    
            // Afficher l'en-tête
            String header = String.format("║ %-10s ║ %-30s ║ %-10s ║ %-35s ║ %-15s ║", 
                "Prix Offre", "Heure Offre", "Quantité", "Email Utilisateur", "ID Vente");
            System.out.println("═".repeat(header.length()));
            System.out.println(header);
            System.out.println("═".repeat(header.length()));
            
            // Initialiser la variable pour suivre les changements d'idVente
            int lastIdVente = -1; // Une valeur qui ne peut pas exister dans la base de données
            
            // Afficher les résultats
            while (res.next()) {
                int currentIdVente = res.getInt("IDVENTE");
                
                // Si l'idVente change, insérer une ligne vide
                if (currentIdVente != lastIdVente && lastIdVente != -1) {
                    System.out.println(); // Ligne vide
                }
                
                // Mettre à jour la dernière idVente traitée
                lastIdVente = currentIdVente;
    
                // Afficher la ligne courante
                String row = String.format("║ %-10s ║ %-30s ║ %-10d ║ %-35s ║ %-15d ║",
                        res.getDouble("PRIXOFFRE"),
                        res.getTimestamp("HEUREOFFRE"), // Récupère le timestamp
                        res.getInt("QUANTITE"),
                        res.getString("EMAIL"),
                        currentIdVente
                );
                System.out.println(row);
            }
            System.out.println("═".repeat(header.length()));
        } catch (SQLException e) {
            System.err.println("Erreur lors de l'affichage des offres");
            e.printStackTrace();
        }
    }
    

    public static void afficherOffreSpecifique(Connection connection, Scanner scanner) {
        ResultSet res = null;
        try {
            // Requête paramétrée pour récupérer les offres d'une vente spécifique
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM Offre WHERE idvente = ? ORDER BY idVente ASC, prixOffre DESC");
            System.out.print("ID de la vente : ");
            int idvente = scanner.nextInt();
            scanner.nextLine(); // Consommer la nouvelle ligne
            statement.setInt(1, idvente);
            res = statement.executeQuery();
        
            // Afficher l'en-tête
            String header = String.format("║ %-10s ║ %-30s ║ %-10s ║ %-35s ║ %-15s ║", 
                "Prix Offre", "Heure Offre", "Quantité", "Email Utilisateur", "ID Vente");
            System.out.println("═".repeat(header.length()));
            System.out.println(header);
            System.out.println("═".repeat(header.length()));
        
            // Afficher les résultats
            boolean found = false;
            while (res.next()) {
                found = true;
                String row = String.format("║ %-10s ║ %-30s ║ %-10d ║ %-35s ║ %-15s ║",
                        res.getDouble("PRIXOFFRE"),
                        res.getTimestamp("HEUREOFFRE"), // Récupère le timestamp
                        res.getInt("QUANTITE"),
                        res.getString("EMAIL"),
                        res.getInt("IDVENTE")
                );
                System.out.println(row);
            }
            if (!found) {
                System.out.println("Aucune offre trouvée pour l'ID de vente spécifié.");
            }
            System.out.println("═".repeat(header.length()));
        } catch (SQLException e) {
            System.err.println("Erreur lors de la recherche des offres");
            e.printStackTrace();
        }
    }
    
    public static void mettreAJourOffresDescendantes(Connection connection, Scanner scanner) {
        try {
            // Obtenir la date et l'heure actuelles
            Timestamp currentTimestamp = new Timestamp(System.currentTimeMillis());
            // Requête pour récupérer toutes les ventes actives sans offres
            PreparedStatement selectStmt = connection.prepareStatement(
                "SELECT V.IdVente, V.PrixDepart, V.Duree, V.HeureVente " +
                "FROM Vente V " +
                "LEFT JOIN Offre O ON V.IdVente = O.IdVente " +
                "WHERE O.IdVente IS NULL " +
                "AND V.HeureVente + NUMTODSINTERVAL(V.Duree, 'MINUTE')) > ?"
            );
            selectStmt.setTimestamp(1, currentTimestamp);
    
            ResultSet res = selectStmt.executeQuery();
    
            // Préparer la mise à jour des prix
            PreparedStatement updateStmt = connection.prepareStatement(
                "UPDATE Vente SET PrixActuel = ? WHERE IdVente = ?"
            );
            // Parcourir les ventes actives sans offres et mettre à jour les prix
            while (res.next()) {
                int idVente = res.getInt("IdVente");
                int prixDepart = res.getInt("PrixDepart");
                int duree = res.getInt("Duree");
                // Calculer le temps écoulé
                Timestamp heureVente = res.getTimestamp("HeureVente");
                long tempsEcouleMinutes = (currentTimestamp.getTime() - heureVente.getTime()) / (60 * 1000);
    
                // Calculer le nouveau prix
                int prixActuel = prixDepart * (int)Math.max(0, (duree - tempsEcouleMinutes) / (double) duree);
    
                // Appliquer la mise à jour
                updateStmt.setInt(1, prixActuel);
                updateStmt.setInt(2, idVente);
                updateStmt.executeUpdate();
            }
    
            System.out.println("Mise à jour des offres descendantes effectuée avec succès !");
            res.close();
            selectStmt.close();
            updateStmt.close();
        } catch (SQLException e) {
            System.err.println("Erreur lors de la mise à jour des offres descendantes.");
            e.printStackTrace();
        }
    }
}
