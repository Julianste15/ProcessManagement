package co.unicauca.domain.enums;

public enum Role {
    STUDENT("estudiante"),
    TEACHER("profesor"),
    ADMINISTRATOR("administrador");
    
    private final String displayName;
    
    Role(String displayName) {
        this.displayName = displayName;
    }
    
    public String getDisplayName() {
        return displayName;
    }
    
    public static Role fromDisplayName(String displayName) {
        if (displayName == null || displayName.trim().isEmpty()) {
            throw new IllegalArgumentException("Role name cannot be null or empty");
        }
        
        for (Role role : values()) {
            if (role.displayName.equalsIgnoreCase(displayName.trim())) {
                return role;
            }
        }
        
        throw new IllegalArgumentException("Unknown role: " + displayName);
    }
    
    public boolean isStudent() {
        return this == STUDENT;
    }
    
    public boolean isTeacher() {
        return this == TEACHER;
    }
    
    public boolean isAdministrator() {
        return this == ADMINISTRATOR;
    }
}