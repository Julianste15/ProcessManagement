package co.unicauca.domain.enums;

public enum Career {
    SYSTEMS_ENGINEERING("Ingeniería de Sistemas"),
    ELECTRONICS_TELECOMMUNICATIONS("Ingeniería Electrónica y Telecomunicaciones"),
    INDUSTRIAL_AUTOMATION("Automatización Industrial"),
    TELEMATICS_TECHNOLOGY("Tecnología en Telemática");
    
    private final String displayName;
    
    Career(String displayName) {
        this.displayName = displayName;
    }
    
    public String getDisplayName() {
        return displayName;
    }
    
    public static Career fromDisplayName(String displayName) {
        if (displayName == null || displayName.trim().isEmpty()) {
            throw new IllegalArgumentException("Career name cannot be null or empty");
        }
        
        for (Career career : values()) {
            if (career.displayName.equalsIgnoreCase(displayName.trim())) {
                return career;
            }
        }
        
        throw new IllegalArgumentException("Unknown career: " + displayName);
    }
    
    public static Career fromDatabaseValue(String dbValue) {
        // Para cuando se guarda el enum.name() en BD (SYSTEMS_ENGINEERING)
        try {
            return Career.valueOf(dbValue.toUpperCase());
        } catch (IllegalArgumentException e) {
            // Fallback: intentar por display name
            return fromDisplayName(dbValue);
        }
    }
}