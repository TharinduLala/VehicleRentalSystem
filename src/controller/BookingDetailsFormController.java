package controller;

import com.jfoenix.controls.JFXButton;
import controller.DatabaseControllers.ControlBookings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import model.Booking;
import utill.AlertUtil;
import view.tm.BookingTm;
import java.sql.SQLException;
import java.util.ArrayList;

public class BookingDetailsFormController {
    public AnchorPane BookingsDetailsFormAP;
    public TextField txtBookingID;
    public JFXButton btnSearch;
    public JFXButton btnClear;
    public JFXButton btnRemove;
    public Label lblCusNIC;
    public Label lblBookedDate;
    public TableView<BookingTm> tblBookings;
    public TableColumn<Object, Object> colBookingID;
    public TableColumn<Object, Object> colVehicleNo;
    public TableColumn<Object, Object> colVehicleType;
    public TableColumn<Object, Object> colDriverID;
    public TableColumn<Object, Object> colCusID;
    public TableColumn<Object, Object> colBookedDate;
    public TableColumn<Object, Object> colStartMeterValue;
    public Label lblPaymentStatus;
    public Label lblDriverId;
    public Label  lblVehicleNo;
    public ComboBox<String> cmbBookings;
    private final ObservableList<String> bookingType = FXCollections.observableArrayList("Not Paid","Paid");

    public void initialize(){
        cmbBookings.setItems(bookingType);
        cmbBookings.getSelectionModel().selectedItemProperty().
           addListener((observable, oldValue, newValue) -> updateTable(newValue)
        );
    }

    public void btnRemove_Action() {
        if (txtBookingID.getText().equals("")){
            new Alert(Alert.AlertType.INFORMATION, "Please Select Booking.....").showAndWait();
        }else {
            if(lblPaymentStatus.getText().equals("Paid")){
                new Alert(Alert.AlertType.INFORMATION,"You Can't Remove Paid Bookings.....").showAndWait();
            }else {
                if (new AlertUtil().getAlert("Do You want to Remove this Booking")) {
                    if (new ControlBookings().removeBooking(txtBookingID.getText(),lblVehicleNo.getText(),lblDriverId.getText())) {
                        new Alert(Alert.AlertType.INFORMATION, "Removed.....").showAndWait();
                        btnClear_Action();
                    } else {
                        new Alert(Alert.AlertType.INFORMATION, "Try Again.....").showAndWait();
                    }
                    updateTable("Not Paid");
                }
            }
        }
    }

    public void btnClear_Action() {
        txtBookingID.setText("");
        lblDriverId.setText("");
        lblVehicleNo.setText("");
        lblBookedDate.setText("");
        lblCusNIC.setText("");
        lblPaymentStatus.setText("");
        txtBookingID.setEditable(true);
    }

    public void btnSearch_Action() {
        try {
            if (txtBookingID.getText().equals("")){
                new Alert(Alert.AlertType.INFORMATION, "Please Enter Booking ID.....").showAndWait();
            }else if (new ControlBookings().alreadyExists(txtBookingID.getText())){
                Booking booking=new ControlBookings().getBooking(txtBookingID.getText());
                lblCusNIC.setText(booking.getCustomerNIC());
                lblBookedDate.setText(booking.getBookedDate());
                lblVehicleNo.setText(booking.getVehicleNo());
                lblDriverId.setText(booking.getDriverNIC());
                String temp=booking.getIsPaid();
                if (temp.equals("no")){
                    lblPaymentStatus.setText("Not Paid");
                }else if (temp.equals("yes")){
                    lblPaymentStatus.setText("Paid");
                }
                txtBookingID.setEditable(false);
            }else {
                new Alert(Alert.AlertType.INFORMATION, "Booking not exist.....").showAndWait();
            }
        } catch (SQLException | ClassNotFoundException throwables) {
            throwables.printStackTrace();
        }
    }

    private void loadBookings(ArrayList<BookingTm> bookings) {
        ObservableList<BookingTm> bookingTms = FXCollections.observableArrayList();
        bookingTms.addAll(bookings);
        tblBookings.setItems(bookingTms);
    }

    private void updateTable(String type){
        try {
            colBookingID.setCellValueFactory(new PropertyValueFactory<>("bookingID"));
            colCusID.setCellValueFactory(new PropertyValueFactory<>("customerNIC"));
            colDriverID.setCellValueFactory(new PropertyValueFactory<>("driverNIC"));
            colVehicleNo.setCellValueFactory(new PropertyValueFactory<>("vehicleNo"));
            colVehicleType.setCellValueFactory(new PropertyValueFactory<>("vehicleType"));
            colBookedDate.setCellValueFactory(new PropertyValueFactory<>("bookedDate"));
            colStartMeterValue.setCellValueFactory(new PropertyValueFactory<>("meterValue"));
            if(type.equals("Not Paid")){
               loadBookings(new ControlBookings().getAllBookings("no"));
            }else if(type.equals("Paid")){
                loadBookings(new ControlBookings().getAllBookings("yes"));
            }
        } catch (SQLException | ClassNotFoundException throwables) {
            throwables.printStackTrace();
        }
    }
}
