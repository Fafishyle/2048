/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modele;

/**
 *
 * @author etine
 */

public class Case {
    
    
    private int valeur;
    private Jeu jeu;

    
    public Case(int _valeur, Jeu _jeu) {
        valeur = _valeur;
        jeu = _jeu;
    }

    public int getValeur() {
        return valeur;
    }
    
    //Déplacement d'une case
    
    public void move(Direction d){
        Case a_cote; 
        do{
            a_cote = jeu.getCase(this, d); //on récupère la case à côté avec laquelle on va comparer this 
           
            //s'il n'y a aucune case à côté, on déplace this à côté
            if(a_cote==null){
                System.out.println("Case.move:: la case d'à côté est nulle");
                jeu.moveCase(this,d);
                Jeu.setAction(true);

            //si la case d'à côté a la même valeur que this
            }else if(a_cote.getValeur()==this.valeur){
                System.out.println("Case.move:: La case d'à côté : "+a_cote.getValeur());
                {
                    //on vérifie si la case d'à côté n'a pas déjà été fusionnée
                    if(jeu.deja_fusionne.search(a_cote) > -1)
                    {
                        break;
                    }
                    //si ce n'est pas le cas, on fusionne
                    else if( jeu.deja_fusionne.search(this) <= -1 )
                    {
                        //fusion
                        jeu.deleteCase(a_cote); //on supprime la case d'à côté
                        this.valeur=this.valeur*2; //on double this
                        jeu.moveCase(this, d); //et on déplace this pour la mettre à la place 
                        //on informe que la case a déjà été fusionnée et ne pourra plus l'être dans le même coup
                        jeu.deja_fusionne.push(this); 
                        Jeu.setAction(true);
                        //on teste si cette fusion a permis de gagner le jeu 
                        if(this.valeur==2048)
                        {
                            jeu.endGame(true);
                            System.out.println(("2048XXXXX"));
                        }
                    }
                    else
                    {
                        break;
                    }
                }
            //si on a une autre valeur ou une bordure (puisque -1 forcément différent de la valeur de this) 
            //on sort de la boucle    
            }else if(a_cote.getValeur()!=this.valeur){
                System.out.println("Case.move:: La case d'à côté : "+a_cote.getValeur());
                break;
            }
           
        }while(a_cote==null || a_cote.getValeur()==this.valeur); 
        //on répète l'action jusqu'à ne plus pouvoir se déplacer / fusionner
    }
    
}
