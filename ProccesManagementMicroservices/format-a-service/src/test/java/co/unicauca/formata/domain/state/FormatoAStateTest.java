package co.unicauca.formata.domain.state;

import co.unicauca.formata.domain.state.states.*;
import co.unicauca.formata.dto.EvaluationRequest;
import co.unicauca.formata.dto.FormatARequest;
import co.unicauca.formata.model.EstadoProyecto;
import co.unicauca.formata.model.FormatoA;
import co.unicauca.formata.model.Modalidad;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests unitarios para el patrón State de FormatoA
 */
class FormatoAStateTest {

    private FormatoA formatoA;
    private FormatoAStateContext context;

    @BeforeEach
    void setUp() {
        formatoA = new FormatoA(
            "Proyecto de prueba",
            Modalidad.INVESTIGACION,
            LocalDate.now(),
            "director@unicauca.edu.co",
            null,
            "estudiante@unicauca.edu.co",
            "Objetivo general de prueba",
            Arrays.asList("Objetivo específico 1", "Objetivo específico 2"),
            "/path/to/pdf.pdf",
            null
        );
        context = formatoA.getStateContext();
    }

    @Test
    void testEstadoInicialEsEnEvaluacion() {
        assertEquals(EstadoProyecto.FORMATO_A_EN_EVALUACION, formatoA.getEstado());
        assertTrue(context.getCurrentState() instanceof EnEvaluacionState);
    }

    @Test
    void testEvaluarYAprobar() {
        EvaluationRequest request = new EvaluationRequest();
        request.setEstado(EstadoProyecto.FORMATO_A_ACEPTADO);
        request.setObservaciones("Aprobado");

        context.evaluate(request);

        assertEquals(EstadoProyecto.FORMATO_A_ACEPTADO, formatoA.getEstado());
        assertEquals("Aprobado", formatoA.getObservaciones());
        assertTrue(context.getCurrentState() instanceof AceptadoState);
    }

    @Test
    void testEvaluarYRechazar() {
        EvaluationRequest request = new EvaluationRequest();
        request.setEstado(EstadoProyecto.FORMATO_A_RECHAZADO);
        request.setObservaciones("Necesita correcciones");

        context.evaluate(request);

        assertEquals(EstadoProyecto.FORMATO_A_RECHAZADO, formatoA.getEstado());
        assertEquals("Necesita correcciones", formatoA.getObservaciones());
        assertTrue(context.getCurrentState() instanceof RechazadoState);
    }

    @Test
    void testReenviarFormatoRechazado() {
        // Primero rechazar
        EvaluationRequest evalRequest = new EvaluationRequest();
        evalRequest.setEstado(EstadoProyecto.FORMATO_A_RECHAZADO);
        evalRequest.setObservaciones("Rechazado");
        context.evaluate(evalRequest);

        int intentosAnteriores = formatoA.getIntentos();

        // Luego reenviar
        FormatARequest resubmitRequest = new FormatARequest();
        resubmitRequest.setTitulo("Proyecto corregido");
        resubmitRequest.setModalidad(Modalidad.INVESTIGACION);
        resubmitRequest.setDirectorEmail("director@unicauca.edu.co");
        resubmitRequest.setStudentEmail("estudiante@unicauca.edu.co");
        resubmitRequest.setObjetivoGeneral("Objetivo corregido");
        resubmitRequest.setObjetivosEspecificos(Arrays.asList("Objetivo 1 corregido"));

        context.resubmit(resubmitRequest);

        assertEquals(EstadoProyecto.FORMATO_A_EN_EVALUACION, formatoA.getEstado());
        assertEquals("Proyecto corregido", formatoA.getTitulo());
        assertEquals(intentosAnteriores + 1, formatoA.getIntentos());
        assertTrue(context.getCurrentState() instanceof EnEvaluacionState);
    }

    @Test
    void testMaximoIntentosAlcanzado() {
        // Simular 3 rechazos
        formatoA.setIntentos(3);
        formatoA.setEstado(EstadoProyecto.FORMATO_A_RECHAZADO);
        context = new FormatoAStateContext(formatoA);

        FormatARequest resubmitRequest = new FormatARequest();
        resubmitRequest.setTitulo("Intento 4");
        resubmitRequest.setModalidad(Modalidad.INVESTIGACION);
        resubmitRequest.setDirectorEmail("director@unicauca.edu.co");
        resubmitRequest.setStudentEmail("estudiante@unicauca.edu.co");
        resubmitRequest.setObjetivoGeneral("Objetivo");
        resubmitRequest.setObjetivosEspecificos(Arrays.asList("Objetivo 1"));

        // Debe lanzar excepción
        assertThrows(IllegalStateException.class, () -> {
            context.resubmit(resubmitRequest);
        });
    }

    @Test
    void testNoSePuedeReenviarEnEstadoAceptado() {
        // Aprobar el formato
        EvaluationRequest evalRequest = new EvaluationRequest();
        evalRequest.setEstado(EstadoProyecto.FORMATO_A_ACEPTADO);
        evalRequest.setObservaciones("Aprobado");
        context.evaluate(evalRequest);

        FormatARequest resubmitRequest = new FormatARequest();
        resubmitRequest.setTitulo("Intento de reenvío");
        resubmitRequest.setModalidad(Modalidad.INVESTIGACION);
        resubmitRequest.setDirectorEmail("director@unicauca.edu.co");
        resubmitRequest.setStudentEmail("estudiante@unicauca.edu.co");
        resubmitRequest.setObjetivoGeneral("Objetivo");
        resubmitRequest.setObjetivosEspecificos(Arrays.asList("Objetivo 1"));

        // Debe lanzar excepción
        assertThrows(IllegalStateException.class, () -> {
            context.resubmit(resubmitRequest);
        });
    }

    @Test
    void testNoSePuedeEvaluarEnEstadoRechazado() {
        // Rechazar el formato
        EvaluationRequest evalRequest1 = new EvaluationRequest();
        evalRequest1.setEstado(EstadoProyecto.FORMATO_A_RECHAZADO);
        evalRequest1.setObservaciones("Rechazado");
        context.evaluate(evalRequest1);

        // Intentar evaluar de nuevo
        EvaluationRequest evalRequest2 = new EvaluationRequest();
        evalRequest2.setEstado(EstadoProyecto.FORMATO_A_ACEPTADO);
        evalRequest2.setObservaciones("Intento de aprobar");

        // Debe lanzar excepción
        assertThrows(IllegalStateException.class, () -> {
            context.evaluate(evalRequest2);
        });
    }

    @Test
    void testTransicionInvalida() {
        // Intentar transicionar directamente a ANTEPROYECTO_SUBIDO desde EN_EVALUACION
        assertThrows(IllegalStateException.class, () -> {
            context.transitionTo(EstadoProyecto.ANTEPROYECTO_SUBIDO);
        });
    }

    @Test
    void testFlujoCompletoExitoso() {
        // 1. Estado inicial: EN_EVALUACION
        assertEquals(EstadoProyecto.FORMATO_A_EN_EVALUACION, formatoA.getEstado());

        // 2. Aprobar
        EvaluationRequest evalRequest = new EvaluationRequest();
        evalRequest.setEstado(EstadoProyecto.FORMATO_A_ACEPTADO);
        evalRequest.setObservaciones("Aprobado");
        context.evaluate(evalRequest);
        assertEquals(EstadoProyecto.FORMATO_A_ACEPTADO, formatoA.getEstado());

        // 3. Subir anteproyecto
        context.transitionTo(EstadoProyecto.ANTEPROYECTO_SUBIDO);
        assertEquals(EstadoProyecto.ANTEPROYECTO_SUBIDO, formatoA.getEstado());

        // 4. Asignar evaluadores (transición a EN_EVALUACION)
        context.transitionTo(EstadoProyecto.ANTEPROYECTO_EN_EVALUACION);
        assertEquals(EstadoProyecto.ANTEPROYECTO_EN_EVALUACION, formatoA.getEstado());

        // 5. Aprobar anteproyecto
        context.transitionTo(EstadoProyecto.ANTEPROYECTO_ACEPTADO);
        assertEquals(EstadoProyecto.ANTEPROYECTO_ACEPTADO, formatoA.getEstado());
    }

    @Test
    void testEstadosFinalesNoPermitenTransiciones() {
        // Llevar a estado final ANTEPROYECTO_ACEPTADO
        formatoA.setEstado(EstadoProyecto.ANTEPROYECTO_ACEPTADO);
        context = new FormatoAStateContext(formatoA);

        // No debe permitir ninguna transición
        assertThrows(IllegalStateException.class, () -> {
            context.transitionTo(EstadoProyecto.FORMATO_A_EN_EVALUACION);
        });

        // Llevar a estado final ANTEPROYECTO_RECHAZADO
        formatoA.setEstado(EstadoProyecto.ANTEPROYECTO_RECHAZADO);
        context = new FormatoAStateContext(formatoA);

        // No debe permitir ninguna transición
        assertThrows(IllegalStateException.class, () -> {
            context.transitionTo(EstadoProyecto.FORMATO_A_EN_EVALUACION);
        });
    }
}
