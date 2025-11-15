package co.unicauca.anteproject.model;
import jakarta.persistence.*;
import java.time.LocalDateTime;
@Entity
@Table(name = "project_progress")
public class ProjectProgress {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "anteproject_id", nullable = false)
    private Anteproject anteproject;    
    @Column(nullable = false)
    private String description;    
    @Column(name = "progress_percentage")
    private Integer progressPercentage;    
    @Column(name = "created_by", nullable = false)
    private String createdBy; 
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;   
    public ProjectProgress() {
        this.createdAt = LocalDateTime.now();
    }    
    public ProjectProgress(Anteproject anteproject, String description, String createdBy) {
        this();
        this.anteproject = anteproject;
        this.description = description;
        this.createdBy = createdBy;
    }
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }    
    public Anteproject getAnteproject() { return anteproject; }
    public void setAnteproject(Anteproject anteproject) { this.anteproject = anteproject; }    
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }    
    public Integer getProgressPercentage() { return progressPercentage; }
    public void setProgressPercentage(Integer progressPercentage) { this.progressPercentage = progressPercentage; }    
    public String getCreatedBy() { return createdBy; }
    public void setCreatedBy(String createdBy) { this.createdBy = createdBy; }    
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}