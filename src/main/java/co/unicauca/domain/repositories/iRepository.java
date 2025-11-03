package co.unicauca.domain.repositories;
import java.util.List;
public interface iRepository<ID> {
    boolean save(Object prmModel);
    List<Object> list();
    Object getById(ID prmId);
}
