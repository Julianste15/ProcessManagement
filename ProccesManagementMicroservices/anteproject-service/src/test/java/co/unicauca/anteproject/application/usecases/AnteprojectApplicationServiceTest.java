package co.unicauca.anteproject.application.usecases;

import co.unicauca.anteproject.client.FormatADTO;
import co.unicauca.anteproject.client.FormatAServiceClient;
import co.unicauca.anteproject.domain.model.Anteproject;
import co.unicauca.anteproject.domain.model.AnteprojectStatus;
import co.unicauca.anteproject.domain.ports.out.AnteprojectEventPublisherPort;
import co.unicauca.anteproject.domain.ports.out.AnteprojectRepositoryPort;
import co.unicauca.anteproject.domain.ports.out.EvaluationServicePort;
import co.unicauca.anteproject.dto.CreateAnteprojectRequest;
import co.unicauca.anteproject.events.AnteprojectSubmittedEvent;
import co.unicauca.anteproject.events.EvaluatorAssignmentEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AnteprojectApplicationServiceTest {

    @Mock
    private AnteprojectRepositoryPort anteprojectRepository;

    @Mock
    private EvaluationServicePort evaluationService;

    @Mock
    private AnteprojectEventPublisherPort eventPublisher;

    @Mock
    private FormatAServiceClient formatAServiceClient;

    @InjectMocks
    private AnteprojectApplicationService anteprojectService;

    private CreateAnteprojectRequest createRequest;
    private FormatADTO formatADTO;
    private Anteproject anteproject;

    @BeforeEach
    void setUp() {
        createRequest = new CreateAnteprojectRequest();
        createRequest.setFormatoAId(1L);
        createRequest.setTitulo("Test Project");
        createRequest.setDirectorEmail("director@test.com");

        formatADTO = new FormatADTO();
        formatADTO.setId(1L);
        formatADTO.setStudentEmail("student@test.com");
        formatADTO.setDirectorEmail("director@test.com");

        anteproject = new Anteproject();
        anteproject.setId(100L);
        anteproject.setFormatoAId(1L);
        anteproject.setStudentEmail("student@test.com");
        anteproject.setDirectorEmail("director@test.com");
        anteproject.setTitulo("Test Project");
        anteproject.setStatus(AnteprojectStatus.DRAFT);
    }

    @Test
    void createAnteproject_Success() {
        when(anteprojectRepository.findByFormatoAId(anyLong())).thenReturn(Optional.empty());
        when(formatAServiceClient.getFormatoAById(anyLong())).thenReturn(formatADTO);
        when(anteprojectRepository.save(any(Anteproject.class))).thenReturn(anteproject);

        Anteproject result = anteprojectService.createAnteproject(createRequest);

        assertNotNull(result);
        assertEquals(anteproject.getId(), result.getId());
        assertEquals("student@test.com", result.getStudentEmail());
        verify(anteprojectRepository).save(any(Anteproject.class));
    }

    @Test
    void createAnteproject_AlreadyExists_ThrowsException() {
        when(anteprojectRepository.findByFormatoAId(anyLong())).thenReturn(Optional.of(anteproject));

        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                anteprojectService.createAnteproject(createRequest));
        assertEquals("Ya existe un anteproyecto para este Formato A", exception.getMessage());
        verify(anteprojectRepository, never()).save(any(Anteproject.class));
    }

    @Test
    void createAnteproject_DirectorMismatch_ThrowsException() {
        when(anteprojectRepository.findByFormatoAId(anyLong())).thenReturn(Optional.empty());
        
        FormatADTO differentDirectorFormat = new FormatADTO();
        differentDirectorFormat.setStudentEmail("student@test.com");
        differentDirectorFormat.setDirectorEmail("other@test.com");
        
        when(formatAServiceClient.getFormatoAById(anyLong())).thenReturn(differentDirectorFormat);

        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                anteprojectService.createAnteproject(createRequest));
        assertTrue(exception.getMessage().contains("El director especificado no coincide"));
        verify(anteprojectRepository, never()).save(any(Anteproject.class));
    }

    @Test
    void submitDocument_AsStudent_Success() {
        when(anteprojectRepository.findById(anyLong())).thenReturn(Optional.of(anteproject));
        when(anteprojectRepository.save(any(Anteproject.class))).thenAnswer(i -> i.getArguments()[0]);

        String docUrl = "http://storage/doc.pdf";

        Anteproject result = anteprojectService.submitDocument(100L, docUrl, "student@test.com");

        assertEquals(AnteprojectStatus.SUBMITTED, result.getStatus());
        assertEquals(docUrl, result.getDocumentUrl());
        assertNotNull(result.getSubmissionDate());
        verify(eventPublisher).publishAnteprojectSubmitted(any(AnteprojectSubmittedEvent.class));
    }

    @Test
    void submitDocument_UnauthorizedUser_ThrowsException() {
        when(anteprojectRepository.findById(anyLong())).thenReturn(Optional.of(anteproject));

        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                anteprojectService.submitDocument(100L, "http://url", "hacker@test.com"));
        assertEquals("No tiene permisos para subir documentos de este anteproyecto", exception.getMessage());
        verify(eventPublisher, never()).publishAnteprojectSubmitted(any());
    }

    @Test
    void assignEvaluators_Success() {
        anteproject.setStatus(AnteprojectStatus.SUBMITTED);
        when(anteprojectRepository.findById(anyLong())).thenReturn(Optional.of(anteproject));
        when(anteprojectRepository.save(any(Anteproject.class))).thenAnswer(i -> i.getArguments()[0]);
        doNothing().when(evaluationService).assignEvaluators(anyLong(), anyString(), anyString());

        Anteproject result = anteprojectService.assignEvaluators(100L, "eval1@test.com", "eval2@test.com", "admin");

        assertEquals(AnteprojectStatus.UNDER_EVALUATION, result.getStatus());
        verify(evaluationService).assignEvaluators(100L, "eval1@test.com", "eval2@test.com");
        verify(eventPublisher).publishEvaluatorAssignment(any(EvaluatorAssignmentEvent.class));
    }

    @Test
    void assignEvaluators_SameEvaluator_ThrowsException() {
        anteproject.setStatus(AnteprojectStatus.SUBMITTED);
        when(anteprojectRepository.findById(anyLong())).thenReturn(Optional.of(anteproject));

        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                anteprojectService.assignEvaluators(100L, "same@test.com", "same@test.com", "admin"));
        assertEquals("Los evaluadores deben ser diferentes", exception.getMessage());
        verify(evaluationService, never()).assignEvaluators(anyLong(), anyString(), anyString());
    }

    @Test
    void getSubmittedAnteprojectsForDepartmentHead_Success() {
        when(anteprojectRepository.findByStatusIn(anyList())).thenReturn(List.of(anteproject));

        List<Anteproject> results = anteprojectService.getSubmittedAnteprojectsForDepartmentHead();

        assertFalse(results.isEmpty());
        assertEquals(1, results.size());
        verify(anteprojectRepository).findByStatusIn(anyList());
    }

    @Test
    void getAnteprojectById_Success() {
        when(anteprojectRepository.findById(100L)).thenReturn(Optional.of(anteproject));

        Anteproject result = anteprojectService.getAnteprojectById(100L);

        assertNotNull(result);
        assertEquals(100L, result.getId());
    }

    @Test
    void getAnteprojectById_NotFound_ThrowsException() {
        when(anteprojectRepository.findById(999L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                anteprojectService.getAnteprojectById(999L));
        assertEquals("Anteproyecto no encontrado", exception.getMessage());
    }

    @Test
    void updateStatus_Success() {
        when(anteprojectRepository.findById(100L)).thenReturn(Optional.of(anteproject));
        when(anteprojectRepository.save(any(Anteproject.class))).thenAnswer(i -> i.getArguments()[0]);

        Anteproject result = anteprojectService.updateStatus(100L, AnteprojectStatus.APPROVED, "admin");

        assertEquals(AnteprojectStatus.APPROVED, result.getStatus());
        verify(anteprojectRepository).save(anteproject);
    }

    @Test
    void createAnteproject_FormatANoStudent_ThrowsException() {
        when(anteprojectRepository.findByFormatoAId(anyLong())).thenReturn(Optional.empty());
        
        FormatADTO noStudentFormat = new FormatADTO();
        noStudentFormat.setStudentEmail(null);
        
        when(formatAServiceClient.getFormatoAById(anyLong())).thenReturn(noStudentFormat);

        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                anteprojectService.createAnteproject(createRequest));
        assertTrue(exception.getMessage().contains("El Formato A no tiene un estudiante asignado"));
    }

    @Test
    void simulateConcurrentUsage_ShouldHandleMultipleUsers() throws InterruptedException {
        int numberOfUsers = 100; // Increased to 100 as requested
        java.util.concurrent.ExecutorService executorService = java.util.concurrent.Executors.newFixedThreadPool(20); // Increased thread pool size
        java.util.concurrent.CountDownLatch latch = new java.util.concurrent.CountDownLatch(numberOfUsers);
        java.util.concurrent.atomic.AtomicInteger successCount = new java.util.concurrent.atomic.AtomicInteger(0);
        java.util.concurrent.atomic.AtomicInteger errorCount = new java.util.concurrent.atomic.AtomicInteger(0);

        when(anteprojectRepository.findByStudentEmail(anyString())).thenReturn(List.of(anteproject));

        long startTime = System.currentTimeMillis();

        for (int i = 0; i < numberOfUsers; i++) {
            final String userEmail = "student" + i + "@unicauca.edu.co";
            executorService.submit(() -> {
                try {
                    Thread.sleep((long) (Math.random() * 50)); // Slightly increased random delay
                    List<Anteproject> projects = anteprojectService.getAnteprojectsByStudent(userEmail);
                    if (projects != null && !projects.isEmpty()) {
                        successCount.incrementAndGet();
                    } else {
                        errorCount.incrementAndGet();
                    }
                } catch (Exception e) {
                    errorCount.incrementAndGet();
                } finally {
                    latch.countDown();
                }
            });
        }

        boolean completed = latch.await(10, java.util.concurrent.TimeUnit.SECONDS);
        executorService.shutdown();
        
        long duration = System.currentTimeMillis() - startTime;
        System.out.println("Concurrent test with " + numberOfUsers + " users completed in " + duration + "ms");

        assertTrue(completed, "Timeout: Not all simulated requests completed in time");
        assertEquals(numberOfUsers, successCount.get(), "All concurrent requests should succeed");
        assertEquals(0, errorCount.get(), "There should be no errors during concurrent execution");
        verify(anteprojectRepository, times(numberOfUsers)).findByStudentEmail(anyString());
    }
}
