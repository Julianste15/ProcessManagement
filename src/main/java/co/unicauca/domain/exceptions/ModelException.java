package co.unicauca.domain.exceptions;
import java.util.List;
import java.util.LinkedList;
public abstract class ModelException extends Exception {
    List<String> atrExceptionMessages;
    public ModelException() {
        this.atrExceptionMessages = new LinkedList<>();
    }
    protected void setExceptionMessages(List<String> prmList) {
        if (prmList != null) {
            atrExceptionMessages = prmList;
        } else {
            atrExceptionMessages = new LinkedList<>();
        }
    }
    @Override
    public String getMessage() {
        if((atrExceptionMessages == null) || atrExceptionMessages.isEmpty()) {
            return null;
        }
        StringBuilder varMessage = new StringBuilder();
        for(String exceptionMessage : atrExceptionMessages) {
            varMessage.append(exceptionMessage).append("\n");
        }
        return varMessage.toString().trim(); // Eliminar el último \n
    }
    public void addExceptionMessage(iFieldEnum prmField, String prmMessage) {
        if(atrExceptionMessages == null) {
            atrExceptionMessages = new LinkedList<>();
        }
        atrExceptionMessages.add("El campo " + prmField.getFieldName() + ": " + prmMessage);
    }
    // Método para limpiar mensajes previos
    public void clearMessages() {
        if (atrExceptionMessages != null) {
            atrExceptionMessages.clear();
        }
    }
}