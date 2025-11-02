
package co.unicauca.infrastructure.validation;

import co.unicauca.domain.entities.User;
import co.unicauca.domain.enums.Career;
import co.unicauca.domain.enums.Role;
import co.unicauca.domain.exceptions.UserException;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class UserValidationTest {
    @Test
    void invalidUserThrowsException() {
        UserValidation v = new UserValidation();
        User u = new User();
        // Usuario vacio debería generar errores de validacion
        assertThrows(UserException.class, () -> v.validate(u));
    }
    @Test
    void minimalValidUserPasses() {
        UserValidation v = new UserValidation();
        User u = new User(1L, "Ana", "Perez", "ana@unicauca.edu.co",
                "Abcdef1!", 3100000000L, Career.SYSTEMS_ENGINEERING, Role.STUDENT);
        assertDoesNotThrow(() -> v.validate(u));
    }
}
