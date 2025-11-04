package co.unicauca.formata.dto;
import co.unicauca.formata.model.EstadoProyecto;
import jakarta.validation.constraints.NotNull;
public class EvaluationRequest {    
    @NotNull(message = "El estado de evaluación es obligatorio")
    private EstadoProyecto estado;    
    private String observaciones;    
    public EstadoProyecto getEstado() { return estado; }
    public void setEstado(EstadoProyecto estado) { this.estado = estado; }    
    public String getObservaciones() { return observaciones; }
    public void setObservaciones(String observaciones) { this.observaciones = observaciones; }
}