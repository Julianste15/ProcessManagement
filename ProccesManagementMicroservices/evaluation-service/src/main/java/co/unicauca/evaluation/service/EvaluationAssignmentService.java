package co.unicauca.evaluation.service;
import co.unicauca.evaluation.model.EvaluationAssignment;
import co.unicauca.evaluation.model.AssignmentStatus;
import co.unicauca.evaluation.repository.EvaluationAssignmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;
@Service
public class EvaluationAssignmentService {    
    private static final Logger logger = Logger.getLogger(EvaluationAssignmentService.class.getName());    
    @Autowired
    private EvaluationAssignmentRepository assignmentRepository;       
    @Autowired
    private EvaluationService evaluationService;    
    public EvaluationAssignment assignEvaluators(Long projectId, String evaluator1Email, 
                                               String evaluator2Email, String assignedBy) {
        logger.info("Asignando evaluadores para proyecto: " + projectId);
        Optional<EvaluationAssignment> existingAssignment = assignmentRepository.findByProjectId(projectId);
        if (existingAssignment.isPresent()) {
            throw new RuntimeException("Ya existe una asignación de evaluadores para este proyecto");
        }        
        EvaluationAssignment assignment = new EvaluationAssignment(projectId, evaluator1Email, evaluator2Email, assignedBy);
        EvaluationAssignment savedAssignment = assignmentRepository.save(assignment);
        evaluationService.createEvaluation(projectId, evaluator1Email);
        evaluationService.createEvaluation(projectId, evaluator2Email);        
        logger.info("Evaluadores asignados exitosamente para proyecto: " + projectId);
        return savedAssignment;
    }    
    public List<EvaluationAssignment> getAssignmentsByEvaluator(String evaluatorEmail) {
        return assignmentRepository.findByEvaluator1EmailOrEvaluator2Email(evaluatorEmail, evaluatorEmail);
    }    
    public Optional<EvaluationAssignment> getAssignmentByProject(Long projectId) {
        return assignmentRepository.findByProjectId(projectId);
    }    
    public List<EvaluationAssignment> getAllAssignments() {
        return assignmentRepository.findAll();
    }    
    public EvaluationAssignment updateAssignmentStatus(Long assignmentId, AssignmentStatus status) {
        EvaluationAssignment assignment = assignmentRepository.findById(assignmentId)
            .orElseThrow(() -> new RuntimeException("Asignación no encontrada"));
        assignment.setStatus(status);
        return assignmentRepository.save(assignment);
    }    
    public void checkAndExpireAssignments() {
        List<EvaluationAssignment> activeAssignments = assignmentRepository.findByStatus(AssignmentStatus.ACTIVE);
        LocalDateTime now = LocalDateTime.now();        
        for (EvaluationAssignment assignment : activeAssignments) {
            if (assignment.getDeadline() != null && assignment.getDeadline().isBefore(now)) {
                assignment.setStatus(AssignmentStatus.EXPIRED);
                assignmentRepository.save(assignment);
                logger.info("Asignación expirada: " + assignment.getId());
            }
        }
    }
}