package model;

public class Vehicle {

    private String vehicleNo;
    private String vehicleType;
    private double pricePkm;
    private String meterReading;
    private int noOfSeats;
    private String availability;
    private String ownerID;

    public Vehicle() {
    }

    public Vehicle(String vehicleNo, String vehicleType, double pricePkm, String meterReading,
                   int noOfSeats,String ownerID ,String availability) {
        this.vehicleNo = vehicleNo;
        this.vehicleType = vehicleType;
        this.pricePkm = pricePkm;
        this.meterReading = meterReading;
        this.noOfSeats = noOfSeats;
        this.availability = availability;
        this.ownerID = ownerID;
    }

    public String getVehicleNo() {
        return vehicleNo;
    }

    public void setVehicleNo(String vehicleNo) {
        this.vehicleNo = vehicleNo;
    }

    public String getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(String vehicleType) {
        this.vehicleType = vehicleType;
    }

    public double getPricePkm() {
        return pricePkm;
    }

    public void setPricePkm(double pricePkm) {
        this.pricePkm = pricePkm;
    }

    public String getMeterReading() {
        return meterReading;
    }

    public void setMeterReading(String meterReading) {
        this.meterReading = meterReading;
    }

    public int getNoOfSeats() {
        return noOfSeats;
    }

    public void setNoOfSeats(int noOfSeats) {
        this.noOfSeats = noOfSeats;
    }

    public String getAvailability() {
        return availability;
    }

    public void setAvailability(String availability) {
        this.availability = availability;
    }

    public String getOwnerID() {
        return ownerID;
    }

    public void setOwnerID(String ownerID) {
        this.ownerID = ownerID;
    }
}
