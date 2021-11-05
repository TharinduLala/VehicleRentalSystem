package utill;

import com.jfoenix.controls.JFXButton;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.regex.Pattern;

public class ValidationUtil {

    public static Object validate(LinkedHashMap<TextField, Pattern> map,ArrayList<JFXButton>btnList) {
        for (JFXButton btn:btnList
             ) {
            btn.setDisable(true);
        }
        for (TextField textFieldKey : map.keySet()) {
            Pattern patternValue = map.get(textFieldKey);
            if (!patternValue.matcher(textFieldKey.getText()).matches()) {
                if (!textFieldKey.getText().isEmpty()) {
                    textFieldKey.setStyle("-fx-border-color: red");
                }
                return textFieldKey;
            }
            textFieldKey.setStyle("-fx-border-color: lime");
        }
        for (JFXButton btn:btnList
        ) {
            btn.setDisable(false);
        }
        return null;

    }

    public boolean checkIsFieldsEmpty(ArrayList<TextField> fields){
        for (TextField textField:fields
             ) {
            if (textField.getText().equals("")){
                return false;
            }
        }
        return true;
    }

    public boolean checkIsLabelsEmpty(ArrayList<Label> labels){
        for (Label label:labels
        ) {
            if (label.getText().equals("")){
                return false;
            }
        }
        return true;
    }
}
