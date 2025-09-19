package co.unicauca.presentation.observer;

import co.unicauca.presentation.observer.iModel;
import co.unicauca.presentation.observer.ObservableBase;

/**
 * Contrato para los observadores
 * 
 * @author javiersolanop777
 */
public interface iObserver {
    
    /**
     * Metodo para validar que la notificacion recibida
     * requiera cambio de estado en el observador
     * 
     * @param prmSubject Recibe la referencia del observado
     * @param prmModel Recibe la referencia del modelo
     */
    void validateNotification(ObservableBase prmSubject, iModel prmModel);
}
