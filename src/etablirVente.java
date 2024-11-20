import java.sql.*;
import java.util.Scanner;

public class etablirVente {
    public static void afficherToutesLesVentes(Connection connection){
        try {
            Statement stmt = connection.createStatement();
            ResultSet res = stmt.executeQuery("SELECT * FROM Vente");
            while (res.next()) {
                System.out.println("Identifiant de la vente : " + res.getString("IdVente") + ", Prix de départ " +  res.getString("PrixDepart") + ", Durée : " + res.getString("Duree") +
                        ", Identifiant du produit : " + res.getString("IdProduit") + ", Identifiant de la Salle : " + res.getString("IdSalle") + 
                        ", Prix actuel : " + res.getString("PrixActuel"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void creerNouvelleVente(Connection connection){
        try{
            Scanner scanner = new Scanner(System.in);
            System.out.print("Veuillez entrer l'identifiant de la vente : ");
            int id = scanner.nextInt();
            scanner.nextLine();

            PreparedStatement checkStatement = connection.prepareStatement("SELECT * FROM Vente WHERE IdSalle = ?");
            checkStatement.setInt(1, id);
            ResultSet res = checkStatement.executeQuery();

            if (res.next()) {
                System.out.println("Cet identifiant est déjà utilisé !");
            } else {
                System.out.print("Prix de départ : ");
                int depart = scanner.nextInt();
                scanner.nextLine();
                System.out.print("Durée de la vente (en minutes, -1 s'il n'y a pas de limite) : ");
                int duree = scanner.nextInt();
                scanner.nextLine();
                System.out.print("Identifiant du produit : ");
                int idproduit = scanner.nextInt();
                scanner.nextLine();
                System.out.print("Identifiant de la salle : ");
                int idsalle = scanner.nextInt();
                scanner.nextLine();
                System.out.print("Prix actuel de la vente : ");
                int actuel = scanner.nextInt();
                scanner.nextLine();


                PreparedStatement insertStatement = connection.prepareStatement(
                        "INSERT INTO Vente (IdVente, PrixDepart, Duree, IdProduit, IdSalle, PrixActuel) VALUES (?, ?, ?, ?, ?, ?)");
                insertStatement.setInt(1, id);
                insertStatement.setInt(2, depart);
                insertStatement.setInt(3, duree);
                insertStatement.setInt(4, idproduit);
                insertStatement.setInt(5, idsalle);
                insertStatement.setInt(6, actuel);

                insertStatement.executeUpdate();
                System.out.println("Création de la salle de vente réussie !");
                }
            
            }
        catch (SQLException e) {
            e.printStackTrace();
        }

    }
}
