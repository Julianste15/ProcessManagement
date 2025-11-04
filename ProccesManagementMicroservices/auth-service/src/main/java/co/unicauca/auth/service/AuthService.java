package co.unicauca.auth.service;

import co.unicauca.auth.dto.LoginRequest;
import co.unicauca.auth.dto.AuthResponse;
import co.unicauca.auth.security.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.util.Map;
import java.util.logging.Logger;

@Service
public class AuthService {    
    private static final Logger logger = Logger.getLogger(AuthService.class.getName());
    
    @Autowired
    private RestTemplate restTemplate;
    
    @Autowired
    private JwtService jwtService;

    public AuthResponse authenticate(LoginRequest request) {
        try {
            logger.info("Autenticando usuario: " + request.getEmail());
            
            // Llamar al user-service usando Map para evitar dependencias
            String userServiceUrl = "http://user-service/api/users/validate-credentials";
            
            // Crear request simple
            Map<String, String> userRequest = Map.of(
                "email", request.getEmail(),
                "password", request.getPassword()
            );
            
            // Llamar al endpoint
            ResponseEntity<Map> response = restTemplate.postForEntity(
                userServiceUrl,
                userRequest,
                Map.class
            );
            
            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                Map<String, Object> userData = response.getBody();
                
                // Extraer datos del Map
                String email = (String) userData.get("email");
                String role = (String) userData.get("role");
                String names = (String) userData.get("names");
                String surnames = (String) userData.get("surnames");
                
                // Generar token JWT
                String token = jwtService.generateToken(email, role);
                
                return new AuthResponse(
                    token,
                    email,
                    role,
                    names + " " + surnames,
                    "Bearer"
                );
            } else {
                throw new RuntimeException("Credenciales inválidas");
            }
            
        } catch (Exception e) {
            logger.severe("Error en autenticación: " + e.getMessage());
            throw new RuntimeException("Error de autenticación: " + e.getMessage());
        }
    }
    
    /**
     * Valida un token JWT
     */
    public boolean validateToken(String token) {
        return jwtService.validateToken(token);
    }
    
    /**
     * Cierra sesión
     */
    public void logout(String token) {
        logger.info("Cerrando sesión para token");
        // En implementación real, podrías invalidar el token
    }
}