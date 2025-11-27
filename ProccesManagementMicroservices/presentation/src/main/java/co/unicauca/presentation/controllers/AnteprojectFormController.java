package co.unicauca.presentation.controllers;

import co.unicauca.domain.services.FormatAService;
import co.unicauca.domain.services.SessionService;
import co.unicauca.presentation.views.AnteprojectFormView;
import co.unicauca.domain.entities.User;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import java.io.File;
import java.util.Map;
import java.util.function.Consumer;
import java.util.logging.Logger;

/**
 * Controller for the AnteprojectFormView.
 * Handles PDF selection, creation of the anteproject record and uploading the document.
 */
public class AnteprojectFormController {
    private static final Logger logger = Logger.getLogger(AnteprojectFormController.class.getName());
    private final AnteprojectFormView view;
    private final Stage stage;
    private final SessionService sessionService;
    private final User user;
    private final Consumer<User> onSuccess;
    private final Runnable onBack;
    private final FormatAService formatAService;
    private final Long formatoAId;

    public AnteprojectFormController(AnteprojectFormView view, Stage stage, SessionService sessionService,
                                    User user, Consumer<User> onSuccess, Runnable onBack, Long formatoAId) {
        this.view = view;
        this.stage = stage;
        this.sessionService = sessionService;
        this.user = user;
        this.onSuccess = onSuccess;
        this.onBack = onBack;
        this.formatoAId = formatoAId;
        this.formatAService = new FormatAService(sessionService.getClient());
    }

    /**
     * Opens a file chooser to select a PDF file and updates the view.
     */
    public void handleSelectPdf() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Seleccionar Archivo PDF");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF Files", "*.pdf"));
        File selected = fileChooser.showOpenDialog(stage);
        view.setSelectedPdfFile(selected);
    }

    /**
     * Submits the anteproject: creates the record then uploads the PDF.
     */
    public void handleSubmit() {
        String titulo = view.getTitulo();
        String studentEmail = view.getStudentEmail();
        String directorEmail = view.getDirectorEmail();
        File pdfFile = view.getSelectedPdfFile();
        if (titulo == null || titulo.isBlank() || studentEmail == null || studentEmail.isBlank() || pdfFile == null) {
            view.showError("Todos los campos obligatorios y el PDF deben estar completados.");
            return;
        }
        try {
            // 1. Create anteproject record
            Map<String, Object> created = formatAService.createAnteproject(formatoAId, titulo, studentEmail, directorEmail);
            Long anteprojectId = null;
            Object idObj = created.get("id");
            if (idObj instanceof Number) {
                anteprojectId = ((Number) idObj).longValue();
            } else if (idObj instanceof String) {
                anteprojectId = Long.parseLong((String) idObj);
            }
            if (anteprojectId == null) {
                view.showError("No se pudo obtener el ID del anteproyecto creado.");
                return;
            }
            // 2. Upload PDF document
            String documentUrl = "http://storage.server.com/anteprojects/" + pdfFile.getName();
            boolean uploadSuccess = formatAService.uploadAnteproject(anteprojectId, documentUrl);
            if (uploadSuccess) {
                view.showInfo("Anteproyecto creado y documento subido exitosamente.");
                // Callback to refresh the formats list
                onSuccess.accept(user);
            } else {
                view.showError("Error al subir el documento del anteproyecto.");
            }
        } catch (Exception e) {
            logger.severe("Error en la creación/subida del anteproyecto: " + e.getMessage());
            view.showError("Error: " + e.getMessage());
        }
    }

    /**
     * Returns to the previous view (teacher formats list).
     */
    public void handleBack() {
        if (onBack != null) {
            onBack.run();
        }
    }
}
