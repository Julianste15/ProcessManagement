package co.unicauca.anteproject.domain.model;

import java.time.LocalDateTime;

public class Anteproject {
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
    private LocalDateTime updatedAt;
    // private List<ProjectProgress> progressUpdates; // Lo omito por ahora para simplificar la migración inicial

    public Anteproject() {
        this.createdAt = LocalDateTime.now();
        this.status = AnteprojectStatus.DRAFT;
    }

    public Anteproject(Long id, Long formatoAId, String titulo, String studentEmail, String directorEmail, 
                       String documentUrl, AnteprojectStatus status, LocalDateTime submissionDate, 
                       LocalDateTime evaluationDeadline, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.formatoAId = formatoAId;
        this.titulo = titulo;
        this.studentEmail = studentEmail;
        this.directorEmail = directorEmail;
        this.documentUrl = documentUrl;
        this.status = status;
        this.submissionDate = submissionDate;
        this.evaluationDeadline = evaluationDeadline;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    // Getters and Setters
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

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
