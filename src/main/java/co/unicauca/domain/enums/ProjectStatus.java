package co.unicauca.domain.enums;

public enum ProjectStatus {
    DRAFT("Borrador"),
    SUBMITTED("Enviado"),
    UNDER_REVIEW("En Revisión"),
    APPROVED("Aprobado"),
    NEEDS_ADJUSTMENT("Requiere Ajustes"),
    REJECTED("Rechazado"),
    DEFINITELY_REJECTED("Rechazado Definitivamente");
    
    private final String displayName;
    
    ProjectStatus(String displayName) {
        this.displayName = displayName;
    }
    
    public String getDisplayName() {
        return displayName;
    }
}