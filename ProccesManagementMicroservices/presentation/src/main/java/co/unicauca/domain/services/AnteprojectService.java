package co.unicauca.domain.services;

import co.unicauca.infrastructure.client.MicroserviceClient;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

public class AnteprojectService {

    private static final Logger logger = Logger.getLogger(AnteprojectService.class.getName());
    private final MicroserviceClient client;

    public AnteprojectService(MicroserviceClient client) {
        this.client = client;
    }

    @SuppressWarnings("unchecked")
    public List<Map<String, Object>> getSubmittedAnteprojects() throws Exception {
        if (client == null) {
            throw new IllegalStateException("No se ha inicializado el cliente para microservicios");
        }
        logger.info("Consultando anteproyectos enviados para jefe de departamento");
        Object response = client.get("/api/anteprojects/submitted", List.class);
        if (response instanceof List) {
            return (List<Map<String, Object>>) response;
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    public Map<String, Object> assignEvaluators(Long anteprojectId, String evaluator1Email, String evaluator2Email) throws Exception {
        String url = String.format("/api/anteprojects/%d/assign-evaluators?evaluator1Email=%s&evaluator2Email=%s",
                anteprojectId, evaluator1Email, evaluator2Email);
        logger.info("Asignando evaluadores para anteproyecto " + anteprojectId);
        return (Map<String, Object>) client.post(url, null, Map.class);
    }
}
