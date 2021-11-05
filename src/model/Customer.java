package model;

public class Customer {

    private String customerNIC;
    private String customerName;
    private String customerAddress;
    private int customerContactNo;

    public Customer() {
    }

    public Customer(String customerNIC, String customerName, String customerAddress, int customerContactNo) {
        this.customerNIC = customerNIC;
        this.customerName = customerName;
        this.customerAddress = customerAddress;
        this.customerContactNo = customerContactNo;
    }

    public String getCustomerNIC() {
        return customerNIC;
    }

    public void setCustomerNIC(String customerNIC) {
        this.customerNIC = customerNIC;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerAddress() {
        return customerAddress;
    }

    public void setCustomerAddress(String customerAddress) {
        this.customerAddress = customerAddress;
    }

    public int getCustomerContactNo() {
        return customerContactNo;
    }

    public void setCustomerContactNo(int customerContactNo) {
        this.customerContactNo = customerContactNo;
    }
}
