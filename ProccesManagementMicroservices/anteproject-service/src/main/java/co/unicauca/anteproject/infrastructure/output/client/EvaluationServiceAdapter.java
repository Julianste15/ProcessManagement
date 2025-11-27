package co.unicauca.anteproject.infrastructure.output.client;

import co.unicauca.anteproject.client.EvaluationServiceClient;
import co.unicauca.anteproject.domain.ports.out.EvaluationServicePort;
import org.springframework.stereotype.Component;

@Component
public class EvaluationServiceAdapter implements EvaluationServicePort {

    private final EvaluationServiceClient feignClient;

    public EvaluationServiceAdapter(EvaluationServiceClient feignClient) {
        this.feignClient = feignClient;
    }

    @Override
    public void assignEvaluators(Long anteprojectId, String evaluator1Email, String evaluator2Email) {
        feignClient.assignEvaluators(anteprojectId, evaluator1Email, evaluator2Email);
    }
}
