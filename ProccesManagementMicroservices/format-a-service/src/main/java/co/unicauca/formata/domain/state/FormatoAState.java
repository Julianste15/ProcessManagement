package co.unicauca.formata.domain.state;

import co.unicauca.formata.dto.EvaluationRequest;
import co.unicauca.formata.dto.FormatARequest;
import co.unicauca.formata.model.EstadoProyecto;
import co.unicauca.formata.model.FormatoA;

/**
 * Interfaz base para el patrón State de FormatoA.
 * Define las operaciones que cada estado puede realizar.
 */
public interface FormatoAState {
    
    /**
     * Evalúa el Formato A (aprobado/rechazado).
     * Solo ciertos estados permiten esta operación.
     * 
     * @param context El contexto que contiene el FormatoA
     * @param request La solicitud de evaluación
     * @throws IllegalStateException si el estado actual no permite evaluación
     */
    void evaluate(FormatoAStateContext context, EvaluationRequest request);
    
    /**
     * Reenvía un Formato A rechazado.
     * Solo ciertos estados permiten esta operación.
     * 
     * @param context El contexto que contiene el FormatoA
     * @param request La solicitud de reenvío
     * @throws IllegalStateException si el estado actual no permite reenvío
     */
    void resubmit(FormatoAStateContext context, FormatARequest request);
    
    /**
     * Verifica si se puede transicionar al nuevo estado.
     * 
     * @param newState El estado al que se quiere transicionar
     * @return true si la transición es válida, false en caso contrario
     */
    boolean canTransitionTo(EstadoProyecto newState);
    
    /**
     * Obtiene el estado actual.
     * 
     * @return El estado actual del proyecto
     */
    EstadoProyecto getEstado();
    
    /**
     * Maneja el comportamiento al entrar en este estado.
     * 
     * @param context El contexto que contiene el FormatoA
     */
    void onEnter(FormatoAStateContext context);
    
    /**
     * Maneja el comportamiento al salir de este estado.
     * 
     * @param context El contexto que contiene el FormatoA
     */
    void onExit(FormatoAStateContext context);
}
