package controller;

import com.jfoenix.controls.JFXButton;
import controller.DatabaseControllers.ControlBills;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import model.Bill;
import java.sql.SQLException;
import java.util.ArrayList;

public class BillDetailsFormController {
    public AnchorPane billDetaisFromAp;
    public TextField txtBillNo;
    public JFXButton btnSearch;
    public Label lblBookingId;
    public Label lblPaymentMethod;
    public Label lblTotalCost;
    public Label lblTotalDistance;
    public Label lblBillDate;
    public JFXButton btnClear;
    public TableView<Bill> tblBills;
    public TableColumn<String, String> colBillNo;
    public TableColumn<String, String> colBookingId;
    public TableColumn<String, String> colTotalDistance;
    public TableColumn<String, String> colTotalCost;
    public TableColumn<String, String> colPaymentMethod;
    public TableColumn<String, String> colBillDate;

    public void initialize(){
        updateTable();
    }

    public void btnClear_Action() {
        txtBillNo.setText("");
        lblBookingId.setText("");
        lblPaymentMethod.setText("");
        lblTotalCost.setText("");
        lblTotalDistance.setText("");
        lblBillDate.setText("");
        txtBillNo.setEditable(true);
    }

    public void btnSearch_Action() {
        try {
            if (txtBillNo.getText().equals("")){
                new Alert(Alert.AlertType.INFORMATION, "Please Enter Bill Number.....").showAndWait();
            }else if (new ControlBills().getBill(txtBillNo.getText())!=null){
                Bill bill = new ControlBills().getBill(txtBillNo.getText());
                lblBookingId.setText(bill.getBookingID());
                lblPaymentMethod.setText(bill.getPaymentType());
                lblTotalCost.setText(String.valueOf(bill.getTotalCost()));
                lblTotalDistance.setText(String.valueOf(bill.getTotalDistance()));
                lblBillDate.setText(bill.getBillDate());
                txtBillNo.setEditable(false);
            }else {
                new Alert(Alert.AlertType.INFORMATION, "Bill not exist.....").showAndWait();
            }
        } catch (SQLException | ClassNotFoundException throwables) {
            throwables.printStackTrace();
        }
    }

    private void loadAllBills(ArrayList<Bill> bills) {
        ObservableList<Bill> billObservableList = FXCollections.observableArrayList();
        billObservableList.addAll(bills);
        tblBills.setItems(billObservableList);
    }

    private void updateTable(){
        try {
            colBillNo.setCellValueFactory(new PropertyValueFactory<>("billNo"));
            colBookingId.setCellValueFactory(new PropertyValueFactory<>("bookingID"));
            colTotalDistance.setCellValueFactory(new PropertyValueFactory<>("totalDistance"));
            colTotalCost.setCellValueFactory(new PropertyValueFactory<>("totalCost"));
            colPaymentMethod.setCellValueFactory(new PropertyValueFactory<>("paymentType"));
            colBillDate.setCellValueFactory(new PropertyValueFactory<>("billDate"));
            loadAllBills(new ControlBills().getAllBills());
            } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
