package co.unicauca.domain.services;
import co.unicauca.infrastructure.client.MicroserviceClient;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
public class FormatAService {
    private static final Logger logger = Logger.getLogger(FormatAService.class.getName());
    private final MicroserviceClient client;
    public FormatAService(MicroserviceClient client) {
        this.client = client;
    }
    @SuppressWarnings("unchecked")
    public Map<String, Object> submitFormatoA(Map<String, Object> request) throws Exception {
        if (client == null) {
            throw new IllegalStateException("No se ha inicializado el cliente para microservicios");
        }
        logger.info("Enviando Formato A al microservicio");
        Object response = client.post("/api/format-a/submit", request, Map.class);
        if (response instanceof Map) {
            return (Map<String, Object>) response;
        }
        return Map.of();
    }
    @SuppressWarnings("unchecked")
    public List<Map<String, Object>> getFormatosPorUsuario(String email) throws Exception {
        if (client == null) {
            throw new IllegalStateException("No se ha inicializado el cliente para microservicios");
        }
        logger.info("Consultando Formato A para el usuario: " + email);
        Object response = client.get("/api/format-a/user/" + email, List.class);
        if (response instanceof List) {
            return (List<Map<String, Object>>) response;
        }
        return List.of();
    }
    @SuppressWarnings("unchecked")
    public List<Map<String, Object>> getProjectsForCoordinator(String coordinatorEmail) throws Exception {
        if (client == null) {
            throw new IllegalStateException("No se ha inicializado el cliente para microservicios");
        }
        logger.info("Consultando proyectos para coordinador: " + coordinatorEmail);
        Object response = client.get("/api/format-a/coordinator/" + coordinatorEmail, List.class);
        if (response instanceof List) {
            return (List<Map<String, Object>>) response;
        }
        return List.of();
    }
    public void approveProject(Long projectId, String observations) throws Exception {
        if (client == null) {
            throw new IllegalStateException("No se ha inicializado el cliente para microservicios");
        }
        logger.info("Aprobando proyecto ID: " + projectId);
        Map<String, Object> request = Map.of(
                "estado", "FORMATO_A_ACEPTADO",
                "observaciones", observations != null ? observations : "");
        client.put("/api/format-a/" + projectId + "/evaluate", request, Map.class);
    }
    public void rejectProject(Long projectId, String observations) throws Exception {
        if (client == null) {
            throw new IllegalStateException("No se ha inicializado el cliente para microservicios");
        }
        logger.info("Rechazando proyecto ID: " + projectId);
        Map<String, Object> request = Map.of(
                "estado", "FORMATO_A_RECHAZADO",
                "observaciones", observations != null ? observations : "");
        client.put("/api/format-a/" + projectId + "/evaluate", request, Map.class);
    }
    @SuppressWarnings("unchecked")
    public Map<String, Object> resubmitFormatoA(Long formatoAId, Map<String, Object> request) throws Exception {
        if (client == null) {
            throw new IllegalStateException("No se ha inicializado el cliente para microservicios");
        }
        logger.info("Reenviando Formato A ID: " + formatoAId);
        Object response = client.put("/api/format-a/" + formatoAId + "/resubmit", request, Map.class);
        if (response instanceof Map) {
            return (Map<String, Object>) response;
        }
        return Map.of();
    }
}