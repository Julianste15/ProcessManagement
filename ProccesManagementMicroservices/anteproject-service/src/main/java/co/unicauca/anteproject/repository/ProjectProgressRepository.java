package co.unicauca.anteproject.repository;
import co.unicauca.anteproject.model.ProjectProgress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
@Repository
public interface ProjectProgressRepository extends JpaRepository<ProjectProgress, Long> {    
    List<ProjectProgress> findByAnteprojectIdOrderByCreatedAtDesc(Long anteprojectId);    
    List<ProjectProgress> findByCreatedBy(String createdBy);
}