package co.unicauca.formata.model;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import co.unicauca.formata.domain.state.FormatoAStateContext;

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

    @Column(name = "student_email")
    private String studentEmail;

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
    private int intentos;

    @Column(name = "observaciones", length = 2000)
    private String observaciones;

    // Patrón State - No se persiste en la base de datos
    @Transient
    private FormatoAStateContext stateContext;

    public FormatoA() {
    }

    public FormatoA(String titulo, Modalidad modalidad, LocalDate fechaCreacion,
            String directorEmail, String codirectorEmail, String studentEmail, String objetivoGeneral,
            List<String> objetivosEspecificos, String archivoPDF, String cartaAceptacionEmpresa) {
        this.titulo = titulo;
        this.modalidad = modalidad;
        this.fechaCreacion = fechaCreacion;
        this.directorEmail = directorEmail;
        this.codirectorEmail = codirectorEmail;
        this.studentEmail = studentEmail;
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

    public String getStudentEmail() {
        return studentEmail;
    }

    public void setStudentEmail(String studentEmail) {
        this.studentEmail = studentEmail;
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

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    /**
     * Obtiene el contexto del patrón State.
     * Si no existe, lo inicializa basado en el estado actual.
     */
    public FormatoAStateContext getStateContext() {
        if (stateContext == null) {
            stateContext = new FormatoAStateContext(this);
        }
        return stateContext;
    }

    /**
     * Inicializa el contexto del patrón State.
     * Debe llamarse después de cargar la entidad de la base de datos.
     */
    public void initializeStateContext() {
        this.stateContext = new FormatoAStateContext(this);
    }

    // ==========================================
    // BUILDER PATTERN IMPLEMENTATION
    // ==========================================

    public static FormatoABuilder builder() {
        return new FormatoABuilder();
    }

    public static class FormatoABuilder {
        private String titulo;
        private Modalidad modalidad;
        private LocalDate fechaCreacion;
        private String directorEmail;
        private String codirectorEmail;
        private String studentEmail;
        private String objetivoGeneral;
        private List<String> objetivosEspecificos = new ArrayList<>();
        private String archivoPDF;
        private String cartaAceptacionEmpresa;
        private EstadoProyecto estado = EstadoProyecto.FORMATO_A_EN_EVALUACION; // Valor por defecto
        private int intentos = 1; // Valor por defecto
        private String observaciones;

        public FormatoABuilder titulo(String titulo) {
            this.titulo = titulo;
            return this;
        }

        public FormatoABuilder modalidad(Modalidad modalidad) {
            this.modalidad = modalidad;
            return this;
        }

        public FormatoABuilder fechaCreacion(LocalDate fechaCreacion) {
            this.fechaCreacion = fechaCreacion;
            return this;
        }

        public FormatoABuilder directorEmail(String directorEmail) {
            this.directorEmail = directorEmail;
            return this;
        }

        public FormatoABuilder codirectorEmail(String codirectorEmail) {
            this.codirectorEmail = codirectorEmail;
            return this;
        }

        public FormatoABuilder studentEmail(String studentEmail) {
            this.studentEmail = studentEmail;
            return this;
        }

        public FormatoABuilder objetivoGeneral(String objetivoGeneral) {
            this.objetivoGeneral = objetivoGeneral;
            return this;
        }

        public FormatoABuilder objetivosEspecificos(List<String> objetivosEspecificos) {
            this.objetivosEspecificos = objetivosEspecificos;
            return this;
        }

        public FormatoABuilder archivoPDF(String archivoPDF) {
            this.archivoPDF = archivoPDF;
            return this;
        }

        public FormatoABuilder cartaAceptacionEmpresa(String cartaAceptacionEmpresa) {
            this.cartaAceptacionEmpresa = cartaAceptacionEmpresa;
            return this;
        }

        public FormatoABuilder estado(EstadoProyecto estado) {
            this.estado = estado;
            return this;
        }

        public FormatoABuilder intentos(int intentos) {
            this.intentos = intentos;
            return this;
        }

        public FormatoABuilder observaciones(String observaciones) {
            this.observaciones = observaciones;
            return this;
        }

        public FormatoA build() {
            FormatoA formatoA = new FormatoA();
            formatoA.titulo = this.titulo;
            formatoA.modalidad = this.modalidad;
            formatoA.fechaCreacion = this.fechaCreacion != null ? this.fechaCreacion : LocalDate.now();
            formatoA.directorEmail = this.directorEmail;
            formatoA.codirectorEmail = this.codirectorEmail;
            formatoA.studentEmail = this.studentEmail;
            formatoA.objetivoGeneral = this.objetivoGeneral;
            formatoA.objetivosEspecificos = this.objetivosEspecificos;
            formatoA.archivoPDF = this.archivoPDF;
            formatoA.cartaAceptacionEmpresa = this.cartaAceptacionEmpresa;
            formatoA.estado = this.estado;
            formatoA.intentos = this.intentos;
            formatoA.observaciones = this.observaciones;
            formatoA.initializeStateContext(); // Importante: Inicializar el contexto
            return formatoA;
        }
    }
}
