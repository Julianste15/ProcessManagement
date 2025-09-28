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
import javax.swing.JOptionPane;
@Controller
public class GUITeacherController implements iObserver {
    private GUITeacher view;
    private User currentTeacher;
    @ControllerAutowired
    private SessionService sessionService;
    public GUITeacherController() {
        // La vista se crea cuando se necesita
    }
    private void initializeView() {
        if (view == null) {
            view = new GUITeacher();
            setupEventHandlers();
        }
    }
    private void setupEventHandlers() {
        view.setLogoutAction(this::handleLogout);
        view.setUserMenuAction(this::handleUserMenu);
    }
    @Override
    public void validateNotification(ObservableBase subject, Object model) {
        if (model instanceof User) {
            User user = (User) model;
            if (user.getRole() == Role.TEACHER) {
                this.currentTeacher = user;
                EventQueue.invokeLater(() -> {
                    initializeView();
                    loadTeacherData(user);
                    view.setVisible(true);
                });
            }
        }
    }
    private void loadTeacherData(User teacher) {
        view.setUserInfo(
            teacher.getNames() + " " + teacher.getSurnames(),
            teacher.getEmail(),
            teacher.getCareer().getDisplayName()
        );
    }
    private void handleLogout() {
        int option = JOptionPane.showConfirmDialog(
            view, 
            "¿Está seguro que desea cerrar sesión?", 
            "Confirmar cierre de sesión", 
            JOptionPane.YES_NO_OPTION
        );
        
        if (option == JOptionPane.YES_OPTION) {
            sessionService.logout();
            view.setVisible(false);
            view.dispose();
            view = null;
            currentTeacher = null;
            
            // Notificar para volver al login
            // Esto depende de tu implementación del patrón Observer
        }
    }
    private void handleUserMenu() {
        JOptionPane.showMessageDialog(
            view, 
            "Menú de usuario del docente: " + currentTeacher.getNames(),
            "Menú de Usuario", 
            JOptionPane.INFORMATION_MESSAGE
        );
    }
}
