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
                "observaciones", observations != null ? observations : ""
        );
        client.put("/api/format-a/" + projectId + "/evaluate", request, Map.class);
    }

    public void rejectProject(Long projectId, String observations) throws Exception {
        if (client == null) {
            throw new IllegalStateException("No se ha inicializado el cliente para microservicios");
        }
        logger.info("Rechazando proyecto ID: " + projectId);
        Map<String, Object> request = Map.of(
                "estado", "FORMATO_A_RECHAZADO",
                "observaciones", observations != null ? observations : ""
        );
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

    public boolean uploadAnteproject(Long anteprojectId, String documentUrl) throws Exception {
        if (client == null) {
            throw new IllegalStateException("No se ha inicializado el cliente para microservicios");
        }
        logger.info("Subiendo anteproyecto ID: " + anteprojectId);
        String encodedDocumentUrl = java.net.URLEncoder.encode(documentUrl, java.nio.charset.StandardCharsets.UTF_8);
        String url = "/api/anteprojects/" + anteprojectId + "/submit-document?documentUrl=" + encodedDocumentUrl;
        Object response = client.post(url, null, Map.class);
        return response != null;
    }

    /**
     * Crea un anteproyecto asociado al Formato A especificado.
     * El email del estudiante se obtiene automáticamente del Formato A en el backend.
     * @param formatoAId Id del Formato A al que pertenece el anteproyecto.
     * @param titulo Título del anteproyecto.
     * @param directorEmail Email del director.
     * @return Map que contiene al menos el campo "id" del anteproyecto creado.
     */
    @SuppressWarnings("unchecked")
    public Map<String, Object> createAnteproject(Long formatoAId, String titulo, String directorEmail) throws Exception {
        if (client == null) {
            throw new IllegalStateException("No se ha inicializado el cliente para microservicios");
        }
        logger.info("Creando anteproyecto para Formato A ID: " + formatoAId);
        Map<String, Object> request = Map.of(
                "formatoAId", formatoAId,
                "titulo", titulo,
                "directorEmail", directorEmail
                // studentEmail ya NO se envía - el backend lo obtiene del Formato A
        );
        Object response = client.post("/api/anteprojects", request, Map.class);
        if (response instanceof Map) {
            return (Map<String, Object>) response;
        }
        throw new IllegalStateException("Respuesta inesperada al crear anteproyecto");
    }
}