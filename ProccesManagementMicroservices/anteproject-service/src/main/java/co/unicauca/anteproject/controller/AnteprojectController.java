package co.unicauca.anteproject.controller;
import co.unicauca.anteproject.dto.AnteprojectDTO;
import co.unicauca.anteproject.dto.CreateAnteprojectRequest;
import co.unicauca.anteproject.dto.ProgressUpdateDTO;
import co.unicauca.anteproject.model.AnteprojectStatus;
import co.unicauca.anteproject.service.AnteprojectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import java.util.List;
import java.util.logging.Logger;
@RestController
@RequestMapping("/api/anteprojects")
@CrossOrigin(origins = "*")
public class AnteprojectController {  
    private static final Logger logger = Logger.getLogger(AnteprojectController.class.getName());    
    @Autowired
    private AnteprojectService anteprojectService;    
    /**
     * Crear un nuevo anteproyecto
     */
    @PostMapping
    public ResponseEntity<?> createAnteproject(@Valid @RequestBody CreateAnteprojectRequest request,
                                             HttpServletRequest httpRequest) {
        try {
            String currentUser = httpRequest.getHeader("X-User-Email");
            String userRole = httpRequest.getHeader("X-User-Role");            
            logger.info("Creando anteproyecto - Usuario: " + currentUser);            
            if (!"STUDENT".equals(userRole)) {
                return ResponseEntity.status(403).body("Acceso denegado: Se requiere rol STUDENT");
            }
            if (!request.getStudentEmail().equals(currentUser)) {
                return ResponseEntity.status(403).body("Acceso denegado: Solo puede crear anteproyectos para su propio Formato A");
            }            
            AnteprojectDTO anteprojectDTO = anteprojectService.createAnteproject(request);
            return ResponseEntity.ok(anteprojectDTO);            
        } catch (Exception e) {
            logger.severe("Error creando anteproyecto: " + e.getMessage());
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }    
    /**
     * Subir documento de anteproyecto
     */
    @PostMapping("/{id}/submit-document")
    public ResponseEntity<?> submitDocument(@PathVariable Long id,@RequestParam String documentUrl,HttpServletRequest httpRequest) {
        try {
            String currentUser = httpRequest.getHeader("X-User-Email");            
            logger.info("Subiendo documento para anteproyecto: " + id + " - Usuario: " + currentUser);            
            AnteprojectDTO anteprojectDTO = anteprojectService.submitDocument(id, documentUrl, currentUser);
            return ResponseEntity.ok(anteprojectDTO);            
        } catch (Exception e) {
            logger.severe("Error subiendo documento: " + e.getMessage());
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }    
    /**
     * Agregar actualización de progreso
     */
    @PostMapping("/{id}/progress")
    public ResponseEntity<?> addProgressUpdate(@PathVariable Long id,@RequestParam String description,
                                             @RequestParam(required = false) Integer progressPercentage,
                                             HttpServletRequest httpRequest) {
        try {
            String currentUser = httpRequest.getHeader("X-User-Email");            
            logger.info("Agregando progreso para anteproyecto: " + id + " - Usuario: " + currentUser);            
            ProgressUpdateDTO progressDTO = anteprojectService.addProgressUpdate(
                id, description, progressPercentage, currentUser);
            return ResponseEntity.ok(progressDTO);            
        } catch (Exception e) {
            logger.severe("Error agregando progreso: " + e.getMessage());
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }    
    /**
     * Obtener anteproyecto por ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> getAnteprojectById(@PathVariable Long id,HttpServletRequest httpRequest) {
        try {
            String currentUser = httpRequest.getHeader("X-User-Email");
            String userRole = httpRequest.getHeader("X-User-Role");            
            AnteprojectDTO anteprojectDTO = anteprojectService.getAnteprojectById(id);
            if (!hasViewPermission(anteprojectDTO, currentUser, userRole)) {
                return ResponseEntity.status(403).body("Acceso denegado");
            }            
            return ResponseEntity.ok(anteprojectDTO);            
        } catch (Exception e) {
            logger.severe("Error obteniendo anteproyecto: " + e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }    
    /**
     * Obtener anteproyectos del estudiante actual
     */
    @GetMapping("/my-anteprojects")
    public ResponseEntity<?> getMyAnteprojects(HttpServletRequest httpRequest) {
        try {
            String currentUser = httpRequest.getHeader("X-User-Email");
            String userRole = httpRequest.getHeader("X-User-Role");            
            if (!"STUDENT".equals(userRole)) {
                return ResponseEntity.status(403).body("Acceso denegado: Se requiere rol STUDENT");
            }            
            List<AnteprojectDTO> anteprojects = anteprojectService.getAnteprojectsByStudent(currentUser);
            return ResponseEntity.ok(anteprojects);            
        } catch (Exception e) {
            logger.severe("Error obteniendo anteproyectos: " + e.getMessage());
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }    
    /**
     * Obtener anteproyectos del director actual
     */
    @GetMapping("/director-projects")
    public ResponseEntity<?> getDirectorAnteprojects(HttpServletRequest httpRequest) {
        try {
            String currentUser = httpRequest.getHeader("X-User-Email");
            String userRole = httpRequest.getHeader("X-User-Role");            
            if (!"TEACHER".equals(userRole)) {
                return ResponseEntity.status(403).body("Acceso denegado: Se requiere rol TEACHER");
            }            
            List<AnteprojectDTO> anteprojects = anteprojectService.getAnteprojectsByDirector(currentUser);
            return ResponseEntity.ok(anteprojects);            
        } catch (Exception e) {
            logger.severe("Error obteniendo anteproyectos: " + e.getMessage());
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }    
    /**
     * Actualizar estado del anteproyecto (solo coordinadores/admin)
     */
    @PutMapping("/{id}/status")
    public ResponseEntity<?> updateStatus(@PathVariable Long id,@RequestParam AnteprojectStatus status,
                HttpServletRequest httpRequest) {
        try {
            String currentUser = httpRequest.getHeader("X-User-Email");
            String userRole = httpRequest.getHeader("X-User-Role");            
            if (!isCoordinatorOrAdmin(userRole)) {
                return ResponseEntity.status(403).body("Acceso denegado: Se requiere rol COORDINATOR o ADMIN");
            }            
            AnteprojectDTO anteprojectDTO = anteprojectService.updateStatus(id, status, currentUser);
            return ResponseEntity.ok(anteprojectDTO);            
        } catch (Exception e) {
            logger.severe("Error actualizando estado: " + e.getMessage());
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }    
    // Métodos helper
    private boolean hasViewPermission(AnteprojectDTO anteproject, String currentUser, String userRole) {
        return anteproject.getStudentEmail().equals(currentUser) ||
               anteproject.getDirectorEmail().equals(currentUser) ||
               isCoordinatorOrAdmin(userRole);
    }    
    private boolean isCoordinatorOrAdmin(String userRole) {
        return "COORDINATOR".equals(userRole) || "ADMINISTRATOR".equals(userRole);
    }
}