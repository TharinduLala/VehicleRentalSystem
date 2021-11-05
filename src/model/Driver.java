package model;

public class Driver {

    private String driverNIC;
    private String driverName;
    private String availability;

    public Driver() {
    }

    public Driver(String driverNIC, String driverName, String availability) {
        this.driverNIC = driverNIC;
        this.driverName = driverName;
        this.availability = availability;
    }

    public String getDriverNIC() {
        return driverNIC;
    }

    public void setDriverNIC(String driverNIC) {
        this.driverNIC = driverNIC;
    }

    public String getDriverName() {
        return driverName;
    }

    public void setDriverName(String driverName) {
        this.driverName = driverName;
    }

    public String getAvailability() {
        return availability;
    }

    public void setAvailability(String availability) {
        this.availability = availability;
    }
}
