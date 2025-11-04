package co.unicauca.coordination.repository;

import co.unicauca.coordination.model.CoordinationDashboard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CoordinationRepository extends JpaRepository<CoordinationDashboard, Long> {
    Optional<CoordinationDashboard> findByCoordinatorId(Long coordinatorId);
}