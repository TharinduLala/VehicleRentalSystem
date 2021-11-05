package controller;

import com.jfoenix.controls.JFXButton;
import controller.DatabaseControllers.ControlDrivers;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import javafx.scene.layout.AnchorPane;
import view.tm.DriverTm;


import java.sql.SQLException;
import java.util.ArrayList;

public class ViewDriversFormController {
    public AnchorPane driversFormAP;
    public ComboBox<String> cmbDrivers;
    public TableView<DriverTm> tblDrivers;
    public TableColumn<Object, Object> colNIC;
    public TableColumn<Object, Object> colName;
    public TableColumn<Object, Object> colContact;
    public TableColumn<Object, Object> colLicenseNo;
    public TableColumn<Object, Object> colAddress;
    public TableColumn<Object, Object> colDOB;
    public TextField txtDriverNIC;
    public JFXButton btnSearch;
    public TextField txtDriverContact;
    public TextField txtDriverName;
    public JFXButton btn_Clear;
    public Label lblAvailability;
    private final ObservableList<String> driverCategory = FXCollections.observableArrayList("In Ride","Not In Ride","Duty Off","All");
    public JFXButton btnDutyOn;
    public JFXButton btnDutyOff;

    public void initialize(){
        btnDutyOff.setDisable(true);
        btnDutyOn.setDisable(true);
        cmbDrivers.setItems(driverCategory);
        cmbDrivers.getSelectionModel().selectedItemProperty().
            addListener((observable, oldValue, newValue) -> updateTable(newValue));
    }

    public void btnSearch_Action() {
        try {
            if (txtDriverNIC.getText().equals("")){
                new Alert(Alert.AlertType.INFORMATION, "Please Enter Driver NIC.....").showAndWait();
            }else if (new ControlDrivers().alreadyExists(txtDriverNIC.getText())){
                DriverTm driver=new ControlDrivers().getDriverWithDetails(txtDriverNIC.getText());
                txtDriverNIC.setText(driver.getDriverNIC());
                txtDriverName.setText(driver.getDriverName());
                txtDriverContact.setText(String.valueOf(driver.getDriverContactNo()));
                String temp=new ControlDrivers().getDriver(txtDriverNIC.getText()).getAvailability();
                switch (temp) {
                    case "inARide":
                        lblAvailability.setText("In Ride");
                        break;
                    case "notInARide":
                        lblAvailability.setText("Not In Ride");
                        btnDutyOff.setDisable(false);
                        break;
                    case "leaved":
                        lblAvailability.setText("Duty Off");
                        btnDutyOn.setDisable(false);
                        break;
                }
                txtDriverNIC.setEditable(false);
            }else {
                new Alert(Alert.AlertType.INFORMATION, "Driver not exist.....").showAndWait();
            }
        } catch (SQLException | ClassNotFoundException throwables) {
            throwables.printStackTrace();
        }
    }

    public void btnClear_Action() {
        txtDriverNIC.setText("");
        txtDriverContact.setText("");
        txtDriverName.setText("");
        lblAvailability.setText("");
        btnDutyOn.setDisable(true);
        btnDutyOff.setDisable(true);
        txtDriverNIC.setEditable(true);
    }

    private void loadAllDrivers(ArrayList<DriverTm> drivers) {
        ObservableList<DriverTm> driverTms = FXCollections.observableArrayList();
        driverTms.addAll(drivers);
        tblDrivers.setItems(driverTms);
    }

    private void updateTable(String category){
        try {
            colNIC.setCellValueFactory(new PropertyValueFactory<>("driverNIC"));
            colName.setCellValueFactory(new PropertyValueFactory<>("driverName"));
            colAddress.setCellValueFactory(new PropertyValueFactory<>("driverAddress"));
            colContact.setCellValueFactory(new PropertyValueFactory<>("driverContactNo"));
            colDOB.setCellValueFactory(new PropertyValueFactory<>("dateOfBirth"));
            colLicenseNo.setCellValueFactory(new PropertyValueFactory<>("drivingLicenseNO"));
            switch (category) {
                case "In Ride":
                    loadAllDrivers(new ControlDrivers().getAllCategorizedDrivers("inARide"));
                    break;
                case "Not In Ride":
                    loadAllDrivers(new ControlDrivers().getAllCategorizedDrivers("notInARide"));
                    break;
                case "All":
                    loadAllDrivers(new ControlDrivers().getAllDrivers());
                    break;
                case "Duty Off":
                    loadAllDrivers(new ControlDrivers().getAllCategorizedDrivers("leaved"));
                    break;
            }
        } catch (SQLException | ClassNotFoundException throwables) {
            throwables.printStackTrace();
        }
    }

    public void btnDutyOff_Action() {
        try {
            new ControlDrivers().setAvailability(txtDriverNIC.getText(),"leaved");
            btnDutyOff.setDisable(true);
            btnDutyOn.setDisable(false);
            lblAvailability.setText("Duty Off");
            if (cmbDrivers.getValue()==null){
                cmbDrivers.setValue("Duty Off");
                updateTable("Duty Off");
            }else {
                updateTable(cmbDrivers.getValue());
            }
        } catch (SQLException | ClassNotFoundException throwables) {
            throwables.printStackTrace();
        }
    }

    public void btnDutyOn_Action() {
        try {
            new ControlDrivers().setAvailability(txtDriverNIC.getText(),"notInARide");
            btnDutyOff.setDisable(false);
            btnDutyOn.setDisable(true);
            lblAvailability.setText("Not In Ride");
            if (cmbDrivers.getValue()==null){
                updateTable("Not In Ride");
                cmbDrivers.setValue("Not In Ride");
            }else {
                updateTable(cmbDrivers.getValue());
            }
        } catch (SQLException | ClassNotFoundException throwables) {
            throwables.printStackTrace();
        }
    }
}
