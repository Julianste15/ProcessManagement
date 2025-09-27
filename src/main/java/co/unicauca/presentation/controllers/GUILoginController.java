package co.unicauca.presentation.controllers;

import co.unicauca.domain.entities.User;
import co.unicauca.domain.services.SessionService;
import co.unicauca.infrastructure.dependency_injection.Controller;
import co.unicauca.infrastructure.dependency_injection.ControllerAutowired;
import co.unicauca.presentation.interfaces.iGUILoginController;
import co.unicauca.presentation.observer.iObserver;
import co.unicauca.presentation.views.GUILogin;
import co.unicauca.presentation.observer.ObservableBase;

import java.awt.EventQueue;
import java.util.logging.Level;
import javax.swing.JOptionPane;

@Controller
public class GUILoginController extends ObservableBase implements iGUILoginController, iObserver {
    
    private final GUILogin view;
    private boolean actionsLoaded;
    
    @ControllerAutowired
    private SessionService sessionService;
    
    @ControllerAutowired
    private GUIRegisterController registerController;
    
    // Logger correctamente inicializado
    private static final java.util.logging.Logger logger = 
        java.util.logging.Logger.getLogger(GUILoginController.class.getName());
    
    public GUILoginController() {
        this.view = new GUILogin();
        this.actionsLoaded = false;
    }
    
    @Override
    public void observersLoader() {
        if (!this.hasNoObservers()) return;
        
        this.addObserver(registerController);
        this.addObserver(new GUIStudentController());
        this.addObserver(new GUITeacherController());
    }
    
    @Override
    public void run() {
        observersLoader();
        view.setVisible(true);
        
        if (!actionsLoaded) {
            setupEventHandlers();
            actionsLoaded = true;
        }
    }
    
    private void setupEventHandlers() {
        view.setLoginAction(this::handleLogin);
        view.setRegisterAction(this::handleRegister);
    }
    
    @Override
    public void login(GUILogin prmGUILogin) {
        handleLogin();
    }
    
    private void handleLogin() {
        try {
            // Validar el formulario primero
            if (!view.validateForm()) {
                return;
            }
            
            String email = view.getFieldEmail().getText().trim();
            String password = new String(view.getFieldPassword().getPassword());
            
            // Intentar el login
            User user = sessionService.login(email, password);
            
            if (user != null) {
                // Login exitoso
                logger.info("Login exitoso para usuario: " + email);
                view.showMessage("¡Bienvenido " + user.getNames() + "!", JOptionPane.INFORMATION_MESSAGE);
                view.setVisible(false);
                view.clearForm(); // Limpiar formulario para próxima sesión
                
                // Notificar a los observadores (controllers de student/teacher)
                this.notifyObservers(user);
            } else {
                // Credenciales incorrectas
                logger.warning("Intento de login fallido para email: " + email);
                view.showMessage("Email o contraseña incorrectos", JOptionPane.ERROR_MESSAGE);
                view.getFieldPassword().setText(""); // Limpiar solo la contraseña
                view.getFieldPassword().requestFocus(); // Poner foco en contraseña
            }
            
        } catch (co.unicauca.domain.exceptions.UserException ex) {
            // Manejar excepciones específicas del dominio
            logger.warning("Error de validación en login: " + ex.getMessage());
            view.showMessage(ex.getMessage(), JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            // Manejar errores inesperados
            logger.log(Level.SEVERE, "Error inesperado durante el login", ex);
            view.showMessage("Error inesperado durante el login. Por favor, intente nuevamente.", 
                           JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void handleRegister() {
        logger.info("Navegando a pantalla de registro");
        view.setVisible(false);
        view.clearForm();
        this.notifyOnly(GUIRegisterController.class, null);
    }
    
    @Override
    public void validateNotification(ObservableBase subject, Object model) {
        // Este método se llama cuando este controller es notificado por otro controller
        EventQueue.invokeLater(() -> {
            this.run(); // Mostrar la vista de login
            
            // Si se recibe un modelo (usuario registrado), cargar el email
            if (model instanceof User) {
                User user = (User) model;
                view.getFieldEmail().setText(user.getEmail());
                view.getFieldPassword().requestFocus(); // Poner foco en contraseña
                logger.info("Email cargado desde registro: " + user.getEmail());
            }
        });
    }
    
    // Método para cerrar sesión (puede ser llamado desde otros controllers)
    public void logout() {
        try {
            sessionService.logout();
            view.clearForm();
            view.setVisible(true);
            view.getFieldEmail().requestFocus();
            logger.info("Logout exitoso");
        } catch (Exception ex) {
            logger.log(Level.SEVERE, "Error durante el logout", ex);
        }
    }
    
    // Método para mostrar la vista de login programáticamente
    public void showLogin() {
        EventQueue.invokeLater(() -> {
            view.clearForm();
            view.setVisible(true);
            view.getFieldEmail().requestFocus();
            logger.info("Vista de login mostrada programáticamente");
        });
    }
    
    // Método para obtener la vista (útil para testing)
    public GUILogin getView() {
        return view;
    }
}