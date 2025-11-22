package co.unicauca.domain.entities;
import java.time.LocalDate;
/**
 * DTO para representar un Formato A en la capa de presentación
 */
public class FormatoADTO {
    private Long id;
    private String titulo;
    private String modalidad;
    private LocalDate fechaCreacion;
    private String directorEmail;
    private String codirectorEmail;
    private String studentEmail;
    private String estado;
    private int intentos;
    private String observaciones;
    public FormatoADTO() {}
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }
    public String getModalidad() { return modalidad; }
    public void setModalidad(String modalidad) { this.modalidad = modalidad; }
    public LocalDate getFechaCreacion() { return fechaCreacion; }
    public void setFechaCreacion(LocalDate fechaCreacion) { this.fechaCreacion = fechaCreacion; }
    public String getDirectorEmail() { return directorEmail; }
    public void setDirectorEmail(String directorEmail) { this.directorEmail = directorEmail; }
    public String getCodirectorEmail() { return codirectorEmail; }
    public void setCodirectorEmail(String codirectorEmail) { this.codirectorEmail = codirectorEmail; }
    public String getStudentEmail() { return studentEmail; }
    public void setStudentEmail(String studentEmail) { this.studentEmail = studentEmail; }
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
    public int getIntentos() { return intentos; }
    public void setIntentos(int intentos) { this.intentos = intentos; }
    public String getObservaciones() { return observaciones; }
    public void setObservaciones(String observaciones) { this.observaciones = observaciones; }
}
