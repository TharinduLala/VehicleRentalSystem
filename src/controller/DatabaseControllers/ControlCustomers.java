package controller.DatabaseControllers;

import db.DbConnection;
import model.Customer;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class ControlCustomers {
    public boolean addNewCustomer(Customer customer) throws SQLException, ClassNotFoundException {
        PreparedStatement statement = DbConnection.getInstance().getConnection().prepareStatement
                ("INSERT INTO Customer VALUES(?,?,?,?)");
        statement.setObject(1,customer.getCustomerNIC());
        statement.setObject(2,customer.getCustomerName());
        statement.setObject(3,customer.getCustomerAddress());
        statement.setObject(4,customer.getCustomerContactNo());
        return statement.executeUpdate()>0;
    }

    public boolean updateCustomer(Customer customer) throws SQLException, ClassNotFoundException {
        PreparedStatement statement = DbConnection.getInstance().getConnection().prepareStatement
                ("UPDATE Customer SET CustName=?, CustAddress=?, CustContact=? WHERE CustNIC=?");
        statement.setObject(1,customer.getCustomerName());
        statement.setObject(2,customer.getCustomerAddress());
        statement.setObject(3,customer.getCustomerContactNo());
        statement.setObject(4,customer.getCustomerNIC());
        return statement.executeUpdate()>0;
    }

    public boolean removeCustomer(String nic) throws SQLException, ClassNotFoundException {
        PreparedStatement statement = DbConnection.getInstance().getConnection().prepareStatement
                ("DELETE FROM Customer WHERE CustNIC=?");
        statement.setObject(1,nic);
        return statement.executeUpdate() > 0;
    }

    public Customer getCustomer(String nic) throws SQLException, ClassNotFoundException {
        PreparedStatement statement = DbConnection.getInstance().getConnection().prepareStatement
                ("SELECT * FROM Customer WHERE CustNIC=?");
        statement.setObject(1, nic);
        ResultSet resultSet = statement.executeQuery();
        if (resultSet.next()) {
            return new Customer(
                    resultSet.getString(1),
                    resultSet.getString(2),
                    resultSet.getString(3),
                    resultSet.getInt(4)
            );
        } else {
            return null;
        }
    }

    public ArrayList<Customer> getAllCustomers() throws SQLException, ClassNotFoundException {
        PreparedStatement statement = DbConnection.getInstance().getConnection().prepareStatement("SELECT * FROM Customer");
        ResultSet resultSet = statement.executeQuery();
        ArrayList<Customer> customers = new ArrayList<>();
        while (resultSet.next()) {
            customers.add(new Customer(
                    resultSet.getString(1),
                    resultSet.getString(2),
                    resultSet.getString(3),
                    resultSet.getInt(4)
            ));
        }
        return customers;
    }

    public boolean alreadyExists(String nic) throws SQLException, ClassNotFoundException {
        PreparedStatement statement = DbConnection.getInstance()
                .getConnection().prepareStatement("SELECT * FROM Customer WHERE CustNIC=?");
        statement.setObject(1,nic);
        ResultSet rst = statement.executeQuery();
        return rst.next();
    }
}
