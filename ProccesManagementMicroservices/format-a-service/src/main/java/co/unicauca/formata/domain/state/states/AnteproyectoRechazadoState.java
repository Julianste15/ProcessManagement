package co.unicauca.formata.domain.state.states;

import co.unicauca.formata.domain.state.FormatoAStateContext;
import co.unicauca.formata.model.EstadoProyecto;

import java.util.logging.Logger;

/**
 * Estado: ANTEPROYECTO_RECHAZADO
 * 
 * Comportamiento:
 * - ❌ No permite evaluación
 * - ❌ No permite reenvío
 * - Estado final
 * 
 * Transiciones permitidas:
 * - Ninguna (estado final)
 */
public class AnteproyectoRechazadoState extends AbstractFormatoAState {
    
    private static final Logger logger = Logger.getLogger(AnteproyectoRechazadoState.class.getName());
    
    @Override
    public boolean canTransitionTo(EstadoProyecto newState) {
        // Estado final, no permite transiciones
        return false;
    }
    
    @Override
    public EstadoProyecto getEstado() {
        return EstadoProyecto.ANTEPROYECTO_RECHAZADO;
    }
    
    @Override
    public void onEnter(FormatoAStateContext context) {
        logger.warning("Anteproyecto rechazado para Formato A: " + context.getFormatoA().getId());
        logger.warning("Estado final alcanzado. No se permiten más acciones.");
    }
}
