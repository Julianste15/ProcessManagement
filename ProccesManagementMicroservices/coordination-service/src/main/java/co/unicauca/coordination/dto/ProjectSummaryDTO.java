package co.unicauca.coordination.dto;
import java.time.LocalDateTime;
public class ProjectSummaryDTO {
    private Long projectId;
    private String projectTitle;
    private String studentName;
    private String currentStage;
    private String status;
    private LocalDateTime lastActivity;

    public ProjectSummaryDTO() {}
    public ProjectSummaryDTO(Long projectId, String projectTitle, String studentName, String currentStage, String status, LocalDateTime lastActivity) {
        this.projectId = projectId;
        this.projectTitle = projectTitle;
        this.studentName = studentName;
        this.currentStage = currentStage;
        this.status = status;
        this.lastActivity = lastActivity;
    }

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

    public String getProjectTitle() {
        return projectTitle;
    }

    public void setProjectTitle(String projectTitle) {
        this.projectTitle = projectTitle;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public String getCurrentStage() {
        return currentStage;
    }

    public void setCurrentStage(String currentStage) {
        this.currentStage = currentStage;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getLastActivity() {
        return lastActivity;
    }

    public void setLastActivity(LocalDateTime lastActivity) {
        this.lastActivity = lastActivity;
    }
    
}
