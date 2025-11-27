package co.unicauca.presentation.views;

import co.unicauca.domain.entities.User;
import co.unicauca.presentation.controllers.DepartmentHeadDashboardController;
import co.unicauca.domain.services.SessionService;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.time.LocalDate;

/**
 * Vista del Dashboard del Jefe de Departamento - Gestión de Anteproyectos
 */
public class DepartmentHeadDashboardView {

    private BorderPane root;
    private TableView<ProjectRow> projectsTable;
    private Label statusLabel;
    private DepartmentHeadDashboardController controller;
    private User user;
    private Button anteprojectsBtn;

    public DepartmentHeadDashboardView(Stage stage, User user, SessionService sessionService) {
        this.controller = new DepartmentHeadDashboardController(this, stage, user, sessionService);
        this.user = user;
        createUI();
        controller.loadAnteprojects();
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
        
        anteprojectsBtn = createNavButton("Anteproyectos", true);
        anteprojectsBtn.setOnAction(e -> {
            setActiveButton(anteprojectsBtn);
            controller.loadAnteprojects();
        });
        
        navMenu.getChildren().addAll(anteprojectsBtn);
        
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
        Label titleLabel = new Label("Dashboard Jefe de Departamento");
        titleLabel.getStyleClass().add("h2");
        
        Label userLabel = new Label("Bienvenido, " + user.getFullName());
        userLabel.getStyleClass().add("subtitle");
        
        headerText.getChildren().addAll(titleLabel, userLabel);
        header.getChildren().add(headerText);
        
        root.setTop(header);

        // Content Area
        VBox contentBox = new VBox(15);
        VBox.setVgrow(contentBox, Priority.ALWAYS);
        
        Label tableTitle = new Label("Anteproyectos Recibidos");
        tableTitle.getStyleClass().add("h3");
        
        // Table Card
        VBox tableCard = new VBox(15);
        tableCard.getStyleClass().add("card");
        VBox.setVgrow(tableCard, Priority.ALWAYS);
        
        HBox tableActions = new HBox(10);
        tableActions.setAlignment(Pos.CENTER_RIGHT);
        
        Button refreshButton = new Button("Actualizar Lista");
        refreshButton.getStyleClass().add("button-primary");
        refreshButton.setOnAction(e -> controller.loadAnteprojects());
        
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

        TableColumn<ProjectRow, String> directorCol = new TableColumn<>("Director");
        directorCol.setCellValueFactory(new PropertyValueFactory<>("director"));
        directorCol.setPrefWidth(200);

        TableColumn<ProjectRow, String> studentCol = new TableColumn<>("Estudiante");
        studentCol.setCellValueFactory(new PropertyValueFactory<>("student"));
        studentCol.setPrefWidth(200);

        TableColumn<ProjectRow, LocalDate> dateCol = new TableColumn<>("Fecha Envío");
        dateCol.setCellValueFactory(new PropertyValueFactory<>("fecha"));
        dateCol.setPrefWidth(100);

        TableColumn<ProjectRow, Void> actionsCol = new TableColumn<>("Acciones");
        actionsCol.setPrefWidth(150);
        actionsCol.setCellFactory(param -> new TableCell<>() {
            private final Button assignEvaluatorsBtn = new Button("Asignar Evaluadores");
            private final HBox pane = new HBox(5);

            {
                assignEvaluatorsBtn.getStyleClass().add("button-primary");
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
                    pane.getChildren().setAll(assignEvaluatorsBtn);
                    setGraphic(pane);
                }
            }
        });

        projectsTable.getColumns().add(idCol);
        projectsTable.getColumns().add(titleCol);
        projectsTable.getColumns().add(directorCol);
        projectsTable.getColumns().add(studentCol);
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
        anteprojectsBtn.getStyleClass().remove("nav-button-active");
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

    public static class ProjectRow {
        private final Long id;
        private final String titulo;
        private final String director;
        private final String student;
        private final LocalDate fecha;

        public ProjectRow(Long id, String titulo, String director, String student, LocalDate fecha) {
            this.id = id;
            this.titulo = titulo;
            this.director = director;
            this.student = student;
            this.fecha = fecha;
        }

        public Long getId() { return id; }
        public String getTitulo() { return titulo; }
        public String getDirector() { return director; }
        public String getStudent() { return student; }
        public LocalDate getFecha() { return fecha; }
    }
}
