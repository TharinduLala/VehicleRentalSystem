package controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import controller.DatabaseControllers.ControlUsers;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Screen;
import javafx.stage.Stage;
import model.User;

import java.io.IOException;
import java.sql.SQLException;

public class AdminLoginFormController {
    public AnchorPane adminLoginFormAP;
    public JFXButton btnLoginAdmin;
    public JFXTextField txtAdminUserName;
    public JFXPasswordField txtAdminPassword;

    public void btnLoginAdmin_Action() throws IOException {
        /*try {
            if (txtAdminUserName.getText().equals("")){
                new Alert(Alert.AlertType.INFORMATION,"Please Enter User Name.....").showAndWait();
            }else if (txtAdminPassword.getText().equals("")){
                new Alert(Alert.AlertType.INFORMATION,"Please Enter Password.....").showAndWait();
            }else {
                if (new ControlUsers().isAddedUserName(txtAdminUserName.getText())){
                    User user=new ControlUsers().getByUserName(txtAdminUserName.getText());
                    if (txtAdminUserName.getText().equals(user.getUserName())) {
                        if (user.getRole().equals("Admin")) {
                            if (user.getPassword().equals(txtAdminPassword.getText())) {
                                Stage window = (Stage) adminLoginFormAP.getScene().getWindow();
                                window.setResizable(false);
                                window.setScene(new Scene(FXMLLoader.load(getClass().getResource("../view/AdminDashBoard.fxml"))));
                                Rectangle2D primScreenBounds = Screen.getPrimary().getVisualBounds();
                                window.setX((primScreenBounds.getWidth() - window.getWidth()) / 2);
                                window.setY((primScreenBounds.getHeight() - window.getHeight()) / 2);
                            } else {
                                new Alert(Alert.AlertType.INFORMATION, "Wrong Password.....").showAndWait();
                            }
                        } else {
                            new Alert(Alert.AlertType.INFORMATION, "Login only for Admins.....").showAndWait();
                        }
                    }else {
                        new Alert(Alert.AlertType.INFORMATION, "Wrong User Name.....").showAndWait();
                    }
                }else {
                    new Alert(Alert.AlertType.INFORMATION,"User Name not exists.....").showAndWait();
                }
            }
        } catch (SQLException | ClassNotFoundException throwables) {
            throwables.printStackTrace();
        }*/
        Stage window = (Stage) adminLoginFormAP.getScene().getWindow();
        window.setResizable(false);
        window.setScene(new Scene(FXMLLoader.load(getClass().getResource("../view/AdminDashBoard.fxml"))));
        Rectangle2D primScreenBounds = Screen.getPrimary().getVisualBounds();
        window.setX((primScreenBounds.getWidth() - window.getWidth()) / 2);
        window.setY((primScreenBounds.getHeight() - window.getHeight()) / 2);
    }

}
