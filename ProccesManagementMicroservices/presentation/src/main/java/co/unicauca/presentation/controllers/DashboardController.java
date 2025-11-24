package co.unicauca.presentation.controllers;

import co.unicauca.domain.entities.User;
import co.unicauca.domain.services.SessionService;
import co.unicauca.presentation.views.DashboardView;
import co.unicauca.presentation.views.LoginView;
import co.unicauca.presentation.views.TeacherFormatsView;
import co.unicauca.presentation.views.FormatAFormView;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.util.logging.Logger;

/**
 * Controlador para la vista de Dashboard
 */
public class DashboardController {
    private static final Logger logger = Logger.getLogger(DashboardController.class.getName());    
    private final Stage stage;
    private final SessionService sessionService;
    private final User user;
    
    public DashboardController(DashboardView view, Stage stage, SessionService sessionService) {
        this.stage = stage;
        this.sessionService = sessionService != null ? sessionService : new SessionService();
        this.user = sessionService != null ? sessionService.getCurrentUser() : null;
    }    

    public void handleLogout() {
        try {
            logger.info("Cerrando sesión...");
            sessionService.logout();            
            LoginView loginView = new LoginView(stage);
            Scene scene = new Scene(loginView.getRoot(), 800, 600);
            scene.getStylesheets().add(getClass().getResource("/styles/application.css").toExternalForm());
            stage.setScene(scene);
            stage.setTitle("Sistema de Gestión de Trabajos de Grado - Universidad del Cauca");            
            logger.info("Sesión cerrada exitosamente");
        } catch (Exception e) {
            logger.severe("Error durante el logout: " + e.getMessage());
        }
    }
    
    public void handleViewFormats() {
        try {
            logger.info("Navegando a vista de formatos del docente");
            TeacherFormatsView formatsView = new TeacherFormatsView(stage, user, sessionService);
            Scene scene = new Scene(formatsView.getRoot(), 1000, 700);
            stage.setScene(scene);
            stage.setTitle("Mis Formatos A - Sistema de Gestión de Trabajos de Grado");
        } catch (Exception e) {
            logger.severe("Error navegando a formatos: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void handleFillFormatA() {
        try {
            logger.info("Navegando a formulario de Formato A");
            FormatAFormView formatAFormView = new FormatAFormView(stage, user, sessionService,
                    updatedUser -> {
                        DashboardView dashboardView = new DashboardView(stage, updatedUser != null ? updatedUser : user, sessionService);
                        Scene scene = new Scene(dashboardView.getRoot(), 900, 700);
                        stage.setScene(scene);
                    });
            Scene scene = new Scene(formatAFormView.getRoot(), 900, 750);
            stage.setScene(scene);
            stage.setTitle("Diligenciar Formato A - " + user.getFullName());
        } catch (Exception e) {
            logger.severe("Error navegando a formulario Formato A: " + e.getMessage());
            e.printStackTrace();
        }
    }
}