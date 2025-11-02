package co.unicauca.domain.services;

import co.unicauca.domain.entities.User;
import java.util.List;

public interface iRepository<ID> {

    boolean save(User prmModel);

    List<User> list(); 

    User getById(ID prmId);
}
