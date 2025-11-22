package co.unicauca.evaluation.dto;
import jakarta.validation.constraints.NotNull;
public class CreateEvaluationRequest {
    @NotNull(message = "El ID del proyecto es requerido")
    private Long projectId;    
    @NotNull(message = "El email del evaluador es requerido")
    private String evaluatorEmail;    
    public Long getProjectId() { return projectId; }
    public void setProjectId(Long projectId) { this.projectId = projectId; }    
    public String getEvaluatorEmail() { return evaluatorEmail; }
    public void setEvaluatorEmail(String evaluatorEmail) { this.evaluatorEmail = evaluatorEmail; }
}