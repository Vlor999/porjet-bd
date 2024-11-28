import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class HeureDate {
        private static ZonedDateTime dateHeure = ZonedDateTime.now();

        private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        private static String[] dateHeureFormatee = dateHeure.format(formatter).split(" ");
        private String heure;
        private String date;

        public HeureDate(String annee, String mois, String jour, String heure, String minute, String seconde){
                this.heure = heure+":"+minute+":"+seconde;
                this.date = annee+"-"+mois+"-"+jour;
        }
        
        public HeureDate(){
                this.heure = dateHeureFormatee[1];
                this.date = dateHeureFormatee[0];
        }
        
        public String getHeure(){
                return this.heure;
        }

        public String getDate(){
                return this.date;
        }

        public boolean Precede(HeureDate h) {
                String thisDateTimeString = this.date + " " + this.heure;
                String otherDateTimeString = h.getDate() + " " + h.getHeure();

                LocalDateTime thisDateTime = LocalDateTime.parse(thisDateTimeString, formatter);
                LocalDateTime otherDateTime = LocalDateTime.parse(otherDateTimeString, formatter);

                return thisDateTime.isBefore(otherDateTime);
        }
}
