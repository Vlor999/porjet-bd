import java.sql.*;
import java.util.Scanner;

public class EnchereService {

    // Méthode pour placer une enchère (offre)
    public static void placerEnchere(Connection connection, Scanner scanner, mainInterface user) throws Exception 
    {
        if (user.getIdSalleDeVente() == -1){
            System.out.println("\033[0;31mVous devez être dans une salle de vente pour pouvoir faire une offre !\033[0m");
            return;
        }
        PreparedStatement pstmt = null;
        try 
        {
            // Vérifier la validité de l'offre avec un JOIN
            connection.setAutoCommit(false);
            // On affiche les différents produits disponibles dans la salle de vente
            String sqlVerif = """
                SELECT P.IdProduit, P.NomProduit, V.Quantite, V.PrixActuel, V.IdVente, S.EstMontante, V.Duree, V.HeureVente
                FROM Vente V 
                JOIN Produit P ON V.IdProduit = P.IdProduit
                JOIN SalledeVente S ON V.IdSalle = S.IdSalle 
                WHERE DispoProduit = 1 AND S.IdSalle = ? AND (DUREE = -1 OR (DUREE > 0 AND ? < HeureVente AND ? > HeureVente - NUMTODSINTERVAL(DUREE, 'MINUTE'))) 
                """; 
                Timestamp currentTimestamp = new Timestamp(System.currentTimeMillis());
                pstmt = connection.prepareStatement(sqlVerif);
                pstmt.setInt(1,user.getIdSalleDeVente());
                pstmt.setTimestamp(2, currentTimestamp);
                pstmt.setTimestamp(3, currentTimestamp);
                ResultSet res = pstmt.executeQuery();
            
                boolean valeur_dedans = false;
            try
            {
                String line = String.format("| %-10s | %-30s | %-10s | %-10s |%-10s |%-10s|", "IdProduit", "NomProduit","Stock", "PrixActuel", "IdVente", "EstMontante");
            
                System.out.println("-".repeat(line.length()));
                System.out.println(line);
                System.out.println("-".repeat(line.length()));
                if (!res.isBeforeFirst()) {
                    System.out.println("Aucun produit disponible pour cette salle de vente.");
                } else {
                    while(res.next())
                    {                         
                        String row = String.format("| %-10s | %-30s | %-10s | %-10s |%-10s |%-10s |",
                            res.getString("IdProduit"),
                            res.getString("NomProduit"),
                            res.getString("Quantite"),
                            res.getString("PrixActuel"),
                            res.getString("IdVente"),
                            res.getString("EstMontante")
                            );
                            valeur_dedans = true;
                        System.out.println(row);
                    }
                }

                System.out.println("-".repeat(line.length()));
            } catch (SQLException e){
                System.out.println("Produits non trouvés ou vente terminée.");
                e.printStackTrace();
            }
            
            // Demander quel produit l'utilisateur veut acheter
            if (!valeur_dedans){
                return;
            }
            System.out.print("Entrez l'ID du produit : ");
            int IdProduit = Integer.parseInt(scanner.nextLine());

            // Demander quel prix et quelle quantité
            System.out.print("Entrez le montant de votre offre : ");
            int PrixOffre = Integer.parseInt(scanner.nextLine());

            System.out.print("Entrez la quantité : ");
            int Quantite = Integer.parseInt(scanner.nextLine());

            // Demander quelle vente est concernée par cette offre
            Statement stmt = connection.createStatement();
            res = stmt.executeQuery("SELECT * FROM Offre ORDER BY idVente ASC, prixOffre DESC");

            System.out.println("| %-10s | %-30s | %-10s | %-35s | %-15s |".formatted("PRIXOFFRE", "HEUREOFFRE", "QUANTITE", "EMAIL", "IDVENTE"));
            while (res.next()) {
                String row = String.format("| %-10s | %-30s | %-10s | %-35s | %-15s |",
                        res.getDouble("PRIXOFFRE"),
                        res.getTimestamp("HEUREOFFRE"), // Récupère le timestamp
                        res.getInt("QUANTITE"),
                        res.getString("EMAIL"),
                        res.getInt("IDVENTE")
                );
                System.out.println(row);
            }

            System.out.print("Entrez la vente pour laquelle vous voulez faire cette offre : ");
            int IdVente = Integer.parseInt(scanner.nextLine());

            // Vérifier si l'offre est valide

            ResultSet rs = null;
        try {
            String sqlPrixActuel = """
                SELECT V.PrixActuel, V.Quantite, S.EstMontante
                FROM Vente V 
                JOIN SalleDeVente S ON V.IDSALLE = S.IDSALLE
                WHERE V.IdProduit = ? AND V.IdSalle = ?
            """;
            pstmt = connection.prepareStatement(sqlPrixActuel);
            pstmt.setInt(1, IdProduit);
            pstmt.setInt(2, user.getIdSalleDeVente());
            rs = pstmt.executeQuery();

            if (rs.next()) {
                int prixActuel = rs.getInt("PrixActuel");
                int Stock = rs.getInt("Quantite");
                int montante = rs.getInt("EstMontante");

                if (PrixOffre <= prixActuel && montante == 1) {
                    System.out.println("\033[0;31mL'offre doit être supérieure au prix actuel (offre montante).\033[0m");
                    throw new Exception("L'offre doit être supérieure au prix actuel.");
                }


                if (Stock < Quantite) {
                    System.out.println("\033[0;31mLa quantité doit être inférieure à celle proposée.\033[0m");
                    throw new Exception("La quantité doit être inférieure à celle proposée.");
                }

                // Insérer l'offre dans la base de données
                String sqlInsert = """
                    INSERT INTO Offre (PrixOffre, HeureOffre, Quantite, Email, IdVente)
                    VALUES (?, CURRENT_TIMESTAMP, ?, ?, ?)
                """;
                pstmt = connection.prepareStatement(sqlInsert);
                pstmt.setInt(1, PrixOffre);
                pstmt.setInt(2, Quantite);
                pstmt.setString(3, user.getIdUser());
                pstmt.setInt(4, IdVente);

                pstmt.executeUpdate();

                // Mise à jour du prix actuel si l'offre est valide
                    String sqlUpdate = """
                        UPDATE Vente 
                        SET PrixActuel = ? 
                        WHERE IdVente = ?
                    """;
                    pstmt = connection.prepareStatement(sqlUpdate);
                    pstmt.setInt(1, PrixOffre); 
                    pstmt.setInt(2, IdVente);
                    pstmt.executeUpdate();

                System.out.println("\033[0;31mOffre enregistrée et prix mis à jour avec succès. \033[0m");

                if (montante == 0){
                    FinEncheres.terminerEnchere(connection, IdVente);
                }
                connection.commit();


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
                
                    System.err.println("\033[0;31mErreur lors du placement de l'enchère ! Vérfiez la cohérence des informations. \033[0m");
                    if (connection != null) {
                        connection.rollback(); 
                    }
                        throw e;
        } finally {
                        if (pstmt != null) pstmt.close();
                        connection.setAutoCommit(true);
        }
        
    }
}