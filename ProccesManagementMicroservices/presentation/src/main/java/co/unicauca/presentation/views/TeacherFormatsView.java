package co.unicauca.presentation.views;

import co.unicauca.domain.entities.User;
import co.unicauca.domain.services.SessionService;
import co.unicauca.presentation.controllers.TeacherFormatsController;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import java.time.LocalDate;

/**
 * Vista para que los docentes vean sus formatos A y puedan reenviar los rechazados
 * Diseño Moderno
 */
public class TeacherFormatsView {

    private final Stage stage;
    private final TeacherFormatsController controller;
    private final User teacher;       
    private BorderPane root;
    private TableView<FormatRow> formatsTable;
    private Label statusLabel;
    private Label teacherNameLabel;
    private Label countBadge;
    private Button refreshButton;
    private Button backButton;
    
    
    public TeacherFormatsView(Stage stage, User user, SessionService sessionService) {
        this.stage = stage;
        this.teacher = user;
        this.controller = new TeacherFormatsController(this, stage, sessionService, user);
        initUI();
        controller.loadFormats();
    }
    
    /**
     * Construye toda la interfaz con mejor estilo.
     */
    private void initUI() {
        root = new BorderPane();
        root.getStyleClass().add("teacher-dashboard-root");
        
        // --- HEADER ---
        HBox headerBar = new HBox(12);
        headerBar.setAlignment(Pos.CENTER_LEFT);
        headerBar.setPadding(new Insets(16, 16, 12, 16));
        headerBar.getStyleClass().add("header-bar");
        
        Label titleLabel = new Label("Mis Formatos A");
        titleLabel.getStyleClass().add("title-label");

        teacherNameLabel = new Label("Docente: " + teacher.getFullName());
        teacherNameLabel.getStyleClass().add("subtitle-label");

        countBadge = new Label("0 formatos");
        countBadge.getStyleClass().add("badge");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        
        backButton = new Button("Volver al Dashboard");
        backButton.getStyleClass().add("primary-button");
        backButton.setOnAction(e -> controller.handleBack());

        Separator verticalSep = new Separator(javafx.geometry.Orientation.VERTICAL);
        verticalSep.setPrefHeight(24);

        headerBar.getChildren().addAll(
                titleLabel,
                verticalSep,
                teacherNameLabel,
                countBadge,
                spacer,
                backButton);

        root.setTop(headerBar);

        // ---------- CONTENIDO CENTRAL (TABLA) ----------
        VBox centerBox = new VBox(10);
        centerBox.setPadding(new Insets(8, 16, 8, 16));

        Label tableTitle = new Label("Formatos cargados");
        tableTitle.getStyleClass().add("section-title");

        formatsTable = buildFormatsTable();
        formatsTable.getStyleClass().add("formats-table");
        VBox.setVgrow(formatsTable, Priority.ALWAYS);
        centerBox.getChildren().addAll(tableTitle, formatsTable);

        root.setCenter(centerBox);

        // ---------- BARRA INFERIOR ----------
        HBox bottomBar = new HBox(12);
        bottomBar.setAlignment(Pos.CENTER_LEFT);
        bottomBar.setPadding(new Insets(10, 16, 12, 16));
        bottomBar.getStyleClass().add("footer-bar");

        statusLabel = new Label("Formatos cargados: 0");
        statusLabel.getStyleClass().add("status-label");

        Region bottomSpacer = new Region();
        HBox.setHgrow(bottomSpacer, Priority.ALWAYS);

        refreshButton = new Button("Actualizar lista");
        refreshButton.getStyleClass().add("secondary-button");
        refreshButton.setOnAction(e -> controller.loadFormats());

        bottomBar.getChildren().addAll(statusLabel, bottomSpacer, refreshButton);
        root.setBottom(bottomBar);
    }
    
    /**
     * Construye la tabla con sus columnas.
     */
    private TableView<FormatRow> buildFormatsTable() {
        TableView<FormatRow> table = new TableView<>();
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        
        TableColumn<FormatRow, Long> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        idCol.setMaxWidth(60);

        TableColumn<FormatRow, String> titleCol = new TableColumn<>("Título");
        titleCol.setCellValueFactory(new PropertyValueFactory<>("titulo"));

        TableColumn<FormatRow, String> modCol = new TableColumn<>("Modalidad");
        modCol.setCellValueFactory(new PropertyValueFactory<>("modalidad"));

        TableColumn<FormatRow, String> statusCol = new TableColumn<>("Estado");
        statusCol.setCellValueFactory(new PropertyValueFactory<>("estado"));
        statusCol.setCellFactory(column -> new TableCell<FormatRow, String>() {

            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                getStyleClass().removeAll("status-success", "status-error", "status-warning");
                if (empty || item == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    Label badge = new Label(item);
                    badge.getStyleClass().add("status-badge");
                    
                    if (item.contains("RECHAZADO")) {
                        badge.setStyle(
                                "-fx-background-color: #fee2e2; -fx-text-fill: #991b1b; -fx-padding: 4 8; -fx-background-radius: 12;");
                    } else if (item.contains("ACEPTADO")) {
                        badge.setStyle(
                                "-fx-background-color: #dcfce7; -fx-text-fill: #166534; -fx-padding: 4 8; -fx-background-radius: 12;");
                    } else {
                        badge.setStyle(
                                "-fx-background-color: #fff7ed; -fx-text-fill: #991b1b; -fx-padding: 4 8; -fx-background-radius: 12;");
                    }
                    setGraphic(badge);
                    setText(null);
                }
            }
        });
        
        // Intento Column
        TableColumn<FormatRow, String> attemptCol = new TableColumn<>("Intento");
        attemptCol.setCellValueFactory(new PropertyValueFactory<>("intentoDisplay"));
        attemptCol.setPrefWidth(90);
        
        // Fecha Column
        TableColumn<FormatRow, LocalDate> dateCol = new TableColumn<>("Fecha");
        dateCol.setCellValueFactory(new PropertyValueFactory<>("fechaCreacion"));
        
        // Acciones Column
        TableColumn<FormatRow, Void> actionsCol = new TableColumn<>("Acciones");
        actionsCol.setCellFactory(param -> new TableCell<FormatRow, Void>() {
            private final Button reenviarBtn = new Button("Reenviar");
            
            {
                reenviarBtn.getStyleClass().add("primary-button");
                reenviarBtn.setStyle("-fx-font-size: 11px; -fx-padding: 4 10;");
                reenviarBtn.setOnAction(event -> {
                    FormatRow format = getTableView().getItems().get(getIndex());
                    controller.handleResubmit(format);
                });
            }
            
            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    FormatRow format = getTableView().getItems().get(getIndex());
                    boolean isRechazado = format.getEstado().contains("RECHAZADO");
                    boolean isAprobado = format.getEstado().contains("ACEPTADO"); 
                    boolean maxIntentos = format.getIntentos() >= 3;
                    
                    HBox actions = new HBox(5);
                    actions.setAlignment(Pos.CENTER);

                    if (isRechazado && !maxIntentos) {
                        reenviarBtn.setDisable(false);
                        reenviarBtn.setText("Reenviar");
                        actions.getChildren().add(reenviarBtn);
                    } else if (isRechazado && maxIntentos) {
                        reenviarBtn.setDisable(true);
                        reenviarBtn.setText("Bloqueado");
                        actions.getChildren().add(reenviarBtn);
                    }
                    
                    if (isAprobado) {
                        Button uploadBtn = new Button("Subir Anteproyecto");
                        uploadBtn.getStyleClass().add("button-success");
                        uploadBtn.setOnAction(e -> controller.handleUploadAnteproject(format));
                        actions.getChildren().add(uploadBtn);
                    }

                    if (actions.getChildren().isEmpty()) {
                        setGraphic(null);
                    } else {
                        setGraphic(actions);
                    }
                }
            }
        });
        
        table.getColumns().addAll(idCol, titleCol, modCol, statusCol, attemptCol, dateCol, actionsCol);
        
        return table;
    }
    
    public BorderPane getRoot() {
        return root;
    }
    
    public TableView<FormatRow> getFormatsTable() {
        return formatsTable;
    }
    
    public void showStatus(String message, boolean isError) {
        statusLabel.setText(message);
         if (message.startsWith("Formatos cargados:")) {
            try {
                String countStr = message.replace("Formatos cargados:", "").trim();
                int count = Integer.parseInt(countStr);
                countBadge.setText(count + (count == 1 ? " formato" : " formatos"));
            } catch (NumberFormatException e) {
                // ignore
            }
        }
    }
    
    public void hideStatus() {
        statusLabel.setText("");
    }
    
    public void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    
    public void showInfo(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Información");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    
    /**
     * Clase interna para representar una fila de la tabla
     */
    public static class FormatRow {
        private final Long id;
        private final String titulo;
        private final String modalidad;
        private final String estado;
        private final Integer intentos;
        private final LocalDate fechaCreacion;
        private final String observaciones;
        private final String directorEmail;
        private final String codirectorEmail;
        private final String studentEmail;
        private final String objetivoGeneral;
        private final String objetivosEspecificos;
        
        public FormatRow(Long id, String titulo, String modalidad, String estado, Integer intentos,
                        LocalDate fechaCreacion, String observaciones, String directorEmail,
                        String codirectorEmail, String studentEmail, String objetivoGeneral, String objetivosEspecificos) {
            this.id = id;
            this.titulo = titulo;
            this.modalidad = modalidad;
            this.estado = formatEstado(estado);
            this.intentos = intentos;
            this.fechaCreacion = fechaCreacion;
            this.observaciones = observaciones;
            this.directorEmail = directorEmail;
            this.codirectorEmail = codirectorEmail;
            this.studentEmail = studentEmail;
            this.objetivoGeneral = objetivoGeneral;
            this.objetivosEspecificos = objetivosEspecificos;
        }
        
        private String formatEstado(String estado) {
            if (estado == null) return "Desconocido";
            return estado.replace("FORMATO_A_", "").replace("_", " ");
        }
        
        public String getIntentoDisplay() {
            return intentos + "/3";
        }
        
        // Getters
        public Long getId() { return id; }
        public String getTitulo() { return titulo; }
        public String getModalidad() { return modalidad; }
        public String getEstado() { return estado; }
        public Integer getIntentos() { return intentos; }
        public LocalDate getFechaCreacion() { return fechaCreacion; }
        public String getObservaciones() { return observaciones; }
        public String getDirectorEmail() { return directorEmail; }
        public String getCodirectorEmail() { return codirectorEmail; }
        public String getStudentEmail() { return studentEmail; }
        public String getObjetivoGeneral() { return objetivoGeneral; }
        public String getObjetivosEspecificos() { return objetivosEspecificos; }
    }
}
