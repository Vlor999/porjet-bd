import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.io.BufferedReader;
import java.io.FileReader;
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
        catch (Exception e)
        {
            System.out.println("");
            e.printStackTrace();
        }
    }

    public void ajoutCat() 
    {
        try (FileReader file = new FileReader("data/categories.sql");
            BufferedReader buffer = new BufferedReader(file))
        {
            String line = buffer.readLine();
            ajoutData ajoutData = new ajoutData(this.connection);
            
            int nombre = 0;
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
                        System.out.print("\rUser ajoutée : " + nombre);
                    }
                    checkStatement.close();
                }
                catch (Exception e) 
                {
                    e.printStackTrace();
                }
                line = buffer.readLine();
                nombre++;
            }
            buffer.close();
            file.close();
            System.out.println("");
        } 
        catch (Exception e) 
        {
            e.printStackTrace();
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
            
            int nombre = 0;
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
                        System.out.print("\rProduit ajouté : " + nombre);
                    }
                    checkStatement.close();
                }
                catch (Exception e) 
                {
                    e.printStackTrace();
                }
                line = buffer.readLine();
                nombre++;
            }
            buffer.close();
            file.close();
            System.out.println("");
        } 
        catch (Exception e) 
        {
            e.printStackTrace();
        }
    }

    public void ajoutVente(){
        try(FileReader file = new FileReader("data/vente.sql");
            BufferedReader buffer = new BufferedReader(file))
        {
            String line = buffer.readLine();
            ajoutData ajoutData = new ajoutData(this.connection);
            
            int nombre = 0;
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
                catch (Exception e) 
                {
                    e.printStackTrace();
                }
                line = buffer.readLine();
                nombre++;
            }
            buffer.close();
            file.close();
            System.out.println("");
        } 
        catch (Exception e) 
        {
            e.printStackTrace();
        }
    }


    public void ajoutSalleDeVente(){
        try(FileReader file = new FileReader("data/salledevente.sql");
            BufferedReader buffer = new BufferedReader(file))
        {
            String line = buffer.readLine();
            ajoutData ajoutData = new ajoutData(this.connection);
            
            int nombre = 0;
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
                        System.out.print("\rSalle de vente ajoutée : " + nombre);
                    }
                    checkStatement.close();
                }
                catch (Exception e) 
                {
                    e.printStackTrace();
                }
                line = buffer.readLine();
                nombre++;
            }
            buffer.close();
            file.close();
            System.out.println("");
        } 
        catch (Exception e) 
        {
            e.printStackTrace();
        }
    }

    public void ajoutUser()
    {     
        try(FileReader file = new FileReader("data/utilisateurs.sql");
            BufferedReader buffer = new BufferedReader(file))
        {
            String line = buffer.readLine();
            ajoutData ajoutData = new ajoutData(this.connection);
            
            int nombre = 0;
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
                        System.out.print("\rCatégorie ajoutée : " + nombre);
                    }
                    checkStatement.close();
                }
                catch (Exception e) 
                {
                    e.printStackTrace();
                }
                line = buffer.readLine();
                nombre++;
            }
            buffer.close();
            file.close();
            System.out.println("");
        } 
        catch (Exception e) 
        {
            e.printStackTrace();
        }
    }
}