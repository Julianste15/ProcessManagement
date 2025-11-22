package co.unicauca.evaluation.controller;
import co.unicauca.evaluation.dto.EvaluationAssignmentDTO;
import co.unicauca.evaluation.model.EvaluationAssignment;
import co.unicauca.evaluation.service.EvaluationAssignmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.stream.Collectors;
import java.util.logging.Logger;
@RestController
@RequestMapping("/api/evaluation-assignments")
@CrossOrigin(origins = "*")
public class EvaluationAssignmentController {    
    private static final Logger logger = Logger.getLogger(EvaluationAssignmentController.class.getName());    
    @Autowired
    private EvaluationAssignmentService assignmentService;    
    /**
     * Asignar evaluadores a un proyecto
     */
    @PostMapping
    public ResponseEntity<?> assignEvaluators(@RequestParam Long projectId,@RequestParam String evaluator1Email,
                                            @RequestParam String evaluator2Email,HttpServletRequest httpRequest) {
        try {
            String currentUser = httpRequest.getHeader("X-User-Email");
            String userRole = httpRequest.getHeader("X-User-Role");            
            logger.info("Asignando evaluadores - Usuario: " + currentUser);
            if (!isCoordinatorOrAdmin(userRole)) {
                return ResponseEntity.status(403).body("Acceso denegado: Se requiere rol COORDINATOR o ADMIN");
            }            
            EvaluationAssignment assignment = assignmentService.assignEvaluators(
                projectId, evaluator1Email, evaluator2Email, currentUser
            );            
            EvaluationAssignmentDTO assignmentDTO = convertToDTO(assignment);
            return ResponseEntity.ok(assignmentDTO);            
        } catch (Exception e) {
            logger.severe("Error asignando evaluadores: " + e.getMessage());
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }    
    /**
     * Obtener asignaciones del evaluador actual
     */
    @GetMapping("/my-assignments")
    public ResponseEntity<?> getMyAssignments(HttpServletRequest httpRequest) {
        try {
            String currentUser = httpRequest.getHeader("X-User-Email");
            String userRole = httpRequest.getHeader("X-User-Role");            
            if (!isTeacher(userRole)) {
                return ResponseEntity.status(403).body("Acceso denegado: Se requiere rol TEACHER");
            }            
            List<EvaluationAssignment> assignments = assignmentService.getAssignmentsByEvaluator(currentUser);
            List<EvaluationAssignmentDTO> assignmentDTOs = assignments.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());            
            return ResponseEntity.ok(assignmentDTOs);            
        } catch (Exception e) {
            logger.severe("Error obteniendo asignaciones: " + e.getMessage());
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }    
    /**
     * Obtener todas las asignaciones (solo admin/coordinator)
     */
    @GetMapping
    public ResponseEntity<?> getAllAssignments(HttpServletRequest httpRequest) {
        try {
            String userRole = httpRequest.getHeader("X-User-Role");            
            if (!isCoordinatorOrAdmin(userRole)) {
                return ResponseEntity.status(403).body("Acceso denegado: Se requiere rol COORDINATOR o ADMIN");
            }            
            List<EvaluationAssignment> assignments = assignmentService.getAllAssignments();
            List<EvaluationAssignmentDTO> assignmentDTOs = assignments.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());            
            return ResponseEntity.ok(assignmentDTOs);            
        } catch (Exception e) {
            logger.severe("Error obteniendo asignaciones: " + e.getMessage());
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }    
    private boolean isCoordinatorOrAdmin(String role) {
        return "COORDINATOR".equals(role) || "ADMINISTRATOR".equals(role);
    }    
    private boolean isTeacher(String role) {
        return "TEACHER".equals(role);
    }    
    private EvaluationAssignmentDTO convertToDTO(EvaluationAssignment assignment) {
        EvaluationAssignmentDTO dto = new EvaluationAssignmentDTO();
        dto.setId(assignment.getId());
        dto.setProjectId(assignment.getProjectId());
        dto.setEvaluator1Email(assignment.getEvaluator1Email());
        dto.setEvaluator2Email(assignment.getEvaluator2Email());
        dto.setAssignedBy(assignment.getAssignedBy());
        dto.setAssignedDate(assignment.getAssignedDate());
        dto.setDeadline(assignment.getDeadline());
        dto.setStatus(assignment.getStatus());
        return dto;
    }
}