package controller.DatabaseControllers;

import db.DbConnection;
import model.Bill;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class ControlBills {

    public boolean addBill(Bill bill,String driverNic,String vehicleNo,String meterValue){
        Connection connection = null;
        try {
            connection = DbConnection.getInstance().getConnection();
            connection.setAutoCommit(false);
            PreparedStatement statement = connection.
                    prepareStatement("INSERT INTO Bill VALUES(?,?,?,?,?,?)");
            statement.setObject(1,bill.getBillNo());
            statement.setObject(2,bill.getBookingID());
            statement.setObject(3,bill.getTotalDistance());
            statement.setObject(4,bill.getTotalCost());
            statement.setObject(5,bill.getPaymentType());
            statement.setObject(6,bill.getBillDate());

            if (statement.executeUpdate() > 0) {
                if (new ControlVehicles().setAvailability(vehicleNo,"notInARide")&&
                    new ControlVehicles().setMeterValue(vehicleNo,meterValue)&&
                    new ControlDrivers().setAvailability(driverNic,"notInARide")&&
                    new ControlBookings().setPaymentStatus(bill.getBookingID(),"yes")){
                    connection.commit();
                    return true;
                }else{
                    connection.rollback();
                    return false;
                }
            }else{
                connection.rollback();
                return false;
            }
        } catch (SQLException | ClassNotFoundException throwables) {
            throwables.printStackTrace();
        } finally{
            try {
                if (connection != null) {
                    connection.setAutoCommit(true);
                }
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
        return false;
    }

    public ArrayList<Bill> getAllBills() throws SQLException, ClassNotFoundException {
        PreparedStatement statement = DbConnection.getInstance().getConnection().prepareStatement
                ("SELECT * FROM Bill");
        ResultSet resultSet = statement.executeQuery();
        ArrayList<Bill> bills = new ArrayList<>();
        while (resultSet.next()) {
            bills.add(new Bill(
                    resultSet.getString(1),
                    resultSet.getString(2),
                    resultSet.getDouble(3),
                    resultSet.getDouble(4),
                    resultSet.getString(5),
                    resultSet.getString(6)
            ));
        }
        return bills;
    }

    public Bill getBill(String billNo) throws SQLException, ClassNotFoundException {
        PreparedStatement statement = DbConnection.getInstance().getConnection().prepareStatement
                ("SELECT * FROM Bill WHERE billID=?");
        statement.setObject(1,billNo);
        ResultSet resultSet = statement.executeQuery();
        if (resultSet.next()) {
            return new Bill(
                    resultSet.getString(1),
                    resultSet.getString(2),
                    resultSet.getDouble(3),
                    resultSet.getDouble(4),
                    resultSet.getString(5),
                    resultSet.getString(6)
            );
        }
        else {
            return null;
        }
    }
}
