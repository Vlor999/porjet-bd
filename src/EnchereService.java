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
                if (!res.isBeforeFirst()) 
                {
                    System.out.println("Aucun produit disponible pour cette salle de vente.");
                } 
                else 
                {
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
            } 
            catch (SQLException e)
            {
                System.err.println("Produits non trouvés ou vente terminée.");
            }
            
            // Demander quel produit l'utilisateur veut acheter
            if (!valeur_dedans)
            {
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
            String sqlOffre = "SELECT * FROM VENTE WHERE vente.IDPRODUIT = ?";
            pstmt = connection.prepareStatement(sqlOffre);
            pstmt.setInt(1, IdProduit);
            res = pstmt.executeQuery();

            String output = "| %-10s | %-25s | %-10s | %-10s | %-10s | %-15s | %-10s | %-10s |".formatted(
                "IDVENTE", "HEUREVENTE", "PRIXDEPART", "PRIXACTUEL", "QUANTITE", "DUREE", "IDSALLE", "IDPRODUIT");
            System.out.println("-".repeat(output.length()));
            System.out.println(output);
            System.out.println("-".repeat(output.length()));
            while (res.next()) 
            {
                String row = String.format(
                        "| %-10d | %-25s | %-10.2f | %-10.2f | %-10d | %-15d | %-10d | %-10d |",
                        res.getInt("IDVENTE"),
                        res.getTimestamp("HEUREVENTE"),
                        res.getDouble("PRIXDEPART"),
                        res.getDouble("PRIXACTUEL"),
                        res.getInt("QUANTITE"),
                        res.getInt("DUREE"),
                        res.getInt("IDSALLE"),
                        res.getInt("IDPRODUIT")
                );
                System.out.println(row);
            }
            System.out.println("-".repeat(output.length()));
            
            System.out.print("Entrez la vente pour laquelle vous voulez faire cette offre : ");
            int IdVente = Integer.parseInt(scanner.nextLine());
            
            // Vérifier si l'offre est valide
            
            System.out.println("-".repeat(output.length()));

            if(lecteur.nombreIdProduitOffreEffectue.containsKey(IdProduit))
            {
                lecteur.nombreIdProduitOffreEffectue.put(IdProduit, lecteur.nombreIdProduitOffreEffectue.get(IdProduit) + 1);
            }
            else
            {
                lecteur.nombreIdProduitOffreEffectue.put(IdProduit, 1);
            }

            ResultSet rs = null;
            try 
            {
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

                if (rs.next()) 
                {
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

                    boolean estIllimite;
                    sqlPrixActuel = """
                        SELECT S.LIMITEOFFRES
                        FROM SalleDeVente S WHERE S.IdSalle = ?""";
                    pstmt = connection.prepareStatement(sqlPrixActuel);
                    pstmt.setInt(1, user.getIdSalleDeVente());
                    rs = pstmt.executeQuery();
                    rs.next();
                    estIllimite = rs.getString("LIMITEOFFRES").equals("limitee");
                    // estIllimite = rs.getInt("LIMITEOFFRES") == -1;
                    if(!estIllimite)
                    {
                        System.out.println("Faites attention à la limite d'offres dans cette salle de vente.");
                        // Normalement c'est ca qu'il faut faire. Si on change le type de limite offre en int et qu'on rajoute cette valeur
                        // dans la table salle de vente
                        /*
                        * String  sqlLimiteOffreProduit = "SELECT NombreOffres FROM Produit WHERE IdProduit = ?";
                        * pstmt = connection.prepareStatement(sqlLimiteOffreProduit);
                        * pstmt.setInt(1, IdProduit);
                        * rs = pstmt.executeQuery();
                        * 
                        * if(rs.next())
                        * {
                        *     int nombreOffres = rs.getInt("NombreOffres");
                        *     if(lecteur.nombreIdProduitOffreEffectue.get(IdProduit) >= nombreOffres)
                        *     {
                        *          System.out.println("Vous avez atteint la limite d'offres pour ce produit.");
                        *          return;
                        *     }
                        *     else
                        *    {
                        *         System.out.println("Vous avez encore " + (nombreOffres - lecteur.nombreIdProduitOffreEffectue.get(IdProduit)) + " offres pour ce produit.");
                        *    }
                        * 
                        * }
                        */
                    }

                    System.out.println("\033[0;31mOffre enregistrée et prix mis à jour avec succès. \033[0m");

                    if (montante == 0){
                        FinEncheres.terminerEnchere(connection, IdVente);
                    }
                    connection.commit();
                } 
                else 
                {
                    System.out.println("Le produit n'existe pas.");
                }
            } 
            catch (SQLException e) 
            {
                lecteur.nombreIdProduitOffreEffectue.put(IdProduit, lecteur.nombreIdProduitOffreEffectue.get(IdProduit) - 1);
                System.out.println("Erreur lors de la récupération du prix actuel du produit.");
                if (connection != null) {
                    connection.rollback();
                }
                throw e;
            }
        } 
        catch (Exception e) 
        {
            System.err.println("\033[0;31mErreur lors du placement de l'enchère ! Vérfiez la cohérence des informations. \033[0m");
            if (connection != null) 
            {
                connection.rollback(); 
            }
            throw e;
        } 
        finally 
        {
            if (pstmt != null) pstmt.close();
            connection.setAutoCommit(true);
        }
    }
}