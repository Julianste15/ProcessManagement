
package co.unicauca.domain.services;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class PasswordValidatorTest {
    @Test
    void validAndInvalidPasswords() {
        PasswordValidator v = new PasswordValidator();
        assertTrue(v.isValid("Abcdef1!"));
        assertFalse(v.isValid("short1!"));      // falta mayúscula
        assertFalse(v.isValid("Noupper1!".toLowerCase())); // falta mayúscula
        assertFalse(v.isValid("NoDigit!!"));    // falta dígito
        assertFalse(v.isValid("NoSpecial12"));  // falta especial
    }
}
