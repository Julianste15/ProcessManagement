package co.unicauca.presentation.observer;

import java.util.Map;

/**
 * Contrato que debe cumplir cualquier modelo
 * 
 * 
 */
public interface iModel {
    
    /**
     * Metodo para validar los campos del modelo
     * 
     * @return 'true' si los campos son correctos. De lo contrario 'false'
     */
    boolean validateFields();
    
    /**
     * Metodo para obtener los campos del modelo, con sus respectivos valores
     * 
     * @return Lista con los campos 
     */
    Map<String, Object> getFields();
}
