package co.unicauca.anteproject.domain.ports.in;

import co.unicauca.anteproject.domain.model.Anteproject;
import co.unicauca.anteproject.domain.model.AnteprojectStatus;

public interface ManageAnteprojectUseCase {
    Anteproject submitDocument(Long anteprojectId, String documentUrl, String submittedBy);
    Anteproject assignEvaluators(Long anteprojectId, String evaluator1Email, String evaluator2Email, String assignedBy);
    Anteproject updateStatus(Long anteprojectId, AnteprojectStatus status, String updatedBy);
}
