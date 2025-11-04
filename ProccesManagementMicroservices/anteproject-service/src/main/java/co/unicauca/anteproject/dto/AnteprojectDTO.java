package co.unicauca.anteproject.dto;

import co.unicauca.anteproject.model.AnteprojectStatus;
import java.time.LocalDateTime;
import java.util.List;

public class AnteprojectDTO {
    private Long id;
    private Long formatoAId;
    private String titulo;
    private String studentEmail;
    private String directorEmail;
    private String documentUrl;
    private AnteprojectStatus status;
    private LocalDateTime submissionDate;
    private LocalDateTime evaluationDeadline;
    private LocalDateTime createdAt;
    private List<ProgressUpdateDTO> progressUpdates;
    
    // Constructores, Getters y Setters
    public AnteprojectDTO() {}
    
    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public Long getFormatoAId() { return formatoAId; }
    public void setFormatoAId(Long formatoAId) { this.formatoAId = formatoAId; }
    
    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }
    
    public String getStudentEmail() { return studentEmail; }
    public void setStudentEmail(String studentEmail) { this.studentEmail = studentEmail; }
    
    public String getDirectorEmail() { return directorEmail; }
    public void setDirectorEmail(String directorEmail) { this.directorEmail = directorEmail; }
    
    public String getDocumentUrl() { return documentUrl; }
    public void setDocumentUrl(String documentUrl) { this.documentUrl = documentUrl; }
    
    public AnteprojectStatus getStatus() { return status; }
    public void setStatus(AnteprojectStatus status) { this.status = status; }
    
    public LocalDateTime getSubmissionDate() { return submissionDate; }
    public void setSubmissionDate(LocalDateTime submissionDate) { this.submissionDate = submissionDate; }
    
    public LocalDateTime getEvaluationDeadline() { return evaluationDeadline; }
    public void setEvaluationDeadline(LocalDateTime evaluationDeadline) { this.evaluationDeadline = evaluationDeadline; }
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    
    public List<ProgressUpdateDTO> getProgressUpdates() { return progressUpdates; }
    public void setProgressUpdates(List<ProgressUpdateDTO> progressUpdates) { this.progressUpdates = progressUpdates; }
}