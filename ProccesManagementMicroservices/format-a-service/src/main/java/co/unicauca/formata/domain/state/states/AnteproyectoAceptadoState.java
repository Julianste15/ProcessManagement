package co.unicauca.formata.domain.state.states;

import co.unicauca.formata.domain.state.FormatoAStateContext;
import co.unicauca.formata.model.EstadoProyecto;

import java.util.logging.Logger;

/**
 * Estado: ANTEPROYECTO_ACEPTADO
 * 
 * Comportamiento:
 * - ❌ No permite evaluación
 * - ❌ No permite reenvío
 * - Estado final exitoso
 * 
 * Transiciones permitidas:
 * - Ninguna (estado final)
 */
public class AnteproyectoAceptadoState extends AbstractFormatoAState {
    
    private static final Logger logger = Logger.getLogger(AnteproyectoAceptadoState.class.getName());
    
    @Override
    public boolean canTransitionTo(EstadoProyecto newState) {
        // Estado final, no permite transiciones
        return false;
    }
    
    @Override
    public EstadoProyecto getEstado() {
        return EstadoProyecto.ANTEPROYECTO_ACEPTADO;
    }
    
    @Override
    public void onEnter(FormatoAStateContext context) {
        logger.info("¡Anteproyecto aceptado para Formato A: " + context.getFormatoA().getId() + "!");
        logger.info("Estado final exitoso alcanzado. El proyecto puede continuar.");
    }
}
