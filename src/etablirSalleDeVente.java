import java.util.Scanner;
import java.sql.*;


public class etablirSalleDeVente {

    public static void afficherToutesLesSalles(Connection connection){
        try {
            Statement stmt = connection.createStatement();
            ResultSet res = stmt.executeQuery("SELECT * FROM SalleDeVente");
            while (res.next()) {
                System.out.println("Identifiant de la salle : " + res.getString("IdSalle") + ", Nom de la Salle : " +  res.getString("NomSalle") + ", Est occupée : " + res.getString("EstOccupee") +
                        ", Est montante : " + res.getString("EstMontante") + ", Nombre d'offres possibles : " + res.getString("LimiteOffres") + 
                        ", Durée : " + res.getString("TypeDuree"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void afficherToutesLesSallesDisponibles(Connection connection){
        try {
            Statement stmt = connection.createStatement();
            ResultSet res = stmt.executeQuery("SELECT * FROM SalleDeVente WHERE ESTOCCUPE = 0");
            while (res.next()) {
                System.out.println("Identifiant de la salle : " + res.getString("IdSalle") + ", Nom de la Salle : " +  res.getString("NomSalle") + ", Est occupée : " + res.getString("EstOccupee") +
                        ", Est montante : " + res.getString("EstMontante") + ", Nombre d'offres possibles : " + res.getString("LimiteOffres") + 
                        ", Durée : " + res.getString("TypeDuree"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void creerNouvelleSalleDeVente(Connection connection){
        try{
            Scanner scanner = new Scanner(System.in);
            System.out.print("Veuillez entrer l'identifiant de la salle : ");
            int id = scanner.nextInt();
            scanner.nextLine();

            PreparedStatement checkStatement = connection.prepareStatement("SELECT * FROM SalleDeVente WHERE IdSalle = ?");
            checkStatement.setInt(1, id);
            ResultSet res = checkStatement.executeQuery();

            if (res.next()) {
                System.out.println("Cet identifiant est déjà utilisé !");
            } else {
                System.out.print("Nom de la salle : ");
                String nom = scanner.nextLine();
                System.out.print("Est-elle occupée (1 si oui, 0 sinon) : ");
                int occupe = scanner.nextInt();
                scanner.nextLine();
                System.out.print("Est-elle montante (1 si oui, 0 sinon) : ");
                int montante = scanner.nextInt();
                scanner.nextLine();
                System.out.print("Nombre d'offres possibles (-1 s'il n'y a pas de limite) : ");
                int nombreOffres = scanner.nextInt();
                scanner.nextLine();
                System.out.print("Type de durée : (limitee ou illimitee) ");
                String typeDuree = scanner.nextLine();
                System.out.print("Nom de la catégorie : ");
                String nomCategorie = scanner.nextLine();


                PreparedStatement insertStatement = connection.prepareStatement(
                        "INSERT INTO SalleDeVente (IdSalle, NomSalle, EstOccupee, EstMontante, LimiteOffres, TypeDuree, CategorieVente) VALUES (?, ?, ?, ?, ?, ?, ?)");
                insertStatement.setInt(1, id);
                insertStatement.setString(2, nom);
                insertStatement.setInt(3, occupe);
                insertStatement.setInt(4, montante);
                insertStatement.setInt(5, nombreOffres);
                insertStatement.setString(6, typeDuree);
                insertStatement.setString(7, nomCategorie);

                insertStatement.executeUpdate();
                System.out.println("Création de la salle de vente réussie !");
                }
                
            }
        catch (SQLException e) {
            e.printStackTrace();
        }

    }
}
