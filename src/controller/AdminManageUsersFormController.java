package controller;

import com.jfoenix.controls.JFXButton;
import controller.DatabaseControllers.ControlUsers;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import model.User;
import utill.AlertUtil;
import utill.ValidationUtil;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.regex.Pattern;


public class AdminManageUsersFormController {
    public AnchorPane usersFormAP;
    public JFXButton btnClear;
    public TextField txtSearchUserNic;
    public JFXButton btnSearch;
    public TextField txtUserName;
    public TextField txtName;
    public JFXButton btnRemove;
    public JFXButton btnAddNew;
    public TextField txtNic;
    public TextField txtPassword;
    public RadioButton radioBtnAdmin;
    public RadioButton radioBtnUser;
    public TableView<User> tblUsers;
    public TableColumn<String, String> colNIC;
    public TableColumn<String, String> colName;
    public TableColumn<String, String> colUserName;
    public TableColumn<String, String>colPassword;
    public TableColumn<String, String> colRole;
    public Label lblRole;
    LinkedHashMap<TextField, Pattern> validationMap=new LinkedHashMap<>();
    ArrayList<JFXButton> btnList=new ArrayList<>();
    ToggleGroup group=new ToggleGroup();
    Pattern nicPattern=Pattern.compile("^((19|20)\\d\\d[0-9]{8})|([5-9][0-9]{8}[vV])$");
    Pattern namePattern=Pattern.compile("^[A-z ]{3,30}$");
    Pattern userNamePattern=Pattern.compile("^[A-Za-z][A-Za-z0-9_]{7,29}$");
    Pattern passwordPattern=Pattern.compile("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#&()–[{}]:;',?/*~$^+=<>]).{8,20}$");


    public void initialize(){
        btnAddNew.setDisable(true);
        setValidation();
        updateTable();
        radioBtnAdmin.setToggleGroup(group);
        radioBtnAdmin.setUserData("Admin");
        radioBtnUser.setToggleGroup(group);
        radioBtnUser.setUserData("User");
        group.selectedToggleProperty().addListener((observable, oldValue, newValue) -> {
            if (group.getSelectedToggle() != null) {
                lblRole.setText(group.getSelectedToggle().getUserData().toString());
            }
        });
    }

    public void btnSearch_Action() {
        try {
            if (txtSearchUserNic.getText().equals("")){
                new Alert(Alert.AlertType.INFORMATION, "Please Enter User NIC.....").showAndWait();
            }else if (new ControlUsers().alreadyExists(txtSearchUserNic.getText())){
                User user=new ControlUsers().getUser(txtSearchUserNic.getText());
                txtNic.setText(user.getNic());
                txtName.setText(user.getName());
                txtUserName.setText(user.getUserName());
                txtPassword.setText(user.getPassword());
                lblRole.setText(user.getRole());
                txtNic.setEditable(false);
                txtName.setEditable(false);
                txtPassword.setEditable(false);
                txtUserName.setEditable(false);
                txtSearchUserNic.setEditable(false);
            }else {
                new Alert(Alert.AlertType.INFORMATION, "Customer not exist.....").showAndWait();
            }
        } catch (SQLException | ClassNotFoundException throwables) {
            throwables.printStackTrace();
        }
    }

    public void btnRemove_Action() {
        if (txtNic.getText().equals("")){
            new Alert(Alert.AlertType.INFORMATION, "Please Select user.....").showAndWait();
        }else {
            if (new AlertUtil().getAlert("Do You want to Remove this User")) {
                try {
                    if (new ControlUsers().removeUser(txtNic.getText())) {
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

    public void btnAddNew_Action() {
        try {
            if(new ControlUsers().alreadyExists(txtNic.getText())) {
                new Alert(Alert.AlertType.INFORMATION, "Customer Already Added.....").showAndWait();
            }if(new ControlUsers().isAddedUserName(txtUserName.getText())) {
                new Alert(Alert.AlertType.INFORMATION, "That user name is taken,try another.....").showAndWait();
            }if(lblRole.getText().equals("")) {
                new Alert(Alert.AlertType.INFORMATION, "Please select user type.....").showAndWait();
            }else {
                User user=new User(
                      txtNic.getText(),txtName.getText(),txtUserName.getText(),txtPassword.getText(),lblRole.getText()
                );
                if (new ControlUsers().addNewUser(user)){
                    new Alert(Alert.AlertType.INFORMATION, "Saved.....").showAndWait();
                    btnClear_Action();
                    btnAddNew.setDisable(true);
                    updateTable();
                } else {
                    new Alert(Alert.AlertType.INFORMATION, "Try Again.....").showAndWait();
                }
            }
        } catch (SQLException | ClassNotFoundException throwables) {
            throwables.printStackTrace();
        }
    }

    public void btnClear_Action() {
        txtNic.setEditable(true);
        txtName.setEditable(true);
        txtPassword.setEditable(true);
        txtUserName.setEditable(true);
        txtSearchUserNic.setEditable(true);
        txtSearchUserNic.setText("");
        txtNic.setText("");
        txtName.setText("");
        txtUserName.setText("");
        txtPassword.setText("");
        lblRole.setText("");
    }

    public void userTextFields_Key_Released(KeyEvent keyEvent) {
        Object response = ValidationUtil.validate(validationMap,btnList);
        if (keyEvent.getCode() == KeyCode.ENTER) {
            if (response instanceof TextField) {
                TextField errorText = (TextField) response;
                errorText.requestFocus();
            }
        }
    }

    private void setValidation(){
        validationMap.put(txtNic,nicPattern);
        validationMap.put(txtName,namePattern);
        validationMap.put(txtUserName,userNamePattern);
        validationMap.put(txtPassword,passwordPattern);
        btnList.add(btnAddNew);
    }

    private void loadAllUsers(ArrayList<User> users) {
        ObservableList<User> userObservableList = FXCollections.observableArrayList();
        userObservableList.addAll(users);
        tblUsers.setItems(userObservableList);
    }

    private void updateTable(){
        try {
            colNIC.setCellValueFactory(new PropertyValueFactory<>("nic"));
            colName.setCellValueFactory(new PropertyValueFactory<>("name"));
            colUserName.setCellValueFactory(new PropertyValueFactory<>("userName"));
            colPassword.setCellValueFactory(new PropertyValueFactory<>("password"));
            colRole.setCellValueFactory(new PropertyValueFactory<>("role"));
            loadAllUsers(new ControlUsers().getAllUsers());
        } catch (SQLException | ClassNotFoundException throwables) {
            throwables.printStackTrace();
        }
    }

}
