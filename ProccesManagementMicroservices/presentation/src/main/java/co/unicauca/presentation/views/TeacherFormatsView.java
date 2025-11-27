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
    
    private BorderPane root;
    private TableView<FormatRow> formatsTable;
    private Label statusLabel;
    private Button backButton;
    private TeacherFormatsController controller;
    
    public TeacherFormatsView(Stage stage, User user, SessionService sessionService) {
        this.controller = new TeacherFormatsController(this, stage, sessionService, user);
        createUI(user);
        controller.loadFormats();
    }
    
    private void createUI(User user) {
        root = new BorderPane();
        root.getStyleClass().add("main-container");
        
        // --- HEADER ---
        HBox header = new HBox();
        header.getStyleClass().add("header");
        header.setAlignment(Pos.CENTER_LEFT);
        header.setSpacing(20);
        
        VBox headerText = new VBox(2);
        Label titleLabel = new Label("Mis Formatos A");
        titleLabel.getStyleClass().add("h2");
        
        Label subtitleLabel = new Label("Docente: " + user.getFullName());
        subtitleLabel.getStyleClass().add("subtitle");
        
        headerText.getChildren().addAll(titleLabel, subtitleLabel);
        
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        
        backButton = new Button("Volver al Dashboard");
        backButton.getStyleClass().add("button-secondary");
        backButton.setOnAction(e -> controller.handleBack());
        
        header.getChildren().addAll(headerText, spacer, backButton);
        root.setTop(header);
        
        // --- CONTENT ---
        VBox content = new VBox(20);
        content.setPadding(new Insets(30));
        content.setAlignment(Pos.TOP_CENTER);
        
        // Status Message
        statusLabel = new Label();
        statusLabel.getStyleClass().add("status-badge");
        statusLabel.setVisible(false);
        statusLabel.setManaged(false);
        
        // Table Container (Card)
        VBox tableCard = new VBox(15);
        tableCard.getStyleClass().add("card");
        VBox.setVgrow(tableCard, Priority.ALWAYS);
        
        HBox tableActions = new HBox(10);
        tableActions.setAlignment(Pos.CENTER_RIGHT);
        
        Button refreshButton = new Button("Actualizar Lista");
        refreshButton.getStyleClass().add("button-primary");
        refreshButton.setOnAction(e -> controller.loadFormats());
        
        tableActions.getChildren().add(refreshButton);
        
        formatsTable = createFormatsTable();
        VBox.setVgrow(formatsTable, Priority.ALWAYS);
        
        tableCard.getChildren().addAll(tableActions, formatsTable);
        
        content.getChildren().addAll(statusLabel, tableCard);
        root.setCenter(content);
    }
    
    @SuppressWarnings("unchecked")
    private TableView<FormatRow> createFormatsTable() {
        TableView<FormatRow> table = new TableView<>();
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        
        // ID Column
        TableColumn<FormatRow, Long> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        idCol.setPrefWidth(50);
        
        // Título Column
        TableColumn<FormatRow, String> tituloCol = new TableColumn<>("Título");
        tituloCol.setCellValueFactory(new PropertyValueFactory<>("titulo"));
        tituloCol.setPrefWidth(200);
        
        // Modalidad Column
        TableColumn<FormatRow, String> modalidadCol = new TableColumn<>("Modalidad");
        modalidadCol.setCellValueFactory(new PropertyValueFactory<>("modalidad"));
        modalidadCol.setPrefWidth(120);
        
        // Estado Column
        TableColumn<FormatRow, String> estadoCol = new TableColumn<>("Estado");
        estadoCol.setCellValueFactory(new PropertyValueFactory<>("estado"));
        estadoCol.setPrefWidth(150);
        estadoCol.setCellFactory(column -> new TableCell<FormatRow, String>() {
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
                        badge.getStyleClass().add("status-error");
                    } else if (item.contains("ACEPTADO")) {
                        badge.getStyleClass().add("status-success");
                    } else {
                        badge.getStyleClass().add("status-warning");
                    }
                    setGraphic(badge);
                    setText(null);
                }
            }
        });
        
        // Intento Column
        TableColumn<FormatRow, String> intentoCol = new TableColumn<>("Intento");
        intentoCol.setCellValueFactory(new PropertyValueFactory<>("intentoDisplay"));
        intentoCol.setPrefWidth(80);
        
        // Fecha Column
        TableColumn<FormatRow, LocalDate> fechaCol = new TableColumn<>("Fecha");
        fechaCol.setCellValueFactory(new PropertyValueFactory<>("fechaCreacion"));
        fechaCol.setPrefWidth(100);
        
        // Acciones Column
        TableColumn<FormatRow, Void> accionesCol = new TableColumn<>("Acciones");
        accionesCol.setPrefWidth(120);
        accionesCol.setCellFactory(param -> new TableCell<FormatRow, Void>() {
            private final Button reenviarBtn = new Button("Reenviar");
            
            {
                reenviarBtn.getStyleClass().add("button-primary");
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
                    boolean isAprobado = format.getEstado().contains("ACEPTADO"); // Assuming ACEPTADO is the approved status
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
        
        table.getColumns().addAll(idCol, tituloCol, modalidadCol, estadoCol, intentoCol, fechaCol, accionesCol);
        
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
        statusLabel.getStyleClass().removeAll("status-success", "status-error");
        statusLabel.getStyleClass().add(isError ? "status-error" : "status-success");
        statusLabel.setVisible(true);
        statusLabel.setManaged(true);
    }
    
    public void hideStatus() {
        statusLabel.setVisible(false);
        statusLabel.setManaged(false);
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
