package co.unicauca.infrastructure.validation;
import co.unicauca.domain.entities.User;
import co.unicauca.domain.exceptions.UserException;
import co.unicauca.domain.exceptions.UserExceptionEnum;
import co.unicauca.infrastructure.dependency_injection.Service;

@Service
public class UserValidation implements iValidator {   
    private UserException atrException;
    public UserValidation()
    {
        atrException = new UserException();
    }
    @Override
    public void validate(User prmModel) throws UserException {
        System.out.println("=== INICIO VALIDACIÓN ===");
        System.out.println("Creando nueva excepción...");
        atrException = new UserException();

        System.out.println("Mensajes iniciales: " + (atrException.getMessage() == null ? "null" : atrException.getMessage()));

        validateNames(prmModel);
        System.out.println("Después de validar nombres: " + (atrException.getMessage() == null ? "null" : atrException.getMessage()));

        validateSurnames(prmModel);
        System.out.println("Después de validar apellidos: " + (atrException.getMessage() == null ? "null" : atrException.getMessage()));

        validateEmail(prmModel);
        System.out.println("Después de validar email: " + (atrException.getMessage() == null ? "null" : atrException.getMessage()));

        validatePassword(prmModel);
        System.out.println("Después de validar contraseña: " + (atrException.getMessage() == null ? "null" : atrException.getMessage()));

        validateTelephone(prmModel);
        System.out.println("Después de validar teléfono: " + (atrException.getMessage() == null ? "null" : atrException.getMessage()));

        System.out.println("Mensajes finales: " + (atrException.getMessage() == null ? "null" : atrException.getMessage()));

        atrException.throwException();
    }
    protected boolean isNull(Object prmField, UserExceptionEnum prmFieldType)
    {
        if(prmField == null)
        {
            atrException.addExceptionMessage(prmFieldType, "No debe estar nulo");
            return true;
        }
        return false;
    }
    protected boolean isEmpty(String prmField, UserExceptionEnum prmFieldType)
    {
        if(prmField.isEmpty())
        {
            atrException.addExceptionMessage(prmFieldType, "No debe estar vacio");
            return true;
        }
        return false;
    }
    public void validateNames(User prmUser)
    {
        if(isNull(prmUser.getNames(), UserExceptionEnum.NAMES)
        || isEmpty(prmUser.getNames(), UserExceptionEnum.NAMES)) return;
        if(!prmUser.getNames().matches("[a-zA-Z ]*"))
        {
            atrException.addExceptionMessage(UserExceptionEnum.NAMES, "Debe contener solamente letras");
        }
    }
    public void validateSurnames(User prmUser)
    {
        if(isNull(prmUser.getSurnames(), UserExceptionEnum.SURNAMES)
        || isEmpty(prmUser.getSurnames(), UserExceptionEnum.SURNAMES)) return;
        
        if(!prmUser.getSurnames().matches("[a-zA-Z ]*"))
        {
            atrException.addExceptionMessage(UserExceptionEnum.SURNAMES, "Debe contener solamente letras");
        }
    }
    public void validateEmail(User prmUser)
    {
        if(isNull(prmUser.getEmail(), UserExceptionEnum.EMAIL)) return;
        if(!prmUser.getEmail().contains("@unicauca.edu.co"))
        {
            atrException.addExceptionMessage(UserExceptionEnum.EMAIL, "No pertenece al dominio de la Universidad del Cauca"
            );
        }
    }
    public void validatePassword(User prmUser) {
        if(isNull(prmUser.getPassword(), UserExceptionEnum.PASSWORD)) return;

        String password = prmUser.getPassword();

        boolean hasError = false;

        if(password.length() < 6) {
            atrException.addExceptionMessage(UserExceptionEnum.PASSWORD, "Debe contener por lo menos seis caracteres");
            hasError = true;
        }
        if(!password.matches(".*[0-9].*")) {
            atrException.addExceptionMessage(UserExceptionEnum.PASSWORD, "Debe contener por lo menos un digito");
            hasError = true;
        }
        if(!password.matches(".*[!@#$%^&*(){}+=-_,./?].*")) {
            atrException.addExceptionMessage(UserExceptionEnum.PASSWORD, "Debe contener por lo menos un caracter especial");
            hasError = true;
        }
        if(!password.matches(".*[A-Z].*")) {
            atrException.addExceptionMessage(UserExceptionEnum.PASSWORD, "Debe contener por lo menos una letra en mayuscula");
            hasError = true;
        }
    }
    public void validateTelephone(User prmUser)
    {
        if(isNull(prmUser.getTelephone(), UserExceptionEnum.TELEPHONE)) return;
        if(prmUser.getTelephone() <= 0)
        {
            atrException.addExceptionMessage( UserExceptionEnum.TELEPHONE,"Debe ser positivo");
        }
    }
}
