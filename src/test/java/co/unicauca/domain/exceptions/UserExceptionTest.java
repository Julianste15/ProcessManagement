
package co.unicauca.domain.exceptions;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class UserExceptionTest {
    @Test
    void addMessagesBuildsReadableMessage() {
        UserException ex = new UserException();
        ex.addExceptionMessage(UserExceptionEnum.EMAIL, "es obligatorio");
        ex.addExceptionMessage(UserExceptionEnum.PASSWORD, "no cumple política");
        String msg = ex.getMessage();
        assertNotNull(msg);
        assertTrue(msg.contains("email"));
        assertTrue(msg.contains("contraseña"));
    }
}
