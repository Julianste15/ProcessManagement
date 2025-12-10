package co.unicauca.notification.factory;

import co.unicauca.notification.dto.EmailMessage;
import co.unicauca.notification.service.TemplateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Factory para crear objetos EmailMessage según el tipo de notificación.
 * Centraliza la lógica de construcción de mensajes.
 */
@Component
public class NotificationFactory {

    @Autowired
    private TemplateService templateService;

    /**
     * Crea mensaje para Formato A enviado
     */
    public EmailMessage createFormatoASubmittedMessage(Long formatoAId, String titulo,
            String directorEmail, String modalidad,
            Integer intento) {
        String coordinatorEmail = "coordinador.sistemas@unicauca.edu.co";
        String subject = "Nuevo Formato A Enviado - " + titulo;
        String message = templateService.generateFormatoAEnviadoTemplate(
                formatoAId, titulo, directorEmail, modalidad, intento);

        return new EmailMessage(coordinatorEmail, subject, message);
    }

    /**
     * Crea mensajes para Formato A evaluado (director, codirector, estudiante)
     */
    public EmailMessage createFormatoAEvaluatedMessage(String recipientEmail, Long formatoAId,
            String titulo, String estado,
            String observaciones) {
        String subject = "Formato A Evaluado - " + titulo;
        String message = templateService.generateFormatoAEvaluadoTemplate(
                formatoAId, titulo, estado, observaciones);

        return new EmailMessage(recipientEmail, subject, message);
    }

    /**
     * Crea mensaje para Formato A reintentado
     */
    public EmailMessage createFormatoARetryMessage(Long formatoAId, String titulo,
            String directorEmail, Integer intento) {
        String coordinatorEmail = "coordinador.sistemas@unicauca.edu.co";
        String subject = "Formato A Reintentado - " + titulo;
        String message = templateService.generateFormatoAReintentadoTemplate(
                formatoAId, titulo, directorEmail, intento);

        return new EmailMessage(coordinatorEmail, subject, message);
    }

    /**
     * Crea mensaje para asignación de evaluador
     */
    public EmailMessage createEvaluatorAssignmentMessage(String evaluatorEmail,
            String anteprojectTitle) {
        String subject = "Asignación como Evaluador - " + anteprojectTitle;
        String message = String.format(
                "Estimado docente,\\n\\n" +
                        "Ha sido asignado como evaluador del anteproyecto '%s'.\\n" +
                        "Por favor ingrese a la plataforma para realizar la evaluación.\\n\\n" +
                        "Atentamente,\\nCoordinación de Sistemas",
                anteprojectTitle);

        return new EmailMessage(evaluatorEmail, subject, message);
    }

    /**
     * Crea mensaje informativo sobre anteproyecto en evaluación
     */
    public EmailMessage createAnteprojectInEvaluationMessage(String recipientEmail,
            String anteprojectTitle) {
        String subject = "Anteproyecto en Evaluación - " + anteprojectTitle;
        String message = String.format(
                "El anteproyecto '%s' ha sido asignado a evaluadores y se encuentra en proceso de evaluación.",
                anteprojectTitle);

        return new EmailMessage(recipientEmail, subject, message);
    }

    /**
     * Crea mensaje para jefe de departamento sobre nuevo anteproyecto
     */
    public EmailMessage createDepartmentHeadNotificationMessage(String departmentHeadEmail,
            co.unicauca.anteproject.events.AnteprojectSubmittedEvent event) {
        String subject = "Nuevo Anteproyecto Enviado - ID: " + event.getAnteprojectId();
        String message = String.format(
                "Estimado Jefe de Departamento,\\n\\n" +
                        "Se ha enviado un nuevo anteproyecto para su revisión.\\n" +
                        "ID: %d\\n" +
                        "Estudiante: %s\\n" +
                        "Fecha: %s\\n" +
                        "Documento: %s\\n\\n" +
                        "Por favor ingrese a la plataforma para asignar evaluadores.\\n\\n" +
                        "Atentamente,\\nSistema de Gestión de Procesos",
                event.getAnteprojectId(),
                event.getStudentEmail(),
                event.getSubmissionDate(),
                event.getDocumentUrl());

        return new EmailMessage(departmentHeadEmail, subject, message);
    }
}
