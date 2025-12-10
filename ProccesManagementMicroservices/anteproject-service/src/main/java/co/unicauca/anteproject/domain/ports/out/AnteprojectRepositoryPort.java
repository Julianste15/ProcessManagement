package co.unicauca.anteproject.domain.ports.out;

import co.unicauca.anteproject.domain.model.Anteproject;
import co.unicauca.anteproject.domain.model.AnteprojectStatus;

import java.util.List;
import java.util.Optional;

/**
 * Puerto de salida para persistencia de anteproyectos
 * Define las operaciones de repositorio que necesita el dominio
 */
public interface AnteprojectRepositoryPort {

    Anteproject save(Anteproject anteproject);

    Optional<Anteproject> findById(Long id);

    Optional<Anteproject> findByFormatoAId(Long formatoAId);

    List<Anteproject> findByStudentEmail(String studentEmail);

    List<Anteproject> findByDirectorEmail(String directorEmail);

    List<Anteproject> findByStatusIn(List<AnteprojectStatus> statuses);

    void delete(Anteproject anteproject);

    List<Anteproject> findAll();
}
