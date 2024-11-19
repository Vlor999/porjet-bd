import java.io.BufferedReader;
import java.io.FileReader;
import java.sql.*;

public class lecteur {
    public static void main(String[] args) {
        // Pilote JDBC
        etablirConnexion.cnxPilote();
        Connection connection = etablirConnexion.cnxBaseDonnees();

        ajoutData ajoutData = new ajoutData(connection);
        try
        {
            FileReader file = new FileReader("data/donnee.sql");
            BufferedReader buffer = new BufferedReader(file);
            String line = buffer.readLine();
            // Format de la ligne : email, nom, prenom, adressePostale
            while (line != null)
            {
                String[] donnees = line.split(",");
                ajoutData.ajouterUtilisateur(donnees[0], donnees[1], donnees[2], donnees[3]);
                line = buffer.readLine();
            }
            buffer.close(); 

            FileReader file2 = new FileReader("data/salle_vente.sql");
            BufferedReader buffer2 = new BufferedReader(file2);
            String line2 = buffer2.readLine();
            while (line2 != null)
            {
                ajoutData.ajoutDatas(line2);
                line2 = buffer2.readLine();
            }
            buffer2.close();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        // Test de la classe gererUtilisateur
        mainInterface mainInterface = new mainInterface(connection);

        // Boucle pour afficher le menu tant que l'option 4 (fermer la connexion) n'est pas choisie
        boolean continuer = true;
        while (continuer) {
            mainInterface.choisirAction();

            // Vérification si la connexion est fermée, si oui, quitter la boucle
            try {
                if (connection.isClosed()) {
                    continuer = false; // Arrêter la boucle si la connexion est fermée
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

    }
}
