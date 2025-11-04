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
    
    /**
     * Crear un nuevo anteproyecto basado en un Formato A aprobado
     */
    public AnteprojectDTO createAnteproject(CreateAnteprojectRequest request) {
        logger.info("Creando anteproyecto para Formato A: " + request.getFormatoAId());
        
        // Verificar si ya existe un anteproyecto para este Formato A
        if (anteprojectRepository.findByFormatoAId(request.getFormatoAId()).isPresent()) {
            throw new RuntimeException("Ya existe un anteproyecto para este Formato A");
        }
        
        // Verificar que el Formato A existe y está aprobado
        validateFormatoA(request.getFormatoAId());
        
        Anteproject anteproject = new Anteproject(
            request.getFormatoAId(),
            request.getTitulo(),
            request.getStudentEmail(),
            request.getDirectorEmail()
        );
        
        Anteproject savedAnteproject = anteprojectRepository.save(anteproject);
        
        logger.info("Anteproyecto creado exitosamente: " + savedAnteproject.getId());
        return convertToDTO(savedAnteproject);
    }
    
    /**
     * Subir documento de anteproyecto
     */
    public AnteprojectDTO submitDocument(Long anteprojectId, String documentUrl, String submittedBy) {
        logger.info("Subiendo documento para anteproyecto: " + anteprojectId);
        
        Anteproject anteproject = anteprojectRepository.findById(anteprojectId)
            .orElseThrow(() -> new RuntimeException("Anteproyecto no encontrado"));
        
        // Verificar que el usuario tiene permisos
        if (!anteproject.getStudentEmail().equals(submittedBy) && 
            !anteproject.getDirectorEmail().equals(submittedBy)) {
            throw new RuntimeException("No tiene permisos para subir documentos de este anteproyecto");
        }
        
        anteproject.setDocumentUrl(documentUrl);
        anteproject.setStatus(AnteprojectStatus.SUBMITTED);
        anteproject.setSubmissionDate(LocalDateTime.now());
        
        Anteproject updatedAnteproject = anteprojectRepository.save(anteproject);
        
        // Asignar evaluadores automáticamente
        assignEvaluators(updatedAnteproject);
        
        logger.info("Documento subido exitosamente para anteproyecto: " + anteprojectId);
        return convertToDTO(updatedAnteproject);
    }
    
    /**
     * Agregar actualización de progreso
     */
    public ProgressUpdateDTO addProgressUpdate(Long anteprojectId, String description, 
                                             Integer progressPercentage, String createdBy) {
        logger.info("Agregando actualización de progreso para anteproyecto: " + anteprojectId);
        
        Anteproject anteproject = anteprojectRepository.findById(anteprojectId)
            .orElseThrow(() -> new RuntimeException("Anteproyecto no encontrado"));
        
        // Verificar permisos
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
    
    /**
     * Obtener anteproyecto por ID
     */
    public AnteprojectDTO getAnteprojectById(Long id) {
        Anteproject anteproject = anteprojectRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Anteproyecto no encontrado"));
        return convertToDTO(anteproject);
    }
    
    /**
     * Obtener anteproyectos por estudiante
     */
    public List<AnteprojectDTO> getAnteprojectsByStudent(String studentEmail) {
        List<Anteproject> anteprojects = anteprojectRepository.findByStudentEmail(studentEmail);
        return anteprojects.stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }
    
    /**
     * Obtener anteproyectos por director
     */
    public List<AnteprojectDTO> getAnteprojectsByDirector(String directorEmail) {
        List<Anteproject> anteprojects = anteprojectRepository.findByDirectorEmail(directorEmail);
        return anteprojects.stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }
    
    /**
     * Actualizar estado del anteproyecto
     */
    public AnteprojectDTO updateStatus(Long anteprojectId, AnteprojectStatus status, String updatedBy) {
        Anteproject anteproject = anteprojectRepository.findById(anteprojectId)
            .orElseThrow(() -> new RuntimeException("Anteproyecto no encontrado"));
        
        anteproject.setStatus(status);
        Anteproject updatedAnteproject = anteprojectRepository.save(anteproject);
        
        logger.info("Estado actualizado para anteproyecto " + anteprojectId + ": " + status);
        return convertToDTO(updatedAnteproject);
    }
    
    /**
     * Validar que el Formato A existe y está aprobado
     */
    private void validateFormatoA(Long formatoAId) {
        try {
            // Llamar al format-a-service para validar
            Object formatoA = formatAServiceClient.getFormatoAById(formatoAId);
            if (formatoA == null) {
                throw new RuntimeException("Formato A no encontrado");
            }
            // En una implementación real, verificarías el estado del Formato A
        } catch (Exception e) {
            throw new RuntimeException("Error validando Formato A: " + e.getMessage());
        }
    }
    
    /**
     * Asignar evaluadores automáticamente
     */
    private void assignEvaluators(Anteproject anteproject) {
        try {
            // Encontrar evaluadores diferentes
            String evaluator1 = findEvaluator(anteproject);
            String evaluator2 = findSecondEvaluator(anteproject, evaluator1);

            // Usar el DTO específico
            EvaluationAssignmentRequest request = new EvaluationAssignmentRequest(
                anteproject.getId(),
                evaluator1,
                evaluator2,
                "sistema_anteproyecto"
            );

            evaluationServiceClient.assignEvaluators(request);
            logger.info("Evaluadores asignados para anteproyecto " + anteproject.getId() + 
                       ": " + evaluator1 + " y " + evaluator2);

        } catch (Exception e) {
            logger.severe("❌ Error asignando evaluadores para anteproyecto " + anteproject.getId() + ": " + e.getMessage());
            // Para debug, imprime el stack trace completo
            e.printStackTrace();
        }
    }
    
    /**
     * Encontrar evaluadores diferentes (lógica mejorada)
     */
    private String findEvaluator(Anteproject anteproject) {
        // En implementación real, buscarías evaluadores de una base de datos
        // Por ahora, usamos lógica simple para retornar evaluadores diferentes

        // Lista de evaluadores de ejemplo (en producción vendría de una base de datos)
        String[] evaluadores = {
            "evaluador1@unicauca.edu.co",
            "evaluador2@unicauca.edu.co", 
            "evaluador3@unicauca.edu.co",
            "evaluador4@unicauca.edu.co"
        };

        // Usar el ID del anteproyecto para seleccionar diferentes evaluadores
        int index = (int) (anteproject.getId() % evaluadores.length);
        return evaluadores[index];
    }
    /**
    * Encontrar segundo evaluador diferente
    */
   private String findSecondEvaluator(Anteproject anteproject, String firstEvaluator) {
       String[] evaluadores = {
           "evaluador1@unicauca.edu.co",
           "evaluador2@unicauca.edu.co",
           "evaluador3@unicauca.edu.co", 
           "evaluador4@unicauca.edu.co"
       };

       // Buscar un evaluador diferente al primero
       for (String evaluador : evaluadores) {
           if (!evaluador.equals(firstEvaluator)) {
               return evaluador;
           }
       }

       // Fallback: retornar un evaluador diferente
       return "evaluador.backup@unicauca.edu.co";
   }
    
    /**
     * Convertir Anteproject a DTO
     */
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
        
        // Cargar actualizaciones de progreso
        List<ProgressUpdateDTO> progressDTOs = progressRepository
            .findByAnteprojectIdOrderByCreatedAtDesc(anteproject.getId())
            .stream()
            .map(this::convertProgressToDTO)
            .collect(Collectors.toList());
        dto.setProgressUpdates(progressDTOs);
        
        return dto;
    }
    
    /**
     * Convertir ProjectProgress a DTO
     */
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