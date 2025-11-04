package co.unicauca.notification.service;

import co.unicauca.notification.dto.EmailMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.util.logging.Logger;

@Service
public class NotificationService {
    
    private static final Logger logger = Logger.getLogger(NotificationService.class.getName());
    
    @Autowired
    private EmailServiceSimulado emailService;
    
    @Autowired
    private TemplateService templateService;
    
    @Value("${notification.email.enabled:true}")
    private boolean notificationsEnabled;
    
    /**
     * Notifica cuando se envía un Formato A
     */
    public void notifyFormatoAEnviado(Long formatoAId, String titulo, String directorEmail, 
                                     String modalidad, Integer intento) {
        if (!notificationsEnabled) {
            logger.info("🔇 Notificaciones deshabilitadas - omitiendo Formato A enviado");
            return;
        }
        
        try {
            String subject = "Nuevo Formato A Enviado - " + titulo;
            String coordinatorEmail = "coordinador.sistemas@unicauca.edu.co";
            
            String message = templateService.generateFormatoAEnviadoTemplate(
                formatoAId, titulo, directorEmail, modalidad, intento
            );
            
            logger.info("📋 Notificando Formato A enviado a coordinador");
            
            EmailMessage email = new EmailMessage(coordinatorEmail, subject, message);
            emailService.sendEmail(email);
            
        } catch (Exception e) {
            logger.severe("❌ Error notificando Formato A enviado: " + e.getMessage());
        }
    }
    
    /**
     * Notifica cuando se evalúa un Formato A
     */
    public void notifyFormatoAEvaluado(Long formatoAId, String titulo, String directorEmail, 
                                      String estado, String observaciones) {
        if (!notificationsEnabled) {
            logger.info("🔇 Notificaciones deshabilitadas - omitiendo Formato A evaluado");
            return;
        }
        
        try {
            String subject = "Formato A Evaluado - " + titulo;
            
            String message = templateService.generateFormatoAEvaluadoTemplate(
                formatoAId, titulo, estado, observaciones
            );
            
            logger.info("📊 Notificando evaluación de Formato A al director");
            
            EmailMessage email = new EmailMessage(directorEmail, subject, message);
            emailService.sendEmail(email);
            
        } catch (Exception e) {
            logger.severe("❌ Error notificando Formato A evaluado: " + e.getMessage());
        }
    }
    
    /**
     * Notifica cuando se reintenta un Formato A
     */
    public void notifyFormatoAReintentado(Long formatoAId, String titulo, String directorEmail, 
                                         Integer intento) {
        if (!notificationsEnabled) {
            logger.info("🔇 Notificaciones deshabilitadas - omitiendo Formato A reintentado");
            return;
        }
        
        try {
            String subject = "Formato A Reintentado - " + titulo;
            String coordinatorEmail = "coordinador.sistemas@unicauca.edu.co";
            
            String message = templateService.generateFormatoAReintentadoTemplate(
                formatoAId, titulo, directorEmail, intento
            );
            
            logger.info("🔄 Notificando reintento de Formato A a coordinador");
            
            EmailMessage email = new EmailMessage(coordinatorEmail, subject, message);
            emailService.sendEmail(email);
            
        } catch (Exception e) {
            logger.severe("❌ Error notificando Formato A reintentado: " + e.getMessage());
        }
    }
    
    /**
     * Notificación genérica
     */
    public void sendGenericNotification(String toEmail, String subject, String message) {
        if (!notificationsEnabled) {
            logger.info("🔇 Notificaciones deshabilitadas - omitiendo notificación genérica");
            return;
        }
        
        try {
            logger.info("📨 Enviando notificación genérica");
            
            EmailMessage email = new EmailMessage(toEmail, subject, message);
            emailService.sendEmail(email);
            
        } catch (Exception e) {
            logger.severe("❌ Error enviando notificación genérica: " + e.getMessage());
        }
    }
    
    /**
     * Verifica estado del servicio
     */
    public String getServiceStatus() {
        return "Notification Service: " + (notificationsEnabled ? "ACTIVO ✅" : "INACTIVO 🔇") +
               " | Email Service: " + (emailService.isEmailAvailable() ? "SIMULADO 📧" : "ERROR ❌");
    }
}