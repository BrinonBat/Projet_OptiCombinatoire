import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class Main{
    public static void main(String[] args) throws IOException {

    	String filename = args[0];
    	try {
    		// Permet de lire un fichier
			InputStream is = new FileInputStream(filename);
			InputStreamReader isr = new InputStreamReader(is);
			BufferedReader br = new BufferedReader(isr);
			String ligne;
			String res[];
			int compteur = 0;
			Terrain terrain= null;
			while((ligne = br.readLine()) != null) {
				if(compteur == 0) { // Première ligne c'est la taille du terrain
					res = ligne.split(" ");
					int largeur = Integer.parseInt(res[0]);
					int profondeur = Integer.parseInt(res[1]);
					terrain = new Terrain(largeur,profondeur);
				}
				else if(compteur == 1) { // Deuxième ligne c'est le nb de batiment
					int nbBatiment = Integer.parseInt(ligne.trim());
				} else { // Le reste des lignes c'est les tailles des batiments
					res = ligne.split(" ");
					int largeur = Integer.parseInt(res[0]);
					int profondeur = Integer.parseInt(res[1]);
					if(compteur == 2) // HDV
						terrain.getliBat().add(new Batiment(largeur, profondeur, true));
					else // batiment normal
						terrain.getliBat().add(new Batiment(largeur, profondeur));
				}
				++compteur;
			}
			terrain.glouton();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
    }

}