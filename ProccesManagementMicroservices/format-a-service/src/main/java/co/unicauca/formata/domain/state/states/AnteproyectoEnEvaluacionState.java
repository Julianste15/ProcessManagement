package co.unicauca.formata.domain.state.states;

import co.unicauca.formata.domain.state.FormatoAStateContext;
import co.unicauca.formata.model.EstadoProyecto;

import java.util.logging.Logger;

/**
 * Estado: ANTEPROYECTO_EN_EVALUACION
 * 
 * Comportamiento:
 * - ❌ No permite evaluación de Formato A
 * - ❌ No permite reenvío de Formato A
 * - ✅ Permite evaluación del anteproyecto
 * 
 * Transiciones permitidas:
 * - ANTEPROYECTO_ACEPTADO (cuando se aprueba el anteproyecto)
 * - ANTEPROYECTO_RECHAZADO (cuando se rechaza el anteproyecto)
 */
public class AnteproyectoEnEvaluacionState extends AbstractFormatoAState {
    
    private static final Logger logger = Logger.getLogger(AnteproyectoEnEvaluacionState.class.getName());
    
    @Override
    public boolean canTransitionTo(EstadoProyecto newState) {
        return newState == EstadoProyecto.ANTEPROYECTO_ACEPTADO || 
               newState == EstadoProyecto.ANTEPROYECTO_RECHAZADO;
    }
    
    @Override
    public EstadoProyecto getEstado() {
        return EstadoProyecto.ANTEPROYECTO_EN_EVALUACION;
    }
    
    @Override
    public void onEnter(FormatoAStateContext context) {
        logger.info("Anteproyecto en evaluación para Formato A: " + context.getFormatoA().getId());
        logger.info("Evaluadores asignados, esperando resultados de evaluación");
    }
}
