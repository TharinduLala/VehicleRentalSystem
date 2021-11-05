package controller.DatabaseControllers;

import db.DbConnection;
import model.Booking;
import view.tm.BookingTm;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class ControlBookings {

    public String createBookingId() throws SQLException, ClassNotFoundException {
        ResultSet rst = DbConnection.getInstance()
                .getConnection().prepareStatement(
                        "SELECT bookingID FROM Booking ORDER BY bookingID DESC LIMIT 1"
                ).executeQuery();
        if (rst.next()){
            int tempId = Integer.
                    parseInt(rst.getString(1).split("B")[1]);
            tempId=tempId+1;
            if (tempId<9){
                return "B00"+tempId;
            }else if(tempId<99){
                return "B0"+tempId;
            }else{
                return "B"+tempId;
            }

        }else{
            return "B001";
        }
    }

    public boolean addBooking(Booking booking){
        Connection connection = null;
        try {
            connection = DbConnection.getInstance().getConnection();
            connection.setAutoCommit(false);
            PreparedStatement statement = connection.prepareStatement
                    ("INSERT INTO Booking VALUES(?,?,?,?,?,?,?,?)");

            statement.setObject(1,booking.getBookingID());
            statement.setObject(2,booking.getCustomerNIC());
            statement.setObject(3,booking.getDriverNIC());
            statement.setObject(4,booking.getVehicleNo());
            statement.setObject(5,booking.getVehicleType());
            statement.setObject(6,booking.getMeterValue());
            statement.setObject(7,booking.getBookedDate());
            statement.setObject(8,"no");

            if (statement.executeUpdate() > 0) {
                if (new ControlVehicles().setAvailability(booking.getVehicleNo(),"inARide")&&
                        new ControlVehicles().setMeterValue(booking.getVehicleNo(),booking.getMeterValue())&&
                        new ControlDrivers().setAvailability(booking.getDriverNIC(),"inARide")){
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

    public boolean removeBooking(String bookingId,String vehicleNo,String driverNic){
        Connection connection = null;
        try {
            connection = DbConnection.getInstance().getConnection();
            connection.setAutoCommit(false);
            PreparedStatement statement = connection.
                    prepareStatement("DELETE FROM Booking WHERE bookingID=?");

            statement.setObject(1,bookingId);
            if (statement.executeUpdate() > 0) {
                if (new ControlVehicles().setAvailability(vehicleNo,"notInARide")
                        &&
                        new ControlDrivers().setAvailability(driverNic,"notInARide")){
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

    public ArrayList<BookingTm>getAllBookings(String type) throws SQLException, ClassNotFoundException {
        PreparedStatement statement = DbConnection.getInstance().getConnection().prepareStatement
                ("SELECT * FROM Booking WHERE paidStatus=?");
        statement.setObject(1,type);
        ResultSet resultSet = statement.executeQuery();
        ArrayList<BookingTm> bookings = new ArrayList<>();
        while (resultSet.next()) {
            bookings.add(new BookingTm(
                    resultSet.getString(1),
                    resultSet.getString(2),
                    resultSet.getString(3),
                    resultSet.getString(4),
                    resultSet.getString(5),
                    resultSet.getString(6),
                    resultSet.getString(7)

            ));
        }
        return bookings;
    }

    public Booking getBooking(String id) throws SQLException, ClassNotFoundException {
        PreparedStatement statement = DbConnection.getInstance().getConnection().prepareStatement
                ("SELECT * FROM Booking WHERE bookingID=?");
        statement.setObject(1,id);
        ResultSet resultSet = statement.executeQuery();
        if (resultSet.next()) {
            return new Booking(
                    resultSet.getString(1),
                    resultSet.getString(2),
                    resultSet.getString(3),
                    resultSet.getString(4),
                    resultSet.getString(5),
                    resultSet.getString(6),
                    resultSet.getString(7),
                    resultSet.getString(8)
            );
        }else {
            return null;
        }
    }

    public boolean alreadyExists(String id) throws SQLException, ClassNotFoundException {
        PreparedStatement statement = DbConnection.getInstance()
                .getConnection().prepareStatement("SELECT * FROM Booking WHERE bookingID=?");
        statement.setObject(1,id);
        ResultSet rst = statement.executeQuery();
        return rst.next();
    }

    public boolean setPaymentStatus(String bookingId,String isPaid) throws SQLException, ClassNotFoundException {
        PreparedStatement statement = DbConnection.getInstance().getConnection().prepareStatement
                ("UPDATE Booking SET paidStatus=? WHERE bookingID=?");
        statement.setObject(1,isPaid);
        statement.setObject(2,bookingId);
        return statement.executeUpdate()>0;
    }
}
