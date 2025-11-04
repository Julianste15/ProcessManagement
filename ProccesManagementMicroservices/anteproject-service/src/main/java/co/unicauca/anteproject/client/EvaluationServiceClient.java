package co.unicauca.anteproject.client;

import co.unicauca.anteproject.dto.EvaluationAssignmentRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "evaluation-service", path = "/api/evaluation-assignments")
public interface EvaluationServiceClient {
    
    @PostMapping
    Object assignEvaluators(@RequestBody EvaluationAssignmentRequest request);
}