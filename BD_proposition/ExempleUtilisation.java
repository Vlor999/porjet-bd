public class ExempleUtilisation{
    public void main(){
        SalleVenteService service = new SalleVenteService();

        SalleVente salle = new SalleVente();
        salle.setNomSalle("Salle A");
        salle.setDescription("Salle pour ventes aux enchères");

        Vente vente = new Vente();
        vente.setDateDebut(new Date());
        vente.setDateFin(/*date future*/);
        vente.setPrixDepart(new BigDecimal("100.00"));

service.creerSalleEtVente(salle, vente);
    }
}