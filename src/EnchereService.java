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
            String sqlVerif = """
                SELECT P.IdProduit, P.NomProduit, V.PrixActuel, V.IdVente 
                FROM Vente V 
                JOIN Produit P ON V.IdProduit = P.IdProduit 
                WHERE P.DispoProduit = 1 AND V.IdSalle = ?
                AND TO_DATE(V.DateVente, 'YYYY-MM-DD') <= CURRENT_DATE 
                AND (V.Duree = -1 OR SYSTIMESTAMP <= TO_DATE(V.DateVente, 'YYYY-MM-DD') + (INTERVAL '1' MINUTE) * V.Duree)
            """; //j'ai considere ici dateVente comme dateFin de vente

            pstmt = connection.prepareStatement(sqlVerif);
            pstmt.setInt(1,user.getIdSalleDeVente());

            ResultSet res = pstmt.executeQuery();
            try
            {
                String line = String.format("| %-10s | %-10s | %-10s | %-10s |", "IdProduit", "NomProduit", "PrixActuel", "IdVente");
            
                System.out.println("-".repeat(line.length()));
                System.out.println(line);
                System.out.println("-".repeat(line.length()));

                if (!res.isBeforeFirst()) {
                    System.out.println("Aucun produit disponible pour cette salle de vente.");
                } else {
                    while(res.next())
                    {
                        String row = String.format("%-10s %-10s %-10s %-10s",
                            res.getString("IdProduit"),
                            res.getString("NomProduit"),
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

            //demander quelle vente est concerne par cette offre
            System.out.print("Entrez la vente pour laquelle vous voulez faire cette offre : ");
            int IdVente = Integer.parseInt(scanner.nextLine());

            // Vérfier si l'offre est valide

            ResultSet rs;
            try 
            {
                Statement statement = connection.createStatement();
                rs = statement.executeQuery("SELECT P.IdProduit, P.NomProduit, V.PrixActuel, V.IdVente FROM Vente V JOIN Produit P ON V.IdProduit = P.IdProduit WHERE IDPRODUIT = " + IdProduit);
                if (!rs.next()) 
                {
                    System.out.println("Le produit n'existe pas.");
                    return;
                }

                BigDecimal prixActuel = new BigDecimal(rs.getInt("PrixActuel"));
                int idVente = rs.getInt("IdVente");
            
                if (!verifierOffre(PrixOffre, prixActuel)) {
                                    throw new Exception("L'offre doit être supérieure au prix actuel.");
                }
                
            }
                catch (SQLException e) 
            {
                System.out.println("Erreur lors de la récupération du prix actuel du produit. Mauvaise ID");
                placerEnchere(connection, scanner, user);
                return;
            }
                
            // lorsque la verification est ok, on insère l'offre dans la BD
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
            connection.commit(); 
                
            // préciser si tout est bon
            System.out.println("Offre enregistrée avec succès.");
                
        } catch (Exception e) {
                
                    System.err.println("Erreur lors du placement de l'enchère: " + e.getMessage());
                    if (connection != null) {
                        connection.rollback(); 
                    }
                        throw e;
        } finally {
                        if (pstmt != null) pstmt.close();
        }
        
    }

    // Méthode pour vérifier si l'offre est valide
    private static boolean verifierOffre(BigDecimal PrixOffre, BigDecimal prixActuel) {
        return PrixOffre.compareTo(prixActuel) > 0; // L'offre doit être supérieure au prix actuel
    }

}