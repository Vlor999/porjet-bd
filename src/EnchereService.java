import java.math.BigDecimal;
import java.sql.*;
import java.util.Scanner;

public class EnchereService {

    // Méthode pour placer une enchère (offre)
    public static void placerEnchere(Connection connection, Scanner scanner, mainInterface user) throws Exception 
    {
        PreparedStatement pstmt = null;
        try 
        {
            // Vérifier la validité de l'offre avec un JOIN
            connection.setAutoCommit(false);
            
            int idSalleDeVente = user.getIdSalleDeVente();
            if(idSalleDeVente == -1)
            {
                throw new Exception("Veuillez choisir une salle de vente.");   
            }

            // On affiche les différents produits disponibles dans la salle de vente
            String sqlVerif = """
                SELECT P.IdProduit, P.NomProduit, P.Stock, V.PrixActuel, V.IdVente 
                FROM Vente V 
                JOIN Produit P ON V.IdProduit = P.IdProduit 
                WHERE DispoProduit = 1 AND V.IdSalle = ?
            """; 

            pstmt = connection.prepareStatement(sqlVerif);
            pstmt.setInt(1,user.getIdSalleDeVente());

            ResultSet res = pstmt.executeQuery();
            try
            {
                String line = String.format("| %-10s | %-10s | %-10s | %-10s |%-10s |", "IdProduit", "NomProduit","Stock", "PrixActuel", "IdVente");
            
                System.out.println("-".repeat(line.length()));
                System.out.println(line);
                System.out.println("-".repeat(line.length()));

                if (!res.isBeforeFirst()) {
                    System.out.println("Aucun produit disponible pour cette salle de vente.");
                } else {
                    while(res.next())
                    {
                        String row = String.format("%-10s %-10s %-10s %-10s %-10s",
                            res.getString("IdProduit"),
                            res.getString("NomProduit"),
                            res.getString("Stock"),
                            res.getString("PrixActuel"),
                            res.getString("IdVente")
                            );
                        System.out.println(row);
                    }
                }

                System.out.println("-".repeat(line.length()));
            } catch (SQLException e){
                System.out.println("Produits non trouvés ou vente terminée.");
                e.printStackTrace();
            }
            
            // Demander quel produit l'utilisateur veut acheter

            System.out.print("Entrez l'ID du produit : ");
            int IdProduit = Integer.parseInt(scanner.nextLine());

            // Demander quel prix et quelle quantité
            System.out.print("Entrez le montant de votre offre : ");
            BigDecimal PrixOffre = new BigDecimal(scanner.nextLine());

            System.out.print("Entrez la quantité : ");
            int Quantite = Integer.parseInt(scanner.nextLine());

            // Demander quelle vente est concernée par cette offre
            System.out.print("Entrez la vente pour laquelle vous voulez faire cette offre : ");
            int IdVente = Integer.parseInt(scanner.nextLine());

            // Vérifier si l'offre est valide

            ResultSet rs = null;
        try {
            String sqlPrixActuel = """
                SELECT V.PrixActuel 
                FROM Vente V 
                WHERE V.IdProduit = ? AND V.IdSalle = ?
            """;
            pstmt = connection.prepareStatement(sqlPrixActuel);
            pstmt.setInt(1, IdProduit);
            pstmt.setInt(2, user.getIdSalleDeVente());
            rs = pstmt.executeQuery();

            if (rs.next()) {
                BigDecimal prixActuel = rs.getBigDecimal("PrixActuel");
                if (!verifierOffre(PrixOffre, prixActuel)) {
                    throw new Exception("L'offre doit être supérieure au prix actuel.");
                }

                // Insérer l'offre dans la base de données
                String sqlInsert = """
                    INSERT INTO Offre (PrixOffre, DateOffre, HeureOffre, Quantite, Email, IdVente)
                    VALUES (?, CURRENT_DATE, CURRENT_TIMESTAMP, ?, ?, ?)
                """;
                pstmt = connection.prepareStatement(sqlInsert);
                pstmt.setBigDecimal(1, PrixOffre);
                pstmt.setInt(2, Quantite);
                pstmt.setString(3, user.getIdUser());
                pstmt.setInt(4, IdVente);

                pstmt.executeUpdate();

                // Mise à jour du prix actuel si l'offre est valide
                if (PrixOffre.compareTo(prixActuel) > 0) {
                    String sqlUpdate = """
                        UPDATE Vente 
                        SET PrixActuel = ? 
                        WHERE IdVente = ?
                    """;
                    pstmt = connection.prepareStatement(sqlUpdate);
                    pstmt.setBigDecimal(1, PrixOffre); // Nouveau prix
                    pstmt.setInt(2, IdVente);

                    pstmt.executeUpdate();
                }

                connection.commit();
                System.out.println("Offre enregistrée et prix mis à jour avec succès.");

            } else {
                System.out.println("Le produit n'existe pas.");
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la récupération du prix actuel du produit.");
            if (connection != null) {
                connection.rollback();
            }
            throw e;
        }
        } catch (Exception e) {
                
                    System.err.println("Erreur lors du placement de l'enchère: " + e.getMessage());
                    if (connection != null) {
                        connection.rollback(); 
                    }
                        throw e;
        } finally {
                        if (pstmt != null) pstmt.close();
                        connection.setAutoCommit(true);
        }
        
    }

    // Méthode pour vérifier si l'offre est valide
    private static boolean verifierOffre(BigDecimal PrixOffre, BigDecimal prixActuel) {
        return PrixOffre.compareTo(prixActuel) > 0; // L'offre doit être supérieure au prix actuel
    }

}