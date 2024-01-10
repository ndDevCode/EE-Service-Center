package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Objects;

public class DBConnection {
    private static DBConnection dbConnection;
    private static Connection connection;

    private DBConnection() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/service_center",
                    "root",
                    "1234");
        } catch (ClassNotFoundException | SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static DBConnection getInstance(){
        return Objects.requireNonNullElseGet(dbConnection, () -> (dbConnection = new DBConnection()));
    }

    public static Connection getConnection() {
        return connection;
    }
}
