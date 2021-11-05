package model;

public class VehicleRegDetails {
    private String vehicleNo;
    private String model;
    private String brand;
    private String meterReadingAssigning;

    public VehicleRegDetails() {
    }

    public VehicleRegDetails(String vehicleNo, String model, String brand, String meterReadingAssigning) {
        this.vehicleNo = vehicleNo;
        this.model = model;
        this.brand = brand;
        this.meterReadingAssigning = meterReadingAssigning;
    }

    public String getVehicleNo() {
        return vehicleNo;
    }

    public void setVehicleNo(String vehicleNo) {
        this.vehicleNo = vehicleNo;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getMeterReadingAssigning() {
        return meterReadingAssigning;
    }

    public void setMeterReadingAssigning(String meterReadingAssigning) {
        this.meterReadingAssigning = meterReadingAssigning;
    }
}
