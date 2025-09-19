package co.unicauca.presentation.controllers;

import co.unicauca.presentation.observer.ObservableBase;
import co.unicauca.presentation.observer.iObserver;
import co.unicauca.presentation.observer.iModel;

/**
 * Controlador de la GUI de registro de usuarios
 * 
 */
public class GUIRegisterController extends ObservableBase implements iObserver {

    /**
     * Almacena la bandera para la carga de las acciones de eventos
     */
    private boolean atrLoadedActions;
    
    // Constructors:
    
    public GUIRegisterController()
    {
        atrLoadedActions = false;
    }
    
    @Override
    public void observersLoader()
    {
        if(!this.noneObserver());
       
    }

    @Override
    public void validateNotification(ObservableBase prmSubject, iModel prmModel) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}
