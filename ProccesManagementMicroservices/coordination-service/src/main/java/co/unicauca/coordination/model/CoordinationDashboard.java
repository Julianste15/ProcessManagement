package co.unicauca.coordination.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "coordination_dashboard")
public class CoordinationDashboard {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "coordinator_id")
    private Long coordinatorId;
    
    @ElementCollection
    @CollectionTable(name = "dashboard_projects")
    private List<Long> projectIds;
    
    @Column(name = "total_projects")
    private Integer totalProjects;
    
    @Column(name = "pending_evaluations")
    private Integer pendingEvaluations;
    
    @Column(name = "approved_projects")
    private Integer approvedProjects;
    
    @Column(name = "rejected_projects")
    private Integer rejectedProjects;    
    private LocalDateTime lastUpdate;
    public CoordinationDashboard() {}    
    public CoordinationDashboard(Long coordinatorId) {
        this.coordinatorId = coordinatorId;
        this.totalProjects = 0;
        this.pendingEvaluations = 0;
        this.approvedProjects = 0;
        this.rejectedProjects = 0;
        this.lastUpdate = LocalDateTime.now();
    }
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }    
    public Long getCoordinatorId() { return coordinatorId; }
    public void setCoordinatorId(Long coordinatorId) { this.coordinatorId = coordinatorId; }    
    public List<Long> getProjectIds() { return projectIds; }
    public void setProjectIds(List<Long> projectIds) { this.projectIds = projectIds; }    
    public Integer getTotalProjects() { return totalProjects; }
    public void setTotalProjects(Integer totalProjects) { this.totalProjects = totalProjects; }    
    public Integer getPendingEvaluations() { return pendingEvaluations; }
    public void setPendingEvaluations(Integer pendingEvaluations) { this.pendingEvaluations = pendingEvaluations; }    
    public Integer getApprovedProjects() { return approvedProjects; }
    public void setApprovedProjects(Integer approvedProjects) { this.approvedProjects = approvedProjects; }    
    public Integer getRejectedProjects() { return rejectedProjects; }
    public void setRejectedProjects(Integer rejectedProjects) { this.rejectedProjects = rejectedProjects; }    
    public LocalDateTime getLastUpdate() { return lastUpdate; }
    public void setLastUpdate(LocalDateTime lastUpdate) { this.lastUpdate = lastUpdate; }
}