package co.unicauca.user.model.enums;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
public enum Career {
    SYSTEMS_ENGINEERING("Ingeniería de Sistemas"),
    ELECTRONICS_TELECOMMUNICATIONS("Ingeniería Electronica y Telecomunicaciones"),
    INDUSTRIAL_AUTOMATION("Automatizacion Industrial"),
    TELEMATICS_TECHNOLOGY("Tecnología en Telematica");    
    private final String displayName;    
    Career(String displayName) {
        this.displayName = displayName;
    }
    @JsonValue
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
    @JsonCreator
    public static Career fromValue(String value) {
        if (value == null) return null;
        try {
            return Career.valueOf(value.toUpperCase());
        } catch (IllegalArgumentException e) {
            for (Career career : values()) {
                if (career.displayName.equalsIgnoreCase(value)) {
                    return career;
                }
            }
            throw new IllegalArgumentException("Carrera desconocida: " + value);
        }
    }
    public static Career fromDatabaseValue(String dbValue) {
        try {
            return Career.valueOf(dbValue.toUpperCase());
        } catch (IllegalArgumentException e) {
            return fromDisplayName(dbValue);
        }
    }
}