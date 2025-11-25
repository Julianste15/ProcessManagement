package co.unicauca.notification.consumers;
import co.unicauca.notification.events.EvaluatorAssignmentEvent;
import co.unicauca.notification.service.NotificationService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.logging.Logger;
@Component
public class AnteprojectEventConsumer {
    private static final Logger logger = Logger.getLogger(AnteprojectEventConsumer.class.getName());
    @Autowired
    private NotificationService notificationService;
    @RabbitListener(queues = "anteproject.notification.queue")
    public void receiveEvaluatorAssignment(EvaluatorAssignmentEvent event) {
        logger.info("Recibido evento de asignación de evaluadores para anteproyecto: " + event.getAnteprojectId());
        try {
            notificationService.notifyEvaluatorAssignment(event);
        } catch (Exception e) {
            logger.severe("Error procesando evento de asignación: " + e.getMessage());
        }
    }
}
