public class Launcher {
    Terrain terrain;
    
    public Launcher(Terrain t){
        terrain=t;
    }

    public void effectuer(int nb_rep,int algo){
        int sum=0;
        int square_sum=0;
        int sum_case_vide = 0;
        int sum_ratio_aire = 0;
        switch(algo){
            case 1:{
                for (int i = 0; i < nb_rep; i++) {
                    terrain.glouton(false,false); 
                    int score=terrain.calculeScore();
                    sum+=score;
                    square_sum+= score*score;
                    sum_case_vide += terrain.nbCaseVide(terrain.getTerrain());
                    sum_ratio_aire += (score*100)/(terrain.getLarg()*terrain.getProf());
                    terrain.terrainVide();
                }
                break;
            } 
            case 2:{
                for (int i = 0; i < nb_rep; i++) {
                    terrain.gloutonEncombrement(false,false); 
                    int score=terrain.calculeScore();
                    sum+=score;
                    square_sum+= score*score;
                    sum_case_vide += terrain.nbCaseVide(terrain.getTerrain());
                    sum_ratio_aire += (score*100)/(terrain.getLarg()*terrain.getProf());
                    terrain.terrainVide();
                }
                break;
            }  
            case 3:{
                for (int i = 0; i < nb_rep; i++) {
                    terrain.gloutonAire(false,false); 
                    int score=terrain.calculeScore();
                    sum+=score;
                    square_sum+= score*score;
                    sum_case_vide += terrain.nbCaseVide(terrain.getTerrain());
                    sum_ratio_aire += (score*100)/(terrain.getLarg()*terrain.getProf());
                    terrain.terrainVide();
                }
                break;
            }
            case 4:{
                for (int i = 0; i < nb_rep; i++) {
                    terrain.gloutonAléatoire(false,false); 
                    int score=terrain.calculeScore();
                    sum+=score;
                    square_sum+= score*score;
                    sum_case_vide += terrain.nbCaseVide(terrain.getTerrain());
                    sum_ratio_aire += (score*100)/(terrain.getLarg()*terrain.getProf());
                    terrain.terrainVide();
                }
                break;
            }
            case 5:{
                for (int i = 0; i < nb_rep; i++) {
                    terrain.gloutonPerso(false,false); 
                    int score=terrain.calculeScore();
                    sum+=score;
                    square_sum+= score*score;
                    sum_case_vide += terrain.nbCaseVide(terrain.getTerrain());
                    sum_ratio_aire += (score*100)/(terrain.getLarg()*terrain.getProf());
                    terrain.terrainVide();
                }
                break;
            }
            case 6:{
                for (int i = 0; i < nb_rep; i++) {
                    terrain.gloutonAireEtEncombrement(false,false); 
                    int score=terrain.calculeScore();
                    sum+=score;
                    square_sum+= score*score;
                    sum_case_vide += terrain.nbCaseVide(terrain.getTerrain());
                    sum_ratio_aire += (score*100)/(terrain.getLarg()*terrain.getProf());
                    terrain.terrainVide();
                }
                break;
            }
            default: System.out.println("selectionnez un algo entre 1 et 6");
        }

        
        double moyenne = sum / nb_rep;
        double ecart_type = Math.sqrt(square_sum / nb_rep - moyenne * moyenne); //cf. Wikipedia
        double case_vide = sum_case_vide / nb_rep;
        double ratio_aire = sum_ratio_aire / nb_rep;
        System.out.println("sum is "+sum);
        System.out.println("Aprés "+nb_rep+" répétitions, voici les statistiques:");
        System.out.println("Moyenne : "+moyenne+"\nÉcart-type : "+ecart_type + "\nMoyenne case vide : " + case_vide + "\nMoyenne ratio aire : " + ratio_aire);
    }
}
