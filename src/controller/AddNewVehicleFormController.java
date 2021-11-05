package controller;

import com.jfoenix.controls.JFXButton;
import controller.DatabaseControllers.ControlVehicleOwners;
import controller.DatabaseControllers.ControlVehicles;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import model.*;
import utill.ValidationUtil;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.regex.Pattern;

public class AddNewVehicleFormController {
    public AnchorPane addVehicleFormAP;
    public JFXButton btnAdd;
    public TextField txtVehiNo;
    public TextField txtPPKM;
    public TextField txtBrand;
    public TextField txtMeterValue;
    public TextField txtModel;
    public TextField txtNoOfSeats;
    public ComboBox<String> cmbType;
    public ComboBox<String> cmbOwnerNIC;
    public JFXButton btnClose;
    private AdminManageVehiclesFormController manageVehiclesFormController;
    private final ObservableList<String> vehicleTypes = FXCollections.observableArrayList("Mini Car","Car","Mini Van","Van");
    LinkedHashMap<TextField, Pattern> validationMap=new LinkedHashMap<>();
    ArrayList<JFXButton> btnList=new ArrayList<>();

    Pattern vehiNoPattern=Pattern.compile("^([A-Z]{2,3}|[1-9][0-9]{2})[-][0-9]{4}$");
    Pattern ppkmPattern=Pattern.compile("^[1-9][0-9]*([.][0-9]{2})?$");
    Pattern brandPattern=Pattern.compile("^[A-z ]{3,30}$");
    Pattern meterValuePattern=Pattern.compile("^[0-9]{5}[.][0-9]$");
    Pattern modelPattern=Pattern.compile("^[A-z 0-9]{3,30}$");
    Pattern noOfSeatsPattern=Pattern.compile("^[1-9]{1,2}$");


    public void initialize(){
        try {
            loadComboBox();
        } catch (SQLException | ClassNotFoundException throwables) {
            throwables.printStackTrace();
        }
        btnAdd.setDisable(true);
        setValidation();
    }

    public void btnAdd_Action() {
        try {
            if (cmbType.getValue()==null||cmbOwnerNIC.getValue()==null){
                new Alert(Alert.AlertType.INFORMATION, "Please Select Values.....").showAndWait();
            }else if(new ControlVehicles().alreadyExists(txtVehiNo.getText())) {
                new Alert(Alert.AlertType.INFORMATION, "Vehicle Already Added.....").showAndWait();
            }else {
                Vehicle vehicle =new Vehicle(
                        txtVehiNo.getText(),cmbType.getValue(),Double.parseDouble(txtPPKM.getText()),txtMeterValue.getText(),
                        Integer.parseInt(txtNoOfSeats.getText()),cmbOwnerNIC.getValue(),"notInARide"
                );
                VehicleRegDetails regDetails =new VehicleRegDetails(
                        txtVehiNo.getText(),txtModel.getText(),txtBrand.getText(),txtMeterValue.getText()
                );
                if (new ControlVehicles().addNewVehicle(vehicle,regDetails)) {
                    new Alert(Alert.AlertType.INFORMATION, "Saved.....").showAndWait();
                    manageVehiclesFormController.updateTable();
                    Stage window = (Stage) addVehicleFormAP.getScene().getWindow();
                    window.close();
                } else {
                    new Alert(Alert.AlertType.INFORMATION, "Try Again.....").showAndWait();
                }
            }
        } catch (SQLException | ClassNotFoundException throwables) {
            throwables.printStackTrace();
        }
    }

    private void setValidation(){
        validationMap.put(txtVehiNo,vehiNoPattern);
        validationMap.put(txtBrand,brandPattern);
        validationMap.put(txtModel,modelPattern);
        validationMap.put(txtNoOfSeats,noOfSeatsPattern);
        validationMap.put(txtMeterValue,meterValuePattern);
        validationMap.put(txtPPKM,ppkmPattern);
        btnList.add(btnAdd);
    }

    public void addVehicle_KeyEvent(KeyEvent keyEvent) {
        Object response = ValidationUtil.validate(validationMap,btnList);
        if (keyEvent.getCode() == KeyCode.ENTER) {
            if (response instanceof TextField) {
                TextField errorText = (TextField) response;
                errorText.requestFocus();
            }
        }
    }

    private void loadComboBox() throws SQLException, ClassNotFoundException {
        ObservableList<String> ownerIds = FXCollections.observableArrayList();
        ArrayList<VehicleOwner> owners=new ControlVehicleOwners().getAllOwners();
        for (VehicleOwner owner:owners
             ) {
            ownerIds.add(owner.getOwnerNIC());
        }
        cmbOwnerNIC.setItems(ownerIds);
        cmbType.setItems(vehicleTypes);
    }

    public void setEvent(AdminManageVehiclesFormController manageVehiclesFormController){
        this.manageVehiclesFormController=manageVehiclesFormController;
    }

    public void btnClose_Action() {
        Stage window = (Stage) addVehicleFormAP.getScene().getWindow();
        window.close();
    }
}
