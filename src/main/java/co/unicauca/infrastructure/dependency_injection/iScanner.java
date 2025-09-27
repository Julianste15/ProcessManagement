package co.unicauca.infrastructure.dependency_injection;
import java.lang.annotation.Annotation;
import java.util.Set;
public interface iScanner {
    Set<Class<?>> getClassesByAnnotation(String prmPackageName, Class<? extends Annotation> prmAnnotationType);
}
