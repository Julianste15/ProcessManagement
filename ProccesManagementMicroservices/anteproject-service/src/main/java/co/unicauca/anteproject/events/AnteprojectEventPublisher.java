package co.unicauca.anteproject.events;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.logging.Logger;
@Service
public class AnteprojectEventPublisher {    
    private static final Logger logger = Logger.getLogger(AnteprojectEventPublisher.class.getName());    
    @Autowired
    private RabbitTemplate rabbitTemplate;    
    public void publishEvaluatorAssignment(EvaluatorAssignmentEvent event) {
        try {
            logger.info("Publicando evento de asignación de evaluadores para anteproyecto: " + event.getAnteprojectId());
            rabbitTemplate.convertAndSend("anteproject.notification.queue", event);
            logger.info("Evento publicado exitosamente");
        } catch (Exception e) {
            logger.severe("Error publicando evento: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void publishAnteprojectSubmitted(AnteprojectSubmittedEvent event) {
        try {
            logger.info("Publicando evento de envío de anteproyecto: " + event.getAnteprojectId());
            rabbitTemplate.convertAndSend("anteproject.submitted.queue", event);
            logger.info("Evento publicado exitosamente");
        } catch (Exception e) {
            logger.severe("Error publicando evento: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
