package co.unicauca.notification.service;

import co.unicauca.notification.model.NotificationTemplate;
import co.unicauca.notification.repository.NotificationTemplateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;
import java.util.logging.Logger;

@Service
public class TemplateService {
    
    private static final Logger logger = Logger.getLogger(TemplateService.class.getName());
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
    
    @Autowired
    private NotificationTemplateRepository templateRepository;
    
    /**
     * Inicializa templates por defecto
     */
    @jakarta.annotation.PostConstruct
    public void initDefaultTemplates() {
        createDefaultTemplateIfNotExists(
            "FORMATO_A_ENVIADO",
            "Formato A Enviado",
            "Nuevo Formato A Enviado - {titulo}",
            getFormatoAEnviadoTemplate(),
            "Template para notificar cuando se envía un Formato A"
        );
        
        createDefaultTemplateIfNotExists(
            "FORMATO_A_EVALUADO", 
            "Formato A Evaluado",
            "Formato A Evaluado - {titulo}",
            getFormatoAEvaluadoTemplate(),
            "Template para notificar evaluación de Formato A"
        );
        
        createDefaultTemplateIfNotExists(
            "FORMATO_A_REINTENTADO",
            "Formato A Reintentado", 
            "Formato A Reintentado - {titulo}",
            getFormatoAReintentadoTemplate(),
            "Template para notificar reintento de Formato A"
        );
    }
    
    private void createDefaultTemplateIfNotExists(String templateType, String name, 
                                                 String subject, String body, String description) {
        if (!templateRepository.existsByTemplateType(templateType)) {
            NotificationTemplate template = new NotificationTemplate(
                templateType, name, subject, body, description
            );
            templateRepository.save(template);
            logger.info("Template creado: " + templateType);
        }
    }
    
    /**
     * Genera template para Formato A enviado usando el template de la base de datos
     */
    public String generateFormatoAEnviadoTemplate(Long formatoAId, String titulo, 
                                                 String directorEmail, String modalidad, 
                                                 Integer intento) {
        Optional<NotificationTemplate> template = templateRepository.findByTemplateType("FORMATO_A_ENVIADO");
        
        if (template.isPresent()) {
            return template.get().processTemplate(
                "formatoAId", formatoAId.toString(),
                "titulo", titulo,
                "directorEmail", directorEmail,
                "modalidad", modalidad,
                "intento", intento.toString(),
                "fecha", dateFormat.format(new Date())
            );
        }
        
        // Fallback al template hardcodeado
        return getFormatoAEnviadoTemplate()
            .replace("{formatoAId}", formatoAId.toString())
            .replace("{titulo}", titulo)
            .replace("{directorEmail}", directorEmail)
            .replace("{modalidad}", modalidad)
            .replace("{intento}", intento.toString())
            .replace("{fecha}", dateFormat.format(new Date()));
    }
    
    /**
     * Genera template para Formato A evaluado
     */
    public String generateFormatoAEvaluadoTemplate(Long formatoAId, String titulo, 
                                                  String estado, String observaciones) {
        Optional<NotificationTemplate> template = templateRepository.findByTemplateType("FORMATO_A_EVALUADO");
        
        if (template.isPresent()) {
            return template.get().processTemplate(
                "formatoAId", formatoAId.toString(),
                "titulo", titulo,
                "estado", estado,
                "observaciones", observaciones != null ? observaciones : "Sin observaciones",
                "fecha", dateFormat.format(new Date())
            );
        }
        
        // Fallback al template hardcodeado
        String estadoEmoji = estado.equals("FORMATO_A_ACEPTADO") ? "✅" : "❌";
        String estadoTexto = estado.equals("FORMATO_A_ACEPTADO") ? "APROBADO" : "RECHAZADO";
        
        return getFormatoAEvaluadoTemplate()
            .replace("{formatoAId}", formatoAId.toString())
            .replace("{titulo}", titulo)
            .replace("{estadoEmoji}", estadoEmoji)
            .replace("{estadoTexto}", estadoTexto)
            .replace("{observaciones}", observaciones != null ? observaciones : "Sin observaciones")
            .replace("{fecha}", dateFormat.format(new Date()));
    }
    
    /**
     * Genera template para Formato A reintentado
     */
    public String generateFormatoAReintentadoTemplate(Long formatoAId, String titulo, 
                                                     String directorEmail, Integer intento) {
        Optional<NotificationTemplate> template = templateRepository.findByTemplateType("FORMATO_A_REINTENTADO");
        
        if (template.isPresent()) {
            return template.get().processTemplate(
                "formatoAId", formatoAId.toString(),
                "titulo", titulo,
                "directorEmail", directorEmail,
                "intento", intento.toString(),
                "fecha", dateFormat.format(new Date())
            );
        }
        
        // Fallback al template hardcodeado
        return getFormatoAReintentadoTemplate()
            .replace("{formatoAId}", formatoAId.toString())
            .replace("{titulo}", titulo)
            .replace("{directorEmail}", directorEmail)
            .replace("{intento}", intento.toString())
            .replace("{fecha}", dateFormat.format(new Date()));
    }
    
    // Templates por defecto (hardcodeados como fallback)
    private String getFormatoAEnviadoTemplate() {
        return "=== SISTEMA DE GESTIÓN DE TRABAJOS DE GRADO - UNICAUCA ===\n\n" +
               "📋 NUEVO FORMOTO A ENVIADO\n\n" +
               "🆔 ID del Formato: {formatoAId}\n" +
               "📚 Título: {titulo}\n" +
               "👤 Director: {directorEmail}\n" +
               "📖 Modalidad: {modalidad}\n" +
               "🔄 Intento: {intento}\n" +
               "📅 Fecha: {fecha}\n\n" +
               "📝 ACCIÓN REQUERIDA:\n" +
               "Por favor revise el Formato A en el sistema y realice la evaluación correspondiente.\n\n" +
               "🔗 Enlace al sistema: http://localhost:8080\n\n" +
               "Saludos cordiales,\n" +
               "Sistema de Gestión de Trabajos de Grado - FIET";
    }
    
    private String getFormatoAEvaluadoTemplate() {
        return "=== SISTEMA DE GESTIÓN DE TRABAJOS DE GRADO - UNICAUCA ===\n\n" +
               "📊 FORMOTO A EVALUADO\n\n" +
               "🆔 ID del Formato: {formatoAId}\n" +
               "📚 Título: {titulo}\n" +
               "📈 Estado: {estadoEmoji} {estadoTexto}\n" +
               "📅 Fecha: {fecha}\n\n" +
               "{observaciones}\n\n" +
               "📝 INSTRUCCIONES:\n" +
               "{estadoTexto}\n\n" +
               "🔗 Enlace al sistema: http://localhost:8080\n\n" +
               "Saludos cordiales,\n" +
               "Sistema de Gestión de Trabajos de Grado - FIET";
    }
    
    private String getFormatoAReintentadoTemplate() {
        return "=== SISTEMA DE GESTIÓN DE TRABAJOS DE GRADO - UNICAUCA ===\n\n" +
               "🔄 FORMOTO A REINTENTADO\n\n" +
               "🆔 ID del Formato: {formatoAId}\n" +
               "📚 Título: {titulo}\n" +
               "👤 Director: {directorEmail}\n" +
               "🔄 Intento: {intento}\n" +
               "📅 Fecha: {fecha}\n\n" +
               "📝 ACCIÓN REQUERIDA:\n" +
               "Se ha realizado un nuevo intento de envío del Formato A. " +
               "Por favor revise la nueva versión en el sistema.\n\n" +
               "🔗 Enlace al sistema: http://localhost:8080\n\n" +
               "Saludos cordiales,\n" +
               "Sistema de Gestión de Trabajos de Grado - FIET";
    }
}