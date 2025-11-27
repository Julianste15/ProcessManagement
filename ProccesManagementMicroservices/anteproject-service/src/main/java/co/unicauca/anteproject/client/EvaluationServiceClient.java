package co.unicauca.anteproject.client;

import co.unicauca.anteproject.config.FeignClientConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "evaluation-service", configuration = FeignClientConfig.class)
public interface EvaluationServiceClient {    
    @PostMapping("/api/evaluation-assignments")
    Object assignEvaluators(
        @RequestParam("projectId") Long projectId,
        @RequestParam("evaluator1Email") String evaluator1Email,
        @RequestParam("evaluator2Email") String evaluator2Email
    );
}