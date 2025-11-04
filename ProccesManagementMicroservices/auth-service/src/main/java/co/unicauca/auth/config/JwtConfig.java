package co.unicauca.auth.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "jwt")
public class JwtConfig {
    private String secret;
    private Long expiration = 86400000L; // Default 24 horas
    
    // Getters y Setters
    public String getSecret() { return secret; }
    public void setSecret(String secret) { this.secret = secret; }
    
    public Long getExpiration() { return expiration; }
    public void setExpiration(Long expiration) { this.expiration = expiration; }
}