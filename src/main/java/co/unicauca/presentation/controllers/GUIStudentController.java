package co.unicauca.presentation.controllers;
import co.unicauca.domain.entities.User;
import co.unicauca.domain.enums.Role;
import co.unicauca.presentation.observer.ObservableBase;
import co.unicauca.presentation.views.GUIStudent;
import co.unicauca.presentation.observer.iObserver;
import co.unicauca.domain.services.SessionService;
import co.unicauca.infrastructure.dependency_injection.Controller;
import co.unicauca.infrastructure.dependency_injection.ControllerAutowired;
import java.awt.EventQueue;
import java.util.logging.Logger;
import java.util.logging.Level;
import javax.swing.JOptionPane;
@Controller
public class GUIStudentController extends ObservableBase implements iObserver {
    private static final Logger logger = Logger.getLogger(GUIStudentController.class.getName());    
    private GUIStudent view;
    private User currentStudent;    
    @ControllerAutowired
    private SessionService sessionService; 
    public GUIStudentController() {
        logger.info("StudentController constructor llamado");
        // La vista se crea cuando se necesita, no en el constructor
    }
    private void initializeView() {
        if (view == null) {
            try {
                view = new GUIStudent();
                setupEventHandlers();
                logger.info("Vista Student inicializada correctamente");
            } catch (Exception e) {
                logger.log(Level.SEVERE, "Error inicializando vista Student", e);
                throw new RuntimeException("Error creando vista de estudiante", e);
            }
        }
    }   
    private void setupEventHandlers() {
        try {
            view.setLogoutAction(this::handleLogout);
            view.setUserMenuAction(this::handleUserMenu);
            view.setMyProjectsAction(this::handleMyProjects);
            logger.info("Event handlers de Student configurados");
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error configurando event handlers", e);
        }
    }
    @Override
    public void validateNotification(ObservableBase subject, Object model) {
        logger.info("StudentController recibió notificación");
        if (model instanceof User) {
            User user = (User) model;
            if (user.getRole() == Role.STUDENT) {
                logger.info("Usuario estudiante recibido: " + user.getNames());
                this.currentStudent = user;
                EventQueue.invokeLater(() -> {
                    try {
                        initializeView();
                        loadStudentData(user);
                        view.setVisible(true);
                        logger.info("Vista de estudiante mostrada exitosamente");
                    } catch (Exception e) {
                        logger.log(Level.SEVERE, "Error mostrando vista de estudiante", e);
                        JOptionPane.showMessageDialog(
                            null, 
                            "Error al abrir la vista de estudiante: " + e.getMessage(),
                            "Error", 
                            JOptionPane.ERROR_MESSAGE
                        );
                    }
                });
            } else {
                logger.warning("Usuario recibido no es estudiante: " + user.getRole());
            }
        } else {
            logger.warning("Model recibido no es User: " + (model != null ? model.getClass() : "null"));
        }
    }
    private void loadStudentData(User student) {
        try {
            // Usar el ID como código de estudiante (se puede ajustar según el modelo)
            String studentId = student.getId() != null ? student.getId().toString() : "N/A";
            view.setStudentInfo(
                student.getNames() + " " + student.getSurnames(),
                student.getEmail(),
                student.getCareer().getDisplayName(),
                studentId
            );
            logger.info("Datos de estudiante cargados: " + student.getNames());
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error cargando datos de estudiante", e);
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
                logger.info("Cerrando sesión de estudiante: " + currentStudent.getNames());
                
                if (sessionService != null) {
                    sessionService.logout();
                } else {
                    logger.warning("SessionService es null durante logout");
                }
                
                if (view != null) {
                    view.setVisible(false);
                    view.dispose();
                    view = null;
                }
                currentStudent = null;
                logger.info("Sesión de estudiante cerrada");
                // Notificar para volver al login
                // this.notifyObservers(null); // Si necesitas notificar a alguien
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error durante logout de estudiante", e);
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
                "Menú de usuario del estudiante: " + (currentStudent != null ? currentStudent.getNames() : "N/A"),
                "Menú de Usuario", 
                JOptionPane.INFORMATION_MESSAGE
            );
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error en menú de usuario", e);
        }
    }
    private void handleMyProjects() {
        try {
            JOptionPane.showMessageDialog(
                view, 
                "Accediendo a mis proyectos...\nEstudiante: " + (currentStudent != null ? currentStudent.getNames() : "N/A"),
                "Mis Proyectos", 
                JOptionPane.INFORMATION_MESSAGE
            );
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error en mis proyectos", e);
        }
    }
    // Método para abrir la vista manualmente si es necesario
    public void showView(User student) {
        if (student != null && student.getRole() == Role.STUDENT) {
            validateNotification(null, student);
        }
    }
    @Override
    public void observersLoader() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}