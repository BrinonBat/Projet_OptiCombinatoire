import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;

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

    // Ajoute un batiment sur le terrain si sa position est valide
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
    
    // Regarde si la position du batiment est valide
    public boolean estValide(Batiment b) {
    	if((b.getX() + b.getProf() > larg) || (b.getY() + b.getProf() > prof)) // Regarde si la taille ne dépasse pas le terrain
    		return false;
    	for(int x = b.getX(); x < (b.getX() + b.getProf()); ++x) {
    		for(int y = b.getY(); y < (b.getY() + b.getLarg()); ++y) {
				if(terrain[x][y] != 0) // Si la valeur est de 0 c'est que la case est vide sinon elle est occupée
					return false;
    		}
    	}
    	return true;
    }
    
    // Affiche le terrain
    // Une case vide sera affiché avec le nombre 0
    // Sinon la case sera occupé par le numéro du batiment à cette position
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
    
    // Regarde si chaque batiment présent dans le terrain est relié à l'hotel de ville
    public boolean estRelieHDV() {
    	Batiment hdv = this.getHDV();
    	int h_x = hdv.getX();
    	int h_y = hdv.getY();
    	
    	Deque<Case> pile = new ArrayDeque<Case>();
    	ArrayList<Case> parcourue = new ArrayList<Case>();
    	
    	// On parcoure les cases autour de l'hdv
    	for(int x = (h_x-1); x <= (hdv.getProf() + h_x); ++x) {
    		for(int y = (h_y-1); y <= (hdv.getLarg() + h_y); ++y) {
    			if(x >= 0 && x < this.prof && y >= 0 && y < this.larg) {
    				if(terrain[x][y] == 0) { // ajoute les cases vides qui sont autour de l'hdv
    					System.out.println("Ajout de la case (" + x + "," + y +")");
    					pile.push(new Case(x,y)); 
    					parcourue.add(new Case(x,y));
    				}
    			}
    		}
    	}
    	
    	ArrayList<Integer> estRelie = new ArrayList<>();
    	
    	// Parcoure la pile
    	while(pile.size() > 0 && estRelie.size() != li_bat.size()) {
    		Case route = pile.pop();
    		int x = route.getX();
    		int y = route.getY();
    		
    		System.out.println("Case (" + x + "," + y + ")");
    		
    		// On regarde la case au-desssus de la case courante
    		if(y-1 >= 0) {
	    		if(terrain[x][y-1] == 0 ) {
	    			if(!this.estParcourue(parcourue, new Case(x,y-1))) { // Si la case est vide et est non parcourue on l'ajoute à la pile
		    			System.out.println("Ajout de la case (" + x + "," + (y-1) +")");
		    			pile.push(new Case(x,y-1));
		    			parcourue.add(new Case(x,y-1));
	    			}
	    		}
	    		else { // Sinon c'est un batiment
	    			if(!li_bat.get(terrain[x][y-1]-1).estRelie()) { // s'il est pas déjà indiqué comme relié alors on le relie
	    				System.out.println("Batiment " + terrain[x][y-1] + " est relié à l'hdv");
	    				li_bat.get(terrain[x][y-1]-1).setRelie(true);
	    				estRelie.add(terrain[x][y-1]-1);
	    			}
	    		}
    		}
    		
    		// On regarde la case au-desssous de la case courante
    		if(y+1 < this.larg) {
	    		if(terrain[x][y+1] == 0 ) {
	    			if(!this.estParcourue(parcourue, new Case(x,y+1))) {
		    			System.out.println("Ajout de la case (" + x + "," + (y+1) +")");
		    			pile.push(new Case(x,y+1));
		    			parcourue.add(new Case(x,y+1));
	    			}
	    		}
	    		else {
	    			if(!li_bat.get(terrain[x][y+1]-1).estRelie()) {
	    				System.out.println("Batiment " + terrain[x][y+1] + " est relié à l'hdv");
	    				li_bat.get(terrain[x][y+1]-1).setRelie(true);
	    				estRelie.add(terrain[x][y+1]-1);
	    			}
	    		}
    		}
    		
    		// On regarde la case à gauche de la case courante
    		if(x-1 >= 0) {
	    		if(terrain[x-1][y] == 0 ) {
	    			if(!this.estParcourue(parcourue, new Case(x-1,y))) {
		    			System.out.println("Ajout de la case (" + (x-1) + "," + y +")");
		    			pile.push(new Case(x-1,y));
		    			parcourue.add(new Case(x-1,y));
	    			}
	    		}
	    		else {
	    			if(!li_bat.get(terrain[x-1][y]-1).estRelie()) {
	    				System.out.println("Batiment " + terrain[x-1][y] + " est relié à l'hdv");
	    				li_bat.get(terrain[x-1][y]-1).setRelie(true);
	    				estRelie.add(terrain[x-1][y]-1);
	    			}
	    		}
    		}
    		
    		// On regarde la case à droite de la case courante
    		if(x+1 < this.prof) {
	    		if(terrain[x+1][y] == 0 ) {
	    			if(!this.estParcourue(parcourue, new Case(x+1,y))) {
		    			System.out.println("Ajout de la case (" + (x+1) + "," + y +")");
		    			pile.push(new Case(x+1,y));
		    			parcourue.add(new Case(x+1,y));
	    			}
	    		}
	    		else {
	    			if(!li_bat.get(terrain[x+1][y]-1).estRelie()) {
	    				System.out.println("Batiment " + terrain[x+1][y] + " est relié à l'hdv");
	    				li_bat.get(terrain[x+1][y]-1).setRelie(true);
	    				estRelie.add(terrain[x+1][y]-1);
	    			}
	    		}
    		}
    		
    	}
    	if(estRelie.size() == li_bat.size())
    		return true;
    	else
    		return false;
    }
    
    // Regarde si un élément est déjà présent dans la liste
    public boolean estParcourue(ArrayList<Case> cases, Case route) {
    	for(int i = 0; i < cases.size(); ++i) {
    		if(cases.get(i).getX() == route.getX() && cases.get(i).getY() == route.getY())
    			return true;
    	}
    	return false;
    }
    
    // Calcul le score du terrain en faisant la somme des aires des batiments
    public int calculeScore() {
    	int aire = 0;
    	for(int i = 0; i < li_bat.size(); ++i) {
    		aire += li_bat.get(i).getLarg() * li_bat.get(i).getProf();
    	}
    	return aire;
    }
    
    // Place les batiments en suivant l'algorithme glouton
    public void glouton() {
    	
    }

    /***********accesseurs**************/

    // ils serviront pas forcément mais je les ajoutes pour l'instant, au cas où

    //getter
    public int getLarg(){return larg;}
    public int getProf(){return prof;}
    public ArrayList<Batiment> getliBat(){return li_bat;}
    // retourne l'hotel de ville
    public Batiment getHDV() {
    	for(int i = 0; i < li_bat.size(); ++i) {
    		if(li_bat.get(i).estHDV())
    			return li_bat.get(i);
    	}
    	return null;
    }
}
