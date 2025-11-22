package co.unicauca.presentation.views;
import co.unicauca.presentation.controllers.LoginController;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
/**
 * Vista de Login con JavaFX
 */
public class LoginView {    
    private VBox root;
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
        root = new VBox(20);
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(40));
        root.setStyle("-fx-background-color: #0F4E97;");
        Label titleLabel = new Label("BIENVENIDO");
        titleLabel.setStyle("-fx-font-size: 32px; -fx-font-weight: bold; -fx-text-fill: white;");
        Label subtitleLabel = new Label("Sistema de Gestión de Trabajos de Grado");
        subtitleLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #E0E0E0;");
        VBox formPanel = new VBox(15);
        formPanel.setAlignment(Pos.CENTER);
        formPanel.setPadding(new Insets(30));
        formPanel.setMaxWidth(400);
        formPanel.setStyle("-fx-background-color: white; -fx-background-radius: 10;");
        Label emailLabel = new Label("Correo electrónico*");
        emailLabel.setStyle("-fx-font-weight: bold;");
        emailField = new TextField();
        emailField.setPromptText("usuario@unicauca.edu.co");
        emailField.setPrefHeight(35);
        Label passwordLabel = new Label("Contraseña*");
        passwordLabel.setStyle("-fx-font-weight: bold;");
        passwordField = new PasswordField();
        passwordField.setPromptText("Ingrese su contraseña");
        passwordField.setPrefHeight(35);
        errorLabel = new Label();
        errorLabel.setStyle("-fx-text-fill: red; -fx-font-size: 12px;");
        errorLabel.setVisible(false);        
        HBox buttonBox = new HBox(15);
        buttonBox.setAlignment(Pos.CENTER);        
        registerButton = new Button("Registrarse");
        registerButton.setPrefWidth(120);
        registerButton.setPrefHeight(35);
        registerButton.setStyle("-fx-background-color: #D9EDF7; -fx-text-fill: #0F4E97; -fx-font-weight: bold;");
        loginButton = new Button("Iniciar Sesión");
        loginButton.setPrefWidth(120);
        loginButton.setPrefHeight(35);
        loginButton.setStyle("-fx-background-color: #0F4E97; -fx-text-fill: white; -fx-font-weight: bold;");
        buttonBox.getChildren().addAll(registerButton, loginButton);
        formPanel.getChildren().addAll(
            emailLabel, emailField,
            passwordLabel, passwordField,
            errorLabel,
            buttonBox
        );
        root.getChildren().addAll(titleLabel, subtitleLabel, formPanel);        
        loginButton.setOnAction(e -> controller.handleLogin());
        registerButton.setOnAction(e -> controller.handleRegister());
        passwordField.setOnAction(e -> controller.handleLogin());
    }
    public VBox getRoot() {
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
    }    
    public void clearError() {
        errorLabel.setVisible(false);
        errorLabel.setText("");
    }    
    public void clearFields() {
        emailField.clear();
        passwordField.clear();
        clearError();
    }
}