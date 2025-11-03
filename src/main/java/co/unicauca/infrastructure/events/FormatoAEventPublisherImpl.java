package co.unicauca.infrastructure.events;
import co.unicauca.domain.entities.FormatoA;
import co.unicauca.domain.ports.IFormatoAEventPublisher;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
@Service
public class FormatoAEventPublisherImpl implements IFormatoAEventPublisher {
    // Constantes de configuración de RabbitMQ
    public static final String EXCHANGE_NAME = "formato_events_exchange";
    public static final String ROUTING_KEY = "formato.a.enviado";

    private final RabbitTemplate rabbitTemplate;

    @Autowired
    public FormatoAEventPublisherImpl(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    /**
     * Implementación real del método de publicación de eventos.
     * Envía el objeto FormatoA como mensaje JSON a RabbitMQ.
     */
    @Override
    public void publishFormatoAEnviado(FormatoA formatoA) {
        System.out.println("📤 Publicando evento en RabbitMQ...");
        System.out.println("Exchange: " + EXCHANGE_NAME);
        System.out.println("Routing Key: " + ROUTING_KEY);
        System.out.println("Enviando FormatoA con título: " + formatoA.getTitulo());

        try {
            // Spring convierte automáticamente el objeto 'formatoA' a JSON
            // y lo envía al exchange con la routing key especificada.
            rabbitTemplate.convertAndSend(EXCHANGE_NAME, ROUTING_KEY, formatoA);
            System.out.println("✅ ¡Evento publicado exitosamente!");
        } catch (Exception e) {
            System.err.println("❌ Error al publicar evento: " + e.getMessage());
            // Aquí podrías manejar el error (reintentos, logs, alertas, etc.)
        }
    }
}
