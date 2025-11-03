package co.unicauca.presentation.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import co.unicauca.domain.entities.User;
import co.unicauca.domain.enums.Role;
import co.unicauca.domain.services.SessionService;
import co.unicauca.presentation.observer.ObservableBase;
import co.unicauca.presentation.views.GUITeacher;
import co.unicauca.presentation.observer.iObserver;
import java.awt.EventQueue;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

@Component
public class GUITeacherController extends ObservableBase implements iObserver {
    private static final Logger logger = Logger.getLogger(GUITeacherController.class.getName());
    private GUITeacher view;
    private User currentTeacher;

    private final SessionService sessionService;
    private final GUILoginController loginController;

    @Autowired
    public GUITeacherController(SessionService sessionService, GUILoginController loginController) {
        this.sessionService = sessionService;
        this.loginController = loginController;
        logger.info("TeacherController constructor llamado e inyectado por Spring");
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
        logger.info("TeacherController recibio notificacion");
        
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
                "¿Esta seguro que desea cerrar sesion?", 
                "Confirmar cierre de sesion", 
                JOptionPane.YES_NO_OPTION
            );
            
            if (option == JOptionPane.YES_OPTION) {
                logger.info("Cerrando sesion de profesor: " + currentTeacher.getNames());
                
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
                
                currentTeacher = null;
                
                if (loginController != null) {
                    logger.info("Notificando a LoginController para volver al login");
                    loginController.showLogin();
                } 
                
                logger.info("Sesion de profesor cerrada exitosamente");
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error durante logout de profesor", e);
            JOptionPane.showMessageDialog(
                view, 
                "Error al cerrar sesion: " + e.getMessage(),
                "Error", 
                JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void handleUserMenu() {
        try {
            JOptionPane.showMessageDialog(
                view, 
                "Menu de usuario del docente: " + (currentTeacher != null ? currentTeacher.getNames() : "N/A"),
                "Menu de Usuario", 
                JOptionPane.INFORMATION_MESSAGE
            );
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error en menu de usuario", e);
        }
    }

    @Override
    public void observersLoader() {
        logger.warning("observersLoader() no implementado en GUITeacherController.");
    }
}