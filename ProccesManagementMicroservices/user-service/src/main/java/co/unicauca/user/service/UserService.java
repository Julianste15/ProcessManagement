package co.unicauca.user.service;
import co.unicauca.user.model.User;
import co.unicauca.user.repository.UserRepository;
import co.unicauca.user.exceptions.UserException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import co.unicauca.user.validation.iValidator;
import co.unicauca.user.exceptions.UserExceptionEnum;
import java.util.Optional;
@Service
public class UserService {    
    @Autowired
    private UserRepository userRepository;    
    @Autowired
    private iValidator userValidator;    
    @Autowired
    private PasswordEncryptor passwordEncryptor;    
    /**
     * Registra un nuevo usuario
     */
    public User registerUser(User user) throws UserException {
        if (user == null) {
            UserException.throwException(UserExceptionEnum.NAMES, "User cannot be null");
        }        
        try {
            // Validar usuario
            userValidator.validate(user);
        } catch (UserException ex) {
            throw ex;
        } catch (Exception ex) {
            UserException.throwException(UserExceptionEnum.NAMES, "Error de validacion: " + ex.getMessage());
        }        
        // Verificar que no exista
        if (userRepository.findByEmail(user.getEmail()) != null) {
            UserException.throwException(UserExceptionEnum.EMAIL, "El usuario ya existe");
        }        
        // Encriptar password
        user.setPassword(passwordEncryptor.encrypt(user.getPassword()));        
        // Guardar usuario
        User savedUser = userRepository.save(user);        
        if (savedUser == null) {
            UserException.throwException(UserExceptionEnum.NAMES, "Error al guardar usuario");
        }        
        return savedUser;
    }    
    /**
     * Obtiene usuario por email
     */
    public User getUserByEmail(String email) throws UserException {
        if (email == null || email.isEmpty()) {
            UserException.throwException(UserExceptionEnum.EMAIL, "Email cannot be empty");
        }
        try {
            User user = userRepository.findByEmail(email);
            return user;
        } catch (Exception ex) {
            throw new UserException(UserExceptionEnum.EMAIL, "Error al buscar usuario: " + ex.getMessage());
        }
    }    
    /**
     * Valida si la contraseña coincide
     */
    public boolean validatePassword(String rawPassword, String encryptedPassword) {
        if (rawPassword == null || encryptedPassword == null) {
            return false;
        }
        return passwordEncryptor.matches(rawPassword, encryptedPassword);
    }    
    /**
     * Obtiene usuario por ID
     */
    public Optional<User> getUserById(Long id) throws UserException {
        if (id == null || id <= 0) {
            UserException.throwException(UserExceptionEnum.NAMES, "ID cannot be null or negative");
        }
        return userRepository.findById(id);
    }
}