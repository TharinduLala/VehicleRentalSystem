package model;

public class Bill {
    private String billNo;
    private String bookingID;
    private double totalDistance;
    private double totalCost;
    private String paymentType;
    private String billDate;

    public Bill() {
    }

    public Bill(String billNo, String bookingID, double totalDistance, double totalCost,String paymentType,String billDate) {
        this.billNo = billNo;
        this.bookingID = bookingID;
        this.totalDistance = totalDistance;
        this.totalCost = totalCost;
        this.paymentType=paymentType;
        this.billDate = billDate;
    }

    public String getBillNo() {
        return billNo;
    }

    public void setBillNo(String billNo) {
        this.billNo = billNo;
    }

    public String getBookingID() {
        return bookingID;
    }

    public void setBookingID(String bookingID) {
        this.bookingID = bookingID;
    }

    public double getTotalDistance() {
        return totalDistance;
    }

    public void setTotalDistance(double totalDistance) {
        this.totalDistance = totalDistance;
    }

    public double getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(double totalCost) {
        this.totalCost = totalCost;
    }

    public String getBillDate() {
        return billDate;
    }

    public void setBillDate(String billDate) {
        this.billDate = billDate;
    }

    public String getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }
}
