package co.unicauca.data.access;

import co.unicauca.domain.entities.User;
import co.unicauca.domain.repositories.IUserRepositoriy;
import java.sql.Connection;
import java.util.Optional;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserRepository implements IUserRepositoriy {

    @Override
    public void save(User user) {
        String sql = "INSERT INTO users(first_name, last_name, phone, program, role, email, password) VALUES(?,?,?,?,?,?,?)";

        try (Connection conn = SQLiteConnection.connect(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, user.getFirstName());
            stmt.setString(2, user.getLastName());
            stmt.setString(3, user.getPhone());
            stmt.setString(4, user.getProgram());
            stmt.setString(5, user.getRole());
            stmt.setString(6, user.getEmail());
            stmt.setString(7, user.getPassword());

            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error al guardar usuario", e);
        }
    }

    @Override
    public Optional<User> findByEmail(String email) {
        String sql = "SELECT * FROM users WHERE email = ?";
        try (Connection conn = SQLiteConnection.connect(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, email);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return Optional.of(new User(
                        rs.getString("first_name"),
                        rs.getString("last_name"),
                        rs.getString("phone"),
                        rs.getString("program"),
                        rs.getString("role"),
                        rs.getString("email"),
                        rs.getString("password")));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar usuario", e);
        }
        return Optional.empty();
    }

    @Override
    public boolean existsByEmail(String email) {
        String sql = "SELECT 1 FROM users WHERE email = ? LIMIT 1";
        try (Connection conn = SQLiteConnection.connect(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            throw new RuntimeException("Error checking user existence", e);
        }
    }

    @Override
    public List<User> findAll() {
        String sql = "SELECT * FROM users ORDER BY id DESC";
        List<User> list = new ArrayList<>();
        try (Connection conn = SQLiteConnection.connect(); Statement st = conn.createStatement(); ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                list.add(mapRow(rs));
            }
            return list;
        } catch (SQLException e) {
            throw new RuntimeException("Error listing users", e);
        }
    }

    private User mapRow(ResultSet rs) throws SQLException {
        return new User(
                rs.getLong("id"),
                rs.getString("first_name"),
                rs.getString("last_name"),
                rs.getString("phone"),
                rs.getString("program"),
                rs.getString("role"),
                rs.getString("email"),
                rs.getString("password")
        );
    }
}
