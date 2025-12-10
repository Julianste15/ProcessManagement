package co.unicauca.user.validation;
import co.unicauca.user.model.User;
import co.unicauca.user.exceptions.UserException;
import co.unicauca.user.exceptions.UserExceptionEnum;
import org.springframework.stereotype.Component;
@Component
public class UserValidation implements iValidator {
    private UserException atrException;
    public UserValidation() {
        atrException = new UserException();
    }
    @Override
    public void validate(User prmModel) throws UserException {
        atrException = new UserException();
        validateNames(prmModel);
        validateSurnames(prmModel);
        validateEmail(prmModel);
        validatePassword(prmModel);
        validateTelephone(prmModel);
        atrException.throwException();
    }
    protected boolean isNull(Object prmField, UserExceptionEnum prmFieldType) {
        if (prmField == null) {
            atrException.addExceptionMessage(prmFieldType, "No debe estar nulo");
            return true;
        }
        return false;
    }
    protected boolean isEmpty(String prmField, UserExceptionEnum prmFieldType) {
        if (prmField.isEmpty()) {
            atrException.addExceptionMessage(prmFieldType, "No debe estar vacío");
            return true;
        }
        return false;
    }
    public void validateNames(User prmUser) {
        if (isNull(prmUser.getNames(), UserExceptionEnum.NAMES)
                || isEmpty(prmUser.getNames(), UserExceptionEnum.NAMES))
            return;
        if (!prmUser.getNames().matches("[a-zA-Z ]*")) {
            atrException.addExceptionMessage(UserExceptionEnum.NAMES, "Debe contener solamente letras");
        }
    }
    public void validateSurnames(User prmUser) {
        if (isNull(prmUser.getSurnames(), UserExceptionEnum.SURNAMES)
                || isEmpty(prmUser.getSurnames(), UserExceptionEnum.SURNAMES))
            return;
        if (!prmUser.getSurnames().matches("[a-zA-Z ]*")) {
            atrException.addExceptionMessage(UserExceptionEnum.SURNAMES, "Debe contener solamente letras");
        }
    }
    public void validateEmail(User prmUser) {
        if (isNull(prmUser.getEmail(), UserExceptionEnum.EMAIL))
            return;
        if (!prmUser.getEmail().contains("@unicauca.edu.co")) {
            atrException.addExceptionMessage(UserExceptionEnum.EMAIL,
                    "No pertenece al dominio de la Universidad del Cauca");
        }
    }
    public void validatePassword(User prmUser) {
        if (isNull(prmUser.getPassword(), UserExceptionEnum.PASSWORD))
            return;
        String password = prmUser.getPassword();
        boolean hasError = false;
        if (password.length() < 6) {
            atrException.addExceptionMessage(UserExceptionEnum.PASSWORD, "Debe contener por lo menos seis caracteres");
            hasError = true;
        }
        if (!password.matches(".*[0-9].*")) {
            atrException.addExceptionMessage(UserExceptionEnum.PASSWORD, "Debe contener por lo menos un dígito");
            hasError = true;
        }
        if (!password.matches(".*[!@#$%^&*(){}+=_ ,./?-].*")) {
            atrException.addExceptionMessage(UserExceptionEnum.PASSWORD,
                    "Debe contener por lo menos un caracter especial");
            hasError = true;
        }
        if (!password.matches(".*[A-Z].*")) {
            atrException.addExceptionMessage(UserExceptionEnum.PASSWORD,
                    "Debe contener por lo menos una letra en mayúscula");
            hasError = true;
        }
    }
    public void validateTelephone(User prmUser) {
        if (prmUser.getTelephone() != null) {
            if (prmUser.getTelephone() <= 0) {
                atrException.addExceptionMessage(UserExceptionEnum.TELEPHONE, "Debe ser un número positivo");
            }
        }
    }
}