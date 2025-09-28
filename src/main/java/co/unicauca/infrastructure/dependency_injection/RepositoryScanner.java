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
            System.out.println("🔍 Escaneando repositorios en paquete: " + varPackageName); 
            Set<Class<?>> repositories = ATR_SCANNER.getClassesByAnnotation(varPackageName, RepositoryFactory.class);
            System.out.println("📦 Repositorios encontrados: " + (repositories != null ? repositories.size() : 0)); 
            if (repositories != null) {
                for (Class<?> repo : repositories) {
                    System.out.println("   - " + repo.getName());
                }
            }
            return repositories;
        }
        System.out.println("⚠️ Anotación @RepositoriesScan no encontrada en ClientMain");
        return null;
    }
}