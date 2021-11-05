package controller;

import com.jfoenix.controls.JFXButton;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AdminDashBoardController {
    public AnchorPane adminADashBoardAP;
    public Label lblDate;
    public Label lblTime;
    public JFXButton btnLogOut;
    public JFXButton btnManageVehicles;
    public JFXButton btnManageOwners;
    public JFXButton btnManageUsers;
    public AnchorPane windowOpenAreaAP;
    public JFXButton btnManageDrivers;
    public JFXButton btnBillDetails;

    public void initialize(){
        setDateTime();
    }

    public void btnManageDrivers_Action(){
        try {
            loadUi("AdminManageDriversForm");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void btnManageVehicles_Action() {
        try {
            loadUi("AdminManageVehiclesForm");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void btnManageOwners_Action() {
        try {
            loadUi("AdminManageVehicleOwnersForm");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void btnManageUsers_Action() {
        try {
            loadUi("AdminManageUsersForm");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void btnBillDetails_Action() {
        try {
            loadUi("BillDetailsForm");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void btnLogout_Action() {
        Stage window = (Stage) adminADashBoardAP.getScene().getWindow();
        window.close();
    }

    private void loadUi(String filName) throws IOException {
        Parent load = FXMLLoader.load(getClass().getResource("../view/"+filName+".fxml"));
        windowOpenAreaAP.getChildren().clear();
        windowOpenAreaAP.getChildren().add(load);
    }

    private void setDateTime(){
        Date date = new Date();
        SimpleDateFormat f = new SimpleDateFormat("yyyy/MM/dd");
        lblDate.setText(f.format(date));

        Timeline time = new Timeline(new KeyFrame(Duration.ZERO, e -> {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("hh : mm : ss a");
            String t=simpleDateFormat.format(new Date());
            lblTime.setText(t);
        }),
                new KeyFrame(Duration.seconds(1))
        );
        time.setCycleCount(Animation.INDEFINITE);
        time.play();
    }


}
