package co.unicauca.presentation.controllers;

import co.unicauca.presentation.observer.ObservableBase;
import co.unicauca.presentation.observer.iModel;
import co.unicauca.presentation.views.GUITeacher;
import co.unicauca.presentation.observer.iObserver;


/**
 * Controlador de la GUI del modulo de profesores
 * @see GUITeacher
 */
//@Controller
public class GUITeacherController implements iObserver {
    
    /**
     * Almacena la vista del modulo de profesores
     */
    private final GUITeacher atrGUITeacher;
    
    public GUITeacherController()
    {
        atrGUITeacher = new GUITeacher();
    }

    @Override
    public void validateNotification(ObservableBase prmSubject, iModel prmModel) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}
