package model;

public class VehicleOwner {

    private String ownerNIC;
    private String ownerName;
    private String ownerAddress;
    private int ownerContactNo;

    public VehicleOwner() {
    }

    public VehicleOwner(String ownerNIC, String ownerName, String ownerAddress, int ownerContactNo) {
        this.ownerNIC = ownerNIC;
        this.ownerName = ownerName;
        this.ownerAddress = ownerAddress;
        this.ownerContactNo = ownerContactNo;
    }

    public String getOwnerNIC() {
        return ownerNIC;
    }

    public void setOwnerNIC(String ownerNIC) {
        this.ownerNIC = ownerNIC;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public String getOwnerAddress() {
        return ownerAddress;
    }

    public void setOwnerAddress(String ownerAddress) {
        this.ownerAddress = ownerAddress;
    }

    public int getOwnerContactNo() {
        return ownerContactNo;
    }

    public void setOwnerContactNo(int ownerContactNo) {
        this.ownerContactNo = ownerContactNo;
    }

}
