package controller;

import com.jfoenix.controls.JFXButton;
import controller.DatabaseControllers.ControlCustomers;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import model.Customer;
import utill.ValidationUtil;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.regex.Pattern;

public class AddNewCustomerFormController {
    public AnchorPane addCustomerFormAP;
    public JFXButton btnAdd;
    public JFXButton btnClose;
    public TextField txtContact;
    public TextField txtAddress;
    public TextField txtName;
    public TextField txtCusNIC;
    LinkedHashMap<TextField, Pattern> validationMap=new LinkedHashMap<>();
    ArrayList<JFXButton> btnList=new ArrayList<>();
    Pattern nicPattern=Pattern.compile("^((19|20)\\d\\d[0-9]{8})|([5-9][0-9]{8}[vV])$");
    Pattern namePattern=Pattern.compile("^[A-z ]{3,30}$");
    Pattern addressPattern=Pattern.compile("^[0-9A-z\\s,]+$");
    Pattern contactPattern=Pattern.compile("^[1-9][0-9]{8}$");

    public void initialize(){
        setValidation();
        btnAdd.setDisable(true);
    }

    public void btnAdd_Action() {
        try {
            if(new ControlCustomers().alreadyExists(txtCusNIC.getText())) {
                new Alert(Alert.AlertType.INFORMATION, "Customer Already Added.....").showAndWait();
            }else {
                Customer customer = new Customer(
                        txtCusNIC.getText(),txtName.getText(),txtAddress.getText(),Integer.parseInt(txtContact.getText())
                );
                if (new ControlCustomers().addNewCustomer(customer)){
                    new Alert(Alert.AlertType.INFORMATION, "Saved.....").showAndWait();
                    btnClose_Action();
                } else {
                    new Alert(Alert.AlertType.INFORMATION, "Try Again.....").showAndWait();
                }
            }
        } catch (SQLException | ClassNotFoundException throwables) {
            throwables.printStackTrace();
        }
    }

    public void btnClose_Action() {
        Stage window = (Stage) addCustomerFormAP.getScene().getWindow();
        window.close();
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

    private void setValidation(){
        validationMap.put(txtCusNIC,nicPattern);
        validationMap.put(txtName,namePattern);
        validationMap.put(txtAddress,addressPattern);
        validationMap.put(txtContact,contactPattern);
        btnList.add(btnAdd);
    }

}
