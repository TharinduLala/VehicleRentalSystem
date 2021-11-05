package view.tm;

public class BookingTm {

    private String bookingID;
    private String customerNIC;
    private String driverNIC;
    private String vehicleNo;
    private String bookedDate;
    private String vehicleType;
    private String meterValue;

    public BookingTm() {
    }

    public BookingTm(String bookingID, String customerNIC, String driverNIC, String vehicleNo,
                     String vehicleType, String meterValue, String bookedDate) {
        this.bookingID = bookingID;
        this.customerNIC = customerNIC;
        this.driverNIC = driverNIC;
        this.vehicleNo = vehicleNo;
        this.bookedDate = bookedDate;
        this.vehicleType = vehicleType;
        this.meterValue = meterValue;
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

    @Override
    public String toString() {
        return "BookingTm{" +
                "bookingID='" + bookingID + '\'' +
                ", customerNIC='" + customerNIC + '\'' +
                ", driverNIC='" + driverNIC + '\'' +
                ", vehicleNo='" + vehicleNo + '\'' +
                ", bookedDate='" + bookedDate + '\'' +
                ", vehicleType='" + vehicleType + '\'' +
                ", meterValue=" + meterValue +
                '}';
    }
}
