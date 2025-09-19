package co.unicauca.domain;

import co.unicauca.data.access.SQLiteConnection;
import co.unicauca.domain.entities.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import co.unicauca.data.access.UserRepository;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class UserRepositoryTest {

    private UserRepository repository;

    @BeforeEach
    void setUp() throws SQLException {
        repository = new UserRepository();

        // Limpiar y recrear la tabla antes de cada prueba
        try (Connection conn = SQLiteConnection.connect();
             Statement stmt = conn.createStatement()) {

            stmt.execute("DROP TABLE IF EXISTS users");
            stmt.execute("CREATE TABLE users (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "first_name TEXT," +
                    "last_name TEXT," +
                    "phone TEXT," +
                    "program TEXT," +
                    "role TEXT," +
                    "email TEXT UNIQUE," +
                    "password TEXT)");
        }
    }

    @Test
    void testSaveAndFindByEmail() {
        User user = new User("Juan", "Pérez", "123456", "Ingeniería", "Estudiante", "juan@example.com", "1234");

        repository.save(user);
        Optional<User> found = repository.findByEmail("juan@example.com");

        assertTrue(found.isPresent());
        assertEquals("Juan", found.get().getFirstName());
        assertEquals("Pérez", found.get().getLastName());
    }

    @Test
    void testExistsByEmail() {
        User user = new User("Ana", "García", "987654", "Medicina", "Docente", "ana@example.com", "abcd");
        repository.save(user);

        assertTrue(repository.existsByEmail("ana@example.com"));
        assertFalse(repository.existsByEmail("noexiste@example.com"));
    }

    @Test
    void testFindAll() {
        User u1 = new User("Luis", "Torres", "111111", "Derecho", "Estudiante", "luis@example.com", "pass1");
        User u2 = new User("Maria", "Lopez", "222222", "Arquitectura", "Docente", "maria@example.com", "pass2");

        repository.save(u1);
        repository.save(u2);

        List<User> users = repository.findAll();

        assertEquals(2, users.size());
        assertEquals("Maria", users.get(0).getFirstName()); // porque está ordenado DESC
    }
}
