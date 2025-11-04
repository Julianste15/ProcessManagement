package co.unicauca.user.exceptions;
import java.util.LinkedList;
public class UserException extends ModelException {
    public UserException()
    {
        super();
        setExceptionMessages(new LinkedList<>());
    }
    public UserException(UserExceptionEnum prmField, String prmMessage)
    {
        super(); // Inicializar primero
        setExceptionMessages(new LinkedList<>());
        super.addExceptionMessage(prmField, prmMessage);
    }
    public void addExceptionMessage(UserExceptionEnum prmField, String prmMessage)
    {
        super.addExceptionMessage(prmField, prmMessage);
    }
    public void throwException() throws UserException
    {
        String message = getMessage();
        if(message == null || message.trim().isEmpty()) {
            return;
        }
        
        // Crear una NUEVA excepcion con solo los mensajes actuales
        UserException newException = new UserException();
        // Copiar los mensajes actuales
        if (this.atrExceptionMessages != null) {
            newException.setExceptionMessages(new LinkedList<>(this.atrExceptionMessages));
        }
        
        throw newException; // Lanzar la nueva excepcion limpia
    }
    public static void throwException(UserExceptionEnum prmField, String prmMessage) throws UserException
    {
        UserException exception = new UserException();
        exception.addExceptionMessage(prmField, prmMessage);
        throw exception;
    }
    public static void throwException(boolean prmCondition, UserExceptionEnum prmField, String prmMessage) throws UserException
    {
        if(prmCondition) {
            throwException(prmField, prmMessage);
        }
    }
}
