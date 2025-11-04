package co.unicauca.evaluation.service;

import co.unicauca.evaluation.model.Evaluation;
import co.unicauca.evaluation.model.EvaluationStatus;
import co.unicauca.evaluation.repository.EvaluationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

@Service
public class EvaluationService {
    
    private static final Logger logger = Logger.getLogger(EvaluationService.class.getName());
    
    @Autowired
    private EvaluationRepository evaluationRepository;
    
    public Evaluation createEvaluation(Long projectId, String evaluatorEmail) {
        logger.info("Creando evaluación para proyecto: " + projectId + ", evaluador: " + evaluatorEmail);
        
        // Verificar si ya existe una evaluación para este proyecto y evaluador
        Optional<Evaluation> existingEvaluation = evaluationRepository
            .findByProjectIdAndEvaluatorEmail(projectId, evaluatorEmail);
        
        if (existingEvaluation.isPresent()) {
            throw new RuntimeException("Ya existe una evaluación para este proyecto y evaluador");
        }
        
        Evaluation evaluation = new Evaluation(projectId, evaluatorEmail);
        return evaluationRepository.save(evaluation);
    }
    
    public Evaluation submitEvaluation(Long evaluationId, Double score, String comments, String recommendations) {
        logger.info("Enviando evaluación: " + evaluationId);        
        Evaluation evaluation = evaluationRepository.findById(evaluationId)
            .orElseThrow(() -> new RuntimeException("Evaluación no encontrada"));
        // Validar que la evaluación esté en estado pendiente o en progreso
        if (evaluation.getStatus() != EvaluationStatus.PENDING && 
            evaluation.getStatus() != EvaluationStatus.IN_PROGRESS) {
            throw new RuntimeException("La evaluación no puede ser modificada en su estado actual");
        }
        evaluation.setScore(score);
        evaluation.setComments(comments);
        evaluation.setRecommendations(recommendations);
        evaluation.setStatus(EvaluationStatus.COMPLETED);
        evaluation.setEvaluationDate(LocalDateTime.now());
        // Determinar el estado basado en la calificación
        if (score >= 3.5) {  // Aprobado si la calificación es 3.5 o superior
            evaluation.setStatus(EvaluationStatus.APPROVED);
        } else if (score >= 2.5) {  // Requiere correcciones si está entre 2.5 y 3.4
            evaluation.setStatus(EvaluationStatus.NEEDS_CORRECTIONS);
        } else {  // Rechazado si es menor a 2.5
            evaluation.setStatus(EvaluationStatus.REJECTED);
        }
        
        return evaluationRepository.save(evaluation);
    }
    /**
    * Marca una evaluación como completada (sin calificación)
    */
    public Evaluation completeEvaluation(Long evaluationId) {
        logger.info("Marcando evaluación como completada: " + evaluationId);

        Evaluation evaluation = evaluationRepository.findById(evaluationId)
            .orElseThrow(() -> new RuntimeException("Evaluación no encontrada"));

        evaluation.setStatus(EvaluationStatus.COMPLETED);
        evaluation.setEvaluationDate(LocalDateTime.now());

        return evaluationRepository.save(evaluation);
    }
    public Evaluation updateEvaluationStatus(Long evaluationId, EvaluationStatus status) {
        Evaluation evaluation = evaluationRepository.findById(evaluationId)
            .orElseThrow(() -> new RuntimeException("Evaluación no encontrada"));
        
        evaluation.setStatus(status);
        return evaluationRepository.save(evaluation);
    }
    
    public List<Evaluation> getEvaluationsByProject(Long projectId) {
        return evaluationRepository.findByProjectId(projectId);
    }
    
    public List<Evaluation> getEvaluationsByEvaluator(String evaluatorEmail) {
        return evaluationRepository.findByEvaluatorEmail(evaluatorEmail);
    }
    
    public Optional<Evaluation> getEvaluation(Long evaluationId) {
        return evaluationRepository.findById(evaluationId);
    }
    
    public boolean hasMinimumApprovals(Long projectId, int minApprovals) {
        long approvedCount = evaluationRepository.countByProjectIdAndStatus(projectId, EvaluationStatus.APPROVED);
        return approvedCount >= minApprovals;
    }
}