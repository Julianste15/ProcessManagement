package co.unicauca.presentation.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import co.unicauca.domain.entities.User;
import co.unicauca.domain.services.SessionService;
import co.unicauca.presentation.interfaces.iGUILoginController;
import co.unicauca.presentation.observer.iObserver;
import co.unicauca.presentation.views.GUILogin;
import co.unicauca.presentation.observer.ObservableBase;
import org.springframework.context.annotation.Lazy;
import java.awt.EventQueue;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

@Component
public class GUILoginController extends ObservableBase implements iGUILoginController, iObserver {
    
    private static final Logger logger = Logger.getLogger(GUILoginController.class.getName());
    private final GUILogin view;
    private boolean actionsLoaded;
    private final SessionService sessionService;
    private final GUIRegisterController registerController;
    private final GUIStudentController studentController;
    private final GUITeacherController teacherController;
    
    @Autowired
    public GUILoginController(SessionService sessionService,
                              @Lazy GUIRegisterController registerController,
                              @Lazy GUIStudentController studentController,
                              @Lazy GUITeacherController teacherController) {
        
        this.view = new GUILogin();
        this.actionsLoaded = false;
        this.sessionService = sessionService;
        this.registerController = registerController;
        this.studentController = studentController;
        this.teacherController = teacherController;

        logger.info("GUILoginController constructor llamado e inyectado por Spring");
    }
    
    @Override
    public void observersLoader() {
        if (!this.hasNoObservers()) {
            logger.info("Observers ya cargados, saltando...");
            return;
        }

        logger.info("Cargando observers...");

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
            // Este error ahora significaría que Spring falló en inyectar
            logger.severe("❌ SessionService es NULL (¡Falla de inyección de Spring!)");
            view.showMessage("Error de configuracion del sistema", JOptionPane.ERROR_MESSAGE);
            return;
        }

        logger.info("✅ Usando SessionService inyectado por Spring");
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
            logger.info("=== INICIANDO LOGIN CON SERVICIO INYECTADO ===");

            if (!view.validateForm()) {
                logger.warning("Validacion de formulario fallida");
                return;
            }

            String email = view.getFieldEmail().getText().trim();
            String password = new String(view.getFieldPassword().getPassword());

            logger.log(Level.INFO, "Intentando login para: {0}", email);

            // Usar la instancia inyectada por Spring
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

    private void notifyObserversBasedOnRole(User user) {
        if(user==null||user.getRole()==null){
            logger.warning("Usuario o rol es null");
            return;
        }
        logger.info("Notificando segun rol: " + user.getRole());

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

    private void showDefaultView(String role) {
        logger.info("Mostrando vista por defecto para: " + role);
        view.showMessage("Bienvenido " + role + ". La vista específica no esta disponible aun.",
                         JOptionPane.INFORMATION_MESSAGE);
        view.setVisible(true);
    }

    private void handleRegister() {
        logger.info("Navegando a pantalla de registro");
        view.setVisible(false);
        view.clearForm();
        
        if (registerController != null) {
            registerController.run(); 
        } else {
            logger.warning("RegisterController es NULL, no se puede navegar a registro");
            view.showMessage("Funcionalidad de registro no disponible", JOptionPane.ERROR_MESSAGE);
            view.setVisible(true); // Vuelve a mostrar el login si falla
        }
    }

    @Override
    public void validateNotification(ObservableBase subject, Object model) {
        EventQueue.invokeLater(() -> {
            logger.info("Recibida notificacion, mostrando vista de login");
            this.run(); // Mostrar la vista de login
            
            if (model instanceof User) {
                User user = (User) model;
                view.getFieldEmail().setText(user.getEmail());
                view.getFieldPassword().requestFocus();
                logger.log(Level.INFO,"Email cargado desde registro: ", user.getEmail());
            }
        });
    }
    
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
    
    public void showLogin() {
        EventQueue.invokeLater(() -> {
            view.clearForm();
            view.setVisible(true);
            view.getFieldEmail().requestFocus();
            logger.info("Vista de login mostrada programaticamente");
        });
    }

    public GUILogin getView() {
        return view;
    }
}