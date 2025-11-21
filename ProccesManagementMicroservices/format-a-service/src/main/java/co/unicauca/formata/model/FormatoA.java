package co.unicauca.formata.model;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "formatos_a")
public class FormatoA {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String titulo;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Modalidad modalidad;
    @Column(name = "fecha_creacion", nullable = false)
    private LocalDate fechaCreacion;
    @Column(name = "director_email", nullable = false)
    private String directorEmail;
    @Column(name = "codirector_email")
    private String codirectorEmail;
    @Column(name = "objetivo_general", length = 1000)
    private String objetivoGeneral;
    @ElementCollection
    @CollectionTable(name = "formato_a_objetivos", joinColumns = @JoinColumn(name = "formato_a_id"))
    @Column(name = "objetivo")
    private List<String> objetivosEspecificos = new ArrayList<>();
    @Column(name = "archivo_pdf", length = 500)
    private String archivoPDF;
    @Column(name = "carta_aceptacion_empresa", length = 500)
    private String cartaAceptacionEmpresa;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoProyecto estado;
    @Column(nullable = false)
    private int intentos = 1;

    public FormatoA() {
    }

    public FormatoA(String titulo, Modalidad modalidad, LocalDate fechaCreacion,
            String directorEmail, String codirectorEmail, String objetivoGeneral,
            List<String> objetivosEspecificos, String archivoPDF, String cartaAceptacionEmpresa) {
        this.titulo = titulo;
        this.modalidad = modalidad;
        this.fechaCreacion = fechaCreacion;
        this.directorEmail = directorEmail;
        this.codirectorEmail = codirectorEmail;
        this.objetivoGeneral = objetivoGeneral;
        this.objetivosEspecificos = objetivosEspecificos;
        this.archivoPDF = archivoPDF;
        this.cartaAceptacionEmpresa = cartaAceptacionEmpresa;
        this.estado = EstadoProyecto.FORMATO_A_EN_EVALUACION;
        this.intentos = 1;
    }

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

    public String getCartaAceptacionEmpresa() {
        return cartaAceptacionEmpresa;
    }

    public void setCartaAceptacionEmpresa(String cartaAceptacionEmpresa) {
        this.cartaAceptacionEmpresa = cartaAceptacionEmpresa;
    }

    public EstadoProyecto getEstado() {
        return estado;
    }

    public void setEstado(EstadoProyecto estado) {
        this.estado = estado;
    }

    public int getIntentos() {
        return intentos;
    }

    public void setIntentos(int intentos) {
        this.intentos = intentos;
    }

    public void incrementarIntentos() {
        this.intentos++;
    }
}