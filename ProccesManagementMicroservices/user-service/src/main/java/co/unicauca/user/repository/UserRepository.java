package co.unicauca.user.repository;
import co.unicauca.user.model.User;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
@Repository
public interface UserRepository extends JpaRepository<User, Long> {    
    /**
     * Busca un usuario por email
     */
    User findByEmail(String email);    
    /**
     * Busca un usuario por email usando Optional
     */
    Optional<User> findOptionalByEmail(String email);    
    /**
     * Verifica si existe un usuario con el email
     */
    boolean existsByEmail(String email);    
    /**
     * Busca usuarios por rol
     */
    List<User> findByRole(co.unicauca.user.model.enums.Role role);    
    /**
     * Busca usuarios por carrera
     */
    List<User> findByCareer(co.unicauca.user.model.enums.Career career);    
    /**
     * Busca usuarios por rol y carrera
     */
    List<User> findByRoleAndCareer(co.unicauca.user.model.enums.Role role, co.unicauca.user.model.enums.Career career);    
    /**
     * Busca usuarios por nombres (búsqueda parcial)
     */
    @Query("SELECT u FROM User u WHERE LOWER(u.names) LIKE LOWER(CONCAT('%', :name, '%'))")
    List<User> findByNamesContainingIgnoreCase(@Param("name") String name);    
    /**
     * Busca usuarios por apellidos (búsqueda parcial)
     */
    @Query("SELECT u FROM User u WHERE LOWER(u.surnames) LIKE LOWER(CONCAT('%', :surname, '%'))")
    List<User> findBySurnamesContainingIgnoreCase(@Param("surname") String surname);
}