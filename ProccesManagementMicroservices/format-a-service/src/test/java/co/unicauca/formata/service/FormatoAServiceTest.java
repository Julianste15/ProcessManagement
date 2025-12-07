package co.unicauca.formata.service;

import co.unicauca.formata.client.EvaluationServiceClient;
import co.unicauca.formata.dto.FormatARequest;
import co.unicauca.formata.events.FormatAEventPublisher;
import co.unicauca.formata.model.FormatoA;
import co.unicauca.formata.model.Modalidad;
import co.unicauca.formata.repository.FormatARepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;

import java.util.Base64;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class FormatoAServiceTest {

    @Mock
    private FormatARepository formatoARepository;

    @Mock
    private FormatAEventPublisher eventPublisher;

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private EvaluationServiceClient evaluationServiceClient;

    @InjectMocks
    private FormatoAService formatoAService;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(formatoAService, "userServiceUrl", "http://user-service/api/users");
        ReflectionTestUtils.setField(formatoAService, "storagePath", "./target/storage/formata");
        // Set limit to 10MB as per new config
        ReflectionTestUtils.setField(formatoAService, "maxPdfSizeBytes", 10485760L);
    }

    @Test
    void submitFormatoA_WithValidPdf_ShouldSuccess() throws Exception {
        // Arrange
        FormatARequest request = new FormatARequest();
        request.setTitulo("Test Project");
        request.setModalidad(Modalidad.INVESTIGACION);
        request.setDirectorEmail("director@test.com");
        request.setObjetivoGeneral("General Objective");
        request.setArchivoPdfNombre("test.pdf");
        // Small PDF content
        request.setArchivoPdfContenido(Base64.getEncoder().encodeToString("dummy content".getBytes()));

        when(restTemplate.getForObject(anyString(), any())).thenReturn(new Object());
        when(formatoARepository.save(any(FormatoA.class))).thenAnswer(invocation -> {
            FormatoA f = invocation.getArgument(0);
            f.setId(1L);
            return f;
        });

        // Act
        var response = formatoAService.submitFormatoA(request);

        // Assert
        assertNotNull(response);
        assertEquals("Test Project", response.getTitulo());
    }

    @Test
    void submitFormatoA_WithLargePdf_ShouldSuccess() throws Exception {
        // Arrange
        FormatARequest request = new FormatARequest();
        request.setTitulo("Large Project");
        request.setModalidad(Modalidad.INVESTIGACION);
        request.setDirectorEmail("director@test.com");
        request.setStudentEmail("student@test.com");
        request.setObjetivoGeneral("General Objective");
        request.setArchivoPdfNombre("large.pdf");

        // Create 6MB dummy content (should now succeed with 10MB limit)
        byte[] largeContent = new byte[6 * 1024 * 1024];
        request.setArchivoPdfContenido(Base64.getEncoder().encodeToString(largeContent));

        when(restTemplate.getForObject(anyString(), any())).thenReturn(new Object());
        when(formatoARepository.save(any(FormatoA.class))).thenAnswer(invocation -> {
            FormatoA f = invocation.getArgument(0);
            f.setId(2L);
            return f;
        });

        // Act
        var response = formatoAService.submitFormatoA(request);

        // Assert
        assertNotNull(response);
        assertEquals("Large Project", response.getTitulo());
    }

    @Test
    void submitFormatoA_WithTooLargePdf_ThrowsException() {
        // Arrange
        FormatARequest request = new FormatARequest();
        request.setTitulo("Too Large Project");
        request.setModalidad(Modalidad.INVESTIGACION);
        request.setDirectorEmail("director@test.com");
        request.setStudentEmail("student@test.com");
        request.setObjetivoGeneral("General Objective");
        request.setArchivoPdfNombre("huge.pdf");

        // Create 11MB dummy content (over 10MB limit)
        byte[] hugeContent = new byte[11 * 1024 * 1024];
        request.setArchivoPdfContenido(Base64.getEncoder().encodeToString(hugeContent));

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> 
            formatoAService.submitFormatoA(request));
        
        assertTrue(exception.getMessage().contains("El archivo PDF supera el tamaño máximo permitido"));
    }

    @Test
    void simulateConcurrentSubmissions_ShouldHandleLoad() throws InterruptedException {
        int numberOfUsers = 100;
        java.util.concurrent.ExecutorService executorService = java.util.concurrent.Executors.newFixedThreadPool(20);
        java.util.concurrent.CountDownLatch latch = new java.util.concurrent.CountDownLatch(numberOfUsers);
        java.util.concurrent.atomic.AtomicInteger successCount = new java.util.concurrent.atomic.AtomicInteger(0);
        java.util.concurrent.atomic.AtomicInteger errorCount = new java.util.concurrent.atomic.AtomicInteger(0);

        // Mock setup for concurrent use
        when(restTemplate.getForObject(anyString(), any())).thenReturn(new Object());
        when(formatoARepository.save(any(FormatoA.class))).thenAnswer(invocation -> {
            FormatoA f = invocation.getArgument(0);
            f.setId((long) (Math.random() * 1000));
            return f;
        });

        long startTime = System.currentTimeMillis();

        for (int i = 0; i < numberOfUsers; i++) {
            final int index = i;
            executorService.submit(() -> {
                try {
                    FormatARequest request = new FormatARequest();
                    request.setTitulo("Project " + index);
                    request.setModalidad(Modalidad.INVESTIGACION);
                    request.setDirectorEmail("director" + index + "@test.com");
                    request.setStudentEmail("student" + index + "@test.com");
                    request.setObjetivoGeneral("Objective " + index);
                    request.setArchivoPdfNombre("doc" + index + ".pdf");
                    request.setArchivoPdfContenido(Base64.getEncoder().encodeToString("content".getBytes()));

                    formatoAService.submitFormatoA(request);
                    successCount.incrementAndGet();
                } catch (Exception e) {
                    errorCount.incrementAndGet();
                } finally {
                    latch.countDown();
                }
            });
        }

        boolean completed = latch.await(20, java.util.concurrent.TimeUnit.SECONDS);
        executorService.shutdown();

        long duration = System.currentTimeMillis() - startTime;
        System.out.println("FormatoA Concurrent Load Test: " + numberOfUsers + " submissions processed in " + duration + "ms");

        assertTrue(completed, "Timeout during load test");
        assertEquals(numberOfUsers, successCount.get(), "All submissions should succeed");
        assertEquals(0, errorCount.get(), "There should be no errors");
    }
}
