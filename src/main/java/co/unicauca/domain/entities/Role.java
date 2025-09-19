package co.unicauca.domain.entities;

/**
 * Enumerador de los roles para un usuario
 */
public enum Role {
    
    ESTUDENT("estudiante"),
    TEACHER("profesor"),
    ADMINISTRATOR("administrador");
    
    private String atrName;
    
    Role(String prmName)
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
     * Metodo para obtener el rol a partir de su nombre
     * 
     * @param prmRole Recibe el nombre
     * 
     * @return El rol si se encuentra. De lo contrario null.
     */
    public static Role getRole(String prmRole)
    {
        for(Role objRole: Role.values())
            if(objRole.getName().equals(prmRole))
                return objRole;
        return null;
    }
}
