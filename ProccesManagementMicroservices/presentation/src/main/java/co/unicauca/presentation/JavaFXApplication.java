package co.unicauca.presentation;
import co.unicauca.presentation.views.LoginView;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.util.logging.Logger;
/**
 * Aplicación principal JavaFX
 */
public class JavaFXApplication extends Application {
    private static final Logger logger = Logger.getLogger(JavaFXApplication.class.getName());    
    @Override
    public void start(Stage primaryStage) {
        try {
            logger.info("Iniciando aplicación JavaFX...");            
            LoginView loginView = new LoginView(primaryStage);
            Scene scene = new Scene(loginView.getRoot(), 800, 600);
            scene.getStylesheets().add(getClass().getResource("/styles/application.css").toExternalForm());            
            primaryStage.setTitle("Sistema de Gestión de Trabajos de Grado - Universidad del Cauca");
            primaryStage.setScene(scene);
            primaryStage.setResizable(false);
            primaryStage.centerOnScreen();
            primaryStage.show();            
            logger.info("Aplicación iniciada correctamente");            
        } catch (Exception e) {
            logger.severe("Error al iniciar la aplicación: " + e.getMessage());
            e.printStackTrace();
        }
    }    
    public static void main(String[] args) {
        launch(args);
    }
}

