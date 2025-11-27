package co.unicauca.presentation.controllers;

import co.unicauca.domain.entities.User;
import co.unicauca.domain.services.FormatAService;
import co.unicauca.domain.services.SessionService;
import co.unicauca.infrastructure.client.MicroserviceClient;
import co.unicauca.presentation.views.CoordinatorDashboardView;
import co.unicauca.presentation.views.CoordinatorDashboardView.ProjectRow;
import co.unicauca.presentation.views.LoginView;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.TextInputDialog;
import javafx.stage.Stage;
import javafx.util.StringConverter;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.logging.Logger;

/**
 * Controlador para el Dashboard del Coordinador
 */
public class CoordinatorDashboardController {

    private static final Logger logger = Logger.getLogger(CoordinatorDashboardController.class.getName());
    private final CoordinatorDashboardView view;
    private final Stage stage;
    private final User user;
    private final SessionService sessionService;
    private final FormatAService formatAService;
    private final co.unicauca.domain.services.UserService userService;

    public CoordinatorDashboardController(CoordinatorDashboardView view, Stage stage, User user,
            SessionService sessionService) {
        this.view = view;
        this.stage = stage;
        this.user = user;
        this.sessionService = sessionService;
        MicroserviceClient client = new MicroserviceClient("http://localhost:8080");
        client.setToken(sessionService.getToken());
        this.formatAService = new FormatAService(client);
        this.userService = new co.unicauca.domain.services.UserService(client);
    }

    public void loadProjects() {
        view.showProjectsView();
        view.setStatusLabel("Cargando proyectos...");
        new Thread(() -> {
            try {
                logger.info("Llamando a getProjectsForCoordinator con email: " + user.getEmail());
                List<Map<String, Object>> projects = formatAService.getProjectsForCoordinator(user.getEmail());
                logger.info("Proyectos recibidos: " + projects.size());

                Platform.runLater(() -> {
                    view.getProjectsTable().getItems().clear();
                    for (Map<String, Object> project : projects) {
                        try {
                            Long id = ((Number) project.get("id")).longValue();
                            String titulo = (String) project.get("titulo");
                            String modalidad = (String) project.get("modalidad");
                            String director = (String) project.get("directorEmail");
                            LocalDate fecha = null;
                            Object fechaObj = project.get("fechaCreacion");
                            if (fechaObj instanceof List) {
                                List<?> fechaList = (List<?>) fechaObj;
                                if (fechaList.size() >= 3) {
                                    int year = ((Number) fechaList.get(0)).intValue();
                                    int month = ((Number) fechaList.get(1)).intValue();
                                    int day = ((Number) fechaList.get(2)).intValue();
                                    fecha = LocalDate.of(year, month, day);
                                }
                            }
                            ProjectRow row = new ProjectRow(id, titulo, modalidad, director, fecha);
                            view.getProjectsTable().getItems().add(row);
                        } catch (Exception e) {
                            logger.warning("Error procesando proyecto: " + e.getMessage());
                        }
                    }
                    view.setStatusLabel("Proyectos cargados: " + projects.size());
                });
            } catch (Exception e) {
                logger.severe("Error cargando proyectos: " + e.getClass().getName());
                logger.severe("Mensaje: " + e.getMessage());
                e.printStackTrace();
                Platform.runLater(() -> {
                    String errorMsg = e.getMessage() != null ? e.getMessage() : e.getClass().getSimpleName();
                    view.showError("Error cargando proyectos: " + errorMsg);
                    view.setStatusLabel("Error al cargar proyectos");
                });
            }
        }).start();
    }

    public void loadDepartmentHeadView() {
        view.setStatusLabel("Cargando vista de jefe de departamento...");
        
        javafx.scene.layout.VBox content = new javafx.scene.layout.VBox(20);
        content.setPadding(new javafx.geometry.Insets(30));
        
        javafx.scene.control.Label title = new javafx.scene.control.Label("Asignación de Jefe de Departamento");
        title.getStyleClass().add("h3");
        
        javafx.scene.control.Label desc = new javafx.scene.control.Label("Seleccione un docente para asignar como Jefe de Departamento.");
        
        javafx.scene.control.ComboBox<User> teacherCombo = new javafx.scene.control.ComboBox<>();
        teacherCombo.setPromptText("Seleccione un docente");
        teacherCombo.setPrefWidth(300);
        teacherCombo.setConverter(new StringConverter<User>() {
            @Override
            public String toString(User user) {
                if (user == null) return null;
                return user.getNames() + " " + user.getSurnames() + " (" + user.getEmail() + ")";
            }

            @Override
            public User fromString(String string) {
                return null;
            }
        });
        
        // Cargar docentes
        new Thread(() -> {
            try {
                List<Map<String, Object>> teachers = userService.getSystemsTeachers();
                Platform.runLater(() -> {
                    for (Map<String, Object> t : teachers) {
                        User u = new User();
                        u.setId(((Number) t.get("id")).longValue());
                        u.setNames((String) t.get("names"));
                        u.setSurnames((String) t.get("surnames"));
                        u.setEmail((String) t.get("email"));
                        teacherCombo.getItems().add(u);
                    }
                });
            } catch (Exception e) {
                logger.severe("Error cargando docentes: " + e.getMessage());
                Platform.runLater(() -> view.showError("Error cargando docentes"));
            }
        }).start();
        
        javafx.scene.control.DatePicker startDate = new javafx.scene.control.DatePicker(LocalDate.now());
        startDate.setPromptText("Fecha Inicio");
        
        javafx.scene.control.DatePicker endDate = new javafx.scene.control.DatePicker(LocalDate.now().plusYears(1));
        endDate.setPromptText("Fecha Fin");
        
        javafx.scene.control.Button assignBtn = new javafx.scene.control.Button("Asignar Jefe");
        assignBtn.getStyleClass().add("button-primary");
        assignBtn.setOnAction(e -> {
            User selected = teacherCombo.getValue();
            if (selected == null || startDate.getValue() == null || endDate.getValue() == null) {
                view.showError("Todos los campos son obligatorios");
                return;
            }
            
            handleAssignDepartmentHead(selected.getId(), startDate.getValue(), endDate.getValue());
        });
        
        content.getChildren().addAll(title, desc, teacherCombo, new javafx.scene.layout.HBox(10, startDate, endDate), assignBtn);
        
        view.setCenterContent(content);
        view.setStatusLabel("");
    }

    private void handleAssignDepartmentHead(Long teacherId, LocalDate start, LocalDate end) {
        view.setStatusLabel("Asignando jefe...");
        new Thread(() -> {
            try {
                boolean success = userService.assignDepartmentHead(teacherId, start.toString() + "T00:00:00", end.toString() + "T23:59:59");
                Platform.runLater(() -> {
                    if (success) {
                        view.showSuccess("Jefe de departamento asignado exitosamente");
                    } else {
                        view.showError("No se pudo asignar el jefe de departamento");
                    }
                    view.setStatusLabel("");
                });
            } catch (Exception e) {
                logger.severe("Error asignando jefe: " + e.getMessage());
                Platform.runLater(() -> {
                    view.showError("Error: " + e.getMessage());
                    view.setStatusLabel("Error");
                });
            }
        }).start();
    }

    public void handleApprove(ProjectRow project) {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Aprobar Proyecto");
        dialog.setHeaderText("Aprobar: " + project.getTitulo());
        dialog.setContentText("Observaciones (opcional):");

        Optional<String> result = dialog.showAndWait();
        result.ifPresent(observations -> {
            new Thread(() -> {
                try {
                    formatAService.approveProject(project.getId(), observations);
                    Platform.runLater(() -> {
                        view.showSuccess("Proyecto aprobado exitosamente");
                        loadProjects();
                    });
                } catch (Exception e) {
                    logger.severe("Error aprobando proyecto: " + e.getMessage());
                    Platform.runLater(() -> view.showError("Error aprobando proyecto: " + e.getMessage()));
                }
            }).start();
        });
    }

    public void handleReject(ProjectRow project) {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Rechazar Proyecto");
        dialog.setHeaderText("Rechazar: " + project.getTitulo());
        dialog.setContentText("Observaciones (requeridas):");

        Optional<String> result = dialog.showAndWait();
        result.ifPresent(observations -> {
            if (observations.trim().isEmpty()) {
                view.showError("Las observaciones son obligatorias al rechazar un proyecto");
                return;
            }
            new Thread(() -> {
                try {
                    formatAService.rejectProject(project.getId(), observations);
                    Platform.runLater(() -> {
                        view.showSuccess("Proyecto rechazado");
                        loadProjects();
                    });
                } catch (Exception e) {
                    logger.severe("Error rechazando proyecto: " + e.getMessage());
                    Platform.runLater(() -> view.showError("Error rechazando proyecto: " + e.getMessage()));
                }
            }).start();
        });
    }

    public void handleLogout() {
        logger.info("Cerrando sesión del coordinador");
        sessionService.logout();
        LoginView loginView = new LoginView(stage);
        Scene scene = new Scene(loginView.getRoot(), 600, 500);
        scene.getStylesheets().add(getClass().getResource("/styles/application.css").toExternalForm());
        stage.setScene(scene);
        stage.setTitle("Login - Sistema de Gestión");
    }
}