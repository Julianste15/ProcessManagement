package co.unicauca.infrastructure.dependency_injection;

import co.unicauca.presentation.ClientMain;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public final class ControllerScanner {
    private final iScanner ATR_SCANNER;
    
    public ControllerScanner() {
        ATR_SCANNER = new ScannerPackage();
    }
    
    public List<Set<Class<?>>> getControllersClasses() {
        if(ClientMain.class.isAnnotationPresent(ControllersScan.class)) {
            String[] arrPackagesNames = ClientMain.class.getAnnotation(ControllersScan.class).packagesNames();
            List<Set<Class<?>>> listPackages = new LinkedList<>();
            for(String objPackageName: arrPackagesNames) {
                listPackages.add(ATR_SCANNER.getClassesByAnnotation(objPackageName, Controller.class));
            }
            return listPackages;
        }
        return null;
    }
}