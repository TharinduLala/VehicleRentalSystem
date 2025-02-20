package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DbConnection {
    private static DbConnection dbConnection=null;
    private final Connection connection;

    private DbConnection() throws ClassNotFoundException, SQLException {
        Class.forName("com.mysql.cj.jdbc.Driver");
        connection= DriverManager.getConnection(
                "jdbc:mysql://127.0.0.1:3306/vehicle_rental_system",
                "root",
                "1234"
        );
    }
    public static DbConnection getInstance() throws SQLException, ClassNotFoundException {
        if(dbConnection==null){
            dbConnection=new DbConnection();
        }
        return dbConnection;
    }
    public Connection getConnection(){
        return connection;
    }
}
