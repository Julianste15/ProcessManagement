package co.unicauca.presentation.controllers;

import co.unicauca.domain.entities.User;
import co.unicauca.domain.services.FormatAService;
import co.unicauca.domain.services.SessionService;
import co.unicauca.presentation.views.LoginView;
import co.unicauca.presentation.views.StudentDashboardView;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

public class StudentDashboardController {
    private static final Logger logger = Logger.getLogger(StudentDashboardController.class.getName());
    private final StudentDashboardView view;
    private final Stage stage;
    private final SessionService sessionService;
    private final FormatAService formatAService;
    private final User user;

    public StudentDashboardController(StudentDashboardView view, Stage stage, SessionService sessionService, User user) {
        this.view = view;
        this.stage = stage;
        this.sessionService = sessionService;
        this.user = user;
        this.formatAService = new FormatAService(sessionService != null ? sessionService.getClient() : null);
    }

    public void loadProjectStatus() {
        try {
            logger.info("Cargando estado del proyecto para el estudiante: " + user.getEmail());
            List<Map<String, Object>> formatos = formatAService.getFormatosPorUsuario(user.getEmail());
            
            if (formatos.isEmpty()) {
                view.showNoProjectMessage();
            } else {
                // Asumimos que el estudiante solo tiene un proyecto activo o mostramos el más reciente
                Map<String, Object> latestFormat = formatos.get(formatos.size() - 1);
                view.showProjectStatus(latestFormat);
            }
        } catch (Exception e) {
            logger.severe("Error cargando estado del proyecto: " + e.getMessage());
            view.showError("No se pudo cargar la información del proyecto.");
        }
    }

    public void handleLogout() {
        try {
            if (sessionService != null) {
                sessionService.logout();
            }
        } catch (Exception e) {
            logger.warning("Error al cerrar sesión: " + e.getMessage());
        }
        LoginView loginView = new LoginView(stage);
        Scene scene = new Scene(loginView.getRoot(), 800, 600);
        scene.getStylesheets().add(getClass().getResource("/styles/application.css").toExternalForm());
        stage.setScene(scene);
        stage.setTitle("Sistema de Gestión de Trabajos de Grado - Universidad del Cauca");
    }
}
