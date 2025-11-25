package co.unicauca.presentation.views;

import co.unicauca.domain.entities.User;
import co.unicauca.domain.enums.Role;
import co.unicauca.presentation.controllers.DashboardController;
import co.unicauca.domain.services.SessionService;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

/**
 * Vista de Dashboard con JavaFX - Diseño Moderno
 */
public class DashboardView {
    
    private BorderPane root;
    private Label welcomeLabel;
    private Label userInfoLabel;
    private Button logoutButton;
    private DashboardController controller;
    private User user;
    
    public DashboardView(Stage stage, User user, SessionService sessionService) {
        this.controller = new DashboardController(this, stage, sessionService);
        this.user = user;
        createUI();
    }
    
    private void createUI() {
        root = new BorderPane();
        root.getStyleClass().add("main-container");
        
        // --- SIDEBAR ---
        VBox sidebar = new VBox(0);
        sidebar.getStyleClass().add("sidebar");
        sidebar.setPrefWidth(250);
        
        // Logo Area
        VBox logoBox = new VBox();
        logoBox.getStyleClass().add("logo-container");
        Label logoText = new Label("Process\nManagement");
        logoText.getStyleClass().add("logo-text");
        logoBox.getChildren().add(logoText);
        
        // Navigation Menu
        VBox navMenu = new VBox(0);
        
        Button homeBtn = createNavButton("Inicio", true);
        navMenu.getChildren().add(homeBtn);
        
        if (user.getRole() == Role.TEACHER) {
            Button fillFormatABtn = createNavButton("Diligenciar Formato A", false);
            fillFormatABtn.setOnAction(e -> controller.handleFillFormatA());
            
            Button viewFormatsBtn = createNavButton("Mis Formatos A", false);
            viewFormatsBtn.setOnAction(e -> controller.handleViewFormats());
            
            navMenu.getChildren().addAll(fillFormatABtn, viewFormatsBtn);
        }
        
        // Spacer to push logout to bottom
        Region spacer = new Region();
        VBox.setVgrow(spacer, Priority.ALWAYS);
        
        // Logout Button in Sidebar
        logoutButton = createNavButton("Cerrar Sesión", false);
        logoutButton.getStyleClass().add("nav-button-logout"); // Optional: add specific style
        logoutButton.setOnAction(e -> controller.handleLogout());
        
        sidebar.getChildren().addAll(logoBox, navMenu, spacer, logoutButton);
        root.setLeft(sidebar);
        
        // --- MAIN CONTENT AREA ---
        VBox mainContent = new VBox(20);
        mainContent.setPadding(new Insets(30));
        
        // Header
        HBox header = new HBox();
        header.getStyleClass().add("header");
        header.setAlignment(Pos.CENTER_LEFT);
        
        VBox headerText = new VBox(2);
        welcomeLabel = new Label("Hola, " + user.getNames());
        welcomeLabel.getStyleClass().add("h2");
        
        String roleText = user.getRole() != null ? user.getRole().getDisplayName() : "Usuario";
        userInfoLabel = new Label(roleText + " | " + user.getEmail());
        userInfoLabel.getStyleClass().add("subtitle");
        
        headerText.getChildren().addAll(welcomeLabel, userInfoLabel);
        header.getChildren().add(headerText);
        
        root.setTop(header);
        
        // Dashboard Cards
        FlowPane cardsContainer = new FlowPane();
        cardsContainer.setHgap(20);
        cardsContainer.setVgap(20);
        
        // Status Card
        VBox statusCard = createInfoCard("Estado del Sistema", "Activo y sincronizado");
        cardsContainer.getChildren().add(statusCard);
        
        if (user.getRole() == Role.TEACHER) {
             String estado = user.getFormatoAEstado();
             if (estado == null || estado.isEmpty()) {
                 estado = user.isRequiresFormatoA() ? "Pendiente" : "N/A";
             }
             VBox formatCard = createInfoCard("Estado Formato A", estado);
             cardsContainer.getChildren().add(formatCard);
        }
        
        mainContent.getChildren().addAll(new Label("Resumen General"), cardsContainer);
        root.setCenter(mainContent);
    }
    
    private Button createNavButton(String text, boolean isActive) {
        Button btn = new Button(text);
        btn.setMaxWidth(Double.MAX_VALUE);
        btn.getStyleClass().add("nav-button");
        if (isActive) {
            btn.getStyleClass().add("nav-button-active");
        }
        return btn;
    }
    
    private VBox createInfoCard(String title, String value) {
        VBox card = new VBox(10);
        card.getStyleClass().add("card");
        card.setPrefWidth(250);
        
        Label titleLbl = new Label(title);
        titleLbl.getStyleClass().add("subtitle");
        
        Label valueLbl = new Label(value);
        valueLbl.getStyleClass().add("h3");
        
        card.getChildren().addAll(titleLbl, valueLbl);
        return card;
    }
    
    public BorderPane getRoot() {
        return root;
    }
}