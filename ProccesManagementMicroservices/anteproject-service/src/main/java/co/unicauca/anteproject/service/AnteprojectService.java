package co.unicauca.anteproject.service;

import co.unicauca.anteproject.client.EvaluationServiceClient;
import co.unicauca.anteproject.client.FormatAServiceClient;
import co.unicauca.anteproject.dto.AnteprojectDTO;
import co.unicauca.anteproject.dto.CreateAnteprojectRequest;
import co.unicauca.anteproject.dto.EvaluationAssignmentRequest;
import co.unicauca.anteproject.dto.ProgressUpdateDTO;
import co.unicauca.anteproject.model.Anteproject;
import co.unicauca.anteproject.model.AnteprojectStatus;
import co.unicauca.anteproject.model.ProjectProgress;
import co.unicauca.anteproject.repository.AnteprojectRepository;
import co.unicauca.anteproject.repository.ProjectProgressRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.logging.Logger;

@Service
public class AnteprojectService {
    private static final Logger logger = Logger.getLogger(AnteprojectService.class.getName());
    @Autowired
    private AnteprojectRepository anteprojectRepository;
    @Autowired
    private ProjectProgressRepository progressRepository;
    @Autowired
    private FormatAServiceClient formatAServiceClient;
    @Autowired
    private EvaluationServiceClient evaluationServiceClient;

    public AnteprojectDTO createAnteproject(CreateAnteprojectRequest request) {
        logger.info("Creando anteproyecto para Formato A: " + request.getFormatoAId());
        if (anteprojectRepository.findByFormatoAId(request.getFormatoAId()).isPresent()) {
            throw new RuntimeException("Ya existe un anteproyecto para este Formato A");
        }
        validateFormatoA(request.getFormatoAId());
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
        logger.info("Subiendo documento para anteproyecto: " + anteprojectId);
        Anteproject anteproject = anteprojectRepository.findById(anteprojectId)
                .orElseThrow(() -> new RuntimeException("Anteproyecto no encontrado"));
        if (!anteproject.getStudentEmail().equals(submittedBy) &&
                !anteproject.getDirectorEmail().equals(submittedBy)) {
            throw new RuntimeException("No tiene permisos para subir documentos de este anteproyecto");
        }
        anteproject.setDocumentUrl(documentUrl);
        anteproject.setStatus(AnteprojectStatus.SUBMITTED);
        anteproject.setSubmissionDate(LocalDateTime.now());
        Anteproject updatedAnteproject = anteprojectRepository.save(anteproject);
        assignEvaluators(updatedAnteproject);
        logger.info("Documento subido exitosamente para anteproyecto: " + anteprojectId);
        return convertToDTO(updatedAnteproject);
    }

    public ProgressUpdateDTO addProgressUpdate(Long anteprojectId, String description,
            Integer progressPercentage, String createdBy) {
        logger.info("Agregando actualización de progreso para anteproyecto: " + anteprojectId);
        Anteproject anteproject = anteprojectRepository.findById(anteprojectId)
                .orElseThrow(() -> new RuntimeException("Anteproyecto no encontrado"));
        if (!anteproject.getStudentEmail().equals(createdBy) &&
                !anteproject.getDirectorEmail().equals(createdBy)) {
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
        List<Anteproject> anteprojects = anteprojectRepository.findByStatus(AnteprojectStatus.SUBMITTED);
        return anteprojects.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    private void validateFormatoA(Long formatoAId) {
        try {
            Object formatoA = formatAServiceClient.getFormatoAById(formatoAId);
            if (formatoA == null) {
                throw new RuntimeException("Formato A no encontrado");
            }
        } catch (Exception e) {
            throw new RuntimeException("Error validando Formato A: " + e.getMessage());
        }
    }

    private void assignEvaluators(Anteproject anteproject) {
        try {
            String evaluator1 = findEvaluator(anteproject);
            String evaluator2 = findSecondEvaluator(anteproject, evaluator1);
            EvaluationAssignmentRequest request = new EvaluationAssignmentRequest(
                    anteproject.getId(),
                    evaluator1,
                    evaluator2,
                    "sistema_anteproyecto");
            evaluationServiceClient.assignEvaluators(request);
            logger.info("Evaluadores asignados para anteproyecto " + anteproject.getId() + ": " + evaluator1 + " y "
                    + evaluator2);
        } catch (Exception e) {
            logger.severe(
                    "Error asignando evaluadores para anteproyecto " + anteproject.getId() + ": " + e.getMessage());
            e.printStackTrace();
        }
    }

    private String findEvaluator(Anteproject anteproject) {
        String[] evaluadores = {
                "evaluador1@unicauca.edu.co",
                "evaluador2@unicauca.edu.co",
                "evaluador3@unicauca.edu.co",
                "evaluador4@unicauca.edu.co"
        };
        int index = (int) (anteproject.getId() % evaluadores.length);
        return evaluadores[index];
    }

    private String findSecondEvaluator(Anteproject anteproject, String firstEvaluator) {
        String[] evaluadores = {
                "evaluador1@unicauca.edu.co",
                "evaluador2@unicauca.edu.co",
                "evaluador3@unicauca.edu.co",
                "evaluador4@unicauca.edu.co"
        };
        for (String evaluador : evaluadores) {
            if (!evaluador.equals(firstEvaluator)) {
                return evaluador;
            }
        }
        return "evaluador.backup@unicauca.edu.co";
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
}