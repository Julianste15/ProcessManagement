package co.unicauca.domain.services;

import co.unicauca.infrastructure.client.MicroserviceClient;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

/**
 * Facade pattern implementation that provides a simplified interface
 * for managing the entire process workflow including Format A submissions,
 * anteprojects, and user management.
 * 
 * This facade coordinates multiple services to provide a unified API
 * for the presentation layer controllers.
 */
public class ProcessManagementFacade {
    private static final Logger logger = Logger.getLogger(ProcessManagementFacade.class.getName());

    private final FormatAService formatAService;
    private final AnteprojectService anteprojectService;
    private final UserService userService;

    /**
     * Creates a new ProcessManagementFacade with the given microservice client.
     * 
     * @param client The microservice client for HTTP communication
     */
    public ProcessManagementFacade(MicroserviceClient client) {
        this.formatAService = new FormatAService(client);
        this.anteprojectService = new AnteprojectService(client);
        this.userService = new UserService(client);
    }

    // ==================== Format A Operations ====================

    /**
     * Submits a new Format A request.
     * 
     * @param request Map containing Format A data (titulo, modalidad,
     *                directorEmail, etc.)
     * @return Map containing the response with at least the "id" and "estado"
     *         fields
     * @throws Exception if submission fails
     */
    public Map<String, Object> submitFormatA(Map<String, Object> request) throws Exception {
        logger.info("Facade: Submitting new Format A");
        return formatAService.submitFormatoA(request);
    }

    /**
     * Resubmits an existing Format A after rejection.
     * 
     * @param formatoAId The ID of the Format A to resubmit
     * @param request    Map containing updated Format A data
     * @return Map containing the response with updated estado
     * @throws Exception if resubmission fails
     */
    public Map<String, Object> resubmitFormatA(Long formatoAId, Map<String, Object> request) throws Exception {
        logger.info("Facade: Resubmitting Format A ID: " + formatoAId);
        return formatAService.resubmitFormatoA(formatoAId, request);
    }

    /**
     * Gets all Format A submissions for a specific user.
     * 
     * @param email The user's email
     * @return List of Format A submissions
     * @throws Exception if retrieval fails
     */
    public List<Map<String, Object>> getFormatAByUser(String email) throws Exception {
        logger.info("Facade: Getting Format A for user: " + email);
        return formatAService.getFormatosPorUsuario(email);
    }

    /**
     * Gets all Format A projects pending review by a coordinator.
     * 
     * @param coordinatorEmail The coordinator's email
     * @return List of projects pending review
     * @throws Exception if retrieval fails
     */
    public List<Map<String, Object>> getProjectsForCoordinator(String coordinatorEmail) throws Exception {
        logger.info("Facade: Getting projects for coordinator: " + coordinatorEmail);
        return formatAService.getProjectsForCoordinator(coordinatorEmail);
    }

    /**
     * Approves a Format A project.
     * 
     * @param projectId    The project ID to approve
     * @param observations Optional observations/comments
     * @throws Exception if approval fails
     */
    public void approveFormatA(Long projectId, String observations) throws Exception {
        logger.info("Facade: Approving Format A project ID: " + projectId);
        formatAService.approveProject(projectId, observations);
    }

    /**
     * Rejects a Format A project.
     * 
     * @param projectId    The project ID to reject
     * @param observations Required observations explaining the rejection
     * @throws Exception if rejection fails
     */
    public void rejectFormatA(Long projectId, String observations) throws Exception {
        logger.info("Facade: Rejecting Format A project ID: " + projectId);
        formatAService.rejectProject(projectId, observations);
    }

    // ==================== Anteproject Operations ====================

    /**
     * Creates a new anteproject associated with an approved Format A.
     * 
     * @param formatoAId    The Format A ID
     * @param titulo        The anteproject title
     * @param directorEmail The director's email
     * @return Map containing the created anteproject data
     * @throws Exception if creation fails
     */
    public Map<String, Object> createAnteproject(Long formatoAId, String titulo, String directorEmail)
            throws Exception {
        logger.info("Facade: Creating anteproject for Format A ID: " + formatoAId);
        return formatAService.createAnteproject(formatoAId, titulo, directorEmail);
    }

    /**
     * Uploads an anteproject document.
     * 
     * @param anteprojectId The anteproject ID
     * @param documentUrl   The document URL
     * @return true if upload was successful
     * @throws Exception if upload fails
     */
    public boolean uploadAnteproject(Long anteprojectId, String documentUrl) throws Exception {
        logger.info("Facade: Uploading anteproject ID: " + anteprojectId);
        return formatAService.uploadAnteproject(anteprojectId, documentUrl);
    }

    /**
     * Gets all anteprojects for a specific student.
     * 
     * @param studentEmail The student's email
     * @return List of anteprojects
     * @throws Exception if retrieval fails
     */
    public List<Map<String, Object>> getAnteprojectsByStudent(String studentEmail) throws Exception {
        logger.info("Facade: Getting anteprojects for student: " + studentEmail);
        return anteprojectService.getAnteprojectsByStudent(studentEmail);
    }

    // ==================== Additional Operations ====================

    /**
     * Registers a new user.
     * 
     * @param user User object containing registration data
     * @return The registered User object
     * @throws Exception if registration fails
     */
    public co.unicauca.domain.entities.User registerUser(co.unicauca.domain.entities.User user) throws Exception {
        logger.info("Facade: Registering new user");
        return userService.registerUser(user);
    }

    /**
     * Gets all teachers from the Systems Engineering department.
     * 
     * @return List of teacher data maps
     * @throws Exception if retrieval fails
     */
    public List<Map<String, Object>> getSystemsTeachers() throws Exception {
        logger.info("Facade: Getting systems teachers");
        return userService.getSystemsTeachers();
    }

    /**
     * Checks if a user is the current department head.
     * 
     * @param email The user's email
     * @return true if the user is the department head, false otherwise
     */
    public boolean isDepartmentHead(String email) {
        logger.info("Facade: Checking if user is department head: " + email);
        return userService.isDepartmentHead(email);
    }

    /**
     * Assigns a teacher as the department head.
     * 
     * @param teacherId The teacher's ID
     * @param startDate The start date of the assignment
     * @param endDate   The end date of the assignment
     * @return true if assignment was successful
     * @throws Exception if assignment fails
     */
    public boolean assignDepartmentHead(Long teacherId, String startDate, String endDate) throws Exception {
        logger.info("Facade: Assigning department head: " + teacherId);
        return userService.assignDepartmentHead(teacherId, startDate, endDate);
    }

    /**
     * Gets all submitted anteprojects (for department head).
     * 
     * @return List of submitted anteprojects
     * @throws Exception if retrieval fails
     */
    public List<Map<String, Object>> getSubmittedAnteprojects() throws Exception {
        logger.info("Facade: Getting submitted anteprojects");
        return anteprojectService.getSubmittedAnteprojects();
    }

    /**
     * Assigns evaluators to an anteproject.
     * 
     * @param anteprojectId   The anteproject ID
     * @param evaluator1Email First evaluator's email
     * @param evaluator2Email Second evaluator's email
     * @return Map containing the assignment result
     * @throws Exception if assignment fails
     */
    public Map<String, Object> assignEvaluators(Long anteprojectId, String evaluator1Email, String evaluator2Email)
            throws Exception {
        logger.info("Facade: Assigning evaluators to anteproject: " + anteprojectId);
        return anteprojectService.assignEvaluators(anteprojectId, evaluator1Email, evaluator2Email);
    }
}
