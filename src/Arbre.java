import java.util.ArrayList;

class Noeud{
    private Noeud pere;
    private ArrayList<Noeud> fils;
    private Batiment b;
    private boolean est_ferme;
    public Noeud(Batiment b){
        this.b=b;
        est_ferme=false;
    }

    /******accesseurs******/
    public Noeud getPere(){return pere;}
    public Batiment getBat(){return b;}
    public Noeud getFils(int pos){return fils.get(pos);}
    public int getNbFils(){return fils.size();}
    public boolean estFerme(){return est_ferme;}
    public void ferme(){est_ferme=true;}
    public void setPere(Noeud p){this.pere=p;}
    public void addFils(Noeud n){
        n.setPere(this);
        fils.add(n);
    }

    //supprime ses fils (fait récursivement, permet de supprimer toute sa branche)
    public void delFils(){
        for (int i = 0; i < fils.size(); i++) {
            fils.get(i).delFils();
        }
        fils.clear();
    }

    public int getBestBranch(ArrayList<Batiment> li_bat){
        li_bat.add(b);//le premier batiment est celui du noeud actuel
        if(est_ferme) return b.getAire(); // si c'est une feuille, on retourne le score
        ArrayList<Batiment> li_bat_tmp=li_bat; // tampon
        int score=0;
        ArrayList<Batiment> meilleure_branche= new ArrayList<Batiment>();
        //parcours des fils, et ajout de la branche avec le meilleur score
        for(int i=0;i<fils.size();i++){
            int score_branche=fils.get(i).getBestBranch(li_bat_tmp); // récupération du score de la branche du fils testé
            if(score_branche>score){
                meilleure_branche=li_bat_tmp;
                score=score_branche;
            } 
            li_bat_tmp=li_bat;
        }
        //on garde la meilleure branche
        li_bat=meilleure_branche;
        return score+b.getAire(); // retourne le score mis à jour
    }

    public void construitBranche(ArrayList<Batiment> li_bat){

    }

}

public class Arbre {
    private Noeud racine;
    private ArrayList<Noeud> li_noeuds;

    public Arbre(Batiment hdv){
        racine=new Noeud(hdv);
    }

    public ArrayList<Batiment> bestBranch(){
        ArrayList<Batiment> result = new ArrayList<Batiment>();
        racine.getBestBranch(result);
        return result;
    }

    //genere un arbre à partir d'une liste de batiments
    public Arbre(ArrayList<Batiment> li_bat){
        ArrayList<Batiment> li_bat_noeuds=li_bat;

        //on retire de la liste les batiments qui n'étaient pas placés
        for(int i=li_bat.size()-1;i>=0;i--){
            if(!li_bat_noeuds.get(i).estRelie())
            	li_bat_noeuds.remove(i);
        }

        racine=new Noeud(li_bat.get(0));
        li_noeuds=new ArrayList<Noeud>();
        li_noeuds.add(racine);
        for (int i=1;i<li_bat_noeuds.size();i++) {
            li_noeuds.add(i, new Noeud(li_bat_noeuds.get(i)));
            li_noeuds.get(i-1).addFils(li_noeuds.get(i)); // ajout du nouveau noeud comme fils du précédant
        }
    }
    
    public ArrayList<Noeud> getListNoeuds() {
    	return this.li_noeuds;
    }
    
}
