
package co.unicauca.domain;

import co.unicauca.domain.repositories.IUserRepositoriy;
import co.unicauca.domain.entities.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class IUserRepositoryTest {

    private IUserRepositoriy repository;

    // Implementación en memoria para pruebas
    static class InMemoryUserRepository implements IUserRepositoriy {
        private final Map<String, User> storage = new HashMap<>();

        @Override
        public void save(User user) {
            storage.put(user.getEmail(), user);
        }

        @Override
        public Optional<User> findByEmail(String email) {
            return Optional.ofNullable(storage.get(email));
        }

        @Override
        public boolean existsByEmail(String email) {
            return storage.containsKey(email);
        }

        @Override
        public List<User> findAll() {
            return new ArrayList<>(storage.values());
        }
    }

    @BeforeEach
    void setUp() {
        repository = new InMemoryUserRepository();
    }

    @Test
    void testSaveAndFindByEmail() {
        User user = new User("Carlos", "Martínez", "123456", "Ingeniería", "Estudiante", "carlos@example.com", "1234");
        repository.save(user);

        Optional<User> found = repository.findByEmail("carlos@example.com");

        assertTrue(found.isPresent());
        assertEquals("Carlos", found.get().getFirstName());
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

        var users = repository.findAll();

        assertEquals(2, users.size());
        assertTrue(users.stream().anyMatch(u -> u.getFirstName().equals("Luis")));
        assertTrue(users.stream().anyMatch(u -> u.getFirstName().equals("Maria")));
    }
}

