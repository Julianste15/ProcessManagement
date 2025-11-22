package co.unicauca.domain.services;
import co.unicauca.domain.entities.User;
import co.unicauca.domain.enums.Role;
import co.unicauca.infrastructure.client.MicroserviceClient;
import co.unicauca.infrastructure.config.MicroserviceConfig;
import java.util.Map;
import java.util.logging.Logger;
/**
 * Servicio de sesión que se comunica con los microservicios
 */
public class SessionService {
    private static final Logger logger = Logger.getLogger(SessionService.class.getName());    
    private final MicroserviceClient client;
    private User currentUser;    
    public SessionService() {
        String gatewayUrl = MicroserviceConfig.getInstance().getGatewayUrl();
        this.client = new MicroserviceClient(gatewayUrl);
        this.currentUser = null;
        logger.info("SessionService inicializado con Gateway: " + gatewayUrl);
    }    
    @SuppressWarnings("unchecked")
    public User login(String email, String password) {
        try {
            logger.info("Intentando login para: " + email);            
            Map<String, String> loginRequest = Map.of(
                "email", email,
                "password", password
            );            
            Map<String, Object> response = client.post(
                "/api/auth/login",
                loginRequest,
                Map.class
            );            
            String token = (String) response.get("token");
            String responseEmail = (String) response.get("email");
            String roleStr = (String) response.get("role");
            String fullName = (String) response.get("names");            
            client.setToken(token);            
            String names = "";
            String surnames = "";
            if (fullName != null && !fullName.trim().isEmpty()) {
                String[] nameParts = fullName.trim().split("\\s+", 2);
                names = nameParts[0];
                surnames = nameParts.length > 1 ? nameParts[1] : "";
            }            
            User user = new User();
            user.setEmail(responseEmail);
            user.setNames(names);
            user.setSurnames(surnames);
            if (roleStr != null) {
                user.setRole(Role.fromValue(roleStr));
            }            
            Object requiresFormatAObj = response.get("requiresFormatA");
            if (requiresFormatAObj instanceof Boolean) {
                user.setRequiresFormatoA((Boolean) requiresFormatAObj);
            } else if (requiresFormatAObj != null) {
                user.setRequiresFormatoA(Boolean.parseBoolean(requiresFormatAObj.toString()));
            }            
            Object formatoAIdObj = response.get("formatoAId");
            if (formatoAIdObj instanceof Number) {
                user.setFormatoAId(((Number) formatoAIdObj).longValue());
            }            
            Object formatoAEstadoObj = response.get("formatoAEstado");
            if (formatoAEstadoObj != null) {
                user.setFormatoAEstado(formatoAEstadoObj.toString());
            }            
            this.currentUser = user;            
            logger.info("Login exitoso para: " + email + " con rol: " + roleStr);
            return user;            
        } catch (Exception e) {
            logger.severe("Error durante el login: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }    
    public void logout() {
        try {
            if (client.getToken() != null) {
                try {
                    client.post("/api/auth/logout", null, String.class);
                } catch (Exception e) {
                    logger.warning("Error llamando al endpoint de logout: " + e.getMessage());
                }
            }            
            client.clearToken();
            this.currentUser = null;
            logger.info("Sesión cerrada exitosamente");            
        } catch (Exception e) {
            logger.severe("Error durante el logout: " + e.getMessage());
        }
    }    
    public User getCurrentUser() {return currentUser;}    
    public boolean isLoggedIn() {
        return currentUser != null && client.getToken() != null;
    }    
    public String getToken() {return client.getToken();}    
    public MicroserviceClient getClient() {return client;}
}