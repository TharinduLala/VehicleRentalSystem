package utill;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.util.Optional;

public class AlertUtil {

    public boolean getAlert(String text){
        ButtonType yes = new ButtonType("Yes", ButtonBar.ButtonData.OK_DONE);
        ButtonType no = new ButtonType("No", ButtonBar.ButtonData.CANCEL_CLOSE);
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, text, yes, no);
        alert.setTitle("Confirmation Alert");
        Optional<ButtonType> result = alert.showAndWait();
        return result.orElse(no) == yes;
    }

    public void refresh(AnchorPane anchorPane, String fileName){
        try {
            Parent load = FXMLLoader.load(getClass().getResource("../view/"+fileName+".fxml"));
            anchorPane.getChildren().clear();
            anchorPane.getChildren().add(load);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
