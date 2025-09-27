package co.unicauca.domain.exceptions;
import java.util.List;
public abstract class ModelException extends Exception {
    private List<String> atrExceptionMessages;
    protected void setExceptionMessages(List<String> prmList){atrExceptionMessages = prmList;}
    @Override
    public String getMessage() 
    {
        if((atrExceptionMessages == null) || atrExceptionMessages.isEmpty()) return null;
        String varMessage = "";
        for(String atrExceptionMessage: atrExceptionMessages)
            varMessage += atrExceptionMessage + "\n";
        return varMessage;
    }
    public void addExceptionMessage(iFieldEnum prmField, String prmMessage)
    {
        if(atrExceptionMessages == null) return;
        atrExceptionMessages.add("El campo " + prmField.getFieldName() + ": " + prmMessage);
    }
}
