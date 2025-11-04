package co.unicauca.formata.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import java.util.Map;

@FeignClient(name = "evaluation-service", path = "/api/evaluation-assignments")
public interface EvaluationServiceClient {
    
    @PostMapping
    Map<String, Object> assignEvaluators(@RequestBody Map<String, Object> request);
}