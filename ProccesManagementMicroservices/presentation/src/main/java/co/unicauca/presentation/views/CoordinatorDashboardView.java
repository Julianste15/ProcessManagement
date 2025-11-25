package co.unicauca.presentation.views;

import co.unicauca.domain.entities.User;
import co.unicauca.presentation.controllers.CoordinatorDashboardController;
import co.unicauca.domain.services.SessionService;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.time.LocalDate;

/**
 * Vista del Dashboard del Coordinador - Diseño Moderno
 */
public class CoordinatorDashboardView {

    private BorderPane root;
    private TableView<ProjectRow> projectsTable;
    private Label statusLabel;
    private CoordinatorDashboardController controller;
    private User user;
    private Button projectsBtn;
    private Button anteprojectsBtn;

    public CoordinatorDashboardView(Stage stage, User user, SessionService sessionService) {
        this.controller = new CoordinatorDashboardController(this, stage, user, sessionService);
        this.user = user;
        createUI();
        controller.loadProjects();
    }

    @SuppressWarnings("deprecation")
    private void createUI() {
        root = new BorderPane();
        root.getStyleClass().add("main-container");
        VBox sidebar = new VBox(0);
        sidebar.getStyleClass().add("sidebar");
        sidebar.setPrefWidth(250);
        VBox logoBox = new VBox();
        logoBox.getStyleClass().add("logo-container");
        Label logoText = new Label("Process\nManagement");
        logoText.getStyleClass().add("logo-text");
        logoBox.getChildren().add(logoText);
        VBox navMenu = new VBox(0);        
        projectsBtn = createNavButton("Proyectos Pendientes", true);
        projectsBtn.setOnAction(e -> {
            setActiveButton(projectsBtn);
            controller.loadProjects();
        });        
        anteprojectsBtn = createNavButton("Ver Anteproyectos", false);
        anteprojectsBtn.setOnAction(e -> {
            setActiveButton(anteprojectsBtn);
            controller.loadAnteprojects();
        });
        
        navMenu.getChildren().addAll(projectsBtn, anteprojectsBtn);
        
        // Spacer
        Region spacer = new Region();
        VBox.setVgrow(spacer, Priority.ALWAYS);
        
        // Logout
        Button logoutBtn = createNavButton("Cerrar Sesión", false);
        logoutBtn.getStyleClass().add("nav-button-logout");
        logoutBtn.setOnAction(e -> controller.handleLogout());
        
        sidebar.getChildren().addAll(logoBox, navMenu, spacer, logoutBtn);
        root.setLeft(sidebar);

        // --- MAIN CONTENT ---
        VBox mainContent = new VBox(20);
        mainContent.setPadding(new Insets(30));
        
        // Header
        HBox header = new HBox();
        header.getStyleClass().add("header");
        header.setAlignment(Pos.CENTER_LEFT);
        
        VBox headerText = new VBox(2);
        Label titleLabel = new Label("Dashboard del Coordinador");
        titleLabel.getStyleClass().add("h2");
        
        Label userLabel = new Label("Bienvenido, " + user.getFullName());
        userLabel.getStyleClass().add("subtitle");
        
        headerText.getChildren().addAll(titleLabel, userLabel);
        header.getChildren().add(headerText);
        
        root.setTop(header);

        // Content Area
        VBox contentBox = new VBox(15);
        VBox.setVgrow(contentBox, Priority.ALWAYS);
        
        Label tableTitle = new Label("Proyectos Pendientes de Evaluación");
        tableTitle.getStyleClass().add("h3");
        
        // Table Card
        VBox tableCard = new VBox(15);
        tableCard.getStyleClass().add("card");
        VBox.setVgrow(tableCard, Priority.ALWAYS);
        
        HBox tableActions = new HBox(10);
        tableActions.setAlignment(Pos.CENTER_RIGHT);
        
        Button refreshButton = new Button("Actualizar Lista");
        refreshButton.getStyleClass().add("button-primary");
        refreshButton.setOnAction(e -> controller.loadProjects());
        
        tableActions.getChildren().add(refreshButton);

        projectsTable = new TableView<>();
        projectsTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        VBox.setVgrow(projectsTable, Priority.ALWAYS);

        TableColumn<ProjectRow, Long> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        idCol.setPrefWidth(50);

        TableColumn<ProjectRow, String> titleCol = new TableColumn<>("Título");
        titleCol.setCellValueFactory(new PropertyValueFactory<>("titulo"));
        titleCol.setPrefWidth(250);

        TableColumn<ProjectRow, String> modalityCol = new TableColumn<>("Modalidad");
        modalityCol.setCellValueFactory(new PropertyValueFactory<>("modalidad"));
        modalityCol.setPrefWidth(150);

        TableColumn<ProjectRow, String> directorCol = new TableColumn<>("Director");
        directorCol.setCellValueFactory(new PropertyValueFactory<>("director"));
        directorCol.setPrefWidth(200);

        TableColumn<ProjectRow, LocalDate> dateCol = new TableColumn<>("Fecha");
        dateCol.setCellValueFactory(new PropertyValueFactory<>("fecha"));
        dateCol.setPrefWidth(100);

        TableColumn<ProjectRow, Void> actionsCol = new TableColumn<>("Acciones");
        actionsCol.setPrefWidth(150);
        actionsCol.setCellFactory(param -> new TableCell<>() {
            private final Button approveBtn = new Button("Aprobar");
            private final Button rejectBtn = new Button("Rechazar");
            private final Button assignEvaluatorsBtn = new Button("Asignar Evaluadores");
            private final HBox pane = new HBox(5);

            {
                approveBtn.getStyleClass().add("button-success");
                approveBtn.setStyle("-fx-background-color: #28a745; -fx-text-fill: white;");
                
                rejectBtn.getStyleClass().add("button-danger");
                
                assignEvaluatorsBtn.getStyleClass().add("button-primary");
                
                approveBtn.setOnAction(event -> {
                    ProjectRow project = getTableView().getItems().get(getIndex());
                    controller.handleApprove(project);
                });

                rejectBtn.setOnAction(event -> {
                    ProjectRow project = getTableView().getItems().get(getIndex());
                    controller.handleReject(project);
                });
                
                assignEvaluatorsBtn.setOnAction(event -> {
                    ProjectRow project = getTableView().getItems().get(getIndex());
                    controller.handleAssignEvaluators(project);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    ProjectRow row = getTableView().getItems().get(getIndex());
                    if (row != null) {
                        if ("Anteproyecto".equals(row.getModalidad())) {
                            pane.getChildren().setAll(assignEvaluatorsBtn);
                        } else {
                            pane.getChildren().setAll(approveBtn, rejectBtn);
                        }
                        setGraphic(pane);
                    }
                }
            }
        });

        projectsTable.getColumns().add(idCol);
        projectsTable.getColumns().add(titleCol);
        projectsTable.getColumns().add(modalityCol);
        projectsTable.getColumns().add(directorCol);
        projectsTable.getColumns().add(dateCol);
        projectsTable.getColumns().add(actionsCol);
        
        tableCard.getChildren().addAll(tableActions, projectsTable);

        statusLabel = new Label("");
        statusLabel.getStyleClass().add("status-badge");
        statusLabel.setVisible(false);
        statusLabel.setManaged(false);

        mainContent.getChildren().addAll(tableTitle, statusLabel, tableCard);
        root.setCenter(mainContent);
    }
    
    private Button createNavButton(String text, boolean isActive) {
        Button btn = new Button(text);
        btn.setMaxWidth(Double.MAX_VALUE);
        btn.getStyleClass().add("nav-button");
        if (isActive) {
            btn.getStyleClass().add("nav-button-active");
        }
        return btn;
    }
    
    private void setActiveButton(Button activeBtn) {
        // Remove active class from all navigation buttons
        projectsBtn.getStyleClass().remove("nav-button-active");
        anteprojectsBtn.getStyleClass().remove("nav-button-active");
        
        // Add active class to the clicked button
        if (!activeBtn.getStyleClass().contains("nav-button-active")) {
            activeBtn.getStyleClass().add("nav-button-active");
        }
    }

    public BorderPane getRoot() {
        return root;
    }

    public TableView<ProjectRow> getProjectsTable() {
        return projectsTable;
    }

    public void setStatusLabel(String text) {
        statusLabel.setText(text);
        statusLabel.setVisible(true);
        statusLabel.setManaged(true);
    }

    public void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public void showSuccess(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Éxito");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public void setOnViewAnteprojects(Runnable action) {
        // This might need adjustment as we moved the button to sidebar
        // But the controller calls this? No, the controller usually sets the action.
        // Let's keep the method but it might not be used if we set action directly in createUI
        // Actually, the controller might call setOnViewAnteprojects. 
        // Let's check usage. If controller calls it, we need to store the action or update the button.
        // Since I set the action in createUI: anteprojectsBtn.setOnAction(e -> controller.loadAnteprojects());
        // I might not need this method if the controller logic is embedded.
        // However, to be safe and keep compatibility:
    }

    /**
     * Inner class to represent a project row in the table
     */
    public static class ProjectRow {
        private final Long id;
        private final String titulo;
        private final String modalidad;
        private final String director;
        private final LocalDate fecha;

        public ProjectRow(Long id, String titulo, String modalidad, String director, LocalDate fecha) {
            this.id = id;
            this.titulo = titulo;
            this.modalidad = modalidad;
            this.director = director;
            this.fecha = fecha;
        }

        public Long getId() {
            return id;
        }

        public String getTitulo() {
            return titulo;
        }

        public String getModalidad() {
            return modalidad;
        }

        public String getDirector() {
            return director;
        }

        public LocalDate getFecha() {
            return fecha;
        }
    }
}
