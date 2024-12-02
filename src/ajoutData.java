import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Timestamp;
import java.time.Duration;
import java.time.Instant;
import java.sql.Connection;



public class ajoutData
{
    Connection connection;
    public ajoutData(Connection connection)
    {
        this.connection = connection;
    }

    public void ajouterUtilisateur(String email, String nom, String prenom, String adressePostale, int nombre) throws Exception {
        PreparedStatement checkStatement = connection.prepareStatement("SELECT * FROM Utilisateur WHERE email = ?");
        checkStatement.setString(1, email);
        ResultSet res = checkStatement.executeQuery();
        if (!res.next())
        {
            PreparedStatement insertStatement = this.connection.prepareStatement("INSERT INTO UTILISATEUR (email, nom, prenom, ADRESSEPOSTALE) VALUES (?, ?, ?, ?)");
            insertStatement.setString(1, email);
            insertStatement.setString(2, nom);
            insertStatement.setString(3, prenom);
            insertStatement.setString(4, adressePostale);
            insertStatement.executeUpdate();
        }
        System.out.print("\rInscription : " + nombre);
    }
  
    public void ajoutDatas(String sentence)
    {
        try
        {
            PreparedStatement statement = connection.prepareStatement(sentence);
            statement.executeUpdate();
            statement.close();
        }
        catch (SQLException e)
        {
            System.err.println("Erreur lors de l'ajout de données");
        }
    }

    public void ajoutCat() 
    {
        try (FileReader file = new FileReader("data/categories.sql");
            BufferedReader buffer = new BufferedReader(file))
        {
            String line = buffer.readLine();
            ajoutData ajoutData = new ajoutData(this.connection);
            
            int nombre = 1;
            while (line != null) 
            {
                String nomcat = line.split("'")[1];
                try (PreparedStatement checkStatement = this.connection.prepareStatement("SELECT * FROM CATEGORIE WHERE nomcat = ?"))
                {
                    checkStatement.setString(1, nomcat);
                    try (ResultSet res = checkStatement.executeQuery()) 
                    {
                        if (!res.next()) 
                        {
                            ajoutData.ajoutDatas(line);
                        }
                        System.out.print("\rUsers ajoutés : " + nombre);
                    }
                    checkStatement.close();
                }
                catch (SQLException e) 
                {
                    System.err.println("Erreur lors de la verification de la catégorie (ajoutCat)");
                }
                line = buffer.readLine();
                nombre++;
            }
            buffer.close();
            file.close();
            System.out.println("");
        } 
        catch (IOException e) 
        {
            System.err.println("Erreur lors de la lecture du fichier de catégories");
        }
    }
    
    public void deleteAny(String table){
        // DELETE FROM "table";
        try{
            PreparedStatement statement = connection.prepareStatement("DELETE FROM " + table);
            statement.executeUpdate();
            statement.close();
            System.out.println("Suppression de " + table + " réussie");
        }
        catch (Exception e)
        {
            System.out.println("Dépendance " + table + " -> suppression impossible");
        }
    }

    public void ajoutProduit(){
        try(FileReader file = new FileReader("data/produits.sql");
            BufferedReader buffer = new BufferedReader(file))
        {
            String line = buffer.readLine();
            ajoutData ajoutData = new ajoutData(this.connection);
            
            int nombre = 1;
            while (line != null) 
            {
                int idproduit = Integer.parseInt(line.split("'")[1]);
                try (PreparedStatement checkStatement = this.connection.prepareStatement("SELECT * FROM PRODUIT WHERE IDPRODUIT = ?"))
                {
                    checkStatement.setInt(1, idproduit);
                    try (ResultSet res = checkStatement.executeQuery()) 
                    {
                        if (!res.next()) 
                        {
                            ajoutData.ajoutDatas(line);
                        }
                        System.out.print("\rProduits ajoutés : " + nombre);
                    }
                    checkStatement.close();
                }
                catch (SQLException e) 
                {
                    System.err.println("Erreur lors de la verification du produit (ajoutProduit)");
                }
                line = buffer.readLine();
                nombre++;
            }
            buffer.close();
            file.close();
            System.out.println("");
        } 
        catch (IOException e) 
        {
            System.err.println("Erreur lors de la lecture du fichier de produits");
        }
    }
    public void ajoutCarac()
    {
        try(FileReader file = new FileReader("data/caracteristiques.sql");
            BufferedReader buffer = new BufferedReader(file))
        {
            String line = buffer.readLine();
            ajoutData ajoutData = new ajoutData(this.connection);
            
            int nombre = 1;
            while (line != null) 
            {
                String[] tab = line.split("'");
                String nomcar = tab[1];
                int idproduit = Integer.parseInt(tab[5]);
                try (PreparedStatement checkStatement = this.connection.prepareStatement("SELECT * FROM CARACTERISTIQUES WHERE NOMCAR = ? AND IDPRODUIT = ?"))
                {
                    checkStatement.setString(1, nomcar);
                    checkStatement.setInt(2,idproduit);
                    try (ResultSet res = checkStatement.executeQuery()) 
                    {
                        if (!res.next()) 
                        {
                            ajoutData.ajoutDatas(line);
                        }
                        System.out.print("\rCaractéristiques ajoutées : " + nombre);
                    }
                    checkStatement.close();
                }
                catch (SQLException e) 
                {
                    System.err.println("Erreur lors de la verification de la caractéristique (ajoutCarac)");
                }
                line = buffer.readLine();
                nombre++;
            }
            buffer.close();
            file.close();
            System.out.println("");
        } 
        catch (IOException e) 
        {
            System.err.println("Erreur lors de la lecture du fichier de caractéristiques");
        }
    }


    public void ajoutVente()
    {
        try(FileReader file = new FileReader("data/vente.sql");
            BufferedReader buffer = new BufferedReader(file))
        {
            String line = buffer.readLine();
            ajoutData ajoutData = new ajoutData(this.connection);
            
            int nombre = 1;
            while (line != null) 
            {
                int idvente = Integer.parseInt(line.split("'")[1]);
                try (PreparedStatement checkStatement = this.connection.prepareStatement("SELECT * FROM VENTE WHERE IDVENTE = ?"))
                {
                    checkStatement.setInt(1, idvente);
                    try (ResultSet res = checkStatement.executeQuery()) 
                    {
                        if (!res.next()) 
                        {
                            ajoutData.ajoutDatas(line);
                        }
                        System.out.print("\rVente ajoutée : " + nombre);
                    }
                    checkStatement.close();
                }
                catch (SQLException e) 
                {
                    System.err.println("Erreur lors de la verification de la vente (ajoutVente)");
                }
                line = buffer.readLine();
                nombre++;
            }
            buffer.close();
            file.close();
            System.out.println("");
        } 
        catch (IOException e) 
        {
            System.err.println("Erreur lors de la lecture du fichier de ventes");
        }
    }


    public void ajoutSalleDeVente()
    {
        try(FileReader file = new FileReader("data/salledevente.sql");
            BufferedReader buffer = new BufferedReader(file))
        {
            String line = buffer.readLine();
            ajoutData ajoutData = new ajoutData(this.connection);
            
            int nombre = 1;
            while (line != null) 
            {
                int idsalle = Integer.parseInt(line.split("'")[1]);
                try  (PreparedStatement checkStatement = this.connection.prepareStatement("SELECT * FROM SALLEDEVENTE WHERE IDSALLE = ?"))
                {
                    checkStatement.setInt(1, idsalle);
                    try (ResultSet res = checkStatement.executeQuery()) 
                    {
                        if (!res.next()) 
                        {
                            ajoutData.ajoutDatas(line);
                        }
                        System.out.print("\rSalles de vente ajoutées : " + nombre);
                    }
                    checkStatement.close();
                }
                catch (SQLException e) 
                {
                    System.err.println("Erreur lors de la verification de la salle de vente (ajoutSalleDeVente)");
                }
                line = buffer.readLine();
                nombre++;
            }
            buffer.close();
            file.close();
            System.out.println("");
        } 
        catch (IOException e) 
        {
            System.err.println("Erreur lors de la lecture du fichier de salles de vente");
        }
    }

    public void ajoutUser()
    {     
        try(FileReader file = new FileReader("data/utilisateurs.sql");
            BufferedReader buffer = new BufferedReader(file))
        {
            String line = buffer.readLine();
            ajoutData ajoutData = new ajoutData(this.connection);
            
            int nombre = 1;
            while (line != null) 
            {
                String email = line.split("'")[1];
                try (PreparedStatement checkStatement = this.connection.prepareStatement("SELECT * FROM UTILISATEUR WHERE email = ?"))
                {
                    checkStatement.setString(1, email);
                    try (ResultSet res = checkStatement.executeQuery()) 
                    {
                        if (!res.next()) 
                        {
                            ajoutData.ajoutDatas(line);
                        }
                        System.out.print("\rCatégories ajoutées : " + nombre);
                    }
                    checkStatement.close();
                }
                catch (SQLException e) 
                {
                    System.err.println("Erreur lors de la verification de l'utilisateur (ajoutUser)");
                }
                line = buffer.readLine();
                nombre++;
            }
            buffer.close();
            file.close();
            System.out.println("");
        } 
        catch (IOException e) 
        {
            System.err.println("Erreur lors de la lecture du fichier d'utilisateurs");
        }
    }

    public static void changerValeursDescendantes(Connection connection, Scanner scnaner, long value){
        try{
            PreparedStatement pstmt = connection.prepareStatement("UPDATE Vente SET PrixActuel = PrixActuel - ? WHERE IDVENTE IN (SELECT IDVENTE FROM VENTE JOIN SALLEDEVENTE ON SALLEDEVENTE.IDSALLE = VENTE.IDSALLE WHERE ESTMONTANTE = 0)");
            pstmt.setLong(1, value); 
            ResultSet res = pstmt.executeQuery();
            pstmt.close();
            res.close();
        }

        catch (SQLException e) {
            System.err.println("Erreur lors de la récupération des ventes !");
        }
    }
    public void ajoutOffre() {
        try (FileReader file = new FileReader("data/offre.sql");
             BufferedReader buffer = new BufferedReader(file)) 
        {
            String line = buffer.readLine();
            ajoutData ajoutData = new ajoutData(this.connection);
            
            int nombre = 1;
            while (line != null) 
            {
                String[] tab = line.split("'");
                int idVente = Integer.parseInt(tab[9]);
                String email = tab[7];
                
                try (PreparedStatement checkStatement = this.connection.prepareStatement(
                    "SELECT * FROM OFFRE WHERE IDVENTE = ? AND EMAIL = ?"))
                {
                    checkStatement.setInt(1, idVente);
                    checkStatement.setString(2, email);
                    
                    try (ResultSet res = checkStatement.executeQuery()) 
                    {
                        if (!res.next()) 
                        {
                            ajoutData.ajoutDatas(line);
                        }
                        System.out.print("\rOffres ajoutées : " + nombre);
                    }
                }
                catch (SQLException e) 
                {
                    System.err.println("Erreur lors de la vérification de l'offre (ajoutOffre)");
                }
                
                line = buffer.readLine();
                nombre++;
            }
            buffer.close();
            file.close();
            System.out.println("");
        } 
        catch (IOException e) 
        {
            System.err.println("Erreur lors de la lecture du fichier d'offres");
        }
    }
    
}