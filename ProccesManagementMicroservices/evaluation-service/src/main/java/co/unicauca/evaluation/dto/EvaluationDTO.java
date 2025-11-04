package co.unicauca.evaluation.dto;

import co.unicauca.evaluation.model.EvaluationStatus;
import java.time.LocalDateTime;

public class EvaluationDTO {
    private Long id;
    private Long projectId;
    private String evaluatorEmail;
    private EvaluationStatus status;
    private Double score;
    private String comments;
    private String recommendations;
    private LocalDateTime submissionDate;
    private LocalDateTime evaluationDate;
    private LocalDateTime createdAt;
    
    // Constructores
    public EvaluationDTO() {}
    
    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public Long getProjectId() { return projectId; }
    public void setProjectId(Long projectId) { this.projectId = projectId; }
    
    public String getEvaluatorEmail() { return evaluatorEmail; }
    public void setEvaluatorEmail(String evaluatorEmail) { this.evaluatorEmail = evaluatorEmail; }
    
    public EvaluationStatus getStatus() { return status; }
    public void setStatus(EvaluationStatus status) { this.status = status; }
    
    public Double getScore() { return score; }
    public void setScore(Double score) { this.score = score; }
    
    public String getComments() { return comments; }
    public void setComments(String comments) { this.comments = comments; }
    
    public String getRecommendations() { return recommendations; }
    public void setRecommendations(String recommendations) { this.recommendations = recommendations; }
    
    public LocalDateTime getSubmissionDate() { return submissionDate; }
    public void setSubmissionDate(LocalDateTime submissionDate) { this.submissionDate = submissionDate; }
    
    public LocalDateTime getEvaluationDate() { return evaluationDate; }
    public void setEvaluationDate(LocalDateTime evaluationDate) { this.evaluationDate = evaluationDate; }
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}