package co.unicauca.formata.dto;

import co.unicauca.formata.model.EstadoProyecto;
import co.unicauca.formata.model.Modalidad;
import java.time.LocalDate;
import java.util.List;

public class FormatAResponse {
    private Long id;
    private String titulo;
    private Modalidad modalidad;
    private LocalDate fechaCreacion;
    private String directorEmail;
    private String codirectorEmail;
    private String objetivoGeneral;
    private List<String> objetivosEspecificos;
    private String archivoPDF;
    private EstadoProyecto estado;
    private String cartaAceptacionEmpresa;
    private int intentos;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public LocalDate getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(LocalDate fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
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

    public EstadoProyecto getEstado() {
        return estado;
    }

    public void setEstado(EstadoProyecto estado) {
        this.estado = estado;
    }

    public String getCartaAceptacionEmpresa() {
        return cartaAceptacionEmpresa;
    }

    public void setCartaAceptacionEmpresa(String cartaAceptacionEmpresa) {
        this.cartaAceptacionEmpresa = cartaAceptacionEmpresa;
    }

    public int getIntentos() {
        return intentos;
    }

    public void setIntentos(int intentos) {
        this.intentos = intentos;
    }
}