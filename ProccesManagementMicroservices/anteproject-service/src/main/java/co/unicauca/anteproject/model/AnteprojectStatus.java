package co.unicauca.anteproject.model;
public enum AnteprojectStatus {
    DRAFT("Borrador"),
    SUBMITTED("Enviado"),
    UNDER_EVALUATION("En evaluación"),
    APPROVED("Aprobado"),
    REJECTED("Rechazado"),
    NEEDS_CORRECTIONS("Requiere correcciones"),
    CORRECTIONS_SUBMITTED("Correcciones enviadas"),
    FINAL_APPROVAL("Aprobación final");    
    private final String displayName;    
    AnteprojectStatus(String displayName) {
        this.displayName = displayName;
    }    
    public String getDisplayName() {
        return displayName;
    }
}