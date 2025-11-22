package co.unicauca.evaluation.model;
import jakarta.persistence.*;
import java.time.LocalDateTime;
@Entity
@Table(name = "evaluations")
public class Evaluation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;    
    @Column(name = "project_id", nullable = false)
    private Long projectId;    
    @Column(name = "evaluator_email", nullable = false)
    private String evaluatorEmail;    
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private EvaluationStatus status;    
    @Column(name = "score")
    private Double score;    
    @Column(name = "comments", length = 2000)
    private String comments;    
    @Column(name = "recommendations", length = 2000)
    private String recommendations;    
    @Column(name = "submission_date")
    private LocalDateTime submissionDate;    
    @Column(name = "evaluation_date")
    private LocalDateTime evaluationDate;    
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;    
    public Evaluation() {
        this.createdAt = LocalDateTime.now();
        this.status = EvaluationStatus.PENDING;
    }    
    public Evaluation(Long projectId, String evaluatorEmail) {
        this();
        this.projectId = projectId;
        this.evaluatorEmail = evaluatorEmail;
    }    
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
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }    
    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}