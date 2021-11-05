package controller.DatabaseControllers;

import db.DbConnection;
import model.Driver;
import model.DriverDetails;
import view.tm.DriverTm;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class ControlDrivers {

    public ArrayList<DriverTm> getAllDrivers() throws SQLException, ClassNotFoundException {
        PreparedStatement statement = DbConnection.getInstance().getConnection().prepareStatement("SELECT * FROM Driver");
        ResultSet resultSet = statement.executeQuery();
        ArrayList<DriverTm> drivers = new ArrayList<>();
        while (resultSet.next()) {
            DriverDetails driverDetails=getDriverDetails(resultSet.getString(1));
            assert driverDetails != null;
            drivers.add(new DriverTm(
                    resultSet.getString(1),
                    resultSet.getString(2),
                    driverDetails.getDriverAddress(),
                    driverDetails.getDrivingLicenseNO(),
                    driverDetails.getDateOfBirth(),
                    driverDetails.getDriverContactNo()));
        }
        return drivers;
    }

    public ArrayList<DriverTm> getAllCategorizedDrivers(String category) throws SQLException, ClassNotFoundException {
        PreparedStatement statement = DbConnection.getInstance().getConnection().prepareStatement
                ("SELECT * FROM Driver WHERE availability=? ");
        statement.setObject(1, category);
        ResultSet resultSet = statement.executeQuery();
        ArrayList<DriverTm> drivers = new ArrayList<>();
        while (resultSet.next()) {
            DriverDetails driverDetails=getDriverDetails(resultSet.getString(1));
            assert driverDetails != null;
            drivers.add(new DriverTm(
                    resultSet.getString(1),
                    resultSet.getString(2),
                    driverDetails.getDriverAddress(),
                    driverDetails.getDrivingLicenseNO(),
                    driverDetails.getDateOfBirth(),
                    driverDetails.getDriverContactNo()));
        }
        return drivers;
    }

    public DriverTm getDriverWithDetails(String nic) throws SQLException, ClassNotFoundException {
        PreparedStatement statement = DbConnection.getInstance().getConnection().prepareStatement
                ("SELECT * FROM Driver WHERE driverNIC=?");
        statement.setObject(1,nic);
        ResultSet resultSet = statement.executeQuery();
        DriverTm driver=null;
        if (resultSet.next()) {
            DriverDetails driverDetails=getDriverDetails(nic);
            assert driverDetails != null;
            driver=new DriverTm(
                    resultSet.getString(1),
                    resultSet.getString(2),
                    driverDetails.getDriverAddress(),
                    driverDetails.getDrivingLicenseNO(),
                    driverDetails.getDateOfBirth(),
                    driverDetails.getDriverContactNo()
            );
        }
        return driver;
    }

    public Driver getDriver(String nic) throws SQLException, ClassNotFoundException {
        PreparedStatement statement = DbConnection.getInstance().getConnection().prepareStatement
                ("SELECT * FROM Driver WHERE driverNIC=?");
        statement.setObject(1, nic);
        ResultSet resultSet = statement.executeQuery();
        if (resultSet.next()) {
            return new Driver(
                    resultSet.getString(1),
                    resultSet.getString(2),
                    resultSet.getString(3)
            );
        } else {
            return null;
        }
    }

    public ArrayList<String> getDriversName(String category) throws SQLException, ClassNotFoundException {
        ArrayList<String> drivers = new ArrayList<>();
        PreparedStatement statement = DbConnection.getInstance().getConnection().prepareStatement
                ("SELECT driverName FROM Driver WHERE availability=?");
        statement.setObject(1, category);
        ResultSet resultSet = statement.executeQuery();
        while (resultSet.next()) {
            drivers.add(resultSet.getString(1));
        }
        return drivers;
    }

    public String getDriverNic(String name) throws SQLException, ClassNotFoundException {
        PreparedStatement statement = DbConnection.getInstance().getConnection().prepareStatement
                ("SELECT driverNIC FROM Driver WHERE driverName=?");
        statement.setObject(1, name);
        ResultSet resultSet = statement.executeQuery();
        if(resultSet.next()) {
           return resultSet.getString(1);
        }else {
            return null;
        }
    }

    public boolean alreadyExists(String nic) throws SQLException, ClassNotFoundException {
        PreparedStatement statement = DbConnection.getInstance()
                .getConnection().prepareStatement("SELECT * FROM Driver WHERE driverNIC=?");
        statement.setObject(1,nic);
        ResultSet rst = statement.executeQuery();
        return rst.next();
    }

    public boolean removeDriver(String nic) throws SQLException, ClassNotFoundException {
        PreparedStatement statement = DbConnection.getInstance().getConnection().prepareStatement
                ("DELETE FROM Driver WHERE driverNIC=?");
        statement.setObject(1,nic);
        return statement.executeUpdate() > 0;
    }

    public boolean addNewDriver(Driver driver,DriverDetails driverDetails) {
        Connection connection = null;
        try {
            connection = DbConnection.getInstance().getConnection();
            connection.setAutoCommit(false);
            PreparedStatement statement = connection.
                    prepareStatement("INSERT INTO Driver VALUES(?,?,?)");

            statement.setObject(1, driver.getDriverNIC());
            statement.setObject(2, driver.getDriverName());
            statement.setObject(3, driver.getAvailability());

            if (statement.executeUpdate() > 0) {
                if (saveDriverDetails(driverDetails)){
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

    public boolean updateDriver(Driver driver,DriverDetails driverDetails) {
        Connection connection = null;
        try {
            connection = DbConnection.getInstance().getConnection();
            connection.setAutoCommit(false);
            PreparedStatement statement = connection.
                    prepareStatement("UPDATE Driver SET driverName=?, availability=?WHERE driverNIC=?");

            statement.setObject(1,driver.getDriverName() );
            statement.setObject(2, driver.getAvailability());
            statement.setObject(3, driver.getDriverNIC());

            if (statement.executeUpdate() > 0) {
                if (updateDriverDetails(driverDetails)){
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

    public boolean setAvailability(String nic,String availability) throws SQLException, ClassNotFoundException {
        PreparedStatement statement = DbConnection.getInstance().getConnection().prepareStatement
                ("UPDATE Driver SET availability=?WHERE driverNIC=?");
        statement.setObject(1,availability);
        statement.setObject(2,nic);
        return statement.executeUpdate()>0;
    }

    private DriverDetails getDriverDetails(String nic) throws SQLException, ClassNotFoundException {
        PreparedStatement statement = DbConnection.getInstance().getConnection().prepareStatement
                ("SELECT * FROM DriverDetail WHERE driverNIC=?");
        statement.setObject(1, nic);
        ResultSet resultSet = statement.executeQuery();
        if(resultSet.next()){
            return new DriverDetails(
                    nic,
                    resultSet.getString(3),
                    resultSet.getString(2),
                    resultSet.getString(5),
                    resultSet.getInt(4)
            );
        }else {
            return null;
        }
    }

    private boolean updateDriverDetails(DriverDetails driverDetails) throws SQLException, ClassNotFoundException {
        PreparedStatement statement = DbConnection.getInstance().getConnection().prepareStatement
                ("UPDATE driverDetail SET driverLicenseNo=?, driverAddress=?,driverContact=?,driverDOB=?WHERE driverNIC=?");
        statement.setObject(1,driverDetails.getDrivingLicenseNO() );
        statement.setObject(2,driverDetails.getDriverAddress());
        statement.setObject(3,driverDetails.getDriverContactNo());
        statement.setObject(4,driverDetails.getDateOfBirth());
        statement.setObject(5,driverDetails.getDriverNIC());
        return statement.executeUpdate() > 0;
    }

    private boolean saveDriverDetails(DriverDetails driverDetails) throws SQLException, ClassNotFoundException {
        PreparedStatement statement = DbConnection.getInstance().getConnection().prepareStatement
                ("INSERT INTO driverDetail VALUES(?,?,?,?,?)");
        statement.setObject(1,driverDetails.getDriverNIC() );
        statement.setObject(2,driverDetails.getDrivingLicenseNO());
        statement.setObject(3,driverDetails.getDriverAddress());
        statement.setObject(4,driverDetails.getDriverContactNo());
        statement.setObject(5,driverDetails.getDateOfBirth());
        return statement.executeUpdate() > 0;
    }

}
