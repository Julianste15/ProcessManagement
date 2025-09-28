package co.unicauca.infrastructure.dependency_injection;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

public class ServiceFactory {
    private static final Logger logger = Logger.getLogger(ServiceFactory.class.getName());
    private static final Map<Class<?>, Object> serviceInstances = new HashMap<>();
    
    public static void registerService(Class<?> serviceClass, Object instance) {
        serviceInstances.put(serviceClass, instance);
        logger.info("✅ Servicio registrado en factory: " + serviceClass.getSimpleName());
    }
    
    public static void injectDependencies(Object target) {
        Class<?> targetClass = target.getClass();
        Field[] fields = targetClass.getDeclaredFields();
        
        for (Field field : fields) {
            if (field.isAnnotationPresent(FactoryAutowired.class)) {
                try {
                    field.setAccessible(true);
                    Class<?> fieldType = field.getType();
                    
                    Object dependency = serviceInstances.get(fieldType);
                    if (dependency != null) {
                        field.set(target, dependency);
                        logger.info("✅ Inyectado " + fieldType.getSimpleName() + " en " + targetClass.getSimpleName());
                    } else {
                        logger.warning("⚠️ No se encontró dependencia para: " + fieldType.getSimpleName());
                    }
                } catch (IllegalAccessException e) {
                    logger.severe("❌ Error inyectando dependencia: " + e.getMessage());
                }
            }
        }
    }
    
    @SuppressWarnings("unchecked")
    public static <T> T getService(Class<T> serviceClass) {
        return (T) serviceInstances.get(serviceClass);
    }
}