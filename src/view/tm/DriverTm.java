package view.tm;

public class DriverTm {
    private String driverNIC;
    private String driverName;
    private String driverAddress;
    private String drivingLicenseNO;
    private String dateOfBirth;
    private int driverContactNo;

    public DriverTm() {
    }

    public DriverTm(String driverNIC, String driverName, String driverAddress,
                    String drivingLicenseNO, String dateOfBirth, int driverContactNo) {
        this.driverNIC = driverNIC;
        this.driverName = driverName;
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

    public String getDriverName() {
        return driverName;
    }

    public void setDriverName(String driverName) {
        this.driverName = driverName;
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
