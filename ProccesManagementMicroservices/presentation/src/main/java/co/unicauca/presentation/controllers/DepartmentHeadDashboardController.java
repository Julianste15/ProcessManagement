package co.unicauca.presentation.controllers;

import co.unicauca.domain.entities.User;
import co.unicauca.domain.services.SessionService;
import co.unicauca.infrastructure.client.MicroserviceClient;
import co.unicauca.presentation.views.DepartmentHeadDashboardView;
import co.unicauca.presentation.views.DepartmentHeadDashboardView.ProjectRow;
import co.unicauca.presentation.views.LoginView;
import co.unicauca.presentation.views.EvaluatorAssignmentDialog;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.util.Pair;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.logging.Logger;

/**
 * Controlador para el Dashboard del Jefe de Departamento
 */
public class DepartmentHeadDashboardController {

    private static final Logger logger = Logger.getLogger(DepartmentHeadDashboardController.class.getName());
    private final DepartmentHeadDashboardView view;
    private final Stage stage;
    private final SessionService sessionService;
    private final co.unicauca.domain.services.AnteprojectService anteprojectService;
    private final co.unicauca.domain.services.UserService userService;

    public DepartmentHeadDashboardController(DepartmentHeadDashboardView view, Stage stage, User user,
            SessionService sessionService) {
        this.view = view;
        this.stage = stage;
        this.sessionService = sessionService;
        MicroserviceClient client = new MicroserviceClient("http://localhost:8080");
        client.setToken(sessionService.getToken());
        this.anteprojectService = new co.unicauca.domain.services.AnteprojectService(client);
        this.userService = new co.unicauca.domain.services.UserService(client);
    }

    public void loadAnteprojects() {
        view.setStatusLabel("Cargando anteproyectos...");
        new Thread(() -> {
            try {
                logger.info("Cargando anteproyectos enviados para el jefe de departamento");
                List<Map<String, Object>> anteprojects = anteprojectService.getSubmittedAnteprojects();

                logger.info("Anteproyectos recibidos: " + anteprojects.size());

                Platform.runLater(() -> {
                    view.getProjectsTable().getItems().clear();

                    for (Map<String, Object> ante : anteprojects) {
                        try {
                            Long id = ((Number) ante.get("id")).longValue();
                            String titulo = (String) ante.get("titulo");
                            String director = (String) ante.get("directorEmail");
                            String student = (String) ante.get("studentEmail"); // Nuevo campo

                            LocalDate fecha = null;
                            Object fechaObj = ante.get("submissionDate");
                            if (fechaObj instanceof List) {
                                List<?> fechaList = (List<?>) fechaObj;
                                if (fechaList.size() >= 3) {
                                    int year = ((Number) fechaList.get(0)).intValue();
                                    int month = ((Number) fechaList.get(1)).intValue();
                                    int day = ((Number) fechaList.get(2)).intValue();
                                    fecha = LocalDate.of(year, month, day);
                                }
                            }

                            // Usar el constructor actualizado con student
                            ProjectRow row = new ProjectRow(id, titulo, director, student, fecha);
                            view.getProjectsTable().getItems().add(row);
                        } catch (Exception ex) {
                            logger.warning("Error procesando anteproyecto: " + ex.getMessage());
                        }
                    }

                    view.setStatusLabel("Anteproyectos cargados: " + anteprojects.size());
                });
            } catch (Exception e) {
                logger.severe("Error cargando anteproyectos: " + e.getMessage());
                Platform.runLater(() -> {
                    String errorMsg = e.getMessage() != null ? e.getMessage() : e.getClass().getSimpleName();
                    view.showError("Error cargando anteproyectos: " + errorMsg);
                    view.setStatusLabel("Error al cargar anteproyectos");
                });
            }
        }).start();
    }

    public void handleAssignEvaluators(ProjectRow project) {
        view.setStatusLabel("Cargando docentes...");
        new Thread(() -> {
            try {
                List<Map<String, Object>> teachers = userService.getSystemsTeachers();
                
                Platform.runLater(() -> {
                    view.setStatusLabel("");
                    EvaluatorAssignmentDialog dialog = new EvaluatorAssignmentDialog(teachers);
                    
                    Optional<Pair<String, String>> result = dialog.showAndWait();
                    
                    result.ifPresent(evaluators -> {
                        assignEvaluators(project.getId(), evaluators.getKey(), evaluators.getValue());
                    });
                });
            } catch (Exception e) {
                logger.severe("Error cargando docentes: " + e.getMessage());
                Platform.runLater(() -> {
                    view.showError("Error cargando lista de docentes: " + e.getMessage());
                    view.setStatusLabel("Error al cargar docentes");
                });
            }
        }).start();
    }

    private void assignEvaluators(Long anteprojectId, String evaluator1, String evaluator2) {
        view.setStatusLabel("Asignando evaluadores...");
        new Thread(() -> {
            try {
                anteprojectService.assignEvaluators(anteprojectId, evaluator1, evaluator2);
                Platform.runLater(() -> {
                    view.showSuccess("Evaluadores asignados exitosamente");
                    loadAnteprojects();
                });
            } catch (Exception e) {
                logger.severe("Error asignando evaluadores: " + e.getMessage());
                Platform.runLater(() -> {
                    view.showError("Error asignando evaluadores: " + e.getMessage());
                    view.setStatusLabel("Error en asignación");
                });
            }
        }).start();
    }

    public void handleLogout() {
        logger.info("Cerrando sesión del jefe de departamento");
        sessionService.logout();
        LoginView loginView = new LoginView(stage);
        Scene scene = new Scene(loginView.getRoot(), 600, 500);
        scene.getStylesheets().add(getClass().getResource("/styles/application.css").toExternalForm());
        stage.setScene(scene);
        stage.setTitle("Login - Sistema de Gestión");
    }
}