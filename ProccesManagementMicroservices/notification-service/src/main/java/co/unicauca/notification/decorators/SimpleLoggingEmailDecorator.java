package co.unicauca.notification.decorators;

import co.unicauca.notification.dto.EmailMessage;
import co.unicauca.notification.service.EmailService;
import java.util.logging.Logger;

/**
 * Decorador que agrega logging adicional al envío de correos.
 */
public class SimpleLoggingEmailDecorator extends EmailServiceDecorator {

    private static final Logger logger = Logger.getLogger(SimpleLoggingEmailDecorator.class.getName());

    public SimpleLoggingEmailDecorator(EmailService wrappedService) {
        super(wrappedService);
    }

    @Override
    public void sendEmail(EmailMessage emailMessage) {
        logger.info("--- [LOGGING DECORATOR] Iniciando envío de correo ---");
        logger.info("Destinatario: " + emailMessage.getTo());

        // Delegamos al servicio original
        super.sendEmail(emailMessage);

        logger.info("--- [LOGGING DECORATOR] Correo enviado exitosamente ---");
    }
}
