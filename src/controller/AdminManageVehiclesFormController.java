package controller;

import com.jfoenix.controls.JFXButton;
import controller.DatabaseControllers.ControlVehicles;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import model.Vehicle;
import model.VehicleRegDetails;
import utill.AlertUtil;
import utill.ValidationUtil;
import view.tm.VehicleTm;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.regex.Pattern;

public class AdminManageVehiclesFormController {
    public AnchorPane manageVehiclesFromAP;
    public TextField txtSearchVehiNo;
    public JFXButton btnSearch;
    public TextField txtNoofSeats;
    public TextField txtVehiType;
    public JFXButton btnClear;
    public TextField txtPPKM;
    public TextField txtMeterReading;
    public TableView<VehicleTm> tblVehicles;
    public TableColumn<Object, Object> colVehiNo;
    public TableColumn<Object, Object> colType;
    public TableColumn<Object, Object> colNoOfSeats;
    public TableColumn<Object, Object> colPPKM;
    public TableColumn<Object, Object> colMeterReading;
    public JFXButton btnRemove;
    public JFXButton btnUpdate;
    public JFXButton btnAddNew;
    public JFXButton btnViewRegDetails;
    LinkedHashMap<TextField, Pattern> validationMap=new LinkedHashMap<>();
    ArrayList<JFXButton>btnList=new ArrayList<>();
    Pattern meterValuePattern=Pattern.compile("^[0-9]{5}[.][0-9]$");
    Pattern pricePattern=Pattern.compile("^[1-9][0-9]*([.][0-9]{1,2})?$");

    public void initialize(){
        btnViewRegDetails.setDisable(true);
        btnUpdate.setDisable(true);
        setValidation();
        updateTable();
    }

    public void btnSearch_Action() {
        try {
            if (txtSearchVehiNo.getText().equals("")){
                new Alert(Alert.AlertType.INFORMATION, "Please Enter Vehicle Number.....").showAndWait();
            }else if (new ControlVehicles().alreadyExists(txtSearchVehiNo.getText())){
                Vehicle vehicle=new ControlVehicles().getVehicle(txtSearchVehiNo.getText());
                VehicleRegDetails regDetails=new ControlVehicles().getVehicleRegDetails(txtSearchVehiNo.getText());
                txtVehiType.setText(vehicle.getVehicleType());
                txtPPKM.setText(String.valueOf(vehicle.getPricePkm()));
                txtNoofSeats.setText(String.valueOf(vehicle.getNoOfSeats()));
                txtMeterReading.setText(vehicle.getMeterReading());
                VehicleDetailsFromController.ownerNic =vehicle.getOwnerID();
                VehicleDetailsFromController.brand =regDetails.getBrand();
                VehicleDetailsFromController.model =regDetails.getModel();
                VehicleDetailsFromController.meterValue =regDetails.getMeterReadingAssigning();
                btnViewRegDetails.setDisable(false);
                txtSearchVehiNo.setEditable(false);
            }else {
                new Alert(Alert.AlertType.INFORMATION, "Vehicle not exist.....").showAndWait();
            }
        } catch (SQLException | ClassNotFoundException throwables) {
            throwables.printStackTrace();
        }
    }

    public void btnClear_Action() {
        txtSearchVehiNo.setText("");
        txtVehiType.setText("");
        txtMeterReading.setText("");
        txtNoofSeats.setText("");
        txtPPKM.setText("");
        btnUpdate.setDisable(true);
        btnViewRegDetails.setDisable(true);
        txtSearchVehiNo.setEditable(true);
    }

    public void btnRemove_Action() {
        try {
        if (txtSearchVehiNo.getText().equals("")){
            new Alert(Alert.AlertType.INFORMATION, "Please Select Driver.....").showAndWait();
        }else if(new ControlVehicles().alreadyExists(txtSearchVehiNo.getText())){
            if (new AlertUtil().getAlert("Do You want to Remove this Vehicle")) {
                    if (new ControlVehicles().removeVehicle(txtSearchVehiNo.getText())) {
                        new Alert(Alert.AlertType.INFORMATION, "Removed.....").showAndWait();
                        btnClear_Action();
                    } else {
                        new Alert(Alert.AlertType.INFORMATION, "Try Again.....").showAndWait();
                    }
                    updateTable();
            }
        }else {
            new Alert(Alert.AlertType.INFORMATION, "Vehicle Not Exist.....").showAndWait();
        }
        } catch (SQLException | ClassNotFoundException throwables) {
            throwables.printStackTrace();
        }
    }

    public void btnUpdate_Action() {
        try {
            if(new ControlVehicles().alreadyExists(txtSearchVehiNo.getText())) {
                String availability=new ControlVehicles().getVehicle(txtSearchVehiNo.getText()).getAvailability();
                String owner=new ControlVehicles().getVehicle(txtSearchVehiNo.getText()).getOwnerID();
                Vehicle vehicle=new Vehicle(
                        txtSearchVehiNo.getText(),txtVehiType.getText(),Double.parseDouble(txtPPKM.getText()),txtMeterReading.getText(),
                        Integer.parseInt(txtNoofSeats.getText()),availability,owner
                );

                if (new ControlVehicles().updateVehicle(vehicle)) {
                    new Alert(Alert.AlertType.INFORMATION, "Updated.....").showAndWait();
                    btnClear_Action();
                    updateTable();
                } else {
                    new Alert(Alert.AlertType.INFORMATION, "Try Again.....").showAndWait();
                }
            }else {
                new Alert(Alert.AlertType.INFORMATION, "Vehicle not exist.....").showAndWait();
            }
        } catch (SQLException | ClassNotFoundException throwables) {
            throwables.printStackTrace();
        }
    }

    public void btnAddNew_Action() {
        try {
            FXMLLoader loader=new FXMLLoader(getClass().getResource("../view/AddNewVehicleForm.fxml"));
            Parent parent=loader.load();
            AddNewVehicleFormController controller = loader.getController();
            controller.setEvent(this);
            Stage stage=new Stage();
            stage.setScene(new Scene(parent));
            stage.setResizable(false);
            stage.initStyle(StageStyle.UNDECORATED);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void btnViewRegDetails() {
        try {
            Stage stage=new Stage();
            stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("../view/VehicleDetailsFrom.fxml"))));
            stage.setResizable(false);
            stage.initStyle(StageStyle.UNDECORATED);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadAllVehicles(ArrayList<VehicleTm> vehicles) {
        ObservableList<VehicleTm> vehiclesTms = FXCollections.observableArrayList();
        vehiclesTms.addAll(vehicles);
        tblVehicles.setItems(vehiclesTms);
    }

    public void updateTable(){
        try {
            colVehiNo.setCellValueFactory(new PropertyValueFactory<>("vehicleNo"));
            colType.setCellValueFactory(new PropertyValueFactory<>("vehicleType"));
            colNoOfSeats.setCellValueFactory(new PropertyValueFactory<>("noOfSeats"));
            colPPKM.setCellValueFactory(new PropertyValueFactory<>("pricePkm"));
            colMeterReading.setCellValueFactory(new PropertyValueFactory<>("meterReading"));
            loadAllVehicles(new ControlVehicles().getAllVehicles());
        } catch (SQLException | ClassNotFoundException throwables) {
            throwables.printStackTrace();
        }
    }

    private void setValidation(){
        validationMap.put(txtPPKM,pricePattern);
        validationMap.put(txtMeterReading,meterValuePattern);
        btnList.add(btnUpdate);
    }

    public void vehicleTextFields_Key_Released(KeyEvent keyEvent) {
        Object response = ValidationUtil.validate(validationMap,btnList);
        if (keyEvent.getCode() == KeyCode.ENTER) {
            if (response instanceof TextField) {
                TextField errorText = (TextField) response;
                errorText.requestFocus();
            }
        }
    }

}
