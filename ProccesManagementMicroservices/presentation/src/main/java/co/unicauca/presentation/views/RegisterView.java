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
 * Vista de Registro con JavaFX - Diseño Moderno
 */
public class RegisterView {
    
    private StackPane root;
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
        root = new StackPane();
        root.getStyleClass().add("main-container");
        root.setStyle("-fx-background-color: linear-gradient(to bottom right, -color-primary, #002B4E);");
        
        // Main Card
        VBox card = new VBox(20);
        card.getStyleClass().add("card");
        card.setMaxWidth(700);
        card.setMaxHeight(Region.USE_PREF_SIZE);
        card.setPadding(new Insets(30));
        card.setAlignment(Pos.CENTER);
        
        // Header
        Label titleLabel = new Label("Crear Cuenta Nueva");
        titleLabel.getStyleClass().add("h1");
        
        Label subtitleLabel = new Label("Complete el formulario para registrarse en el sistema");
        subtitleLabel.getStyleClass().add("subtitle");
        
        VBox headerBox = new VBox(5, titleLabel, subtitleLabel);
        headerBox.setAlignment(Pos.CENTER);
        
        // Form Grid (2 Columns)
        GridPane grid = new GridPane();
        grid.setHgap(20);
        grid.setVgap(15);
        grid.setAlignment(Pos.CENTER);
        
        // Column Constraints
        ColumnConstraints col1 = new ColumnConstraints();
        col1.setPercentWidth(50);
        ColumnConstraints col2 = new ColumnConstraints();
        col2.setPercentWidth(50);
        grid.getColumnConstraints().addAll(col1, col2);
        
        // Fields
        nameField = createTextField("Nombres");
        surnameField = createTextField("Apellidos");
        emailField = createTextField("Correo Institucional");
        passwordField = new PasswordField();
        passwordField.setPromptText("Contraseña");
        phoneField = createTextField("Teléfono");
        
        careerCombo = new ComboBox<>();
        careerCombo.setPromptText("Seleccione Carrera");
        careerCombo.setMaxWidth(Double.MAX_VALUE);
        careerCombo.getItems().addAll(
                Career.SYSTEMS_ENGINEERING.getDisplayName(),
                Career.ELECTRONICS_TELECOMMUNICATIONS.getDisplayName(),
                Career.INDUSTRIAL_AUTOMATION.getDisplayName(),
                Career.TELEMATICS_TECHNOLOGY.getDisplayName());
        
        roleCombo = new ComboBox<>();
        roleCombo.setPromptText("Seleccione Rol");
        roleCombo.setMaxWidth(Double.MAX_VALUE);
        roleCombo.getItems().addAll(
                Role.STUDENT.getDisplayName(),
                Role.TEACHER.getDisplayName(),
                Role.COORDINATOR.getDisplayName());
        roleCombo.setValue(Role.STUDENT.getDisplayName());

        // Add to Grid
        addFormField(grid, "Nombres *", nameField, 0, 0);
        addFormField(grid, "Apellidos *", surnameField, 1, 0);
        addFormField(grid, "Correo Electrónico *", emailField, 0, 1);
        addFormField(grid, "Contraseña *", passwordField, 1, 1);
        addFormField(grid, "Teléfono", phoneField, 0, 2);
        addFormField(grid, "Carrera *", careerCombo, 1, 2);
        addFormField(grid, "Rol *", roleCombo, 0, 3);
        GridPane.setColumnSpan(roleCombo, 2); // Role takes full width if needed, or keep it in col 1
        
        // Error Label
        errorLabel = new Label();
        errorLabel.getStyleClass().add("status-error");
        errorLabel.setVisible(false);
        errorLabel.setManaged(false);
        
        // Buttons
        HBox buttonBox = new HBox(15);
        buttonBox.setAlignment(Pos.CENTER_RIGHT);
        buttonBox.setPadding(new Insets(20, 0, 0, 0));
        
        backButton = new Button("Cancelar");
        backButton.getStyleClass().add("button-secondary");
        
        registerButton = new Button("Registrarse");
        registerButton.getStyleClass().add("button-primary");
        registerButton.setPrefWidth(150);
        
        buttonBox.getChildren().addAll(backButton, registerButton);
        
        // Assemble
        card.getChildren().addAll(headerBox, new Separator(), grid, errorLabel, buttonBox);
        
        // ScrollPane for smaller screens
        ScrollPane scrollPane = new ScrollPane(card);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background: transparent; -fx-background-color: transparent;");
        scrollPane.setPadding(new Insets(20));
        
        root.getChildren().add(scrollPane);
        StackPane.setAlignment(scrollPane, Pos.CENTER);
        
        // Events
        registerButton.setOnAction(e -> controller.handleRegister());
        backButton.setOnAction(e -> controller.handleBack());
    }
    
    private void addFormField(GridPane grid, String labelText, Control field, int col, int row) {
        VBox box = new VBox(5);
        Label label = new Label(labelText);
        label.getStyleClass().add("label-bold");
        box.getChildren().addAll(label, field);
        grid.add(box, col, row);
    }
    
    private TextField createTextField(String prompt) {
        TextField tf = new TextField();
        tf.setPromptText(prompt);
        return tf;
    }
    
    public StackPane getRoot() {
        return root;
    }
    
    public String getName() { return nameField.getText().trim(); }
    public String getSurname() { return surnameField.getText().trim(); }
    public String getEmail() { return emailField.getText().trim(); }
    public String getPassword() { return passwordField.getText(); }
    public String getPhone() { return phoneField.getText().trim(); }
    public String getCareer() { return careerCombo.getValue(); }
    public String getRole() { return roleCombo.getValue(); }
    
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