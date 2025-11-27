package co.unicauca.notification.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "user-service")
public interface UserServiceClient {
    @GetMapping("/api/department-head/current")
    String getCurrentDepartmentHeadEmail();
}
