package co.unicauca.anteproject.events;

import java.time.LocalDateTime;

public class AnteprojectSubmittedEvent {
    private Long anteprojectId;
    private String studentEmail;
    private LocalDateTime submissionDate;
    private String documentUrl;

    public AnteprojectSubmittedEvent() {
    }

    public AnteprojectSubmittedEvent(Long anteprojectId, String studentEmail, LocalDateTime submissionDate, String documentUrl) {
        this.anteprojectId = anteprojectId;
        this.studentEmail = studentEmail;
        this.submissionDate = submissionDate;
        this.documentUrl = documentUrl;
    }

    public Long getAnteprojectId() {
        return anteprojectId;
    }

    public void setAnteprojectId(Long anteprojectId) {
        this.anteprojectId = anteprojectId;
    }

    public String getStudentEmail() {
        return studentEmail;
    }

    public void setStudentEmail(String studentEmail) {
        this.studentEmail = studentEmail;
    }

    public LocalDateTime getSubmissionDate() {
        return submissionDate;
    }

    public void setSubmissionDate(LocalDateTime submissionDate) {
        this.submissionDate = submissionDate;
    }

    public String getDocumentUrl() {
        return documentUrl;
    }

    public void setDocumentUrl(String documentUrl) {
        this.documentUrl = documentUrl;
    }
}
