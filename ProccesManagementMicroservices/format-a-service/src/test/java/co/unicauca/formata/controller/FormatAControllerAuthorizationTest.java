package co.unicauca.formata.controller;

import co.unicauca.formata.dto.FormatAResponse;
import co.unicauca.formata.service.FormatoAService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Reglas de autorización sobre la evaluación de un Formato A.
 *
 * Solo el Coordinador puede aprobar o rechazar. Un estudiante nunca puede
 * aprobar su propio proyecto, aunque conozca el endpoint.
 */
@ExtendWith(MockitoExtension.class)
class FormatAControllerAuthorizationTest {

    private static final String EVALUATE_URL = "/api/format-a/10/evaluate";
    private static final String EVALUATE_BODY = "{\"estado\":\"FORMATO_A_ACEPTADO\"}";

    @Mock
    private FormatoAService formatoAService;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        FormatAController controller = new FormatAController();
        ReflectionTestUtils.setField(controller, "formatoAService", formatoAService);
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    @DisplayName("Un estudiante no puede evaluar (aprobar) un Formato A")
    void evaluateAsStudentIsForbidden() throws Exception {
        mockMvc.perform(put(EVALUATE_URL)
                        .header("X-User-Email", "student@unicauca.edu.co")
                        .header("X-User-Role", "STUDENT")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(EVALUATE_BODY))
                .andExpect(status().isForbidden());

        verify(formatoAService, never()).evaluateFormatoA(any(), any());
    }

    @Test
    @DisplayName("Un docente no puede evaluar un Formato A")
    void evaluateAsTeacherIsForbidden() throws Exception {
        mockMvc.perform(put(EVALUATE_URL)
                        .header("X-User-Email", "teacher@unicauca.edu.co")
                        .header("X-User-Role", "TEACHER")
                        .header("X-User-IsDepartmentHead", "true")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(EVALUATE_BODY))
                .andExpect(status().isForbidden());

        verify(formatoAService, never()).evaluateFormatoA(any(), any());
    }

    @Test
    @DisplayName("Sin cabecera de rol se rechaza la evaluación")
    void evaluateWithoutRoleIsForbidden() throws Exception {
        mockMvc.perform(put(EVALUATE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(EVALUATE_BODY))
                .andExpect(status().isForbidden());

        verify(formatoAService, never()).evaluateFormatoA(any(), any());
    }

    @Test
    @DisplayName("El coordinador puede evaluar un Formato A")
    void evaluateAsCoordinatorIsAllowed() throws Exception {
        when(formatoAService.evaluateFormatoA(eq(10L), any())).thenReturn(new FormatAResponse());

        mockMvc.perform(put(EVALUATE_URL)
                        .header("X-User-Email", "coordinator@unicauca.edu.co")
                        .header("X-User-Role", "COORDINATOR")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(EVALUATE_BODY))
                .andExpect(status().isOk());

        verify(formatoAService).evaluateFormatoA(eq(10L), any());
    }
}
