package co.unicauca.presentation.views;

import co.unicauca.presentation.controllers.LoginController;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

/**
 * Vista de Login con JavaFX - Diseño Moderno
 */
public class LoginView {
    
    private StackPane root;
    private TextField emailField;
    private PasswordField passwordField;
    private Button loginButton;
    private Button registerButton;
    private Label errorLabel;
    private LoginController controller;
    
    public LoginView(Stage stage) {
        this.controller = new LoginController(this, stage);
        createUI();
    }
    
    private void createUI() {
        // Root container with background color
        root = new StackPane();
        root.getStyleClass().add("main-container");
        root.setStyle("-fx-background-color: linear-gradient(to bottom right, -color-primary, #002B4E);");

        // Login Card
        VBox loginCard = new VBox(20);
        loginCard.getStyleClass().add("card");
        loginCard.setMaxWidth(400);
        loginCard.setMaxHeight(Region.USE_PREF_SIZE);
        loginCard.setAlignment(Pos.CENTER_LEFT);
        loginCard.setPadding(new Insets(40));

        // Header Section
        VBox headerBox = new VBox(5);
        headerBox.setAlignment(Pos.CENTER);
        
        Label titleLabel = new Label("Bienvenido");
        titleLabel.getStyleClass().add("h1");
        
        Label subtitleLabel = new Label("Sistema de Gestión de Trabajos de Grado");
        subtitleLabel.getStyleClass().add("subtitle");
        
        headerBox.getChildren().addAll(titleLabel, subtitleLabel);

        // Form Section
        VBox formBox = new VBox(15);
        formBox.setAlignment(Pos.CENTER_LEFT);
        
        // Email Input
        VBox emailGroup = new VBox(5);
        Label emailLabel = new Label("Correo Institucional");
        emailLabel.getStyleClass().add("label-bold");
        emailField = new TextField();
        emailField.setPromptText("usuario@unicauca.edu.co");
        emailGroup.getChildren().addAll(emailLabel, emailField);
        
        // Password Input
        VBox passwordGroup = new VBox(5);
        Label passwordLabel = new Label("Contraseña");
        passwordLabel.getStyleClass().add("label-bold");
        passwordField = new PasswordField();
        passwordField.setPromptText("Ingrese su contraseña");
        passwordGroup.getChildren().addAll(passwordLabel, passwordField);
        
        // Error Message
        errorLabel = new Label();
        errorLabel.getStyleClass().add("status-error");
        errorLabel.setWrapText(true);
        errorLabel.setMaxWidth(320);
        errorLabel.setVisible(false);
        errorLabel.setManaged(false); // Don't take space when hidden

        formBox.getChildren().addAll(emailGroup, passwordGroup, errorLabel);

        // Actions Section
        VBox actionsBox = new VBox(10);
        actionsBox.setAlignment(Pos.CENTER);
        actionsBox.setPadding(new Insets(10, 0, 0, 0));
        
        loginButton = new Button("Iniciar Sesión");
        loginButton.getStyleClass().add("button-primary");
        loginButton.setMaxWidth(Double.MAX_VALUE);
        
        registerButton = new Button("Crear una cuenta");
        registerButton.getStyleClass().add("button-secondary");
        registerButton.setMaxWidth(Double.MAX_VALUE);
        
        actionsBox.getChildren().addAll(loginButton, registerButton);

        // Assemble Card
        loginCard.getChildren().addAll(headerBox, new Separator(), formBox, actionsBox);

        // Add to Root
        root.getChildren().add(loginCard);
        
        // Event Handlers
        loginButton.setOnAction(e -> controller.handleLogin());
        registerButton.setOnAction(e -> controller.handleRegister());
        passwordField.setOnAction(e -> controller.handleLogin());
        emailField.setOnAction(e -> passwordField.requestFocus());
    }
    
    public StackPane getRoot() {
        return root;
    }
    
    public String getEmail() {
        return emailField.getText().trim();
    }
    
    public String getPassword() {
        return passwordField.getText();
    }
    
    public void showError(String message) {
        errorLabel.setText(message);
        errorLabel.setVisible(true);
        errorLabel.setManaged(true);
    }
    
    public void clearError() {
        errorLabel.setVisible(false);
        errorLabel.setManaged(false);
        errorLabel.setText("");
    }
    
    public void clearFields() {
        emailField.clear();
        passwordField.clear();
        clearError();
    }
}