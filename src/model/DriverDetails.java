package model;

public class DriverDetails {
    private String driverNIC;
    private String driverAddress;
    private String drivingLicenseNO;
    private String dateOfBirth;
    private int driverContactNo;

    public DriverDetails() {
    }

    public DriverDetails(String driverNIC, String driverAddress, String drivingLicenseNO, String dateOfBirth, int driverContactNo) {
        this.driverNIC = driverNIC;
        this.driverAddress = driverAddress;
        this.drivingLicenseNO = drivingLicenseNO;
        this.dateOfBirth = dateOfBirth;
        this.driverContactNo = driverContactNo;
    }

    public String getDriverNIC() {
        return driverNIC;
    }

    public void setDriverNIC(String driverNIC) {
        this.driverNIC = driverNIC;
    }

    public String getDriverAddress() {
        return driverAddress;
    }

    public void setDriverAddress(String driverAddress) {
        this.driverAddress = driverAddress;
    }

    public String getDrivingLicenseNO() {
        return drivingLicenseNO;
    }

    public void setDrivingLicenseNO(String drivingLicenseNO) {
        this.drivingLicenseNO = drivingLicenseNO;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public int getDriverContactNo() {
        return driverContactNo;
    }

    public void setDriverContactNo(int driverContactNo) {
        this.driverContactNo = driverContactNo;
    }
}
