package co.unicauca.anteproject.dto;
public class EvaluationAssignmentRequest {
    private Long projectId;
    private String evaluator1Email;
    private String evaluator2Email;
    private String assignedBy;    
    public EvaluationAssignmentRequest() {}
    public EvaluationAssignmentRequest(Long projectId, String evaluator1Email, String evaluator2Email, String assignedBy) {
        this.projectId = projectId;
        this.evaluator1Email = evaluator1Email;
        this.evaluator2Email = evaluator2Email;
        this.assignedBy = assignedBy;
    }    
    public Long getProjectId() { return projectId; }
    public void setProjectId(Long projectId) { this.projectId = projectId; }    
    public String getEvaluator1Email() { return evaluator1Email; }
    public void setEvaluator1Email(String evaluator1Email) { this.evaluator1Email = evaluator1Email; }    
    public String getEvaluator2Email() { return evaluator2Email; }
    public void setEvaluator2Email(String evaluator2Email) { this.evaluator2Email = evaluator2Email; }    
    public String getAssignedBy() { return assignedBy; }
    public void setAssignedBy(String assignedBy) { this.assignedBy = assignedBy; }
}