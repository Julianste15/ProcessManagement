package co.unicauca.evaluation.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "evaluation_assignments")
public class EvaluationAssignment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "project_id", nullable = false)
    private Long projectId;
    
    @Column(name = "evaluator1_email", nullable = false)
    private String evaluator1Email;
    
    @Column(name = "evaluator2_email", nullable = false)
    private String evaluator2Email;
    
    @Column(name = "assigned_by", nullable = false)
    private String assignedBy; // Email del coordinador/admin que asignó
    
    @Column(name = "assigned_date", nullable = false)
    private LocalDateTime assignedDate;
    
    @Column(name = "deadline")
    private LocalDateTime deadline;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private AssignmentStatus status;
    
    // Constructores
    public EvaluationAssignment() {
        this.assignedDate = LocalDateTime.now();
        this.status = AssignmentStatus.ACTIVE;
    }
    
    public EvaluationAssignment(Long projectId, String evaluator1Email, String evaluator2Email, String assignedBy) {
        this();
        this.projectId = projectId;
        this.evaluator1Email = evaluator1Email;
        this.evaluator2Email = evaluator2Email;
        this.assignedBy = assignedBy;
    }
    
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