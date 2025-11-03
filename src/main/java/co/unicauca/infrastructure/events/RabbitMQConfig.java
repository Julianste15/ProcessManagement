package co.unicauca.infrastructure.events;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;

@Configuration
public class RabbitMQConfig {
    // 1. Definimos los mismos nombres que usamos en el Publicador
    public static final String EXCHANGE_NAME = "formato_events_exchange";
    public static final String ROUTING_KEY = "formato.a.enviado";
    
    // 2. Damos un nombre a nuestra nueva "cola"
    public static final String QUEUE_NAME = "notificaciones_queue";

    /**
     * Define el Exchange (el "cartero" que distribuye mensajes)
     */
    @Bean
    public TopicExchange exchange() {
        return new TopicExchange(EXCHANGE_NAME);
    }

    /**
     * Define la Cola (la "bandeja de entrada" para las notificaciones)
     */
    @Bean
    public Queue queue() {
        // durable(true) = la cola sobrevive si RabbitMQ se reinicia
        return new Queue(QUEUE_NAME, true);
    }

    /**
     * Define el "Binding" (la regla que conecta el Exchange con la Cola)
     * Le dice al Exchange: "Cualquier mensaje con esta ROUTING_KEY,
     * envíalo a esta QUEUE_NAME".
     */
    @Bean
    public Binding binding(Queue queue, TopicExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with(ROUTING_KEY);
    }
    
    /**
    * Define el "convertidor de mensajes".
    * Le dice a Spring que use JSON (Jackson) para serializar y deserializar
    * todos los objetos que se envíen o reciban por RabbitMQ.
    */
    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}
