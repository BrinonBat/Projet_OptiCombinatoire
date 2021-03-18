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
    		System.out.println(" \n Erreur !! Position du batiment " + (li_bat.size()+1) + " incorrectes \n");
    	}
    }
    
    public boolean estValide(Batiment b) {
    	if((b.getX() + b.getProf() > larg) || (b.getY() + b.getProf() > prof))
    		return false;
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


    //verifie que le batiment est à un emplacement valide
    public boolean batimentBienPlace(Batiment bat){
        for(int pos=0;pos<li_bat.size();pos++){
            if(li_bat.get(pos)!=bat){
                if(bat.getX()<0 || (bat.getX()+bat.getLarg()-1)>larg || bat.getY()<0 || (bat.getY()+bat.getProf()-1)>prof) return false; // verifie que le batiment rentre entierement dans le plateau
                if(seSuperposent(li_bat.get(pos),bat)) return false; // verifie la superposition
            }
        }
        return true;
    }

    //verifie que le batiment A et le batiment B ne se superposent pas (return true s'ils se superposent)
    public boolean seSuperposent(Batiment bat_a, Batiment bat_b){

        // si le batiment a est à gauche du batiment b
        if(bat_a.getX()<bat_b.getX()){ 
            if((bat_a.getX()+bat_a.getLarg()-1)<bat_b.getX()){ // et que le batiment b ne croise pas a sur l'axe des X
                return false; // alors qu'importe Y, ils ne se croisent pas
            }else if( // sinon, si le batiment b et le batiment a ne se croisent pas sur l'axe Y, alors ils ne se croisent pas
                (bat_a.getY()<bat_b.getY() && (bat_b.getY()+bat_b.getProf()-1)<bat_a.getY())
                || bat_b.getY()<bat_a.getY() && (bat_a.getY()+bat_a.getProf()-1)<bat_b.getY()
            ) return false;
        }

        // si le batiment a est à droite du batiment b
        else if(bat_a.getX()>bat_b.getX()){ 
            if((bat_b.getX()+bat_b.getLarg()-1)<bat_a.getX()){ // et que le batiment a ne croise pas b sur l'axe des X
                return false; // alors qu'importe Y, ils ne se croisent pas
            }else if( // sinon, si le batiment b et le batiment a ne se croisent pas sur l'axe Y, alors ils ne se croisent pas
                (bat_a.getY()<bat_b.getY() && (bat_b.getY()+bat_b.getProf()-1)<bat_a.getY())
                || bat_b.getY()<bat_a.getY() && (bat_a.getY()+bat_a.getProf()-1)<bat_b.getY()
            ) return false;
        }
        
        

        // sinon, ils se superposent
        return true; 
    }
    */

    /***********accesseurs**************/

    // ils serviront pas forcément mais je les ajoutes pour l'instant, au cas où

    //getter
    public int getLarg(){return larg;}
    public int getProf(){return prof;}
    public ArrayList<Batiment> getliBat(){return li_bat;}
}
