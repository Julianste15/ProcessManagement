package co.unicauca.notification.factory;

import co.unicauca.notification.dto.EmailMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Factory class for creating different types of email notification messages.
 * Implements the Factory design pattern to centralize email message creation
 * logic.
 */
@Component
public class NotificationFactory {

    @Value("${notification.coordinator.email:coordinador@unicauca.edu.co}")
    private String coordinatorEmail;

    @Value("${notification.system.name:Sistema de Gestión de Procesos}")
    private String systemName;

    /**
     * Creates an email message for when a Formato A is submitted
     */
    public EmailMessage createFormatoASubmittedMessage(
            Long formatoAId,
            String titulo,
            String directorEmail,
            String modalidad,
            Integer intento) {

        String subject = String.format("[%s] Nuevo Formato A Enviado - %s", systemName, titulo);

        String body = String.format(
                "Estimado/a Coordinador/a,\n\n" +
                        "Se ha recibido un nuevo Formato A para revisión:\n\n" +
                        "ID: %d\n" +
                        "Título: %s\n" +
                        "Director: %s\n" +
                        "Modalidad: %s\n" +
                        "Intento: %d\n\n" +
                        "Por favor, revise el formato en el sistema.\n\n" +
                        "Saludos cordiales,\n" +
                        "%s",
                formatoAId, titulo, directorEmail, modalidad, intento, systemName);

        return new EmailMessage(coordinatorEmail, subject, body);
    }

    /**
     * Creates an email message for when a Formato A is evaluated
     */
    public EmailMessage createFormatoAEvaluatedMessage(
            String recipientEmail,
            Long formatoAId,
            String titulo,
            String estado,
            String observaciones) {

        String subject = String.format("[%s] Formato A Evaluado - %s", systemName, titulo);

        String body = String.format(
                "Estimado/a usuario/a,\n\n" +
                        "El Formato A ha sido evaluado:\n\n" +
                        "ID: %d\n" +
                        "Título: %s\n" +
                        "Estado: %s\n" +
                        "Observaciones: %s\n\n" +
                        "Puede consultar los detalles completos en el sistema.\n\n" +
                        "Saludos cordiales,\n" +
                        "%s",
                formatoAId, titulo, estado,
                (observaciones != null && !observaciones.isEmpty()) ? observaciones : "Sin observaciones",
                systemName);

        return new EmailMessage(recipientEmail, subject, body);
    }

    /**
     * Creates an email message for when a Formato A is retried
     */
    public EmailMessage createFormatoARetryMessage(
            Long formatoAId,
            String titulo,
            String directorEmail,
            Integer intento) {

        String subject = String.format("[%s] Formato A Reintentado - %s", systemName, titulo);

        String body = String.format(
                "Estimado/a Coordinador/a,\n\n" +
                        "Se ha recibido un reintento de Formato A:\n\n" +
                        "ID: %d\n" +
                        "Título: %s\n" +
                        "Director: %s\n" +
                        "Número de Intento: %d\n\n" +
                        "Por favor, revise el formato actualizado en el sistema.\n\n" +
                        "Saludos cordiales,\n" +
                        "%s",
                formatoAId, titulo, directorEmail, intento, systemName);

        return new EmailMessage(coordinatorEmail, subject, body);
    }

    /**
     * Creates an email message for evaluator assignment notification
     */
    public EmailMessage createEvaluatorAssignmentMessage(
            String evaluatorEmail,
            String anteprojectTitle) {

        String subject = String.format("[%s] Asignación como Evaluador - %s", systemName, anteprojectTitle);

        String body = String.format(
                "Estimado/a Evaluador/a,\n\n" +
                        "Ha sido asignado/a como evaluador del siguiente anteproyecto:\n\n" +
                        "Título: %s\n\n" +
                        "Por favor, acceda al sistema para revisar los detalles y realizar la evaluación correspondiente.\n\n"
                        +
                        "Saludos cordiales,\n" +
                        "%s",
                anteprojectTitle, systemName);

        return new EmailMessage(evaluatorEmail, subject, body);
    }

    /**
     * Creates an email message for anteproject in evaluation notification
     */
    public EmailMessage createAnteprojectInEvaluationMessage(
            String recipientEmail,
            String anteprojectTitle) {

        String subject = String.format("[%s] Anteproyecto en Evaluación - %s", systemName, anteprojectTitle);

        String body = String.format(
                "Estimado/a usuario/a,\n\n" +
                        "Le informamos que el anteproyecto '%s' ha sido asignado a evaluadores " +
                        "y se encuentra actualmente en proceso de evaluación.\n\n" +
                        "Recibirá una notificación cuando la evaluación haya sido completada.\n\n" +
                        "Saludos cordiales,\n" +
                        "%s",
                anteprojectTitle, systemName);

        return new EmailMessage(recipientEmail, subject, body);
    }

    /**
     * Creates an email message for department head notification
     */
    public EmailMessage createDepartmentHeadNotificationMessage(
            String departmentHeadEmail,
            co.unicauca.anteproject.events.AnteprojectSubmittedEvent event) {

        String subject = String.format("[%s] Nuevo Anteproyecto Enviado - ID: %d",
                systemName, event.getAnteprojectId());

        String body = String.format(
                "Estimado/a Jefe de Departamento,\n\n" +
                        "Se ha recibido un nuevo anteproyecto para su conocimiento:\n\n" +
                        "ID: %d\n" +
                        "Estudiante: %s\n" +
                        "Fecha de Envío: %s\n" +
                        "Documento: %s\n\n" +
                        "El anteproyecto será asignado a evaluadores para su revisión.\n\n" +
                        "Saludos cordiales,\n" +
                        "%s",
                event.getAnteprojectId(),
                event.getStudentEmail() != null ? event.getStudentEmail() : "No especificado",
                event.getSubmissionDate() != null ? event.getSubmissionDate().toString() : "No especificada",
                event.getDocumentUrl() != null ? event.getDocumentUrl() : "No disponible",
                systemName);

        return new EmailMessage(departmentHeadEmail, subject, body);
    }
}
