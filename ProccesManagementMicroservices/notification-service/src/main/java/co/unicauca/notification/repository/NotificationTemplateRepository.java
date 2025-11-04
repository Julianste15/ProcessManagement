package co.unicauca.notification.repository;

import co.unicauca.notification.model.NotificationTemplate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface NotificationTemplateRepository extends JpaRepository<NotificationTemplate, Long> {
    
    /**
     * Busca template por tipo
     */
    Optional<NotificationTemplate> findByTemplateType(String templateType);
    
    /**
     * Verifica si existe un template por tipo
     */
    boolean existsByTemplateType(String templateType);
    
    /**
     * Busca templates por nombre (búsqueda parcial)
     */
    java.util.List<NotificationTemplate> findByTemplateNameContainingIgnoreCase(String templateName);
}