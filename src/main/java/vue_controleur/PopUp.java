package vue_controleur;

import static javax.swing.JOptionPane.showMessageDialog;


public class PopUp {

    PopUp()
    {
        System.out.println("PopUp cree");
    }

    public void show(String sms)
    {
        showMessageDialog(null, sms);
    }

}
