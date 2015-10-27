package utilities;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;

/**
 * Created by witmi on 26/10/2015.
 */
public class FormVerifier extends InputVerifier{
    //Input validation regex
    private final static String doubleRegex = "^[0-9]+(\\.[0-9]{1,2})?$";
    private final static String intRegex    = "\\d+";
    private final String regex;


    /**
     * Constructor, takes String type as parameter to build the verifier - 'double' will verify decimal input - xx.xx,
     * otherwise it will verify integer inputs - xx
     * @param type
     */
    public FormVerifier(String type){
        regex = type.equals("double") ? doubleRegex : intRegex;

    }


    /**
     * Validates form inputs, changes input field border colour: green if input is valid, red otherwise.
     */
    @Override
    public boolean verify(JComponent input) {
        JTextField field = (JTextField) input;

        String string = field.getText();
        if(string.matches(regex)) {
            field.setBorder(new LineBorder(Color.GREEN));
            return true;
        }else{
            field.setBorder(new LineBorder(Color.RED));
            return false;
        }
    }
}
