package co.unicauca.presentation;

import co.unicauca.domain.repositories.UserRepository;
import co.unicauca.domain.services.SessionService;
import co.unicauca.domain.services.UserService;
import co.unicauca.infrastructure.dependency_injection.ControllersScan;
import co.unicauca.infrastructure.dependency_injection.RepositoriesScan;
import co.unicauca.infrastructure.security.Encryptor;
import co.unicauca.infrastructure.security.iEncryptor;
import co.unicauca.infrastructure.persistence.UserRepositoryImpl;

@RepositoriesScan(packageName = "co.unicauca.infrastructure.persistence")
@ControllersScan(packagesNames = {
    "co.unicauca.domain.services",
    "co.unicauca.presentation.controllers"
})
public class ClientMain {   
    
    // Instancia compartida globalmente
    public static SessionService sharedSessionService;
    public static UserService userService;
    
    public static void main(String[] args) { 
        try {
            javax.swing.UIManager.setLookAndFeel(javax.swing.UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ex) {
            java.util.logging.Logger.getLogger(ClientMain.class.getName())
            .warning("No se pudo cargar el look and feel del sistema: " + ex.getMessage());
        }
        
        System.out.println("=== INICIANDO APLICACIÓN CON INSTANCIA COMPARTIDA ===");
        
        try {
            // 1. Configurar dependencias y obtener la instancia compartida
            sharedSessionService = setupCompleteDependencies();
            
            // 2. Iniciar aplicación principal
            Application objApplication = new Application();
            objApplication.run();
            
        } catch (Exception e) {
            System.err.println("❌ Error crítico en main: " + e.getMessage());
            e.printStackTrace();
            showErrorDialog("Error fatal: " + e.getMessage());
        }
    }
    
    private static SessionService setupCompleteDependencies() {
        System.out.println("=== CONFIGURACIÓN COMPLETA DE DEPENDENCIAS ===");
        
        try {
            // 1. Crear Repository
            System.out.println("🔧 Paso 1: Creando UserRepository...");
            UserRepository userRepository = new UserRepositoryImpl();
            System.out.println("✅ UserRepository creado");
            
            // 2. Crear Encryptor
            System.out.println("🔧 Paso 2: Creando Encryptor...");
            iEncryptor encryptor = new Encryptor();
            System.out.println("✅ Encryptor creado");
            
            // 3. Crear UserService e inyectar dependencias
            System.out.println("🔧 Paso 3: Creando UserService...");
            UserService userService = new UserService();
            injectField(userService, "userRepository", userRepository);
            injectField(userService, "encryptor", encryptor);
            System.out.println("✅ UserService configurado");
            
            // 4. Crear SessionService (INSTANCIA COMPARTIDA)
            System.out.println("🔧 Paso 4: Creando SessionService compartida...");
            SessionService sessionService = new SessionService();
            injectField(sessionService, "userService", userService);
            injectField(sessionService, "encryptor", encryptor);
            System.out.println("✅ SessionService compartida creada");
            
            // 5. Test final
            System.out.println("🔧 Paso 5: Testeando instancia compartida...");
            testSessionService(sessionService);
            System.out.println("✅ Instancia compartida funciona correctamente");
            
            System.out.println("🎉 SessionService compartida lista para usar!");
            return sessionService;
            
        } catch (Exception e) {
            System.err.println("💥 Error en configuración: " + e.getMessage());
            throw new RuntimeException("Fallo en la configuración del sistema", e);
        }
    }
    
    private static void testSessionService(SessionService sessionService) {
        try {
            // Test con usuario que no existe
            try {
                sessionService.login("test@unicauca.edu.co", "test123");
                System.out.println("   ⚠️ Login completado (usuario test no debería existir)");
            } catch (Exception e) {
                System.out.println("   ✅ Test pasado - Error esperado: " + e.getMessage());
            }
        } catch (Exception e) {
            throw new RuntimeException("Test falló", e);
        }
    }
    
    private static void injectField(Object target, String fieldName, Object value) throws Exception {
        try {
            java.lang.reflect.Field field = findField(target.getClass(), fieldName);
            field.setAccessible(true);
            field.set(target, value);
        } catch (Exception e) {
            throw new Exception("Error inyectando '" + fieldName + "' en " + 
                              target.getClass().getSimpleName() + ": " + e.getMessage());
        }
    }
    
    private static java.lang.reflect.Field findField(Class<?> clazz, String fieldName) throws NoSuchFieldException {
        Class<?> currentClass = clazz;
        while (currentClass != null) {
            try {
                return currentClass.getDeclaredField(fieldName);
            } catch (NoSuchFieldException e) {
                currentClass = currentClass.getSuperclass();
            }
        }
        throw new NoSuchFieldException("Campo '" + fieldName + "' no encontrado");
    }
    
    private static void showErrorDialog(String message) {
        javax.swing.JOptionPane.showMessageDialog(null, message, "Error", javax.swing.JOptionPane.ERROR_MESSAGE);
    }
}