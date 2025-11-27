package co.unicauca.anteproject.service;

import co.unicauca.anteproject.client.EvaluationServiceClient;
import co.unicauca.anteproject.dto.AnteprojectDTO;
import co.unicauca.anteproject.dto.CreateAnteprojectRequest;
import co.unicauca.anteproject.dto.ProgressUpdateDTO;
import co.unicauca.anteproject.events.AnteprojectSubmittedEvent;
import co.unicauca.anteproject.events.AnteprojectEventPublisher;
import co.unicauca.anteproject.events.EvaluatorAssignmentEvent;
import co.unicauca.anteproject.model.Anteproject;
import co.unicauca.anteproject.model.AnteprojectStatus;
import co.unicauca.anteproject.model.ProjectProgress;
import co.unicauca.anteproject.repository.AnteprojectRepository;
import co.unicauca.anteproject.repository.ProjectProgressRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Service
public class AnteprojectService {

    private static final Logger logger = Logger.getLogger(AnteprojectService.class.getName());

    @Autowired
    private AnteprojectRepository anteprojectRepository;

    @Autowired
    private ProjectProgressRepository progressRepository;

    @Autowired
    private EvaluationServiceClient evaluationServiceClient;

    @Autowired
    private AnteprojectEventPublisher eventPublisher;

    public AnteprojectDTO createAnteproject(CreateAnteprojectRequest request) {
        logger.info("Creando anteproyecto para Formato A: " + request.getFormatoAId());
        if (anteprojectRepository.findByFormatoAId(request.getFormatoAId()).isPresent()) {
            throw new RuntimeException("Ya existe un anteproyecto para este Formato A");
        }
        Anteproject anteproject = new Anteproject(
                request.getFormatoAId(),
                request.getTitulo(),
                request.getStudentEmail(),
                request.getDirectorEmail());
        Anteproject savedAnteproject = anteprojectRepository.save(anteproject);
        logger.info("Anteproyecto creado exitosamente: " + savedAnteproject.getId());
        return convertToDTO(savedAnteproject);
    }

    public AnteprojectDTO submitDocument(Long anteprojectId, String documentUrl, String submittedBy) {
        System.out.println("========== SUBMIT DOCUMENT ==========");
        System.out.println("Anteproject ID: " + anteprojectId);
        System.out.println("Document URL: " + documentUrl);
        System.out.println("Submitted By: " + submittedBy);
        
        logger.info("Subiendo documento para anteproyecto: " + anteprojectId);
        Anteproject anteproject = anteprojectRepository.findById(anteprojectId)
                .orElseThrow(() -> new RuntimeException("Anteproyecto no encontrado"));

        System.out.println("Anteproject encontrado:");
        System.out.println("  - Student Email: " + anteproject.getStudentEmail());
        System.out.println("  - Director Email: " + anteproject.getDirectorEmail());
        System.out.println("  - Current Status: " + anteproject.getStatus());
        
        boolean isStudent = anteproject.getStudentEmail().equals(submittedBy);
        boolean isDirector = anteproject.getDirectorEmail().equals(submittedBy);
        
        System.out.println("  - Is Student? " + isStudent);
        System.out.println("  - Is Director? " + isDirector);

        if (!isStudent && !isDirector) {
            System.out.println("ERROR: No tiene permisos");
            System.out.println("=====================================");
            throw new RuntimeException("No tiene permisos para subir documentos de este anteproyecto");
        }

        anteproject.setDocumentUrl(documentUrl);
        anteproject.setStatus(AnteprojectStatus.SUBMITTED);
        anteproject.setSubmissionDate(LocalDateTime.now());

        Anteproject updatedAnteproject = anteprojectRepository.save(anteproject);
        System.out.println("Documento subido exitosamente");
        System.out.println("=====================================");
        logger.info("Documento subido exitosamente para anteproyecto: " + anteprojectId);

        AnteprojectSubmittedEvent event = new AnteprojectSubmittedEvent(
                updatedAnteproject.getId(),
                updatedAnteproject.getStudentEmail(),
                updatedAnteproject.getSubmissionDate(),
                updatedAnteproject.getDocumentUrl()
        );
        eventPublisher.publishAnteprojectSubmitted(event);

        return convertToDTO(updatedAnteproject);
    }

    public ProgressUpdateDTO addProgressUpdate(Long anteprojectId, String description,
                                                Integer progressPercentage, String createdBy) {
        logger.info("Agregando actualización de progreso para anteproyecto: " + anteprojectId);
        Anteproject anteproject = anteprojectRepository.findById(anteprojectId)
                .orElseThrow(() -> new RuntimeException("Anteproyecto no encontrado"));

        if (!anteproject.getStudentEmail().equals(createdBy) && !anteproject.getDirectorEmail().equals(createdBy)) {
            throw new RuntimeException("No tiene permisos para actualizar este anteproyecto");
        }

        ProjectProgress progress = new ProjectProgress(anteproject, description, createdBy);
        progress.setProgressPercentage(progressPercentage);
        ProjectProgress savedProgress = progressRepository.save(progress);
        logger.info("Actualización de progreso agregada: " + savedProgress.getId());
        return convertProgressToDTO(savedProgress);
    }

    public AnteprojectDTO getAnteprojectById(Long id) {
        Anteproject anteproject = anteprojectRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Anteproyecto no encontrado"));
        return convertToDTO(anteproject);
    }

    public List<AnteprojectDTO> getAnteprojectsByStudent(String studentEmail) {
        List<Anteproject> anteprojects = anteprojectRepository.findByStudentEmail(studentEmail);
        return anteprojects.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<AnteprojectDTO> getAnteprojectsByDirector(String directorEmail) {
        List<Anteproject> anteprojects = anteprojectRepository.findByDirectorEmail(directorEmail);
        return anteprojects.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public AnteprojectDTO updateStatus(Long anteprojectId, AnteprojectStatus status, String updatedBy) {
        Anteproject anteproject = anteprojectRepository.findById(anteprojectId)
                .orElseThrow(() -> new RuntimeException("Anteproyecto no encontrado"));
        anteproject.setStatus(status);
        Anteproject updatedAnteproject = anteprojectRepository.save(anteproject);
        logger.info("Estado actualizado para anteproyecto " + anteprojectId + ": " + status);
        return convertToDTO(updatedAnteproject);
    }

    public List<AnteprojectDTO> getSubmittedAnteprojectsForDepartmentHead() {
        List<Anteproject> anteprojects = anteprojectRepository.findByStatusIn(
            List.of(AnteprojectStatus.SUBMITTED, AnteprojectStatus.DRAFT)
        );
        return anteprojects.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public AnteprojectDTO assignEvaluatorsToAnteproject(Long anteprojectId, String evaluator1Email,
                                                          String evaluator2Email, String assignedBy) {
        logger.info("Asignando evaluadores para anteproyecto: " + anteprojectId);
        Anteproject anteproject = anteprojectRepository.findById(anteprojectId)
                .orElseThrow(() -> new RuntimeException("Anteproyecto no encontrado"));

        // Permitir DRAFT o SUBMITTED para facilitar pruebas
        if (anteproject.getStatus() != AnteprojectStatus.SUBMITTED && anteproject.getStatus() != AnteprojectStatus.DRAFT) {
            throw new RuntimeException("El anteproyecto debe estar en estado ENVIADO o BORRADOR para asignar evaluadores");
        }

        if (evaluator1Email.equals(evaluator2Email)) {
            throw new RuntimeException("Los evaluadores deben ser diferentes");
        }

        try {
            evaluationServiceClient.assignEvaluators(anteprojectId, evaluator1Email, evaluator2Email);
        } catch (Exception e) {
            logger.severe("Error llamando a evaluation-service: " + e.getMessage());
            throw new RuntimeException("Error asignando evaluadores en el servicio de evaluación: " + e.getMessage());
        }

        anteproject.setStatus(AnteprojectStatus.UNDER_EVALUATION);
        Anteproject updatedAnteproject = anteprojectRepository.save(anteproject);

        EvaluatorAssignmentEvent event = new EvaluatorAssignmentEvent(
                anteproject.getId(),
                anteproject.getTitulo(),
                evaluator1Email,
                evaluator2Email,
                anteproject.getDirectorEmail(),
                anteproject.getStudentEmail()
        );
        eventPublisher.publishEvaluatorAssignment(event);

        AnteprojectDTO dto = convertToDTO(updatedAnteproject);
        dto.setDocumentUrl(anteproject.getDocumentUrl());
        dto.setStatus(anteproject.getStatus());
        dto.setSubmissionDate(anteproject.getSubmissionDate());
        dto.setEvaluationDeadline(anteproject.getEvaluationDeadline());
        dto.setCreatedAt(anteproject.getCreatedAt());

        List<ProgressUpdateDTO> progressDTOs = progressRepository
                .findByAnteprojectIdOrderByCreatedAtDesc(anteproject.getId())
                .stream()
                .map(this::convertProgressToDTO)
                .collect(Collectors.toList());
        dto.setProgressUpdates(progressDTOs);

        return dto;
    }

    private ProgressUpdateDTO convertProgressToDTO(ProjectProgress progress) {
        ProgressUpdateDTO dto = new ProgressUpdateDTO();
        dto.setId(progress.getId());
        dto.setDescription(progress.getDescription());
        dto.setProgressPercentage(progress.getProgressPercentage());
        dto.setCreatedBy(progress.getCreatedBy());
        dto.setCreatedAt(progress.getCreatedAt());
        return dto;
    }

    private AnteprojectDTO convertToDTO(Anteproject anteproject) {
        AnteprojectDTO dto = new AnteprojectDTO();
        dto.setId(anteproject.getId());
        dto.setFormatoAId(anteproject.getFormatoAId());
        dto.setTitulo(anteproject.getTitulo());
        dto.setStudentEmail(anteproject.getStudentEmail());
        dto.setDirectorEmail(anteproject.getDirectorEmail());
        dto.setDocumentUrl(anteproject.getDocumentUrl());
        dto.setStatus(anteproject.getStatus());
        dto.setSubmissionDate(anteproject.getSubmissionDate());
        dto.setEvaluationDeadline(anteproject.getEvaluationDeadline());
        dto.setCreatedAt(anteproject.getCreatedAt());
        return dto;
    }
}