package co.unicauca.infrastructure.security;
import co.unicauca.infrastructure.dependency_injection.Service;
import org.mindrot.jbcrypt.BCrypt;
@Service
public class Encryptor implements iEncryptor {
    public Encryptor(){}
    @Override
    public String encrypt(String prmChain) 
    {
        return BCrypt.hashpw(prmChain, BCrypt.gensalt());
    }
    @Override
    public boolean checkHash(String prmChain, String prmHash) 
    {
        return BCrypt.checkpw(prmChain, prmHash);
    }
}
