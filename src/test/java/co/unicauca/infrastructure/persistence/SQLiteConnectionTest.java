
package co.unicauca.infrastructure.persistence;

import org.junit.jupiter.api.Test;
import java.sql.*;
import static org.junit.jupiter.api.Assertions.*;

class SQLiteConnectionTest {
    @Test
    void connectsAndCreatesUsersTable() throws Exception {
        SQLiteConnection conn = new SQLiteConnection();
        conn.connect();
        // comprueba que exista la tabla 'users'
        Connection raw = DriverManager.getConnection("jdbc:sqlite:university.db");
        try (PreparedStatement ps = raw.prepareStatement(
            "SELECT name FROM sqlite_master WHERE type='table' AND name='users'");
             ResultSet rs = ps.executeQuery()) {
            assertTrue(rs.next(), "Tabla 'users' no existe");
        } finally {
            raw.close();
            conn.disconnect();
        }
    }
}
