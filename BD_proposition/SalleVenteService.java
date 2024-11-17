public class SalleVenteService {
    String url = "jdbc:oracle:thin:@oracle1.ensimag.fr:1521:oracle1";
    String user = " adnetw " ;
    String password = " ****** ";
    
    void creerSalleEtVente(SalleVente salle, Vente vente) {
        PreparedStatement pstmtSalle = null;
        PreparedStatement pstmtVerif = null;
        PreparedStatement pstmtVente = null;
        
        try {
            // Enregistrement du driver
            DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());
            
            // Établissement de la connexion
            connection = DriverManager.getConnection(url, "user", "password");
            
            // Désactivation de l'auto-commit
            connection.setAutoCommit(false);
            
            // Création de la salle
            String sqlSalle = "INSERT INTO SalleVente (NomSalle, Description, DateCreation) VALUES (?, ?, ?)";
            pstmtSalle = connection.prepareStatement(sqlSalle, Statement.RETURN_GENERATED_KEYS);
            pstmtSalle.setString(1, salle.getNomSalle());
            pstmtSalle.setString(2, salle.getDescription());
            pstmtSalle.setDate(3, new java.sql.Date(System.currentTimeMillis()));
            pstmtSalle.executeUpdate();
            
            // Récupération de l'ID généré pour la salle
            int idSalle;
            try (ResultSet rs = pstmtSalle.getGeneratedKeys()) {
                if (rs.next()) {
                    idSalle = rs.getInt(1);
                } else {
                    throw new SQLException("Création de la salle échouée");
                }
            }
            
            // Vérification de la disponibilité du produit
            String sqlVerif = "SELECT COUNT(*) FROM Vente WHERE IdProduit = ? AND DateFin >= CURRENT_DATE";
            pstmtVerif = connection.prepareStatement(sqlVerif);
            pstmtVerif.setInt(1, vente.getIdProduit());
            
            try (ResultSet rs = pstmtVerif.executeQuery()) {
                if (rs.next() && rs.getInt(1) > 0) {
                    throw new SQLException("Produit déjà en vente");
                }
            }
            
            // Création de la vente
            String sqlVente = "INSERT INTO Vente (DateDebut, DateFin, HeureDebut, HeureFin, PrixDepart, TypeVente, Revocable, IdProduit, IdSalle) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
            pstmtVente = connection.prepareStatement(sqlVente);
            pstmtVente.setDate(1, new java.sql.Date(vente.getDateDebut().getTime()));
            pstmtVente.setDate(2, new java.sql.Date(vente.getDateFin().getTime()));
            pstmtVente.setTime(3, vente.getHeureDebut());
            pstmtVente.setTime(4, vente.getHeureFin());
            pstmtVente.setBigDecimal(5, vente.getPrixDepart());
            pstmtVente.setString(6, vente.getTypeVente());
            pstmtVente.setBoolean(7, vente.isRevocable());
            pstmtVente.setInt(8, vente.getIdProduit());
            pstmtVente.setInt(9, idSalle);
            pstmtVente.executeUpdate();
            
            // Si tout s'est bien passé, on valide la transaction
            connection.commit();
            
        } catch (SQLException e) {
            // En cas d'erreur, on effectue un rollback
            if (connection != null) {
                try {
                    connection.rollback();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            throw new RuntimeException("Erreur lors de la création de la salle et de la vente", e);
        } finally {
            // Fermeture des ressources dans l'ordre inverse de leur création
            try {
                if (pstmtVente != null) pstmtVente.close();
                if (pstmtVerif != null) pstmtVerif.close();
                if (pstmtSalle != null) pstmtSalle.close();
                if (connection != null) {
                    connection.setAutoCommit(true);
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}