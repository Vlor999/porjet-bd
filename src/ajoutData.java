import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Connection;

public class ajoutData
{
    Connection connection;
    public ajoutData(Connection connection)
    {
        this.connection = connection;
    }

    public void ajouterUtilisateur(String email, String nom, String prenom, String adressePostale) throws Exception {
        PreparedStatement checkStatement = connection.prepareStatement("SELECT * FROM Utilisateur WHERE email = ?");
        checkStatement.setString(1, email);
        ResultSet res = checkStatement.executeQuery();
        if (res.next()) {
            System.out.println("Cet email est déjà utilisé.");
        } 
        else 
        {
            PreparedStatement insertStatement = this.connection.prepareStatement("INSERT INTO Utilisateur (email, nom, prenom, ADRESSEPOSTALE) VALUES (?, ?, ?, ?)");
            insertStatement.setString(1, email);
            insertStatement.setString(2, nom);
            insertStatement.setString(3, prenom);
            insertStatement.setString(4, adressePostale);
            insertStatement.executeUpdate();
            System.out.println("Inscription réussie. Vous êtes maintenant membre !");
        }
    }

    public void ajoutDatas(String sentence)
    {
        try
        {
            PreparedStatement statement = connection.prepareStatement(sentence);
            statement.executeUpdate();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}