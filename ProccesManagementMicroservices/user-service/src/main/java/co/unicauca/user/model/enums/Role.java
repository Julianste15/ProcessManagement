package co.unicauca.user.model.enums;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
public enum Role {
    STUDENT("estudiante"),
    TEACHER("profesor"), 
    ADMINISTRATOR("administrador");    
    private final String displayName;    
    Role(String displayName) {
        this.displayName = displayName;
    }    
    @JsonValue
    public String getDisplayName() {
        return displayName;
    }    
    @JsonCreator
    public static Role fromValue(String value) {
        if (value == null) return null;        
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
        try {
            return Role.valueOf(value);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Rol desconocido: " + value);
        }
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