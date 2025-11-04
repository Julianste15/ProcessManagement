package co.unicauca.evaluation.model;
public enum EvaluationStatus {
    PENDING("Pendiente"),
    IN_PROGRESS("En progreso"),
    APPROVED("Aprobado"),
    REJECTED("Rechazado"),
    NEEDS_CORRECTIONS("Requiere correcciones"),    
    COMPLETED("Completado");
    private final String displayName;    
    EvaluationStatus(String displayName) {
        this.displayName = displayName;
    }    
    public String getDisplayName() {
        return displayName;
    }
}