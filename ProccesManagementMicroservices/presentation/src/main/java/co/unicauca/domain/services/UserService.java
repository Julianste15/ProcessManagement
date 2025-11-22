package co.unicauca.domain.services;
import co.unicauca.domain.entities.User;
import co.unicauca.domain.enums.Career;
import co.unicauca.domain.enums.Role;
import co.unicauca.domain.exceptions.UserException;
import co.unicauca.infrastructure.client.MicroserviceClient;
import co.unicauca.infrastructure.config.MicroserviceConfig;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;
/**
 * Servicio de usuario que se comunica con los microservicios
 */
public class UserService {
    private static final Logger logger = Logger.getLogger(UserService.class.getName());    
    private final MicroserviceClient client;    
    public UserService() {
        String gatewayUrl = MicroserviceConfig.getInstance().getGatewayUrl();
        this.client = new MicroserviceClient(gatewayUrl);
        logger.info("UserService inicializado con Gateway: " + gatewayUrl);
    }
    @SuppressWarnings("unchecked")
    public User registerUser(User user) throws UserException {
        try {
            logger.info("Registrando usuario: " + user.getEmail());            
            Map<String, Object> registerRequest = new HashMap<>();
            registerRequest.put("names", user.getNames());
            registerRequest.put("surnames", user.getSurnames());
            registerRequest.put("email", user.getEmail());
            registerRequest.put("password", user.getPassword());
            if (user.getTelephone() != null) {
                registerRequest.put("telephone", user.getTelephone());
            }
            registerRequest.put("career", user.getCareer() != null ? user.getCareer().name() : "SYSTEMS_ENGINEERING");
            registerRequest.put("role", user.getRole() != null ? user.getRole().name() : "STUDENT");            
            Map<String, Object> response = client.post(
                "/api/users/register",
                registerRequest,
                Map.class
            );            
            User registeredUser = convertMapToUser(response);            
            logger.info("Usuario registrado exitosamente: " + registeredUser.getEmail());
            return registeredUser;            
        } catch (RuntimeException e) {
            logger.severe("Error registrando usuario: " + e.getMessage());
            throw new UserException("Error al registrar usuario: " + e.getMessage(), e);
        } catch (Exception e) {
            logger.severe("Error inesperado registrando usuario: " + e.getMessage());
            throw new UserException("Error inesperado al registrar usuario: " + e.getMessage(), e);
        }
    }   
    private User convertMapToUser(Map<String, Object> map) {
        User user = new User();        
        if (map.containsKey("id")) {
            Object id = map.get("id");
            if (id instanceof Number) {
                user.setId(((Number) id).longValue());
            }
        }        
        if (map.containsKey("names")) {
            user.setNames((String) map.get("names"));
        }        
        if (map.containsKey("surnames")) {
            user.setSurnames((String) map.get("surnames"));
        }        
        if (map.containsKey("email")) {
            user.setEmail((String) map.get("email"));
        }        
        if (map.containsKey("telephone")) {
            Object tel = map.get("telephone");
            if (tel != null && tel instanceof Number) {
                user.setTelephone(((Number) tel).longValue());
            }
        }        
        if (map.containsKey("requiresFormatA")) {
            Object requires = map.get("requiresFormatA");
            if (requires instanceof Boolean) {
                user.setRequiresFormatoA((Boolean) requires);
            } else if (requires != null) {
                user.setRequiresFormatoA(Boolean.parseBoolean(requires.toString()));
            }
        }        
        if (map.containsKey("formatoAId")) {
            Object formatoId = map.get("formatoAId");
            if (formatoId instanceof Number) {
                user.setFormatoAId(((Number) formatoId).longValue());
            }
        }        
        if (map.containsKey("formatoAEstado")) {
            Object estado = map.get("formatoAEstado");
            if (estado != null) {
                user.setFormatoAEstado(estado.toString());
            }
        }        
        if (map.containsKey("career")) {
            Object careerObj = map.get("career");
            if (careerObj != null) {
                try {
                    user.setCareer(Career.fromValue(careerObj.toString()));
                } catch (Exception e) {
                    logger.warning("No se pudo parsear career: " + careerObj);
                }
            }
        }        
        if (map.containsKey("role")) {
            Object roleObj = map.get("role");
            if (roleObj != null) {
                try {
                    user.setRole(Role.fromValue(roleObj.toString()));
                } catch (Exception e) {
                    logger.warning("No se pudo parsear role: " + roleObj);
                }
            }
        }        
        return user;
    }
}