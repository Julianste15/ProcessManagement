package co.unicauca.domain.services;

import co.unicauca.domain.entities.Project;
import co.unicauca.domain.entities.User;
import co.unicauca.domain.enums.ProjectModality;
import co.unicauca.domain.enums.ProjectStatus;
import co.unicauca.domain.exceptions.ProjectException;
import co.unicauca.domain.repositories.ProjectRepository;
import co.unicauca.infrastructure.dependency_injection.Service;
import co.unicauca.infrastructure.dependency_injection.FactoryAutowired;

import java.time.LocalDateTime;
import java.util.List;
import java.util.logging.Logger;

@Service
public class ProjectService {
    private static final Logger logger = Logger.getLogger(ProjectService.class.getName());
    
    @FactoryAutowired
    private ProjectRepository projectRepository;
    
    public Project submitFormatA(Project project, User teacher) throws ProjectException {
        // Validaciones
        validateProject(project);
        
        // Asignar docente
        project.setTeacher(teacher);
        project.setStatus(ProjectStatus.SUBMITTED);
        project.setSubmissionDate(LocalDateTime.now());
        
        // Guardar proyecto
        Project savedProject = projectRepository.save(project);
        logger.info("Formato A enviado exitosamente: " + savedProject.getId());
        
        return savedProject;
    }
    
    public Project resubmitFormatA(String projectId, Project updatedProject, User teacher) throws ProjectException {
        // Buscar proyecto existente
        Project existingProject = projectRepository.findById(projectId)
            .orElseThrow(() -> new ProjectException("Proyecto no encontrado: " + projectId));
        
        // Verificar que pertenece al docente
        if (!existingProject.getTeacher().getEmail().equals(teacher.getEmail())) {
            throw new ProjectException("No tiene permisos para modificar este proyecto");
        }
        
        // Verificar que puede re-enviar
        if (!existingProject.canResubmit()) {
            throw new ProjectException("No se pueden realizar más envíos para este proyecto");
        }
        
        // Preparar para re-envío
        existingProject.prepareForResubmission();
        
        // Actualizar datos
        existingProject.setTitle(updatedProject.getTitle());
        existingProject.setModality(updatedProject.getModality());
        existingProject.setGeneralObjective(updatedProject.getGeneralObjective());
        existingProject.setSpecificObjectives(updatedProject.getSpecificObjectives());
        existingProject.setPdfFilePath(updatedProject.getPdfFilePath());
        
        if (updatedProject.getModality() == ProjectModality.PROFESSIONAL_PRACTICE) {
            existingProject.setAcceptanceLetterPath(updatedProject.getAcceptanceLetterPath());
        }
        
        // Validar y guardar
        validateProject(existingProject);
        Project savedProject = projectRepository.save(existingProject);
        
        logger.info("Formato A re-enviado. Intento: " + savedProject.getAttemptNumber());
        return savedProject;
    }
    
    private void validateProject(Project project) throws ProjectException {
        if (project.getTitle() == null || project.getTitle().trim().isEmpty()) {
            throw new ProjectException("El título del proyecto es obligatorio");
        }
        
        if (project.getModality() == null) {
            throw new ProjectException("La modalidad del proyecto es obligatoria");
        }
        
        if (project.getGeneralObjective() == null || project.getGeneralObjective().trim().isEmpty()) {
            throw new ProjectException("El objetivo general es obligatorio");
        }
        
        if (project.getSpecificObjectives() == null || project.getSpecificObjectives().isEmpty()) {
            throw new ProjectException("Debe incluir al menos un objetivo específico");
        }
        
        if (project.getPdfFilePath() == null || project.getPdfFilePath().trim().isEmpty()) {
            throw new ProjectException("Debe adjuntar el archivo PDF del formato A");
        }
        
        // Validación específica para práctica profesional
        if (project.getModality() == ProjectModality.PROFESSIONAL_PRACTICE && 
            (project.getAcceptanceLetterPath() == null || project.getAcceptanceLetterPath().trim().isEmpty())) {
            throw new ProjectException("Para práctica profesional debe adjuntar la carta de aceptación de la empresa");
        }
    }
    
    public List<Project> getTeacherProjects(User teacher) {
        return projectRepository.findByTeacher(teacher);
    }
    
    public Project getProjectById(String projectId) throws ProjectException {
        return projectRepository.findById(projectId)
            .orElseThrow(() -> new ProjectException("Proyecto no encontrado: " + projectId));
    }
}