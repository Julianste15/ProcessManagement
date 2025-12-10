package co.unicauca.notification.service;

import co.unicauca.notification.dto.EmailMessage;
import co.unicauca.notification.events.EvaluatorAssignmentEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.util.logging.Logger;

@Service
public class NotificationService {

    private static final Logger logger = Logger.getLogger(NotificationService.class.getName());

    @Autowired
    private EmailService emailService;

    @Autowired
    private co.unicauca.notification.factory.NotificationFactory notificationFactory;

    @Autowired
    private co.unicauca.notification.client.UserServiceClient userServiceClient;

    @Value("${notification.email.enabled:true}")
    private boolean notificationsEnabled;

    /** Notifica cuando se envía un Formato A */
    public void notifyFormatoAEnviado(Long formatoAId, String titulo, String directorEmail,
            String modalidad, Integer intento) {
        if (!notificationsEnabled) {
            logger.info("Notificaciones deshabilitadas - omitiendo Formato A enviado");
            return;
        }
        try {
            logger.info("Notificando Formato A enviado a coordinador");
            EmailMessage email = notificationFactory.createFormatoASubmittedMessage(
                    formatoAId, titulo, directorEmail, modalidad, intento);
            emailService.sendEmail(email);
        } catch (Exception e) {
            logger.severe("Error notificando Formato A enviado: " + e.getMessage());
        }
    }

    /** Notifica cuando se evalúa un Formato A */
    public void notifyFormatoAEvaluado(Long formatoAId, String titulo, String directorEmail,
            String codirectorEmail, String studentEmail, String estado, String observaciones) {
        if (!notificationsEnabled) {
            logger.info("Notificaciones deshabilitadas - omitiendo Formato A evaluado");
            return;
        }
        try {
            logger.info("Notificando evaluación de Formato A al director: " + directorEmail);
            EmailMessage directorMail = notificationFactory.createFormatoAEvaluatedMessage(
                    directorEmail, formatoAId, titulo, estado, observaciones);
            emailService.sendEmail(directorMail);

            if (codirectorEmail != null && !codirectorEmail.isEmpty()) {
                logger.info("Notificando evaluación de Formato A al codirector: " + codirectorEmail);
                EmailMessage codirectorMail = notificationFactory.createFormatoAEvaluatedMessage(
                        codirectorEmail, formatoAId, titulo, estado, observaciones);
                emailService.sendEmail(codirectorMail);
            }

            if (studentEmail != null && !studentEmail.isEmpty()) {
                logger.info("Notificando evaluación de Formato A al estudiante: " + studentEmail);
                EmailMessage studentMail = notificationFactory.createFormatoAEvaluatedMessage(
                        studentEmail, formatoAId, titulo, estado, observaciones);
                emailService.sendEmail(studentMail);
            }
            logger.info("Notificaciones enviadas para Formato A: " + formatoAId);
        } catch (Exception e) {
            logger.severe("Error notificando Formato A evaluado: " + e.getMessage());
        }
    }

    /** Notifica cuando se reintenta un Formato A */
    public void notifyFormatoAReintentado(Long formatoAId, String titulo, String directorEmail,
            Integer intento) {
        if (!notificationsEnabled) {
            logger.info("Notificaciones deshabilitadas - omitiendo Formato A reintentado");
            return;
        }
        try {
            logger.info("Notificando reintento de Formato A a coordinador");
            EmailMessage email = notificationFactory.createFormatoARetryMessage(
                    formatoAId, titulo, directorEmail, intento);
            emailService.sendEmail(email);
        } catch (Exception e) {
            logger.severe("Error notificando Formato A reintentado: " + e.getMessage());
        }
    }

    /** Envío de notificación genérica */
    public void sendGenericNotification(String toEmail, String subject, String message) {
        if (!notificationsEnabled) {
            logger.info("Notificaciones deshabilitadas - omitiendo notificación genérica");
            return;
        }
        try {
            logger.info("Enviando notificación genérica");
            EmailMessage email = new EmailMessage(toEmail, subject, message);
            emailService.sendEmail(email);
        } catch (Exception e) {
            logger.severe("Error enviando notificación genérica: " + e.getMessage());
        }
    }

    /** Notifica asignación de evaluadores */
    public void notifyEvaluatorAssignment(EvaluatorAssignmentEvent event) {
        if (!notificationsEnabled) {
            logger.info("Notificaciones deshabilitadas - omitiendo asignación de evaluadores");
            return;
        }
        try {
            logger.info("Notificando a evaluador 1: " + event.getEvaluator1Email());
            EmailMessage eval1Message = notificationFactory.createEvaluatorAssignmentMessage(
                    event.getEvaluator1Email(), event.getAnteprojectTitle());
            emailService.sendEmail(eval1Message);

            logger.info("Notificando a evaluador 2: " + event.getEvaluator2Email());
            EmailMessage eval2Message = notificationFactory.createEvaluatorAssignmentMessage(
                    event.getEvaluator2Email(), event.getAnteprojectTitle());
            emailService.sendEmail(eval2Message);

            // Opcional: notificar estudiante y director
            if (event.getStudentEmail() != null) {
                EmailMessage studentMessage = notificationFactory.createAnteprojectInEvaluationMessage(
                        event.getStudentEmail(), event.getAnteprojectTitle());
                emailService.sendEmail(studentMessage);
            }
            if (event.getDirectorEmail() != null) {
                EmailMessage directorMessage = notificationFactory.createAnteprojectInEvaluationMessage(
                        event.getDirectorEmail(), event.getAnteprojectTitle());
                emailService.sendEmail(directorMessage);
            }
        } catch (Exception e) {
            logger.severe("Error notificando asignación de evaluadores: " + e.getMessage());
        }
    }

    /** Notifica al jefe de departamento sobre un anteproyecto enviado */
    public void notifyDepartmentHead(co.unicauca.anteproject.events.AnteprojectSubmittedEvent event) {
        if (!notificationsEnabled) {
            logger.info("Notificaciones deshabilitadas - omitiendo notificación a jefe de departamento");
            return;
        }
        try {
            String departmentHeadEmail = userServiceClient.getCurrentDepartmentHeadEmail();
            if (departmentHeadEmail == null || departmentHeadEmail.isEmpty()) {
                logger.warning("No se encontró un jefe de departamento asignado actualmente");
                return;
            }

            logger.info("Notificando a jefe de departamento: " + departmentHeadEmail);
            EmailMessage message = notificationFactory.createDepartmentHeadNotificationMessage(
                    departmentHeadEmail, event);
            emailService.sendEmail(message);
        } catch (Exception e) {
            logger.severe("Error notificando a jefe de departamento: " + e.getMessage());
        }
    }

    /** Verifica estado del servicio */
    public String getServiceStatus() {
        return "Notification Service: " + (notificationsEnabled ? "ACTIVO " : "INACTIVO") +
                " | Email Service: " + (emailService.isEmailAvailable() ? "SIMULADO " : "ERROR");
    }
}