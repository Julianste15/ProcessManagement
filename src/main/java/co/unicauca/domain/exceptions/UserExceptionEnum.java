package co.unicauca.domain.exceptions;
public enum UserExceptionEnum implements iFieldEnum {   
    NAMES("nombres"),
    SURNAMES("apellidos"),
    EMAIL("email"),
    PASSWORD("contraseña"),
    TELEPHONE("telefono");
    private String atrName;
    UserExceptionEnum(String prmName){this.atrName = prmName;}
    @Override
    public String getFieldName(){return this.atrName;}
}
