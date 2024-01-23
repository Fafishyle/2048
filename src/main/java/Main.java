/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author etine
 */
import modele.Jeu;
import vue_controleur.Console2048;
import vue_controleur.Swing2048;

public class Main {

    public static void main(String[] args) {
        //mainConsole();
        
        //Pour ce projet, on s'est concentrée sur la vue swing. 
        
        mainSwing();
        
        

    }

    public static void mainConsole() {
        Jeu jeu = new Jeu(4);
        Console2048 vue = new Console2048(jeu);
        jeu.addObserver(vue);

        vue.start();

    }

    public static void mainSwing() {

        Jeu jeu = new Jeu(4);
        Swing2048 vue = new Swing2048(jeu);
        jeu.addObserver(vue);

        vue.setVisible(true);


    }



}

