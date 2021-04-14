import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;
import java.util.Random;

public class Terrain {
    private ArrayList<Batiment> li_bat;
    private ArrayList<Batiment> li_bat_bb;
    private int larg,prof;
    private int terrain[][];
    private int terrain_bb[][];
    private int borneSup;
    private int borneInf;

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

/*
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
*/    
    // Regarde si la position du batiment est valide
    public boolean estValide(Batiment b) {
    	if((b.getX() + b.getProf() > larg) || (b.getY() + b.getProf() > prof)) // Regarde si la taille ne dépasse pas le terrain
    		return false;

  /*
        //verifie qu'on ne déborde pas du terrain
    	if(b.getX()<0 || b.getY()<0 || (b.getX() + b.getProf() > larg) || (b.getY() + b.getProf() > prof)) 
            return false;
        // verifie que le batiment ne se superpose pas à un autre

*/
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
    			if(terrain[x][y] < 10)
    				ligne += terrain[x][y] + " | ";
    			else
    				ligne += terrain[x][y] + "| ";
    		}
    		ligne += "\n";
    	}
    	System.out.println(ligne);
    }
    
    // Retourne le nombre de batiment sur le terrain
    public int getNbBatiment() {
    	ArrayList<Integer> batiments = new ArrayList<>();
    	for(int x = 0; x < prof; ++x) {
        	for(int y = 0; y < larg; ++y) {
        		if(terrain[x][y] != 0) {
        			if(!batiments.contains(terrain[x][y])) {
        				batiments.add(terrain[x][y]);
        			}
        		}
        	}
        }
    	return batiments.size();
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
    					//System.out.println("Ajout de la case (" + x + "," + y +")");
    					pile.push(new Case(x,y)); 
    					parcourue.add(new Case(x,y));
    				}
    			}
    		}
    	}
    	
    	ArrayList<Integer> estRelie = new ArrayList<>();
    	int nb_batiment = this.getNbBatiment();
    	
    	// Parcoure la pile
    	while(pile.size() > 0 && estRelie.size() != nb_batiment) {
    		Case route = pile.pop();
    		int x = route.getX();
    		int y = route.getY();
    		
    		//System.out.println("Case (" + x + "," + y + ")");
    		
    		// On regarde la case au-desssus de la case courante
    		if(y-1 >= 0) {
	    		if(terrain[x][y-1] == 0 ) {
	    			if(!this.estParcourue(parcourue, new Case(x,y-1))) { // Si la case est vide et est non parcourue on l'ajoute à la pile
		    			//System.out.println("Ajout de la case (" + x + "," + (y-1) +")");
		    			pile.push(new Case(x,y-1));
		    			parcourue.add(new Case(x,y-1));
	    			}
	    		}
	    		else { // Sinon c'est un batiment
	    			if(!li_bat.get(terrain[x][y-1]-1).estRelie()) { // s'il est pas déjà indiqué comme relié alors on le relie
	    				//System.out.println("Batiment " + terrain[x][y-1] + " est relié à l'hdv");
	    				li_bat.get(terrain[x][y-1]-1).setRelie(true);
	    			}
	    			if(!estRelie.contains(terrain[x][y-1]-1))
	    				estRelie.add(terrain[x][y-1]-1);
	    		}
    		}
    		
    		// On regarde la case au-desssous de la case courante
    		if(y+1 < this.larg) {
	    		if(terrain[x][y+1] == 0 ) {
	    			if(!this.estParcourue(parcourue, new Case(x,y+1))) {
		    			//System.out.println("Ajout de la case (" + x + "," + (y+1) +")");
		    			pile.push(new Case(x,y+1));
		    			parcourue.add(new Case(x,y+1));
	    			}
	    		}
	    		else {
	    			if(!li_bat.get(terrain[x][y+1]-1).estRelie()) {
	    				//System.out.println("Batiment " + terrain[x][y+1] + " est relié à l'hdv");
	    				li_bat.get(terrain[x][y+1]-1).setRelie(true);
	    			}
	    			if(!estRelie.contains(terrain[x][y+1]-1))
	    				estRelie.add(terrain[x][y+1]-1);
	    		}
    		}
    		
    		// On regarde la case à gauche de la case courante
    		if(x-1 >= 0) {
	    		if(terrain[x-1][y] == 0 ) {
	    			if(!this.estParcourue(parcourue, new Case(x-1,y))) {
		    			//System.out.println("Ajout de la case (" + (x-1) + "," + y +")");
		    			pile.push(new Case(x-1,y));
		    			parcourue.add(new Case(x-1,y));
	    			}
	    		}
	    		else {
	    			if(!li_bat.get(terrain[x-1][y]-1).estRelie()) {
	    				//System.out.println("Batiment " + terrain[x-1][y] + " est relié à l'hdv");
	    				li_bat.get(terrain[x-1][y]-1).setRelie(true);
	    			}
	    			if(!estRelie.contains(terrain[x-1][y]-1))
	    				estRelie.add(terrain[x-1][y]-1);
	    		}
    		}
    		
    		// On regarde la case à droite de la case courante
    		if(x+1 < this.prof) {
	    		if(terrain[x+1][y] == 0 ) {
	    			if(!this.estParcourue(parcourue, new Case(x+1,y))) {
		    			//System.out.println("Ajout de la case (" + (x+1) + "," + y +")");
		    			pile.push(new Case(x+1,y));
		    			parcourue.add(new Case(x+1,y));
	    			}
	    		}
	    		else {
	    			if(!li_bat.get(terrain[x+1][y]-1).estRelie()) {
	    				//System.out.println("Batiment " + terrain[x+1][y] + " est relié à l'hdv");
	    				li_bat.get(terrain[x+1][y]-1).setRelie(true);	
	    			}
	    			if(!estRelie.contains(terrain[x+1][y]-1))
	    				estRelie.add(terrain[x+1][y]-1);
	    		}
    		}
    		
    	}
    	if(estRelie.size() == nb_batiment)
    		return true;
    	return false;
    }
    
    // Regarde si la position est relié à l'hdv
    public boolean estRelie(int bx, int by, int largeur, int profondeur) {
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
    					pile.push(new Case(x,y)); 
    					parcourue.add(new Case(x,y));
    				}
    			}
    		}
    	}
    	
    	while(pile.size() > 0) {
    		Case route = pile.pop();
    		int x = route.getX();
    		int y = route.getY();
    		    		
    		// On regarde la case au-desssus de la case courante
    		if(y-1 >= 0) {
	    		if(terrain[x][y-1] == 0 ) {
	    			if(x >= bx && x <= (bx+profondeur) && (y-1) >= by && (y-1) <= (by+largeur)) { // Si les coordonnées correspondent alors la position est relié
	    				return true;
	    			}
	    			if(!this.estParcourue(parcourue, new Case(x,y-1))) { // Si la case est vide et est non parcourue on l'ajoute à la pile
		    			pile.push(new Case(x,y-1));
		    			parcourue.add(new Case(x,y-1));
	    			}
	    		}
    		}
    		
    		// On regarde la case au-desssous de la case courante
    		if(y+1 < this.larg) {
	    		if(terrain[x][y+1] == 0 ) {
	    			if(x == bx && x <= (bx+profondeur) && (y+1) == by && (y+1) <= (by+largeur)) { // Si les coordonnées correspondent alors la position est relié
	    				return true;
	    			}
	    			if(!this.estParcourue(parcourue, new Case(x,y+1))) {
		    			pile.push(new Case(x,y+1));
		    			parcourue.add(new Case(x,y+1));
	    			}
	    		}
    		}
    		
    		// On regarde la case à gauche de la case courante
    		if(x-1 >= 0) {
	    		if(terrain[x-1][y] == 0 ) {
	    			if((x-1) == bx && (x-1) <= (bx+profondeur) && y == by && y <= (by+largeur)) { // Si les coordonnées correspondent alors la position est relié
	    				return true;
	    			}
	    			if(!this.estParcourue(parcourue, new Case(x-1,y))) {
		    			pile.push(new Case(x-1,y));
		    			parcourue.add(new Case(x-1,y));
	    			}
	    		}
    		}
    		
    		// On regarde la case à droite de la case courante
    		if(x+1 < this.prof) {
	    		if(terrain[x+1][y] == 0 ) {
	    			if((x+1) == bx && (x+1) <= (bx+profondeur) && y == by && y <= (by+largeur)) { // Si les coordonnées correspondent alors la position est relié
	    				return true;
	    			}
	    			if(!this.estParcourue(parcourue, new Case(x+1,y))) {
		    			pile.push(new Case(x+1,y));
		    			parcourue.add(new Case(x+1,y));
	    			}
	    		}
    		}
    		
    	}
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
    	ArrayList<Batiment> batiments = new ArrayList<>();
    	ArrayList<Integer> batiments_visite = new ArrayList<>();
    	for(int x = 0; x < prof; ++x) {
        	for(int y = 0; y < larg; ++y) {
        		if(terrain[x][y] != 0) {
        			if(!batiments_visite.contains(terrain[x][y])) {
        				batiments_visite.add(terrain[x][y]);
        				batiments.add(li_bat.get(terrain[x][y]-1));
        			}
        		}
        	}
        }
    	int aire = 0;
    	for(int i = 0; i < batiments.size(); ++i) {
    		aire += batiments.get(i).getLarg() * batiments.get(i).getProf();
    	}
    	return aire;
    }
    
    // Ajoute l'hdv aléatoirement sur un terrain vide
    public void ajoutAleatoireHDV() {   	
    	Batiment hdv = this.getHDV();
    	int largeur = hdv.getLarg();
    	int profondeur = hdv.getProf();
    	ArrayList<Case> valides = new ArrayList<Case>(); // Liste des positions valides
    	
    	for(int x = 0; x < this.prof; ++x) {
    		for(int y = 0; y < this.larg; ++y) {
    			if((x+profondeur) <= this.prof && (y+largeur) <= this.larg) {
    				valides.add(new Case(x,y)); // On ajoute toutes les positions valides
    			}
    		}
    	}
    	
    	Random rand = new Random();
    	int indice = rand.nextInt(valides.size()); // On tire une position aléatoire
    	Case pos = valides.get(indice);

    	for(int x = pos.getX(); x < (pos.getX() + profondeur); ++x) {
    		for(int y = pos.getY(); y < (pos.getY() + largeur); ++y) {
    			terrain[x][y] = 1;
    		}
    	}

    }

/////////////////////////////////////////////////// GLOUTONS ///////////////////////////////////////////////////////////////////////////////////////////////////////////

    // On parcoure chaque case du terrain
    // Si à la position courante on peut ajouter un batiment qui
    // a une position valide,
    // est relié à l'hdv,
    // et ne bloque pas de route pour un autre batiment
    // alors on l'ajoute
    public void repartitionBatiment() {
    	for(int x = 0; x < prof; ++x) {
    		for(int y = 0; y < larg; ++y) {
    			if(terrain[x][y] == 0) {
    				getBatimentBienPlace(x,y);		
    			}    			
    		}
    	}
    }
    
    // Retourne vrai si un batiment est déjà présent sur le terrain
    public boolean batimentDejaPlace(int indice) {
    	for(int x = 0; x < prof; ++x) {
    		for(int y = 0; y < larg; ++y) {
    			if(terrain[x][y] == indice) {
    				return true;
    			}    			
    		}
    	}
    	return false;
    }
    
    // Cherche le premier batiment, non placé, qui est valide à cette position et l'ajoute au terrain
    public void getBatimentBienPlace(int x, int y) {
    	for(int i = 0; i < li_bat.size(); ++i) {
    		if(!batimentDejaPlace(i+1)) {
				Batiment bat = li_bat.get(i);
				int largeur = bat.getLarg();
				int profondeur = bat.getProf();
				if((x+profondeur) <= prof && (y+largeur) <= larg) { // Si le batiment rentre dans le terrain
					if(estVide(x,y,largeur,profondeur)) { // Si le batiment peut-être ajouté à la position courante
						if(estRelie(x,y,largeur,profondeur)) { // Si la position est relié à l'hdv
							//System.out.println("Batiment " + (i+1) + " est placé et est relié");
							for(int a = x; a < (x + profondeur); ++a) { // On ajoute le batiment
		    		    		for(int b = y; b < (y + largeur); ++b) {
		    		    			terrain[a][b] = i+1;
		    		    		}
		    		    	}
							//this.afficherTerrain();
							if(!estRelieHDV()) { // Si tous les batiments ne sont pas relié après l'ajout alors on retire le batiment
		    					//System.out.println("Batiment " + (i+1) + " bloque une route");
		    					for(int a = x; a < (x + profondeur); ++a) {
			    		    		for(int b = y; b < (y + largeur); ++b) {
			    		    			terrain[a][b] = 0;
			    		    		}
			    		    	}
		    				}
							else{
                                System.out.println("Batiment "+(i+1)+" placé =====> (aire:"+li_bat.get(i).getAire()+" , encombrement:"+li_bat.get(i).getEncombrement()+")");
                                return;
                            }
						}
					}
				}
    		}
		}
    }
    
    // Regarde si l'espace représenté par les données est vide
    public boolean estVide(int x, int y, int largeur, int profondeur) {
    	for(int a = x; a < (x+profondeur); ++a) {
			for(int b = y; b < (y+largeur); ++b) {
				if(terrain[a][b] != 0)
					return false;
			}
		}
    	return true;
    }


    // Applique l'algorithme glouton
    public void glouton(boolean opti_hdv) {
		if(opti_hdv){
			System.out.println("placement optimisé !");
			ajoutOptiHDV();
		}
		else ajoutAleatoireHDV();
    	repartitionBatiment();
    }
    
    // Applique l'algorithme glouton avec affichage du 
    public void affichageGlouton() {

    	System.out.println("====== Résultat ======");
    	afficherTerrain();
    	System.out.println("Score : " + calculeScore());
    }


    // Quicksort basé sur la valeur d'encombrement de chaque batiment
    public static void quicksortEncombrement(ArrayList<Batiment> tableau, int début, int fin) {
        if (début < fin) {
            int indicePivot = partitionEncombrement(tableau, début, fin);
            quicksortEncombrement(tableau, début, indicePivot-1);
            quicksortEncombrement(tableau, indicePivot+1, fin);
        }
    }
    public static int partitionEncombrement(ArrayList<Batiment> b, int début, int fin) {
        Batiment batPivot = b.get(début);
        int d = début+1;
        int f = fin;
        while (d < f) {
            while(d < f && b.get(f).getEncombrement() <= batPivot.getEncombrement()) f--;
            while(d < f && b.get(d).getEncombrement() >= batPivot.getEncombrement()) d++;
            Batiment temp = b.get(d);
            b.set(d, b.get(f));
            b.set(f, temp);
        }
        if (b.get(d).getEncombrement() < batPivot.getEncombrement()) d--;
        b.set(début, b.get(d));
        b.set(d,batPivot);
        return d;
    }

    //glouton placant en priorité les batiments à plus grand encombrement
    public void gloutonEncombrement(boolean opti_hdv){
        quicksortEncombrement(li_bat,0,li_bat.size()-1);
        glouton(opti_hdv);
    }

    //quicksort basé sur l'aire de chaque batiment
    public static void quicksortAire(ArrayList<Batiment> tableau, int début, int fin) {
        if (début < fin) {
            int indicePivot = partitionAire(tableau, début, fin);
            quicksortAire(tableau, début, indicePivot-1);
            quicksortAire(tableau, indicePivot+1, fin);
        }
    }
    public static int partitionAire(ArrayList<Batiment> b, int début, int fin) {
        Batiment batPivot = b.get(début);
        int d = début+1;
        int f = fin;
        while (d < f) {
            while(d < f && b.get(f).getAire() <= batPivot.getAire()) f--;
            while(d < f && b.get(d).getAire() >= batPivot.getAire()) d++;
            Batiment temp = b.get(d);
            b.set(d, b.get(f));
            b.set(f, temp);
        }
        if (b.get(d).getAire() < batPivot.getAire()) d--;
        b.set(début, b.get(d));
        b.set(d,batPivot);
        return d;
    }

    //glouton placant en priorité les batiments qui ont les plus grandes aires
    public void gloutonAire(boolean opti_hdv){
        quicksortAire(li_bat, 0 , li_bat.size()-1);
        glouton(opti_hdv);
    }

    //glouton placant les batiments dans un ordre aléatoire
    public void gloutonAléatoire(boolean opti_hdv){
        Collections.shuffle(li_bat);
    	glouton(opti_hdv);

    }

    public void gloutonPerso(boolean opti_hdv){

    }
    
    public int nbCaseVide(int plateau[][]) {
    	int nb = 0;
    	for(int x = 0; x < prof; ++x) {
        	for(int y = 0; y < larg; ++y) {
        		if(plateau[x][y] == 0) {
        			++nb;
        		}
        	}
        }
    	return nb;
    }
    
/////////////////////////////////////////////////// BRANCH & BOUND /////////////////////////////////////////////////////////////////////////////////////////////////////////

	/*
    // Calcul le score du terrain en faisant la somme des aires des batiments
    public int calculeScore(int plateau[][], ArrayList<Batiment> bat) {
    	ArrayList<Batiment> batiments = new ArrayList<>();
    	ArrayList<Integer> batiments_visite = new ArrayList<>();
    	for(int x = 0; x < prof; ++x) {
        	for(int y = 0; y < larg; ++y) {
        		if(terrain[x][y] != 0) {
        			if(!batiments_visite.contains(plateau[x][y])) {
        				batiments_visite.add(plateau[x][y]);
        				batiments.add(bat.get(plateau[x][y]-1));
        			}
        		}
        	}
        }
    	int aire = 0;
    	for(int i = 0; i < batiments.size(); ++i) {
    		aire += batiments.get(i).getLarg() * batiments.get(i).getProf();
    	}
    	return aire;
    }
    */

    // Fait tourner l'algo gloutonAire et gloutonEncombrement pour récupérer une solution partielle et le majorant
    public int majorant() {
    	ArrayList<Batiment> memorise_li_bat = this.li_bat;
    	int memorise_terrain[][] = this.terrain;
    	//gloutonEncombrement();
    	int terrain_encombrement[][] = this.terrain;
    	ArrayList<Batiment> li_bat_encombrement = this.li_bat;
    	this.li_bat = memorise_li_bat;
    	this.terrain = memorise_terrain;
    	//gloutonAire();
    	int terrain_aire[][] = this.terrain;
    	ArrayList<Batiment> li_bat_aire = this.li_bat;
    	this.li_bat = memorise_li_bat;
    	this.terrain = memorise_terrain;
    	
    	int nb_vide_encombrement = nbCaseVide(terrain_encombrement);
    	int nb_vide_aire = nbCaseVide(terrain_aire);
    	
    	if(nb_vide_aire <= nb_vide_encombrement) {
    		this.li_bat_bb = li_bat_aire;
    		this.terrain_bb = terrain_aire;
    		return nb_vide_aire;
    	}
    	else {
    		this.li_bat_bb = li_bat_encombrement;
    		this.terrain_bb = terrain_encombrement;
    		return nb_vide_encombrement;
    	}
    	
    }
    
    public void branchAndBound() {
    	this.borneSup = majorant();
    	this.borneInf = 0;
    	
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

	public void ajoutOptiHDV(){
		Batiment hdv = this.getHDV();
    	int largeur = hdv.getLarg();
    	int profondeur = hdv.getProf();

		Case best_pos=new Case(2,2);
		int best_score=0;

		for(int l=0;l<=larg/2;l++){
			for(int c=0; c<=prof/2;c++){
				if(estVide(l,c,largeur,profondeur)){ // si l'emplacement est valide

					//placement de l'HDV
					for(int x = l; x < (l + profondeur); x++) {
						for(int y = c; y < (c + largeur); y++) {
							terrain[x][y] = 1;
						}
					}

					//test du glouton
					repartitionBatiment();
					if(best_score<calculeScore()){
						best_pos.setX(l);
						best_pos.setY(c);
						best_score=calculeScore();
					}

					//vide le terrain
					for(int x=0;x<this.prof;x++){
						for(int y=0;y<this.larg;y++){
							terrain[x][y]=0;
						}
					}

					System.out.println("test de "+l+","+c);
				}
			}
		}
		System.out.println(" le meilleur score est "+best_score);

		//placement de l'HDV à la meilleur position
		for(int x = best_pos.getX(); x < (best_pos.getX() + profondeur); x++) {
			for(int y = best_pos.getY(); y < (best_pos.getY() + largeur); y++) {
				terrain[x][y] = 1;
			}
		}
	}
}
