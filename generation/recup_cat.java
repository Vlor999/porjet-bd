import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
public class recup_cat {
    // lis le fichier catégorie, trouve les différntes catégoris puis créer les lignes pour le sjoauter à la base de données
    public static void main(String[] args) {
        
        try {
            ArrayList<String> categories = new ArrayList<>();
            FileReader file = new FileReader("data/categories.sql");
            BufferedReader buffer = new BufferedReader(file);
            String line = buffer.readLine();
            while (line != null) {
                String[] donnees = line.split("'");
                categories.add(donnees[1]);
                line = buffer.readLine();
            }
            buffer.close();

            int compteur = 0;
            FileWriter file2 = new FileWriter("data/salle_vente.sql");
            for(String cat: categories){
                int val_random_01 = (int)(Math.random() * 2);
                String lineToAdd = "INSERT INTO SALLE_DE_VENTE (IDSALLE, CATEGORIE, TYPEVENTE, DESCRIPTION) VALUES ('" + compteur +  "', '" + cat + "', '" + val_random_01 +"', '" + compteur + " : salle de vente de la catégorie " + cat + "')";
                file2.write(lineToAdd + "\n");
                compteur++;
            }
            file2.close();
        } 
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
