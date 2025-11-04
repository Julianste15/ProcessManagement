package co.unicauca.notification.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class NotificationRequest {
    
    @NotBlank(message = "El email destino es obligatorio")
    @Email(message = "El formato del email es inválido")
    private String toEmail;
    
    @NotBlank(message = "El asunto es obligatorio")
    private String subject;
    
    @NotBlank(message = "El mensaje es obligatorio")
    private String message;
    
    private String templateType;
    
    // Getters y Setters
    public String getToEmail() { return toEmail; }
    public void setToEmail(String toEmail) { this.toEmail = toEmail; }
    
    public String getSubject() { return subject; }
    public void setSubject(String subject) { this.subject = subject; }
    
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
    
    public String getTemplateType() { return templateType; }
    public void setTemplateType(String templateType) { this.templateType = templateType; }
}