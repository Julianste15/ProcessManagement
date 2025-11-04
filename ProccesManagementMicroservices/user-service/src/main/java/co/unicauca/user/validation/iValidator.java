package co.unicauca.user.validation;
import co.unicauca.user.model.User;
import co.unicauca.user.exceptions.ModelException;
public interface iValidator {
    public void validate(User prmModel) throws ModelException;
}
