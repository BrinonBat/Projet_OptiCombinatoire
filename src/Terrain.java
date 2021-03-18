import java.util.ArrayList;

public class Terrain {
    private ArrayList<Batiment> li_bat;
    private int larg,prof;
    private int terrain[][];

    //constructeur
    public Terrain(int larg, int prof){
        li_bat = new ArrayList<Batiment>();
        this.larg=larg;
        this.prof=prof;
        this.terrain = new int[larg][prof];
        for(int x = 0; x < prof; ++x) {
        	for(int y = 0; y < larg; ++y) {
        		terrain[x][y] = 0;
        	}
        }
    }

    /************méthodes***************/

    // Ajoute un batiment sur le terrain
    public void addBatiment(Batiment b) {
    	if(estValide(b)) {
	    	li_bat.add(b);
	    	for(int x = b.getX(); x < (b.getX() + b.getProf()); ++x) {
	    		for(int y = b.getY(); y < (b.getY() + b.getLarg()); ++y) {
    				terrain[x][y] = li_bat.size();
	    		}
	    	}
    	}
    	else {
    		System.out.println(" \n Erreur !! Position du batiment " + (li_bat.size()+1) + " incorrecte \n");
    	}
    }
    
    public boolean estValide(Batiment b) {
        //verifie qu'on ne déborde pas du terrain
    	if(b.getX()<0 || b.getY()<0 || (b.getX() + b.getProf() > larg) || (b.getY() + b.getProf() > prof)) 
            return false;
        // verifie que le batiment ne se superpose pas à un autre
    	for(int x = b.getX(); x < (b.getX() + b.getProf()); ++x) {
    		for(int y = b.getY(); y < (b.getY() + b.getLarg()); ++y) {
				if(terrain[x][y] != 0)
					return false;
    		}
    	}
    	return true;
    }
    
    public void afficherTerrain() {
    	String ligne = "";
    	for(int y = 0; y < prof; ++y) {
    		ligne += "| ";
    		for(int x = 0; x < larg; ++x) {
    			ligne += terrain[x][y] + " | ";
    		}
    		ligne += "\n";
    	}
    	System.out.println(ligne);
    }
    
    public boolean estRelieHDV() {
    	Batiment hdv = li_bat.get(0);
    	int h_x = hdv.getX();
    	int h_y = hdv.getY();
    	
    	for(int x = 0; x < prof; ++x) {
        	for(int y = 0; y < larg; ++y) {
        		if(terrain[x][y] == 0)
        			terrain[x][y] = -1;
        	}
        }
    	
    	
    	
    	return false;
    }
    
    
    /*
    public void afficher(){
        for(int x=0;x<larg;x++){ // lignes
            String ligne="| ";
            int sur_la_case=-1; // numero du batiment actuellement sur la case
            for(int y=0;y<prof;y++){ // colonnes
                sur_la_case=-1;
                //parcours des batiments pour en trouver un qui doit être affiché sur la case actuelle
                for(int pos=0;pos<li_bat.size();pos++){
                    if(li_bat.get(pos).estSur(x,y)){
                        if(sur_la_case!=-1){ // cas d'erreur
                            System.out.println(" \n \n ERREUR : "+pos+" et "+sur_la_case+" sont tous les deux sur la case ["+x+","+y+"] \n \n");
                            sur_la_case=-2;
                            break;
                        }else{ // cas de case occupée
                            sur_la_case=pos;
                            ligne+=(pos+1)+" | ";
                        }
                    }
                }
                if(sur_la_case==-1) ligne+="0 | "; // cas de case vide
                if(sur_la_case==-2) break; // cas d'erreur, donc arrêt de l'affichage
            }
            if(sur_la_case==-2) break;
            System.out.println(ligne);
        }
    }

    */

    /***********accesseurs**************/

    // ils serviront pas forcément mais je les ajoutes pour l'instant, au cas où

    //getter
    public int getLarg(){return larg;}
    public int getProf(){return prof;}
    public ArrayList<Batiment> getliBat(){return li_bat;}
}
