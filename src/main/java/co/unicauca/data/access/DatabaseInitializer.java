package co.unicauca.data.access;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseInitializer {
    
    public static void initialize() {
        String sql = """
            CREATE TABLE IF NOT EXISTS users (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                first_name TEXT NOT NULL,
                last_name TEXT NOT NULL,
                phone TEXT,
                program TEXT NOT NULL,
                role TEXT NOT NULL,
                email TEXT UNIQUE NOT NULL,
                password TEXT NOT NULL
            );
            """;

        try (Connection conn = SQLiteConnection.connect();
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
            System.out.println("✅ Users table ready.");
        } catch (SQLException e) {
            throw new RuntimeException("Error initializing database", e);
        }
    }
}
