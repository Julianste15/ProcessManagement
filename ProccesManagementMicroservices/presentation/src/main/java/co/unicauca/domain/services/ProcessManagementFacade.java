package co.unicauca.domain.services;

import co.unicauca.infrastructure.client.MicroserviceClient;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

/**
 * Facade que simplifica las interacciones entre los controladores JavaFX
 * y los múltiples microservicios backend.
 * 
 * Encapsula la complejidad de:
 * - Llamadas HTTP a múltiples servicios
 * - Manejo de errores centralizado
 * - Orquestación de operaciones complejas
 */
public class ProcessManagementFacade {

    private static final Logger logger = Logger.getLogger(ProcessManagementFacade.class.getName());

    private final FormatAService formatAService;
    private final AnteprojectService anteprojectService;

    public ProcessManagementFacade(MicroserviceClient client) {
        this.formatAService = new FormatAService(client);
        this.anteprojectService = new AnteprojectService(client);
    }

    // ==================== FormatA Operations ====================

    /**
     * Envía un nuevo Formato A
     */
    public Map<String, Object> submitFormatA(Map<String, Object> request) throws Exception {
        logger.info("Facade: Enviando Formato A");
        return formatAService.submitFormatoA(request);
    }

    /**
     * Reenvía un Formato A rechazado
     */
    public Map<String, Object> resubmitFormatA(Long formatoAId, Map<String, Object> request) throws Exception {
        logger.info("Facade: Reenviando Formato A ID: " + formatoAId);
        return formatAService.resubmitFormatoA(formatoAId, request);
    }

    /**
     * Obtiene los Formatos A de un usuario
     */
    public List<Map<String, Object>> getFormatAsByUser(String email) throws Exception {
        logger.info("Facade: Consultando Formatos A para usuario: " + email);
        return formatAService.getFormatosPorUsuario(email);
    }

    /**
     * Obtiene proyectos pendientes para coordinador
     */
    public List<Map<String, Object>> getProjectsForCoordinator(String coordinatorEmail) throws Exception {
        logger.info("Facade: Consultando proyectos para coordinador");
        return formatAService.getProjectsForCoordinator(coordinatorEmail);
    }

    /**
     * Aprueba un proyecto (Formato A)
     */
    public void approveProject(Long projectId, String observations) throws Exception {
        logger.info("Facade: Aprobando proyecto ID: " + projectId);
        formatAService.approveProject(projectId, observations);
    }

    /**
     * Rechaza un proyecto (Formato A)
     */
    public void rejectProject(Long projectId, String observations) throws Exception {
        logger.info("Facade: Rechazando proyecto ID: " + projectId);
        formatAService.rejectProject(projectId, observations);
    }

    // ==================== Anteproject Operations ====================

    /**
     * Crea un anteproyecto a partir de un Formato A aprobado
     */
    public Map<String, Object> createAnteproject(Long formatoAId, String titulo, String directorEmail)
            throws Exception {
        logger.info("Facade: Creando anteproyecto para Formato A ID: " + formatoAId);
        return formatAService.createAnteproject(formatoAId, titulo, directorEmail);
    }

    /**
     * Sube el documento de un anteproyecto
     */
    public boolean uploadAnteprojectDocument(Long anteprojectId, String documentUrl) throws Exception {
        logger.info("Facade: Subiendo documento para anteproyecto ID: " + anteprojectId);
        return formatAService.uploadAnteproject(anteprojectId, documentUrl);
    }

    /**
     * Obtiene anteproyectos enviados (para jefe de departamento)
     */
    public List<Map<String, Object>> getSubmittedAnteprojects() throws Exception {
        logger.info("Facade: Consultando anteproyectos enviados");
        return anteprojectService.getSubmittedAnteprojects();
    }

    /**
     * Asigna evaluadores a un anteproyecto
     */
    public Map<String, Object> assignEvaluators(Long anteprojectId, String evaluator1Email, String evaluator2Email)
            throws Exception {
        logger.info("Facade: Asignando evaluadores para anteproyecto ID: " + anteprojectId);
        return anteprojectService.assignEvaluators(anteprojectId, evaluator1Email, evaluator2Email);
    }

    // ==================== Complex Workflows ====================

    /**
     * Flujo completo: Evaluar y aprobar proyecto
     * Orquesta: aprobación + creación de anteproyecto
     */
    public Map<String, Object> approveAndCreateAnteproject(Long formatoAId, String titulo,
            String directorEmail, String observations) throws Exception {
        logger.info("Facade: Ejecutando flujo de aprobación completo para Formato A ID: " + formatoAId);

        // 1. Aprobar el Formato A
        approveProject(formatoAId, observations);

        // 2. Crear el anteproyecto automáticamente
        Map<String, Object> anteproject = createAnteproject(formatoAId, titulo, directorEmail);

        logger.info("Facade: Flujo completado exitosamente");
        return anteproject;
    }

    /**
     * Obtiene el dashboard completo para un profesor
     * Combina datos de múltiples servicios
     */
    public Map<String, Object> getTeacherDashboard(String teacherEmail) throws Exception {
        logger.info("Facade: Construyendo dashboard para profesor: " + teacherEmail);

        // Obtener formatos del profesor
        List<Map<String, Object>> formatos = getFormatAsByUser(teacherEmail);

        // Combinar en un solo objeto
        return Map.of(
                "teacherEmail", teacherEmail,
                "formatos", formatos,
                "totalFormatos", formatos.size());
    }
}
