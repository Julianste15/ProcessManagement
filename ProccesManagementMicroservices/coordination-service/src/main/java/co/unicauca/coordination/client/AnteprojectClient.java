package co.unicauca.coordination.client;
import co.unicauca.coordination.dto.ProjectSummaryDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import java.util.List;
@FeignClient(name = "ANTEPROJECT-SERVICE")
public interface AnteprojectClient {
    @GetMapping("/api/anteprojects/coordinator/{coordinatorId}")
    List<ProjectSummaryDTO> getProjectsByCoordinator(@PathVariable Long coordinatorId);
}
