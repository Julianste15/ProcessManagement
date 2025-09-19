package co.unicauca.domain;

import co.unicauca.domain.entities.User;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class userTest {

    @Test
    void testConstructorSinId() {
        User user = new User("Juan", "Pérez", "123456789", "Ingeniería", "Estudiante", "juan@mail.com", "1234");

        assertNull(user.getId());  // No debería tener id
        assertEquals("Juan", user.getFirstName());
        assertEquals("Pérez", user.getLastName());
        assertEquals("123456789", user.getPhone());
        assertEquals("Ingeniería", user.getProgram());
        assertEquals("Estudiante", user.getRole());
        assertEquals("juan@mail.com", user.getEmail());
        assertEquals("1234", user.getPassword());
    }

    @Test
    void testConstructorConId() {
        User user = new User(1L, "Ana", "García", "987654321", "Derecho", "Docente", "ana@mail.com", "abcd");

        assertEquals(1L, user.getId());
        assertEquals("Ana", user.getFirstName());
        assertEquals("García", user.getLastName());
        assertEquals("987654321", user.getPhone());
        assertEquals("Derecho", user.getProgram());
        assertEquals("Docente", user.getRole());
        assertEquals("ana@mail.com", user.getEmail());
        assertEquals("abcd", user.getPassword());
    }

    @Test
    void testSettersAndGetters() {
        User user = new User("Carlos", "López", "5555555", "Medicina", "Estudiante", "carlos@mail.com", "pass");

        user.setId(10L);
        user.setFirstName("Luis");
        user.setLastName("Martínez");
        user.setPhone("1111111");
        user.setProgram("Arquitectura");
        user.setRole("Administrador");
        user.setEmail("luis@mail.com");
        user.setPassword("newpass");

        assertEquals(10L, user.getId());
        assertEquals("Luis", user.getFirstName());
        assertEquals("Martínez", user.getLastName());
        assertEquals("1111111", user.getPhone());
        assertEquals("Arquitectura", user.getProgram());
        assertEquals("Administrador", user.getRole());
        assertEquals("luis@mail.com", user.getEmail());
        assertEquals("newpass", user.getPassword());
    }
}
