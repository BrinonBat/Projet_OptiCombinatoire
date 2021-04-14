import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Scanner;

//import jdk.internal.org.jline.terminal.Size; ??? ça à l'air de fonctionner sans, mais je le laisse en commentaire au cas où

public class Main{
    public static void main(String[] args) throws IOException {

        if(args.length!=1){
            System.out.println("utilisation : Main [nom_fichier.dat]");
        }
        else {
            String filename = args[0];
            try {
                // lecture du fichier
                InputStream is = new FileInputStream(filename);
                InputStreamReader isr = new InputStreamReader(is);
                BufferedReader br = new BufferedReader(isr);
                String ligne;
                String res[];
                int compteur = 0;
                //int nbBatiment;
                Terrain terrain= null;
                while((ligne = br.readLine()) != null) {
                    if(compteur == 0) { // Première ligne c'est la taille du terrain
                        res = ligne.split(" ");
                        int largeur = Integer.parseInt(res[0]);
                        int profondeur = Integer.parseInt(res[1]);
                        terrain = new Terrain(largeur,profondeur);
                    }
                    else if(compteur == 1) { // Deuxième ligne c'est le nb de batiment
                  //      nbBatiment = Integer.parseInt(ligne.trim());
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
                br.close();

            // application de l'algo glouton
                //selection de la méthode de placement de l'HDV
                Scanner sc=new Scanner(System.in);
                System.out.println("l'hotel de ville doit-il être placé de façon optimisée? true/false");
                boolean opti_hdv;
                opti_hdv=Boolean.parseBoolean(sc.nextLine());
                System.out.println("valeur saisie: "+opti_hdv);

                //sélection de l'algo glouton à utiliser
                System.out.println("Choisissez un algo Glouton à tester parmis les suivants:");
                short test_num;
                System.out.println(" 1: classique");
                System.out.println(" 2: bâtiments d'aire maximale en premier");
                System.out.println(" 3: bâtiments les plus encombrants en premier");
                System.out.println(" 4: placement des bâtiments dans un ordre aléatoire");
                System.out.println(" 5: personnalisé");
                test_num=Short.parseShort(sc.nextLine());

                sc.close();

                //effectue le test saisi
                switch(test_num){
                    case 1: terrain.glouton(opti_hdv); break;
                    case 2: terrain.gloutonAire(opti_hdv); break;
                    case 3: terrain.gloutonEncombrement(opti_hdv);  break;
                    case 4: terrain.gloutonAléatoire(opti_hdv); break;
                    case 5: //terrain.gloutonPerso(); break;
                }

                //affichage du résultat
                terrain.affichageGlouton();
            } catch (FileNotFoundException e) {
                System.out.println(" erreur dans le main lors de la lecture du fichier");
                e.printStackTrace();
            }
            
        }
        
    }

}