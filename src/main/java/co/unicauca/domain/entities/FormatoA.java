/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package co.unicauca.domain.entities;

import java.time.LocalDate;
import java.util.List;
/**
 *
 * @author AN515-54-55MX
 */
public class FormatoA {
    private String titulo;
    private Modalidad modalidad;
    private LocalDate fechaCreacion;
    private User director;
    private User codirector;
    private String objetivoGeneral;
    private List<String> objetivosEspecificos;
    private String archivoPDF;
    private EstadoProyecto estado;
    private int intentos;
    
    public FormatoA() {
    }

    public FormatoA(String titulo, Modalidad modalidad, LocalDate fechaCreacion, 
                         User director, User codirector, String objetivoGeneral, 
                         List<String> objetivosEspecificos, String archivoPDF) {
        this.titulo = titulo;
        this.modalidad = modalidad;
        this.fechaCreacion = fechaCreacion;
        this.director = director;
        this.codirector = codirector;
        this.objetivoGeneral = objetivoGeneral;
        this.objetivosEspecificos = objetivosEspecificos;
        this.archivoPDF = archivoPDF;
        this.estado = EstadoProyecto.FORMATO_A_EN_EVALUACION;
        this.intentos = 1;
    }

    // Getters y Setters
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

    public User getDirector() {
        return director;
    }

    public void setDirector(User director) {
        this.director = director;
    }

    public User getCodirector() {
        return codirector;
    }

    public void setCodirector(User codirector) {
        this.codirector = codirector;
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

    public int getIntentos() {
        return intentos;
    }
    
    public void setIntentos(int intentos) {
        this.intentos = intentos;
    }

    public void incrementarIntentos() {
        this.intentos++;
    }

    @Override
    public String toString() {
        return "ProyectoGrado{" +
                "titulo='" + titulo + '\'' +
                ", modalidad=" + modalidad +
                ", fechaCreacion=" + fechaCreacion +
                ", director=" + (director != null ? director.getNames() : "N/A") +
                ", codirector=" + (codirector != null ? codirector.getNames() : "N/A") +
                ", estado=" + estado +
                ", intentos=" + intentos +
                '}';
    }
}
