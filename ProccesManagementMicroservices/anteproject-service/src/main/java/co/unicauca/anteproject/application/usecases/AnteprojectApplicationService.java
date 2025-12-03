package co.unicauca.anteproject.application.usecases;

import co.unicauca.anteproject.client.FormatADTO;
import co.unicauca.anteproject.client.FormatAServiceClient;
import co.unicauca.anteproject.domain.model.Anteproject;
import co.unicauca.anteproject.domain.model.AnteprojectStatus;
import co.unicauca.anteproject.domain.ports.in.CreateAnteprojectUseCase;
import co.unicauca.anteproject.domain.ports.in.GetAnteprojectUseCase;
import co.unicauca.anteproject.domain.ports.in.ManageAnteprojectUseCase;
import co.unicauca.anteproject.domain.ports.out.AnteprojectEventPublisherPort;
import co.unicauca.anteproject.domain.ports.out.AnteprojectRepositoryPort;
import co.unicauca.anteproject.domain.ports.out.EvaluationServicePort;
import co.unicauca.anteproject.dto.CreateAnteprojectRequest;
import co.unicauca.anteproject.events.AnteprojectSubmittedEvent;
import co.unicauca.anteproject.events.EvaluatorAssignmentEvent;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class AnteprojectApplicationService implements CreateAnteprojectUseCase, ManageAnteprojectUseCase, GetAnteprojectUseCase {

    private final AnteprojectRepositoryPort anteprojectRepository;
    private final EvaluationServicePort evaluationService;
    private final AnteprojectEventPublisherPort eventPublisher;
    private final FormatAServiceClient formatAServiceClient;

    public AnteprojectApplicationService(AnteprojectRepositoryPort anteprojectRepository,
                                         EvaluationServicePort evaluationService,
                                         AnteprojectEventPublisherPort eventPublisher,
                                         FormatAServiceClient formatAServiceClient) {
        this.anteprojectRepository = anteprojectRepository;
        this.evaluationService = evaluationService;
        this.eventPublisher = eventPublisher;
        this.formatAServiceClient = formatAServiceClient;
    }

    @Override
    @Transactional
    public Anteproject createAnteproject(CreateAnteprojectRequest request) {
        // Validar que no exista un anteproyecto para este Formato A
        if (anteprojectRepository.findByFormatoAId(request.getFormatoAId()).isPresent()) {
            throw new RuntimeException("Ya existe un anteproyecto para este Formato A");
        }
        
        // Obtener el Formato A para extraer el studentEmail automáticamente
        FormatADTO formatA;
        try {
            formatA = formatAServiceClient.getFormatoAById(request.getFormatoAId());
        } catch (Exception e) {
            throw new RuntimeException("No se pudo obtener el Formato A con ID: " + request.getFormatoAId() + ". Error: " + e.getMessage());
        }
        
        // Validar que el Formato A tenga un estudiante asignado
        if (formatA.getStudentEmail() == null || formatA.getStudentEmail().trim().isEmpty()) {
            throw new RuntimeException("El Formato A no tiene un estudiante asignado");
        }
        
        // Validar que el director del request coincida con el del Formato A
        if (!request.getDirectorEmail().equals(formatA.getDirectorEmail())) {
            throw new RuntimeException("El director especificado no coincide con el director del Formato A");
        }
        
        Anteproject anteproject = new Anteproject();
        anteproject.setFormatoAId(request.getFormatoAId());
        anteproject.setTitulo(request.getTitulo());
        anteproject.setStudentEmail(formatA.getStudentEmail()); // Obtenido automáticamente del Formato A
        anteproject.setDirectorEmail(request.getDirectorEmail());
        
        return anteprojectRepository.save(anteproject);
    }

    @Override
    @Transactional
    public Anteproject submitDocument(Long anteprojectId, String documentUrl, String submittedBy) {
        Anteproject anteproject = anteprojectRepository.findById(anteprojectId)
                .orElseThrow(() -> new RuntimeException("Anteproyecto no encontrado"));

        boolean isStudent = anteproject.getStudentEmail().equals(submittedBy);
        boolean isDirector = anteproject.getDirectorEmail().equals(submittedBy);

        if (!isStudent && !isDirector) {
            throw new RuntimeException("No tiene permisos para subir documentos de este anteproyecto");
        }

        anteproject.setDocumentUrl(documentUrl);
        anteproject.setStatus(AnteprojectStatus.SUBMITTED);
        anteproject.setSubmissionDate(LocalDateTime.now());

        Anteproject updatedAnteproject = anteprojectRepository.save(anteproject);

        AnteprojectSubmittedEvent event = new AnteprojectSubmittedEvent(
                updatedAnteproject.getId(),
                updatedAnteproject.getStudentEmail(),
                updatedAnteproject.getSubmissionDate(),
                updatedAnteproject.getDocumentUrl()
        );
        eventPublisher.publishAnteprojectSubmitted(event);

        return updatedAnteproject;
    }

    @Override
    @Transactional
    public Anteproject assignEvaluators(Long anteprojectId, String evaluator1Email, String evaluator2Email, String assignedBy) {
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
            evaluationService.assignEvaluators(anteprojectId, evaluator1Email, evaluator2Email);
        } catch (Exception e) {
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

        return updatedAnteproject;
    }

    @Override
    @Transactional
    public Anteproject updateStatus(Long anteprojectId, AnteprojectStatus status, String updatedBy) {
        Anteproject anteproject = anteprojectRepository.findById(anteprojectId)
                .orElseThrow(() -> new RuntimeException("Anteproyecto no encontrado"));
        anteproject.setStatus(status);
        return anteprojectRepository.save(anteproject);
    }

    @Override
    public Anteproject getAnteprojectById(Long id) {
        return anteprojectRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Anteproyecto no encontrado"));
    }

    @Override
    public List<Anteproject> getAnteprojectsByStudent(String studentEmail) {
        return anteprojectRepository.findByStudentEmail(studentEmail);
    }

    @Override
    public List<Anteproject> getAnteprojectsByDirector(String directorEmail) {
        return anteprojectRepository.findByDirectorEmail(directorEmail);
    }

    @Override
    public List<Anteproject> getSubmittedAnteprojectsForDepartmentHead() {
        return anteprojectRepository.findByStatusIn(
            List.of(AnteprojectStatus.SUBMITTED, AnteprojectStatus.DRAFT)
        );
    }
}
