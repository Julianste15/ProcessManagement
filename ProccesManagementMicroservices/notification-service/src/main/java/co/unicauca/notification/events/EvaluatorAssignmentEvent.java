package co.unicauca.notification.events;
import java.io.Serializable;
public class EvaluatorAssignmentEvent implements Serializable {
    private Long anteprojectId;
    private String anteprojectTitle;
    private String evaluator1Email;
    private String evaluator2Email;
    private String directorEmail;
    private String studentEmail;
    public EvaluatorAssignmentEvent() {}
    public EvaluatorAssignmentEvent(Long anteprojectId, String anteprojectTitle, String evaluator1Email,
            String evaluator2Email, String directorEmail, String studentEmail) {
        this.anteprojectId = anteprojectId;
        this.anteprojectTitle = anteprojectTitle;
        this.evaluator1Email = evaluator1Email;
        this.evaluator2Email = evaluator2Email;
        this.directorEmail = directorEmail;
        this.studentEmail = studentEmail;
    }
    public Long getAnteprojectId() {return anteprojectId;}
    public void setAnteprojectId(Long anteprojectId) {this.anteprojectId = anteprojectId;}
    public String getAnteprojectTitle() {return anteprojectTitle;}
    public void setAnteprojectTitle(String anteprojectTitle) {this.anteprojectTitle = anteprojectTitle;}
    public String getEvaluator1Email() {return evaluator1Email;}
    public void setEvaluator1Email(String evaluator1Email) {this.evaluator1Email = evaluator1Email;}
    public String getEvaluator2Email() {return evaluator2Email;}
    public void setEvaluator2Email(String evaluator2Email) {this.evaluator2Email = evaluator2Email;}
    public String getDirectorEmail() {return directorEmail;}
    public void setDirectorEmail(String directorEmail) {this.directorEmail = directorEmail;}
    public String getStudentEmail() {return studentEmail;}
    public void setStudentEmail(String studentEmail) {this.studentEmail = studentEmail;}
}
