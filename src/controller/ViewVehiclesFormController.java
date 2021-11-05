package controller;

import com.jfoenix.controls.JFXButton;
import controller.DatabaseControllers.ControlVehicles;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import model.Vehicle;
import view.tm.VehicleTm;
import java.sql.SQLException;
import java.util.ArrayList;

public class ViewVehiclesFormController {
    public AnchorPane vehiclesFormAP;
    public TextField txtVehiNo;
    public JFXButton btnSearch;
    public TextField txtNoofSeats;
    public TextField txtVehiType;
    public JFXButton btnClear;
    public TextField txtPPKM;
    public TextField txtMeterReading;
    public Label lblAvailability;
    public ComboBox<String> cmbVehicles;
    public TableView<VehicleTm>tblVehicles;
    public TableColumn<String, String> colVehiNo;
    public TableColumn<String, String> colType;
    public TableColumn<String, String> colNoOfSeats;
    public TableColumn<String, String> colPPKM;
    public TableColumn<String, String> colMeterReading;
    private final ObservableList<String> driverCategory = FXCollections.observableArrayList("In Ride","Not In Ride","In Repair","All");
    public JFXButton btnInWork;
    public JFXButton btnInRepair;


    public void initialize(){
        btnInRepair.setDisable(true);
        btnInWork.setDisable(true);
        cmbVehicles.setItems(driverCategory);
        cmbVehicles.getSelectionModel().selectedItemProperty().
                addListener((observable, oldValue, newValue) -> updateTable(newValue));
    }

    public void btnSearch_Action() {
        try {
            if (txtVehiNo.getText().equals("")){
                new Alert(Alert.AlertType.INFORMATION, "Please Enter Vehicle Number.....").showAndWait();
            }else if (new ControlVehicles().alreadyExists(txtVehiNo.getText())){
                Vehicle vehicle=new ControlVehicles().getVehicle(txtVehiNo.getText());
                txtVehiType.setText(vehicle.getVehicleType());
                txtPPKM.setText(String.valueOf(vehicle.getPricePkm()));
                txtNoofSeats.setText(String.valueOf(vehicle.getNoOfSeats()));
                txtMeterReading.setText(vehicle.getMeterReading());
                String temp=vehicle.getAvailability();
                switch (temp) {
                    case "inARide":
                        lblAvailability.setText("In Ride");
                        break;
                    case "notInARide":
                        lblAvailability.setText("Not In Ride");
                        btnInRepair.setDisable(false);
                        break;
                    case "inRepair":
                        lblAvailability.setText("In Repair");
                        btnInWork.setDisable(false);
                        break;
                }
                txtVehiNo.setEditable(false);
            }else {
                new Alert(Alert.AlertType.INFORMATION, "Vehicle not exist.....").showAndWait();
            }
        } catch (SQLException | ClassNotFoundException throwables) {
            throwables.printStackTrace();
        }
    }

    public void btnClear_Action() {
        txtVehiNo.setText("");
        txtVehiType.setText("");
        txtMeterReading.setText("");
        txtNoofSeats.setText("");
        txtPPKM.setText("");
        lblAvailability.setText("");
        btnInWork.setDisable(true);
        btnInRepair.setDisable(true);
        txtVehiNo.setEditable(true);
    }

    private void loadAllVehicles(ArrayList<VehicleTm> vehicles) {
        ObservableList<VehicleTm> vehicleTms = FXCollections.observableArrayList();
        vehicleTms.addAll(vehicles);
        tblVehicles.setItems(vehicleTms);
    }

    private void updateTable(String category){
        try {
            colVehiNo.setCellValueFactory(new PropertyValueFactory<>("vehicleNo"));
            colType.setCellValueFactory(new PropertyValueFactory<>("vehicleType"));
            colNoOfSeats.setCellValueFactory(new PropertyValueFactory<>("noOfSeats"));
            colPPKM.setCellValueFactory(new PropertyValueFactory<>("pricePkm"));
            colMeterReading.setCellValueFactory(new PropertyValueFactory<>("meterReading"));
            switch (category) {
                case "In Ride":
                    loadAllVehicles(new ControlVehicles().getAllCategorizedVehicles("inARide"));
                    break;
                case "Not In Ride":
                    loadAllVehicles(new ControlVehicles().getAllCategorizedVehicles("notInARide"));
                    break;
                case "All":
                    loadAllVehicles(new ControlVehicles().getAllVehicles());
                    break;
                case "In Repair":
                    loadAllVehicles(new ControlVehicles().getAllCategorizedVehicles("inRepair"));
                    break;
            }
        } catch (SQLException | ClassNotFoundException throwables) {
            throwables.printStackTrace();
        }
    }

    public void btnInWork_Action() {
        try {
            new ControlVehicles().setAvailability(txtVehiNo.getText(),"notInARide");
            btnInRepair.setDisable(false);
            btnInWork.setDisable(true);
            lblAvailability.setText("Not In Ride");
            if (cmbVehicles.getValue()==null){
                updateTable("Not In Ride");
                cmbVehicles.setValue("Not In Ride");
            }else {
                updateTable(cmbVehicles.getValue());
            }
        } catch (SQLException | ClassNotFoundException throwables) {
            throwables.printStackTrace();
        }
    }

    public void btnInRepair_Action() {
        try {
            new ControlVehicles().setAvailability(txtVehiNo.getText(),"inRepair");
            btnInRepair.setDisable(true);
            btnInWork.setDisable(false);
            lblAvailability.setText("In Repair");
            if (cmbVehicles.getValue()==null){
                updateTable("In Repair");
                cmbVehicles.setValue("In Repair");
            }else {
                updateTable(cmbVehicles.getValue());
            }
        } catch (SQLException | ClassNotFoundException throwables) {
            throwables.printStackTrace();
        }
    }
}
