package co.unicauca.anteproject.repository;

import co.unicauca.anteproject.model.Anteproject;
import co.unicauca.anteproject.model.AnteprojectStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface AnteprojectRepository extends JpaRepository<Anteproject, Long> {
    
    Optional<Anteproject> findByFormatoAId(Long formatoAId);
    
    List<Anteproject> findByStudentEmail(String studentEmail);
    
    List<Anteproject> findByDirectorEmail(String directorEmail);
    
    List<Anteproject> findByStatus(AnteprojectStatus status);
    
    List<Anteproject> findByStudentEmailAndStatus(String studentEmail, AnteprojectStatus status);
    
    List<Anteproject> findByDirectorEmailAndStatus(String directorEmail, AnteprojectStatus status);
}