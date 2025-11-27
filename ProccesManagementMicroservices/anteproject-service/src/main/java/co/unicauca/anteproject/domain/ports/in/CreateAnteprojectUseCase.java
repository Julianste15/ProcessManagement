package co.unicauca.anteproject.domain.ports.in;

import co.unicauca.anteproject.domain.model.Anteproject;
import co.unicauca.anteproject.dto.CreateAnteprojectRequest;

public interface CreateAnteprojectUseCase {
    Anteproject createAnteproject(CreateAnteprojectRequest request);
}
