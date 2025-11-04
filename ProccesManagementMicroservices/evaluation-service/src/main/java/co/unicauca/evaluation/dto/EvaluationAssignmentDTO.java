package co.unicauca.evaluation.dto;

import co.unicauca.evaluation.model.AssignmentStatus;
import java.time.LocalDateTime;

public class EvaluationAssignmentDTO {
    private Long id;
    private Long projectId;
    private String evaluator1Email;
    private String evaluator2Email;
    private String assignedBy;
    private LocalDateTime assignedDate;
    private LocalDateTime deadline;
    private AssignmentStatus status;
    
    // Constructores
    public EvaluationAssignmentDTO() {}
    
    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public Long getProjectId() { return projectId; }
    public void setProjectId(Long projectId) { this.projectId = projectId; }
    
    public String getEvaluator1Email() { return evaluator1Email; }
    public void setEvaluator1Email(String evaluator1Email) { this.evaluator1Email = evaluator1Email; }
    
    public String getEvaluator2Email() { return evaluator2Email; }
    public void setEvaluator2Email(String evaluator2Email) { this.evaluator2Email = evaluator2Email; }
    
    public String getAssignedBy() { return assignedBy; }
    public void setAssignedBy(String assignedBy) { this.assignedBy = assignedBy; }
    
    public LocalDateTime getAssignedDate() { return assignedDate; }
    public void setAssignedDate(LocalDateTime assignedDate) { this.assignedDate = assignedDate; }
    
    public LocalDateTime getDeadline() { return deadline; }
    public void setDeadline(LocalDateTime deadline) { this.deadline = deadline; }
    
    public AssignmentStatus getStatus() { return status; }
    public void setStatus(AssignmentStatus status) { this.status = status; }
}