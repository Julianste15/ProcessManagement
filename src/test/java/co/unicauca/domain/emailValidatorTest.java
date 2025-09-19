package co.unicauca.domain;

import co.unicauca.domain.services.EmailValidator;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class emailValidatorTest {

    private final EmailValidator validator = new EmailValidator();

    @Test
    void testValidInstitutionalEmail() {
        assertTrue(validator.isInstitutional("juan@unicauca.edu.co"));
        assertTrue(validator.isInstitutional("maria123@unicauca.edu.co"));
        assertTrue(validator.isInstitutional("carlos.lopez@unicauca.edu.co"));
    }

    @Test
    void testInvalidEmailDifferentDomain() {
        assertFalse(validator.isInstitutional("juan@gmail.com"));
        assertFalse(validator.isInstitutional("ana@yahoo.com"));
    }

    @Test
    void testInvalidEmailWrongFormat() {
        assertFalse(validator.isInstitutional("not-an-email"));
        assertFalse(validator.isInstitutional("pedro@unicauca"));
        assertFalse(validator.isInstitutional("pedro@unicauca.edu.com"));
        assertFalse(validator.isInstitutional("pedro@unicauca.edu.co.extra"));
    }

    @Test
    void testNullEmail() {
        assertFalse(validator.isInstitutional(null));
    }

    @Test
    void testEmptyEmail() {
        assertFalse(validator.isInstitutional(""));
    }
}
