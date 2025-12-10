package co.unicauca.formata.domain.state.states;

import co.unicauca.formata.domain.state.FormatoAStateContext;
import co.unicauca.formata.dto.FormatARequest;
import co.unicauca.formata.model.EstadoProyecto;
import co.unicauca.formata.model.FormatoA;

import java.time.LocalDate;
import java.util.logging.Logger;

/**
 * Estado: FORMATO_A_RECHAZADO
 * 
 * Comportamiento:
 * - ❌ No permite evaluación
 * - ✅ Permite reenvío (si intentos < 3)
 * 
 * Transiciones permitidas:
 * - FORMATO_A_EN_EVALUACION (cuando se reenvía)
 * 
 * Reglas de negocio:
 * - Máximo 3 intentos permitidos
 * - Incrementa el contador de intentos al reenviar
 */
public class RechazadoState extends AbstractFormatoAState {
    
    private static final Logger logger = Logger.getLogger(RechazadoState.class.getName());
    private static final int MAX_INTENTOS = 3;
    
    @Override
    public void resubmit(FormatoAStateContext context, FormatARequest request) {
        logger.info("Reenviando Formato A en estado RECHAZADO");
        
        FormatoA formatoA = context.getFormatoA();
        
        // Validar que no se haya alcanzado el máximo de intentos
        if (formatoA.getIntentos() >= MAX_INTENTOS) {
            throw new IllegalStateException(
                "Máximo de intentos alcanzado (" + MAX_INTENTOS + "). No se puede reintentar."
            );
        }
        
        // Actualizar los datos del Formato A
        formatoA.setTitulo(request.getTitulo());
        formatoA.setModalidad(request.getModalidad());
        formatoA.setDirectorEmail(request.getDirectorEmail());
        formatoA.setCodirectorEmail(request.getCodirectorEmail());
        formatoA.setStudentEmail(request.getStudentEmail());
        formatoA.setObjetivoGeneral(request.getObjetivoGeneral());
        formatoA.setObjetivosEspecificos(request.getObjetivosEspecificos());
        
        // Actualizar el archivo PDF si se proporcionó
        if (request.getArchivoPDF() != null && !request.getArchivoPDF().isBlank()) {
            formatoA.setArchivoPDF(request.getArchivoPDF());
        }
        
        formatoA.setFechaCreacion(LocalDate.now());
        
        // Incrementar intentos
        formatoA.incrementarIntentos();
        
        // Transicionar a EN_EVALUACION
        context.transitionTo(EstadoProyecto.FORMATO_A_EN_EVALUACION);
        
        logger.info("Formato A reenviado: " + formatoA.getId() + " - Intento: " + formatoA.getIntentos());
    }
    
    @Override
    public boolean canTransitionTo(EstadoProyecto newState) {
        return newState == EstadoProyecto.FORMATO_A_EN_EVALUACION;
    }
    
    @Override
    public EstadoProyecto getEstado() {
        return EstadoProyecto.FORMATO_A_RECHAZADO;
    }
    
    @Override
    public void onEnter(FormatoAStateContext context) {
        FormatoA formatoA = context.getFormatoA();
        logger.warning("Formato A rechazado: " + formatoA.getId() + 
                      " - Intento: " + formatoA.getIntentos() + "/" + MAX_INTENTOS);
        
        // Si ya alcanzó el máximo de intentos, registrar
        if (formatoA.getIntentos() >= MAX_INTENTOS) {
            logger.severe("Formato A " + formatoA.getId() + 
                         " ha alcanzado el máximo de intentos. No se puede reintentar.");
        }
    }
}
