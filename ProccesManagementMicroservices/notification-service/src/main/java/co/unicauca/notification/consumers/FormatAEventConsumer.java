package co.unicauca.notification.consumers;

import co.unicauca.notification.service.NotificationService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.Map;
import java.util.logging.Logger;

@Component
public class FormatAEventConsumer {
    
    private static final Logger logger = Logger.getLogger(FormatAEventConsumer.class.getName());
    
    @Autowired
    private NotificationService notificationService;
    
    /**
     * Consume eventos de Formato A enviado
     */
    @RabbitListener(queues = "notifications_queue")
    public void handleFormatAEvent(Map<String, Object> eventData) {
        try {
            String eventType = (String) eventData.get("eventType");
            logger.info("📨 Evento recibido: " + eventType);
            
            switch (eventType) {
                case "FORMATO_A_ENVIADO":
                    handleFormatoAEnviado(eventData);
                    break;
                case "FORMATO_A_EVALUADO":
                    handleFormatoAEvaluado(eventData);
                    break;
                case "FORMATO_A_REINTENTADO":
                    handleFormatoAReintentado(eventData);
                    break;
                default:
                    logger.warning("Tipo de evento no manejado: " + eventType);
            }
            
        } catch (Exception e) {
            logger.severe("❌ Error procesando evento: " + e.getMessage());
        }
    }
    
    private void handleFormatoAEnviado(Map<String, Object> eventData) {
        try {
            String titulo = (String) eventData.get("titulo");
            String directorEmail = (String) eventData.get("directorEmail");
            Long formatoAId = ((Number) eventData.get("formatoAId")).longValue();
            String modalidad = (String) eventData.get("modalidad");
            Integer intento = (Integer) eventData.get("intento");
            
            logger.info("=== NOTIFICACIÓN: FORMOTO A ENVIADO ===");
            logger.info("📋 Título: " + titulo);
            logger.info("👤 Director: " + directorEmail);
            logger.info("🆔 ID: " + formatoAId);
            logger.info("📚 Modalidad: " + modalidad);
            logger.info("🔄 Intento: " + intento);
            logger.info("=======================================");
            
            // Notificar al coordinador
            notificationService.notifyFormatoAEnviado(
                formatoAId, titulo, directorEmail, modalidad, intento
            );
            
        } catch (Exception e) {
            logger.severe("Error en handleFormatoAEnviado: " + e.getMessage());
        }
    }
    
    private void handleFormatoAEvaluado(Map<String, Object> eventData) {
        try {
            String titulo = (String) eventData.get("titulo");
            String directorEmail = (String) eventData.get("directorEmail");
            Long formatoAId = ((Number) eventData.get("formatoAId")).longValue();
            String estado = (String) eventData.get("estado");
            String observaciones = (String) eventData.get("observaciones");
            
            logger.info("=== NOTIFICACIÓN: FORMOTO A EVALUADO ===");
            logger.info("📋 Título: " + titulo);
            logger.info("👤 Director: " + directorEmail);
            logger.info("🆔 ID: " + formatoAId);
            logger.info("📊 Estado: " + estado);
            logger.info("💬 Observaciones: " + observaciones);
            logger.info("=========================================");
            
            // Notificar al director
            notificationService.notifyFormatoAEvaluado(
                formatoAId, titulo, directorEmail, estado, observaciones
            );
            
        } catch (Exception e) {
            logger.severe("Error en handleFormatoAEvaluado: " + e.getMessage());
        }
    }
    
    private void handleFormatoAReintentado(Map<String, Object> eventData) {
        try {
            String titulo = (String) eventData.get("titulo");
            String directorEmail = (String) eventData.get("directorEmail");
            Long formatoAId = ((Number) eventData.get("formatoAId")).longValue();
            Integer intento = (Integer) eventData.get("intento");
            
            logger.info("=== NOTIFICACIÓN: FORMOTO A REINTENTADO ===");
            logger.info("📋 Título: " + titulo);
            logger.info("👤 Director: " + directorEmail);
            logger.info("🆔 ID: " + formatoAId);
            logger.info("🔄 Intento: " + intento);
            logger.info("===========================================");
            
            // Notificar al coordinador
            notificationService.notifyFormatoAReintentado(
                formatoAId, titulo, directorEmail, intento
            );
            
        } catch (Exception e) {
            logger.severe("Error en handleFormatoAReintentado: " + e.getMessage());
        }
    }
}