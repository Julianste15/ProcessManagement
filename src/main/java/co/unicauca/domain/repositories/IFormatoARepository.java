package co.unicauca.domain.repositories;
import co.unicauca.domain.entities.FormatoA;
import java.util.Optional;
public interface IFormatoARepository {
    FormatoA save(FormatoA proyecto);
    Optional<FormatoA> findById(String titulo);
    void update(FormatoA proyecto);
}
