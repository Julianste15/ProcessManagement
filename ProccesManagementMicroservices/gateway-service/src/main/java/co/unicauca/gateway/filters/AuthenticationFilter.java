package co.unicauca.gateway.filters;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import javax.crypto.SecretKey;
import java.util.List;
import java.util.logging.Logger;
import io.jsonwebtoken.Claims;
@Component
public class AuthenticationFilter extends AbstractGatewayFilterFactory<AuthenticationFilter.Config> {    
    private static final Logger logger = Logger.getLogger(AuthenticationFilter.class.getName());    
    @Value("${jwt.secret}")
    private String jwtSecret;    
    public AuthenticationFilter() {
        super(Config.class);
    }    
    public static class Config {}    
    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(jwtSecret.getBytes());
    }    
    private boolean isAuthMissing(ServerHttpRequest request) {
        return !request.getHeaders().containsKey("Authorization");
    }    
    private String getAuthHeader(ServerHttpRequest request) {
        List<String> headers = request.getHeaders().getOrEmpty("Authorization");
        return headers.isEmpty() ? null : headers.get(0);
    }    
    private boolean isBearerToken(String authHeader) {
        return authHeader != null && authHeader.startsWith("Bearer ");
    }    
    private boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            logger.warning("Token JWT inválido: " + e.getMessage());
            return false;
        }
    }    
    private Claims getAllClaimsFromToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }    
    private void populateRequestWithHeaders(ServerWebExchange exchange, String token) {
        Claims claims = getAllClaimsFromToken(token);
        
        exchange.getRequest().mutate()
                .header("X-User-Email", claims.getSubject())
                .header("X-User-Role", claims.get("role", String.class))
                .build();
    }    
    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();
            String path = request.getPath().toString();            
            logger.info("Procesando ruta: " + path);            
            if (isPublicEndpoint(path)) {
                logger.info("Endpoint público, omitiendo autenticación: " + path);
                return chain.filter(exchange);
            }            
            if (isAuthMissing(request)) {
                logger.warning("Falta header Authorization en ruta protegida: " + path);
                exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                return exchange.getResponse().setComplete();
            }            
            String authHeader = getAuthHeader(request);            
            if (!isBearerToken(authHeader)) {
                logger.warning("Formato de token inválido");
                exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                return exchange.getResponse().setComplete();
            }            
            String token = authHeader.substring(7);
            if (!validateToken(token)) {
                logger.warning("Token JWT inválido");
                exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                return exchange.getResponse().setComplete();
            }            
            populateRequestWithHeaders(exchange, token);            
            logger.info("Autenticación exitosa para: " + getAllClaimsFromToken(token).getSubject());
            return chain.filter(exchange);
        };
    }
    private boolean isPublicEndpoint(String path) {
        return path.equals("/api/auth/login") || 
               path.startsWith("/api/auth/validate") ||
               path.equals("/api/users/register") ||
               path.startsWith("/actuator/") ||
               path.equals("/eureka/") ||
               path.startsWith("/h2-console/");
    }
}