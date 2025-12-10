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
    private TemplateService templateService;

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
            String subject = "Nuevo Formato A Enviado - " + titulo;
            String coordinatorEmail = "coordinador.sistemas@unicauca.edu.co";
            String message = templateService.generateFormatoAEnviadoTemplate(
                    formatoAId, titulo, directorEmail, modalidad, intento);
            logger.info("Notificando Formato A enviado a coordinador");
            EmailMessage email = new EmailMessage(coordinatorEmail, subject, message);
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
            String subject = "Formato A Evaluado - " + titulo;
            String message = templateService.generateFormatoAEvaluadoTemplate(
                    formatoAId, titulo, estado, observaciones);
            logger.info("Notificando evaluación de Formato A al director" + directorEmail);
            EmailMessage directorMail = new EmailMessage(directorEmail, subject, message);
            emailService.sendEmail(directorMail);
            if (codirectorEmail != null && !codirectorEmail.isEmpty()) {
                logger.info("Notificando evaluación de Formato A al codirector: " + codirectorEmail);
                EmailMessage codirectorMail = new EmailMessage(codirectorEmail, subject, message);
                emailService.sendEmail(codirectorMail);
            }
            if (studentEmail != null && !studentEmail.isEmpty()) {
                logger.info("Notificando evaluación de Formato A al estudiante: " + studentEmail);
                EmailMessage studentMail = new EmailMessage(studentEmail, subject, message);
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
            String subject = "Formato A Reintentado - " + titulo;
            String coordinatorEmail = "coordinador.sistemas@unicauca.edu.co";
            String message = templateService.generateFormatoAReintentadoTemplate(
                    formatoAId, titulo, directorEmail, intento);
            logger.info("Notificando reintento de Formato A a coordinador");
            EmailMessage email = new EmailMessage(coordinatorEmail, subject, message);
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
            String subject = "Asignación como Evaluador - " + event.getAnteprojectTitle();
            String message = String.format(
                    "Estimado docente,\n\n" +
                            "Ha sido asignado como evaluador del anteproyecto '%s'.\n" +
                            "Por favor ingrese a la plataforma para realizar la evaluación.\n\n" +
                            "Atentamente,\nCoordinación de Sistemas",
                    event.getAnteprojectTitle());
            logger.info("Notificando a evaluador 1: " + event.getEvaluator1Email());
            emailService.sendEmail(new EmailMessage(event.getEvaluator1Email(), subject, message));
            logger.info("Notificando a evaluador 2: " + event.getEvaluator2Email());
            emailService.sendEmail(new EmailMessage(event.getEvaluator2Email(), subject, message));
            // Opcional: notificar estudiante y director
            String infoSubject = "Anteproyecto en Evaluación - " + event.getAnteprojectTitle();
            String infoMessage = String.format(
                    "El anteproyecto '%s' ha sido asignado a evaluadores y se encuentra en proceso de evaluación.",
                    event.getAnteprojectTitle());
            if (event.getStudentEmail() != null) {
                emailService.sendEmail(new EmailMessage(event.getStudentEmail(), infoSubject, infoMessage));
            }
            if (event.getDirectorEmail() != null) {
                emailService.sendEmail(new EmailMessage(event.getDirectorEmail(), infoSubject, infoMessage));
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

            String subject = "Nuevo Anteproyecto Enviado - ID: " + event.getAnteprojectId();
            String message = String.format(
                    "Estimado Jefe de Departamento,\n\n" +
                            "Se ha enviado un nuevo anteproyecto para su revisión.\n" +
                            "ID: %d\n" +
                            "Estudiante: %s\n" +
                            "Fecha: %s\n" +
                            "Documento: %s\n\n" +
                            "Por favor ingrese a la plataforma para asignar evaluadores.\n\n" +
                            "Atentamente,\nSistema de Gestión de Procesos",
                    event.getAnteprojectId(),
                    event.getStudentEmail(),
                    event.getSubmissionDate(),
                    event.getDocumentUrl());

            logger.info("Notificando a jefe de departamento: " + departmentHeadEmail);
            emailService.sendEmail(new EmailMessage(departmentHeadEmail, subject, message));
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