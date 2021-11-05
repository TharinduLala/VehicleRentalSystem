package controller.DatabaseControllers;

import db.DbConnection;
import model.Vehicle;
import model.VehicleRegDetails;
import view.tm.VehicleTm;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class ControlVehicles {
    public ArrayList<VehicleTm> getAllVehicles() throws SQLException, ClassNotFoundException {
        PreparedStatement statement = DbConnection.getInstance().getConnection().prepareStatement("SELECT * FROM Vehicle");
        ResultSet resultSet = statement.executeQuery();
        ArrayList<VehicleTm> vehicles = new ArrayList<>();
        while (resultSet.next()) {
            vehicles.add(new VehicleTm(
                    resultSet.getString(1),
                    resultSet.getString(2),
                    resultSet.getDouble(3),
                    resultSet.getString(4),
                    resultSet.getInt(5)
                    ));
        }
        return vehicles;
    }

    public VehicleRegDetails getVehicleRegDetails(String vehicleNo) throws SQLException, ClassNotFoundException {
        PreparedStatement statement = DbConnection.getInstance().getConnection().prepareStatement
                ("SELECT * FROM VehicleRegDetail WHERE vehicleNo=?");
        statement.setObject(1,vehicleNo);
        ResultSet resultSet = statement.executeQuery();
        if(resultSet.next()){
            return new VehicleRegDetails(
                resultSet.getString(1),
                resultSet.getString(3),
                resultSet.getString(2),
                resultSet.getString(4)
            );
        }else {
            return null;
        }
    }

    public Vehicle getVehicle(String vehicleNo) throws SQLException, ClassNotFoundException {
        PreparedStatement statement = DbConnection.getInstance().getConnection().prepareStatement
                ("SELECT * FROM Vehicle WHERE vehicleNo=?");
        statement.setObject(1,vehicleNo);
        ResultSet resultSet = statement.executeQuery();
        if(resultSet.next()){
            return new Vehicle(
                    resultSet.getString(1),
                    resultSet.getString(2),
                    resultSet.getDouble(3),
                    resultSet.getString(4),
                    resultSet.getInt(5),
                    resultSet.getString(6),
                    resultSet.getString(7)
            );
        }else {
            return null;
        }
    }

    public ArrayList<VehicleTm> getAllCategorizedVehicles(String category) throws SQLException, ClassNotFoundException {
        PreparedStatement statement = DbConnection.getInstance().getConnection().prepareStatement
                ("SELECT * FROM Vehicle WHERE availability=? ");
        statement.setObject(1, category);
        ResultSet resultSet = statement.executeQuery();
        ArrayList<VehicleTm> vehicles = new ArrayList<>();
        while (resultSet.next()) {
            vehicles.add(new VehicleTm(
                    resultSet.getString(1),
                    resultSet.getString(2),
                    resultSet.getDouble(3),
                    resultSet.getString(4),
                    resultSet.getInt(5)
                   ));
        }
        return vehicles;
    }

    public boolean addNewVehicle(Vehicle vehicle,VehicleRegDetails regDetails) {
        Connection connection = null;
        try {
            connection = DbConnection.getInstance().getConnection();
            connection.setAutoCommit(false);
            PreparedStatement statement = connection.
                    prepareStatement("INSERT INTO Vehicle VALUES(?,?,?,?,?,?,?)");

            statement.setObject(1,vehicle.getVehicleNo());
            statement.setObject(2,vehicle.getVehicleType());
            statement.setObject(3,vehicle.getPricePkm());
            statement.setObject(4,vehicle.getMeterReading());
            statement.setObject(5,vehicle.getNoOfSeats());
            statement.setObject(6,vehicle.getOwnerID());
            statement.setObject(7,vehicle.getAvailability());

            if (statement.executeUpdate() > 0) {
                if (saveVehicleRegDetails(regDetails)){
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

    public boolean updateVehicle(Vehicle vehicle) throws SQLException, ClassNotFoundException {
        PreparedStatement statement = DbConnection.getInstance().getConnection().prepareStatement
                ("UPDATE Vehicle SET ppkm=? ,meterValue=? WHERE vehicleNo=?");
        statement.setObject(1,vehicle.getPricePkm());
        statement.setObject(2,vehicle.getMeterReading());
        statement.setObject(3,vehicle.getVehicleNo());
        return statement.executeUpdate()>0;
    }

    public boolean removeVehicle(String vehicleNo) throws SQLException, ClassNotFoundException {
        PreparedStatement statement = DbConnection.getInstance().getConnection().prepareStatement
                ("DELETE FROM Vehicle WHERE vehicleNo=?");
        statement.setObject(1,vehicleNo);
        return statement.executeUpdate() > 0;
    }

    public boolean alreadyExists(String vehicleNo) throws SQLException, ClassNotFoundException {
        PreparedStatement statement = DbConnection.getInstance()
                .getConnection().prepareStatement("SELECT * FROM Vehicle WHERE vehicleNo=?");
        statement.setObject(1,vehicleNo);
        ResultSet rst = statement.executeQuery();
        return rst.next();
    }

    public boolean setAvailability(String vehicleNo,String availability) throws SQLException, ClassNotFoundException {
        PreparedStatement statement = DbConnection.getInstance().getConnection().prepareStatement
                ("UPDATE Vehicle SET availability=? WHERE vehicleNo=?");
        statement.setObject(1,availability);
        statement.setObject(2,vehicleNo);
        return statement.executeUpdate()>0;
    }

    public boolean setMeterValue(String vehicleNo,String meterValue) throws SQLException, ClassNotFoundException {
        PreparedStatement statement = DbConnection.getInstance().getConnection().prepareStatement
                ("UPDATE Vehicle SET meterValue=? WHERE vehicleNo=?");
        statement.setObject(1,meterValue);
        statement.setObject(2,vehicleNo);
        return statement.executeUpdate()>0;
    }

    public ArrayList<VehicleTm> getAvailableVehicles(String type) throws SQLException, ClassNotFoundException {
        PreparedStatement statement = DbConnection.getInstance().getConnection().prepareStatement
                ("SELECT * FROM Vehicle WHERE availability=? AND vehicleType=?");
        statement.setObject(1, "notInARide");
        statement.setObject(2, type);
        ResultSet resultSet = statement.executeQuery();
        ArrayList<VehicleTm> vehicles = new ArrayList<>();
        while (resultSet.next()) {
            vehicles.add(new VehicleTm(
                    resultSet.getString(1),
                    resultSet.getString(2),
                    resultSet.getDouble(3),
                    resultSet.getString(4),
                    resultSet.getInt(5)
            ));
        }
        return vehicles;
    }

    private boolean saveVehicleRegDetails(VehicleRegDetails regDetails) throws SQLException, ClassNotFoundException {
        PreparedStatement statement = DbConnection.getInstance().getConnection().prepareStatement
                ("INSERT INTO VehicleRegDetail VALUES(?,?,?,?)");
        statement.setObject(1,regDetails.getVehicleNo());
        statement.setObject(2,regDetails.getBrand());
        statement.setObject(3,regDetails.getModel());
        statement.setObject(4,regDetails.getMeterReadingAssigning());
        return statement.executeUpdate() > 0;
    }
}
