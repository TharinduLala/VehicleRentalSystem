package controller.DatabaseControllers;

import db.DbConnection;
import model.VehicleOwner;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class ControlVehicleOwners {
    public boolean addNewOwner(VehicleOwner owner) throws SQLException, ClassNotFoundException {
        PreparedStatement statement = DbConnection.getInstance().getConnection().prepareStatement
                ("INSERT INTO VehicleOwner VALUES(?,?,?,?)");
        statement.setObject(1,owner.getOwnerNIC());
        statement.setObject(2,owner.getOwnerName());
        statement.setObject(3,owner.getOwnerAddress());
        statement.setObject(4,owner.getOwnerContactNo());
        return statement.executeUpdate()>0;
    }

    public boolean updateOwner(VehicleOwner owner) throws SQLException, ClassNotFoundException {
        PreparedStatement statement = DbConnection.getInstance().getConnection().prepareStatement
                ("UPDATE VehicleOwner SET OwnerName=?, OwnerAddress=?,OwnerContact=? WHERE OwnerNIC=?");
        statement.setObject(1,owner.getOwnerName());
        statement.setObject(2,owner.getOwnerAddress());
        statement.setObject(3,owner.getOwnerContactNo());
        statement.setObject(4,owner.getOwnerNIC());
        return statement.executeUpdate()>0;
    }

    public boolean removeOwner(String nic) throws SQLException, ClassNotFoundException {
        PreparedStatement statement = DbConnection.getInstance().getConnection().prepareStatement
                ("DELETE FROM VehicleOwner WHERE OwnerNIC=?");
        statement.setObject(1,nic);
        return statement.executeUpdate() > 0;
    }

    public VehicleOwner getOwner(String nic) throws SQLException, ClassNotFoundException {
        PreparedStatement statement = DbConnection.getInstance().getConnection().prepareStatement
                ("SELECT * FROM VehicleOwner WHERE OwnerNIC=?");
        statement.setObject(1, nic);
        ResultSet resultSet = statement.executeQuery();
        if (resultSet.next()) {
            return new VehicleOwner(
                    resultSet.getString(1),
                    resultSet.getString(2),
                    resultSet.getString(3),
                    resultSet.getInt(4)
            );
        } else {
            return null;
        }
    }

    public ArrayList<VehicleOwner> getAllOwners() throws SQLException, ClassNotFoundException {
        PreparedStatement statement = DbConnection.getInstance().getConnection().prepareStatement("SELECT * FROM VehicleOwner");
        ResultSet resultSet = statement.executeQuery();
        ArrayList<VehicleOwner> owners = new ArrayList<>();
        while (resultSet.next()) {
            owners.add(new VehicleOwner(
                    resultSet.getString(1),
                    resultSet.getString(2),
                    resultSet.getString(3),
                    resultSet.getInt(4)
            ));
        }
        return owners;
    }

    public boolean alreadyExists(String nic) throws SQLException, ClassNotFoundException {
        PreparedStatement statement = DbConnection.getInstance()
                .getConnection().prepareStatement("SELECT * FROM VehicleOwner WHERE OwnerNIC=?");
        statement.setObject(1,nic);
        ResultSet rst = statement.executeQuery();
        return rst.next();
    }

}
