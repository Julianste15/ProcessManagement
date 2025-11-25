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
        return List.of();
    }
}
