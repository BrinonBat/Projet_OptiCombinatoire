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
			InputStream is = new FileInputStream(filename);
			InputStreamReader isr = new InputStreamReader(is);
			BufferedReader br = new BufferedReader(isr);
			String ligne;
			String res[];
			int compteur = 0;
			Terrain terrain= null;
			while((ligne = br.readLine()) != null) {
				if(compteur == 0) {
					res = ligne.split(" ");
					int largeur = Integer.parseInt(res[0]);
					int profondeur = Integer.parseInt(res[1]);
					terrain = new Terrain(largeur,profondeur);
				}
				else if(compteur == 1) {
					int nbBatiment = Integer.parseInt(ligne.trim());
				} else {
					res = ligne.split(" ");
					int largeur = Integer.parseInt(res[0]);
					int profondeur = Integer.parseInt(res[1]);
					if(compteur == 2)
						terrain.getliBat().add(new Batiment(largeur, profondeur, true));
					else
						terrain.getliBat().add(new Batiment(largeur, profondeur));
				}
				++compteur;
			}
			terrain.glouton();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    	
        /*Terrain test=new Terrain(4,4);
        test.addBatiment(new Batiment(0,0,3,2,true));
        test.addBatiment(new Batiment(0,3,1,1));
        test.addBatiment(new Batiment(2,2,2,2));
        test.afficherTerrain();
        System.out.println("Score : " + test.calculeScore());
        //System.out.println(test.estRelieHDV());
        
        System.out.println("======== Glouton ========");
        test.glouton();*/
        
    }

}