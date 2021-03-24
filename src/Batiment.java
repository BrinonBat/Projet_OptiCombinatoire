public class Batiment {
    private int x,y,larg,prof;
    private boolean est_HDV,est_relie;

    /********constructeurs**************/

    // constructeur par défaut
    public Batiment(int larg, int prof){
        this.larg=larg;
        this.prof=prof;
        est_HDV=false;
        est_relie=false;
    }

    //constructeur HDV
    public Batiment(int larg, int prof, boolean hdv){
        this.larg=larg;
        this.prof=prof;
        est_HDV=hdv;
        est_relie=hdv;
    }

    //constructeur temporaire pour les tests, en attendant d'avoir la fonction de placement
    public Batiment(int x,int y,int larg, int prof){
        this.x=x;
        this.y=y;
        this.larg=larg;
        this.prof=prof;
        est_HDV=false;
        est_relie=false;
    }
    
    public Batiment(int x,int y,int larg, int prof, boolean hdv){
        this.x=x;
        this.y=y;
        this.larg=larg;
        this.prof=prof;
        est_HDV=hdv;
        est_relie=false;
    }

    /************méthodes***************/

    //return true si la case située en x,y contient le mur
    public boolean estSur(int x, int y){
        if((this.x<=x && x<=(this.x+larg-1)) && this.y<=y && y<=(this.y+prof-1)) return true;
        return false;
    }

    /***********accesseurs**************/

    //getters
    public int getX(){return x;}
    public int getY(){return y;}
    public int getLarg(){return larg;}
    public int getProf(){return prof;}
    public boolean estHDV(){return est_HDV;}
    public boolean estRelie(){return est_relie;}

    //setters
    public void deplacer(int new_x, int new_y){
        this.x=new_x;
        this.y=new_y;
    }
    public void setRelie(boolean relie){this.est_relie=relie;}


}
