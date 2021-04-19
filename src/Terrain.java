import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Deque;
import java.util.Random;

class SortByCoeff implements Comparator<Batiment>
{
    //utilisé pour le tri descendant par coef
    public int compare(Batiment a, Batiment b){
		float coefA=(float)a.getAire()/(float)a.getEncombrement();
		float coefB=(float)b.getAire()/(float)b.getEncombrement();
        if(coefA<coefB) return 1;
		if(coefB<coefA) return -1;
		return 0;
    }
}

class AireSortPerso implements Comparator<Batiment>
{
    //utilisé pour le tri descendant par coef
    public int compare(Batiment a, Batiment b){
        if(a.getAire()>b.getAire()) return -1;
		if(b.getAire()>a.getAire()) return 1;
		//si aires égales, on place d'abord celui qui a le plus petit encombrement
		return (new SortByCoeff()).compare(a,b);
    }
}

public class Terrain {
    private ArrayList<Batiment> li_bat;
    private int larg,prof;
    private int terrain[][];
    private int borneSup;
    private int borneInf;

    //constructeur
    public Terrain(int larg, int prof){
        li_bat = new ArrayList<Batiment>();
        this.larg=larg;
        this.prof=prof;
        this.terrain = new int[larg][prof];
        for(int x = 0; x < larg; ++x) {
        	for(int y = 0; y < prof; ++y) {
        		terrain[x][y] = 0;
        	}
        }
    }

    /************méthodes***************/

    // Ajoute un batiment sur le terrain
    public void poseBatiment(Batiment b, int bx, int by) {
		b.deplacer(bx, by);
		for(int x = bx; x < (bx + b.getLarg()); ++x) {
			for(int y = by; y < (by + b.getProf()); ++y) {
				terrain[x][y] = b.getId();
			}
		}
		b.setRelie(true);
    }

	//retire un batiment du terrain
	public void retireBatiment(Batiment b){
		for(int x = b.getX(); x < (b.getX() + b.getLarg()); ++x) {
			for(int y = b.getY(); y < (b.getY() + b.getProf()); ++y) {
				terrain[x][y] = 0;
			}
		}
		b.setRelie(false);
		b.desinstaller();
	}

	//vide le terrain
	public void terrainVide(){
		for(int i=li_bat.size()-1;i>=0;i--){
			if(li_bat.get(i).estRelie()) retireBatiment(li_bat.get(i));
		}
	}
    
	//retourne le batiment présent aux coordonnées x,y
	private Batiment batA(int x,int y){
		int id=terrain[x][y];
		for (Batiment bat : li_bat) {
			if(bat.getId()==id) return bat;
		}
		return null;
	}

    // Regarde si la position du batiment est valide
    public boolean estValide(Batiment b, int bx, int by) {
    	if((bx + b.getLarg() > larg) || (by + b.getProf() > prof)) // Regarde si la taille ne dépasse pas le terrain
    		return false;
    	
    	for(int x = bx; x < (bx + b.getLarg()); ++x) {
    		for(int y = by; y < (by + b.getProf()); ++y) {
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
		int nbBat=0;
		for(int i=0;i<li_bat.size();i++){
			if(li_bat.get(i).estRelie()) nbBat++;
		}
		return nbBat;
    }
    
    // Regarde si chaque batiment présent dans le terrain est relié à l'hotel de ville
    public boolean estRelieHDV() {
    	Batiment hdv = this.getHDV();
    	int h_x = hdv.getX();
    	int h_y = hdv.getY();
    	
    	Deque<Case> pile = new ArrayDeque<Case>();
    	ArrayList<Case> parcourue = new ArrayList<Case>();
    	
    	// On parcoure les cases autour de l'hdv
    	for(int x = (h_x-1); x <= (hdv.getLarg() + h_x); ++x) {
    		for(int y = (h_y-1); y <= (hdv.getProf() + h_y); ++y) {
    			if(x >= 0 && x < this.larg && y >= 0 && y < this.prof) {
    				if(terrain[x][y] == 0) { // ajoute les cases vides qui sont autour de l'hdv
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
    		    		
    		// On regarde la case au-desssus de la case courante
    		if(x>=0 && y-1 >= 0) {
	    		if(terrain[x][y-1] == 0 ) {
	    			if(!this.estParcourue(parcourue, new Case(x,y-1))) { // Si la case est vide et est non parcourue on l'ajoute à la pile
		    			pile.push(new Case(x,y-1));
		    			parcourue.add(new Case(x,y-1));
	    			}
	    		}
	    		else { // Sinon c'est un batiment
	    			if(!batA(x,y-1).estRelie()) { // s'il est pas déjà indiqué comme relié alors on le relie
	    				batA(x,y-1).setRelie(true);
	    			}
	    			if(!estRelie.contains(terrain[x][y-1]-1))
	    				estRelie.add(terrain[x][y-1]-1);
	    		}
    		}
    		
    		// On regarde la case au-desssous de la case courante
    		if(x>=0 && y+1 < this.prof) {
	    		if(terrain[x][y+1] == 0 ) {
	    			if(!this.estParcourue(parcourue, new Case(x,y+1))) {
		    			pile.push(new Case(x,y+1));
		    			parcourue.add(new Case(x,y+1));
	    			}
	    		}
	    		else {
	    			if(!batA(x,y+1).estRelie()) {
	    				batA(x,y+1).setRelie(true);
	    			}
	    			if(!estRelie.contains(terrain[x][y+1]-1))
	    				estRelie.add(terrain[x][y+1]-1);
	    		}
    		}
    		
    		// On regarde la case à gauche de la case courante
    		if(x-1 >= 0 && y>=0) {
	    		if(terrain[x-1][y] == 0 ) {
	    			if(!this.estParcourue(parcourue, new Case(x-1,y))) {
		    			pile.push(new Case(x-1,y));
		    			parcourue.add(new Case(x-1,y));
	    			}
	    		}
	    		else {
	    			if(!batA(x-1,y).estRelie()) {
	    				batA(x-1,y).setRelie(true);
	    			}
	    			if(!estRelie.contains(terrain[x-1][y]-1))
	    				estRelie.add(terrain[x-1][y]-1);
	    		}
    		}
    		
    		// On regarde la case à droite de la case courante
    		if(x+1 < this.larg && y>=0) {
	    		if(terrain[x+1][y] == 0 ) {
	    			if(!this.estParcourue(parcourue, new Case(x+1,y))) {
		    			pile.push(new Case(x+1,y));
		    			parcourue.add(new Case(x+1,y));
	    			}
	    		}
	    		else {
	    			if(!batA(x+1,y).estRelie()) {
	    				batA(x+1,y).setRelie(true);	
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
    	for(int x = (h_x-1); x <= (hdv.getLarg() + h_x); ++x) {
    		for(int y = (h_y-1); y <= (hdv.getProf() + h_y); ++y) {
    			if(x >= 0 && x < this.larg && y >= 0 && y < this.prof) {
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
	    			if(x >= bx && x <= (bx+largeur) && (y-1) >= by && (y-1) <= (by+profondeur)) { // Si les coordonnées correspondent alors la position est relié
	    				return true;
	    			}
	    			if(!this.estParcourue(parcourue, new Case(x,y-1))) { // Si la case est vide et est non parcourue on l'ajoute à la pile
		    			pile.push(new Case(x,y-1));
		    			parcourue.add(new Case(x,y-1));
	    			}
	    		}
    		}
    		
    		// On regarde la case au-desssous de la case courante
    		if(y+1 < this.prof) {
	    		if(terrain[x][y+1] == 0 ) {
	    			if(x == bx && x <= (bx+largeur) && (y+1) == by && (y+1) <= (by+profondeur)) { // Si les coordonnées correspondent alors la position est relié
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
	    			if((x-1) == bx && (x-1) <= (bx+largeur) && y == by && y <= (by+profondeur)) { // Si les coordonnées correspondent alors la position est relié
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
	    			if((x+1) == bx && (x+1) <= (bx+largeur) && y == by && y <= (by+profondeur)) { // Si les coordonnées correspondent alors la position est relié
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
		int aire=0;
		for(int i=0;i<li_bat.size();i++){
			if(li_bat.get(i).estRelie()) aire+=li_bat.get(i).getAire();
		}
		return aire;
    }
    
/////////////////////////////////////////////////// GLOUTONS ///////////////////////////////////////////////////////////////////////////////////////////////////////////
    
    // Ajoute l'hdv aléatoirement sur un terrain vide
    public void ajoutAleatoireHDV() {   	
    	Batiment hdv = this.getHDV();
    	int largeur = hdv.getLarg();
    	int profondeur = hdv.getProf();
    	ArrayList<Case> valides = new ArrayList<Case>(); // Liste des positions valides
    	
    	for(int x = 0; x < this.larg; ++x) {
    		for(int y = 0; y < this.prof; ++y) {
    			if((x+largeur) <= this.larg && (y+profondeur) <= this.prof) {
    				valides.add(new Case(x,y)); // On ajoute toutes les positions valides
    			}
    		}
    	}
    	
    	Random rand = new Random();
    	int indice = rand.nextInt(valides.size()); // On tire une position aléatoire
    	Case pos = valides.get(indice);

		poseBatiment(hdv, pos.getX(), pos.getY());

    }

    // On parcoure chaque case du terrain
    // Si à la position courante on peut ajouter un batiment qui
    // a une position valide,
    // est relié à l'hdv,
    // et ne bloque pas de route pour un autre batiment
    // alors on l'ajoute
    public void repartitionBatiment(boolean verbose) {
    	for(int x = 0; x < larg; ++x) {
    		for(int y = 0; y < prof; ++y) {
    			if(terrain[x][y] == 0) {
    				getBatimentBienPlace(x,y,verbose);		
    			}    			
    		}
    	}
    }
   
    // Cherche le premier batiment, non placé, qui est valide à cette position et l'ajoute au terrain
    public void getBatimentBienPlace(int x, int y, boolean verbose) {
    	for(int i = 0; i < li_bat.size(); ++i) {
    		//if(!batimentDejaPlace(i+1)) {
			if(!li_bat.get(i).estRelie()){
				Batiment bat = li_bat.get(i);
				int largeur = bat.getLarg();
				int profondeur = bat.getProf();
				
				if((x+largeur) <= prof && (y+profondeur) <= larg) { // Si le batiment rentre dans le terrain
					if(estVide(x,y,largeur,profondeur)) { // Si le batiment peut-être ajouté à la position courante
						if(estRelie(x,y,largeur,profondeur)) { // Si la position est relié à l'hdv
							//System.out.println("Batiment " + (i+1) + " est placé et est relié");
							poseBatiment(bat, x, y);
							
							//this.afficherTerrain();
							if(!estRelieHDV()) { // Si tous les batiments ne sont pas relié après l'ajout alors on retire le batiment
		    					//System.out.println("Batiment " + (i+1) + " bloque une route");
								retireBatiment(bat);
		    				}
							else{
                                if(verbose) System.out.println("Batiment "+(i+1)+" placé =====> (aire:"+li_bat.get(i).getAire()+" , encombrement:"+li_bat.get(i).getEncombrement()+")");
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
    	for(int a = x; a < (x+largeur); ++a) {
			for(int b = y; b < (y+profondeur); ++b) {
				if(terrain[a][b] != 0)
					return false;
			}
		}
    	return true;
    }


    // Applique l'algorithme glouton
    public void glouton(boolean opti_hdv, boolean verbose) {
		if(opti_hdv){
			System.out.println("placement optimisé !");
			ajoutOptiHDV();
		}
		else ajoutAleatoireHDV();
    	repartitionBatiment(verbose);
    }
    
    // Applique l'algorithme glouton avec affichage du 
    public void affichageResultat() {
    	System.out.println("====== Résultat ======");
    	afficherTerrain();
		int score=calculeScore();
		int aire_max=prof*larg;
    	System.out.println("Score : " + score +"/"+aire_max+" ("+ (float)(score*100.0)/(float)aire_max+"%)");
    }


    // Quicksort basé sur la valeur d'encombrement de chaque batiment
    public static void quicksortEncombrement(ArrayList<Batiment> tableau, int début, int fin) {
        if (début < fin) {
            int indicePivot = partitionEncombrement(tableau, début, fin);
            quicksortEncombrement(tableau, début, indicePivot-1);
            quicksortEncombrement(tableau, indicePivot+1, fin);
        }
    }
    
    // Trie la liste en fonction de l'encombrement le plus haut
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

    // Glouton placant en priorité les batiments à plus grand encombrement
    public void gloutonEncombrement(boolean opti_hdv, boolean verbose){
        quicksortEncombrement(li_bat,0,li_bat.size()-1);
        glouton(opti_hdv,verbose);
    }

    // Quicksort basé sur l'aire de chaque batiment
    public static void quicksortAire(ArrayList<Batiment> tableau, int début, int fin) {
        if (début < fin) {
            int indicePivot = partitionAire(tableau, début, fin);
            quicksortAire(tableau, début, indicePivot-1);
            quicksortAire(tableau, indicePivot+1, fin);
        }
    }
    
    // Trie la liste en fonction de l'aire la plus grande
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

    // Glouton placant en priorité les batiments qui ont les plus grandes aires
    public void gloutonAire(boolean opti_hdv,boolean verbose){
        quicksortAire(li_bat, 0 , li_bat.size()-1);
        glouton(opti_hdv,verbose);
    }

    // Glouton placant les batiments dans un ordre aléatoire
    public void gloutonAléatoire(boolean opti_hdv,boolean verbose){
        Collections.shuffle(li_bat);
    	glouton(opti_hdv,verbose);
    }

    // Glouton plaçant en priorité les batiments qui ont les plus grandes aires
    // en cas d'égalité on prend l'encombrement le plus grand
    public void gloutonPerso(boolean opti_hdv,boolean verbose){
		Collections.sort(li_bat,new SortByCoeff());
		for (int i = 0; i < li_bat.size(); i++) {
			if(verbose)System.out.println(" batiment "+i+" d'id "+li_bat.get(i).getId()+" a le coeff "+((float)li_bat.get(i).getAire()/(float)li_bat.get(i).getEncombrement()));
		}
		glouton(opti_hdv,verbose);
    }

    // Glouton plaçant en priorité les batiments ayant le ratio aire/encombrement le plus haut
	public void gloutonAireEtEncombrement(boolean opti_hdv,boolean verbose){
		Collections.sort(li_bat,new AireSortPerso());
		for (int i = 0; i < li_bat.size(); i++) {
			if(verbose)System.out.println(" batiment "+i+" d'id "+li_bat.get(i).getId()+"et d'aire "+li_bat.get(i).getAire()+" a le coeff "+((float)li_bat.get(i).getAire()/(float)li_bat.get(i).getEncombrement()));
		}
		glouton(opti_hdv,verbose);
    }
    
	// Retourne le nombre de case vide d'un terrain
    public int nbCaseVide(int plateau[][]) {
    	int nb = 0;
    	for(int x = 0; x < larg; ++x) {
        	for(int y = 0; y < prof; ++y) {
        		if(plateau[x][y] == 0) {
        			++nb;
        		}
        	}
        }
    	return nb;
    }
    
/////////////////////////////////////////////////// BRANCH & BOUND /////////////////////////////////////////////////////////////////////////////////////////////////////////

	
    // Calcul le score du terrain en faisant la somme des aires des batiments
    public int calculeScore(int plateau[][], ArrayList<Batiment> bat) {
    	ArrayList<Batiment> batiments = new ArrayList<>();
    	ArrayList<Integer> batiments_visite = new ArrayList<>();
    	for(int x = 0; x < larg; ++x) {
        	for(int y = 0; y < prof; ++y) {
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
    
    // Cherche la position optimale de l'hdv
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
					poseBatiment(hdv, l, c);

					//test du glouton
					repartitionBatiment(false);
					if(best_score<calculeScore()){
						best_pos.setX(l);
						best_pos.setY(c);
						best_score=calculeScore();
					}

					//vide le terrain
					terrainVide();

				}
			}
		}
		System.out.println(" le meilleur score est "+best_score);
		System.out.println(" meilleur position : " + best_pos.getX() + "," + best_pos.getY());

		//placement de l'HDV à la meilleur position
		poseBatiment(hdv, best_pos.getX(), best_pos.getY());
	}

    // Fait tourner l'algo gloutonAire et gloutonEncombrement pour récupérer une solution partielle et le majorant
    public int majorant(ArrayList<Batiment> major_list) {
    	ArrayList<Batiment> memorise_li_bat = this.li_bat;
    	int memorise_terrain[][] = this.terrain;
    	gloutonEncombrement(true,false);
    	int terrain_encombrement[][] = this.terrain;
    	ArrayList<Batiment> li_bat_encombrement = this.li_bat;
    	this.li_bat = memorise_li_bat;
    	this.terrain = memorise_terrain;
    	gloutonAire(true,false);
    	int terrain_aire[][] = this.terrain;
    	ArrayList<Batiment> li_bat_aire = this.li_bat;
    	this.li_bat = memorise_li_bat;
    	this.terrain = memorise_terrain;
    	
    	int score_encombrement = calculeScore(terrain_encombrement, li_bat_encombrement);
    	int score_aire = calculeScore(terrain_aire, li_bat_aire);
    	
    	if(score_aire <= score_encombrement) {
			major_list=li_bat_aire; // solution partielle
    		return score_aire;
    	}
    	else {
			major_list=li_bat_encombrement; //solution partielle
    		return score_encombrement;
    	}
    	
    }
    
    
    // Lance l'algorithme du branch and bound !! Pas fini !!
    public void branchAndBound() {
		ArrayList<Batiment> result_majorant=new ArrayList<Batiment>();
    	this.borneSup = majorant(result_majorant);
    	this.borneInf = 0;
    	
    	this.ajoutOptiHDV();
    	
    	for(int x = 0; x < larg; ++x) {
        	for(int y = 0; y < prof; ++y) {
        		if(terrain[x][y] == 0) {  // On regarde si la case est vide
        			int max_aire = 0;
        			
        			for(int i = 0; i < li_bat.size(); ++i) {
        				Batiment bat = li_bat.get(i);
        				if(!bat.estRelie()) { // Regarde si le batiment n'est pas déjà placé
	        				int aire = bat.getLarg()*bat.getProf();
	        				
	        				if(aire > max_aire && estValide(bat, x, y) && estRelie(x, y, bat.getLarg(), bat.getProf())) {
	        					max_aire = aire;
								poseBatiment(bat, x, y);
	        					if(!estRelieHDV()) {
									retireBatiment(bat);
	        					}
	        				}
        				}
        			}
        			
        			int score = calculeScore(terrain, li_bat);
        			if(score > borneSup)
        				borneSup = score;
        			if(score > borneInf)
        				borneInf = score;
        		}
        	}
        }
    	
    }

    /***********accesseurs**************/

    // ils serviront pas forcément mais je les ajoutes pour l'instant, au cas où

    //getter
    public int getLarg(){return larg;}
    public int getProf(){return prof;}
    public ArrayList<Batiment> getliBat(){return li_bat;}
    public int[][] getTerrain() {return this.terrain;}
    // retourne l'hotel de ville
    public Batiment getHDV() {
    	for(int i = 0; i < li_bat.size(); ++i) {
    		if(li_bat.get(i).estHDV())
    			return li_bat.get(i);
    	}
    	return null;
    }
}
