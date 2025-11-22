package co.unicauca.presentation.views;
import co.unicauca.domain.entities.User;
import co.unicauca.domain.enums.Modalidad;
import co.unicauca.domain.services.SessionService;
import co.unicauca.presentation.controllers.FormatAController;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.io.File;
import java.util.function.Consumer;
public class FormatAFormView {
    private VBox root;
    private TextField tituloField;
    private ComboBox<String> modalidadCombo;
    private TextField codirectorField;
    private TextField studentEmailField;
    private TextArea objetivoGeneralArea;
    private TextArea objetivosEspecificosArea;
    private TextField archivoPdfField;
    private Label errorLabel;
    private File selectedPdfFile;
    private boolean updatingPdfField;
    private final FormatAController controller;
    public FormatAFormView(Stage stage, User user, SessionService sessionService, Consumer<User> onSuccess) {
        this.controller = new FormatAController(this, stage, sessionService, user, onSuccess);
        createUI(user);
    }
    private void createUI(User user) {
        root = new VBox(20);
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(30));
        root.setStyle("-fx-background-color: #0F4E97;");
        Label titleLabel = new Label("FORMATO A - INFORMACIÓN DEL PROYECTO");
        titleLabel.setStyle("-fx-font-size: 28px; -fx-font-weight: bold; -fx-text-fill: white;");
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background: transparent; -fx-background-color: transparent;");
        VBox formPanel = new VBox(15);
        formPanel.setAlignment(Pos.CENTER_LEFT);
        formPanel.setPadding(new Insets(30));
        formPanel.setMaxWidth(650);
        formPanel.setStyle("-fx-background-color: white; -fx-background-radius: 10;");
        Label instructionsLabel = new Label(
                "Bienvenido, " + user.getFullName().trim() + ". Debes diligenciar el Formato A para continuar.\n" +
                        "Completa la información solicitada y presiona \"Enviar Formato A\".");
        instructionsLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #333;");
        instructionsLabel.setWrapText(true);
        Label tituloLabel = new Label("Título del proyecto*");
        tituloLabel.setStyle("-fx-font-weight: bold;");
        tituloField = new TextField();
        tituloField.setPromptText("Ingrese el título del proyecto");
        tituloField.setPrefHeight(35);
        Label modalidadLabel = new Label("Modalidad*");
        modalidadLabel.setStyle("-fx-font-weight: bold;");
        modalidadCombo = new ComboBox<>();
        modalidadCombo.getItems().addAll(
                Modalidad.INVESTIGACION.getDisplayName(),
                Modalidad.PRACTICA_PROFESIONAL.getDisplayName());
        modalidadCombo.setPrefHeight(35);
        modalidadCombo.setValue(Modalidad.INVESTIGACION.getDisplayName());
        Label directorLabel = new Label("Director (correo)");
        directorLabel.setStyle("-fx-font-weight: bold;");
        TextField directorField = new TextField(user.getEmail());
        directorField.setEditable(false);
        directorField.setPrefHeight(35);
        directorField.setStyle("-fx-background-color: #F5F5F5;");
        Label codirectorLabel = new Label("Codirector (correo, opcional)");
        codirectorLabel.setStyle("-fx-font-weight: bold;");
        codirectorField = new TextField();
        codirectorField.setPromptText("Ingrese el correo del codirector si aplica");
        codirectorField.setPrefHeight(35);
        Label studentEmailLabel = new Label("Correo del estudiante*");
        studentEmailLabel.setStyle("-fx-font-weight: bold;");
        studentEmailField = new TextField();
        studentEmailField.setPromptText("Ingrese el correo del estudiante (@unicauca.edu.co)");
        studentEmailField.setPrefHeight(35);
        Label objetivoGeneralLabel = new Label("Objetivo general*");
        objetivoGeneralLabel.setStyle("-fx-font-weight: bold;");
        objetivoGeneralArea = new TextArea();
        objetivoGeneralArea.setPromptText("Describa el objetivo general del proyecto");
        objetivoGeneralArea.setWrapText(true);
        objetivoGeneralArea.setPrefRowCount(4);
        Label objetivosEspecificosLabel = new Label("Objetivos específicos*");
        objetivosEspecificosLabel.setStyle("-fx-font-weight: bold;");
        objetivosEspecificosArea = new TextArea();
        objetivosEspecificosArea.setPromptText("Ingrese cada objetivo específico en una nueva línea");
        objetivosEspecificosArea.setWrapText(true);
        objetivosEspecificosArea.setPrefRowCount(5);
        Label archivoPdfLabel = new Label("Archivo PDF (puedes adjuntar o ingresar una URL)");
        archivoPdfLabel.setStyle("-fx-font-weight: bold;");
        archivoPdfField = new TextField();
        archivoPdfField.setPromptText("Selecciona el PDF o ingresa una URL");
        archivoPdfField.setPrefHeight(35);
        archivoPdfField.textProperty().addListener((obs, oldVal, newVal) -> {
            if (!updatingPdfField) {
                selectedPdfFile = null;
            }
        });
        Button selectPdfButton = new Button("Adjuntar PDF");
        selectPdfButton.setOnAction(e -> controller.handleSelectPdf());
        Button clearPdfButton = new Button("Quitar archivo");
        clearPdfButton.setOnAction(e -> clearSelectedPdfFile());
        HBox archivoPdfBox = new HBox(10, archivoPdfField, selectPdfButton, clearPdfButton);
        archivoPdfBox.setAlignment(Pos.CENTER_LEFT);
        errorLabel = new Label();
        errorLabel.setStyle("-fx-text-fill: red; -fx-font-size: 12px;");
        errorLabel.setVisible(false);
        HBox buttonBox = new HBox(15);
        buttonBox.setAlignment(Pos.CENTER);
        Button submitButton = new Button("Enviar Formato A");
        submitButton.setPrefWidth(160);
        submitButton.setPrefHeight(40);
        submitButton.setStyle("-fx-background-color: #0F4E97; -fx-text-fill: white; -fx-font-weight: bold;");
        Button logoutButton = new Button("Cerrar Sesión");
        logoutButton.setPrefWidth(140);
        logoutButton.setPrefHeight(40);
        logoutButton.setStyle("-fx-background-color: #DC3545; -fx-text-fill: white; -fx-font-weight: bold;");
        buttonBox.getChildren().addAll(submitButton, logoutButton);
        formPanel.getChildren().addAll(
                instructionsLabel,
                tituloLabel, tituloField,
                modalidadLabel, modalidadCombo,
                directorLabel, directorField,
                codirectorLabel, codirectorField,
                studentEmailLabel, studentEmailField,
                objetivoGeneralLabel, objetivoGeneralArea,
                objetivosEspecificosLabel, objetivosEspecificosArea,
                archivoPdfLabel, archivoPdfBox,
                errorLabel,
                buttonBox);
        scrollPane.setContent(formPanel);
        scrollPane.setPrefViewportHeight(650);
        root.getChildren().addAll(titleLabel, scrollPane);
        submitButton.setOnAction(e -> controller.handleSubmit());
        logoutButton.setOnAction(e -> controller.handleLogout());
    }
    public VBox getRoot() {
        return root;
    }
    public String getTitulo() {
        return tituloField.getText().trim();
    }
    public Modalidad getModalidad() {
        String value = modalidadCombo.getValue();
        return value != null ? Modalidad.fromDisplayName(value) : null;
    }
    public String getCodirectorEmail() {
        return codirectorField.getText().trim();
    }
    public String getStudentEmail() {
        return studentEmailField.getText().trim();
    }
    public String getObjetivoGeneral() {
        return objetivoGeneralArea.getText().trim();
    }
    public String getObjetivosEspecificos() {
        return objetivosEspecificosArea.getText();
    }
    public String getArchivoPdf() {
        return archivoPdfField.getText().trim();
    }
    public File getSelectedPdfFile() {
        return selectedPdfFile;
    }
    public void setSelectedPdfFile(File file) {
        this.selectedPdfFile = file;
        updatingPdfField = true;
        archivoPdfField.setText(file != null ? file.getAbsolutePath() : "");
        updatingPdfField = false;
    }
    public void clearSelectedPdfFile() {
        setSelectedPdfFile(null);
    }
    public void showError(String message) {
        errorLabel.setText(message);
        errorLabel.setVisible(true);
    }
    public void clearError() {
        errorLabel.setVisible(false);
        errorLabel.setText("");
    }
    public void showInfo(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Formato A");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}