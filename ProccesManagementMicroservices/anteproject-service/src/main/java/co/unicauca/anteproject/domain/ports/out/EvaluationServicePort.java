package co.unicauca.anteproject.domain.ports.out;

/**
 * Puerto de salida para comunicación con el servicio de evaluación
 */
public interface EvaluationServicePort {

    /**
     * Asigna evaluadores a un anteproyecto
     * 
     * @param anteprojectId   ID del anteproyecto
     * @param evaluator1Email Email del primer evaluador
     * @param evaluator2Email Email del segundo evaluador
     */
    void assignEvaluators(Long anteprojectId, String evaluator1Email, String evaluator2Email);
}
