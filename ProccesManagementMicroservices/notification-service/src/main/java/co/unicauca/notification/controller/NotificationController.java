package co.unicauca.notification.controller;
import co.unicauca.notification.dto.NotificationRequest;
import co.unicauca.notification.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import java.util.logging.Logger;
@RestController
@RequestMapping("/api/notifications")
public class NotificationController {
    private static final Logger logger = Logger.getLogger(NotificationController.class.getName());
    @Autowired
    private NotificationService notificationService;
    /**
     * Envía una notificación manualmente
     */
    @PostMapping("/send")
    public ResponseEntity<?> sendNotification(@Valid @RequestBody NotificationRequest request) {
        try {
            logger.info("Enviando notificación manual a: " + request.getToEmail());
            notificationService.sendGenericNotification(
                    request.getToEmail(),
                    request.getSubject(),
                    request.getMessage());
            return ResponseEntity.ok("Notificación simulada exitosamente");
        } catch (Exception e) {
            logger.severe("Error enviando notificación: " + e.getMessage());
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }
    /**
     * Envía notificación de Formato A enviado (para testing)
     */
    @PostMapping("/test/formato-enviado")
    public ResponseEntity<?> testFormatoAEnviado() {
        try {
            logger.info("Enviando notificación de prueba: Formato A Enviado");
            notificationService.notifyFormatoAEnviado(
                    999L,
                    "Sistema de Prueba con Microservicios",
                    "profesor.prueba@unicauca.edu.co",
                    "INVESTIGACION",
                    1);
            return ResponseEntity.ok("Notificación de prueba enviada");
        } catch (Exception e) {
            logger.severe("Error en notificación de prueba: " + e.getMessage());
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }
    /**
     * Envía notificación de Formato A evaluado (para testing)
     */
    @PostMapping("/test/formato-evaluado")
    public ResponseEntity<?> testFormatoAEvaluado() {
        try {
            logger.info("Enviando notificación de prueba: Formato A Evaluado");

            notificationService.notifyFormatoAEvaluado(
                    999L,
                    "Sistema de Prueba con Microservicios",
                    "profesor.prueba@unicauca.edu.co",
                    "codirector.prueba@unicauca.edu.co",
                    "estudiante.prueba@unicauca.edu.co",
                    "FORMATO_A_ACEPTADO",
                    "Excelente trabajo, puede continuar con el anteproyecto");
            return ResponseEntity.ok("Notificación de evaluación enviada");
        } catch (Exception e) {
            logger.severe("Error en notificación de evaluación: " + e.getMessage());
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }
    /**
     * Health check del servicio
     */
    @GetMapping("/health")
    public ResponseEntity<?> healthCheck() {
        String status = notificationService.getServiceStatus();
        return ResponseEntity.ok("Notification Service Status: " + status);
    }
    /**
     * Información del servicio
     */
    @GetMapping("/info")
    public ResponseEntity<?> getServiceInfo() {
        return ResponseEntity.ok("""
                📧 NOTIFICATION SERVICE - UNICAUCA FIET
                =====================================
                Estado: ACTIVO
                Modo:   SIMULACIÓN (Logs)
                Email:  Simulado - Listo para producción
                Eventos: RabbitMQ - Activo
                =====================================
                Endpoints disponibles:
                • POST /api/notifications/send
                • POST /api/notifications/test/formato-enviado
                • POST /api/notifications/test/formato-evaluado
                • GET  /api/notifications/health
                • GET  /api/notifications/info
                """);
    }
}