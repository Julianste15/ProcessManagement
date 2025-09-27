package co.unicauca.domain.services;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
public class PasswordEncryptor {
    private final BCryptPasswordEncoder encoder;
    public PasswordEncryptor() {
        this.encoder = new BCryptPasswordEncoder(10);
    }
    public String encrypt(String rawPassword) {
        return encoder.encode(rawPassword);
    }
    public boolean matches(String rawPassword, String encryptedPassword) {
        if (rawPassword == null || encryptedPassword == null) {
            return false;
        }
        return encoder.matches(rawPassword, encryptedPassword);
    }
}
