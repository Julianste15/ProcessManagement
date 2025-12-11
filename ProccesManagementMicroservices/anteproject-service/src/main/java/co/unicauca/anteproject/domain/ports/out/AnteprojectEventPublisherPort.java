package co.unicauca.anteproject.domain.ports.out;

import co.unicauca.anteproject.events.AnteprojectSubmittedEvent;
import co.unicauca.anteproject.events.EvaluatorAssignmentEvent;

/**
 * Puerto de salida para publicación de eventos de dominio
 */
public interface AnteprojectEventPublisherPort {

    /**
     * Publica evento cuando se envía un anteproyecto
     */
    void publishAnteprojectSubmitted(AnteprojectSubmittedEvent event);

    /**
     * Publica evento cuando se asignan evaluadores
     */
    void publishEvaluatorAssignment(EvaluatorAssignmentEvent event);
}
