package co.unicauca.formata.domain.state;

import co.unicauca.formata.dto.EvaluationRequest;
import co.unicauca.formata.dto.FormatARequest;
import co.unicauca.formata.model.EstadoProyecto;
import co.unicauca.formata.model.FormatoA;
import co.unicauca.formata.domain.state.states.*;

/**
 * Contexto del patrón State para FormatoA.
 * Mantiene una referencia al estado actual y delega las operaciones al estado.
 */
public class FormatoAStateContext {
    
    private FormatoAState currentState;
    private final FormatoA formatoA;
    
    public FormatoAStateContext(FormatoA formatoA) {
        this.formatoA = formatoA;
        // Inicializar el estado basado en el estado actual del FormatoA
        this.currentState = createStateFromEstado(formatoA.getEstado());
    }
    
    /**
     * Evalúa el Formato A delegando al estado actual.
     */
    public void evaluate(EvaluationRequest request) {
        currentState.evaluate(this, request);
    }
    
    /**
     * Reenvía el Formato A delegando al estado actual.
     */
    public void resubmit(FormatARequest request) {
        currentState.resubmit(this, request);
    }
    
    /**
     * Cambia el estado actual.
     * Ejecuta onExit del estado anterior y onEnter del nuevo estado.
     */
    public void setState(FormatoAState newState) {
        if (this.currentState != null) {
            this.currentState.onExit(this);
        }
        this.currentState = newState;
        this.currentState.onEnter(this);
        // Sincronizar el estado del FormatoA
        this.formatoA.setEstado(newState.getEstado());
    }
    
    /**
     * Transiciona a un nuevo estado basado en el EstadoProyecto.
     */
    public void transitionTo(EstadoProyecto newEstado) {
        if (!currentState.canTransitionTo(newEstado)) {
            throw new IllegalStateException(
                "No se puede transicionar de " + currentState.getEstado() + 
                " a " + newEstado
            );
        }
        FormatoAState newState = createStateFromEstado(newEstado);
        setState(newState);
    }
    
    /**
     * Obtiene el estado actual.
     */
    public FormatoAState getCurrentState() {
        return currentState;
    }
    
    /**
     * Obtiene el FormatoA asociado.
     */
    public FormatoA getFormatoA() {
        return formatoA;
    }
    
    /**
     * Crea una instancia de estado basada en el EstadoProyecto.
     */
    private FormatoAState createStateFromEstado(EstadoProyecto estado) {
        if (estado == null) {
            return new EnEvaluacionState();
        }
        
        return switch (estado) {
            case FORMATO_A_EN_EVALUACION -> new EnEvaluacionState();
            case FORMATO_A_RECHAZADO -> new RechazadoState();
            case FORMATO_A_ACEPTADO -> new AceptadoState();
            case ANTEPROYECTO_SUBIDO -> new AnteproyectoSubidoState();
            case ANTEPROYECTO_EN_EVALUACION -> new AnteproyectoEnEvaluacionState();
            case ANTEPROYECTO_RECHAZADO -> new AnteproyectoRechazadoState();
            case ANTEPROYECTO_ACEPTADO -> new AnteproyectoAceptadoState();
        };
    }
}
