package co.unicauca.domain.ports;
import co.unicauca.domain.entities.FormatoA;
public interface IFormatoAEventPublisher {
    
    /**
     * Publica un evento cuando se envía el Formato A.
     *
     * @param proyecto El proyecto asociado al evento.
     */
    void publishFormatoAEnviado(FormatoA proyecto);
}
