package co.unicauca.formata.domain.state.states;

import co.unicauca.formata.domain.state.FormatoAStateContext;
import co.unicauca.formata.model.EstadoProyecto;

import java.util.logging.Logger;

/**
 * Estado: FORMATO_A_ACEPTADO
 * 
 * Comportamiento:
 * - ❌ No permite evaluación
 * - ❌ No permite reenvío
 * - ✅ Permite subir anteproyecto
 * 
 * Transiciones permitidas:
 * - ANTEPROYECTO_SUBIDO (cuando se sube el anteproyecto)
 * 
 * Comportamiento especial:
 * - Al entrar en este estado, se pueden asignar evaluadores automáticamente
 */
public class AceptadoState extends AbstractFormatoAState {
    
    private static final Logger logger = Logger.getLogger(AceptadoState.class.getName());
    
    @Override
    public boolean canTransitionTo(EstadoProyecto newState) {
        return newState == EstadoProyecto.ANTEPROYECTO_SUBIDO;
    }
    
    @Override
    public EstadoProyecto getEstado() {
        return EstadoProyecto.FORMATO_A_ACEPTADO;
    }
    
    @Override
    public void onEnter(FormatoAStateContext context) {
        logger.info("Formato A aceptado: " + context.getFormatoA().getId());
        logger.info("El estudiante ahora puede subir su anteproyecto");
        
        // Aquí se podría notificar al estudiante o realizar otras acciones
        // Por ejemplo, enviar un evento para asignar evaluadores
    }
}
