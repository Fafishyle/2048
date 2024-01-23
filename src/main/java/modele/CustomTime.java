package modele;
import java.util.TimerTask;


public class CustomTime extends TimerTask {
    private int timeSecond = 0;
    Jeu jeu;

    CustomTime(Jeu _jeu)
    {
        timeSecond = 0;
        jeu= _jeu;
    }

    @Override
    public void run() {
        System.out.println(
                "temps = "+
                        jeu.gameTime.get("heure") + "h " +
                        jeu.gameTime.get("minute") + "m " +
                        jeu.gameTime.get("seconde") + "s " ) ;
        timeSecond++;
        jeu.gameTime.put("seconde", timeSecond % 60);
        jeu.gameTime.put("minute", timeSecond / 60);
        jeu.gameTime.put("heure", timeSecond / 3600);
    }
}
