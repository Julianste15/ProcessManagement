package co.unicauca.anteproject.infrastructure.output.persistence.mapper;

import co.unicauca.anteproject.domain.model.Anteproject;
import co.unicauca.anteproject.infrastructure.output.persistence.entity.AnteprojectEntity;
import org.springframework.stereotype.Component;

@Component
public class AnteprojectMapper {

    public Anteproject toDomain(AnteprojectEntity entity) {
        if (entity == null) {
            return null;
        }
        return new Anteproject(
            entity.getId(),
            entity.getFormatoAId(),
            entity.getTitulo(),
            entity.getStudentEmail(),
            entity.getDirectorEmail(),
            entity.getDocumentUrl(),
            entity.getStatus(),
            entity.getSubmissionDate(),
            entity.getEvaluationDeadline(),
            entity.getCreatedAt(),
            entity.getUpdatedAt()
        );
    }

    public AnteprojectEntity toEntity(Anteproject domain) {
        if (domain == null) {
            return null;
        }
        AnteprojectEntity entity = new AnteprojectEntity();
        entity.setId(domain.getId());
        entity.setFormatoAId(domain.getFormatoAId());
        entity.setTitulo(domain.getTitulo());
        entity.setStudentEmail(domain.getStudentEmail());
        entity.setDirectorEmail(domain.getDirectorEmail());
        entity.setDocumentUrl(domain.getDocumentUrl());
        entity.setStatus(domain.getStatus());
        entity.setSubmissionDate(domain.getSubmissionDate());
        entity.setEvaluationDeadline(domain.getEvaluationDeadline());
        entity.setCreatedAt(domain.getCreatedAt());
        entity.setUpdatedAt(domain.getUpdatedAt());
        return entity;
    }
}
