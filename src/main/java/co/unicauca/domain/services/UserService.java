package co.unicauca.domain.services;
import co.unicauca.domain.entities.User;
import co.unicauca.domain.repositories.UserRepository;
import co.unicauca.domain.exceptions.UserException;
import co.unicauca.domain.exceptions.UserExceptionEnum;
import co.unicauca.infrastructure.dependency_injection.Controller;
import co.unicauca.infrastructure.security.iEncryptor;
import co.unicauca.infrastructure.validation.iValidator;
import co.unicauca.infrastructure.dependency_injection.FactoryAutowired;
@Controller//userService debe ser controller para que funcione
public class UserService {
    @FactoryAutowired
    private UserRepository userRepository;
    @FactoryAutowired
    private iValidator userValidator;
    @FactoryAutowired
    private iEncryptor encryptor;
    public User registerUser(User user) throws UserException {
        if (user == null) {
            UserException.throwException(UserExceptionEnum.NAMES, "User cannot be null");
        }
        try {
            userValidator.validate(user);//Validar usuario
        } catch (UserException ex) {
            throw ex;
        } catch (Exception ex) {
            UserException.throwException(UserExceptionEnum.NAMES, "Error de validación: " + ex.getMessage());
        }
        if (userRepository.findByEmail(user.getEmail()) != null) {// Verificar que no exista
            UserException.throwException(UserExceptionEnum.EMAIL, "El usuario ya existe");
        }
        user.setPassword(encryptor.encrypt(user.getPassword()));// Encriptar password
        User savedUser = userRepository.save(user);// Guardar usuario
        if (savedUser == null) {
            UserException.throwException(UserExceptionEnum.NAMES, "Error al guardar usuario");
        }
        return savedUser;
    }
    public User getUserByEmail(String email) throws UserException {
        if (email == null || email.isEmpty()) {
            UserException.throwException(UserExceptionEnum.EMAIL, "Email cannot be empty");
        }

        try {
            User user = userRepository.findByEmail(email);
            
            System.out.println("📦 Resultado búsqueda: " + (user != null ? "ENCONTRADO" : "NO ENCONTRADO"));
            if (user != null) {
                System.out.println("   Nombre: " + user.getNames());
                System.out.println("   Email: " + user.getEmail());
                System.out.println("   Password length: " + (user.getPassword() != null ? user.getPassword().length() : "null"));
            }
            return user;
        } catch (Exception ex) {
            System.out.println("💥 Error en getUserByEmail: " + ex.getMessage());
            ex.printStackTrace();
            throw new UserException(UserExceptionEnum.EMAIL, "Error al buscar usuario: " + ex.getMessage());
        }
    }
    public User getUserById(Long id) throws UserException {
        if (id == null || id <= 0) {
            UserException.throwException(UserExceptionEnum.NAMES, "ID cannot be null or negative");
        }
        return userRepository.findById(id);
    }
}