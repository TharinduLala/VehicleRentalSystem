package controller;

import com.jfoenix.controls.JFXButton;
import controller.DatabaseControllers.ControlDrivers;
import controller.DatabaseControllers.ControlVehicles;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import java.io.IOException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DashBoardController {
    public AnchorPane dashBoardAP;
    public AnchorPane windowOpenAreaAP;
    public Label lblDate;
    public Label lblTime;
    public JFXButton btnDashBoard;
    public JFXButton btnBooking;
    public JFXButton btnMakeBill;
    public JFXButton btnCustomers;
    public JFXButton btnDrivers;
    public JFXButton btnVehicles;
    public JFXButton btnLogOut;
    public JFXButton btnMakeReservation;
    public Label noOfCars;
    public Label noOfMiniCars;
    public Label noOfMiniVans;
    public Label noOfVans;
    public Label noOfDrivers;
    public static String date;
    public Hyperlink linkLoginAdmin;


    public void initialize(){
        try {
            noOfCars.setText(String.valueOf(new ControlVehicles().getAvailableVehicles("Car").size()));
            noOfMiniCars.setText(String.valueOf(new ControlVehicles().getAvailableVehicles("Mini Car").size()));
            noOfMiniVans.setText(String.valueOf(new ControlVehicles().getAvailableVehicles("Mini Van").size()));
            noOfVans.setText(String.valueOf(new ControlVehicles().getAvailableVehicles("Van").size()));
            noOfDrivers.setText(String.valueOf(new ControlDrivers().getAllCategorizedDrivers("notInARide").size()));
        } catch (SQLException | ClassNotFoundException throwables) {
            throwables.printStackTrace();
        }
        setDateTime();
        date=lblDate.getText();
    }

    public void btnDashBoard_Action() {
        try {
            loadUi("DashBoard");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void btnMakeReservation_Action() {
        try {
            loadUi("MakeReservationForm");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void btnBooking_Action() {
        try {
            loadUi("BookingDetailsForm");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void btnMakeBill_Action() {
        try {
            loadUi("MakeBillForm");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void btnCustomers_Action() {
        try {
            loadUi("CustomersForm");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void btnDrivers_Action() {
        try {
            loadUi("ViewDriversForm");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void btnVehicles_Action() {
        try {
            loadUi("ViewVehiclesForm");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void btnLogOut_Action() throws IOException {
        Stage window = (Stage) dashBoardAP.getScene().getWindow();
        window.setScene(new Scene(FXMLLoader.load(getClass().getResource("../view/FrontForm.fxml"))));
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

    public void linkLoginAdmin_Action() throws IOException {
        Stage stage=new Stage();
        stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("../view/AdminLoginForm.fxml"))));
        stage.setResizable(false);
        stage.setTitle("Admin");
        stage.show();
    }
}
