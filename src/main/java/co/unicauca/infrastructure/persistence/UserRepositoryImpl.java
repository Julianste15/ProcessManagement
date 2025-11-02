package co.unicauca.infrastructure.persistence;

import org.springframework.stereotype.Repository;
import co.unicauca.domain.entities.User;
import co.unicauca.domain.enums.Career;
import co.unicauca.domain.enums.Role;
import co.unicauca.domain.repositories.UserRepository;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
@Repository
public class UserRepositoryImpl implements UserRepository {
    private final SQLiteConnection db;
    public UserRepositoryImpl() {
        this.db = new SQLiteConnection();
    }
    @Override
    public User save(User user) {
        String sql = "INSERT INTO users(names, surnames, email, password, telephone, career, role) VALUES(?,?,?,?,?,?,?)";    
        db.connect();
        try (PreparedStatement stmt = db.getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, user.getNames());
            stmt.setString(2, user.getSurnames());
            stmt.setString(3, user.getEmail());
            stmt.setString(4, user.getPassword());
            stmt.setLong(5, user.getTelephone());
            stmt.setString(6, user.getCareer().name()); // Convert enum to string
            stmt.setString(7, user.getRole().name());   // Convert enum to string           
            int affectedRows = stmt.executeUpdate();            
            if (affectedRows > 0) {
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        user.setId(generatedKeys.getLong(1));
                    }
                }
            }
            return user;
        } catch (SQLException e) {
            throw new RuntimeException("Error al guardar usuario", e);
        } finally {
            db.disconnect();
        }
    }   
    @Override
    public User findByEmail(String email) {
        String sql = "SELECT * FROM users WHERE email = ?";
        db.connect();
        try (PreparedStatement stmt = db.getConnection().prepareStatement(sql)) {
            stmt.setString(1, email);            
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return mapRow(rs);
            }
            return null;
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar usuario", e);
        } finally {
            db.disconnect();
        }
    }    
    @Override
    public User findById(Long id) {
        String sql = "SELECT * FROM users WHERE id = ?";
        db.connect();
        try (PreparedStatement stmt = db.getConnection().prepareStatement(sql)) {
            stmt.setLong(1, id);
            
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return mapRow(rs);
            }
            return null;
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar usuario por ID", e);
        } finally {
            db.disconnect();
        }
    }    
    @Override
    public boolean delete(Long id) {
        String sql = "DELETE FROM users WHERE id = ?";
        db.connect();
        try (PreparedStatement stmt = db.getConnection().prepareStatement(sql)) {
            stmt.setLong(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Error al eliminar usuario", e);
        } finally {
            db.disconnect();
        }
    }   
    @Override
    public List<User> findAll() {
        String sql = "SELECT * FROM users ORDER BY id DESC";
        List<User> users = new ArrayList<>();
        db.connect();
        try (Statement stmt = db.getConnection().createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                users.add(mapRow(rs));
            }
            return users;
        } catch (SQLException e) {
            throw new RuntimeException("Error al listar usuarios", e);
        } finally {
            db.disconnect();
        }
    }  
    private User mapRow(ResultSet rs) throws SQLException {
        User user = new User();
        user.setId(rs.getLong("id"));
        user.setNames(rs.getString("names"));
        user.setSurnames(rs.getString("surnames"));
        user.setEmail(rs.getString("email"));
        user.setPassword(rs.getString("password"));
        user.setTelephone(rs.getLong("telephone"));
        user.setCareer(Career.valueOf(rs.getString("career")));
        user.setRole(Role.valueOf(rs.getString("role")));       
        return user;
    }
}
