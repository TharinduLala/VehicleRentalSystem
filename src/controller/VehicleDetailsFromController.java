package controller;


import com.jfoenix.controls.JFXButton;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;


public class VehicleDetailsFromController {
    public TextField txtOwnerNic;
    public TextField txtBrand;
    public TextField txtModel;
    public TextField txtMeterValue;
    public static String ownerNic;
    public static String brand;
    public static String model;
    public static String meterValue;
    public JFXButton btnClose;
    public AnchorPane detailsFormAp;

    public void initialize(){
        txtOwnerNic.setText(ownerNic);
        txtBrand.setText(brand);
        txtModel.setText(model);
        txtMeterValue.setText(meterValue);
        ownerNic="";
        brand="";
        model="";
        meterValue="";
    }

    public void btnClose_Action() {
        Stage window = (Stage) detailsFormAp.getScene().getWindow();
        window.close();
    }
}
