package co.unicauca.formata.domain.state.states;

import co.unicauca.formata.domain.state.FormatoAState;
import co.unicauca.formata.domain.state.FormatoAStateContext;
import co.unicauca.formata.dto.EvaluationRequest;
import co.unicauca.formata.dto.FormatARequest;
import co.unicauca.formata.model.EstadoProyecto;

/**
 * Clase base abstracta para los estados de FormatoA.
 * Proporciona implementaciones por defecto que lanzan excepciones.
 * Los estados concretos sobrescriben solo los métodos que permiten.
 */
public abstract class AbstractFormatoAState implements FormatoAState {
    
    @Override
    public void evaluate(FormatoAStateContext context, EvaluationRequest request) {
        throw new IllegalStateException(
            "No se puede evaluar un Formato A en estado " + getEstado()
        );
    }
    
    @Override
    public void resubmit(FormatoAStateContext context, FormatARequest request) {
        throw new IllegalStateException(
            "No se puede reenviar un Formato A en estado " + getEstado()
        );
    }
    
    @Override
    public void onEnter(FormatoAStateContext context) {
        // Comportamiento por defecto: no hacer nada
    }
    
    @Override
    public void onExit(FormatoAStateContext context) {
        // Comportamiento por defecto: no hacer nada
    }
    
    /**
     * Implementación por defecto de canTransitionTo.
     * Los estados concretos pueden sobrescribir para definir transiciones específicas.
     */
    @Override
    public abstract boolean canTransitionTo(EstadoProyecto newState);
}
