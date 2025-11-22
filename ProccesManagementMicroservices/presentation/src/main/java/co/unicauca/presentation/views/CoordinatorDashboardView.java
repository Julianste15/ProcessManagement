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
 * Vista del Dashboard del Coordinador
 */
public class CoordinatorDashboardView {
    private VBox root;
    private TableView<ProjectRow> projectsTable;
    private Button refreshButton;
    private Button logoutButton;
    private Label statusLabel;
    private CoordinatorDashboardController controller;
    public CoordinatorDashboardView(Stage stage, User user, SessionService sessionService) {
        this.controller = new CoordinatorDashboardController(this, stage, user, sessionService);
        createUI(user);
    }
    private void createUI(User user) {
        root = new VBox(20);
        root.setAlignment(Pos.TOP_CENTER);
        root.setPadding(new Insets(20));
        root.setStyle("-fx-background-color: #F5F5F5;");
        VBox headerBox = new VBox(10);
        headerBox.setAlignment(Pos.CENTER);
        headerBox.setPadding(new Insets(20));
        headerBox.setStyle("-fx-background-color: #0F4E97; -fx-background-radius: 10;");
        Label titleLabel = new Label("Dashboard del Coordinador");
        titleLabel.setStyle("-fx-font-size: 28px; -fx-font-weight: bold; -fx-text-fill: white;");
        Label userLabel = new Label("Bienvenido, " + user.getFullName());
        userLabel.setStyle("-fx-font-size: 16px; -fx-text-fill: #E0E0E0;");
        headerBox.getChildren().addAll(titleLabel, userLabel);
        VBox contentBox = new VBox(15);
        contentBox.setAlignment(Pos.TOP_CENTER);
        contentBox.setPadding(new Insets(20));
        contentBox.setStyle("-fx-background-color: white; -fx-background-radius: 10;");
        contentBox.setPrefWidth(900);
        Label tableTitle = new Label("Proyectos Pendientes de Evaluación");
        tableTitle.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: #0F4E97;");
        projectsTable = new TableView<>();
        projectsTable.setPrefHeight(400);
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
            private final HBox pane = new HBox(5, approveBtn, rejectBtn);
            {
                approveBtn.setStyle("-fx-background-color: #28a745; -fx-text-fill: white; -fx-font-size: 10px;");
                rejectBtn.setStyle("-fx-background-color: #dc3545; -fx-text-fill: white; -fx-font-size: 10px;");
                approveBtn.setOnAction(event -> {
                    ProjectRow project = getTableView().getItems().get(getIndex());
                    controller.handleApprove(project);
                });
                rejectBtn.setOnAction(event -> {
                    ProjectRow project = getTableView().getItems().get(getIndex());
                    controller.handleReject(project);
                });
            }
            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : pane);
            }
        });
        projectsTable.getColumns().add(idCol);
        projectsTable.getColumns().add(titleCol);
        projectsTable.getColumns().add(modalityCol);
        projectsTable.getColumns().add(directorCol);
        projectsTable.getColumns().add(dateCol);
        projectsTable.getColumns().add(actionsCol);
        statusLabel = new Label("");
        statusLabel.setStyle("-fx-text-fill: #666; -fx-font-size: 12px;");
        HBox buttonBox = new HBox(15);
        buttonBox.setAlignment(Pos.CENTER);
        refreshButton = new Button("Actualizar Lista");
        refreshButton.setPrefWidth(150);
        refreshButton.setPrefHeight(35);
        refreshButton.setStyle("-fx-background-color: #0F4E97; -fx-text-fill: white; -fx-font-weight: bold;");
        refreshButton.setOnAction(e -> controller.loadProjects());
        logoutButton = new Button("Cerrar Sesión");
        logoutButton.setPrefWidth(150);
        logoutButton.setPrefHeight(35);
        logoutButton.setStyle("-fx-background-color: #DC3545; -fx-text-fill: white; -fx-font-weight: bold;");
        logoutButton.setOnAction(e -> controller.handleLogout());
        buttonBox.getChildren().addAll(refreshButton, logoutButton);
        contentBox.getChildren().addAll(tableTitle, projectsTable, statusLabel, buttonBox);
        root.getChildren().addAll(headerBox, contentBox);
        controller.loadProjects();
    }
    public VBox getRoot() {
        return root;
    }
    public TableView<ProjectRow> getProjectsTable() {
        return projectsTable;
    }
    public void setStatusLabel(String text) {
        statusLabel.setText(text);
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
        public Long getId() {return id;}
        public String getTitulo() {return titulo;}
        public String getModalidad() {return modalidad;}
        public String getDirector() {return director;}
        public LocalDate getFecha() {return fecha;}
    }
}