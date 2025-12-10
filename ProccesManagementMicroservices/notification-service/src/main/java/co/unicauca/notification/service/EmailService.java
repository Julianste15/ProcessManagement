package co.unicauca.notification.service;

import co.unicauca.notification.dto.EmailMessage;

/**
 * Interface for email service implementations.
 * Defines the contract for sending emails and checking service availability.
 */
public interface EmailService {
    
    /**
     * Sends an email message.
     * 
     * @param emailMessage The email message to send
     */
    void sendEmail(EmailMessage emailMessage);
    
    /**
     * Checks if the email service is available.
     * 
     * @return true if the service is available, false otherwise
     */
    boolean isEmailAvailable();
    
    /**
     * Gets information about the email service.
     * 
     * @return Service information as a string
     */
    String getServiceInfo();
}
