package co.unicauca.presentation.views;

import co.unicauca.domain.entities.User;
import co.unicauca.domain.services.SessionService;
import co.unicauca.presentation.controllers.StudentDashboardController;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.Map;

public class StudentDashboardView {
    private VBox root;
    private Label welcomeLabel;
    private VBox contentBox;
    private StudentDashboardController controller;

    public StudentDashboardView(Stage stage, User user, SessionService sessionService) {
        this.controller = new StudentDashboardController(this, stage, sessionService, user);
        createUI(user);
        controller.loadProjectStatus();
    }

    private void createUI(User user) {
        root = new VBox(30);
        root.setAlignment(Pos.TOP_CENTER);
        root.setPadding(new Insets(40));
        root.setStyle("-fx-background-color: #F5F5F5;");

        // Header
        VBox headerBox = new VBox(10);
        headerBox.setAlignment(Pos.CENTER);
        headerBox.setPadding(new Insets(20));
        headerBox.setStyle("-fx-background-color: #0F4E97; -fx-background-radius: 10;");

        welcomeLabel = new Label("¡Bienvenido, " + user.getFullName() + "!");
        welcomeLabel.setStyle("-fx-font-size: 28px; -fx-font-weight: bold; -fx-text-fill: white;");

        Label roleLabel = new Label("Estudiante");
        roleLabel.setStyle("-fx-font-size: 16px; -fx-text-fill: #E0E0E0;");

        headerBox.getChildren().addAll(welcomeLabel, roleLabel);

        // Content Area
        contentBox = new VBox(20);
        contentBox.setAlignment(Pos.CENTER);
        contentBox.setPadding(new Insets(30));
        contentBox.setMaxWidth(800);
        contentBox.setStyle("-fx-background-color: white; -fx-background-radius: 10; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.1), 10, 0, 0, 0);");

        Button logoutButton = new Button("Cerrar Sesión");
        logoutButton.setPrefWidth(150);
        logoutButton.setPrefHeight(40);
        logoutButton.setStyle("-fx-background-color: #DC3545; -fx-text-fill: white; -fx-font-weight: bold;");
        logoutButton.setOnAction(e -> controller.handleLogout());

        root.getChildren().addAll(headerBox, contentBox, logoutButton);
    }

    public void showNoProjectMessage() {
        contentBox.getChildren().clear();
        Label messageLabel = new Label("No tienes ningún proyecto de grado registrado actualmente.");
        messageLabel.setStyle("-fx-font-size: 18px; -fx-text-fill: #666;");
        contentBox.getChildren().add(messageLabel);
    }

    public void showProjectStatus(Map<String, Object> project) {
        contentBox.getChildren().clear();

        String titulo = (String) project.get("titulo");
        String estado = (String) project.get("estado");
        Integer intentos = (Integer) project.get("intentos");
        String observaciones = (String) project.get("observaciones");

        Label titleLabel = new Label("Proyecto: " + titulo);
        titleLabel.setStyle("-fx-font-size: 22px; -fx-font-weight: bold; -fx-text-fill: #0F4E97;");
        titleLabel.setWrapText(true);

        // Formatear estado amigable
        String estadoAmigable = formatEstado(estado, intentos);
        Label statusLabel = new Label(estadoAmigable);
        statusLabel.setStyle(getStatusStyle(estado));
        statusLabel.setPadding(new Insets(10, 20, 10, 20));
        
        VBox statusBox = new VBox(10);
        statusBox.setAlignment(Pos.CENTER);
        statusBox.getChildren().add(statusLabel);

        contentBox.getChildren().addAll(titleLabel, statusBox);

        if (observaciones != null && !observaciones.isEmpty()) {
            Label obsTitle = new Label("Observaciones:");
            obsTitle.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");
            
            Label obsLabel = new Label(observaciones);
            obsLabel.setWrapText(true);
            obsLabel.setStyle("-fx-font-style: italic; -fx-text-fill: #555;");
            
            VBox obsBox = new VBox(5);
            obsBox.setAlignment(Pos.CENTER_LEFT);
            obsBox.setStyle("-fx-background-color: #F8F9FA; -fx-padding: 15; -fx-background-radius: 5; -fx-border-color: #DEE2E6; -fx-border-radius: 5;");
            obsBox.getChildren().addAll(obsTitle, obsLabel);
            obsBox.setMaxWidth(600);
            
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

    private String getStatusStyle(String estado) {
        String baseStyle = "-fx-font-size: 18px; -fx-font-weight: bold; -fx-background-radius: 20; -fx-text-fill: white;";
        if ("FORMATO_A_ACEPTADO".equals(estado)) {
            return baseStyle + " -fx-background-color: #28A745;"; // Verde
        } else if ("FORMATO_A_RECHAZADO".equals(estado)) {
            return baseStyle + " -fx-background-color: #DC3545;"; // Rojo
        } else {
            return baseStyle + " -fx-background-color: #FFC107; -fx-text-fill: black;"; // Amarillo
        }
    }

    public void showError(String message) {
        contentBox.getChildren().clear();
        Label errorLabel = new Label(message);
        errorLabel.setStyle("-fx-text-fill: red; -fx-font-size: 16px;");
        contentBox.getChildren().add(errorLabel);
    }

    public VBox getRoot() {
        return root;
    }
}
