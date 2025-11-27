package co.unicauca.anteproject.infrastructure.output.persistence.repository;

import co.unicauca.anteproject.infrastructure.output.persistence.entity.AnteprojectEntity;
import co.unicauca.anteproject.domain.model.AnteprojectStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface JpaAnteprojectRepository extends JpaRepository<AnteprojectEntity, Long> {
    Optional<AnteprojectEntity> findByFormatoAId(Long formatoAId);
    List<AnteprojectEntity> findByStudentEmail(String studentEmail);
    List<AnteprojectEntity> findByDirectorEmail(String directorEmail);
    List<AnteprojectEntity> findByStatusIn(List<AnteprojectStatus> statuses);
}
