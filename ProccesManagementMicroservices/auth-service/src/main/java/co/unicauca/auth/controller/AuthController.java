package co.unicauca.auth.controller;
import co.unicauca.auth.dto.LoginRequest;
import co.unicauca.auth.dto.AuthResponse;
import co.unicauca.auth.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import java.util.logging.Logger;
@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {    
    private static final Logger logger = Logger.getLogger(AuthController.class.getName());    
    @Autowired
    private AuthService authService;    
    /**
     * Autentica un usuario y genera token JWT
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest request) {
        try {
            logger.info("Intentando login para: " + request.getEmail());            
            AuthResponse response = authService.authenticate(request);            
            logger.info("Login exitoso para: " + request.getEmail());
            return ResponseEntity.ok(response);            
        } catch (Exception e) {
            logger.severe("Error en login: " + e.getMessage());
            return ResponseEntity.badRequest().body("Error de autenticación: " + e.getMessage());
        }
    }    
    /**
     * Valida un token JWT
     */
    @GetMapping("/validate")
    public ResponseEntity<?> validateToken(@RequestHeader("Authorization") String token) {
        try {
            if (token != null && token.startsWith("Bearer ")) {
                token = token.substring(7);
            }            
            boolean isValid = authService.validateToken(token);
            return ResponseEntity.ok(isValid);            
        } catch (Exception e) {
            logger.severe("Error validando token: " + e.getMessage());
            return ResponseEntity.badRequest().body(false);
        }
    }    
    /**
     * Cierra sesión
     */
    @PostMapping("/logout")
    public ResponseEntity<?> logout(@RequestHeader(value = "Authorization", required = false) String token) {
        try {
            if (token != null && token.startsWith("Bearer ")) {
                token = token.substring(7);
                authService.logout(token);
            }
            return ResponseEntity.ok("Logout exitoso");            
        } catch (Exception e) {
            logger.severe("Error en logout: " + e.getMessage());
            return ResponseEntity.badRequest().body("Error en logout");
        }
    }
}