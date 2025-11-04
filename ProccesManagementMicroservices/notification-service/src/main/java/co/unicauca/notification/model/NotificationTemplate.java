package co.unicauca.notification.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "notification_templates")
public class NotificationTemplate {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "template_type", nullable = false, unique = true)
    private String templateType;
    
    @Column(name = "template_name", nullable = false)
    private String templateName;
    
    @Column(name = "subject_template", nullable = false, length = 500)
    private String subjectTemplate;
    
    @Column(name = "body_template", nullable = false, columnDefinition = "TEXT")
    private String bodyTemplate;
    
    @Column(name = "is_html", nullable = false)
    private boolean isHtml = false;
    
    @Column(name = "description")
    private String description;
    
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    // Constructores
    public NotificationTemplate() {
        this.createdAt = LocalDateTime.now();
    }
    
    public NotificationTemplate(String templateType, String templateName, 
                               String subjectTemplate, String bodyTemplate, 
                               String description) {
        this();
        this.templateType = templateType;
        this.templateName = templateName;
        this.subjectTemplate = subjectTemplate;
        this.bodyTemplate = bodyTemplate;
        this.description = description;
    }
    
    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getTemplateType() { return templateType; }
    public void setTemplateType(String templateType) { this.templateType = templateType; }
    
    public String getTemplateName() { return templateName; }
    public void setTemplateName(String templateName) { this.templateName = templateName; }
    
    public String getSubjectTemplate() { return subjectTemplate; }
    public void setSubjectTemplate(String subjectTemplate) { this.subjectTemplate = subjectTemplate; }
    
    public String getBodyTemplate() { return bodyTemplate; }
    public void setBodyTemplate(String bodyTemplate) { this.bodyTemplate = bodyTemplate; }
    
    public boolean isHtml() { return isHtml; }
    public void setHtml(boolean html) { isHtml = html; }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
    
    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
    
    /**
     * Reemplaza variables en el template
     */
    public String processTemplate(String... variables) {
        String processed = bodyTemplate;
        for (int i = 0; i < variables.length; i += 2) {
            if (i + 1 < variables.length) {
                String key = "{" + variables[i] + "}";
                String value = variables[i + 1];
                processed = processed.replace(key, value);
            }
        }
        return processed;
    }
    
    /**
     * Reemplaza variables en el subject
     */
    public String processSubject(String... variables) {
        String processed = subjectTemplate;
        for (int i = 0; i < variables.length; i += 2) {
            if (i + 1 < variables.length) {
                String key = "{" + variables[i] + "}";
                String value = variables[i + 1];
                processed = processed.replace(key, value);
            }
        }
        return processed;
    }
}