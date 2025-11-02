package co.unicauca.domain.entities;

import org.junit.jupiter.api.Test;
import co.unicauca.domain.enums.Career;
import co.unicauca.domain.enums.Role;
import static org.junit.jupiter.api.Assertions.*;

class UserEntityTest {
    @Test
    void builderOrConstructorSetsFields() {
        User user = new User(1L, "Ana", "Perez", "ana@unicauca.edu.co",
                             "secret", 3100000000L, Career.SYSTEMS_ENGINEERING, Role.STUDENT);
        assertEquals(1L, user.getId());
        assertEquals("Ana", user.getNames());
        assertEquals("Perez", user.getSurnames());
        assertEquals("ana@unicauca.edu.co", user.getEmail());
        assertEquals(3100000000L, user.getTelephone());
        assertEquals(Career.SYSTEMS_ENGINEERING, user.getCareer());
        assertEquals(Role.STUDENT, user.getRole());
    }
}