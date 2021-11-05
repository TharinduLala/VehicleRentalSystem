package controller.DatabaseControllers;

import db.DbConnection;
import model.User;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class ControlUsers {

    public boolean addNewUser(User user) throws SQLException, ClassNotFoundException {
        PreparedStatement statement = DbConnection.getInstance().getConnection().prepareStatement
                ("INSERT INTO LogInDetails VALUES(?,?,?,?,?)");
        statement.setObject(1,user.getNic());
        statement.setObject(2,user.getName());
        statement.setObject(3,user.getUserName());
        statement.setObject(4,user.getPassword());
        statement.setObject(5,user.getRole());
        return statement.executeUpdate()>0;
    }

    public boolean removeUser(String nic) throws SQLException, ClassNotFoundException {
        PreparedStatement statement = DbConnection.getInstance().getConnection().prepareStatement
                ("DELETE FROM LogInDetails WHERE nic=?");
        statement.setObject(1,nic);
        return statement.executeUpdate() > 0;
    }

    public User getUser(String nic) throws SQLException, ClassNotFoundException {
        PreparedStatement statement = DbConnection.getInstance().getConnection().prepareStatement
                ("SELECT * FROM LogInDetails WHERE nic=?");
        statement.setObject(1, nic);
        ResultSet resultSet = statement.executeQuery();
        if (resultSet.next()) {
            return new User(
                    resultSet.getString(1),
                    resultSet.getString(2),
                    resultSet.getString(3),
                    resultSet.getString(4),
                    resultSet.getString(5)
            );
        } else {
            return null;
        }
    }

    public ArrayList<User> getAllUsers() throws SQLException, ClassNotFoundException {
        PreparedStatement statement = DbConnection.getInstance().getConnection().prepareStatement("SELECT * FROM LogInDetails");
        ResultSet resultSet = statement.executeQuery();
        ArrayList<User> users = new ArrayList<>();
        while (resultSet.next()) {
            users.add(new User(
                    resultSet.getString(1),
                    resultSet.getString(2),
                    resultSet.getString(3),
                    resultSet.getString(4),
                    resultSet.getString(5)
            ));
        }
        return users;
    }

    public boolean alreadyExists(String nic) throws SQLException, ClassNotFoundException {
        PreparedStatement statement = DbConnection.getInstance()
                .getConnection().prepareStatement("SELECT * FROM LogInDetails WHERE nic=?");
        statement.setObject(1,nic);
        ResultSet rst = statement.executeQuery();
        return rst.next();
    }

    public boolean isAddedUserName(String userName) throws SQLException, ClassNotFoundException {
        PreparedStatement statement = DbConnection.getInstance()
                .getConnection().prepareStatement("SELECT * FROM LogInDetails WHERE userName=?");
        statement.setObject(1,userName);
        ResultSet rst = statement.executeQuery();
        return rst.next();
    }

    public User getByUserName(String userName) throws SQLException, ClassNotFoundException {
        PreparedStatement statement = DbConnection.getInstance().getConnection().prepareStatement
                ("SELECT * FROM LogInDetails WHERE userName=?");
        statement.setObject(1, userName);
        ResultSet resultSet = statement.executeQuery();
        if (resultSet.next()) {
            return new User(
                    resultSet.getString(1),
                    resultSet.getString(2),
                    resultSet.getString(3),
                    resultSet.getString(4),
                    resultSet.getString(5)
            );
        } else {
            return null;
        }
    }
}
