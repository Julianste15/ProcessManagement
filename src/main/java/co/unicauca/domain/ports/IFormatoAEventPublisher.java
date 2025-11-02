/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package co.unicauca.domain.ports;

import co.unicauca.domain.entities.FormatoA;

/**
 *
 * @author AN515-54-55MX
 */
public interface IFormatoAEventPublisher {
    
    /**
     * Publica un evento cuando se envía el Formato A.
     *
     * @param proyecto El proyecto asociado al evento.
     */
    void publishFormatoAEnviado(FormatoA proyecto);
}
