package model;

public class Booking {

    private String bookingID;
    private String customerNIC;
    private String driverNIC;
    private String vehicleNo;
    private String bookedDate;
    private String vehicleType;
    private String meterValue;
    private String isPaid;

    public Booking() {
    }

    public Booking(String bookingID, String customerNIC, String driverNIC, String vehicleNo,
                   String vehicleType,String meterValue,String bookedDate,String isPaid) {
        this.bookingID = bookingID;
        this.customerNIC = customerNIC;
        this.driverNIC = driverNIC;
        this.vehicleNo = vehicleNo;
        this.bookedDate = bookedDate;
        this.vehicleType = vehicleType;
        this.meterValue = meterValue;
        this.isPaid = isPaid;
    }

    public String getBookingID() {
        return bookingID;
    }

    public void setBookingID(String bookingID) {
        this.bookingID = bookingID;
    }

    public String getCustomerNIC() {
        return customerNIC;
    }

    public void setCustomerNIC(String customerNIC) {
        this.customerNIC = customerNIC;
    }

    public String getDriverNIC() {
        return driverNIC;
    }

    public void setDriverNIC(String driverNIC) {
        this.driverNIC = driverNIC;
    }

    public String getVehicleNo() {
        return vehicleNo;
    }

    public void setVehicleNo(String vehicleNo) {
        this.vehicleNo = vehicleNo;
    }

    public String getBookedDate() {
        return bookedDate;
    }

    public void setBookedDate(String bookedDate) {
        this.bookedDate = bookedDate;
    }

    public String getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(String vehicleType) {
        this.vehicleType = vehicleType;
    }

    public String getMeterValue() {
        return meterValue;
    }

    public void setMeterValue(String meterValue) {
        this.meterValue = meterValue;
    }

    public String getIsPaid() {
        return isPaid;
    }

    public void setIsPaid(String isPaid) {
        this.isPaid = isPaid;
    }
}

