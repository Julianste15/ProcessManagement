package co.unicauca.formata.domain.state.states;

import co.unicauca.formata.domain.state.FormatoAStateContext;
import co.unicauca.formata.dto.EvaluationRequest;
import co.unicauca.formata.model.EstadoProyecto;
import co.unicauca.formata.model.FormatoA;

import java.util.logging.Logger;

/**
 * Estado: FORMATO_A_EN_EVALUACION
 * 
 * Comportamiento:
 * - ✅ Permite evaluación (aprobar o rechazar)
 * - ❌ No permite reenvío
 * 
 * Transiciones permitidas:
 * - FORMATO_A_ACEPTADO (cuando se aprueba)
 * - FORMATO_A_RECHAZADO (cuando se rechaza)
 */
public class EnEvaluacionState extends AbstractFormatoAState {
    
    private static final Logger logger = Logger.getLogger(EnEvaluacionState.class.getName());
    
    @Override
    public void evaluate(FormatoAStateContext context, EvaluationRequest request) {
        logger.info("Evaluando Formato A en estado EN_EVALUACION");
        
        FormatoA formatoA = context.getFormatoA();
        EstadoProyecto nuevoEstado = request.getEstado();
        
        // Validar que la transición sea válida
        if (!canTransitionTo(nuevoEstado)) {
            throw new IllegalStateException(
                "Transición inválida desde EN_EVALUACION a " + nuevoEstado
            );
        }
        
        // Actualizar observaciones
        formatoA.setObservaciones(request.getObservaciones());
        
        // Transicionar al nuevo estado
        context.transitionTo(nuevoEstado);
        
        logger.info("Formato A evaluado: " + formatoA.getId() + " - Nuevo estado: " + nuevoEstado);
    }
    
    @Override
    public boolean canTransitionTo(EstadoProyecto newState) {
        return newState == EstadoProyecto.FORMATO_A_ACEPTADO || 
               newState == EstadoProyecto.FORMATO_A_RECHAZADO;
    }
    
    @Override
    public EstadoProyecto getEstado() {
        return EstadoProyecto.FORMATO_A_EN_EVALUACION;
    }
    
    @Override
    public void onEnter(FormatoAStateContext context) {
        logger.info("Entrando en estado EN_EVALUACION para Formato A: " + context.getFormatoA().getId());
    }
}
