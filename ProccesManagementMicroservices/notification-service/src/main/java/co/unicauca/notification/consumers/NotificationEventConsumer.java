package co.unicauca.notification.consumers;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import java.util.Map;
import java.util.logging.Logger;
@Component
public class NotificationEventConsumer {    
    private static final Logger logger = Logger.getLogger(NotificationEventConsumer.class.getName());    
    /**
     * Consume eventos generales de notificación
     */
    @RabbitListener(queues = "evaluations_queue")
    public void handleEvaluationEvent(Map<String, Object> eventData) {
        try {
            String eventType = (String) eventData.get("eventType");
            logger.info("Evento de evaluación recibido: " + eventType);
            logger.info("Datos del evento: " + eventData);
        } catch (Exception e) {
            logger.severe("Error procesando evento de evaluación: " + e.getMessage());
        }
    }
}