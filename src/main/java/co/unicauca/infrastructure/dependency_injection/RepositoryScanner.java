package co.unicauca.infrastructure.dependency_injection;
import co.unicauca.presentation.ClientMain;
import java.util.Set;
public final class RepositoryScanner {
    private final iScanner ATR_SCANNER; 
    public RepositoryScanner() {
        ATR_SCANNER = new ScannerPackage();
    } 
    public Set<Class<?>> getRepositoryFactoriesClasses() { 
        if(ClientMain.class.isAnnotationPresent(RepositoriesScan.class)) {
            String varPackageName = ClientMain.class.getAnnotation(RepositoriesScan.class).packageName();
            return ATR_SCANNER.getClassesByAnnotation(varPackageName, RepositoryFactory.class);
        }
        return null;
    }
}