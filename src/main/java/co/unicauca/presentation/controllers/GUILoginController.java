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
import java.util.logging.Logger;
import javax.swing.JOptionPane;
@Controller
public class GUILoginController extends ObservableBase implements iGUILoginController, iObserver {
    private static final Logger logger = Logger.getLogger(GUILoginController.class.getName());
    private final GUILogin view;
    private boolean actionsLoaded;

    private SessionService sessionService;
    @ControllerAutowired
    private GUIRegisterController registerController;
    @ControllerAutowired
    private GUIStudentController studentController;
    @ControllerAutowired
    private GUITeacherController teacherController;
    public GUILoginController() {
        this.view = new GUILogin();
        this.actionsLoaded = false;
        this.sessionService = co.unicauca.presentation.ClientMain.sharedSessionService;
        
        logger.info("GUILoginController constructor llamado");
        logger.info("SessionService: " + (sessionService != null ? "COMPARTIDA" : "NULL"));
    }
    @Override
    public void observersLoader() {
        if (!this.hasNoObservers()) {
            logger.info("Observers ya cargados, saltando...");
            return;
        } 
        
        logger.info("Cargando observers...");
        
        // Verificar que los controllers no sean null antes de agregarlos
        if (registerController != null) {
            this.addObserver(registerController);
            logger.info("✅ RegisterController agregado como observer");
        } else {
            logger.warning("⚠️ RegisterController es NULL");
        }
        
        if (studentController != null) {
            this.addObserver(studentController);
            logger.info("✅ StudentController agregado como observer");
        } else {
            logger.warning("⚠️ StudentController es NULL");
        }
        
        if (teacherController != null) {
            this.addObserver(teacherController);
            logger.info("✅ TeacherController agregado como observer");
        } else {
            logger.warning("⚠️ TeacherController es NULL");
        }
        
        logger.info("Carga de Observers completada ");
    }
    @Override
    public void run() {
        if (sessionService == null) {
            logger.severe("❌ SessionService compartida es NULL");
            view.showMessage("Error de configuración del sistema", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        logger.info("✅ Usando SessionService compartida");
        observersLoader();
        view.setVisible(true);
        setupEventHandlersOnce();
    } 
    private synchronized void setupEventHandlersOnce() {
        if (!actionsLoaded) {
            EventQueue.invokeLater(() -> {
                view.getButtonLogin().addActionListener(event -> handleLogin());
                view.getButtonRegister().addActionListener(event -> handleRegister());
            });
            actionsLoaded = true;
            logger.info("Event handlers configurados");
        };
    }
    @Override
    public void login(GUILogin prmGUILogin) {
        handleLogin();
    } 
    private void handleLogin() {
        try {
            logger.info("=== INICIANDO LOGIN CON INSTANCIA COMPARTIDA ===");
            
            if (!view.validateForm()) {
                logger.warning("Validación de formulario fallida");
                return;
            } 
            
            String email = view.getFieldEmail().getText().trim();
            String password = new String(view.getFieldPassword().getPassword());      
            
            logger.log(Level.INFO, "Intentando login para: {0}", email);
            
            // Usar la instancia compartida
            User user = sessionService.login(email, password);   
            
            if (user != null) {
                logger.log(Level.INFO, "Login exitoso para usuario: {0}", email);
                view.showMessage("¡Bienvenido " + user.getNames() + "!", JOptionPane.INFORMATION_MESSAGE);
                view.setVisible(false);
                view.clearForm();
                notifyObserversBasedOnRole(user);
            } else {
                logger.warning("Intento de login fallido para email: " + email);
                view.showMessage("Email o contraseña incorrectos", JOptionPane.ERROR_MESSAGE);
                view.getFieldPassword().setText("");
                view.getFieldPassword().requestFocus();
            }
            
        } catch (Exception ex) {
            logger.log(Level.SEVERE, "Error durante el login", ex);
            view.showMessage("Error durante el login: " + ex.getMessage(), JOptionPane.ERROR_MESSAGE);
        }
    }
    /**
     * Notifica solo al observer apropiado según el rol del usuario
     */
    private void notifyObserversBasedOnRole(User user) {
        if(user==null||user.getRole()==null){
            logger.warning("Usuario o rol es null");
            return;
        }
        logger.info("Notificando según rol: " + user.getRole());
        
        try {
            switch (user.getRole()) {
                case STUDENT:
                    logger.info("Buscando StudentController...");
                    if (studentController != null) {
                        logger.info("✅ Notificando a StudentController");
                        this.notifyOnly(GUIStudentController.class, user);
                    } else {
                        logger.warning("❌ StudentController es NULL - mostrando vista por defecto");
                        showDefaultView("Estudiante");
                    }
                    break;
                    
                case TEACHER:
                    logger.info("Buscando TeacherController...");
                    if (teacherController != null) {
                        logger.info("✅ Notificando a TeacherController");
                        this.notifyOnly(GUITeacherController.class, user);
                    } else {
                        logger.warning("❌ TeacherController es NULL - mostrando vista por defecto");
                        showDefaultView("Profesor");
                    }
                    break;
                    
                default:
                    logger.warning("Rol no manejado: " + user.getRole());
                    view.showMessage("Rol de usuario no soportado: " + user.getRole(), 
                                   JOptionPane.WARNING_MESSAGE);
                    break;
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error notificando observers", e);
            view.showMessage("Error al redirigir: " + e.getMessage(), JOptionPane.ERROR_MESSAGE);
        }
    }
    /**
     * Método temporal para mostrar una vista cuando el controller específico no está disponible
     */
    private void showDefaultView(String role) {
        logger.info("Mostrando vista por defecto para: " + role);
        view.showMessage("Bienvenido " + role + ". La vista específica no está disponible aún.", 
                        JOptionPane.INFORMATION_MESSAGE);
        // Aquí podrías abrir una vista genérica o mantener el login visible
        view.setVisible(true);
    }
    private void handleRegister() {
        logger.info("Navegando a pantalla de registro");
        view.setVisible(false);
        view.clearForm();
        
        if (registerController != null) {
            this.notifyOnly(GUIRegisterController.class, null);
        } else {
            logger.warning("RegisterController es NULL, no se puede navegar a registro");
            view.showMessage("Funcionalidad de registro no disponible", JOptionPane.ERROR_MESSAGE);
            view.setVisible(true);
        }
    }   
    @Override
    public void validateNotification(ObservableBase subject, Object model) {
        EventQueue.invokeLater(() -> {
            logger.info("Recibida notificación, mostrando vista de login");
            this.run(); // Mostrar la vista de login
            
            // Si se recibe un modelo (usuario registrado), cargar el email
            if (model instanceof User) {
                User user = (User) model;
                view.getFieldEmail().setText(user.getEmail());
                view.getFieldPassword().requestFocus(); // Poner foco en contraseña
                logger.log(Level.INFO,"Email cargado desde registro: ", user.getEmail());
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