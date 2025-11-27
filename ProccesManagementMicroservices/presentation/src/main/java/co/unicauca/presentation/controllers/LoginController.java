package co.unicauca.presentation.controllers;

import co.unicauca.domain.entities.User;
import co.unicauca.domain.enums.Role;
import co.unicauca.domain.services.SessionService;
import co.unicauca.presentation.views.LoginView;
import co.unicauca.presentation.views.RegisterView;
import co.unicauca.presentation.views.DashboardView;
import co.unicauca.presentation.views.CoordinatorDashboardView;
import co.unicauca.presentation.views.StudentDashboardView;
import co.unicauca.infrastructure.client.MicroserviceClient;
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

        try {
            logger.info("Intentando login para: " + email);
            User user = sessionService.login(email, password);

            if (user != null) {
                logger.info("Login exitoso para: " + email);
                if (Role.COORDINATOR.equals(user.getRole())) {
                    logger.info("Usuario es coordinador, redirigiendo a dashboard de coordinador");
                    showCoordinatorDashboard(user);
                } else if (Role.STUDENT.equals(user.getRole())) {
                    logger.info("Usuario es estudiante, redirigiendo a dashboard de estudiante");
                    showStudentDashboard(user);
                } else {
                    // Check if user is Department Head
                    MicroserviceClient client = new MicroserviceClient("http://localhost:8080");
                    client.setToken(sessionService.getToken());
                    co.unicauca.domain.services.UserService userService = new co.unicauca.domain.services.UserService(client);
                    
                    if (userService.isDepartmentHead(user.getEmail())) {
                        logger.info("Usuario es jefe de departamento, redirigiendo a dashboard de jefe");
                        showDepartmentHeadDashboard(user);
                    } else {
                        logger.info("Redirigiendo a dashboard");
                        showDashboard(user);
                    }
                }
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
        DashboardView dashboardView = new DashboardView(stage, user, sessionService);
        Scene scene = new Scene(dashboardView.getRoot(), 900, 700);
        scene.getStylesheets().add(getClass().getResource("/styles/application.css").toExternalForm());
        stage.setScene(scene);
        stage.setTitle("Dashboard - " + user.getFullName());
    }

    private void showCoordinatorDashboard(User user) {
        CoordinatorDashboardView coordinatorView = new CoordinatorDashboardView(stage, user, sessionService);
        Scene scene = new Scene(coordinatorView.getRoot(), 1000, 700);
        scene.getStylesheets().add(getClass().getResource("/styles/application.css").toExternalForm());
        stage.setScene(scene);
        stage.setTitle("Dashboard Coordinador - " + user.getFullName());
    }

    private void showStudentDashboard(User user) {
        StudentDashboardView studentView = new StudentDashboardView(stage, user, sessionService);
        Scene scene = new Scene(studentView.getRoot(), 900, 700);
        scene.getStylesheets().add(getClass().getResource("/styles/application.css").toExternalForm());
        stage.setScene(scene);
        stage.setTitle("Mi Proyecto de Grado - " + user.getFullName());
    }

    private void showDepartmentHeadDashboard(User user) {
        co.unicauca.presentation.views.DepartmentHeadDashboardView departmentHeadView = 
            new co.unicauca.presentation.views.DepartmentHeadDashboardView(stage, user, sessionService);
        Scene scene = new Scene(departmentHeadView.getRoot(), 1000, 700);
        scene.getStylesheets().add(getClass().getResource("/styles/application.css").toExternalForm());
        stage.setScene(scene);
        stage.setTitle("Dashboard Jefe de Departamento - " + user.getFullName());
    }
}