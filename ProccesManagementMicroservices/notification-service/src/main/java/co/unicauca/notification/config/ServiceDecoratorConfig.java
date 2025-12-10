package co.unicauca.notification.config;

import co.unicauca.notification.decorators.SimpleLoggingEmailDecorator;
import co.unicauca.notification.service.EmailService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class ServiceDecoratorConfig {

    @Bean
    @Primary
    public EmailService emailService(@Qualifier("baseEmailService") EmailService baseService) {
        return new SimpleLoggingEmailDecorator(baseService);
    }
}
// Decorator configuration loaded
