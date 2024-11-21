import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.io.BufferedReader;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.Date;
import java.sql.Timestamp;


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


    public void ajouterCaracteristique(String NOMCAR, String VALEURCAR) throws Exception {
        // Vérification si la catégorie existe déjà
        PreparedStatement checkStatement = connection.prepareStatement("SELECT * FROM caracteristiques WHERE NOMCAR = ?");
        checkStatement.setString(1, NOMCAR);
        ResultSet res = checkStatement.executeQuery();
    
        if (res.next()) {
            System.out.println("Cet caracteristique existe déjà.");
        } else {
            // Insertion de la nouvelle caracteristique
            PreparedStatement insertStatement = connection.prepareStatement("INSERT INTO Categorie (NOMCAR, VALEURCAR) VALUES (?, ?)");
            insertStatement.setString(1, NOMCAR);
            insertStatement.setString(2, VALEURCAR);
            insertStatement.executeUpdate();
            System.out.println("Nouvelle caracteristique ajoutée avec succès !");
        }
    }

    public void ajouterCategorie(String NOMCAT, String DESCRCAT) throws Exception {
        // Vérification si la catégorie existe déjà
        PreparedStatement checkStatement = connection.prepareStatement("SELECT * FROM Categorie WHERE NOMCAT = ?");
        checkStatement.setString(1, NOMCAT);
        ResultSet res = checkStatement.executeQuery();
    
        if (res.next()) {
            System.out.println("Cette catégorie existe déjà.");
        } else {
            // Insertion de la nouvelle catégorie
            PreparedStatement insertStatement = connection.prepareStatement("INSERT INTO Categorie (NOMCAT, DESCRCAT) VALUES (?, ?)");
            insertStatement.setString(1, NOMCAT);
            insertStatement.setString(2, DESCRCAT);
            insertStatement.executeUpdate();
            System.out.println("Nouvelle catégorie ajoutée avec succès !");
        }
    }
    
    public void ajouterOffre(String EMAIL, String IDVENTE, String PRIXOFFRE, String DATEOFFRE, String HEUREOFFRE, String QUANTITE) throws Exception {
        // Vérification si l'email est présent dans la table Utilisateur
        PreparedStatement checkStatement = connection.prepareStatement("SELECT * FROM Utilisateur WHERE EMAIL = ?");
        checkStatement.setString(1, EMAIL);
        ResultSet res = checkStatement.executeQuery();
    
        if (!res.next()) {
            System.out.println("Cet email n'existe pas dans la base de données.");
            return;
        }
    
        // Vérification si l'offre existe déjà (basée sur l'IDVENTE et l'EMAIL pour éviter les doublons)
        PreparedStatement checkOffreStatement = connection.prepareStatement("SELECT * FROM OFFRE WHERE EMAIL = ? AND IDVENTE = ?");
        checkOffreStatement.setString(1, EMAIL);
        checkOffreStatement.setString(2, IDVENTE);
        ResultSet offreRes = checkOffreStatement.executeQuery();
    
        if (offreRes.next()) {
            System.out.println("Cette offre existe déjà pour cet email et cette vente.");
        } else {
            // Conversion des valeurs de String à leurs types appropriés
            int idVente = Integer.parseInt(IDVENTE);  // Conversion de l'IDVENTE en entier
            double prixOffre = Double.parseDouble(PRIXOFFRE);  // Conversion du PRIXOFFRE en double
            int quantite = Integer.parseInt(QUANTITE);  // Conversion de la QUANTITE en entier
            
            // Conversion de la DATEOFFRE et HEUREOFFRE en types SQL appropriés
            Date dateOffre = Date.valueOf(DATEOFFRE);  // DATEOFFRE doit être au format YYYY-MM-DD
            Timestamp heureOffre = Timestamp.valueOf(HEUREOFFRE);  // HEUREOFFRE doit être au format YYYY-MM-DD HH:MM:SS
    
            // Insertion de la nouvelle offre
            PreparedStatement insertStatement = connection.prepareStatement("INSERT INTO OFFRE (EMAIL, IDVENTE, PRIXOFFRE, DATEOFFRE, HEUREOFFRE, QUANTITE) VALUES (?, ?, ?, ?, ?, ?)");
            insertStatement.setString(1, EMAIL);
            insertStatement.setInt(2, idVente);
            insertStatement.setDouble(3, prixOffre);
            insertStatement.setDate(4, dateOffre);
            insertStatement.setTimestamp(5, heureOffre);
            insertStatement.setInt(6, quantite);
            insertStatement.executeUpdate();
            System.out.println("Offre ajoutée avec succès !");
        }
    }
    

    public void supprimerToutesCategories() throws Exception {
        // Suppression de toutes les catégories de la table
        PreparedStatement deleteStatement = connection.prepareStatement("TRUNCATE TABLE categorie");
        int rowsAffected = deleteStatement.executeUpdate();
        
        if (rowsAffected > 0) {
            System.out.println("Toutes les catégories ont été supprimées avec succès !");
        } else {
            System.out.println("Aucune catégorie n'a été supprimée.");
        }
    }

    public void ajouterProduit(String idProduitStr, String nomProduit, String prixRevientStr, String stockStr, String nomCat, String nomCar) throws Exception {
        // Convertir les chaînes de caractères en types appropriés
        int idProduit = Integer.parseInt(idProduitStr);  // Conversion de String à int
        double prixRevient = Double.parseDouble(prixRevientStr);  // Conversion de String à double
        int stock = Integer.parseInt(stockStr);  // Conversion de String à int
    
        // Vérification si la catégorie existe
        PreparedStatement checkCatStatement = connection.prepareStatement("SELECT * FROM Categorie WHERE NOMCAT = ?");
        checkCatStatement.setString(1, nomCat);
        ResultSet resCat = checkCatStatement.executeQuery();
    
        if (!resCat.next()) {
            System.out.println("La catégorie " + nomCat + " n'existe pas.");
            return;  // Quitte la fonction si la catégorie n'existe pas
        }
    
        // Vérification si la caractéristique existe
        PreparedStatement checkCarStatement = connection.prepareStatement("SELECT * FROM Caracteristiques WHERE NOMCAR = ?");
        checkCarStatement.setString(1, nomCar);
        ResultSet resCar = checkCarStatement.executeQuery();
    
        if (!resCar.next()) {
            System.out.println("La caractéristique " + nomCar + " n'existe pas.");
            return;  // Quitte la fonction si la caractéristique n'existe pas
        }
    
        // Insertion du produit dans la base de données
        PreparedStatement insertStatement = connection.prepareStatement("INSERT INTO PRODUIT (IDPRODUIT, NomProduit, PrixRevient, Stock, NomCat, NomCar) VALUES (?, ?, ?, ?, ?, ?)");
        insertStatement.setInt(1, idProduit);
        insertStatement.setString(2, nomProduit);
        insertStatement.setDouble(3, prixRevient);
        insertStatement.setInt(4, stock);
        insertStatement.setString(5, nomCat);
        insertStatement.setString(6, nomCar);
        insertStatement.executeUpdate();
        System.out.println("Produit ajouté avec succès !");
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
            System.out.println("");
            e.printStackTrace();
        }
    }


    public void deleteCat(){
        // DELETE FROM "CATEGORIE";
        try{
            PreparedStatement statement = connection.prepareStatement("DELETE FROM CATEGORIE");
            statement.executeUpdate();
        }
        catch (Exception e)
        {
            System.out.println("Dépendance Catégorie -> suppression impossible");
        }
    }

    public void ajoutCat() {
        try 
        {
            FileReader file = new FileReader("data/categories.sql");
            BufferedReader buffer = new BufferedReader(file);
            String line = buffer.readLine();
            ajoutData ajoutData = new ajoutData(this.connection);
            
            int nombre = 0;
            while (line != null) 
            {
                String nomcat = line.split("'")[1];
                try  
                {
                    PreparedStatement checkStatement = this.connection.prepareStatement("SELECT * FROM CATEGORIE WHERE nomcat = ?");
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
    
    public void deleteUser(){
        // supprimer les utilisateurs avant de les ajouter
        // DELETE FROM "UTILISATEUR";

        try{
            PreparedStatement statement = connection.prepareStatement("DELETE FROM UTILISATEUR");
            statement.executeUpdate();
        }
        catch (Exception e)
        {
            System.out.println("Dépendance User -> suppression impossible");
        }
    }


    public void deleteSalleDeVente(){
        // supprimer les salles de vente avant de les ajouter
        // DELETE FROM "SALLEDEVENTE";

        try{
            PreparedStatement statement = connection.prepareStatement("DELETE FROM SALLEDEVENTE");
            statement.executeUpdate();
        }
        catch (Exception e)
        {
            System.out.println("Dépendance SalleDeVente -> suppression impossible");
        }
    }


    public void deleteVente(){
        // supprimer les ventes avant de les ajouter
        // DELETE FROM "VENTE";

        try{
            PreparedStatement statement = connection.prepareStatement("DELETE FROM VENTE");
            statement.executeUpdate();
        }
        catch (Exception e)
        {
            System.out.println("Dépendance Vente -> suppression impossible");
        }
    }


    public void ajoutVente(){
        try
        {
            FileReader file = new FileReader("data/vente.sql");
            BufferedReader buffer = new BufferedReader(file);
            String line = buffer.readLine();
            ajoutData ajoutData = new ajoutData(this.connection);
            
            int nombre = 0;
            while (line != null) 
            {
                int idvente = Integer.parseInt(line.split("'")[1]);
                try  
                {
                    PreparedStatement checkStatement = this.connection.prepareStatement("SELECT * FROM VENTE WHERE IDVENTE = ?");
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
        try
        {
            FileReader file = new FileReader("data/salledevente.sql");
            BufferedReader buffer = new BufferedReader(file);
            String line = buffer.readLine();
            ajoutData ajoutData = new ajoutData(this.connection);
            
            int nombre = 0;
            while (line != null) 
            {
                int idsalle = Integer.parseInt(line.split("'")[1]);
                try  
                {
                    PreparedStatement checkStatement = this.connection.prepareStatement("SELECT * FROM SALLEDEVENTE WHERE IDSALLE = ?");
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
        try
        {
            FileReader file = new FileReader("data/utilisateurs.sql");
            BufferedReader buffer = new BufferedReader(file);
            String line = buffer.readLine();
            ajoutData ajoutData = new ajoutData(this.connection);
            
            int nombre = 0;
            while (line != null) 
            {
                String email = line.split("'")[1];
                try  
                {
                    PreparedStatement checkStatement = this.connection.prepareStatement("SELECT * FROM UTILISATEUR WHERE email = ?");
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