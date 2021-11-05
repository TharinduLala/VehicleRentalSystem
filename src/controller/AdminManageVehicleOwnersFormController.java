package controller;

import com.jfoenix.controls.JFXButton;
import controller.DatabaseControllers.ControlVehicleOwners;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import model.VehicleOwner;
import utill.AlertUtil;
import utill.ValidationUtil;


import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.regex.Pattern;

public class AdminManageVehicleOwnersFormController {
    public AnchorPane ownersFormAP;
    public JFXButton btnClear;
    public TextField txtSearchOwnerNIC;
    public JFXButton btnSearch;
    public TextField txtOwnerContact;
    public TextField txtOwnerAddress;
    public TextField txtOwnerName;
    public JFXButton btnRemove;
    public JFXButton btnUpdate;
    public TableView<VehicleOwner> tblOwners;
    public TableColumn<Object, Object> colNIC;
    public TableColumn<Object, Object> colName;
    public TableColumn<Object, Object> colAddress;
    public TableColumn<Object, Object> colContact;
    public JFXButton btnAddNew;
    public TextField txtOwnerNIC;

    LinkedHashMap<TextField, Pattern> validationMap=new LinkedHashMap<>();
    ArrayList<JFXButton> btnList=new ArrayList<>();
    Pattern nicPattern=Pattern.compile("^((19|20)\\d\\d[0-9]{8})|([5-9][0-9]{8}[vV])$");
    Pattern namePattern=Pattern.compile("^[A-z ]{3,30}$");
    Pattern addressPattern=Pattern.compile("^[0-9A-z\\s,]+$");
    Pattern contactPattern=Pattern.compile("^[1-9][0-9]{8}$");


    public void initialize(){
        btnAddNew.setDisable(true);
        btnUpdate.setDisable(true);
        setValidation();
        updateTable();
    }

    public void btnAddNew_Action() {
        try {
            if(new ControlVehicleOwners().alreadyExists(txtOwnerNIC.getText())) {
                new Alert(Alert.AlertType.INFORMATION, "Owner Already Added.....").showAndWait();
            }else {
                VehicleOwner owner = new VehicleOwner(
                        txtOwnerNIC.getText(),txtOwnerName.getText(),txtOwnerAddress.getText(),Integer.parseInt(txtOwnerContact.getText())
                );
                if (new ControlVehicleOwners().addNewOwner(owner)){
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

    public void btnUpdate_Action() {
        try {
            if(new ControlVehicleOwners().alreadyExists(txtOwnerNIC.getText())) {
                VehicleOwner owner = new VehicleOwner(
                        txtOwnerNIC.getText(),txtOwnerName.getText(),txtOwnerAddress.getText(),Integer.parseInt(txtOwnerContact.getText())
                );
                if (new ControlVehicleOwners().updateOwner(owner)){
                    new Alert(Alert.AlertType.INFORMATION, "Updated.....").showAndWait();
                    btnClear_Action();
                    btnAddNew.setDisable(true);
                    btnUpdate.setDisable(true);
                    updateTable();
                } else {
                    new Alert(Alert.AlertType.INFORMATION, "Try Again.....").showAndWait();
                }
            }else {
                new Alert(Alert.AlertType.INFORMATION, "Owner not exist.....").showAndWait();
            }
        } catch (SQLException | ClassNotFoundException throwables) {
            throwables.printStackTrace();
        }
    }

    public void btnRemove_Action() {
        if (txtOwnerNIC.getText().equals("")){
            new Alert(Alert.AlertType.INFORMATION, "Please Select Owner.....").showAndWait();
        }else {
            if (new AlertUtil().getAlert("Do You want to Remove this Owner")) {
                try {
                    if (new ControlVehicleOwners().removeOwner(txtOwnerNIC.getText())) {
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

    public void btnSearch_Action() {
        try {
            if (txtSearchOwnerNIC.getText().equals("")){
                new Alert(Alert.AlertType.INFORMATION, "Please Enter Owner NIC.....").showAndWait();
            }else if (new ControlVehicleOwners().alreadyExists(txtSearchOwnerNIC.getText())){
                VehicleOwner owner=new ControlVehicleOwners().getOwner(txtSearchOwnerNIC.getText());
                txtOwnerNIC.setText(owner.getOwnerNIC());
                txtOwnerName.setText(owner.getOwnerName());
                txtOwnerAddress.setText(owner.getOwnerAddress());
                txtOwnerContact.setText(String.valueOf(owner.getOwnerContactNo()));
                txtOwnerNIC.setEditable(false);
            }else {
                new Alert(Alert.AlertType.INFORMATION, "Owner not exist.....").showAndWait();
            }
        } catch (SQLException | ClassNotFoundException throwables) {
            throwables.printStackTrace();
        }
    }

    public void btnClear_Action() {
        txtSearchOwnerNIC.setText((""));
        txtOwnerNIC.setText((""));
        txtOwnerName.setText((""));
        txtOwnerAddress.setText((""));
        txtOwnerContact.setText((""));
        txtOwnerNIC.setEditable(true);
    }

    private void setValidation(){
        validationMap.put(txtOwnerNIC,nicPattern);
        validationMap.put(txtOwnerName,namePattern);
        validationMap.put(txtOwnerContact,contactPattern);
        validationMap.put(txtOwnerAddress,addressPattern);
        btnList.add(btnAddNew);
        btnList.add(btnUpdate);
    }

    public void textFields_Key_Released(KeyEvent keyEvent) {
        Object response = ValidationUtil.validate(validationMap,btnList);
        if (keyEvent.getCode() == KeyCode.ENTER) {
            if (response instanceof TextField) {
                TextField errorText = (TextField) response;
                errorText.requestFocus();
            }
        }
    }

    private void loadAllOwners(ArrayList<VehicleOwner> owners) {
        ObservableList<VehicleOwner> ownerObservableList = FXCollections.observableArrayList();
        for (VehicleOwner owner:owners
        ) {
            ownerObservableList.add(new VehicleOwner(
                    owner.getOwnerNIC(),owner.getOwnerName(),owner.getOwnerAddress(),owner.getOwnerContactNo()
            ));
        }
        tblOwners.setItems(ownerObservableList);
    }

    private void updateTable(){
        try {
            colNIC.setCellValueFactory(new PropertyValueFactory<>("ownerNIC"));
            colName.setCellValueFactory(new PropertyValueFactory<>("ownerName"));
            colAddress.setCellValueFactory(new PropertyValueFactory<>("ownerAddress"));
            colContact.setCellValueFactory(new PropertyValueFactory<>("ownerContactNo"));
            loadAllOwners(new ControlVehicleOwners().getAllOwners());
        } catch (SQLException | ClassNotFoundException throwables) {
            throwables.printStackTrace();
        }
    }
}
