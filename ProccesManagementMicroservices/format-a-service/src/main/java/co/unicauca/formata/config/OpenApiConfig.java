package co.unicauca.formata.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI formatAOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Format A Service API")
                        .description("API for managing Format A submissions in the Process Management System")
                        .version("1.0")
                        .contact(new Contact()
                                .name("Process Management Team")
                                .email("support@unicauca.edu.co")));
    }
}
