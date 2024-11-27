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
            int idSalleDeVente = user.getIdSalleDeVente();
            if(idSalleDeVente == -1)
            {
                throw new Exception("Veuillez choisir une salle de vente.");   
            }

            // On affiche les différents produits disponibles dans la salle de vente

            String sqlProduits = """
                SELECT * FROM Produit JOIN SALLEDEVENTE ON Produit.idsalle = SALLEDEVENTE.idsalle WHERE SALLEDEVENTE.idsalle = ?""";
                
            pstmt = connection.prepareStatement(sqlProduits);
            pstmt.setInt(1, idSalleDeVente);
            ResultSet res = pstmt.executeQuery();
            String line = String.format("| %-40s | %-20s | %-20s | %-50s |", "ID Produit", "Nom Produit", "Description Produit", "Prix Produit");
            System.out.println("-".repeat(line.length()));
            System.out.println(line);
            System.out.println("-".repeat(line.length()));
            while (res.next()) 
            {
                String row = String.format("%-40s %-20s %-20s %-50s",
                        res.getString("idProduit"),
                        res.getString("nomProduit"),
                        res.getString("descriptionProduit"),
                        res.getString("prixProduit"));
                System.out.println(row);
            }

            System.out.print("Entrez l'ID du produit : ");
            int idProduit = Integer.parseInt(scanner.nextLine());
            System.out.print("Entrez le montant de votre offre : ");
            BigDecimal montantOffre = new BigDecimal(scanner.nextLine());
            System.out.print("Entrez la quantité : ");
            int quantite = Integer.parseInt(scanner.nextLine());

            String sqlVerif = """
                SELECT V.PrixActuel, V.IdVente 
                FROM Vente V 
                JOIN Produit P ON V.IdProduit = P.IdProduit 
                WHERE P.IdProduit = ? AND P.DispoProduit = 1 
                AND V.DateVente <= CURRENT_DATE AND 
                (V.Duree = -1 OR SYSTIMESTAMP <= V.DateVente + NUMTODSINTERVAL(V.Duree, 'MINUTE'))
            """;
            pstmt = connection.prepareStatement(sqlVerif);
            pstmt.setInt(1, idProduit);
            ResultSet rs = pstmt.executeQuery();

            if (!rs.next()) {
                throw new Exception("Produit non trouvé ou vente terminée.");
            }

            BigDecimal prixActuel = new BigDecimal(rs.getInt("PrixActuel"));
            int idVente = rs.getInt("IdVente");
            if (!verifierOffre(montantOffre, prixActuel)) {
                throw new Exception("L'offre doit être supérieure au prix actuel.");
            }

            // Insérer l'offre
            String sqlInsert = 
            """
                INSERT INTO Offre (PrixOffre, DateOffre, HeureOffre, Quantite, Email, IdVente)
                VALUES (?, CURRENT_DATE, CURRENT_TIMESTAMP, ?, ?, ?)
            """;
            pstmt = connection.prepareStatement(sqlInsert);
            pstmt.setBigDecimal(1, montantOffre);
            pstmt.setInt(2, quantite);
            pstmt.setString(3, user.getIdUser());
            pstmt.setInt(4, idVente);

            pstmt.executeUpdate();
            connection.commit(); 

            // préciser si tout est bon
            System.out.println("Offre enregistrée avec succès.");

        } 
        catch (Exception e) 
        {
            if (connection != null) 
            {
                connection.rollback(); 
            }
            throw e;
        } 
        finally 
        {
            if (pstmt != null)
            {
                pstmt.close();
            } 
            if (connection != null) connection.close();
        }
    }

    // Méthode pour vérifier si l'offre est valide
    private static boolean verifierOffre(BigDecimal montantOffre, BigDecimal prixActuel) {
        return montantOffre.compareTo(prixActuel) > 0; // L'offre doit être supérieure au prix actuel
    }
}