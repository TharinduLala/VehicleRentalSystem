package controller;

import com.jfoenix.controls.JFXButton;
import controller.DatabaseControllers.ControlBills;
import controller.DatabaseControllers.ControlBookings;
import controller.DatabaseControllers.ControlCustomers;
import controller.DatabaseControllers.ControlVehicles;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import model.Bill;
import model.Booking;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.view.JasperViewer;
import utill.AlertUtil;
import utill.ValidationUtil;

import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.regex.Pattern;


public class MakeBillFormController {
    public AnchorPane makePaymentFormAP;
    public TextField txtBookingID;
    public JFXButton btnSearch;
    public Label lblCusNIC;
    public Label lblBillID;
    public Label lblCusName;
    public Label lblVehicleNo;
    public Label lblVehicleType;
    public TextField txtEndMeterReading;
    public TextField txtStartMeterReading;
    public JFXButton btnCalculate;
    public Label lblTotalDistance;
    public Label lblTotalCost;
    public Label lblPPKM;
    public JFXButton btnProceed;
    public RadioButton radioBtnCardPayment;
    public RadioButton radioBtnCashPayment;
    private final ArrayList<Label> labels=new ArrayList<>();
    private final ArrayList<TextField> fields=new ArrayList<>();
    public Label lblPaymentType;
    public JFXButton btnCancel;
    LinkedHashMap<TextField, Pattern> validationMap=new LinkedHashMap<>();
    ArrayList<JFXButton> btnList=new ArrayList<>();
    Pattern meterValuePattern=Pattern.compile("^[0-9]{5}[.][0-9]$");
    ToggleGroup group=new ToggleGroup();


    public void initialize(){
        btnProceed.setDisable(true);
        btnCalculate.setDisable(true);
        setValidation();
        fields.add(txtBookingID);
        fields.add(txtStartMeterReading);
        fields.add(txtEndMeterReading);
        labels.add(lblCusNIC);
        labels.add(lblVehicleNo);
        radioBtnCashPayment.setToggleGroup(group);
        radioBtnCashPayment.setUserData("Cash");
        radioBtnCardPayment.setToggleGroup(group);
        radioBtnCardPayment.setUserData("Card");
        group.selectedToggleProperty().addListener((observable, oldValue, newValue) -> {
            if (group.getSelectedToggle() != null) {
                lblPaymentType.setText(group.getSelectedToggle().getUserData().toString());
            }
        });
    }

    public void btnSearch_Action() {
        try {
            if (txtBookingID.getText().equals("")){
                new Alert(Alert.AlertType.INFORMATION, "Please Enter Booking ID.....").showAndWait();
            }else if (new ControlBookings().alreadyExists(txtBookingID.getText())){
                Booking booking = new ControlBookings().getBooking(txtBookingID.getText());
                if (booking.getIsPaid().equals("yes")){
                    new Alert(Alert.AlertType.INFORMATION, "Payments have been already done.....").showAndWait();
                }else {
                    lblCusNIC.setText(booking.getCustomerNIC());
                    lblCusName.setText(new ControlCustomers().getCustomer(booking.getCustomerNIC()).getCustomerName());
                    lblVehicleNo.setText(booking.getVehicleNo());
                    lblVehicleType.setText(booking.getVehicleType());
                    lblPPKM.setText(String.valueOf(new ControlVehicles().getVehicle(booking.getVehicleNo()).getPricePkm()));
                    txtStartMeterReading.setText(booking.getMeterValue());
                    txtBookingID.setEditable(false);
                }
            }else {
                new Alert(Alert.AlertType.INFORMATION, "Booking not exist.....").showAndWait();
            }
        } catch (SQLException | ClassNotFoundException throwables) {
            throwables.printStackTrace();
        }
    }

    public void btnCalculate_Action() {
        if(new ValidationUtil().checkIsFieldsEmpty(fields)&&new ValidationUtil().checkIsLabelsEmpty(labels)){
            DecimalFormat decimalFormat = new DecimalFormat(".#");
            double startValue= Double.parseDouble(txtStartMeterReading.getText());
            double endValue= Double.parseDouble(txtEndMeterReading.getText());
            double ppkm= Double.parseDouble(lblPPKM.getText());
            double totalDistance=endValue-startValue;
            double totalCost=totalDistance*ppkm;
            lblTotalDistance.setText(String.valueOf(decimalFormat.format(totalDistance)));
            lblTotalCost.setText(decimalFormat.format(totalCost)+"0");
            btnProceed.setDisable(false);
            try {
                lblBillID.setText(setBillId());
            } catch (SQLException | ClassNotFoundException throwables) {
                throwables.printStackTrace();
            }
        }else {
            new Alert(Alert.AlertType.INFORMATION,"Please Complete Details.....").showAndWait();
        }
    }

    public void btnProceed_Action() {
        try {
            if (lblPaymentType.getText().equals("")){
                new Alert(Alert.AlertType.INFORMATION, "Select Payment Method.....").showAndWait();
            } else {
                Bill bill = new Bill(
                        lblBillID.getText(),
                        txtBookingID.getText(),
                        Double.parseDouble(lblTotalDistance.getText()),
                        Double.parseDouble(lblTotalCost.getText()),
                        lblPaymentType.getText(),
                        DashBoardController.date
                );
                String vehicleNo = lblVehicleNo.getText();
                String driverNic = new ControlBookings().getBooking(txtBookingID.getText()).getDriverNIC();
                String meterValue = txtEndMeterReading.getText();
                if (new ControlBills().addBill(bill, driverNic, vehicleNo, meterValue)) {
                    new Alert(Alert.AlertType.INFORMATION, "Success.....").showAndWait();
                    printReceipt();
                    new AlertUtil().refresh(makePaymentFormAP, "MakeBillForm");
                } else {
                    new Alert(Alert.AlertType.INFORMATION, "Try Again.....").showAndWait();
                }
            }
        } catch (SQLException | ClassNotFoundException throwables) {
            throwables.printStackTrace();
        }
    }

    private String setBillId() throws SQLException, ClassNotFoundException {
        Booking booking = new ControlBookings().getBooking(txtBookingID.getText());
        String num=booking.getBookingID().split("B")[1];
        return "#b"+num;
    }

    private void setValidation(){
        validationMap.put(txtEndMeterReading,meterValuePattern);
        btnList.add(btnCalculate);
    }

    public void addData_keyEvent(KeyEvent keyEvent) {
        Object response = ValidationUtil.validate(validationMap,btnList);
        if (keyEvent.getCode() == KeyCode.ENTER) {
            if (response instanceof TextField) {
                TextField errorText = (TextField) response;
                errorText.requestFocus();
            }
        }
    }

    private void printReceipt(){
        try {
            JasperDesign design = JRXmlLoader.load(this.getClass().getResourceAsStream("/view/reports/PaymentReceipt.jrxml"));
            JasperReport compileReport = JasperCompileManager.compileReport(design);
            HashMap<String, Object> map=new HashMap<>();
            map.put("bookingId",txtBookingID.getText());
            map.put("customerName",lblCusName.getText());
            map.put("vehicleType",lblVehicleType.getText());
            map.put("startMeterReading",txtStartMeterReading.getText());
            map.put("ppkm",lblPPKM.getText());
            map.put("endMeterReading",txtEndMeterReading.getText());
            map.put("totalDistance",lblTotalDistance.getText());
            map.put("paymentType",lblPaymentType.getText());
            map.put("totalCost",lblTotalCost.getText());
            map.put("billNo",lblBillID.getText());
            JasperPrint jasperPrint = JasperFillManager.fillReport(compileReport, map, new JREmptyDataSource(1));
            JasperViewer.viewReport(jasperPrint, false);
            //JasperPrintManager.printReport(jasperPrint,false);
        } catch (JRException e) {
            e.printStackTrace();
        }
    }

    public void btnCancel_Action() {
        new AlertUtil().refresh(makePaymentFormAP, "MakeBillForm");
    }
}
