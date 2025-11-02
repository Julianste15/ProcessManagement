package co.unicauca.infrastructure.security;
public interface iEncryptor {
    String encrypt(String prmChain);
    boolean checkHash(String prmChain, String prmHash);
}
