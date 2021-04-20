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
                            terrain.getliBat().add(new Batiment(compteur-1,largeur, profondeur, true));
                        else // batiment normal
                            terrain.getliBat().add(new Batiment(compteur-1,largeur, profondeur));
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
                System.out.println(" 2: bâtiments les plus encombrants en premier");
                System.out.println(" 3: bâtiments d'aire maximale en premier");
                System.out.println(" 4: placement des bâtiments dans un ordre aléatoire");
                System.out.println(" 5: coef personnalisé aire/encombrement");
                System.out.println(" 6: bâtiments d'aire maximale avec le meilleur coef d'abord");
                System.out.println(" 7: Branch & Bound profondeur");
                System.out.println(" 8: Launcher");
                test_num=Short.parseShort(sc.nextLine());
                
                //effectue le test saisi
                switch(test_num){
                    case 1: terrain.glouton(opti_hdv,true); break;
                    case 2: terrain.gloutonEncombrement(opti_hdv,true);  break;
                    case 3: terrain.gloutonAire(opti_hdv,true); break;
                    case 4: terrain.gloutonAléatoire(opti_hdv,true); break;
                    case 5: terrain.gloutonAireEtEncombrement(opti_hdv,true); break;
                    case 6: terrain.gloutonPerso(opti_hdv,true); break;
                    case 7: System.out.println("Fonctionnalité pas opérationnelle");break;
                    case 8: 
                    	System.out.println(" Veuillez saisir le nombre de répétitions : ");
                    	int nb_rep = Integer.parseInt(sc.nextLine());
                    	System.out.println(" Veuillez saisir le numéro de l'algo : ");
                    	System.out.println(" 1: Glouton classique");
                    	System.out.println(" 2: Glouton par encombrement");
                    	System.out.println(" 3: Glouton par aire");
                    	System.out.println(" 4: Glouton aléatoire");
                    	System.out.println(" 5: Glouton personalisé");
                    	System.out.println(" 6: Glouton ratio aire/encombrement");
                    	int algo = Integer.parseInt(sc.nextLine());
                    	new Launcher(terrain).effectuer(nb_rep, algo);
                        break;
                    default: System.out.println(" entrez une valeur entre 1 et 8 inclus");break;
                }
                
                sc.close();
                //affichage du résultat
                if(test_num != 8)
                	terrain.affichageResultat();

            } catch (FileNotFoundException e) {
                System.out.println(" erreur dans le main lors de la lecture du fichier");
                e.printStackTrace();
            }
            
        }
        
    }

}