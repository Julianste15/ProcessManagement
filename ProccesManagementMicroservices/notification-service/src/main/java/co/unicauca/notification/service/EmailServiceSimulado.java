package co.unicauca.notification.service;

import co.unicauca.notification.dto.EmailMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Logger;

@Service
public class EmailServiceSimulado {
    
    private static final Logger logger = Logger.getLogger(EmailServiceSimulado.class.getName());
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
    
    @Value("${notification.email.from:notificaciones@unicauca.edu.co}")
    private String fromEmail;
    
    /**
     * Simula el envío de email (solo logs)
     */
    public void sendEmail(EmailMessage emailMessage) {
        String timestamp = dateFormat.format(new Date());
        
        logger.info("╔══════════════════════════════════════════════════════════════╗");
        logger.info("║ 📧 SIMULACIÓN DE EMAIL - " + timestamp + " ║");
        logger.info("╠══════════════════════════════════════════════════════════════╣");
        logger.info("║ De:      " + fromEmail);
        logger.info("║ Para:    " + emailMessage.getTo());
        logger.info("║ Asunto:  " + emailMessage.getSubject());
        logger.info("╠══════════════════════════════════════════════════════════════╣");
        logger.info("║ Mensaje:");
        String[] lines = emailMessage.getMessage().split("\n");
        for (String line : lines) {
            logger.info("║   " + line);
        }
        logger.info("╠══════════════════════════════════════════════════════════════╣");
        logger.info("║ ✅ Email simulado exitosamente - Listo para producción ║");
        logger.info("╚══════════════════════════════════════════════════════════════╝\n");
    }
    
    /**
     * Siempre disponible para simulación
     */
    public boolean isEmailAvailable() {
        return true;
    }
    
    /**
     * Obtiene información del servicio
     */
    public String getServiceInfo() {
        return "Email Service Simulado - Listo para producción con SMTP real";
    }
}