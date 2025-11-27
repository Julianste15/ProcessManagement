package co.unicauca.notification.consumers;

import co.unicauca.anteproject.events.AnteprojectSubmittedEvent;
import co.unicauca.notification.service.NotificationService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.logging.Logger;

@Component
public class AnteprojectSubmittedConsumer {

    private static final Logger logger = Logger.getLogger(AnteprojectSubmittedConsumer.class.getName());

    @Autowired
    private NotificationService notificationService;

    @RabbitListener(queues = "anteproject.submitted.queue")
    public void receiveAnteprojectSubmitted(AnteprojectSubmittedEvent event) {
        logger.info("Recibido evento de envío de anteproyecto: " + event.getAnteprojectId());
        try {
            notificationService.notifyDepartmentHead(event);
        } catch (Exception e) {
            logger.severe("Error procesando evento de envío de anteproyecto: " + e.getMessage());
        }
    }
}
