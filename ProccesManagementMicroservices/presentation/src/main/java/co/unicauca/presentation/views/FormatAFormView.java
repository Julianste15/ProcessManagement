package co.unicauca.presentation.views;

import co.unicauca.domain.entities.User;
import co.unicauca.domain.enums.Modalidad;
import co.unicauca.domain.services.SessionService;
import co.unicauca.presentation.controllers.FormatAController;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.io.File;
import java.util.function.Consumer;

/**
 * Vista del formulario de Formato A con JavaFX
 */
public class FormatAFormView {
    
    private VBox root;
    private TextField tituloField;
    private ComboBox<String> modalidadCombo;
    private TextField directorField;
    private TextField codirectorField;
    private TextField estudianteField;
    private TextArea objetivoGeneralArea;
    private TextArea objetivosEspecificosArea;
    
    // PDF Project File
    private Label pdfLabel;
    private Button selectPdfButton;
    private Label selectedFileLabel;
    
    // Company Acceptance Letter (Only for PRACTICA_PROFESIONAL)
    private Label cartaLabel;
    private Button selectCartaButton;
    private Label selectedCartaLabel;
    private VBox cartaBox;
    
    private Button submitButton;
    private Button backButton;
    private Label statusLabel;
    
    private FormatAController controller;
    private File selectedPdfFile;
    private File selectedCartaFile;
    private Long formatoAIdToResubmit;
    
    public FormatAFormView(Stage stage, User user, SessionService sessionService, Consumer<User> onSuccess) {
        this.formatoAIdToResubmit = null;
        this.controller = new FormatAController(this, stage, sessionService, user, onSuccess);
        createUI(user);
    }
    
    public FormatAFormView(Stage stage, User user, SessionService sessionService, Consumer<User> onSuccess,
                          Long formatoAId, String titulo, String modalidad, String directorEmail, String codirectorEmail,
                          String studentEmail, String objetivoGeneral, String objetivosEspecificos,
                          int intentoActual) {
        this.formatoAIdToResubmit = formatoAId;
        this.controller = new FormatAController(this, stage, sessionService, user, onSuccess);
        createUI(user);
        prefillForm(titulo, modalidad, directorEmail, codirectorEmail, studentEmail, 
                   objetivoGeneral, objetivosEspecificos, intentoActual);
    }
    
    private void createUI(User user) {
        root = new VBox(20);
        root.setAlignment(Pos.TOP_CENTER);
        root.setPadding(new Insets(30));
        root.setStyle("-fx-background-color: #F5F5F5;");
        
        // Header
        VBox headerBox = new VBox(10);
        headerBox.setAlignment(Pos.CENTER);
        headerBox.setPadding(new Insets(20));
        headerBox.setStyle("-fx-background-color: #0F4E97; -fx-background-radius: 10;");
        
        Label titleLabel = new Label("Formato A - Anteproyecto");
        titleLabel.setStyle("-fx-font-size: 28px; -fx-font-weight: bold; -fx-text-fill: white;");
        
        Label subtitleLabel = new Label("Complete la información del proyecto de grado");
        subtitleLabel.setStyle("-fx-font-size: 16px; -fx-text-fill: #E0E0E0;");
        
        headerBox.getChildren().addAll(titleLabel, subtitleLabel);
        
        // Form
        VBox formBox = new VBox(15);
        formBox.setAlignment(Pos.CENTER);
        formBox.setPadding(new Insets(30));
        formBox.setMaxWidth(700);
        formBox.setStyle("-fx-background-color: white; -fx-background-radius: 10;");
        
        // Título
        Label tituloLabel = new Label("Título del Proyecto *");
        tituloLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");
        tituloField = new TextField();
        tituloField.setPromptText("Ingrese el título del proyecto");
        tituloField.setPrefHeight(35);
        
        // Modalidad
        Label modalidadLabel = new Label("Modalidad *");
        modalidadLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");
        modalidadCombo = new ComboBox<>();
        modalidadCombo.getItems().addAll("INVESTIGACION", "PRACTICA_PROFESIONAL");
        modalidadCombo.setPromptText("Seleccione la modalidad");
        modalidadCombo.setPrefHeight(35);
        modalidadCombo.setMaxWidth(Double.MAX_VALUE);
        
        // Director (Read-only, current user)
        Label directorLabel = new Label("Email del Director *");
        directorLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");
        directorField = new TextField();
        directorField.setText(user.getEmail());
        directorField.setEditable(false);
        directorField.setStyle("-fx-background-color: #E9ECEF;");
        directorField.setPrefHeight(35);
        
        // Codirector
        Label codirectorLabel = new Label("Email del Codirector (Opcional)");
        codirectorLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");
        codirectorField = new TextField();
        codirectorField.setPromptText("codirector@unicauca.edu.co");
        codirectorField.setPrefHeight(35);
        
        // Estudiante
        Label estudianteLabel = new Label("Email del Estudiante *");
        estudianteLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");
        estudianteField = new TextField();
        estudianteField.setPromptText("estudiante@unicauca.edu.co");
        estudianteField.setPrefHeight(35);
        
        // Objetivo General
        Label objetivoGeneralLabel = new Label("Objetivo General *");
        objetivoGeneralLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");
        objetivoGeneralArea = new TextArea();
        objetivoGeneralArea.setPromptText("Describa el objetivo general del proyecto");
        objetivoGeneralArea.setPrefRowCount(3);
        objetivoGeneralArea.setWrapText(true);
        
        // Objetivos Específicos
        Label objetivosEspecificosLabel = new Label("Objetivos Específicos *");
        objetivosEspecificosLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");
        objetivosEspecificosArea = new TextArea();
        objetivosEspecificosArea.setPromptText("Liste los objetivos específicos del proyecto");
        objetivosEspecificosArea.setPrefRowCount(4);
        objetivosEspecificosArea.setWrapText(true);
        
        // PDF Selection
        pdfLabel = new Label("Archivo PDF del Anteproyecto *");
        pdfLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");
        
        selectPdfButton = new Button("Seleccionar Archivo PDF");
        selectPdfButton.setPrefWidth(200);
        selectPdfButton.setOnAction(e -> controller.handleSelectPdf());
        
        selectedFileLabel = new Label("Ningún archivo seleccionado");
        selectedFileLabel.setStyle("-fx-text-fill: #666; -fx-font-style: italic;");
        
        // Company Acceptance Letter (Hidden by default)
        cartaBox = new VBox(10);
        cartaBox.setAlignment(Pos.CENTER_LEFT);
        cartaBox.setVisible(false);
        cartaBox.setManaged(false);
        
        cartaLabel = new Label("Carta de Aceptación de la Empresa *");
        cartaLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");
        
        selectCartaButton = new Button("Seleccionar Carta");
        selectCartaButton.setPrefWidth(200);
        selectCartaButton.setOnAction(e -> controller.handleSelectCarta());
        
        selectedCartaLabel = new Label("Ningún archivo seleccionado");
        selectedCartaLabel.setStyle("-fx-text-fill: #666; -fx-font-style: italic;");
        
        cartaBox.getChildren().addAll(cartaLabel, selectCartaButton, selectedCartaLabel);
        
        // Add listener to modality combo
        modalidadCombo.valueProperty().addListener((obs, oldVal, newVal) -> {
            boolean isPractica = "PRACTICA_PROFESIONAL".equals(newVal);
            cartaBox.setVisible(isPractica);
            cartaBox.setManaged(isPractica);
        });
        
        formBox.getChildren().addAll(
            tituloLabel, tituloField,
            modalidadLabel, modalidadCombo,
            directorLabel, directorField,
            codirectorLabel, codirectorField,
            estudianteLabel, estudianteField,
            objetivoGeneralLabel, objetivoGeneralArea,
            objetivosEspecificosLabel, objetivosEspecificosArea,
            pdfLabel, selectPdfButton, selectedFileLabel,
            cartaBox
        );
        
        // Status label
        statusLabel = new Label();
        statusLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #DC3545; -fx-font-weight: bold;");
        statusLabel.setVisible(false);
        
        // Buttons
        HBox buttonBox = new HBox(15);
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.setPadding(new Insets(10));
        
        submitButton = new Button("Enviar Formato A");
        submitButton.setPrefWidth(200);
        submitButton.setPrefHeight(40);
        submitButton.setStyle("-fx-background-color: #28A745; -fx-text-fill: white; -fx-font-weight: bold;");
        
        backButton = new Button("Volver");
        backButton.setPrefWidth(150);
        backButton.setPrefHeight(40);
        backButton.setStyle("-fx-background-color: #6C757D; -fx-text-fill: white; -fx-font-weight: bold;");
        
        buttonBox.getChildren().addAll(submitButton, backButton);
        
        // Contenedor con scroll para el formulario y botones
        VBox scrollContent = new VBox(20);
        scrollContent.setAlignment(Pos.TOP_CENTER);
        scrollContent.getChildren().addAll(formBox, statusLabel, buttonBox);
        
        ScrollPane scrollPane = new ScrollPane(scrollContent);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background: #F5F5F5; -fx-background-color: #F5F5F5;");
        scrollPane.setPrefViewportHeight(600);
        VBox.setVgrow(scrollPane, Priority.ALWAYS);
        
        root.getChildren().addAll(headerBox, scrollPane);
        
        // Event handlers
        submitButton.setOnAction(e -> controller.handleSubmit());
        backButton.setOnAction(e -> controller.handleBack());
    }
    
    private void prefillForm(String titulo, String modalidad, String directorEmail, String codirectorEmail,
                            String studentEmail, String objetivoGeneral, String objetivosEspecificos,
                            int intentoActual) {
        if (titulo != null) tituloField.setText(titulo);
        if (modalidad != null) modalidadCombo.setValue(modalidad);
        if (directorEmail != null) directorField.setText(directorEmail);
        if (codirectorEmail != null) codirectorField.setText(codirectorEmail);
        if (studentEmail != null) estudianteField.setText(studentEmail);
        if (objetivoGeneral != null) objetivoGeneralArea.setText(objetivoGeneral);
        if (objetivosEspecificos != null) objetivosEspecificosArea.setText(objetivosEspecificos);
        
        submitButton.setText("Reenviar Formato A (Intento " + (intentoActual + 1) + "/3)");
    }
    
    public VBox getRoot() { return root; }
    public String getTitulo() { return tituloField.getText().trim(); }
    public Modalidad getModalidad() {
        String value = modalidadCombo.getValue();
        return value != null ? Modalidad.valueOf(value) : null;
    }
    public String getDirectorEmail() { return directorField.getText().trim(); }
    public String getCodirectorEmail() { return codirectorField.getText().trim(); }
    public String getStudentEmail() { return estudianteField.getText().trim(); }
    public String getObjetivoGeneral() { return objetivoGeneralArea.getText().trim(); }
    public String getObjetivosEspecificos() { return objetivosEspecificosArea.getText().trim(); }
    public String getArchivoPdf() { return ""; }
    
    public File getSelectedPdfFile() { return selectedPdfFile; }
    public void setSelectedPdfFile(File file) {
        this.selectedPdfFile = file;
        if (selectedFileLabel != null) {
            selectedFileLabel.setText(file != null ? file.getName() : "Ningún archivo seleccionado");
            selectedFileLabel.setStyle("-fx-text-fill: " + (file != null ? "#28A745" : "#666") + "; -fx-font-style: " + (file != null ? "normal" : "italic") + ";");
        }
    }
    
    public File getSelectedCartaFile() { return selectedCartaFile; }
    public void setSelectedCartaFile(File file) {
        this.selectedCartaFile = file;
        if (selectedCartaLabel != null) {
            selectedCartaLabel.setText(file != null ? file.getName() : "Ningún archivo seleccionado");
            selectedCartaLabel.setStyle("-fx-text-fill: " + (file != null ? "#28A745" : "#666") + "; -fx-font-style: " + (file != null ? "normal" : "italic") + ";");
        }
    }
    
    public boolean isResubmitMode() { return formatoAIdToResubmit != null; }
    public Long getFormatoAIdToResubmit() { return formatoAIdToResubmit; }
    
    public void showError(String message) {
        statusLabel.setText(message);
        statusLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #DC3545; -fx-font-weight: bold;");
        statusLabel.setVisible(true);
    }
    public void clearError() {
        statusLabel.setVisible(false);
        statusLabel.setText("");
    }
    public void showInfo(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Formato A");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    public void showStatus(String message, boolean isError) {
        statusLabel.setText(message);
        statusLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: " + (isError ? "#DC3545" : "#28A745") + "; -fx-font-weight: bold;");
        statusLabel.setVisible(true);
    }
    public void hideStatus() { statusLabel.setVisible(false); }
    public void setSubmitEnabled(boolean enabled) { submitButton.setDisable(!enabled); }
}