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
import javax.swing.JOptionPane;
@Controller
public class GUIStudentController implements iObserver {
    private GUIStudent view;
    private User currentStudent;
    @ControllerAutowired
    private SessionService sessionService;
    public GUIStudentController() {
        // La vista se crea cuando se necesita
    }
    private void initializeView() {
        if (view == null) {
            view = new GUIStudent();
            setupEventHandlers();
        }
    }   
    private void setupEventHandlers() {
        view.setLogoutAction(this::handleLogout);
        view.setUserMenuAction(this::handleUserMenu);
        view.setMyProjectsAction(this::handleMyProjects);
    }
    @Override
    public void validateNotification(ObservableBase subject, Object model) {
        if (model instanceof User) {
            User user = (User) model;
            if (user.getRole() == Role.STUDENT) {
                this.currentStudent = user;
                EventQueue.invokeLater(() -> {
                    initializeView();
                    loadStudentData(user);
                    view.setVisible(true);
                });
            }
        }
    }
    private void loadStudentData(User student) {
        // Usar el ID como código de estudiante (se puede ajustar según el modelo)
        String studentId = student.getId() != null ? student.getId().toString() : null;
        view.setStudentInfo(
            student.getNames() + " " + student.getSurnames(),
            student.getEmail(),
            student.getCareer().getDisplayName(),
            studentId
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
            currentStudent = null;
            // Notificar para volver al login
            // Esto depende de la implementación del patrón Observer
        }
    }
    private void handleUserMenu() {
        JOptionPane.showMessageDialog(
            view, 
            "Menú de usuario del estudiante: " + currentStudent.getNames(),
            "Menú de Usuario", 
            JOptionPane.INFORMATION_MESSAGE
        );
    }
    
    private void handleMyProjects() {
        JOptionPane.showMessageDialog(
            view, 
            "Accediendo a mis proyectos...",
            "Mis Proyectos", 
            JOptionPane.INFORMATION_MESSAGE
        );
        // Aquí implementar la lógica para mostrar proyectos del estudiante
    }
}
