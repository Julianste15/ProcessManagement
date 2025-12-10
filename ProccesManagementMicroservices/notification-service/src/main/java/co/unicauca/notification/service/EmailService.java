package co.unicauca.notification.service;

import co.unicauca.notification.dto.EmailMessage;

/**
 * Interface base para el servicio de envío de correos.
 * Permite aplicar el patrón Decorator.
 */
public interface EmailService {
    void sendEmail(EmailMessage emailMessage);
    boolean isEmailAvailable();
}
