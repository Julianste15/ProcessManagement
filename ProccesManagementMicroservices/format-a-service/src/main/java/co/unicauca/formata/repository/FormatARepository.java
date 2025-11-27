package co.unicauca.formata.repository;
import co.unicauca.formata.model.FormatoA;
import co.unicauca.formata.model.EstadoProyecto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
@Repository
public interface FormatARepository extends JpaRepository<FormatoA, Long> {    
    /**
     * Busca Formatos A por director o codirector
     */
    List<FormatoA> findByDirectorEmailOrCodirectorEmail(String directorEmail, String codirectorEmail);

    /**
     * Busca Formatos A por director, codirector o estudiante
     */
    List<FormatoA> findByDirectorEmailOrCodirectorEmailOrStudentEmail(String directorEmail, String codirectorEmail, String studentEmail);

    /**
     * Busca Formatos A por estado
     */
    List<FormatoA> findByEstado(EstadoProyecto estado);    
    /**
     * Busca Formatos A por título (búsqueda parcial)
     */
    @Query("SELECT f FROM FormatoA f WHERE LOWER(f.titulo) LIKE LOWER(CONCAT('%', :titulo, '%'))")
    List<FormatoA> findByTituloContainingIgnoreCase(@Param("titulo") String titulo);    
    /**
     * Busca Formatos A por modalidad
     */
    List<FormatoA> findByModalidad(co.unicauca.formata.model.Modalidad modalidad);    
    /**
     * Busca Formatos A pendientes de evaluación
     */
    @Query("SELECT f FROM FormatoA f WHERE f.estado = 'FORMATO_A_EN_EVALUACION' ORDER BY f.fechaCreacion ASC")
    List<FormatoA> findPendientesEvaluacion();    
    /**
     * Cuenta intentos de un usuario
     */
    @Query("SELECT COUNT(f) FROM FormatoA f WHERE f.directorEmail = :email AND f.estado = 'FORMATO_A_RECHAZADO'")
    long countIntentosFallidosByUser(@Param("email") String email);

    /**
     * Busca los formatos A de un estudiante ordenados por fecha de creación
     * descendente
     */
    List<FormatoA> findByStudentEmailOrderByFechaCreacionDesc(String studentEmail);
}