package co.unicauca.presentation.controllers;
import co.unicauca.domain.entities.User;
import co.unicauca.domain.enums.Role;
import co.unicauca.domain.services.SessionService;
import co.unicauca.infrastructure.dependency_injection.Controller;
import co.unicauca.infrastructure.dependency_injection.ControllerAutowired;
import co.unicauca.presentation.observer.ObservableBase;
import co.unicauca.presentation.views.GUITeacher;
import co.unicauca.presentation.observer.iObserver;
import java.awt.EventQueue;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
@Controller
public class GUITeacherController extends ObservableBase implements iObserver {
    private static final Logger logger = Logger.getLogger(GUIStudentController.class.getName());
    private GUITeacher view;
    private User currentTeacher;
    @ControllerAutowired
    private SessionService sessionService;
    @ControllerAutowired
    private GUILoginController loginController;
    public GUITeacherController() {
        // La vista se crea cuando se necesita
    }
    private void initializeView() {
        if (view == null) {
            try {
                view = new GUITeacher();
                setupEventHandlers();
                logger.info("Vista Teacher inicializada correctamente");
            } catch (Exception e) {
                logger.log(Level.SEVERE, "Error inicializando vista Teacher", e);
                throw new RuntimeException("Error creando vista de profesor", e);
            }
        }
    }
    private void setupEventHandlers() {
        try {
            view.setLogoutAction(this::handleLogout);
            view.setUserMenuAction(this::handleUserMenu);
            logger.info("Event handlers de Teacher configurados");
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error configurando event handlers", e);
        }
    }
    @Override
    public void validateNotification(ObservableBase subject, Object model) {
        logger.info("TeacherController recibió notificación");
        
        if (model instanceof User) {
            User user = (User) model;
            if (user.getRole() == Role.TEACHER) {
                logger.info("Usuario profesor recibido: " + user.getNames());
                this.currentTeacher = user;
                
                EventQueue.invokeLater(() -> {
                    try {
                        initializeView();
                        loadTeacherData(user);
                        view.setVisible(true);
                        logger.info("Vista de profesor mostrada exitosamente");
                    } catch (Exception e) {
                        logger.log(Level.SEVERE, "Error mostrando vista de profesor", e);
                        JOptionPane.showMessageDialog(
                            null, 
                            "Error al abrir la vista de profesor: " + e.getMessage(),
                            "Error", 
                            JOptionPane.ERROR_MESSAGE
                        );
                    }
                });
            } else {
                logger.warning("Usuario recibido no es profesor: " + user.getRole());
            }
        } else {
            logger.warning("Model recibido no es User: " + (model != null ? model.getClass() : "null"));
        }
    }
    private void loadTeacherData(User teacher) {
        try {
            view.setUserInfo(
                teacher.getNames() + " " + teacher.getSurnames(),
                teacher.getEmail(),
                teacher.getCareer().getDisplayName()
            );
            logger.info("Datos de profesor cargados: " + teacher.getNames());
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error cargando datos de profesor", e);
        }
    }
    private void handleLogout() {
        try {
            int option = JOptionPane.showConfirmDialog(
                view, 
                "¿Está seguro que desea cerrar sesión?", 
                "Confirmar cierre de sesión", 
                JOptionPane.YES_NO_OPTION
            );
            
            if (option == JOptionPane.YES_OPTION) {
                logger.info("Cerrando sesión de profesor: " + currentTeacher.getNames());
                
                // 1. Cerrar sesión en el servicio
                if (sessionService != null) {
                    sessionService.logout();
                } else {
                    logger.warning("SessionService es null durante logout");
                }
                
                // 2. Cerrar vista actual
                if (view != null) {
                    view.setVisible(false);
                    view.dispose();
                    view = null;
                }
                
                // 3. Limpiar datos
                currentTeacher = null;
                
                // 4. Volver al login - OPCIÓN 1: Notificar al login controller
                if (loginController != null) {
                    logger.info("Notificando a LoginController para volver al login");
                    loginController.showLogin();
                } 
                
                logger.info("Sesión de profesor cerrada exitosamente");
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error durante logout de profesor", e);
            JOptionPane.showMessageDialog(
                view, 
                "Error al cerrar sesión: " + e.getMessage(),
                "Error", 
                JOptionPane.ERROR_MESSAGE
            );
        }
    }
    private void handleUserMenu() {
        try {
            JOptionPane.showMessageDialog(
                view, 
                "Menú de usuario del docente: " + (currentTeacher != null ? currentTeacher.getNames() : "N/A"),
                "Menú de Usuario", 
                JOptionPane.INFORMATION_MESSAGE
            );
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error en menú de usuario", e);
        }
    }

    @Override
    public void observersLoader() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}
