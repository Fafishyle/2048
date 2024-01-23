/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modele;

/**
 *
 * @author etine
 */


import java.util.*;
import java.util.Observable;
import java.util.Random;
import java.util.Date;


public class Jeu extends Observable {
    
    //---------------------------------------------------------
    //                   ATTRIBUTS
    //---------------------------------------------------------

    //le tableau et la hashmap contenant les cases du tableau
    private Case[][] tabCases; 
    private HashMap<Case, Point> hmCases;
    
    //Pour générer un jeu aléatoire et différent à chaque execution. 
    private static Date date = new Date(); //pour la graine du random
    private static Random rnd = new Random(date.getTime());
    
    //true si le mouvement dans une direction donnée est possible, false sinon 
    private static boolean action = false; 
    
    //gestion du temps de jeu 
    private Timer timer;
    public HashMap <String, Integer> gameTime;
    
    //Pile des cases ayant déjà subie une fusion au tour en cours 
    public Stack  deja_fusionne;
    
    //Pile des cases ajoutées au tour actuel
    public Stack addedCase;
    
    //true si 2048 a été atteint
    public static boolean EndGagnant = false;
    
    //Fichier de log
    private static LogFile log; 

    
    //-----------------------------------------------------------
    //              INITIALISATION DU JEU 
    //-----------------------------------------------------------
    
    public Jeu(int size) {
        tabCases = new Case[size+2][size+2];
        hmCases = new HashMap<Case, Point>();
        deja_fusionne = new Stack();
        addedCase = new Stack();
        timer = new Timer();
        gameTime= new HashMap<String, Integer>();
        gameTime.put("heure",0);
        gameTime.put("minute",0);
        gameTime.put("seconde",0);
        init();
    }
    
    
    //---------------------------------------------------------
    //     Retourne la taille d'un coté de la grille
    //---------------------------------------------------------
   
    public int getSize(){ return tabCases.length-2;}

    
    //---------------------------------------------------------
    // deux fonctions getCase() 
    //  1. prends la position dans le tableau avec deux entiers
    //     et renvoie la case correspondante
    //--------------------------------------------------------- 
    
    synchronized public Case getCase(int i, int j) {
        return tabCases[i][j];
    }

    
    //---------------------------------------------------------
    // 2. prends une case et une direction, et renvoie la case
    //     a côté de la case passée en param dans la direction 
    //     correspondante
    //---------------------------------------------------------
    
    synchronized public Case getCase(Case c, Direction d) {
        switch(d){
            case haut: return tabCases[hmCases.get(c).getPositionX()-1][hmCases.get(c).getPositionY()];
            case bas: return tabCases[hmCases.get(c).getPositionX()+1][hmCases.get(c).getPositionY()];
            case gauche: return tabCases[hmCases.get(c).getPositionX()][hmCases.get(c).getPositionY()-1];
            case droite: return tabCases[hmCases.get(c).getPositionX()][hmCases.get(c).getPositionY()+1];
        }
        return new Case(-1,this);
    }
    
    
    //---------------------------------------------------------
    //    Supression d'une case (en cas de fusion)
    //---------------------------------------------------------

    synchronized public void deleteCase(Case c){
        tabCases[hmCases.get(c).getPositionX()][hmCases.get(c).getPositionY()]=null;
        hmCases.remove(c);
    }
         
    
    //---------------------------------------------------------
    // modifier la valeur d'action (appeler par case s'il y a 
    // eu un mouvement) 
    //---------------------------------------------------------
    
    synchronized public static void setAction(boolean b){
        action = b; 
    }
    
    
    //---------------------------------------------------------
    //      prend une case et une direction en paramètre
    //      et modifie sa position pour déplacer la case 
    //      à la bonne position. 
    //      Cette fonction est appelée par la classe Case
    //      C'est dans cette classe qu'est géré la gestion 
    //      de si une case doit se déplacer, fusionner, ou 
    //      ne rien faire. 
    //---------------------------------------------------------
    
    synchronized public void moveCase(Case c, Direction d){
        Point last_position= new Point(hmCases.get(c).getPositionX(), hmCases.get(c).getPositionY());
        Point new_position= new Point(-1,-1);
        // On définie la nouvelle position en fonction de la direction vers laquelle bouge la case
        switch(d){
            case haut: new_position.setPosition(last_position.getPositionX()-1,last_position.getPositionY());
            break;
            case bas: new_position.setPosition(hmCases.get(c).getPositionX()+1,hmCases.get(c).getPositionY());
            break;
            case gauche: new_position.setPosition(hmCases.get(c).getPositionX(),hmCases.get(c).getPositionY()-1);
            break;
            case droite: new_position.setPosition(hmCases.get(c).getPositionX(),hmCases.get(c).getPositionY()+1);
            break;
        }
        //modification de la position dans la hashmap
        hmCases.put(c, new_position);
        //mise à jour de la position dans le tableau
        tabCases[last_position.getPositionX()][last_position.getPositionY()]=null;
        tabCases[new_position.getPositionX()][new_position.getPositionY()]=c;
    }
    
    
    //---------------------------------------------------------
    //      Prends une direction en paramètre et génère 
    //      la nouvelle table en fonction de la direction
    //---------------------------------------------------------
    
    synchronized public void moveJeu(Direction d) {
        new Thread(){ //permet de libérer le processus graphique
            @Override
            public void run(){
                // nouveau tour : on re initialise nos variables s'appliquant à un seul tour 
                action=false; //est mis à true dans Case::move lorsqu'une case a effectué une action (bouger ou fusionner)
                addedCase.clear();
                deja_fusionne.clear();
                if (d == Direction.gauche || d == Direction.haut) {
                    //on move toutes les casess du tableau sauf les cases de bordure
                    //on commence en haut à gauche
                    for (int i = 1; i < tabCases.length - 1; i++) {
                        for (int j = 1; j < tabCases.length - 1; j++) {
                            System.out.println("moveJeu:: on déplace la case " + i + "," + j);
                            //si la case n'est pas nulle, on regarde si elle peut faire une action
                            if (tabCases[i][j] != null) {
                                tabCases[i][j].move(d); 
                            }
                        }
                    }
                }
        
                if (d == Direction.droite || d == Direction.bas) {
                    //on move toutes les casess du tableau sauf les cases de bordure
                    //on commence en bas à droite 
                    for (int i = tabCases.length - 2; i > 0; i--) {
                        for (int j = tabCases.length - 2; j > 0; j--) {
                            System.out.println("moveJeu:: on déplace la case " + i + "," + j);
                            //si la case n'est pas nulle, on regarde si elle peut faire une action
                            if (tabCases[i][j] != null) {
                                tabCases[i][j].move(d);
                            }
                        }
                    }
                }
                System.out.println("action::" +action);
                        
                //Si une action a été faite, le coup a été joué, on ajoute une nouvelle case 
                if(action){
                    ajoutCase();
                }
                //on ajoute la nouvelle table au log 
                log.writeLogFile();
                //on test si le jeu est terminé
                testEndGame();
                
                //on informe le thread d'affichage qu'il peut afficher
                setChanged();
                System.out.println("Notify 4");
                notifyObservers();
            }
        }.start();
        
        
        
    }
    
    
    //---------------------------------------------------------
    //   renvoie le nombre de cases non nulle (en retirant les 
    //   bordures à -1) 
    //---------------------------------------------------------
    
    public int numberCurentCase()
    {
        return hmCases.size()-(4*getSize()) -4 ;
    }
    
    
    //---------------------------------------------------------
    //       On test sitoutes les cases du tableau sont remplies
    //       et aucun mouvement n'est possible
    //---------------------------------------------------------

    public boolean testEndGame()
    {
        int nb_cases_totale = getSize()*getSize();
        //Si toutes les cases sont remplies
        if ( numberCurentCase() ==nb_cases_totale)
        {
            boolean action_possible = false;
            for(Direction d : Direction.values())
            {
                action_possible = action_possible || test_action(d);
            }
            if(!action_possible)
            {
               endGame(false); //on appelle la fonction de fin de jeu, bool false car perdant
                return true ;
            }
        }
        return false;
    }
    
    //---------------------------------------------------------
    //    On regarde si la case d'à côté a déjà été fusionnée
    //    pour voir si on peut fusionner avec ou pas
    //---------------------------------------------------------

    public boolean test_action(Direction d)
    {
        if (d == Direction.gauche || d == Direction.haut)
        {
            for (int i = 1; i < tabCases.length - 1; i++) {
                for (int j = 1; j < tabCases.length - 1; j++) {
                    System.out.println("testEndGame:: on teste la fusion de la case " + i + "," + j);
                    Case a_cote= getCase(tabCases[i][j],d );
                    if(a_cote.getValeur()==tabCases[i][j].getValeur())
                    {
                        return true;
                    }
                }
            }
        }

        if (d == Direction.droite || d == Direction.bas) {
            for (int i = tabCases.length - 2; i > 0; i--) {
                for (int j = tabCases.length - 2; j > 0; j--) {
                    System.out.println("testEndGame:: on teste la fusion de la case " + i + "," + j);
                    Case a_cote= getCase(tabCases[i][j],d );
                    if(a_cote.getValeur()==tabCases[i][j].getValeur())
                    {
                        return true;
                    }
                }
            }
        }
        return false;
    }


    //---------------------------------------------------------
    //   Ajoute une nouvelle case
    //---------------------------------------------------------

    synchronized public void ajoutCase()
    {
        //creation nouvelle case
        int nb_Cases_null = tabCases.length*tabCases.length - hmCases.size();
        int compteur = 0; //va parcourir le tableau
        int position = rnd.nextInt(nb_Cases_null);

        int valeur = rnd.nextInt(2);

        for (int i = 1; i < tabCases.length-1 ; i++) {
            for (int j = 1; j < tabCases.length -1; j++) {
                if (tabCases[i][j] == null) {
                    if (position == compteur) {
                        switch (valeur) {
                            case 0:
                                tabCases[i][j] = new Case(2048,this);
                                hmCases.put(tabCases[i][j], new Point(i,j));
                                addedCase.push(tabCases[i][j]);
                                break;
                            case 1:
                                tabCases[i][j] = new Case(4,this);
                                hmCases.put(tabCases[i][j], new Point(i,j));
                                addedCase.push(tabCases[i][j]);
                                break;
                        }
                    }
                    compteur++;
                }
            }
        }
    }
    
    
    //---------------------------------------------------------
    //   Si le jeu est terminé, on appelle cette fonction
    //   prend un boolean en paramètre pour savoir si le jeu
    //   est gagné ou perdu 
    //---------------------------------------------------------
    
    protected void endGame(boolean gagnante){
        timer.cancel();
        if(gagnante)
        {
            EndGagnant = true;
            System.out.println("endGame:: Vous avez obtenue 2048, vous avez gagnez.");
        }
        else{
            EndGagnant = false;
            System.out.println("endGame:: Plus aucune action possible, vous avez perdue.");
        }
        log.deleteLogFile();   
    }
   

    //---------------------------------------------------------
    //      Fonction appelée dans le constructeur de jeu
    //      Pour initialiser la partie 
    //---------------------------------------------------------

    synchronized public void init() { //processus métier
        Jeu this_jeu = this; 
        new Thread() { // permet de libérer le processus graphique ou de la console
            public void run() {
                //initialisation des cases en bordure à -1    
                for(int i=0; i<this_jeu.getSize()+2; i++){
                    tabCases[0][i] = new Case(-1,this_jeu);     
                    hmCases.put(tabCases[0][i], new Point(0,i));
                    tabCases[this_jeu.getSize()+1][i] = new Case(-1,this_jeu);     
                    hmCases.put(tabCases[this_jeu.getSize()+1][i], new Point(this_jeu.getSize()+1,i));
                    if(i<this_jeu.getSize()){
                       tabCases[i+1][0] = new Case(-1,this_jeu);     
                       hmCases.put(tabCases[i+1][0], new Point(i+1,0));
                       tabCases[i+1][this_jeu.getSize()+1] = new Case(-1,this_jeu);     
                       hmCases.put(tabCases[i+1][this_jeu.getSize()+1], new Point(i+1,this_jeu.getSize()+1));
                    }
                }
                //on ajoute les deux cases de départ
                for(int i=0; i<2; i++){
                    ajoutCase();
                }

                //demarrage de chronometre
                System.out.println("rnd():: Demarrage du chronometre");
                CustomTime t = new CustomTime(this_jeu);
                timer.schedule(t , 1000,1000);
                
                //création du fichier de log et ajout de l'initialisation
                log = new LogFile(this_jeu);
                log.writeLogFile();
                
                //notifie l'affichage qu'il peut afficher
                setChanged();
                notifyObservers(); 
            }
        }.start();
    }
    
    
    //---------------------------------------------------------
    //  Permet de revenir au coup précédent en récupérant la 
    //  la table dans le fichier de log
    //--------------------------------------------------------- 
    
    synchronized public void coupPrecedent(){
        Jeu this_jeu = this;
        new Thread(){
            public void run(){
                 hmCases.clear();
                String str = log.readLastLineLogFile();
                String[] str_val = str.split(" ");
                int val;
                int i=0, j=0; 
                for(int n=0; n<str_val.length; n++){
                    val=Integer.parseInt(str_val[n]);
                    if(val==0){
                        tabCases[i][j]=null;
                    }else{
                        Case c = new Case(val, this_jeu);
                        tabCases[i][j]=c;
                        hmCases.put(c, new Point(i,j)); 
                    }
                    j++;
                    if(j>=6){
                        j=0;
                        i++;
                    }
                }
                setChanged();
                notifyObservers();
            }     
        }.start();         
    }
}
