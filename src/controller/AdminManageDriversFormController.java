package controller;

import com.jfoenix.controls.JFXButton;
import controller.DatabaseControllers.ControlDrivers;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import model.Driver;
import model.DriverDetails;
import utill.AlertUtil;
import utill.ValidationUtil;
import view.tm.DriverTm;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.regex.Pattern;

public class AdminManageDriversFormController {
    public AnchorPane driversFormAP;
    public JFXButton btnClear;
    public TextField txtSearchDriverNIC;
    public JFXButton btnSearch;
    public TextField txtDriverContact;
    public TextField txtDriverAddress;
    public TextField txtDriverName;
    public JFXButton btnRemove;
    public JFXButton btnUpdate;
    public TableView<DriverTm> tblDrivers;
    public TableColumn<Object, Object> colNIC;
    public TableColumn<Object, Object> colName;
    public TableColumn<Object, Object> colAddress;
    public TableColumn<Object, Object> colContact;
    public TableColumn<Object, Object> colLicenseNO;
    public TableColumn<Object, Object> colDOB;
    public JFXButton btnAddNew;
    public TextField txtDriverNIC;
    public TextField txtLicenseNo;
    public TextField txtDOB;

    LinkedHashMap<TextField, Pattern> validationMap=new LinkedHashMap<>();
    ArrayList<JFXButton>btnList=new ArrayList<>();
    Pattern nicPattern=Pattern.compile("^((19|20)\\d\\d[0-9]{8})|([5-9][0-9]{8}[vV])$");
    Pattern namePattern=Pattern.compile("^[A-z ]{3,30}$");
    Pattern addressPattern=Pattern.compile("^[0-9A-z\\s,]+$");
    Pattern contactPattern=Pattern.compile("^[1-9][0-9]{8}$");
    Pattern licenseNoPattern=Pattern.compile("^[A-Z][0-9]{6,7}$");
    Pattern dobPattern=Pattern.compile("^(19|20)\\d\\d[/|-](([0][1-9])|([1][0|1,2]))[/|-](([0][1-9])|([1|2][0-9])|(3[0|1]))$");

    public void initialize(){
        btnAddNew.setDisable(true);
        btnUpdate.setDisable(true);
        setValidation();
        updateTable();

    }

    public void btnClear_Action() {
        txtSearchDriverNIC.setText("");
        txtDriverNIC.setText("");
        txtDriverName.setText("");
        txtDriverAddress.setText("");
        txtDOB.setText("");
        txtDriverContact.setText("");
        txtLicenseNo.setText("");
        txtDriverNIC.setEditable(true);
    }

    public void btnSearch_Action() {
        try {
            if (txtSearchDriverNIC.getText().equals("")){
                new Alert(Alert.AlertType.INFORMATION, "Please Enter Driver NIC.....").showAndWait();
            }else if (new ControlDrivers().alreadyExists(txtSearchDriverNIC.getText())){
                DriverTm driver=new ControlDrivers().getDriverWithDetails(txtSearchDriverNIC.getText());
                txtDriverNIC.setText(driver.getDriverNIC());
                txtDriverName.setText(driver.getDriverName());
                txtDriverContact.setText(String.valueOf(driver.getDriverContactNo()));
                txtLicenseNo.setText(driver.getDrivingLicenseNO());
                txtDriverAddress.setText(driver.getDriverAddress());
                txtDOB.setText(driver.getDateOfBirth());
                txtDriverNIC.setEditable(false);
            }else {
                new Alert(Alert.AlertType.INFORMATION, "Driver not exist.....").showAndWait();
            }
        } catch (SQLException | ClassNotFoundException throwables) {
            throwables.printStackTrace();
        }
    }

    public void btnRemove_Action() {
        if (txtDriverNIC.getText().equals("")){
            new Alert(Alert.AlertType.INFORMATION, "Please Select Driver.....").showAndWait();
        }else {
            if (new AlertUtil().getAlert("Do You want to Remove this Driver")) {
                try {
                    if (new ControlDrivers().removeDriver(txtDriverNIC.getText())) {
                        new Alert(Alert.AlertType.INFORMATION, "Removed.....").showAndWait();
                        btnClear_Action();
                    } else {
                        new Alert(Alert.AlertType.INFORMATION, "Try Again.....").showAndWait();
                    }
                    updateTable();
                } catch (SQLException | ClassNotFoundException throwables) {
                    throwables.printStackTrace();
                }
            }
        }
    }

    public void btnUpdate_Action() {
        try {
            if(new ControlDrivers().alreadyExists(txtDriverNIC.getText())) {
                String availability=new ControlDrivers().getDriver(txtDriverNIC.getText()).getAvailability();
                Driver driver=new Driver(
                        txtDriverNIC.getText(),txtDriverName.getText(),availability
                );
                DriverDetails updatedDetails =new DriverDetails(
                        txtDriverNIC.getText(),txtDriverAddress.getText(),txtLicenseNo.getText(),txtDOB.getText(),Integer.parseInt(txtDriverContact.getText())
                );
                if (new ControlDrivers().updateDriver(driver,updatedDetails) ) {
                    new Alert(Alert.AlertType.INFORMATION, "Updated.....").showAndWait();
                    btnClear_Action();
                    btnAddNew.setDisable(true);
                    btnUpdate.setDisable(true);
                    updateTable();
                } else {
                    new Alert(Alert.AlertType.INFORMATION, "Try Again.....").showAndWait();
                }
            }else {
                new Alert(Alert.AlertType.INFORMATION, "Driver not exist.....").showAndWait();
            }
        } catch (SQLException | ClassNotFoundException throwables) {
            throwables.printStackTrace();
        }
    }

    public void btnAddNew_Action() {
        try {
            if(new ControlDrivers().alreadyExists(txtDriverNIC.getText())) {
                new Alert(Alert.AlertType.INFORMATION, "Driver Already Added.....").showAndWait();
            }else {
                Driver driver=new Driver(
                        txtDriverNIC.getText(),txtDriverName.getText(),"notInARide"
                );
                DriverDetails driverDetails=new DriverDetails(
                        txtDriverNIC.getText(),txtDriverAddress.getText(),txtLicenseNo.getText(),txtDOB.getText(),Integer.parseInt(txtDriverContact.getText())
                );
                if (new ControlDrivers().addNewDriver(driver,driverDetails) ) {
                    new Alert(Alert.AlertType.INFORMATION, "Saved.....").showAndWait();
                    btnClear_Action();
                    btnAddNew.setDisable(true);
                    btnUpdate.setDisable(true);
                    updateTable();
                } else {
                    new Alert(Alert.AlertType.INFORMATION, "Try Again.....").showAndWait();
                }
            }
        } catch (SQLException | ClassNotFoundException throwables) {
            throwables.printStackTrace();
        }
    }

    private void loadAllDrivers(ArrayList<DriverTm> drivers) {
        ObservableList<DriverTm> driverTms = FXCollections.observableArrayList();
        driverTms.addAll(drivers);
        tblDrivers.setItems(driverTms);
    }

    private void updateTable(){
        try {
            colNIC.setCellValueFactory(new PropertyValueFactory<>("driverNIC"));
            colName.setCellValueFactory(new PropertyValueFactory<>("driverName"));
            colAddress.setCellValueFactory(new PropertyValueFactory<>("driverAddress"));
            colContact.setCellValueFactory(new PropertyValueFactory<>("driverContactNo"));
            colDOB.setCellValueFactory(new PropertyValueFactory<>("dateOfBirth"));
            colLicenseNO.setCellValueFactory(new PropertyValueFactory<>("drivingLicenseNO"));
            loadAllDrivers(new ControlDrivers().getAllDrivers());
        } catch (SQLException | ClassNotFoundException throwables) {
            throwables.printStackTrace();
        }
    }

    private void setValidation(){

        validationMap.put(txtDriverNIC,nicPattern);
        validationMap.put(txtDriverName,namePattern);
        validationMap.put(txtDriverAddress,addressPattern);
        validationMap.put(txtDriverContact,contactPattern);
        validationMap.put(txtLicenseNo,licenseNoPattern);
        validationMap.put(txtDOB,dobPattern);
        btnList.add(btnAddNew);
        btnList.add(btnUpdate);
    }

    public void driverTextFields_Key_Released(KeyEvent keyEvent) {
        Object response = ValidationUtil.validate(validationMap,btnList);
        if (keyEvent.getCode() == KeyCode.ENTER) {
            if (response instanceof TextField) {
                TextField errorText = (TextField) response;
                errorText.requestFocus();
            }
        }
    }
}
