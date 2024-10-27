package controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import controller.DatabaseControllers.ControlUsers;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Hyperlink;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import model.User;
import java.io.IOException;
import java.sql.SQLException;


public class FrontFormController {
    public AnchorPane loginFormAP;
    public JFXButton btnLoginUser;
    public JFXTextField txtUserName;
    public JFXPasswordField txtPassword;
    public Hyperlink linkLoginAdmin;


    public void btnLoginUser_Action() throws IOException {
        try {
            if (txtUserName.getText().equals("")){
                new Alert(Alert.AlertType.INFORMATION,"Please Enter User Name.....").showAndWait();
            }else if (txtPassword.getText().equals("")){
                new Alert(Alert.AlertType.INFORMATION,"Please Enter Password.....").showAndWait();
            }else {
                    if (new ControlUsers().isAddedUserName(txtUserName.getText())){
                        User user=new ControlUsers().getByUserName(txtUserName.getText());
                        if (txtUserName.getText().equals(user.getUserName())) {
                            if (user.getPassword().equals(txtPassword.getText())) {
                                Stage window = (Stage) loginFormAP.getScene().getWindow();
                                window.setScene(new Scene(FXMLLoader.load(getClass().getResource("../view/DashBoard.fxml"))));
                            } else {
                                new Alert(Alert.AlertType.INFORMATION, "Wrong Password.....").showAndWait();
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
        }
//        Stage window = (Stage) loginFormAP.getScene().getWindow();
//        window.setScene(new Scene(FXMLLoader.load(getClass().getResource("../view/DashBoard.fxml"))));
    }

    public void linkLoginAdmin_Action() throws IOException {
        Stage stage=new Stage();
        stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("../view/AdminLoginForm.fxml"))));
        stage.setResizable(false);
        stage.setTitle("Admin");
        stage.show();
    }

}
