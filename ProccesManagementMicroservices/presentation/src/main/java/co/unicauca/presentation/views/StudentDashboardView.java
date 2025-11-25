package co.unicauca.presentation.views;

import co.unicauca.domain.entities.User;
import co.unicauca.domain.services.SessionService;
import co.unicauca.presentation.controllers.StudentDashboardController;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.util.Map;

/**
 * Vista del Dashboard del Estudiante - Diseño Moderno
 */
public class StudentDashboardView {
    
    private BorderPane root;
    private Label welcomeLabel;
    private VBox contentBox;
    private StudentDashboardController controller;
    private User user;

    public StudentDashboardView(Stage stage, User user, SessionService sessionService) {
        this.controller = new StudentDashboardController(this, stage, sessionService, user);
        this.user = user;
        createUI();
        controller.loadProjectStatus();
    }

    private void createUI() {
        root = new BorderPane();
        root.getStyleClass().add("main-container");

        // --- SIDEBAR ---
        VBox sidebar = new VBox(0);
        sidebar.getStyleClass().add("sidebar");
        sidebar.setPrefWidth(250);
        
        // Logo
        VBox logoBox = new VBox();
        logoBox.getStyleClass().add("logo-container");
        Label logoText = new Label("Process\nManagement");
        logoText.getStyleClass().add("logo-text");
        logoBox.getChildren().add(logoText);
        
        // Navigation
        VBox navMenu = new VBox(0);
        Button homeBtn = new Button("Inicio");
        homeBtn.getStyleClass().addAll("nav-button", "nav-button-active");
        homeBtn.setMaxWidth(Double.MAX_VALUE);
        navMenu.getChildren().add(homeBtn);
        
        // Spacer
        Region spacer = new Region();
        VBox.setVgrow(spacer, Priority.ALWAYS);
        
        // Logout
        Button logoutBtn = new Button("Cerrar Sesión");
        logoutBtn.getStyleClass().addAll("nav-button", "nav-button-logout");
        logoutBtn.setMaxWidth(Double.MAX_VALUE);
        logoutBtn.setOnAction(e -> controller.handleLogout());
        
        sidebar.getChildren().addAll(logoBox, navMenu, spacer, logoutBtn);
        root.setLeft(sidebar);

        // --- MAIN CONTENT ---
        VBox mainContent = new VBox(20);
        mainContent.setPadding(new Insets(30));
        mainContent.setAlignment(Pos.TOP_CENTER);
        
        // Header
        HBox header = new HBox();
        header.getStyleClass().add("header");
        header.setAlignment(Pos.CENTER_LEFT);
        
        VBox headerText = new VBox(2);
        welcomeLabel = new Label("Hola, " + user.getNames());
        welcomeLabel.getStyleClass().add("h2");
        
        Label roleLabel = new Label("Estudiante | " + user.getEmail());
        roleLabel.getStyleClass().add("subtitle");
        
        headerText.getChildren().addAll(welcomeLabel, roleLabel);
        header.getChildren().add(headerText);
        
        root.setTop(header);

        // Content Area (Card)
        contentBox = new VBox(20);
        contentBox.getStyleClass().add("card");
        contentBox.setMaxWidth(800);
        contentBox.setAlignment(Pos.CENTER);
        contentBox.setPadding(new Insets(40));
        
        mainContent.getChildren().add(contentBox);
        root.setCenter(mainContent);
    }

    public void showNoProjectMessage() {
        contentBox.getChildren().clear();
        Label messageLabel = new Label("No tienes ningún proyecto de grado registrado actualmente.");
        messageLabel.getStyleClass().add("subtitle");
        contentBox.getChildren().add(messageLabel);
    }

    public void showProjectStatus(Map<String, Object> project) {
        contentBox.getChildren().clear();

        String titulo = (String) project.get("titulo");
        String estado = (String) project.get("estado");
        Integer intentos = (Integer) project.get("intentos");
        String observaciones = (String) project.get("observaciones");

        Label titleLabel = new Label(titulo);
        titleLabel.getStyleClass().add("h3");
        titleLabel.setWrapText(true);
        titleLabel.setAlignment(Pos.CENTER);
        titleLabel.setTextAlignment(javafx.scene.text.TextAlignment.CENTER);

        // Formatear estado amigable
        String estadoAmigable = formatEstado(estado, intentos);
        Label statusLabel = new Label(estadoAmigable);
        statusLabel.getStyleClass().add("status-badge");
        
        if ("FORMATO_A_ACEPTADO".equals(estado)) {
            statusLabel.getStyleClass().add("status-success");
        } else if ("FORMATO_A_RECHAZADO".equals(estado)) {
            statusLabel.getStyleClass().add("status-error");
        } else {
            statusLabel.getStyleClass().add("status-warning");
        }
        
        VBox statusBox = new VBox(10);
        statusBox.setAlignment(Pos.CENTER);
        statusBox.getChildren().add(statusLabel);

        contentBox.getChildren().addAll(new Label("Estado del Proyecto"), titleLabel, statusBox);

        if (observaciones != null && !observaciones.isEmpty()) {
            VBox obsBox = new VBox(10);
            obsBox.getStyleClass().add("card"); // Nested card or just a box
            obsBox.setStyle("-fx-background-color: #F8F9FA; -fx-padding: 15; -fx-background-radius: 5;");
            obsBox.setMaxWidth(600);
            
            Label obsTitle = new Label("Observaciones:");
            obsTitle.getStyleClass().add("label-bold");
            
            Label obsLabel = new Label(observaciones);
            obsLabel.setWrapText(true);
            
            obsBox.getChildren().addAll(obsTitle, obsLabel);
            contentBox.getChildren().add(obsBox);
        }
    }

    private String formatEstado(String estado, Integer intentos) {
        if (estado == null) return "Estado Desconocido";
        
        switch (estado) {
            case "FORMATO_A_EN_EVALUACION":
                if (intentos == 1) return "En Primera Evaluación (Formato A)";
                if (intentos == 2) return "En Segunda Evaluación (Formato A)";
                if (intentos == 3) return "En Tercera Evaluación (Formato A)";
                return "En Evaluación (Intento " + intentos + ")";
            case "FORMATO_A_ACEPTADO":
                return "Aceptado (Formato A)";
            case "FORMATO_A_RECHAZADO":
                return "Rechazado (Formato A)";
            default:
                return estado.replace("_", " ");
        }
    }

    public void showError(String message) {
        contentBox.getChildren().clear();
        Label errorLabel = new Label(message);
        errorLabel.getStyleClass().add("status-error");
        contentBox.getChildren().add(errorLabel);
    }

    public BorderPane getRoot() {
        return root;
    }
}
