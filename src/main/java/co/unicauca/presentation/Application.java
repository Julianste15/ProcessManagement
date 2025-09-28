package co.unicauca.presentation;
import java.lang.reflect.Field;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import co.unicauca.presentation.controllers.GUILoginController;
import co.unicauca.infrastructure.dependency_injection.ControllerAutowired;
import co.unicauca.infrastructure.dependency_injection.ControllerScanner;
import co.unicauca.infrastructure.dependency_injection.FactoryAutowired;
import co.unicauca.infrastructure.dependency_injection.RepositoryScanner;
import co.unicauca.infrastructure.dependency_injection.ServiceScanner;

public class Application {
    private final List<Object> atrRepositoryFactories;
    private final List<Object> atrControllers;
    private final List<Object> atrServices; // Nueva lista para servicios
    
    public Application() {
        atrRepositoryFactories = new LinkedList<>();
        atrControllers = new LinkedList<>();
        atrServices = new LinkedList<>(); // Inicializar lista de servicios
    }
    
    private void repositoryFactoriesLoader() {
        try {
            RepositoryScanner objRepositoryScanner = new RepositoryScanner();
            Set<Class<?>> listClasses = objRepositoryScanner.getRepositoryFactoriesClasses();

            if (listClasses != null && !listClasses.isEmpty()) {
                for(Class<?> objClass: listClasses) {
                    System.out.println("🔄 Instanciando repository: " + objClass.getSimpleName());
                    atrRepositoryFactories.add(objClass.getDeclaredConstructor().newInstance());
                }
                System.out.println("✅ Repositories cargados: " + atrRepositoryFactories.size());
            } else {
                System.out.println("❌ No se encontraron repositories factories");
            }
        } catch(Exception ex) {
            Logger.getLogger(Application.class.getName()).log(Level.SEVERE, "Error loading repository factories", ex);
        }
    }
    
    // Nuevo método para cargar servicios
    private void servicesLoader() {
        try {
            ServiceScanner serviceScanner = new ServiceScanner();
            // Escanear en los paquetes donde estarán los servicios
            Set<Class<?>> serviceClasses = serviceScanner.getServiceClasses(
                "co.unicauca.domain.services", 
                "co.unicauca.infrastructure.validation",
                "co.unicauca.infrastructure.security"
            );
            
            if (serviceClasses != null) {
                for(Class<?> serviceClass : serviceClasses) {
                    atrServices.add(serviceClass.getDeclaredConstructor().newInstance());
                }
            }
        } catch(Exception ex) {
            Logger.getLogger(Application.class.getName()).log(Level.SEVERE, "Error loading services", ex);
        }
    }
    
    // Modificar factoryAssignment para buscar en servicios también
    private void factoryAssignment(Object prmObject) throws IllegalArgumentException, IllegalAccessException {
        Field[] arrFields = prmObject.getClass().getDeclaredFields(); 
        for(Field objField: arrFields) {
            objField.setAccessible(true); 
            if(objField.isAnnotationPresent(FactoryAutowired.class)) {
                boolean dependencyFound = false;
                
                // 1. Buscar en repositories primero
                for(Object objFactory: atrRepositoryFactories) {
                    if(objField.getType().isAssignableFrom(objFactory.getClass())) {
                        objField.set(prmObject, objFactory);
                        dependencyFound = true;
                        break;
                    }
                }
                
                // 2. Si no se encontró en repositories, buscar en servicios
                if (!dependencyFound) {
                    for(Object objService: atrServices) {
                        if(objField.getType().isAssignableFrom(objService.getClass())) {
                            objField.set(prmObject, objService);
                            dependencyFound = true;
                            break;
                        }
                    }
                }
                
                // 3. Log si no se encontró la dependencia
                if (!dependencyFound) {
                    Logger.getLogger(Application.class.getName()).warning(
                        "Dependency not found for field: " + objField.getName() + 
                        " in class: " + prmObject.getClass().getSimpleName()
                    );
                }
            }
        }
    }
    
    private void controllersAssignment() {
        try {
            for(Object objController1: atrControllers) {
                Field[] arrFields = objController1.getClass().getDeclaredFields();
                for(Field objField: arrFields) {
                    objField.setAccessible(true);
                    if(objField.isAnnotationPresent(ControllerAutowired.class)) {
                        for(Object objController2: atrControllers) {
                            if(objField.getType().isAssignableFrom(objController2.getClass())) {  
                                objField.set(objController1, objController2);
                                break;
                            }
                        }
                    }
                }
            }
        } catch(Exception ex) {
            Logger.getLogger(Application.class.getName()).log(Level.SEVERE, "Error in controller assignment", ex);
        }
    }
    
    private void controllersLoader() {
        try {
            ControllerScanner objControllerScanner = new ControllerScanner();
            List<Set<Class<?>>> listPackages = objControllerScanner.getControllersClasses();
            
            if (listPackages != null) {
                for(Set<Class<?>> listClasses: listPackages) {
                    for(Class<?> objClass: listClasses) {
                        Object objController = objClass.getDeclaredConstructor().newInstance();
                        factoryAssignment(objController); // Ahora buscará en servicios también
                        atrControllers.add(objController);
                    } 
                }  
                controllersAssignment();
            }
        } catch(Exception ex) {
            Logger.getLogger(Application.class.getName()).log(Level.SEVERE, "Error loading controllers", ex);
        }
    }
    
    public void run() {
        repositoryFactoriesLoader();
        servicesLoader(); // Cargar servicios antes de controllers
        controllersLoader();
        printDependenciesStatus(); // Temporal para diagnóstico
        GUILoginController objGUILoginController = findLoginController();
        if(objGUILoginController != null) {
            objGUILoginController.run();
        } else {
            Logger.getLogger(Application.class.getName()).severe("GUILoginController not found!");
        }
    }
    
    private GUILoginController findLoginController() {
        for(Object objController: atrControllers) {
            if(objController instanceof GUILoginController) {
                return (GUILoginController) objController;
            }
        }
        return null;
    }
    private void printDependenciesStatus() {
    System.out.println("=== DEPENDENCY INJECTION STATUS ===");
    System.out.println("Repositories loaded: " + atrRepositoryFactories.size());
    System.out.println("Services loaded: " + atrServices.size());
    System.out.println("Controllers loaded: " + atrControllers.size());
    
    // Verificar UserService específicamente
    for (Object controller : atrControllers) {
        if (controller instanceof co.unicauca.domain.services.UserService) {
            co.unicauca.domain.services.UserService userService = (co.unicauca.domain.services.UserService) controller;
            try {
                java.lang.reflect.Field validatorField = userService.getClass().getDeclaredField("userValidator");
                validatorField.setAccessible(true);
                Object validator = validatorField.get(userService);
                System.out.println("UserService.userValidator: " + (validator != null ? "✓ INYECTADO" : "✗ NULL"));
            } catch (Exception e) {
                System.out.println("Error checking UserService dependencies: " + e.getMessage());
            }
        }
    }
}
}