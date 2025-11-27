package co.unicauca.presentation.controllers;

import co.unicauca.domain.entities.User;
import co.unicauca.domain.enums.Modalidad;
import co.unicauca.domain.services.FormatAService;
import co.unicauca.domain.services.SessionService;
import co.unicauca.presentation.views.FormatAFormView;
import co.unicauca.presentation.views.LoginView;
import javafx.scene.Scene;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class FormatAController {
    private static final Logger logger = Logger.getLogger(FormatAController.class.getName());
    private static final long MAX_PDF_BYTES = 5 * 1024 * 1024;
    private final FormatAFormView view;
    private final Stage stage;
    private final SessionService sessionService;
    private final User user;
    private final Consumer<User> onSuccess;
    private final FormatAService formatAService;

    public FormatAController(FormatAFormView view, Stage stage, SessionService sessionService, User user,
            Consumer<User> onSuccess) {
        this.view = view;
        this.stage = stage;
        this.sessionService = sessionService;
        this.user = user;
        this.onSuccess = onSuccess;
        this.formatAService = new FormatAService(sessionService != null ? sessionService.getClient() : null);
    }

    public void handleSubmit() {
        view.clearError();
        try {
            String titulo = view.getTitulo();
            if (titulo.isEmpty()) {
                view.showError("El título del proyecto es obligatorio.");
                return;
            }
            Modalidad modalidad = view.getModalidad();
            if (modalidad == null) {
                view.showError("Debe seleccionar una modalidad válida.");
                return;
            }
            String objetivoGeneral = view.getObjetivoGeneral();
            if (objetivoGeneral.isEmpty()) {
                view.showError("El objetivo general es obligatorio.");
                return;
            }
            List<String> objetivosEspecificos = parseObjetivosEspecificos(view.getObjetivosEspecificos());
            if (objetivosEspecificos.isEmpty()) {
                view.showError("Debe ingresar al menos un objetivo específico.");
                return;
            }

            // Validate PDF
            if (view.getSelectedPdfFile() == null && !view.isResubmitMode()) {
                view.showError("Debe adjuntar el archivo PDF del anteproyecto.");
                return;
            }

            // Validate Carta Aceptacion for PRACTICA_PROFESIONAL
            if (modalidad == Modalidad.PRACTICA_PROFESIONAL) {
                if (view.getSelectedCartaFile() == null && !view.isResubmitMode()) {
                    view.showError("Para Práctica Profesional, debe adjuntar la Carta de Aceptación de la Empresa.");
                    return;
                }
            }

            Map<String, Object> request = new HashMap<>();
            request.put("titulo", titulo);
            request.put("modalidad", modalidad.name());
            request.put("directorEmail", user.getEmail());

            String codirector = view.getCodirectorEmail();
            if (!codirector.isEmpty()) {
                request.put("codirectorEmail", codirector);
            }

            String studentEmail = view.getStudentEmail();
            if (studentEmail.isEmpty()) {
                view.showError("El correo del estudiante es obligatorio.");
                return;
            }
            if (!studentEmail.endsWith("@unicauca.edu.co")) {
                view.showError("El correo del estudiante debe ser del dominio @unicauca.edu.co");
                return;
            }
            request.put("studentEmail", studentEmail);
            request.put("objetivoGeneral", objetivoGeneral);
            request.put("objetivosEspecificos", objetivosEspecificos);

            handlePdfPayload(request);
            handleCartaPayload(request);

            Map<String, Object> response;

            // Verificar si está en modo reenvío o creación
            if (view.isResubmitMode()) {
                Long formatoAId = view.getFormatoAIdToResubmit();
                logger.info("Reenviando Formato A ID: " + formatoAId + " para el docente: " + user.getEmail());
                response = formatAService.resubmitFormatoA(formatoAId, request);
                view.showInfo(
                        "Formato A reenviado exitosamente. El formato ha sido actualizado y está en revisión nuevamente.");
            } else {
                logger.info("Enviando nuevo Formato A para el docente: " + user.getEmail());
                response = formatAService.submitFormatoA(request);
                view.showInfo("Formato A enviado exitosamente. El estado actual es: " + user.getFormatoAEstado());
            }

            actualizarEstadoUsuario(response);

            if (onSuccess != null) {
                onSuccess.accept(sessionService != null ? sessionService.getCurrentUser() : user);
            }
        } catch (IllegalStateException ex) {
            logger.severe("Error de configuración en FormatAService: " + ex.getMessage());
            view.showError("No se pudo enviar el Formato A. Verifique la configuración del cliente.");
        } catch (RuntimeException ex) {
            logger.severe("Error del microservicio FormatA: " + ex.getMessage());
            if (ex.getMessage() != null && ex.getMessage().contains("menos de 3 rechazos")) {
                javafx.application.Platform.runLater(() -> {
                    javafx.scene.control.Alert alert = new javafx.scene.control.Alert(
                            javafx.scene.control.Alert.AlertType.WARNING);
                    alert.setTitle("No se puede crear otro Formato A");
                    alert.setHeaderText("Límite de Formato A");
                    alert.setContentText(
                            "Solo puedes crear un Formato A a la vez.\n" +
                                    "Podrás crear uno nuevo cuando el actual haya sido " +
                                    "rechazado 3 veces por el coordinador.");
                    alert.showAndWait();
                });
            } else {
                view.showError(ex.getMessage());
            }
        } catch (IOException ex) {
            logger.severe("No se pudo leer el archivo seleccionado: " + ex.getMessage());
            view.showError("No se pudo leer el archivo seleccionado. Verifique el archivo e intente nuevamente.");
        } catch (Exception ex) {
            logger.severe("Error inesperado al enviar el Formato A: " + ex.getMessage());
            view.showError("Ocurrió un error al enviar el Formato A. Intente nuevamente.");
        }
    }

    public void handleBack() {
        if (onSuccess != null) {
            onSuccess.accept(sessionService != null ? sessionService.getCurrentUser() : user);
        }
    }

    public void handleLogout() {
        try {
            if (sessionService != null) {
                sessionService.logout();
            }
        } catch (Exception e) {
            logger.warning("Error al cerrar sesión desde el formulario de Formato A: " + e.getMessage());
        }
        LoginView loginView = new LoginView(stage);
        Scene scene = new Scene(loginView.getRoot(), 800, 600);
        scene.getStylesheets().add(getClass().getResource("/styles/application.css").toExternalForm());
        stage.setScene(scene);
        stage.setTitle("Sistema de Gestión de Trabajos de Grado - Universidad del Cauca");
    }

    private List<String> parseObjetivosEspecificos(String objetivosTexto) {
        if (objetivosTexto == null || objetivosTexto.trim().isEmpty()) {
            return new ArrayList<>();
        }
        return objetivosTexto.lines()
                .map(String::trim)
                .filter(line -> !line.isEmpty())
                .collect(Collectors.toList());
    }

    private void actualizarEstadoUsuario(Map<String, Object> response) {
        if (response == null) {
            return;
        }
        Object estadoObj = response.get("estado");
        String estado = estadoObj != null ? estadoObj.toString() : "FORMATO_A_EN_EVALUACION";
        Object idObj = response.get("id");
        Long formatoId = (idObj instanceof Number) ? ((Number) idObj).longValue() : null;
        user.setRequiresFormatoA(false);
        user.setFormatoAEstado(estado);
        user.setFormatoAId(formatoId);
        if (sessionService != null && sessionService.getCurrentUser() != null) {
            User current = sessionService.getCurrentUser();
            current.setRequiresFormatoA(false);
            current.setFormatoAEstado(estado);
            current.setFormatoAId(formatoId);
        }
    }

    public void handleSelectPdf() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Seleccionar Formato A (PDF)");
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Archivos PDF (*.pdf)", "*.pdf"));
        File selected = fileChooser.showOpenDialog(stage);
        if (selected != null) {
            if (!selected.getName().toLowerCase().endsWith(".pdf")) {
                view.showError("Debe seleccionar un archivo con extensión .pdf");
                return;
            }
            if (selected.length() > MAX_PDF_BYTES) {
                view.showError("El archivo PDF no puede superar los 5 MB.");
                return;
            }
            view.clearError();
            view.setSelectedPdfFile(selected);
        }
    }

    public void handleSelectCarta() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Seleccionar Carta de Aceptación (PDF)");
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Archivos PDF (*.pdf)", "*.pdf"));
        File selected = fileChooser.showOpenDialog(stage);
        if (selected != null) {
            if (!selected.getName().toLowerCase().endsWith(".pdf")) {
                view.showError("Debe seleccionar un archivo con extensión .pdf");
                return;
            }
            if (selected.length() > MAX_PDF_BYTES) {
                view.showError("El archivo PDF no puede superar los 5 MB.");
                return;
            }
            view.clearError();
            view.setSelectedCartaFile(selected);
        }
    }

    private void handlePdfPayload(Map<String, Object> request) throws IOException {
        File selectedPdf = view.getSelectedPdfFile();
        if (selectedPdf != null) {
            byte[] bytes = Files.readAllBytes(selectedPdf.toPath());
            String base64 = Base64.getEncoder().encodeToString(bytes);
            request.put("archivoPdfNombre", selectedPdf.getName());
            request.put("archivoPdfContenido", base64);
            return;
        }
        String archivoPdf = view.getArchivoPdf();
        if (!archivoPdf.isEmpty()) {
            request.put("archivoPDF", archivoPdf);
        }
    }

    private void handleCartaPayload(Map<String, Object> request) throws IOException {
        File selectedCarta = view.getSelectedCartaFile();
        if (selectedCarta != null) {
            byte[] bytes = Files.readAllBytes(selectedCarta.toPath());
            String base64 = Base64.getEncoder().encodeToString(bytes);
            request.put("cartaAceptacionEmpresaNombre", selectedCarta.getName());
            request.put("cartaAceptacionEmpresaContenido", base64);
        }
    }
}