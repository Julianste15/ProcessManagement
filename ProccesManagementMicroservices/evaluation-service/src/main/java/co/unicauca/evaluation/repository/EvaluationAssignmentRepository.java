package co.unicauca.evaluation.repository;
import co.unicauca.evaluation.model.EvaluationAssignment;
import co.unicauca.evaluation.model.AssignmentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;
@Repository
public interface EvaluationAssignmentRepository extends JpaRepository<EvaluationAssignment, Long> {    
    Optional<EvaluationAssignment> findByProjectId(Long projectId);    
    List<EvaluationAssignment> findByEvaluator1EmailOrEvaluator2Email(String email1, String email2);    
    List<EvaluationAssignment> findByStatus(AssignmentStatus status);    
    List<EvaluationAssignment> findByAssignedBy(String assignedBy);
}