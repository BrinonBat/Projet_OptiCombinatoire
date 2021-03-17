import java.util.Scanner;

public class Main{
    public static void main(String[] args) {

        // Saisie au clavier du test à effectuer
        Scanner sc=new Scanner(System.in);
        short test_num;
        System.out.println("entre un numero en fonction du test que vous voulez effectuer:");
        System.out.println(" 1: test de l'affichage");

        test_num=Short.parseShort(sc.nextLine());

        //effectue le test saisi
        switch(test_num){
            case 1:{//test de l'affichage
                System.out.println(" test de l'affichage \nAffichage erreur :");
        
                Terrain error_test=new Terrain(4,4);
                error_test.getliBat().add(new Batiment(0,0,3,2));
                error_test.getliBat().add(new Batiment(3,2,1,1));
                error_test.getliBat().add(new Batiment(2,2,2,2));
                error_test.afficher();
        
                System.out.println("Affichage terrain correct :");
        
                Terrain test=new Terrain(4,4);
                test.getliBat().add(new Batiment(0,0,3,2));
                test.getliBat().add(new Batiment(0,3,1,1));
                test.getliBat().add(new Batiment(2,2,2,2));
                test.afficher();

                break;
            }
            default : {
                // lancement classique du programme
            }
        }
        sc.close();
        
    }

}