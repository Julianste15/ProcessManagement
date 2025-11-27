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
     * Asignar evaluadores a un proyecto - SOLO JEFE DE DEPARTAMENTO (permanente o temporal)
     */
    @PostMapping
    public ResponseEntity<?> assignEvaluators(@RequestParam Long projectId,
                                            @RequestParam String evaluator1Email,
                                            @RequestParam String evaluator2Email,
                                            HttpServletRequest httpRequest) {
        try {
            String currentUser = httpRequest.getHeader("X-User-Email");
            String userRole = httpRequest.getHeader("X-User-Role");
            String isDepartmentHead = httpRequest.getHeader("X-User-IsDepartmentHead");
            
            logger.info("Asignando evaluadores - Usuario: " + currentUser + 
                       ", Rol: " + userRole + 
                       ", EsJefeDepto: " + isDepartmentHead);
            
            // SOLO JEFE DE DEPARTAMENTO (permanente o temporal) puede asignar evaluadores
            if (!isDepartmentHead(userRole, isDepartmentHead)) {
                return ResponseEntity.status(403).body("Acceso denegado: Se requiere ser Jefe de Departamento");
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
     * Obtener asignaciones del evaluador actual - PARA PROFESORES
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
     * Obtener todas las asignaciones - JEFE DE DEPARTAMENTO (Coordinador eliminado)
     */
    @GetMapping
    public ResponseEntity<?> getAllAssignments(HttpServletRequest httpRequest) {
        try {
            String userRole = httpRequest.getHeader("X-User-Role");
            String isDepartmentHead = httpRequest.getHeader("X-User-IsDepartmentHead");
            
            // Jefe de Departamento (permanente o temporal) puede ver todas las asignaciones
            if (!isDepartmentHeadOrCoordinator(userRole, isDepartmentHead)) {
                return ResponseEntity.status(403).body("Acceso denegado: Se requiere ser Jefe de Departamento");
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
    
    /**
     * Obtener asignaciones por proyecto - ACCESO PARA ROLES AUTORIZADOS
     */
    @GetMapping("/project/{projectId}")
    public ResponseEntity<?> getAssignmentsByProject(@PathVariable Long projectId, 
                                                   HttpServletRequest httpRequest) {
        try {
            String userRole = httpRequest.getHeader("X-User-Role");
            String isDepartmentHead = httpRequest.getHeader("X-User-IsDepartmentHead");
            
            // Jefe de Departamento, Admin y Profesores relacionados pueden ver
            if (!isAuthorizedRole(userRole, isDepartmentHead)) {
                return ResponseEntity.status(403).body("Acceso denegado: Rol no autorizado");
            }            
            List<EvaluationAssignment> assignments = assignmentService.getAssignmentsByProject(projectId);
            List<EvaluationAssignmentDTO> assignmentDTOs = assignments.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());            
            return ResponseEntity.ok(assignmentDTOs);            
        } catch (Exception e) {
            logger.severe("Error obteniendo asignaciones por proyecto: " + e.getMessage());
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }
    
    // Métodos de verificación de roles actualizados para jefes temporales
    
    /**
     * Verifica si el usuario es Jefe de Departamento (permanente o temporal)
     */
    private boolean isDepartmentHead(String role, String isDepartmentHead) {
        // Jefe permanente
        if ("DEPARTMENT_HEAD".equals(role)) {
            return true;
        }
        // Profesor con cargo temporal de jefe
        if ("TEACHER".equals(role) && "true".equalsIgnoreCase(isDepartmentHead)) {
            return true;
        }
        return false;
    }
    
    /**
     * Verifica si es Jefe de Departamento (Coordinador eliminado)
     */
    private boolean isDepartmentHeadOrCoordinator(String role, String isDepartmentHead) {
        return isDepartmentHead(role, isDepartmentHead);
    }
    
    /**
     * Verifica si es profesor
     */
    private boolean isTeacher(String role) {
        return "TEACHER".equals(role);
    }
    
    /**
     * Verifica roles autorizados para ver información
     */
    private boolean isAuthorizedRole(String role, String isDepartmentHead) {
        return isDepartmentHead(role, isDepartmentHead) || 
               "ADMINISTRATOR".equals(role) || 
               "TEACHER".equals(role);
    }
    
    /**
     * Endpoint adicional: Verificar si el usuario actual puede asignar evaluadores
     */
    @GetMapping("/can-assign")
    public ResponseEntity<?> canAssignEvaluators(HttpServletRequest httpRequest) {
        try {
            String userRole = httpRequest.getHeader("X-User-Role");
            String isDepartmentHead = httpRequest.getHeader("X-User-IsDepartmentHead");
            
            boolean canAssign = isDepartmentHead(userRole, isDepartmentHead);
            
            return ResponseEntity.ok().body(
                new CanAssignResponse(canAssign, userRole, isDepartmentHead)
            );
            
        } catch (Exception e) {
            logger.severe("Error verificando permisos: " + e.getMessage());
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }
    
    // Clase interna para respuesta de verificación de permisos
    private static class CanAssignResponse {
        private boolean canAssign;
        private String userRole;
        private String isDepartmentHead;
        
        public CanAssignResponse(boolean canAssign, String userRole, String isDepartmentHead) {
            this.canAssign = canAssign;
            this.userRole = userRole;
            this.isDepartmentHead = isDepartmentHead;
        }
        
        // Getters
        public boolean isCanAssign() { return canAssign; }
        public String getUserRole() { return userRole; }
        public String getIsDepartmentHead() { return isDepartmentHead; }
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