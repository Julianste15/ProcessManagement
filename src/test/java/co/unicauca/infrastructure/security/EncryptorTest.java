
package co.unicauca.infrastructure.security;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class EncryptorTest {
    @Test
    void encryptAndVerify() {
        Encryptor enc = new Encryptor();
        String hash = enc.encrypt("Secreto123!");
        assertNotNull(hash);
        assertTrue(enc.checkHash("Secreto123!", hash));
        assertFalse(enc.checkHash("otro", hash));
    }
}
