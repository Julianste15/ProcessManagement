package co.unicauca.anteproject.dto;
import jakarta.validation.constraints.NotNull;
public class CreateAnteprojectRequest {
    @NotNull(message = "El ID del Formato A es requerido")
    private Long formatoAId;    
    @NotNull(message = "El título es requerido")
    private String titulo;    
    @NotNull(message = "El email del estudiante es requerido")
    private String studentEmail;    
    @NotNull(message = "El email del director es requerido")
    private String directorEmail;    
    public Long getFormatoAId() { return formatoAId; }
    public void setFormatoAId(Long formatoAId) { this.formatoAId = formatoAId; }    
    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }    
    public String getStudentEmail() { return studentEmail; }
    public void setStudentEmail(String studentEmail) { this.studentEmail = studentEmail; }    
    public String getDirectorEmail() { return directorEmail; }
    public void setDirectorEmail(String directorEmail) { this.directorEmail = directorEmail; }
}