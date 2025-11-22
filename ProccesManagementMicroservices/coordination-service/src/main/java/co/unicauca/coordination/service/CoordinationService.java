package co.unicauca.coordination.service;
import co.unicauca.coordination.client.AnteprojectClient;
import co.unicauca.coordination.client.FormatAClient;
import co.unicauca.coordination.dto.DashboardStatsDTO;
import co.unicauca.coordination.dto.ProjectSummaryDTO;
import co.unicauca.coordination.repository.CoordinationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.logging.Logger;
@Service
public class CoordinationService {    
    private static final Logger logger = Logger.getLogger(CoordinationService.class.getName());    
    @Autowired
    private CoordinationRepository coordinationRepository;    
    @Autowired
    private AnteprojectClient anteprojectClient;    
    @Autowired
    private FormatAClient formatAClient;    
    public DashboardStatsDTO getDashboardStats(Long coordinatorId) {
        logger.info("Obteniendo estadísticas del dashboard para coordinador: " + coordinatorId);        
        try {
            List<ProjectSummaryDTO> allProjects = anteprojectClient.getProjectsByCoordinator(coordinatorId);
            List<ProjectSummaryDTO> pendingFormatA = formatAClient.getPendingFormatAByCoordinator(coordinatorId);            
            DashboardStatsDTO stats = new DashboardStatsDTO(coordinatorId);
            stats.setTotalProjects(allProjects.size());
            stats.setPendingFormatA(pendingFormatA.size());
            stats.setPendingAnteprojects((int) allProjects.stream()
                    .filter(p -> "PENDING_EVALUATION".equals(p.getStatus()))
                    .count());
            stats.setApprovedProjects((int) allProjects.stream()
                    .filter(p -> "APPROVED".equals(p.getStatus()))
                    .count());
            stats.setRejectedProjects((int) allProjects.stream()
                    .filter(p -> "REJECTED".equals(p.getStatus()))
                    .count());
            stats.setLastUpdate(LocalDateTime.now());            
            logger.info("Estadísticas generadas exitosamente para coordinador: " + coordinatorId);
            return stats;            
        } catch (Exception e) {
            logger.severe("Error obteniendo estadísticas del dashboard: " + e.getMessage());
            throw new RuntimeException("Error al obtener estadísticas del dashboard", e);
        }
    }    
    public List<ProjectSummaryDTO> getAllProjects(Long coordinatorId) {
        logger.info("Obteniendo todos los proyectos para coordinador: " + coordinatorId);
        try {
            return anteprojectClient.getProjectsByCoordinator(coordinatorId);
        } catch (Exception e) {
            logger.severe("Error obteniendo proyectos: " + e.getMessage());
            throw new RuntimeException("Error al obtener proyectos", e);
        }
    }    
    public List<ProjectSummaryDTO> getPendingFormatA(Long coordinatorId) {
        logger.info("Obteniendo Formatos A pendientes para coordinador: " + coordinatorId);
        try {
            return formatAClient.getPendingFormatAByCoordinator(coordinatorId);
        } catch (Exception e) {
            logger.severe("Error obteniendo Formatos A pendientes: " + e.getMessage());
            throw new RuntimeException("Error al obtener Formatos A pendientes", e);
        }
    }    
    public String generateReport(Long coordinatorId) {
        logger.info("Generando reporte para coordinador: " + coordinatorId);        
        DashboardStatsDTO stats = getDashboardStats(coordinatorId);        
        String report = String.format(
            "REPORTE DE COORDINACIÓN - COORDINADOR: %d\n" +
            "Fecha de generación: %s\n" +
            "Total de proyectos: %d\n" +
            "Formatos A pendientes: %d\n" +
            "Anteproyectos pendientes: %d\n" +
            "Proyectos aprobados: %d\n" +
            "Proyectos rechazados: %d",
            coordinatorId,
            LocalDateTime.now(),
            stats.getTotalProjects(),
            stats.getPendingFormatA(),
            stats.getPendingAnteprojects(),
            stats.getApprovedProjects(),
            stats.getRejectedProjects()
        );        
        logger.info("Reporte generado exitosamente para coordinador: " + coordinatorId);
        return report;
    }
}