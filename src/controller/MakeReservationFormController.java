package controller;

import com.jfoenix.controls.JFXButton;
import controller.DatabaseControllers.ControlBookings;
import controller.DatabaseControllers.ControlCustomers;
import controller.DatabaseControllers.ControlDrivers;
import controller.DatabaseControllers.ControlVehicles;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import model.Booking;
import model.Customer;
import model.Vehicle;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.view.JasperViewer;
import utill.AlertUtil;
import utill.ValidationUtil;
import view.tm.VehicleTm;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;


public class MakeReservationFormController {
    public AnchorPane newBookingAP;
    public ComboBox<String> cmbVehicleType;
    public ComboBox<String> cmbAvailableVehicles;
    public Label lblNoOfSeats;
    public Label lblPPKM;
    public TextField txtCustomerNIC;
    public JFXButton btnSearch;
    public Label lblCusName;
    public Label lblCusContact;
    public ComboBox<String> cmbDrivers;
    public Label lblDriverNIC;
    public JFXButton btnStartRide;
    public Label lblBookingId;
    public TextField txtMeterReading;
    private final ObservableList<String> vehicleTypes = FXCollections.observableArrayList("Mini Car","Car","Mini Van","Van");
    private final ArrayList<Label> labels=new ArrayList<>();
    private final ArrayList<TextField> fields=new ArrayList<>();
    public JFXButton btnAddNewCustomer;

    public void initialize(){

        labels.add(lblPPKM);
        labels.add(lblDriverNIC);
        labels.add(lblCusName);
        fields.add(txtCustomerNIC);
        fields.add(txtMeterReading);
        cmbVehicleType.setItems(vehicleTypes);
        setBookingId();
        ///////////////////////////////////////////////////////////////////////////////////////////////
        try {
            loadDriverComboBox(new ControlDrivers().getDriversName("notInARide"));
        } catch (SQLException | ClassNotFoundException throwables) {
            throwables.printStackTrace();
        }
        ///////////////////////////////////////////////////////////////////////////////////////////////
        cmbVehicleType.getSelectionModel().selectedItemProperty().
           addListener((observable, oldValue, newValue) -> {
           try {
               txtMeterReading.setText("");
               lblPPKM.setText("");
               lblNoOfSeats.setText("");
               cmbAvailableVehicles.setItems(null);
               loadVehicleComboBox(new ControlVehicles().getAvailableVehicles(newValue));
           } catch (SQLException | ClassNotFoundException throwables) {
               throwables.printStackTrace();
           }
           });
        //##########################################################//
        cmbAvailableVehicles.getSelectionModel().selectedItemProperty().
           addListener((observable, oldValue, newValue) -> {
           try {
               if(cmbAvailableVehicles.getItems()!=null) {
                   Vehicle vehicle = new ControlVehicles().getVehicle(newValue);
                   txtMeterReading.setText(vehicle.getMeterReading());
                   lblNoOfSeats.setText(String.valueOf(vehicle.getNoOfSeats()));
                   lblPPKM.setText(String.valueOf(vehicle.getPricePkm()));
               }
           } catch (SQLException | ClassNotFoundException throwables) {
               throwables.printStackTrace();
           }
           });
        //##########################################################//
        cmbDrivers.getSelectionModel().selectedItemProperty().
           addListener((observable, oldValue, newValue) -> {
           try {
               lblDriverNIC.setText(new ControlDrivers().getDriverNic(newValue));
           } catch (SQLException | ClassNotFoundException throwables) {
               throwables.printStackTrace();
           }
           });
        ///////////////////////////////////////////////////////////////////////////////////////////////
    }

    public void btnSearch_Action() {
        try {
            if (txtCustomerNIC.getText().equals("")){
                new Alert(Alert.AlertType.INFORMATION, "Please Enter Customer NIC.....").showAndWait();
            }else if (new ControlCustomers().alreadyExists(txtCustomerNIC.getText())){
                Customer customer=new ControlCustomers().getCustomer(txtCustomerNIC.getText());
                lblCusName.setText(customer.getCustomerName());
                lblCusContact.setText(String.valueOf(customer.getCustomerContactNo()));
            }else {
                new Alert(Alert.AlertType.INFORMATION, "Customer not exist.....").showAndWait();
                lblCusName.setText("");
                lblCusContact.setText("");
            }
        } catch (SQLException | ClassNotFoundException throwables) {
            throwables.printStackTrace();
        }
    }

    public void btnStartRide_Action() {
        if(new ValidationUtil().checkIsFieldsEmpty(fields)&&new ValidationUtil().checkIsLabelsEmpty(labels)) {
            Booking booking = new Booking(
                    lblBookingId.getText(),
                    txtCustomerNIC.getText(),
                    lblDriverNIC.getText(),
                    cmbAvailableVehicles.getValue(),
                    cmbVehicleType.getValue(),
                    txtMeterReading.getText(),
                    DashBoardController.date,
                    "no"
            );
                if (new ControlBookings().addBooking(booking)
                ) {
                    new Alert(Alert.AlertType.INFORMATION, "Success.....").showAndWait();
                    printReceipt();
                    setBookingId();
                    new AlertUtil().refresh(newBookingAP, "MakeReservationForm");
                } else {
                    new Alert(Alert.AlertType.INFORMATION, "Try Again.....").showAndWait();
                }
        }else {
            new Alert(Alert.AlertType.INFORMATION,"Please Complete Details.....").show();
        }
    }

    private void loadVehicleComboBox(ArrayList<VehicleTm>vehicles ){
        ObservableList<String> vehicleNo = FXCollections.observableArrayList();
        for (VehicleTm vehicleTm :vehicles) {
            vehicleNo.add(vehicleTm.getVehicleNo());
        }
        cmbAvailableVehicles.setItems(vehicleNo);
    }

    private void loadDriverComboBox(ArrayList<String>driverNames ){
        ObservableList<String> driverName = FXCollections.observableArrayList();
        driverName.addAll(driverNames);
        cmbDrivers.setItems(driverName);
    }

    private void setBookingId() {
        try {
            lblBookingId.setText(new ControlBookings().createBookingId());
        } catch (SQLException | ClassNotFoundException throwables) {
            throwables.printStackTrace();
        }
    }

    public void btnAddNewCustomer_Action() throws IOException {
        Stage stage=new Stage();
        stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("../view/AddNewCustomerForm.fxml"))));
        stage.setResizable(false);
        stage.setTitle("Add New Customer");
        stage.initStyle(StageStyle.UNDECORATED);
        stage.show();


    }

    private void printReceipt(){
        try {
            JasperDesign design = JRXmlLoader.load(this.getClass().getResourceAsStream("/view/reports/BookingReceipt.jrxml"));
            JasperReport compileReport = JasperCompileManager.compileReport(design);
            HashMap<String, Object> map=new HashMap<>();
            map.put("bookingId",lblBookingId.getText());
            map.put("customerName",lblCusName.getText());
            map.put("customerNic",txtCustomerNIC.getText());
            map.put("customerTel",lblCusContact.getText());
            map.put("vehicleType",cmbVehicleType.getValue());
            map.put("vehicleNo",cmbAvailableVehicles.getValue());
            map.put("meterReading",txtMeterReading.getText());
            map.put("ppkm",lblPPKM.getText());
            map.put("driverName",cmbDrivers.getValue());
            JasperPrint jasperPrint = JasperFillManager.fillReport(compileReport, map, new JREmptyDataSource(1));
            JasperViewer.viewReport(jasperPrint, false);
            //JasperPrintManager.printReport(jasperPrint,false);
        } catch (JRException e) {
            e.printStackTrace();
        }
    }
}








