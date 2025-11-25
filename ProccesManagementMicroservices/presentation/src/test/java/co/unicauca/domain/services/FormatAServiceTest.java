package co.unicauca.domain.services;

import co.unicauca.infrastructure.client.MicroserviceClient;
import org.junit.jupiter.api.Test;
import java.util.List;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests unitarios para la lógica de integración con el microservicio de Formato A.
 * Cubre:
 *  - Envío de Formato A (submitFormatoA)
 *  - Consulta por usuario (getFormatosPorUsuario)
 *  - Consulta de proyectos para coordinador (getProjectsForCoordinator)
 *  - Aprobación y rechazo de proyectos (approveProject / rejectProject)
 *  - Resubmisión de Formato A (resubmitFormatoA)
 *
 * Estos tests usan un MicroserviceClient "fake" que NO hace HTTP real.
 */
public class FormatAServiceTest {
    
    /**
     * Cliente fake que registra las llamadas realizadas por FormatAService
     * y devuelve respuestas preconfiguradas.
     */
    static class FakeMicroserviceClient extends MicroserviceClient {
        String lastPostEndpoint;
        Object lastPostBody;
        String lastGetEndpoint;
        String lastPutEndpoint;
        Object lastPutBody;
        Object nextPostResponse;
        Object nextGetResponse;
        Object nextPutResponse;
        
        FakeMicroserviceClient() {
            super("http://fake-gateway");
        }
        
        @Override
        public <T> T post(String endpoint, Object requestBody, Class<T> responseType) {
            this.lastPostEndpoint = endpoint;
            this.lastPostBody = requestBody;
            //noinspection unchecked
            return (T) nextPostResponse;
        }
        
        @Override
        public <T> T get(String endpoint, Class<T> responseType) {
            this.lastGetEndpoint = endpoint;
            //noinspection unchecked
            return (T) nextGetResponse;
        }
        
        @Override
        public <T> T put(String endpoint, Object requestBody, Class<T> responseType) {
            this.lastPutEndpoint = endpoint;
            this.lastPutBody = requestBody;
            //noinspection unchecked
            return (T) nextPutResponse;
        }
    }
    
    @Test
    void submitFormatoA_shouldThrowWhenClientIsNull() {
        FormatAService service = new FormatAService(null);
        
        IllegalStateException ex = assertThrows(
                IllegalStateException.class,
                () -> service.submitFormatoA(Map.of("titulo", "Proyecto de prueba"))
        );
        
        assertEquals(
                "No se ha inicializado el cliente para microservicios",
                ex.getMessage()
        );
    }
    
    @Test
    void submitFormatoA_shouldCallCorrectEndpointAndReturnResponse() throws Exception {
        FakeMicroserviceClient client = new FakeMicroserviceClient();
        Map<String, Object> expectedResponse = Map.of(
                "id", 123L,
                "estado", "FORMATO_A_EN_EVALUACION"
        );
        client.nextPostResponse = expectedResponse;
        
        FormatAService service = new FormatAService(client);
        
        Map<String, Object> request = Map.of(
                "titulo", "Proyecto de prueba",
                "modalidad", "INVESTIGACION"
        );
        
        Map<String, Object> response = service.submitFormatoA(request);
        
        assertEquals("/api/format-a/submit", client.lastPostEndpoint);
        assertEquals(request, client.lastPostBody);
        assertEquals(expectedResponse, response);
    }
    
    @Test
    void getFormatosPorUsuario_shouldReturnListFromClient() throws Exception {
        FakeMicroserviceClient client = new FakeMicroserviceClient();
        List<Map<String, Object>> expectedList = List.of(
                Map.of("id", 1L, "titulo", "P1"),
                Map.of("id", 2L, "titulo", "P2")
        );
        client.nextGetResponse = expectedList;
        
        FormatAService service = new FormatAService(client);
        
        List<Map<String, Object>> result =
                service.getFormatosPorUsuario("student@unicauca.edu.co");
        
        assertEquals("/api/format-a/user/student@unicauca.edu.co", client.lastGetEndpoint);
        assertEquals(2, result.size());
        assertEquals("P2", result.get(1).get("titulo"));
    }
    
    @Test
    void getProjectsForCoordinator_shouldReturnListFromClient() throws Exception {
        FakeMicroserviceClient client = new FakeMicroserviceClient();
        List<Map<String, Object>> expectedList = List.of(
                Map.of("id", 10L, "titulo", "Proyecto 10"),
                Map.of("id", 11L, "titulo", "Proyecto 11")
        );
        client.nextGetResponse = expectedList;
        
        FormatAService service = new FormatAService(client);
        
        List<Map<String, Object>> result =
                service.getProjectsForCoordinator("coord@unicauca.edu.co");
        
        assertEquals("/api/format-a/coordinator/coord@unicauca.edu.co", client.lastGetEndpoint);
        assertEquals(2, result.size());
        assertEquals(11L, result.get(1).get("id"));
    }
    
    @Test
    void approveProject_shouldSendCorrectPayload() throws Exception {
        FakeMicroserviceClient client = new FakeMicroserviceClient();
        FormatAService service = new FormatAService(client);
        
        service.approveProject(99L, "Todo OK");
        
        assertEquals("/api/format-a/99/evaluate", client.lastPutEndpoint);
        assertNotNull(client.lastPutBody);
        assertTrue(client.lastPutBody instanceof Map);
        
        Map<?, ?> body = (Map<?, ?>) client.lastPutBody;
        assertEquals("FORMATO_A_ACEPTADO", body.get("estado"));
        assertEquals("Todo OK", body.get("observaciones"));
    }
    
    @Test
    void rejectProject_shouldSendCorrectPayload() throws Exception {
        FakeMicroserviceClient client = new FakeMicroserviceClient();
        FormatAService service = new FormatAService(client);
        
        service.rejectProject(77L, "Faltan requisitos");
        
        assertEquals("/api/format-a/77/evaluate", client.lastPutEndpoint);
        assertNotNull(client.lastPutBody);
        assertTrue(client.lastPutBody instanceof Map);
        
        Map<?, ?> body = (Map<?, ?>) client.lastPutBody;
        assertEquals("FORMATO_A_RECHAZADO", body.get("estado"));
        assertEquals("Faltan requisitos", body.get("observaciones"));
    }
    
    @Test
    void resubmitFormatoA_shouldCallCorrectEndpointAndReturnResponse() throws Exception {
        FakeMicroserviceClient client = new FakeMicroserviceClient();
        Map<String, Object> expectedResponse = Map.of(
                "id", 50L,
                "estado", "FORMATO_A_EN_EVALUACION",
                "intentos", 2
        );
        client.nextPutResponse = expectedResponse;
        
        FormatAService service = new FormatAService(client);
        
        Map<String, Object> request = Map.of(
                "titulo", "Proyecto corregido",
                "modalidad", "INVESTIGACION"
        );
        
        Map<String, Object> response =
                service.resubmitFormatoA(50L, request);
        
        assertEquals("/api/format-a/50/resubmit", client.lastPutEndpoint);
        assertEquals(request, client.lastPutBody);
        assertEquals(2, response.get("intentos"));
    }
}

