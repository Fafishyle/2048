/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modele;

/**
 *
 * @author etine
 */

import java.io.File;
import java.io.FileWriter;
import java.io.FileNotFoundException;
import java.util.Scanner; 
import java.io.IOException;

public class LogFile {
    File log; 
    Jeu jeu; 
    
    
    LogFile(Jeu _jeu){
        jeu = _jeu; 
        createLogFile();
    }
    
    //création d'un fichier de log
    synchronized protected void createLogFile(){
        try {
            log = new File("log.txt");
            if (log.createNewFile()) {
              System.out.println("Jeu.createLogFile:: Fichier créé: " + log.getName());
            } else {
              System.out.println("Jeu.createLogFile:: Le fichier existe déjà. ");
            }
        } catch (IOException e) {
            System.out.println("Jeu.createLogFile:: Une erreur est survenue. ");
            e.printStackTrace();
        }
    }
    
    //écriture de la table en cours dans le fichier de log, sur une nouvelle ligne
    synchronized protected void writeLogFile(){
        try {
            FileWriter myWriter = new FileWriter(log, true); //File, boolean : true si on écrit à la suite du fichier existant
      
            myWriter.write("\n"); //on créé une nouvelle ligne
            
            //on transforme nos cases en string et on les ajoute au fichier
            for(int i=0; i<jeu.getSize()+2; i++){
                for(int j=0; j<jeu.getSize()+2; j++){
                    Case c = jeu.getCase(i, j);
                    int val;
                    if(c==null){
                        val = 0; 
                    } else {
                        val = c.getValeur();
                    }
                    String s = Integer.toString(val);
                    myWriter.write(s + " ");
                }
            }
            myWriter.close();
            System.out.println("Jeu.writeLogFile:: la table a été ajouté au fichier de log. ");
        } catch (IOException e) {
            System.out.println("Jeu.writeLogFile:: Une erreur est survenue. ");
            e.printStackTrace();
        }
    }
    
    //suppression du fichier
    synchronized protected void deleteLogFile(){
        if (log.delete()) { 
          System.out.println("Jeu.deleteLogFile:: Le fichier de log a été supprimé. ");
        } else {
          System.out.println("Jeu.deleteLogFile:: Une erreur est survenue : le fichier de log n'a pas été supprimé. ");
        } 
    }
    
    //Lire la ligne correspondant au coup précédent 
    synchronized protected String readLastLineLogFile(){
        String last_line = "";
        String before_last_line = "";
        try {
            Scanner myReader = new Scanner(log); 
            while (myReader.hasNextLine()) {
                before_last_line = last_line;
               last_line = myReader.nextLine();
            }
            System.out.println("LogFile.readLastLineLogFile:: la dernière ligne du fichier a été lue. ");
            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("LogFile.readLastLineLogFile:: Une erreur est survenue. ");
            e.printStackTrace();
        }
        return before_last_line;

    }

    //idee faire une liste
}
