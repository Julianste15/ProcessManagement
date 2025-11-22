package co.unicauca.presentation.controllers;
import co.unicauca.domain.entities.User;
import co.unicauca.domain.enums.Career;
import co.unicauca.domain.enums.Role;
import co.unicauca.domain.exceptions.UserException;
import co.unicauca.domain.services.UserService;
import co.unicauca.presentation.views.RegisterView;
import co.unicauca.presentation.views.LoginView;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.util.logging.Logger;
/**
 * Controlador para la vista de Registro
 */
public class RegisterController {
    private static final Logger logger = Logger.getLogger(RegisterController.class.getName());    
    private RegisterView view;
    private Stage stage;
    private UserService userService;    
    public RegisterController(RegisterView view, Stage stage) {
        this.view = view;
        this.stage = stage;
        this.userService = new UserService();
    }    
    public void handleRegister() {
        view.clearError();        
        if (view.getName().isEmpty() || view.getSurname().isEmpty() || 
            view.getEmail().isEmpty() || view.getPassword().isEmpty()) {
            view.showError("Todos los campos obligatorios deben ser completados");
            return;
        }        
        if (!view.getEmail().toLowerCase().endsWith("@unicauca.edu.co")) {
            view.showError("Debe usar su email institucional (@unicauca.edu.co)");
            return;
        }        
        if (view.getPassword().length() < 6) {
            view.showError("La contraseña debe tener al menos 6 caracteres");
            return;
        }        
        try {
            User user = new User();
            user.setNames(view.getName());
            user.setSurnames(view.getSurname());
            user.setEmail(view.getEmail());
            user.setPassword(view.getPassword());   
            String phone = view.getPhone();
            if (!phone.isEmpty()) {
                try {
                    user.setTelephone(Long.valueOf(phone));
                } catch (NumberFormatException e) {
                    view.showError("El teléfono debe ser un número válido");
                    return;
                }
            }
            user.setCareer(Career.fromDisplayName(view.getCareer()));
            user.setRole(Role.fromDisplayName(view.getRole()));            
            logger.info("Registrando usuario: " + user.getEmail());
            User registeredUser = userService.registerUser(user);            
            logger.info("Usuario registrado exitosamente: " + registeredUser.getEmail());            
            javafx.scene.control.Alert alert = new javafx.scene.control.Alert(
                javafx.scene.control.Alert.AlertType.INFORMATION
            );
            alert.setTitle("Registro Exitoso");
            alert.setHeaderText(null);
            alert.setContentText("Usuario registrado exitosamente. Por favor, inicie sesión.");
            alert.showAndWait();            
            handleBack();            
        } catch (UserException e) {
            logger.warning("Error en registro: " + e.getMessage());
            view.showError(e.getMessage());
        } catch (Exception e) {
            logger.severe("Error inesperado durante el registro: " + e.getMessage());
            view.showError("Error de conexión. Verifique que los microservicios estén corriendo.");
        }
    }    
    public void handleBack() {
        logger.info("Volviendo a pantalla de login");
        LoginView loginView = new LoginView(stage);
        Scene scene = new Scene(loginView.getRoot(), 800, 600);
        scene.getStylesheets().add(getClass().getResource("/styles/application.css").toExternalForm());
        stage.setScene(scene);
        stage.setTitle("Sistema de Gestión de Trabajos de Grado - Universidad del Cauca");
    }
}