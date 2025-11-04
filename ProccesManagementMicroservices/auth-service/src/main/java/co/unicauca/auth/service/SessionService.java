package co.unicauca.auth.service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.util.logging.Logger;
@Service
public class SessionService {    
    private static final Logger logger = Logger.getLogger(SessionService.class.getName());    
    @Autowired
    private RestTemplate restTemplate;    
    @Value("${user.service.url}")
    private String userServiceUrl;    
    /**
     * Valida credenciales llamando al user-service
     */
    public boolean validateCredentials(String email, String password) {
        try {
            // Crear request para user-service
            co.unicauca.auth.dto.LoginRequest request = 
                new co.unicauca.auth.dto.LoginRequest(email, password);            
            // Llamar al user-service
            Object response = restTemplate.postForObject(
                userServiceUrl + "/validate-credentials",
                request,
                Object.class
            );            
            return response != null;            
        } catch (Exception e) {
            logger.severe("Error validando credenciales: " + e.getMessage());
            return false;
        }
    }    
    public void logout() {
        logger.info("Sesión cerrada");
    }
}