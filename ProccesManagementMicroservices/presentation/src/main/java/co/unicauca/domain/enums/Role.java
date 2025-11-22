package co.unicauca.domain.enums;
public enum Role {
    STUDENT("estudiante"),
    TEACHER("profesor"),
    ADMINISTRATOR("administrador"),
    COORDINATOR("coordinador");
    private final String displayName;
    Role(String displayName) {
        this.displayName = displayName;
    }
    public String getDisplayName() {
        return displayName;
    }
    public static Role fromDisplayName(String displayName) {
        if (displayName == null || displayName.trim().isEmpty()) {
            throw new IllegalArgumentException("Role display name cannot be null or empty");
        }
        for (Role role : values()) {
            if (role.displayName.equalsIgnoreCase(displayName.trim())) {
                return role;
            }
        }
        throw new IllegalArgumentException("Unknown role: " + displayName);
    }
    public static Role fromValue(String value) {
        if (value == null)
            return null;
        value = value.toUpperCase();
        if ("ADMIN".equals(value) || "ADMINISTRATOR".equals(value)) {
            return ADMINISTRATOR;
        }
        if ("TEACHER".equals(value) || "PROFESSOR".equals(value) || "PROFESOR".equals(value)) {
            return TEACHER;
        }
        if ("STUDENT".equals(value) || "ESTUDIANTE".equals(value)) {
            return STUDENT;
        }
        if ("COORDINATOR".equals(value) || "COORDINADOR".equals(value)) {
            return COORDINATOR;
        }
        try {
            return Role.valueOf(value);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Rol desconocido: " + value);
        }
    }
}
