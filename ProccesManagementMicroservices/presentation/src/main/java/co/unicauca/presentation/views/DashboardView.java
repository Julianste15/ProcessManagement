package co.unicauca.presentation.views;

import co.unicauca.domain.entities.User;
import co.unicauca.domain.enums.Role;
import co.unicauca.presentation.controllers.DashboardController;
import co.unicauca.domain.services.SessionService;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

/**
 * Vista de Dashboard con JavaFX
 */
public class DashboardView {
    
    private VBox root;
    private Label welcomeLabel;
    private Label userInfoLabel;
    private Button logoutButton;
    private DashboardController controller;
    
    public DashboardView(Stage stage, User user, SessionService sessionService) {
        this.controller = new DashboardController(this, stage, sessionService);
        createUI(user);
    }
    
    private void createUI(User user) {
        root = new VBox(30);
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(40));
        root.setStyle("-fx-background-color: #F5F5F5;");
        
        // Header
        VBox headerBox = new VBox(10);
        headerBox.setAlignment(Pos.CENTER);
        headerBox.setPadding(new Insets(20));
        headerBox.setStyle("-fx-background-color: #0F4E97; -fx-background-radius: 10;");
        
        welcomeLabel = new Label("¡Bienvenido, " + user.getFullName().trim() + "!");
        welcomeLabel.setStyle("-fx-font-size: 28px; -fx-font-weight: bold; -fx-text-fill: white;");
        
        userInfoLabel = new Label();
        userInfoLabel.setStyle("-fx-font-size: 16px; -fx-text-fill: #E0E0E0;");
        
        String roleText = user.getRole() != null ? user.getRole().getDisplayName() : "Usuario";
        String emailText = user.getEmail() != null ? user.getEmail() : "";
        userInfoLabel.setText("Rol: " + roleText + " | Email: " + emailText);
        
        headerBox.getChildren().addAll(welcomeLabel, userInfoLabel);
        
        // Content area
        VBox contentBox = new VBox(20);
        contentBox.setAlignment(Pos.CENTER);
        contentBox.setPadding(new Insets(30));
        contentBox.setMaxWidth(600);
        contentBox.setStyle("-fx-background-color: white; -fx-background-radius: 10;");
        
        Label contentTitle = new Label("Dashboard");
        contentTitle.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: #0F4E97;");
        
        Label contentText = new Label("Bienvenido al Sistema de Gestión de Trabajos de Grado.\n" +
                                     "Aquí podrás gestionar tus proyectos y evaluaciones.");
        contentText.setStyle("-fx-font-size: 14px; -fx-text-fill: #666;");
        contentText.setAlignment(Pos.CENTER);
        contentText.setWrapText(true);
        
        contentBox.getChildren().addAll(contentTitle, contentText);
        
        if (user.getRole() == Role.TEACHER) {
            Label formatoALabel = new Label();
            formatoALabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #0F4E97; -fx-font-weight: bold;");
            
            String estado = user.getFormatoAEstado();
            if (estado == null || estado.isEmpty()) {
                estado = user.isRequiresFormatoA() ? "Pendiente de diligenciar" : "Sin información";
            } else {
                estado = estado.replace('_', ' ').toLowerCase();
                estado = estado.substring(0, 1).toUpperCase() + estado.substring(1);
            }
            
            formatoALabel.setText("Estado del Formato A: " + estado);
            contentBox.getChildren().add(formatoALabel);
        }
        
        // Logout button
        logoutButton = new Button("Cerrar Sesión");
        logoutButton.setPrefWidth(150);
        logoutButton.setPrefHeight(40);
        logoutButton.setStyle("-fx-background-color: #DC3545; -fx-text-fill: white; -fx-font-weight: bold;");
        
        root.getChildren().addAll(headerBox, contentBox, logoutButton);
        
        // Event handler
        logoutButton.setOnAction(e -> controller.handleLogout());
    }
    
    public VBox getRoot() {
        return root;
    }
}

