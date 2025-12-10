package co.unicauca.notification.decorators;

import co.unicauca.notification.dto.EmailMessage;
import co.unicauca.notification.service.EmailService;

/**
 * Decorador base para servicios de email.
 * Implementa la interfaz pero delega en el objeto envuelto (wrappee).
 */
public abstract class EmailServiceDecorator implements EmailService {

    protected EmailService wrappedService;

    public EmailServiceDecorator(EmailService wrappedService) {
        this.wrappedService = wrappedService;
    }

    @Override
    public void sendEmail(EmailMessage emailMessage) {
        wrappedService.sendEmail(emailMessage);
    }

    @Override
    public boolean isEmailAvailable() {
        return wrappedService.isEmailAvailable();
    }
}
