package co.unicauca.evaluation.model;
public enum AssignmentStatus {
    ACTIVE("Activa"),
    COMPLETED("Completada"),
    CANCELLED("Cancelada"),
    EXPIRED("Expirada");    
    private final String displayName;    
    AssignmentStatus(String displayName) {
        this.displayName = displayName;
    }    
    public String getDisplayName() {
        return displayName;
    }
}