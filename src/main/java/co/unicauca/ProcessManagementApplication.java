package co.unicauca;

import co.unicauca.presentation.controllers.GUILoginController;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

import java.awt.EventQueue;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {
    "co.unicauca.config",
    "co.unicauca.domain.services",
    "co.unicauca.presentation.controllers",
    "co.unicauca.domain.repositories"
})
public class ProcessManagementApplication {

    public static void main(String[] args) {
        
        // 1. Preparamos la aplicación Spring
        SpringApplicationBuilder builder = new SpringApplicationBuilder(ProcessManagementApplication.class);
        
        // 2. Le decimos a Spring que esta NO es una aplicación web
        // y que SÍ debe permitir crear una interfaz gráfica (GUI)
        builder.headless(false); 
        
        // 3. Lanzamos la aplicación y guardamos el "contexto"
        // El contexto es donde Spring guarda todos los @Service y @Component que creó
        ConfigurableApplicationContext context = builder.run(args);
        
        // 4. Le pedimos a Spring que nos dé el controlador de Login
        // que ya creó e inyectó
        GUILoginController loginController = context.getBean(GUILoginController.class);
        
        // 5. Ejecutamos el método .run() para iniciar la GUI
        // Lo hacemos dentro de EventQueue para seguridad de Swing
        EventQueue.invokeLater(() -> {
            loginController.run();
        });
    }
}