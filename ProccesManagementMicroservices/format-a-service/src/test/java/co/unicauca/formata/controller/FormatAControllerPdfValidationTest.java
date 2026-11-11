package co.unicauca.formata.controller;

import co.unicauca.formata.dto.FormatARequest;
import co.unicauca.formata.model.Modalidad;
import co.unicauca.formata.service.FormatoAService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Base64;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Un PDF por encima del límite debe rechazarse con 4xx y un mensaje legible,
 * sin llamar a servicios remotos.
 */
class FormatAControllerPdfValidationTest {

    private static final long MAX_PDF_SIZE_BYTES = 10485760L;

    private MockMvc mockMvc;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        FormatoAService formatoAService = new FormatoAService();
        ReflectionTestUtils.setField(formatoAService, "maxPdfSizeBytes", MAX_PDF_SIZE_BYTES);

        FormatAController controller = new FormatAController();
        ReflectionTestUtils.setField(controller, "formatoAService", formatoAService);
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    @DisplayName("Un PDF de 11 MB se rechaza con 400 y un mensaje legible")
    void submitWithTooLargePdfReturnsBadRequest() throws Exception {
        FormatARequest request = new FormatARequest();
        request.setTitulo("Proyecto con PDF gigante");
        request.setModalidad(Modalidad.INVESTIGACION);
        request.setDirectorEmail("director@unicauca.edu.co");
        request.setStudentEmail("student@unicauca.edu.co");
        request.setObjetivoGeneral("Objetivo general");
        request.setArchivoPdfNombre("gigante.pdf");
        request.setArchivoPdfContenido(Base64.getEncoder()
                .encodeToString(new byte[11 * 1024 * 1024]));

        mockMvc.perform(post("/api/format-a/submit")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(request)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(
                        org.hamcrest.Matchers.containsString("supera el tamaño máximo permitido")));
    }
}
