package co.unicauca.formata.dto;
import co.unicauca.formata.model.Modalidad;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.List;
public class FormatARequest {    
    @NotBlank(message = "El título es obligatorio")
    private String titulo;    
    @NotNull(message = "La modalidad es obligatoria")
    private Modalidad modalidad;    
    @NotBlank(message = "El email del director es obligatorio")
    private String directorEmail;    
    private String codirectorEmail;    
    @NotBlank(message = "El objetivo general es obligatorio")
    private String objetivoGeneral;    
    private List<String> objetivosEspecificos;    
    private String archivoPDF;    
    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }    
    public Modalidad getModalidad() { return modalidad; }
    public void setModalidad(Modalidad modalidad) { this.modalidad = modalidad; }    
    public String getDirectorEmail() { return directorEmail; }
    public void setDirectorEmail(String directorEmail) { this.directorEmail = directorEmail; }    
    public String getCodirectorEmail() { return codirectorEmail; }
    public void setCodirectorEmail(String codirectorEmail) { this.codirectorEmail = codirectorEmail; }    
    public String getObjetivoGeneral() { return objetivoGeneral; }
    public void setObjetivoGeneral(String objetivoGeneral) { this.objetivoGeneral = objetivoGeneral; }    
    public List<String> getObjetivosEspecificos() { return objetivosEspecificos; }
    public void setObjetivosEspecificos(List<String> objetivosEspecificos) { this.objetivosEspecificos = objetivosEspecificos; }    
    public String getArchivoPDF() { return archivoPDF; }
    public void setArchivoPDF(String archivoPDF) { this.archivoPDF = archivoPDF; }
}