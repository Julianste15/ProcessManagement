package co.unicauca.infrastructure.security;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
@Service
public class Encryptor implements iEncryptor {
    private final PasswordEncoder encoder;
    public Encryptor(){
        this.encoder = new BCryptPasswordEncoder();
    }
    @Override
    public String encrypt(String prmChain) {
        return encoder.encode(prmChain);
    }
    @Override
    public boolean checkHash(String prmChain, String prmHash) {
        return encoder.matches(prmChain, prmHash);
    }
}