package co.unicauca.domain.repositories;

import co.unicauca.domain.entities.User;
import java.util.List;
import java.util.Optional;

public interface IUserRepositoriy {
    void save (User user);
    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);
    List<User> findAll();
}
