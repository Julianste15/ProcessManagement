package co.unicauca.domain.enums;

public enum ProjectModality {
    RESEARCH("Investigación"),
    PROFESSIONAL_PRACTICE("Práctica Profesional");
    
    private final String displayName;
    
    ProjectModality(String displayName) {
        this.displayName = displayName;
    }
    
    public String getDisplayName() {
        return displayName;
    }
    
    public static ProjectModality fromDisplayName(String displayName) {
        for (ProjectModality modality : values()) {
            if (modality.displayName.equals(displayName)) {
                return modality;
            }
        }
        throw new IllegalArgumentException("Modalidad no válida: " + displayName);
    }
}