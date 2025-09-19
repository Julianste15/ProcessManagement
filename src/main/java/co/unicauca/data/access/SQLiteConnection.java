package co.unicauca.data.access;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class SQLiteConnection {
    private static final String URL = "jdbc:sqlite:processManagement.db";
    
    public static Connection connect() throws SQLException {
        return DriverManager.getConnection(URL);
    }
}
