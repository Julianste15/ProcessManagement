package co.unicauca.anteproject.infrastructure.input.controller;

import co.unicauca.anteproject.domain.model.Anteproject;
import co.unicauca.anteproject.domain.ports.in.CreateAnteprojectUseCase;
import co.unicauca.anteproject.domain.ports.in.GetAnteprojectUseCase;
import co.unicauca.anteproject.domain.ports.in.ManageAnteprojectUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Reglas de autorización del ciclo del anteproyecto:
 * - El jefe de departamento asigna evaluadores.
 * - Solo coordinador o jefe de departamento cambian el estado.
 * - Un estudiante nunca puede aprobar su propio proyecto.
 */
@ExtendWith(MockitoExtension.class)
class AnteprojectRestControllerAuthorizationTest {

    private static final String ASSIGN_URL =
            "/api/anteprojects/10/assign-evaluators?evaluator1Email=eval1@unicauca.edu.co&evaluator2Email=eval2@unicauca.edu.co";
    private static final String STATUS_URL = "/api/anteprojects/10/status?status=APPROVED";

    @Mock
    private CreateAnteprojectUseCase createAnteprojectUseCase;
    @Mock
    private ManageAnteprojectUseCase manageAnteprojectUseCase;
    @Mock
    private GetAnteprojectUseCase getAnteprojectUseCase;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        AnteprojectRestController controller =
                new AnteprojectRestController(createAnteprojectUseCase, manageAnteprojectUseCase, getAnteprojectUseCase);
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    @DisplayName("Un estudiante no puede asignar evaluadores")
    void assignEvaluatorsAsStudentIsForbidden() throws Exception {
        mockMvc.perform(post(ASSIGN_URL)
                        .header("X-User-Email", "student@unicauca.edu.co")
                        .header("X-User-Role", "STUDENT"))
                .andExpect(status().isForbidden());

        verify(manageAnteprojectUseCase, never())
                .assignEvaluators(anyLong(), anyString(), anyString(), anyString());
    }

    @Test
    @DisplayName("El coordinador no puede asignar evaluadores (regla: lo hace el jefe de departamento)")
    void assignEvaluatorsAsCoordinatorIsForbidden() throws Exception {
        mockMvc.perform(post(ASSIGN_URL)
                        .header("X-User-Email", "coordinator@unicauca.edu.co")
                        .header("X-User-Role", "COORDINATOR"))
                .andExpect(status().isForbidden());

        verify(manageAnteprojectUseCase, never())
                .assignEvaluators(anyLong(), anyString(), anyString(), anyString());
    }

    @Test
    @DisplayName("El jefe de departamento puede asignar evaluadores")
    void assignEvaluatorsAsDepartmentHeadIsAllowed() throws Exception {
        when(manageAnteprojectUseCase.assignEvaluators(eq(10L), anyString(), anyString(), anyString()))
                .thenReturn(new Anteproject());

        mockMvc.perform(post(ASSIGN_URL)
                        .header("X-User-Email", "head@unicauca.edu.co")
                        .header("X-User-Role", "TEACHER")
                        .header("X-User-IsDepartmentHead", "true"))
                .andExpect(status().isOk());

        verify(manageAnteprojectUseCase).assignEvaluators(eq(10L), anyString(), anyString(), anyString());
    }

    @Test
    @DisplayName("Un estudiante no puede cambiar el estado de su anteproyecto")
    void updateStatusAsStudentIsForbidden() throws Exception {
        mockMvc.perform(put(STATUS_URL)
                        .header("X-User-Email", "student@unicauca.edu.co")
                        .header("X-User-Role", "STUDENT"))
                .andExpect(status().isForbidden());

        verify(manageAnteprojectUseCase, never()).updateStatus(anyLong(), any(), anyString());
    }

    @Test
    @DisplayName("Un docente que no es jefe de departamento no puede cambiar el estado")
    void updateStatusAsTeacherIsForbidden() throws Exception {
        mockMvc.perform(put(STATUS_URL)
                        .header("X-User-Email", "teacher@unicauca.edu.co")
                        .header("X-User-Role", "TEACHER"))
                .andExpect(status().isForbidden());

        verify(manageAnteprojectUseCase, never()).updateStatus(anyLong(), any(), anyString());
    }

    @Test
    @DisplayName("El coordinador puede cambiar el estado de un anteproyecto")
    void updateStatusAsCoordinatorIsAllowed() throws Exception {
        when(manageAnteprojectUseCase.updateStatus(eq(10L), any(), anyString()))
                .thenReturn(new Anteproject());

        mockMvc.perform(put(STATUS_URL)
                        .header("X-User-Email", "coordinator@unicauca.edu.co")
                        .header("X-User-Role", "COORDINATOR"))
                .andExpect(status().isOk());

        verify(manageAnteprojectUseCase).updateStatus(eq(10L), any(), anyString());
    }
}
