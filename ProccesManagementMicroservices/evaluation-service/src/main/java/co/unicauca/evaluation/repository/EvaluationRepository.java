package co.unicauca.evaluation.repository;
import co.unicauca.evaluation.model.Evaluation;
import co.unicauca.evaluation.model.EvaluationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;
@Repository
public interface EvaluationRepository extends JpaRepository<Evaluation, Long> {    
    List<Evaluation> findByProjectId(Long projectId);    
    List<Evaluation> findByEvaluatorEmail(String evaluatorEmail);    
    List<Evaluation> findByStatus(EvaluationStatus status);    
    Optional<Evaluation> findByProjectIdAndEvaluatorEmail(Long projectId, String evaluatorEmail);    
    List<Evaluation> findByProjectIdAndStatus(Long projectId, EvaluationStatus status);    
    long countByProjectIdAndStatus(Long projectId, EvaluationStatus status);
}