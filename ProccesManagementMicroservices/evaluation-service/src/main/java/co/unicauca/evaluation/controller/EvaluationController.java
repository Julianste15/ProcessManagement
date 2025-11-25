package co.unicauca.evaluation.controller;
import co.unicauca.evaluation.dto.EvaluationDTO;
import co.unicauca.evaluation.dto.CreateEvaluationRequest;
import co.unicauca.evaluation.model.Evaluation;
import co.unicauca.evaluation.service.EvaluationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;
import java.util.logging.Logger;
@RestController
@RequestMapping("/api/evaluations")
@CrossOrigin(origins = "*")
public class EvaluationController {    
    private static final Logger logger = Logger.getLogger(EvaluationController.class.getName());    
    @Autowired
    private EvaluationService evaluationService;    
    /**
     * Crear una nueva evaluación
     */
    @PostMapping
    public ResponseEntity<?> createEvaluation(@Valid @RequestBody CreateEvaluationRequest request,HttpServletRequest httpRequest) {
        try {
            String currentUser = httpRequest.getHeader("X-User-Email");
            String userRole = httpRequest.getHeader("X-User-Role");            
            logger.info("Creando evaluación - Usuario: " + currentUser + ", Rol: " + userRole);            
            if (!isCoordinatorOrAdmin(userRole)) {
                return ResponseEntity.status(403).body("Acceso denegado: Se requiere rol COORDINATOR o ADMIN");
            }            
            Evaluation evaluation = evaluationService.createEvaluation(
                request.getProjectId(), 
                request.getEvaluatorEmail()
            );            
            EvaluationDTO evaluationDTO = convertToDTO(evaluation);
            return ResponseEntity.ok(evaluationDTO);            
        } catch (Exception e) {
            logger.severe("Error creando evaluación: " + e.getMessage());
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }    
    /**
     * Enviar evaluación completada
     */
    @PostMapping("/{evaluationId}/submit")
    public ResponseEntity<?> submitEvaluation(@PathVariable Long evaluationId,@RequestParam Double score,
                                            @RequestParam String comments,@RequestParam(required = false) String recommendations,
                                            HttpServletRequest httpRequest) {
        try {
            String currentUser = httpRequest.getHeader("X-User-Email");                      
            logger.info("Enviando evaluación - Usuario: " + currentUser);            
            if (!isAssignedEvaluator(evaluationId, currentUser)) {
                return ResponseEntity.status(403).body("Acceso denegado: No es el evaluador asignado");
            }            
            Evaluation evaluation = evaluationService.submitEvaluation(
                evaluationId, score, comments, recommendations
            );            
            EvaluationDTO evaluationDTO = convertToDTO(evaluation);
            return ResponseEntity.ok(evaluationDTO);            
        } catch (Exception e) {
            logger.severe("Error enviando evaluación: " + e.getMessage());
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }    
    /**
     * Obtener evaluaciones por proyecto
     */
    @GetMapping("/project/{projectId}")
    public ResponseEntity<?> getEvaluationsByProject(@PathVariable Long projectId,HttpServletRequest httpRequest) {
        try {
            String userRole = httpRequest.getHeader("X-User-Role");
            if (!isAuthorizedUser(userRole)) {
                return ResponseEntity.status(403).body("Acceso denegado");
            }            
            List<Evaluation> evaluations = evaluationService.getEvaluationsByProject(projectId);
            List<EvaluationDTO> evaluationDTOs = evaluations.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());            
            return ResponseEntity.ok(evaluationDTOs);            
        } catch (Exception e) {
            logger.severe("Error obteniendo evaluaciones: " + e.getMessage());
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }    
    /**
     * Obtener evaluaciones del evaluador actual
     */
    @GetMapping("/my-evaluations")
    public ResponseEntity<?> getMyEvaluations(HttpServletRequest httpRequest) {
        try {
            String currentUser = httpRequest.getHeader("X-User-Email");
            String userRole = httpRequest.getHeader("X-User-Role");            
            if (!isTeacher(userRole)) {
                return ResponseEntity.status(403).body("Acceso denegado: Se requiere rol TEACHER");
            }            
            List<Evaluation> evaluations = evaluationService.getEvaluationsByEvaluator(currentUser);
            List<EvaluationDTO> evaluationDTOs = evaluations.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());            
            return ResponseEntity.ok(evaluationDTOs);            
        } catch (Exception e) {
            logger.severe("Error obteniendo evaluaciones: " + e.getMessage());
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }    
    private boolean isCoordinatorOrAdmin(String role) {
        return "COORDINATOR".equals(role) || "ADMINISTRATOR".equals(role);
    }    
    private boolean isTeacher(String role) {
        return "TEACHER".equals(role);
    }    
    private boolean isAuthorizedUser(String role) {
        return isCoordinatorOrAdmin(role) || isTeacher(role);
    }    
    private boolean isAssignedEvaluator(Long evaluationId, String userEmail) {
        return evaluationService.getEvaluation(evaluationId)
            .map(evaluation -> evaluation.getEvaluatorEmail().equals(userEmail))
            .orElse(false);
    }    
    private EvaluationDTO convertToDTO(Evaluation evaluation) {
        EvaluationDTO dto = new EvaluationDTO();
        dto.setId(evaluation.getId());
        dto.setProjectId(evaluation.getProjectId());
        dto.setEvaluatorEmail(evaluation.getEvaluatorEmail());
        dto.setStatus(evaluation.getStatus());
        dto.setScore(evaluation.getScore());
        dto.setComments(evaluation.getComments());
        dto.setRecommendations(evaluation.getRecommendations());
        dto.setSubmissionDate(evaluation.getSubmissionDate());
        dto.setEvaluationDate(evaluation.getEvaluationDate());
        dto.setCreatedAt(evaluation.getCreatedAt());
        return dto;
    }
}