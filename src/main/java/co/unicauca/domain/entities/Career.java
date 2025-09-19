package co.unicauca.domain.entities;
/**
 * Eprogramas permitidos para un usuario
 * 
 */
public enum Career {
    
    SYSTEM_ENGINEERING("Ingenieria de Sistemas"),
    ELECTRONIC_ENGINEERING_AND_TELECOM("Ingenieria Electronica y Telecomunicaciones"),
    INDUSTRIAL_AUTOMATIC("Automatica Industrial"),
    TELEMATICS_TECHNOLOGY("Tecnologia en Telematica");
    
    private String atrName;
    
    Career(String prmName)
    {
        this.atrName = prmName;
    }
    
    /**
     * Metodo para obtener el nombre del programa
     * 
     * @return El nombre
     */
    public String getName()
    {
        return this.atrName;
    }
    
    /**
     * Metodo para obtener el programa a partir de su nombre
     * 
     * @param prmCareer Recibe el nombre
     * 
     * @return El programa si se encuentra. De lo contrario null.
     */
    public static Career getCareer(String prmCareer)
    {
        for(Career objCareer: Career.values())
            if(objCareer.getName().equals(prmCareer))
                return objCareer;
        return null;
    }
}
