package co.unicauca.infrastructure.dependency_injection;
import org.reflections.Reflections;
import java.util.Set;

public class ServiceScanner {
    public Set<Class<?>> getServiceClasses(String... packages) {
        Reflections reflections = new Reflections((Object[]) packages);
        return reflections.getTypesAnnotatedWith(Service.class);
    } 
}
