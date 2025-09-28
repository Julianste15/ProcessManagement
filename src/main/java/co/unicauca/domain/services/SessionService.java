package co.unicauca.domain.services;
import co.unicauca.domain.entities.User;
import co.unicauca.domain.exceptions.UserException;
import co.unicauca.domain.exceptions.UserExceptionEnum;
import co.unicauca.infrastructure.dependency_injection.Service;
import co.unicauca.infrastructure.dependency_injection.FactoryAutowired;
import co.unicauca.infrastructure.security.iEncryptor;
@Service
public class SessionService {
    @FactoryAutowired
    private UserService userService;
    @FactoryAutowired
    private iEncryptor encryptor;
    private User currentUser;
    public SessionService() {
        System.out.println("SessionService creado como Service");
    }
    // Constructor manual (para inyección garantizada)
    public SessionService(UserService userService, iEncryptor encryptor) {
        this.userService = userService;
        this.encryptor = encryptor;
        System.out.println("SessionService creado con dependencias manuales");
    }
    public User login(String email, String password) throws UserException {
        System.out.println("=== INICIO LOGIN ===");
        System.out.println("Email: " + email);
        
        // Validar que las dependencias estén inyectadas
        if (userService == null) {
            throw new UserException(UserExceptionEnum.EMAIL, 
                "Error de configuración: UserService no está disponible");
        }
        
        if (encryptor == null) {
            throw new UserException(UserExceptionEnum.EMAIL, 
                "Error de configuración: Encryptor no está disponible");
        }
        
        if (email == null || password == null) {
            UserException.throwException(UserExceptionEnum.EMAIL, "Email y contraseña son obligatorios");
        }
        
        if (email.trim().isEmpty() || password.trim().isEmpty()) {
            UserException.throwException(UserExceptionEnum.EMAIL, "Email y contraseña no pueden estar vacíos");
        }
        
        if (!email.toLowerCase().endsWith("@unicauca.edu.co")) {
            UserException.throwException(UserExceptionEnum.EMAIL, "Debe usar email institucional (@unicauca.edu.co)");
        }
        
        User user = userService.getUserByEmail(email);
        if (user == null) {
            System.out.println("❌ Usuario no encontrado en BD");
            return null;
        }
        
        System.out.println("✅ Usuario encontrado: " + user.getNames());
        
        if (encryptor.checkHash(password, user.getPassword())) {
            this.currentUser = user;
            System.out.println("✅ Login exitoso");
            return user;
        }
        
        System.out.println("❌ Contraseña incorrecta");
        return null;
    }
    public void logout() {this.currentUser=null;}
    public User getCurrentUser() {return currentUser;}
    public boolean isUserLoggedIn() {return currentUser != null;}
}
