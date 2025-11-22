package co.unicauca.presentation.views;
import co.unicauca.domain.enums.Career;
import co.unicauca.domain.enums.Role;
import co.unicauca.presentation.controllers.RegisterController;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
/**
 * Vista de Registro con JavaFX
 */
public class RegisterView {
    private VBox root;
    private TextField nameField;
    private TextField surnameField;
    private TextField emailField;
    private PasswordField passwordField;
    private TextField phoneField;
    private ComboBox<String> careerCombo;
    private ComboBox<String> roleCombo;
    private Button registerButton;
    private Button backButton;
    private Label errorLabel;
    private RegisterController controller;
    public RegisterView(Stage stage) {
        this.controller = new RegisterController(this, stage);
        createUI();
    }
    private void createUI() {
        root = new VBox(20);
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(30));
        root.setStyle("-fx-background-color: #0F4E97;");
        Label titleLabel = new Label("REGISTRO DE USUARIO");
        titleLabel.setStyle("-fx-font-size: 28px; -fx-font-weight: bold; -fx-text-fill: white;");
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background: transparent; -fx-background-color: transparent;");
        VBox formPanel = new VBox(15);
        formPanel.setAlignment(Pos.CENTER);
        formPanel.setPadding(new Insets(30));
        formPanel.setMaxWidth(500);
        formPanel.setStyle("-fx-background-color: white; -fx-background-radius: 10;");
        Label nameLabel = new Label("Nombres*");
        nameLabel.setStyle("-fx-font-weight: bold;");
        nameField = new TextField();
        nameField.setPromptText("Ingrese sus nombres");
        nameField.setPrefHeight(35);
        Label surnameLabel = new Label("Apellidos*");
        surnameLabel.setStyle("-fx-font-weight: bold;");
        surnameField = new TextField();
        surnameField.setPromptText("Ingrese sus apellidos");
        surnameField.setPrefHeight(35);
        Label emailLabel = new Label("Correo electrónico*");
        emailLabel.setStyle("-fx-font-weight: bold;");
        emailField = new TextField();
        emailField.setPromptText("usuario@unicauca.edu.co");
        emailField.setPrefHeight(35);
        Label passwordLabel = new Label("Contraseña*");
        passwordLabel.setStyle("-fx-font-weight: bold;");
        passwordField = new PasswordField();
        passwordField.setPromptText("Mínimo 6 caracteres");
        passwordField.setPrefHeight(35);    
        Label phoneLabel = new Label("Teléfono (opcional)");
        phoneLabel.setStyle("-fx-font-weight: bold;");
        phoneField = new TextField();
        phoneField.setPromptText("Número de teléfono");
        phoneField.setPrefHeight(35);
        Label careerLabel = new Label("Carrera*");
        careerLabel.setStyle("-fx-font-weight: bold;");
        careerCombo = new ComboBox<>();
        careerCombo.getItems().addAll(
                Career.SYSTEMS_ENGINEERING.getDisplayName(),
                Career.ELECTRONICS_TELECOMMUNICATIONS.getDisplayName(),
                Career.INDUSTRIAL_AUTOMATION.getDisplayName(),
                Career.TELEMATICS_TECHNOLOGY.getDisplayName());
        careerCombo.setPrefHeight(35);
        careerCombo.setValue(Career.SYSTEMS_ENGINEERING.getDisplayName());
        Label roleLabel = new Label("Rol*");
        roleLabel.setStyle("-fx-font-weight: bold;");
        roleCombo = new ComboBox<>();
        roleCombo.getItems().addAll(
                Role.STUDENT.getDisplayName(),
                Role.TEACHER.getDisplayName(),
                Role.COORDINATOR.getDisplayName());
        roleCombo.setPrefHeight(35);
        roleCombo.setValue(Role.STUDENT.getDisplayName());
        errorLabel = new Label();
        errorLabel.setStyle("-fx-text-fill: red; -fx-font-size: 12px;");
        errorLabel.setVisible(false);
        HBox buttonBox = new HBox(15);
        buttonBox.setAlignment(Pos.CENTER);
        backButton = new Button("Volver");
        backButton.setPrefWidth(120);
        backButton.setPrefHeight(35);
        backButton.setStyle("-fx-background-color: #D9EDF7; -fx-text-fill: #0F4E97; -fx-font-weight: bold;");
        registerButton = new Button("Registrarse");
        registerButton.setPrefWidth(120);
        registerButton.setPrefHeight(35);
        registerButton.setStyle("-fx-background-color: #0F4E97; -fx-text-fill: white; -fx-font-weight: bold;");
        buttonBox.getChildren().addAll(backButton, registerButton);
        formPanel.getChildren().addAll(
                nameLabel, nameField,
                surnameLabel, surnameField,
                emailLabel, emailField,
                passwordLabel, passwordField,
                phoneLabel, phoneField,
                careerLabel, careerCombo,
                roleLabel, roleCombo,
                errorLabel,
                buttonBox);
        scrollPane.setContent(formPanel);
        scrollPane.setPrefViewportHeight(600);
        root.getChildren().addAll(titleLabel, scrollPane);
        registerButton.setOnAction(e -> controller.handleRegister());
        backButton.setOnAction(e -> controller.handleBack());
    }
    public VBox getRoot() {
        return root;
    }
    public String getName() {
        return nameField.getText().trim();
    }
    public String getSurname() {
        return surnameField.getText().trim();
    }
    public String getEmail() {
        return emailField.getText().trim();
    }
    public String getPassword() {
        return passwordField.getText();
    }
    public String getPhone() {
        return phoneField.getText().trim();
    }
    public String getCareer() {
        return careerCombo.getValue();
    }
    public String getRole() {
        return roleCombo.getValue();
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
        nameField.clear();
        surnameField.clear();
        emailField.clear();
        passwordField.clear();
        phoneField.clear();
        careerCombo.setValue(Career.SYSTEMS_ENGINEERING.getDisplayName());
        roleCombo.setValue(Role.STUDENT.getDisplayName());
        clearError();
    }
}