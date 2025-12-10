package co.unicauca.anteproject.infrastructure.output.persistence;

import co.unicauca.anteproject.domain.model.Anteproject;
import co.unicauca.anteproject.domain.model.AnteprojectStatus;
import co.unicauca.anteproject.domain.ports.out.AnteprojectRepositoryPort;
import co.unicauca.anteproject.infrastructure.output.persistence.entity.AnteprojectEntity;
import co.unicauca.anteproject.infrastructure.output.persistence.mapper.AnteprojectMapper;
import co.unicauca.anteproject.infrastructure.output.persistence.repository.JpaAnteprojectRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class AnteprojectPersistenceAdapter implements AnteprojectRepositoryPort {

    private final JpaAnteprojectRepository jpaRepository;
    private final AnteprojectMapper mapper;

    public AnteprojectPersistenceAdapter(JpaAnteprojectRepository jpaRepository, AnteprojectMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }

    @Override
    public Anteproject save(Anteproject anteproject) {
        AnteprojectEntity entity = mapper.toEntity(anteproject);
        AnteprojectEntity savedEntity = jpaRepository.save(entity);
        return mapper.toDomain(savedEntity);
    }

    @Override
    public Optional<Anteproject> findById(Long id) {
        return jpaRepository.findById(id)
                .map(mapper::toDomain);
    }

    @Override
    public Optional<Anteproject> findByFormatoAId(Long formatoAId) {
        return jpaRepository.findByFormatoAId(formatoAId)
                .map(mapper::toDomain);
    }

    @Override
    public List<Anteproject> findByStudentEmail(String studentEmail) {
        return jpaRepository.findByStudentEmail(studentEmail).stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Anteproject> findByDirectorEmail(String directorEmail) {
        return jpaRepository.findByDirectorEmail(directorEmail).stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Anteproject> findByStatusIn(List<AnteprojectStatus> statuses) {
        return jpaRepository.findByStatusIn(statuses).stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public void delete(Anteproject anteproject) {
        jpaRepository.delete(mapper.toEntity(anteproject));
    }

    @Override
    public List<Anteproject> findAll() {
        return jpaRepository.findAll().stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }
}
