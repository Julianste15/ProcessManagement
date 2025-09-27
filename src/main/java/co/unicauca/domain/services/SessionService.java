package co.unicauca.domain.services;
import co.unicauca.domain.entities.User;
import co.unicauca.domain.exceptions.UserException;
import co.unicauca.domain.exceptions.UserExceptionEnum;
import co.unicauca.infrastructure.dependency_injection.Controller;
import co.unicauca.infrastructure.dependency_injection.FactoryAutowired;
import co.unicauca.infrastructure.security.iEncryptor;
@Controller
public class SessionService {
    @FactoryAutowired
    private UserService userService;
    @FactoryAutowired
    private iEncryptor encryptor;
    private User currentUser;
    public User login(String email, String password) throws UserException {
        if (email == null || password == null) {
            UserException.throwException(UserExceptionEnum.EMAIL, "Email y contraseña son obligatorios");
        }
        
        if (email.trim().isEmpty() || password.trim().isEmpty()) {
            UserException.throwException(UserExceptionEnum.EMAIL, "Email y contraseña no pueden estar vacíos");
        }
        
        // Validar formato de email institucional
        if (!email.toLowerCase().endsWith("@unicauca.edu.co")) {
            UserException.throwException(UserExceptionEnum.EMAIL, "Debe usar email institucional (@unicauca.edu.co)");
        }
        
        User user = userService.getUserByEmail(email);
        if (user == null) {
            // No revelar que el usuario no existe por seguridad
            return null;
        }
        
        if (encryptor.checkHash(password, user.getPassword())) {
            this.currentUser = user;
            return user;
        }
        
        return null;
    }
    public void logout() {this.currentUser=null;}
    public User getCurrentUser() {return currentUser;}
    public boolean isUserLoggedIn() {return currentUser != null;}
}
