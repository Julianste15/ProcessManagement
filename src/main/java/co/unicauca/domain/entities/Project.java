package co.unicauca.domain.entities;

import co.unicauca.domain.enums.ProjectModality;
import co.unicauca.domain.enums.ProjectStatus;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Project {
    private String id;
    private String title;
    private ProjectModality modality;
    private User teacher;
    private User student;
    private String generalObjective;
    private List<String> specificObjectives;
    private ProjectStatus status;
    private int attemptNumber;
    private LocalDateTime submissionDate;
    private String pdfFilePath;
    private String acceptanceLetterPath; // Solo para práctica profesional
    private String committeeComments;
    
    // Constructores, getters y setters
    public Project() {
        this.specificObjectives = new ArrayList<>();
        this.status = ProjectStatus.DRAFT;
        this.attemptNumber = 1;
        this.submissionDate = LocalDateTime.now();
    }
    
    // Métodos de negocio
    public boolean canResubmit() {
        return this.attemptNumber < 3 && 
               (this.status == ProjectStatus.REJECTED || this.status == ProjectStatus.NEEDS_ADJUSTMENT);
    }
    
    public void prepareForResubmission() {
        if (canResubmit()) {
            this.attemptNumber++;
            this.status = ProjectStatus.DRAFT;
            this.committeeComments = null;
        }
    }
    
    // Getters y Setters...
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    
    public ProjectModality getModality() { return modality; }
    public void setModality(ProjectModality modality) { this.modality = modality; }
    
    public User getTeacher() { return teacher; }
    public void setTeacher(User teacher) { this.teacher = teacher; }
    
    public User getStudent() { return student; }
    public void setStudent(User student) { this.student = student; }
    
    public String getGeneralObjective() { return generalObjective; }
    public void setGeneralObjective(String generalObjective) { this.generalObjective = generalObjective; }
    
    public List<String> getSpecificObjectives() { return specificObjectives; }
    public void setSpecificObjectives(List<String> specificObjectives) { 
        this.specificObjectives = specificObjectives; 
    }
    
    public ProjectStatus getStatus() { return status; }
    public void setStatus(ProjectStatus status) { this.status = status; }
    
    public int getAttemptNumber() { return attemptNumber; }
    public void setAttemptNumber(int attemptNumber) { this.attemptNumber = attemptNumber; }
    
    public LocalDateTime getSubmissionDate() { return submissionDate; }
    public void setSubmissionDate(LocalDateTime submissionDate) { this.submissionDate = submissionDate; }
    
    public String getPdfFilePath() { return pdfFilePath; }
    public void setPdfFilePath(String pdfFilePath) { this.pdfFilePath = pdfFilePath; }
    
    public String getAcceptanceLetterPath() { return acceptanceLetterPath; }
    public void setAcceptanceLetterPath(String acceptanceLetterPath) { 
        this.acceptanceLetterPath = acceptanceLetterPath; 
    }
    
    public String getCommitteeComments() { return committeeComments; }
    public void setCommitteeComments(String committeeComments) { 
        this.committeeComments = committeeComments; 
    }
}