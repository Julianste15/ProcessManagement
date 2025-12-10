package co.unicauca.anteproject.client;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
@FeignClient(name = "format-a-service", path = "/api/format-a")
public interface FormatAServiceClient {   
    @GetMapping("/{id}")
    FormatADTO getFormatoAById(@PathVariable Long id);
}