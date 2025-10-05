package co.unicauca.domain.repositories;

import co.unicauca.domain.entities.Project;
import co.unicauca.domain.entities.User;
import java.util.List;
import java.util.Optional;

public interface ProjectRepository {
    Project save(Project project);
    Optional<Project> findById(String id);
    List<Project> findByTeacher(User teacher);
    List<Project> findByStudent(User student);
    List<Project> findByStatus(String status);
    boolean delete(String id);
    List<Project> findAll();
}