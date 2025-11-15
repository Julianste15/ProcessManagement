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
}

