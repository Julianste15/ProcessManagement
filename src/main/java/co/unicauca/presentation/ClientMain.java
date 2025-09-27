package co.unicauca.presentation;
import co.unicauca.infrastructure.dependency_injection.ControllersScan;
import co.unicauca.infrastructure.dependency_injection.RepositoriesScan;
@RepositoriesScan(packageName = "co.unicauca.infrastructure.persistence.repositories")
@ControllersScan(packagesNames = {
    "co.unicauca.domain.services",           // Services (UserService, SessionService)
    "co.unicauca.presentation.controllers"   // MVC Controllers
})
public class ClientMain {   
    public static void main(String[] args) { 
        try {
            // Configurar el look and feel del sistema
            javax.swing.UIManager.setLookAndFeel(javax.swing.UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ex) {
            java.util.logging.Logger.getLogger(ClientMain.class.getName())
                .warning("No se pudo cargar el look and feel del sistema: " + ex.getMessage());
        }
        
        Application objApplication = new Application();
        objApplication.run();
    }
}