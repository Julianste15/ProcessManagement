/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package co.unicauca.infrastructure.events.consumers;

import co.unicauca.domain.entities.FormatoA;
import co.unicauca.infrastructure.events.RabbitMQConfig;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 *
 * @author AN515-54-55MX
 */
@Component
public class NotificationEventConsumer {
    @RabbitListener(queues = RabbitMQConfig.QUEUE_NAME)
    public void handleFormatoAEnviado(FormatoA formatoA) {
        
        System.out.println("=============================================");
        System.out.println("¡EVENTO RECIBIDO EN EL SERVICIO DE NOTIFICACIONES!");
        System.out.println("=============================================");
        System.out.println("Simulando envío de email al coordinador...");
        System.out.println("Título del Proyecto: " + formatoA.getTitulo());
        System.out.println("Modalidad: " + formatoA.getModalidad());
        System.out.println("Estado: " + formatoA.getEstado());
        System.out.println("---------------------------------------------");
        
        // Aquí iría la lógica real para enviar un email
        // (Ej: usando JavaMailSender)
    }
}
