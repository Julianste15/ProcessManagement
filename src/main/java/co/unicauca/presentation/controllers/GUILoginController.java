package co.unicauca.presentation.controllers;

import co.unicauca.presentation.observer.ObservableBase;
import co.unicauca.presentation.observer.iObserver;
import co.unicauca.presentation.observer.iModel;

/**
 * Controlador de la GUI de inicio de sesion
 * 
 */
public class GUILoginController extends ObservableBase implements iObserver {
    

    /**
     * Almacena la bandera para la carga de las acciones de eventos
     */
    private boolean atrLoadedActions;
    
    
    @Override
    public void observersLoader()
    {
        if(!this.noneObserver()) return;
        
        //this.addObserver((iObserver) atrGUIRegisterController);
        this.addObserver(new GUIStudentController());
        this.addObserver(new GUITeacherController());
    }

    @Override
    public void validateNotification(ObservableBase prmSubject, iModel prmModel) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
    

}