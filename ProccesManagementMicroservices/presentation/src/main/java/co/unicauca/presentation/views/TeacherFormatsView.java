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
 */
public class TeacherFormatsView {
    
    private VBox root;
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
        root = new VBox(20);
        root.setAlignment(Pos.TOP_CENTER);
        root.setPadding(new Insets(30));
        root.setStyle("-fx-background-color: #F5F5F5;");
        
        // Header
        VBox headerBox = new VBox(10);
        headerBox.setAlignment(Pos.CENTER);
        headerBox.setPadding(new Insets(20));
        headerBox.setStyle("-fx-background-color: #0F4E97; -fx-background-radius: 10;");
        
        Label titleLabel = new Label("Mis Formatos A");
        titleLabel.setStyle("-fx-font-size: 28px; -fx-font-weight: bold; -fx-text-fill: white;");
        
        Label subtitleLabel = new Label("Docente: " + user.getFullName());
        subtitleLabel.setStyle("-fx-font-size: 16px; -fx-text-fill: #E0E0E0;");
        
        headerBox.getChildren().addAll(titleLabel, subtitleLabel);
        
        // Status label
        statusLabel = new Label();
        statusLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #666;");
        statusLabel.setVisible(false);
        
        // Table
        formatsTable = createFormatsTable();
        
        // Buttons
        HBox buttonBox = new HBox(15);
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.setPadding(new Insets(10));
        
        backButton = new Button("Volver al Dashboard");
        backButton.setPrefWidth(200);
        backButton.setPrefHeight(40);
        backButton.setStyle("-fx-background-color: #6C757D; -fx-text-fill: white; -fx-font-weight: bold;");
        backButton.setOnAction(e -> controller.handleBack());
        
        Button refreshButton = new Button("Actualizar");
        refreshButton.setPrefWidth(150);
        refreshButton.setPrefHeight(40);
        refreshButton.setStyle("-fx-background-color: #17A2B8; -fx-text-fill: white; -fx-font-weight: bold;");
        refreshButton.setOnAction(e -> controller.loadFormats());
        
        buttonBox.getChildren().addAll(refreshButton, backButton);
        
        root.getChildren().addAll(headerBox, statusLabel, formatsTable, buttonBox);
    }
    
    @SuppressWarnings("unchecked")
    private TableView<FormatRow> createFormatsTable() {
        TableView<FormatRow> table = new TableView<>();
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        table.setPrefHeight(400);
        
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
                if (empty || item == null) {
                    setText(null);
                    setStyle("");
                } else {
                    setText(item);
                    if (item.contains("RECHAZADO")) {
                        setStyle("-fx-text-fill: #DC3545; -fx-font-weight: bold;");
                    } else if (item.contains("ACEPTADO")) {
                        setStyle("-fx-text-fill: #28A745; -fx-font-weight: bold;");
                    } else {
                        setStyle("-fx-text-fill: #FFA500; -fx-font-weight: bold;");
                    }
                }
            }
        });
        
        // Intento Column
        TableColumn<FormatRow, String> intentoCol = new TableColumn<>("Intento");
        intentoCol.setCellValueFactory(new PropertyValueFactory<>("intentoDisplay"));
        intentoCol.setPrefWidth(80);
        intentoCol.setCellFactory(column -> new TableCell<FormatRow, String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setStyle("");
                } else {
                    setText(item);
                    if (item.contains("3/3")) {
                        setStyle("-fx-text-fill: #DC3545; -fx-font-weight: bold;");
                    } else if (item.contains("2/3")) {
                        setStyle("-fx-text-fill: #FFA500; -fx-font-weight: bold;");
                    } else {
                        setStyle("-fx-text-fill: #666;");
                    }
                }
            }
        });
        
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
                reenviarBtn.setStyle("-fx-background-color: #28A745; -fx-text-fill: white; -fx-font-size: 10px;");
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
                    boolean maxIntentos = format.getIntentos() >= 3;
                    
                    if (isRechazado && !maxIntentos) {
                        reenviarBtn.setDisable(false);
                        reenviarBtn.setText("Reenviar");
                        reenviarBtn.setStyle("-fx-background-color: #28A745; -fx-text-fill: white; -fx-font-size: 10px;");
                        setGraphic(reenviarBtn);
                    } else if (isRechazado && maxIntentos) {
                        reenviarBtn.setDisable(true);
                        reenviarBtn.setText("Bloqueado");
                        reenviarBtn.setStyle("-fx-background-color: #999; -fx-text-fill: white; -fx-font-size: 10px;");
                        setGraphic(reenviarBtn);
                    } else {
                        setGraphic(null);
                    }
                }
            }
        });
        
        table.getColumns().addAll(idCol, tituloCol, modalidadCol, estadoCol, intentoCol, fechaCol, accionesCol);
        
        return table;
    }
    
    public VBox getRoot() {
        return root;
    }
    
    public TableView<FormatRow> getFormatsTable() {
        return formatsTable;
    }
    
    public void showStatus(String message, boolean isError) {
        statusLabel.setText(message);
        statusLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: " + (isError ? "#DC3545" : "#28A745") + "; -fx-font-weight: bold;");
        statusLabel.setVisible(true);
    }
    
    public void hideStatus() {
        statusLabel.setVisible(false);
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
