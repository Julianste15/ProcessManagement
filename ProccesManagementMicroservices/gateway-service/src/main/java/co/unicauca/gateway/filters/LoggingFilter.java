package co.unicauca.gateway.filters;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import java.util.logging.Logger;
@Component
public class LoggingFilter extends AbstractGatewayFilterFactory<LoggingFilter.Config> {    
    private static final Logger logger = Logger.getLogger(LoggingFilter.class.getName());    
    public LoggingFilter() {
        super(Config.class);
    }    
    public static class Config {
        // Configuración vacía
    }    
    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            var request = exchange.getRequest();            
            logger.info("=== GATEWAY REQUEST ===");
            logger.info("Method: " + request.getMethod());
            logger.info("Path: " + request.getPath());
            logger.info("Headers: " + request.getHeaders());
            logger.info("Remote Address: " + request.getRemoteAddress());            
            return chain.filter(exchange).then(Mono.fromRunnable(() -> {
                var response = exchange.getResponse();                
                logger.info("=== GATEWAY RESPONSE ===");
                logger.info("Status: " + response.getStatusCode());
                logger.info("Headers: " + response.getHeaders());
                logger.info("========================\n");
            }));
        };
    }
}