package co.unicauca.formata.config;
import org.springframework.amqp.core.*;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
@Configuration
public class RabbitMQConfig {    
    // Exchange
    public static final String EXCHANGE_NAME = "formato_events_exchange";    
    // Queues
    public static final String NOTIFICATIONS_QUEUE = "notifications_queue";
    public static final String EVALUATIONS_QUEUE = "evaluations_queue";    
    // Routing Keys
    public static final String FORMATO_ENVIADO_KEY = "formato.a.enviado";
    public static final String FORMATO_EVALUADO_KEY = "formato.a.evaluado";
    public static final String FORMATO_REINTENTADO_KEY = "formato.a.reintentado";    
    /**
     * Exchange para eventos de Formatos A
     */
    @Bean
    public TopicExchange exchange() {
        return new TopicExchange(EXCHANGE_NAME);
    }    
    /**
     * Cola para notificaciones
     */
    @Bean
    public Queue notificationsQueue() {
        return new Queue(NOTIFICATIONS_QUEUE, true);
    }    
    /**
     * Cola para evaluaciones
     */
    @Bean
    public Queue evaluationsQueue() {
        return new Queue(EVALUATIONS_QUEUE, true);
    }    
    /**
     * Binding para notificaciones
     */
    @Bean
    public Binding notificationsBinding(Queue notificationsQueue, TopicExchange exchange) {
        return BindingBuilder.bind(notificationsQueue)
                .to(exchange)
                .with(FORMATO_ENVIADO_KEY);
    }    
    /**
     * Binding para evaluaciones
     */
    @Bean
    public Binding evaluationsBinding(Queue evaluationsQueue, TopicExchange exchange) {
        return BindingBuilder.bind(evaluationsQueue)
                .to(exchange)
                .with(FORMATO_EVALUADO_KEY);
    }    
    /**
     * Binding para reintentos
     */
    @Bean
    public Binding reintentosBinding(Queue notificationsQueue, TopicExchange exchange) {
        return BindingBuilder.bind(notificationsQueue)
                .to(exchange)
                .with(FORMATO_REINTENTADO_KEY);
    }    
    /**
     * Convertidor JSON para mensajes
     */
    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}