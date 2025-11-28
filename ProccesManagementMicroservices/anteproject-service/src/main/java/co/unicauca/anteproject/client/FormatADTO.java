package co.unicauca.anteproject.client;

public class FormatADTO {
    private Long id;
    private String titulo;
    private String studentEmail;
    private String directorEmail;
    private String codirectorEmail;
    private String estado;
    
    public FormatADTO() {}
    
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }
    
    public String getStudentEmail() { return studentEmail; }
    public void setStudentEmail(String studentEmail) { this.studentEmail = studentEmail; }
    
    public String getDirectorEmail() { return directorEmail; }
    public void setDirectorEmail(String directorEmail) { this.directorEmail = directorEmail; }
    
    public String getCodirectorEmail() { return codirectorEmail; }
    public void setCodirectorEmail(String codirectorEmail) { this.codirectorEmail = codirectorEmail; }
    
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
}
