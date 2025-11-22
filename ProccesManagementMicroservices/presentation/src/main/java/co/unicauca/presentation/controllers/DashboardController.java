package co.unicauca.presentation.controllers;
import co.unicauca.domain.services.SessionService;
import co.unicauca.presentation.views.DashboardView;
import co.unicauca.presentation.views.LoginView;
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
    public DashboardController(DashboardView view, Stage stage, SessionService sessionService) {
        this.stage = stage;
        this.sessionService = sessionService != null ? sessionService : new SessionService();
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
}