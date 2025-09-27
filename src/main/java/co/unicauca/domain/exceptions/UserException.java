package co.unicauca.domain.exceptions;
import java.util.LinkedList;
public class UserException extends ModelException {
    public UserException()
    {
        setExceptionMessages(new LinkedList<>());
    }
    public UserException(UserExceptionEnum prmField, String prmMessage)
    {
        setExceptionMessages(new LinkedList<>());
        super.addExceptionMessage(prmField, prmMessage);
    }
    public void addExceptionMessage(UserExceptionEnum prmField, String prmMessage)
    {
        super.addExceptionMessage(prmField, prmMessage);
    }
    public void throwException() throws UserException
    {
        if(getMessage() == null) return;
        throw this;
    }
    public static void throwException(UserExceptionEnum prmField, String prmMessage) throws UserException
    {
        throw new UserException(prmField, prmMessage);
    }
    public static void throwException(boolean prmCondition, UserExceptionEnum prmField, String prmMessage) throws UserException
    {
        if(prmCondition) throw new UserException(prmField, prmMessage);
    }
}
