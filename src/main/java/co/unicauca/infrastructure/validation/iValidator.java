package co.unicauca.infrastructure.validation;
import co.unicauca.domain.entities.User;
import co.unicauca.domain.exceptions.ModelException;
public interface iValidator {
    public void validate(User prmModel) throws ModelException;
}
