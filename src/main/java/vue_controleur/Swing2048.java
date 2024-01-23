package vue_controleur;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author etine
 */


import modele.Case;
import modele.Jeu;
import modele.Direction;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.Color;
//import java.awt.Font; //voir si on rajoute une font oupa (dépendra du temps) 
import java.awt.event.*;
import java.util.Observable;
import java.util.Observer;

import static javax.swing.JOptionPane.showMessageDialog;

//FINIR DE COMMENTER 

public class Swing2048 extends JFrame implements Observer {
    private static final int PIXEL_PER_SQUARE = 70;
    // tableau de cases : i, j -> case graphique
    private JLabel[][] tabC;
    private JLabel Time;
    private Jeu jeu;
    private PopUp popup; //pour le message de fin 



    public Swing2048(Jeu _jeu) {
        
        popup= new PopUp();
        jeu = _jeu;
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //pour fermer la fenetre
        setSize(jeu.getSize() * PIXEL_PER_SQUARE, jeu.getSize() * PIXEL_PER_SQUARE); //taille de la fenêtre
        tabC = new JLabel[jeu.getSize()][jeu.getSize()];
        Time = new JLabel();
        JPanel contentPane = new JPanel(new GridLayout(jeu.getSize(), jeu.getSize()));
        JPanel time_panel =new JPanel();




        //Affichage Time
        Border border = BorderFactory.createLineBorder(Color.BLUE, 5);
        Time.setHorizontalAlignment(SwingConstants.RIGHT);
        Time.setForeground(Color.BLACK);
        time_panel.add(Time);
        Time.setFont(new Font(Time.getFont().getName(), Time.getFont().getStyle(), 15));

        //Affichage grille 2048
        contentPane.setPreferredSize(new Dimension(200, 200));
        for (int i = 0; i < jeu.getSize(); i++) {
            for (int j = 0; j < jeu.getSize(); j++) {
                border = BorderFactory.createLineBorder(Color.darkGray, 5, true);
                tabC[i][j] = new JLabel();
                tabC[i][j].setBorder(border);
                tabC[i][j].setHorizontalAlignment(SwingConstants.CENTER);
                tabC[i][j].setOpaque(true);
                contentPane.add(tabC[i][j]);
            }
        }

        //on cree le JPanel principal
        time_panel.setLayout(new FlowLayout(FlowLayout.CENTER, 100, 5));

        //on y ajoute l'autre JPanel contentPane (la grille 2048)
        time_panel.add(contentPane);

        //mettre Le JPanel time_panel à la fenetre Jframe
        setContentPane(time_panel);
        ajouterEcouteurClavier();
        rafraichir();

    }



    /**
     * Correspond à la fonctionnalité de Vue : affiche les données du modèle
     */
    private void rafraichir()  {

        SwingUtilities.invokeLater(new Runnable() { // demande au processus graphique de réaliser le traitement
            @Override
            public void run() {
                for (int i = 0; i < jeu.getSize(); i++) {
                    for (int j = 0; j < jeu.getSize(); j++) {
                        Case c = jeu.getCase(i+1, j+1);

                        if (c == null) {

                            tabC[i][j].setText("");
                            tabC[i][j].setBackground(new Color(245, 245, 245));

                        } else {
                            //voir pour mettre tout ça dans une fonction
                            tabC[i][j].setText(c.getValeur() + "");
                            switch(c.getValeur()){
                                case 2 : tabC[i][j].setBackground(new Color(238, 237, 74)); break;
                                case 4 : tabC[i][j].setBackground(new Color(237, 224, 200)); break;
                                case 8 : tabC[i][j].setBackground(new Color(242, 177, 121)); break; 
                                case 16 : tabC[i][j].setBackground(new Color(245, 149, 99)); break;
                                case 32 : tabC[i][j].setBackground(new Color(246, 124, 85)); break; 
                                case 64 : tabC[i][j].setBackground(new Color(246, 84, 59)); break;
                                case 128 : tabC[i][j].setBackground(new Color(237, 207, 114)); break;
                                case 256 : tabC[i][j].setBackground(new Color(237, 208, 97)); break;
                                case 512 : tabC[i][j].setBackground(new Color(237, 204, 70)); break;
                                case 1024 : tabC[i][j].setBackground(new Color(237, 197, 63)); break;
                                case 2048 : tabC[i][j].setBackground(new Color(237, 194, 46)); break;
                            }


                            //apparition de nouvelle case
                            if (jeu.addedCase.search(c) > -1) //on regarde si la case actuelle est la nouvelle case
                             {
                                 JLabel ajout = tabC[i][j];

                                 //Ne marche marche, degradé de couleur pour les nouvelles cases
                                 /*for(int k=0; k<251; k= k+50)
                                {

                                    ajout.setBackground(new Color(0, 0+k, 0));
                                    ajout.repaint(); //ça fait quoi ? 
                                    ajout.setVisible(true);
                                    try {
                                        Thread.sleep(100);
                                        System.out.println("sleep " + k);
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }

                                }
                                */
                                 ajout.setBackground(new Color(5, 253, 233));

                             }

                        }
                    }
                }
                //Affichage Time
                Time.setText("Temps : " +
                        Integer.toString(jeu.gameTime.get("heure")) + "h " +
                        Integer.toString(jeu.gameTime.get("minute"))+ "m " +
                        Integer.toString(jeu.gameTime.get("seconde"))+ "s "
                        );
            }
            
        });
        System.out.println("Notify 3");

    }


    /**
     * Correspond à la fonctionnalité de Contrôleur : écoute les évènements, et déclenche des traitements sur le modèle
     */
    private void ajouterEcouteurClavier() {
        addKeyListener(new KeyAdapter() { // new KeyAdapter() { ... } est une instance de classe anonyme, il s'agit d'un objet qui correspond au controleur dans MVC
            @Override
            public void keyPressed(KeyEvent e) {
                switch(e.getKeyCode()) {  // on regarde quelle touche a été pressée
			//à modifier par les fonctions de décalage
                    case KeyEvent.VK_LEFT : jeu.moveJeu(Direction.gauche); break;
                    case KeyEvent.VK_RIGHT : jeu.moveJeu(Direction.droite); break;
                    case KeyEvent.VK_DOWN : jeu.moveJeu(Direction.bas); break;
                    case KeyEvent.VK_UP : jeu.moveJeu(Direction.haut); break;
                    case KeyEvent.VK_BACK_SPACE: jeu.coupPrecedent(); break;
                }
                if(jeu.testEndGame() || jeu.EndGagnant)
                {
                    System.out.println("ICIIIIIIXXX");
                    if(!Jeu.EndGagnant)
                    {
                        popup.show(" Plus aucune action possible, vous avez perdue ");
                    }
                    else
                    {
                        popup.show(" Vous avez obtenue 2048! vous avez gagnez! ");
                    }
                    
                    System.exit(0);

                }
            }
        });
    }
    
     /*   
   public void animationGlisseCase(Case c, Direction d){
       int i_depart = jeu.hmCases.get(c).getPositionX(); //position i dans le tableau de départ
       int j_depart = jeu.hmCases.get(c).getPositionY(); //position j dans le tableau de départ
       
       float x_depart = j_depart*PIXEL_PER_SQUARE; // coordonné x de départ 
       float y_depart = i_depart*PIXEL_PER_SQUARE; // coordonné y de départ 
       
       float x_arrive; // coordonné x d'arrivé
       float y_arrive; // coordonné y d'arrivé 
       switch(d){
           case haut :
               for(int i=0; i<10; i++){
                    float pas = PIXEL_PER_SQUARE/10*i;
                    tabC[i_depart][j_depart].setAlignmentY(y_depart-pas);
                    
                    Thread.sleep(100);
                }
                break;
           case bas : 
                for(int i=0; i<10; i++){
                    float pas = PIXEL_PER_SQUARE/10*i;
                    tabC[i_depart][j_depart].setAlignmentY(y_depart+pas);
                }
                break;
           case gauche : 
                for(int i=0; i<10; i++){
                    float pas = PIXEL_PER_SQUARE/10*i;
                    tabC[i_depart][j_depart].setAlignmentX(x_depart-pas);
                }
                break;
           case droite : 
                for(int i=0; i<10; i++){
                    float pas = PIXEL_PER_SQUARE/10*i;
                    tabC[i_depart][j_depart].setAlignmentX(x_depart+pas);
                }
                break;
       }
   
       
   }
   */

    @Override
    public void update(Observable o, Object arg) {
        System.out.println("Notify 2");
        rafraichir();
    }
}
