package co.unicauca.user.repository;

import co.unicauca.user.model.DepartmentHeadAssignment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface DepartmentHeadRepository extends JpaRepository<DepartmentHeadAssignment, Long> {
    
    @Query("SELECT d FROM DepartmentHeadAssignment d WHERE d.startDate <= :now AND d.endDate >= :now ORDER BY d.id DESC")
    java.util.List<DepartmentHeadAssignment> findCurrentDepartmentHeads(java.time.LocalDateTime now);
}
