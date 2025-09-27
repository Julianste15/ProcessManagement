package co.unicauca.infrastructure.persistence;

import java.sql.*;
public class SQLiteConnection{
    private static final String URL = "jdbc:sqlite:university.db";
    private Connection connection; 
    public void connect() {
        try {
            if (connection == null || connection.isClosed()) {
                connection = DriverManager.getConnection(URL);
                initializeDatabase();
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al conectar con la base de datos", e);
        }
    }
    public void disconnect() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al cerrar la conexión", e);
        }
    }
    public Connection getConnection() {
        return connection;
    }
    private void initializeDatabase() {
        String sql = """
            CREATE TABLE IF NOT EXISTS users (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                names TEXT NOT NULL,
                surnames TEXT NOT NULL,
                email TEXT UNIQUE NOT NULL,
                password TEXT NOT NULL,
                telephone INTEGER,
                career TEXT NOT NULL,
                role TEXT NOT NULL
            )
            """;  
        try (Statement stmt = connection.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            throw new RuntimeException("Error al inicializar la base de datos", e);
        }
    }
}

