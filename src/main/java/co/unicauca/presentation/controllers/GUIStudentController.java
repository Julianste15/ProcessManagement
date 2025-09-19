package co.unicauca.presentation.controllers;

import co.unicauca.presentation.observer.ObservableBase;
import co.unicauca.presentation.observer.iModel;
import co.unicauca.presentation.views.GUIStudent;
import co.unicauca.presentation.observer.iObserver;

/**
 * Controlador de la GUI del modulo de estudiantes
 * @see GUIStudent
 */
public class GUIStudentController implements iObserver {
    /**
     * Almacena la vista del modulo de estudiantes
     */
    private final GUIStudent atrGUIStudent;

    public GUIStudentController()
    {
        atrGUIStudent = new GUIStudent();
    }

    @Override
    public void validateNotification(ObservableBase prmSubject, iModel prmModel) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }


}
