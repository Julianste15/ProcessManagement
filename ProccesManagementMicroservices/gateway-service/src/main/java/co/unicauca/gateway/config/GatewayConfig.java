package co.unicauca.gateway.config;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
@Configuration
public class GatewayConfig {    
    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                // Ruta para auth-service (login, registro)
                .route("auth_route", r -> r
                    .path("/api/auth/**")
                    .uri("lb://auth-service"))                
                // Ruta para user-service
                .route("user_route", r -> r
                    .path("/api/users/**")
                    .uri("lb://user-service"))                
                // Ruta por defecto para servicios no encontrados
                .route("fallback_route", r -> r
                    .path("/**")
                    .filters(f -> f.rewritePath("/(?<segment>.*)", "/fallback/${segment}"))
                    .uri("lb://auth-service"))
                .build();
    }
}