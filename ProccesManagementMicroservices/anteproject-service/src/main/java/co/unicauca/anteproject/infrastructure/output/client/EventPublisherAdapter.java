package co.unicauca.anteproject.infrastructure.output.client;

import co.unicauca.anteproject.domain.ports.out.AnteprojectEventPublisherPort;
import co.unicauca.anteproject.events.AnteprojectEventPublisher;
import co.unicauca.anteproject.events.AnteprojectSubmittedEvent;
import co.unicauca.anteproject.events.EvaluatorAssignmentEvent;
import org.springframework.stereotype.Component;

@Component
public class EventPublisherAdapter implements AnteprojectEventPublisherPort {

    private final AnteprojectEventPublisher eventPublisher;

    public EventPublisherAdapter(AnteprojectEventPublisher eventPublisher) {
        this.eventPublisher = eventPublisher;
    }

    @Override
    public void publishAnteprojectSubmitted(AnteprojectSubmittedEvent event) {
        eventPublisher.publishAnteprojectSubmitted(event);
    }

    @Override
    public void publishEvaluatorAssignment(EvaluatorAssignmentEvent event) {
        eventPublisher.publishEvaluatorAssignment(event);
    }
}
