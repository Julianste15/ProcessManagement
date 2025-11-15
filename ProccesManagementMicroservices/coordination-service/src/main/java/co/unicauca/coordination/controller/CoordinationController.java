package co.unicauca.coordination.controller;
import co.unicauca.coordination.dto.DashboardStatsDTO;
import co.unicauca.coordination.dto.ProjectSummaryDTO;
import co.unicauca.coordination.service.CoordinationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.logging.Logger;

@RestController
@RequestMapping("/api/coordination")
@CrossOrigin(origins = "*")
public class CoordinationController {
    
    private static final Logger logger = Logger.getLogger(CoordinationController.class.getName());
    
    @Autowired
    private CoordinationService coordinationService;
    
    @GetMapping("/dashboard/{coordinatorId}")
    public ResponseEntity<DashboardStatsDTO> getDashboardStats(@PathVariable Long coordinatorId) {
        logger.info("Solicitando dashboard para coordinador: " + coordinatorId);
        try {
            DashboardStatsDTO stats = coordinationService.getDashboardStats(coordinatorId);
            return ResponseEntity.ok(stats);
        } catch (Exception e) {
            logger.severe("Error en dashboard: " + e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }
    
    @GetMapping("/projects/{coordinatorId}")
    public ResponseEntity<List<ProjectSummaryDTO>> getAllProjects(@PathVariable Long coordinatorId) {
        logger.info("Solicitando todos los proyectos para coordinador: " + coordinatorId);
        try {
            List<ProjectSummaryDTO> projects = coordinationService.getAllProjects(coordinatorId);
            return ResponseEntity.ok(projects);
        } catch (Exception e) {
            logger.severe("Error obteniendo proyectos: " + e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }
    
    @GetMapping("/pending-format-a/{coordinatorId}")
    public ResponseEntity<List<ProjectSummaryDTO>> getPendingFormatA(@PathVariable Long coordinatorId) {
        logger.info("Solicitando Formatos A pendientes para coordinador: " + coordinatorId);
        try {
            List<ProjectSummaryDTO> pendingFormatA = coordinationService.getPendingFormatA(coordinatorId);
            return ResponseEntity.ok(pendingFormatA);
        } catch (Exception e) {
            logger.severe("Error obteniendo Formatos A pendientes: " + e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }
    
    @GetMapping("/reports/{coordinatorId}")
    public ResponseEntity<String> generateReport(@PathVariable Long coordinatorId) {
        logger.info("Generando reporte para coordinador: " + coordinatorId);
        try {
            String report = coordinationService.generateReport(coordinatorId);
            return ResponseEntity.ok(report);
        } catch (Exception e) {
            logger.severe("Error generando reporte: " + e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }
}