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

public class Application {
    private final List<Object> atrRepositoryFactories; // ✅ Nombre corregido
    private final List<Object> atrControllers;
    
    public Application() {
        atrRepositoryFactories = new LinkedList<>();
        atrControllers = new LinkedList<>();
    }
    
    private void repositoryFactoriesLoader() {
        try {
            RepositoryScanner objRepositoryScanner = new RepositoryScanner();
            Set<Class<?>> listClasses = objRepositoryScanner.getRepositoryFactoriesClasses(); // ✅ Método corregido
            if (listClasses != null) {
                for(Class<?> objClass: listClasses) {
                    atrRepositoryFactories.add(objClass.getDeclaredConstructor().newInstance());
                }
            }
        } catch(Exception ex) {
            Logger.getLogger(Application.class.getName()).log(Level.SEVERE, "Error loading repository factories", ex);
        }
    }
    
    private void factoryAssignment(Object prmObject) throws IllegalArgumentException, IllegalAccessException {
        Field[] arrFields = prmObject.getClass().getDeclaredFields(); 
        for(Field objField: arrFields) {
            objField.setAccessible(true); 
            if(objField.isAnnotationPresent(FactoryAutowired.class)) {
                for(Object objFactory: atrRepositoryFactories) {
                    if(objField.getType().isAssignableFrom(objFactory.getClass())) {
                        objField.set(prmObject, objFactory);
                        break;
                    }
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
                        factoryAssignment(objController);
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
        controllersLoader();
        
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
}