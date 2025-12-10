package co.unicauca.formata.model;

import co.unicauca.formata.domain.state.FormatoAStateContext;
import co.unicauca.formata.domain.state.states.EnEvaluacionState;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests para verificar el patrón Builder en FormatoA.
 */
class FormatoABuilderTest {

    @Test
    void testBuilderCreatesInstanciaValida() {
        // Uso del patrón Builder para crear un objeto complejo
        FormatoA formatoA = FormatoA.builder()
                .titulo("Proyecto con Builder")
                .modalidad(Modalidad.INVESTIGACION)
                .studentEmail("estudiante@unicauca.edu.co")
                .directorEmail("director@unicauca.edu.co")
                .objetivoGeneral("Objetivo General")
                .objetivosEspecificos(Arrays.asList("Obj 1", "Obj 2"))
                .build();

        // Verificaciones
        assertNotNull(formatoA);
        assertEquals("Proyecto con Builder", formatoA.getTitulo());
        assertEquals(Modalidad.INVESTIGACION, formatoA.getModalidad());
        assertEquals("estudiante@unicauca.edu.co", formatoA.getStudentEmail());
        
        // Verificar valores por defecto
        assertNotNull(formatoA.getFechaCreacion()); // Debe ser LocalDate.now()
        assertEquals(EstadoProyecto.FORMATO_A_EN_EVALUACION, formatoA.getEstado());
        assertEquals(1, formatoA.getIntentos());
        
        // Verificar inicialización del contexto State
        assertNotNull(formatoA.getStateContext());
        assertTrue(formatoA.getStateContext().getCurrentState() instanceof EnEvaluacionState);
    }
    
    @Test
    void testBuilderConCamposOpcionales() {
        // Creación mínima
        FormatoA formatoA = FormatoA.builder()
                .titulo("Proyecto Minimo")
                .modalidad(Modalidad.PRACTICA_PROFESIONAL)
                .build();
                
        assertEquals("Proyecto Minimo", formatoA.getTitulo());
        assertNull(formatoA.getCodirectorEmail()); // Campo opcional nulo
        assertNull(formatoA.getStudentEmail()); // Campo no seteado
    }
}
