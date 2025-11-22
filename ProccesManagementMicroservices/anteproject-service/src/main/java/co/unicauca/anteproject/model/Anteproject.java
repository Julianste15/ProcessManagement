package co.unicauca.anteproject.model;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
@Entity
@Table(name = "anteprojects")
public class Anteproject {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;    
    @Column(name = "formato_a_id", nullable = false, unique = true)
    private Long formatoAId;    
    @Column(nullable = false)
    private String titulo;    
    @Column(name = "student_email", nullable = false)
    private String studentEmail;    
    @Column(name = "director_email", nullable = false)
    private String directorEmail;    
    @Column(name = "document_url")
    private String documentUrl;    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AnteprojectStatus status;    
    @Column(name = "submission_date")
    private LocalDateTime submissionDate;    
    @Column(name = "evaluation_deadline")
    private LocalDateTime evaluationDeadline;    
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;    
    @OneToMany(mappedBy = "anteproject", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ProjectProgress> progressUpdates = new ArrayList<>();   
    public Anteproject() {
        this.createdAt = LocalDateTime.now();
        this.status = AnteprojectStatus.DRAFT;
    }    
    public Anteproject(Long formatoAId, String titulo, String studentEmail, String directorEmail) {
        this();
        this.formatoAId = formatoAId;
        this.titulo = titulo;
        this.studentEmail = studentEmail;
        this.directorEmail = directorEmail;
    }    
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }    
    public Long getFormatoAId() { return formatoAId; }
    public void setFormatoAId(Long formatoAId) { this.formatoAId = formatoAId; }    
    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }    
    public String getStudentEmail() { return studentEmail; }
    public void setStudentEmail(String studentEmail) { this.studentEmail = studentEmail; }    
    public String getDirectorEmail() { return directorEmail; }
    public void setDirectorEmail(String directorEmail) { this.directorEmail = directorEmail; }    
    public String getDocumentUrl() { return documentUrl; }
    public void setDocumentUrl(String documentUrl) { this.documentUrl = documentUrl; }    
    public AnteprojectStatus getStatus() { return status; }
    public void setStatus(AnteprojectStatus status) { this.status = status; }    
    public LocalDateTime getSubmissionDate() { return submissionDate; }
    public void setSubmissionDate(LocalDateTime submissionDate) { this.submissionDate = submissionDate; }    
    public LocalDateTime getEvaluationDeadline() { return evaluationDeadline; }
    public void setEvaluationDeadline(LocalDateTime evaluationDeadline) { this.evaluationDeadline = evaluationDeadline; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }    
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }    
    public List<ProjectProgress> getProgressUpdates() { return progressUpdates; }
    public void setProgressUpdates(List<ProjectProgress> progressUpdates) { this.progressUpdates = progressUpdates; }    
    @PreUpdate
    public void preUpdate() {this.updatedAt = LocalDateTime.now();}
}