package co.unicauca.presentation.controllers;

import co.unicauca.domain.entities.User;
import co.unicauca.domain.services.SessionService;
import co.unicauca.presentation.views.LoginView;
import co.unicauca.presentation.views.RegisterView;
import co.unicauca.presentation.views.DashboardView;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.util.logging.Logger;

/**
 * Controlador para la vista de Login
 */
public class LoginController {
    private static final Logger logger = Logger.getLogger(LoginController.class.getName());
    
    private LoginView view;
    private Stage stage;
    private SessionService sessionService;
    
    public LoginController(LoginView view, Stage stage) {
        this.view = view;
        this.stage = stage;
        this.sessionService = new SessionService();
    }
    
    public void handleLogin() {
        view.clearError();
        
        String email = view.getEmail();
        String password = view.getPassword();
        
        if (email.isEmpty() || password.isEmpty()) {
            view.showError("Todos los campos son obligatorios");
            return;
        }
        
        if (!email.toLowerCase().endsWith("@unicauca.edu.co")) {
            view.showError("Debe usar su email institucional (@unicauca.edu.co)");
            return;
        }
        
        if (password.length() < 6) {
            view.showError("La contraseña debe tener al menos 6 caracteres");
            return;
        }
        
        try {
            logger.info("Intentando login para: " + email);
            User user = sessionService.login(email, password);
            
            if (user != null) {
                logger.info("Login exitoso para: " + email);
                showDashboard(user);
            } else {
                view.showError("Email o contraseña incorrectos");
            }
        } catch (Exception e) {
            logger.severe("Error durante el login: " + e.getMessage());
            view.showError("Error de conexión. Verifique que los microservicios estén corriendo.");
        }
    }
    
    public void handleRegister() {
        logger.info("Navegando a pantalla de registro");
        RegisterView registerView = new RegisterView(stage);
        Scene scene = new Scene(registerView.getRoot(), 800, 700);
        scene.getStylesheets().add(getClass().getResource("/styles/application.css").toExternalForm());
        stage.setScene(scene);
    }
    
    private void showDashboard(User user) {
        DashboardView dashboardView = new DashboardView(stage, user);
        Scene scene = new Scene(dashboardView.getRoot(), 900, 700);
        scene.getStylesheets().add(getClass().getResource("/styles/application.css").toExternalForm());
        stage.setScene(scene);
        stage.setTitle("Dashboard - " + user.getFullName());
    }
}

