package co.unicauca.user.controller;
import co.unicauca.user.dto.LoginRequest;
import co.unicauca.user.dto.RegisterRequest;
import co.unicauca.user.dto.UserDTO;
import co.unicauca.user.service.UserService;
import co.unicauca.user.model.User;
import co.unicauca.user.repository.UserRepository;
import co.unicauca.user.util.AuthHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.stream.Collectors;
import java.util.logging.Logger;
@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "*")
public class UserController {    
    private static final Logger logger = Logger.getLogger(UserController.class.getName());    
    @Autowired
    private UserService userService;    
    @Autowired
    private UserRepository userRepository;   
    /**
     * Registra un nuevo usuario
     */
    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest request) {
        try {
            logger.info("Registrando usuario: " + request.getEmail());            
            User user = new User();
            user.setNames(request.getNames());
            user.setSurnames(request.getSurnames());
            user.setEmail(request.getEmail());
            user.setPassword(request.getPassword());
            user.setTelephone(request.getTelephone());
            user.setCareer(request.getCareer());
            user.setRole(request.getRole());            
            User registeredUser = userService.registerUser(user);            
            UserDTO userDTO = convertToDTO(registeredUser);            
            return ResponseEntity.ok(userDTO);            
        } catch (Exception e) {
            logger.severe("Error en registro: " + e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }    
    /**
     * Valida credenciales de login
     */
    @PostMapping("/validate-credentials")
    public ResponseEntity<?> validateCredentials(@Valid @RequestBody LoginRequest request) {
        try {
            logger.info("Validando credenciales para: " + request.getEmail());            
            User user = userService.getUserByEmail(request.getEmail());            
            if (user != null && userService.validatePassword(request.getPassword(), user.getPassword())) {
                Map<String, Object> response = new HashMap<>();
                response.put("email", user.getEmail());
                response.put("role", user.getRole().toString());
                response.put("names", user.getNames());
                response.put("surnames", user.getSurnames());
                return ResponseEntity.ok(response);
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Credenciales inválidas");
            }            
        } catch (Exception e) {
            logger.severe("Error validando credenciales: " + e.getMessage());
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }    
    /**
     * Obtiene usuario por email
     */
    @GetMapping("/{email}")
    public ResponseEntity<?> getUserByEmail(@PathVariable String email) {
        try {
            User user = userService.getUserByEmail(email);
            if (user != null) {
                UserDTO userDTO = convertToDTO(user);
                return ResponseEntity.ok(userDTO);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }    
    /**
     * ENDPOINT PROTEGIDO - Obtiene el perfil del usuario actual
     */
    @GetMapping("/profile")
    public ResponseEntity<?> getCurrentUserProfile(HttpServletRequest request) {
        try {
            String userEmail = request.getHeader("X-User-Email");
            String userRole = request.getHeader("X-User-Role");            
            logger.info("Accediendo perfil protegido para: " + userEmail + " con rol: " + userRole);            
            if (userEmail == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Usuario no autenticado");
            }            
            User user = userService.getUserByEmail(userEmail);
            if (user != null) {
                UserDTO userDTO = convertToDTO(user);
                return ResponseEntity.ok(userDTO);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }    
    /**
     * ENDPOINT PROTEGIDO - Solo para administradores
     * Obtiene todos los usuarios (solo admin)
     */
    @GetMapping("/all")
    public ResponseEntity<?> getAllUsers(HttpServletRequest request) {
        try {
            AuthHelper.validateAdminRole(request);
            List<User> users = userRepository.findAll();
            List<UserDTO> userDTOs = users.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
            return ResponseEntity.ok(userDTOs);
        } catch (SecurityException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        } catch (Exception e) {
            logger.severe("Error obteniendo todos los usuarios: " + e.getMessage());
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }
    /**
     * Convierte User a UserDTO (sin password)
     */
    private UserDTO convertToDTO(User user) {
        return new UserDTO(
            user.getId(),
            user.getNames(),
            user.getSurnames(),
            user.getEmail(),
            user.getTelephone(),
            user.getCareer(),
            user.getRole()
        );
    }
}