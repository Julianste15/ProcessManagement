package co.unicauca.presentation.controllers;
import co.unicauca.domain.entities.User;
import co.unicauca.domain.services.FormatAService;
import co.unicauca.domain.services.SessionService;
import co.unicauca.presentation.views.DashboardView;
import co.unicauca.presentation.views.FormatAFormView;
import co.unicauca.presentation.views.TeacherFormatsView;
import co.unicauca.presentation.views.TeacherFormatsView.FormatRow;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.logging.Logger;

/**
 * Controlador para TeacherFormatsView
 */
public class TeacherFormatsController {
    
    private static final Logger logger = Logger.getLogger(TeacherFormatsController.class.getName());
    
    private final TeacherFormatsView view;
    private final Stage stage;
    private final SessionService sessionService;
    private final User user;
    private final FormatAService formatAService;
    
    public TeacherFormatsController(TeacherFormatsView view, Stage stage, SessionService sessionService, User user) {
        this.view = view;
        this.stage = stage;
        this.sessionService = sessionService;
        this.user = user;
        this.formatAService = new FormatAService(sessionService.getClient());
    }
    
    /**
     * Carga los formatos del docente
     */
    public void loadFormats() {
        view.hideStatus();
        
        new Thread(() -> {
            try {
                logger.info("Cargando formatos para: " + user.getEmail());
                List<Map<String, Object>> formatos = formatAService.getFormatosPorUsuario(user.getEmail());
                
                Platform.runLater(() -> {
                    if (formatos.isEmpty()) {
                        view.showStatus("No tienes formatos enviados", false);
                        view.getFormatsTable().setItems(FXCollections.observableArrayList());
                    } else {
                        ObservableList<FormatRow> rows = FXCollections.observableArrayList();
                        
                        for (Map<String, Object> formato : formatos) {
                            FormatRow row = mapToFormatRow(formato);
                            rows.add(row);
                        }
                        
                        view.getFormatsTable().setItems(rows);
                        view.showStatus("Formatos cargados: " + formatos.size(), false);
                        logger.info("Formatos cargados exitosamente: " + formatos.size());
                    }
                });
                
            } catch (Exception e) {
                logger.severe("Error cargando formatos: " + e.getMessage());
                e.printStackTrace();
                Platform.runLater(() -> {
                    view.showError("Error cargando formatos: " + e.getMessage());
                });
            }
        }).start();
    }
    
    /**
     * Convierte un Map a FormatRow
     */
    private FormatRow mapToFormatRow(Map<String, Object> formato) {
        Long id = formato.get("id") != null ? ((Number) formato.get("id")).longValue() : null;
        String titulo = (String) formato.get("titulo");
        String modalidad = formato.get("modalidad") != null ? formato.get("modalidad").toString() : "N/A";
        String estado = (String) formato.get("estado");
        Integer intentos = formato.get("intentos") != null ? ((Number) formato.get("intentos")).intValue() : 1;
        
        LocalDate fechaCreacion = null;
        if (formato.get("fechaCreacion") != null) {
            Object fechaObj = formato.get("fechaCreacion");
            if (fechaObj instanceof List) {
                @SuppressWarnings("unchecked")
                List<Integer> fechaList = (List<Integer>) fechaObj;
                if (fechaList.size() >= 3) {
                    fechaCreacion = LocalDate.of(fechaList.get(0), fechaList.get(1), fechaList.get(2));
                }
            } else if (fechaObj instanceof String) {
                fechaCreacion = LocalDate.parse((String) fechaObj);
            }
        }
        
        String observaciones = (String) formato.get("observaciones");
        String directorEmail = (String) formato.get("directorEmail");
        String codirectorEmail = (String) formato.get("codirectorEmail");
        String studentEmail = (String) formato.get("studentEmail");
        String objetivoGeneral = (String) formato.get("objetivoGeneral");
        String objetivosEspecificos = formato.get("objetivosEspecificos") != null ? 
                formato.get("objetivosEspecificos").toString() : "";
        
        return new FormatRow(id, titulo, modalidad, estado, intentos, fechaCreacion,
                observaciones, directorEmail, codirectorEmail, studentEmail, objetivoGeneral, objetivosEspecificos);
    }
    
    /**
     * Maneja el reenvío de un formato rechazado
     */
    public void handleResubmit(FormatRow format) {
        if (format.getIntentos() >= 3) {
            view.showError("Este formato ha alcanzado el máximo de intentos (3). No se puede reenviar.");
            return;
        }
        
        // Mostrar observaciones del coordinador
        Alert confirmDialog = new Alert(Alert.AlertType.CONFIRMATION);
        confirmDialog.setTitle("Reenviar Formato A");
        confirmDialog.setHeaderText("¿Deseas reenviar este formato?");
        
        VBox content = new VBox(10);
        content.getChildren().add(new javafx.scene.control.Label("Título: " + format.getTitulo()));
        content.getChildren().add(new javafx.scene.control.Label("Intento actual: " + format.getIntentoDisplay()));
        
        if (format.getObservaciones() != null && !format.getObservaciones().isEmpty()) {
            content.getChildren().add(new javafx.scene.control.Label("Observaciones del coordinador:"));
            TextArea obsArea = new TextArea(format.getObservaciones());
            obsArea.setEditable(false);
            obsArea.setWrapText(true);
            obsArea.setPrefRowCount(4);
            content.getChildren().add(obsArea);
        }
        
        confirmDialog.getDialogPane().setContent(content);
        
        Optional<ButtonType> result = confirmDialog.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            // Abrir formulario de reenvío (reutilizando FormatAFormView)
            openResubmitForm(format);
        }
    }
    
        /**
     * Abre el formulario para reenviar el formato
     */
    private void openResubmitForm(FormatRow format) {
        // Mostrar diálogo con las observaciones del coordinador
        if (format.getObservaciones() != null && !format.getObservaciones().isEmpty()) {
            Alert observacionesDialog = new Alert(Alert.AlertType.INFORMATION);
            observacionesDialog.setTitle("Observaciones del Coordinador");
            observacionesDialog.setHeaderText("Revisa las observaciones antes de reenviar");
            
            VBox content = new VBox(10);
            content.setPadding(new Insets(10));
            
            Label warningLabel = new Label("⚠️ OBSERVACIONES DEL COORDINADOR:");
            warningLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: #DC3545; -fx-font-size: 14px;");
            
            TextArea obsArea = new TextArea(format.getObservaciones());
            obsArea.setEditable(false);
            obsArea.setWrapText(true);
            obsArea.setPrefRowCount(5);
            obsArea.setStyle("-fx-control-inner-background: #FFE6E6;");
            
            Label infoLabel = new Label(
                "A continuación se abrirá el formulario con los datos actuales.\n" +
                "Puedes editarlos según las observaciones y reenviar."
            );
            infoLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: #666;");
            infoLabel.setWrapText(true);
            
            content.getChildren().addAll(warningLabel, obsArea, infoLabel);
            
            observacionesDialog.getDialogPane().setContent(content);
            observacionesDialog.showAndWait();
        }
        
        // Abrir formulario pre-llenado con los datos actuales
        FormatAFormView formatAFormView = new FormatAFormView(
            stage, 
            user, 
            sessionService, 
            updatedUser -> {
                // Volver a la vista de formatos después de enviar
                TeacherFormatsView formatsView = new TeacherFormatsView(stage, user, sessionService);
                Scene scene = new Scene(formatsView.getRoot(), 1000, 700);
                stage.setScene(scene);
                stage.setTitle("Mis Formatos A - Sistema de Gestión de Trabajos de Grado");
            },
            format.getId(), // ← AGREGAR ESTE PARÁMETRO (ID del formato)
            format.getTitulo(),
            format.getModalidad(),
            format.getDirectorEmail(),
            format.getCodirectorEmail(),
            format.getStudentEmail(),
            format.getObjetivoGeneral(),
            format.getObjetivosEspecificos(),
            format.getIntentos()
        );
        
        Scene scene = new Scene(formatAFormView.getRoot(), 900, 750);
        stage.setScene(scene);
        stage.setTitle("Reenviar Formato A - Intento " + (format.getIntentos() + 1) + "/3");
    }
    
    /**
     * Agrega un campo de texto copiable al contenedor
     */
    private void addCopyableField(VBox container, String label, String value) {
        Label fieldLabel = new Label(label + ":");
        fieldLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 12px;");
        
        TextField field = new TextField(value != null ? value : "");
        field.setEditable(false);
        field.setStyle("-fx-background-color: #F5F5F5;");
        
        container.getChildren().addAll(fieldLabel, field);
    }
    
    /**
     * Agrega un área de texto copiable al contenedor
     */
    private void addCopyableTextArea(VBox container, String label, String value) {
        Label fieldLabel = new Label(label + ":");
        fieldLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 12px;");
        
        TextArea area = new TextArea(value != null ? value : "");
        area.setEditable(false);
        area.setWrapText(true);
        area.setPrefRowCount(3);
        area.setStyle("-fx-control-inner-background: #F5F5F5;");
        
        container.getChildren().addAll(fieldLabel, area);
    }
    
    /**
     * Vuelve al dashboard
     */
    public void handleBack() {
        DashboardView dashboardView = new DashboardView(stage, user, sessionService);
        Scene scene = new Scene(dashboardView.getRoot(), 900, 700);
        stage.setScene(scene);
        stage.setTitle("Dashboard - Sistema de Gestión de Trabajos de Grado");
    }
}
