
package co.unicauca.domain;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import co.unicauca.domain.services.PasswordEncryptor;
class PasswordEncryptorTest {

    private final PasswordEncryptor encryptor = new PasswordEncryptor();

    @Test
    void testEncryptAndMatchValidPassword() {
        String rawPassword = "MySecurePass123!";
        String encrypted = encryptor.encrypt(rawPassword);

        assertNotNull(encrypted);
        assertTrue(encrypted.startsWith("$2a$")); // BCrypt hashes start like this
        assertTrue(encryptor.matches(rawPassword, encrypted));
    }

    @Test
    void testDifferentPasswordsDoNotMatch() {
        String encrypted = encryptor.encrypt("CorrectPassword");

        assertFalse(encryptor.matches("WrongPassword", encrypted));
    }

    @Test
    void testEncryptGeneratesDifferentHashesForSamePassword() {
        String rawPassword = "RepeatPassword";

        String encrypted1 = encryptor.encrypt(rawPassword);
        String encrypted2 = encryptor.encrypt(rawPassword);

        assertNotEquals(encrypted1, encrypted2); // BCrypt should salt differently
        assertTrue(encryptor.matches(rawPassword, encrypted1));
        assertTrue(encryptor.matches(rawPassword, encrypted2));
    }

    @Test
    void testNullValues() {
        String encrypted = encryptor.encrypt("SomePassword");

        assertFalse(encryptor.matches(null, encrypted));
        assertFalse(encryptor.matches("SomePassword", null));
        assertFalse(encryptor.matches(null, null));
    }
}
