package co.unicauca.infrastructure.dependency_injection;
import java.lang.annotation.Annotation;
import java.util.Set;
import org.reflections.Reflections;
public class ScannerPackage implements iScanner {
    public ScannerPackage()
    {}
    @Override
    public Set<Class<?>> getClassesByAnnotation(String prmPackageName, Class<? extends Annotation> prmAnnotationType)
    {
        Reflections objScanner = new Reflections(prmPackageName);
        return objScanner.getTypesAnnotatedWith(prmAnnotationType);           
    }
}
