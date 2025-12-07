package co.unicauca.evaluation.service;

import co.unicauca.evaluation.model.Evaluation;
import co.unicauca.evaluation.model.EvaluationStatus;
import co.unicauca.evaluation.repository.EvaluationRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EvaluationServiceTest {

    @Mock
    private EvaluationRepository evaluationRepository;

    @InjectMocks
    private EvaluationService evaluationService;

    @Test
    void createEvaluation_Success() {
        when(evaluationRepository.findByProjectIdAndEvaluatorEmail(anyLong(), anyString()))
                .thenReturn(Optional.empty());
        
        when(evaluationRepository.save(any(Evaluation.class))).thenAnswer(i -> {
            Evaluation e = i.getArgument(0);
            e.setId(1L);
            e.setStatus(EvaluationStatus.PENDING);
            return e;
        });

        Evaluation result = evaluationService.createEvaluation(100L, "evaluator@test.com");

        assertNotNull(result);
        assertEquals(100L, result.getProjectId());
        assertEquals("evaluator@test.com", result.getEvaluatorEmail());
        assertEquals(EvaluationStatus.PENDING, result.getStatus());
    }

    @Test
    void createEvaluation_AlreadyExists_ThrowsException() {
        Evaluation existing = new Evaluation(100L, "evaluator@test.com");
        when(evaluationRepository.findByProjectIdAndEvaluatorEmail(anyLong(), anyString()))
                .thenReturn(Optional.of(existing));

        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                evaluationService.createEvaluation(100L, "evaluator@test.com"));
        
        assertEquals("Ya existe una evaluación para este proyecto y evaluador", exception.getMessage());
    }

    @Test
    void submitEvaluation_Success_Approved() {
        Evaluation evaluation = new Evaluation(100L, "evaluator@test.com");
        evaluation.setId(1L);
        evaluation.setStatus(EvaluationStatus.PENDING);

        when(evaluationRepository.findById(1L)).thenReturn(Optional.of(evaluation));
        when(evaluationRepository.save(any(Evaluation.class))).thenAnswer(i -> i.getArgument(0));

        Evaluation result = evaluationService.submitEvaluation(1L, 4.5, "Great work", "None");

        assertEquals(EvaluationStatus.APPROVED, result.getStatus());
        assertEquals(4.5, result.getScore());
        assertNotNull(result.getEvaluationDate());
    }

    @Test
    void submitEvaluation_Success_Rejected() {
        Evaluation evaluation = new Evaluation(100L, "evaluator@test.com");
        evaluation.setId(1L);
        evaluation.setStatus(EvaluationStatus.PENDING);

        when(evaluationRepository.findById(1L)).thenReturn(Optional.of(evaluation));
        when(evaluationRepository.save(any(Evaluation.class))).thenAnswer(i -> i.getArgument(0));

        Evaluation result = evaluationService.submitEvaluation(1L, 2.0, "Poor work", "Redo");

        assertEquals(EvaluationStatus.REJECTED, result.getStatus());
        assertEquals(2.0, result.getScore());
    }

    @Test
    void simulateConcurrentEvaluations_ShouldHandleLoad() throws InterruptedException {
        int numberOfUsers = 100;
        var executorService = Executors.newFixedThreadPool(20);
        var latch = new java.util.concurrent.CountDownLatch(numberOfUsers);
        var successCount = new java.util.concurrent.atomic.AtomicInteger(0);
        var errorCount = new java.util.concurrent.atomic.AtomicInteger(0);

        // Setup mock behavior for multiple calls
        when(evaluationRepository.findByProjectIdAndEvaluatorEmail(anyLong(), anyString()))
            .thenReturn(Optional.empty());
        when(evaluationRepository.save(any(Evaluation.class))).thenAnswer(i -> {
            Evaluation e = i.getArgument(0);
            e.setId((long) (Math.random() * 1000));
            return e;
        });

        long startTime = System.currentTimeMillis();

        for (int i = 0; i < numberOfUsers; i++) {
            final int index = i;
            executorService.submit(() -> {
                try {
                    // Simulate processing time
                    Thread.sleep((long) (Math.random() * 50));
                    
                    Evaluation result = evaluationService.createEvaluation(100L + index, "eval" + index + "@test.com");
                    if (result != null) {
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

        boolean completed = latch.await(10, TimeUnit.SECONDS);
        executorService.shutdown();
        
        long duration = System.currentTimeMillis() - startTime;
        System.out.println("Concurrent Evaluation Test with " + numberOfUsers + " users completed in " + duration + "ms");

        assertTrue(completed, "Timeout: Not all simulated requests completed in time");
        assertEquals(numberOfUsers, successCount.get(), "All concurrent requests should succeed");
        assertEquals(0, errorCount.get(), "There should be no errors during concurrent execution");
    }
}
