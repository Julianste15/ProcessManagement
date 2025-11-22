package co.unicauca.domain.enums;
public enum Modalidad {
    INVESTIGACION("Investigación"),
    PRACTICA_PROFESIONAL("Práctica profesional");
    private final String displayName;
    Modalidad(String displayName) {
        this.displayName = displayName;
    }
    public String getDisplayName() {
        return displayName;
    }
    public static Modalidad fromDisplayName(String displayName) {
        if (displayName == null || displayName.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre de la modalidad no puede estar vacío");
        }
        for (Modalidad modalidad : values()) {
            if (modalidad.displayName.equalsIgnoreCase(displayName.trim())) {
                return modalidad;
            }
        }
        throw new IllegalArgumentException("Modalidad desconocida: " + displayName);
    }
    public static Modalidad fromValue(String value) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException("El valor de la modalidad no puede estar vacío");
        }
        try {
            return Modalidad.valueOf(value.trim().toUpperCase());
        } catch (IllegalArgumentException ex) {
            return fromDisplayName(value);
        }
    }
}

