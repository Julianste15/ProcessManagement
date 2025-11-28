package co.unicauca.presentation.views;

import co.unicauca.domain.entities.User;
import co.unicauca.domain.services.SessionService;
import co.unicauca.presentation.controllers.AnteprojectFormController;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import java.io.File;
import java.util.function.Consumer;

/**
 * Vista del formulario para crear y subir un anteproyecto.
 * Se muestra al profesor cuando decide subir el anteproyecto asociado a un Formato A.
 */
public class AnteprojectFormView {
    private VBox root;
    private TextField tituloField;
    private TextField estudianteField;
    private TextField directorField;
    private Label pdfLabel;
    private Button selectPdfButton;
    private Label selectedFileLabel;
    private Button submitButton;
    private Button backButton;
    private Label statusLabel;
    private File selectedPdfFile;
    private Long formatoAId;

    private final AnteprojectFormController controller;

    public AnteprojectFormView(Stage stage, User user, SessionService sessionService,
                               Consumer<User> onSuccess, Runnable onBack, Long formatoAId,
                               String tituloPrefill, String studentEmailPrefill) {
        this.formatoAId = formatoAId;
        this.controller = new AnteprojectFormController(this, stage, sessionService, user, onSuccess, onBack, formatoAId);
        createUI(tituloPrefill, studentEmailPrefill);
    }

    private void createUI(String tituloPrefill, String studentEmailPrefill) {
        root = new VBox(20);
        root.setAlignment(Pos.TOP_CENTER);
        root.setPadding(new Insets(30));
        root.setStyle("-fx-background-color: #F5F5F5;");

        // Header
        VBox headerBox = new VBox(10);
        headerBox.setAlignment(Pos.CENTER);
        headerBox.setPadding(new Insets(20));
        headerBox.setStyle("-fx-background-color: #0F4E97; -fx-background-radius: 10;");
        Label titleLabel = new Label("Anteproyecto");
        titleLabel.setStyle("-fx-font-size: 28px; -fx-font-weight: bold; -fx-text-fill: white;");
        headerBox.getChildren().add(titleLabel);

        // Form fields
        VBox formBox = new VBox(15);
        formBox.setAlignment(Pos.CENTER);
        formBox.setPadding(new Insets(30));
        formBox.setMaxWidth(700);
        formBox.setStyle("-fx-background-color: white; -fx-background-radius: 10;");

        // Título
        Label tituloLabel = new Label("Título del Anteproyecto *");
        tituloLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");
        tituloField = new TextField();
        tituloField.setPromptText("Ingrese el título del anteproyecto");
        tituloField.setPrefHeight(35);
        if (tituloPrefill != null) {
            tituloField.setText(tituloPrefill);
        }

        // Estudiante (email - READ ONLY, obtenido del Formato A)
        Label estudianteLabel = new Label("Email del Estudiante *");
        estudianteLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");
        estudianteField = new TextField();
        estudianteField.setEditable(false); // SOLO LECTURA
        estudianteField.setStyle("-fx-background-color: #E9ECEF;");
        estudianteField.setPrefHeight(35);
        if (studentEmailPrefill != null && !studentEmailPrefill.isBlank()) {
            estudianteField.setText(studentEmailPrefill);
        } else {
            estudianteField.setPromptText("(Se obtiene del Formato A)");
        }

        // Director (email – read-only, current user)
        Label directorLabel = new Label("Email del Director *");
        directorLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");
        directorField = new TextField();
        directorField.setEditable(false);
        directorField.setStyle("-fx-background-color: #E9ECEF;");
        directorField.setPrefHeight(35);

        // PDF selection
        pdfLabel = new Label("Archivo PDF del Anteproyecto *");
        pdfLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");
        selectPdfButton = new Button("Seleccionar Archivo PDF");
        selectPdfButton.setPrefWidth(200);
        selectPdfButton.setOnAction(e -> controller.handleSelectPdf());
        selectedFileLabel = new Label("Ningún archivo seleccionado");
        selectedFileLabel.setStyle("-fx-text-fill: #666; -fx-font-style: italic;");

        // Status label
        statusLabel = new Label();
        statusLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #DC3545; -fx-font-weight: bold;");
        statusLabel.setVisible(false);

        // Buttons
        submitButton = new Button("Crear y Subir Anteproyecto");
        submitButton.setPrefWidth(250);
        submitButton.setPrefHeight(40);
        submitButton.setStyle("-fx-background-color: #28A745; -fx-text-fill: white; -fx-font-weight: bold;");
        submitButton.setOnAction(e -> controller.handleSubmit());
        backButton = new Button("Volver");
        backButton.setPrefWidth(150);
        backButton.setPrefHeight(40);
        backButton.setStyle("-fx-background-color: #6C757D; -fx-text-fill: white; -fx-font-weight: bold;");
        backButton.setOnAction(e -> controller.handleBack());
        HBox buttonBox = new HBox(15, submitButton, backButton);
        buttonBox.setAlignment(Pos.CENTER);

        formBox.getChildren().addAll(
                tituloLabel, tituloField,
                estudianteLabel, estudianteField,
                directorLabel, directorField,
                pdfLabel, selectPdfButton, selectedFileLabel,
                buttonBox
        );

        VBox scrollContent = new VBox(20, formBox, statusLabel);
        scrollContent.setAlignment(Pos.TOP_CENTER);
        ScrollPane scrollPane = new ScrollPane(scrollContent);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background: #F5F5F5; -fx-background-color: #F5F5F5;");
        scrollPane.setPrefViewportHeight(600);
        VBox.setVgrow(scrollPane, Priority.ALWAYS);

        root.getChildren().addAll(headerBox, scrollPane);
    }

    // Getters used by the controller
    public String getTitulo() { return tituloField.getText().trim(); }
    public String getStudentEmail() { return estudianteField.getText().trim(); }
    public String getDirectorEmail() { return directorField.getText().trim(); }
    public File getSelectedPdfFile() { return selectedPdfFile; }
    public void setSelectedPdfFile(File file) {
        this.selectedPdfFile = file;
        if (selectedFileLabel != null) {
            selectedFileLabel.setText(file != null ? file.getName() : "Ningún archivo seleccionado");
            selectedFileLabel.setStyle("-fx-text-fill: " + (file != null ? "#28A745" : "#666") + "; -fx-font-style: " + (file != null ? "normal" : "italic"));
        }
    }
    public void setDirectorEmail(String email) { directorField.setText(email); }
    public void showError(String message) {
        statusLabel.setText(message);
        statusLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #DC3545; -fx-font-weight: bold;");
        statusLabel.setVisible(true);
    }
    public void hideError() { statusLabel.setVisible(false); }
    public void showInfo(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Anteproyecto");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    public VBox getRoot() { return root; }
}
