package co.unicauca.domain.repositories;
import co.unicauca.domain.entities.User;
import java.util.List;
public interface UserRepository{ 
    User save(User user);
    User findByEmail(String email);
    User findById(Long id);
    boolean delete(Long id);
    List<User> findAll();
}
