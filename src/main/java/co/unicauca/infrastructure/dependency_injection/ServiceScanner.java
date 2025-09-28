package co.unicauca.infrastructure.dependency_injection;

import org.reflections.Reflections;
import java.util.Set;
import java.util.logging.Logger;

public class ServiceScanner {
    private static final Logger logger = Logger.getLogger(ServiceScanner.class.getName());
    
    public Set<Class<?>> getServiceClasses(String... packages) {
        Reflections reflections = new Reflections((Object[]) packages);
        return reflections.getTypesAnnotatedWith(Service.class);
    }
    
    public void scanAndRegisterServices(String... packages) {
        Set<Class<?>> serviceClasses = getServiceClasses(packages);
        logger.info("Encontrados " + serviceClasses.size() + " servicios");
        
        for (Class<?> serviceClass : serviceClasses) {
            try {
                Object serviceInstance = serviceClass.getDeclaredConstructor().newInstance();
                ServiceFactory.registerService(serviceClass, serviceInstance);
                
                // Inyectar dependencias en este servicio
                ServiceFactory.injectDependencies(serviceInstance);
                
            } catch (Exception e) {
                logger.severe("❌ Error creando servicio " + serviceClass.getSimpleName() + ": " + e.getMessage());
            }
        }
    }
}