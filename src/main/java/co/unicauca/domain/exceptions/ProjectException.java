package co.unicauca.domain.exceptions;

public class ProjectException extends Exception {
    public ProjectException(String message) {
        super(message);
    }
    
    public ProjectException(String message, Throwable cause) {
        super(message, cause);
    }
}