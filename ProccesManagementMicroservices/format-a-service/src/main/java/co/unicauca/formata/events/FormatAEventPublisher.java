package co.unicauca.formata.events;
import co.unicauca.formata.model.FormatoA;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;
@Service
public class FormatAEventPublisher {
    private static final Logger logger = Logger.getLogger(FormatAEventPublisher.class.getName());
    @Autowired
    private RabbitTemplate rabbitTemplate;
    @Value("${event.exchange:formato_events_exchange}")
    private String exchangeName;
    @Value("${event.routing-key:formato.a.enviado}")
    private String routingKey;
    /**
     * Publica evento cuando se envía un Formato A
     */
    public void publishFormatoAEnviado(FormatoA formatoA) {
        try {
            Map<String, Object> eventData = new HashMap<>();
            eventData.put("eventType", "FORMATO_A_ENVIADO");
            eventData.put("formatoAId", formatoA.getId());
            eventData.put("titulo", formatoA.getTitulo());
            eventData.put("directorEmail", formatoA.getDirectorEmail());
            eventData.put("modalidad", formatoA.getModalidad());
            eventData.put("fechaEnvio", formatoA.getFechaCreacion());
            eventData.put("intento", formatoA.getIntentos());
            rabbitTemplate.convertAndSend(exchangeName, routingKey, eventData);
            logger.info("Evento FORMOTO_A_ENVIADO publicado: " + formatoA.getId());
        } catch (Exception e) {
            logger.severe("Error publicando evento FORMOTO_A_ENVIADO: " + e.getMessage());
        }
    }
    /**
     * Publica evento cuando se evalúa un Formato A
     */
    public void publishFormatoAEvaluado(FormatoA formatoA, String observaciones) {
        try {
            Map<String, Object> eventData = new HashMap<>();
            eventData.put("eventType", "FORMATO_A_EVALUADO");
            eventData.put("formatoAId", formatoA.getId());
            eventData.put("titulo", formatoA.getTitulo());
            eventData.put("directorEmail", formatoA.getDirectorEmail());
            eventData.put("codirectorEmail", formatoA.getCodirectorEmail());
            eventData.put("studentEmail", formatoA.getStudentEmail());
            eventData.put("estado", formatoA.getEstado());
            eventData.put("observaciones", observaciones);
            eventData.put("fechaEvaluacion", java.time.LocalDateTime.now());
            rabbitTemplate.convertAndSend(exchangeName, "formato.a.evaluado", eventData);
            logger.info("Evento FORMOTO_A_EVALUADO publicado: " + formatoA.getId());
        } catch (Exception e) {
            logger.severe("Error publicando evento FORMOTO_A_EVALUADO: " + e.getMessage());
        }
    }
    /**
     * Publica evento cuando se reintenta un Formato A
     */
    public void publishFormatoAReintentado(FormatoA formatoA) {
        try {
            Map<String, Object> eventData = new HashMap<>();
            eventData.put("eventType", "FORMATO_A_REINTENTADO");
            eventData.put("formatoAId", formatoA.getId());
            eventData.put("titulo", formatoA.getTitulo());
            eventData.put("directorEmail", formatoA.getDirectorEmail());
            eventData.put("intento", formatoA.getIntentos());
            eventData.put("fechaReintento", java.time.LocalDateTime.now());
            rabbitTemplate.convertAndSend(exchangeName, "formato.a.reintentado", eventData);
            logger.info("Evento FORMOTO_A_REINTENTADO publicado: " + formatoA.getId());
        } catch (Exception e) {
            logger.severe("Error publicando evento FORMOTO_A_REINTENTADO: " + e.getMessage());
        }
    }
}