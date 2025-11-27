package co.unicauca.coordination.dto;
import java.time.LocalDateTime;
public class DashboardStatsDTO {
    private Long coordinatorId;
    private Integer totalProjects;
    private Integer pendingFormatA;
    private Integer pendingAnteprojects;
    private Integer approvedProjects;
    private Integer rejectedProjects;
    private LocalDateTime lastUpdate;
    public DashboardStatsDTO() {
    }
    public DashboardStatsDTO(Long coordinatorId) {
        this.coordinatorId = coordinatorId;
        this.totalProjects = 0;
        this.pendingFormatA = 0;
        this.pendingAnteprojects = 0;
        this.approvedProjects = 0;
        this.rejectedProjects = 0;
        this.lastUpdate = LocalDateTime.now();
    }
    public Long getCoordinatorId() {return coordinatorId;}
    public void setCoordinatorId(Long coordinatorId) {this.coordinatorId = coordinatorId;}
    public Integer getTotalProjects() {return totalProjects;}
    public void setTotalProjects(Integer totalProjects) {this.totalProjects = totalProjects;}
    public Integer getPendingFormatA() {return pendingFormatA;}
    public void setPendingFormatA(Integer pendingFormatA) {this.pendingFormatA = pendingFormatA;}
    public Integer getPendingAnteprojects() {return pendingAnteprojects;}
    public void setPendingAnteprojects(Integer pendingAnteprojects) {this.pendingAnteprojects = pendingAnteprojects;}
    public Integer getApprovedProjects() {return approvedProjects;}
    public void setApprovedProjects(Integer approvedProjects) {this.approvedProjects = approvedProjects;}
    public Integer getRejectedProjects() {return rejectedProjects;}
    public void setRejectedProjects(Integer rejectedProjects) {this.rejectedProjects = rejectedProjects;}
    public LocalDateTime getLastUpdate() {return lastUpdate;}
    public void setLastUpdate(LocalDateTime lastUpdate) {this.lastUpdate = lastUpdate;}
}
