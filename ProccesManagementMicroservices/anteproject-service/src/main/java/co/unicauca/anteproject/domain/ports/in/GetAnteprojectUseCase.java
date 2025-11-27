package co.unicauca.anteproject.domain.ports.in;

import co.unicauca.anteproject.domain.model.Anteproject;
import java.util.List;

public interface GetAnteprojectUseCase {
    Anteproject getAnteprojectById(Long id);
    List<Anteproject> getAnteprojectsByStudent(String studentEmail);
    List<Anteproject> getAnteprojectsByDirector(String directorEmail);
    List<Anteproject> getSubmittedAnteprojectsForDepartmentHead();
}
