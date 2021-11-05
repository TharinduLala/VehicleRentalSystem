package controller;

import com.jfoenix.controls.JFXButton;
import controller.DatabaseControllers.ControlCustomers;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import model.Customer;
import utill.AlertUtil;
import utill.ValidationUtil;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.regex.Pattern;

public class CustomersFormController {
    public AnchorPane customerFormAP;
    public TextField txtCusNIC;
    public JFXButton btnSearch;
    public TextField txtContact;
    public TextField txtAddress;
    public TextField txtName;
    public JFXButton btnClear;
    public JFXButton btnRemove;
    public JFXButton btnUpdate;
    public TableView<Customer> tblCustomers;
    public TableColumn<Object, Object> colNIC;
    public TableColumn<Object, Object> colName;
    public TableColumn<Object, Object> colAddress;
    public TableColumn<Object, Object> colContact;
    public JFXButton btnAddNew;
    public TextField txtSearchCusNIC;
    LinkedHashMap<TextField, Pattern> validationMap=new LinkedHashMap<>();
    ArrayList<JFXButton>btnList=new ArrayList<>();
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
            if(new ControlCustomers().alreadyExists(txtCusNIC.getText())) {
                new Alert(Alert.AlertType.INFORMATION, "Customer Already Added.....").showAndWait();
            }else {
                Customer customer = new Customer(
                        txtCusNIC.getText(),txtName.getText(),txtAddress.getText(),Integer.parseInt(txtContact.getText())
                );
                if (new ControlCustomers().addNewCustomer(customer)){
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
            if(new ControlCustomers().alreadyExists(txtCusNIC.getText())) {
                Customer customer = new Customer(
                        txtCusNIC.getText(),txtName.getText(),txtAddress.getText(),Integer.parseInt(txtContact.getText())
                );
                if (new ControlCustomers().updateCustomer(customer)){
                    new Alert(Alert.AlertType.INFORMATION, "Saved.....").showAndWait();
                    btnClear_Action();
                    btnAddNew.setDisable(true);
                    btnUpdate.setDisable(true);
                    updateTable();
                } else {
                    new Alert(Alert.AlertType.INFORMATION, "Try Again.....").showAndWait();
                }
            }else {
                new Alert(Alert.AlertType.INFORMATION, "Customer not exist.....").showAndWait();
            }
        } catch (SQLException | ClassNotFoundException throwables) {
            throwables.printStackTrace();
        }
    }

    public void btnRemove_Action() {
        if (txtCusNIC.getText().equals("")){
            new Alert(Alert.AlertType.INFORMATION, "Please Select Customer.....").showAndWait();
        }else {
            if (new AlertUtil().getAlert("Do You want to Remove this Customer")) {
                try {
                    if (new ControlCustomers().removeCustomer(txtCusNIC.getText())) {
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

    public void btnClear_Action() {
        txtSearchCusNIC.setText((""));
        txtCusNIC.setText((""));
        txtName.setText((""));
        txtAddress.setText((""));
        txtContact.setText((""));
        txtCusNIC.setEditable(true);
    }

    public void btnSearch_Action() {
        try {
            if (txtSearchCusNIC.getText().equals("")){
                new Alert(Alert.AlertType.INFORMATION, "Please Enter Customer NIC.....").showAndWait();
            }else if (new ControlCustomers().alreadyExists(txtSearchCusNIC.getText())){
                Customer customer=new ControlCustomers().getCustomer(txtSearchCusNIC.getText());
                txtCusNIC.setText(customer.getCustomerNIC());
                txtName.setText(customer.getCustomerName());
                txtAddress.setText(customer.getCustomerAddress());
                txtContact.setText(String.valueOf(customer.getCustomerContactNo()));
                txtCusNIC.setEditable(false);
            }else {
                new Alert(Alert.AlertType.INFORMATION, "Customer not exist.....").showAndWait();
            }
        } catch (SQLException | ClassNotFoundException throwables) {
            throwables.printStackTrace();
        }
    }

    private void setValidation(){
        validationMap.put(txtCusNIC,nicPattern);
        validationMap.put(txtName,namePattern);
        validationMap.put(txtContact,contactPattern);
        validationMap.put(txtAddress,addressPattern);
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

    private void loadAllCustomer(ArrayList<Customer> customers) {
        ObservableList<Customer> customerObservableList = FXCollections.observableArrayList();
        customerObservableList.addAll(customers);
        tblCustomers.setItems(customerObservableList);
    }

    private void updateTable(){
        try {
            colNIC.setCellValueFactory(new PropertyValueFactory<>("customerNIC"));
            colName.setCellValueFactory(new PropertyValueFactory<>("customerName"));
            colAddress.setCellValueFactory(new PropertyValueFactory<>("customerAddress"));
            colContact.setCellValueFactory(new PropertyValueFactory<>("customerContactNo"));
            loadAllCustomer(new ControlCustomers().getAllCustomers());
        } catch (SQLException | ClassNotFoundException throwables) {
            throwables.printStackTrace();
        }
    }
}
