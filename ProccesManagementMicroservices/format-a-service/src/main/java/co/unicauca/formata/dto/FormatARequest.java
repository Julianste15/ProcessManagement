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
    /**
     * Ruta o URL ya existente del PDF (compatibilidad hacia atrás)
     */
    private String archivoPDF;
    /**
     * Nombre del archivo PDF adjuntado desde el cliente
     */
    private String archivoPdfNombre;
    /**
     * Contenido del PDF codificado en Base64
     */
    private String archivoPdfContenido;

    private String cartaAceptacionEmpresaContenido;
    private String cartaAceptacionEmpresaNombre;

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public Modalidad getModalidad() {
        return modalidad;
    }

    public void setModalidad(Modalidad modalidad) {
        this.modalidad = modalidad;
    }

    public String getDirectorEmail() {
        return directorEmail;
    }

    public void setDirectorEmail(String directorEmail) {
        this.directorEmail = directorEmail;
    }

    public String getCodirectorEmail() {
        return codirectorEmail;
    }

    public void setCodirectorEmail(String codirectorEmail) {
        this.codirectorEmail = codirectorEmail;
    }

    public String getObjetivoGeneral() {
        return objetivoGeneral;
    }

    public void setObjetivoGeneral(String objetivoGeneral) {
        this.objetivoGeneral = objetivoGeneral;
    }

    public List<String> getObjetivosEspecificos() {
        return objetivosEspecificos;
    }

    public void setObjetivosEspecificos(List<String> objetivosEspecificos) {
        this.objetivosEspecificos = objetivosEspecificos;
    }

    public String getArchivoPDF() {
        return archivoPDF;
    }

    public void setArchivoPDF(String archivoPDF) {
        this.archivoPDF = archivoPDF;
    }

    public String getArchivoPdfNombre() {
        return archivoPdfNombre;
    }

    public void setArchivoPdfNombre(String archivoPdfNombre) {
        this.archivoPdfNombre = archivoPdfNombre;
    }

    public String getArchivoPdfContenido() {
        return archivoPdfContenido;
    }

    public void setArchivoPdfContenido(String archivoPdfContenido) {
        this.archivoPdfContenido = archivoPdfContenido;
    }

    public String getCartaAceptacionEmpresaContenido() {
        return cartaAceptacionEmpresaContenido;
    }

    public void setCartaAceptacionEmpresaContenido(String cartaAceptacionEmpresaContenido) {
        this.cartaAceptacionEmpresaContenido = cartaAceptacionEmpresaContenido;
    }

    public String getCartaAceptacionEmpresaNombre() {
        return cartaAceptacionEmpresaNombre;
    }

    public void setCartaAceptacionEmpresaNombre(String cartaAceptacionEmpresaNombre) {
        this.cartaAceptacionEmpresaNombre = cartaAceptacionEmpresaNombre;
    }
}