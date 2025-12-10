package co.unicauca.formata.domain.state.states;

import co.unicauca.formata.domain.state.FormatoAStateContext;
import co.unicauca.formata.model.EstadoProyecto;

import java.util.logging.Logger;

/**
 * Estado: ANTEPROYECTO_SUBIDO
 * 
 * Comportamiento:
 * - ❌ No permite evaluación de Formato A
 * - ❌ No permite reenvío de Formato A
 * - ✅ Permite asignar evaluadores
 * 
 * Transiciones permitidas:
 * - ANTEPROYECTO_EN_EVALUACION (cuando se asignan evaluadores)
 */
public class AnteproyectoSubidoState extends AbstractFormatoAState {
    
    private static final Logger logger = Logger.getLogger(AnteproyectoSubidoState.class.getName());
    
    @Override
    public boolean canTransitionTo(EstadoProyecto newState) {
        return newState == EstadoProyecto.ANTEPROYECTO_EN_EVALUACION;
    }
    
    @Override
    public EstadoProyecto getEstado() {
        return EstadoProyecto.ANTEPROYECTO_SUBIDO;
    }
    
    @Override
    public void onEnter(FormatoAStateContext context) {
        logger.info("Anteproyecto subido para Formato A: " + context.getFormatoA().getId());
        logger.info("Esperando asignación de evaluadores");
    }
}
