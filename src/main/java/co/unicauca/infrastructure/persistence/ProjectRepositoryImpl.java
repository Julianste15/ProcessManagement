package co.unicauca.infrastructure.persistence;

import co.unicauca.domain.entities.Project;
import co.unicauca.domain.entities.User;
import co.unicauca.domain.repositories.ProjectRepository;
import java.util.*;

public class ProjectRepositoryImpl implements ProjectRepository {
    private final Map<String, Project> projects = new HashMap<>();
    private static long idCounter = 1;
    
    @Override
    public Project save(Project project) {
        if (project.getId() == null) {
            project.setId("PROJ-" + (idCounter++));
        }
        projects.put(project.getId(), project);
        return project;
    }
    
    @Override
    public Optional<Project> findById(String id) {
        return Optional.ofNullable(projects.get(id));
    }
    
    @Override
    public List<Project> findByTeacher(User teacher) {
        List<Project> result = new ArrayList<>();
        for (Project project : projects.values()) {
            if (project.getTeacher() != null && 
                project.getTeacher().getEmail().equals(teacher.getEmail())) {
                result.add(project);
            }
        }
        return result;
    }
    
    @Override
    public List<Project> findByStudent(User student) {
        List<Project> result = new ArrayList<>();
        for (Project project : projects.values()) {
            if (project.getStudent() != null && 
                project.getStudent().getEmail().equals(student.getEmail())) {
                result.add(project);
            }
        }
        return result;
    }
    
    @Override
    public List<Project> findByStatus(String status) {
        List<Project> result = new ArrayList<>();
        for (Project project : projects.values()) {
            if (project.getStatus().name().equals(status)) {
                result.add(project);
            }
        }
        return result;
    }
    
    @Override
    public boolean delete(String id) {
        return projects.remove(id) != null;
    }
    
    @Override
    public List<Project> findAll() {
        return new ArrayList<>(projects.values());
    }
}