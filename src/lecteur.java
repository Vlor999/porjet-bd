import java.sql.*;

public class lecteur {
    public static void main(String[] args) {
        // Pilote JDBC
        etablirConnexion.cnxPilote();
        Connection connection = etablirConnexion.cnxBaseDonnees();

        // Test de la classe gererUtilisateur
        gererUtilisateur gererUtilisateur = new gererUtilisateur(connection);

        // Boucle pour afficher le menu tant que l'option 4 (fermer la connexion) n'est pas choisie
        boolean continuer = true;
        while (continuer) {
            gererUtilisateur.choisirAction();

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
