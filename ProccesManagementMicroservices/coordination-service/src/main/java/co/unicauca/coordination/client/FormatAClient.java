package co.unicauca.coordination.client;
import co.unicauca.coordination.dto.ProjectSummaryDTO;
import java.util.List;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
@FeignClient(name = "FORMAT-A-SERVICE")
public interface FormatAClient {
    @GetMapping("/api/format-a/coordinator/{coordinatorId}/pending")
    List<ProjectSummaryDTO> getPendingFormatAByCoordinator(@PathVariable Long coordinatorId);
}
