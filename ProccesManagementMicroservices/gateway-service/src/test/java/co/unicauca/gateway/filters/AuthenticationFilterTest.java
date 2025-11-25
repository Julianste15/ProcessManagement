package co.unicauca.gateway.filters;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.http.HttpStatus;
import org.springframework.mock.http.server.reactive.MockServerHttpRequest;
import org.springframework.mock.web.server.MockServerWebExchange;
import org.springframework.test.util.ReflectionTestUtils;
import reactor.core.publisher.Mono;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.junit.jupiter.api.Assertions.*;

class AuthenticationFilterTest {

    private static final String JWT_SECRET = "mi-clave-secreta-muy-segura-para-el-proyecto-de-grado-2025-fiet-unicauca";
    private AuthenticationFilter filter;

    @BeforeEach
    void setUp() {
        filter = new AuthenticationFilter();
        ReflectionTestUtils.setField(filter, "jwtSecret", JWT_SECRET);
    }

    @Test
    void publicEndpoint_allowsWithoutAuthorizationHeader() {
        GatewayFilter gatewayFilter = filter.apply(new AuthenticationFilter.Config());

        MockServerHttpRequest request = MockServerHttpRequest
                .get("/api/auth/login")
                .build();
        MockServerWebExchange exchange = MockServerWebExchange.from(request);

        AtomicBoolean chainCalled = new AtomicBoolean(false);
        GatewayFilterChain chain = ex -> {
            chainCalled.set(true);
            return Mono.empty();
        };

        gatewayFilter.filter(exchange, chain).block();

        assertTrue(chainCalled.get());
        assertNull(exchange.getResponse().getStatusCode());
    }

    @Test
    void protectedEndpoint_withoutAuthorization_returnsUnauthorized() {
        GatewayFilter gatewayFilter = filter.apply(new AuthenticationFilter.Config());

        MockServerHttpRequest request = MockServerHttpRequest
                .get("/api/format-a/some-endpoint")
                .build();
        MockServerWebExchange exchange = MockServerWebExchange.from(request);

        GatewayFilterChain chain = ex -> Mono.empty();

        gatewayFilter.filter(exchange, chain).block();

        assertEquals(HttpStatus.UNAUTHORIZED, exchange.getResponse().getStatusCode());
    }

    @Test
    void protectedEndpoint_withValidToken_noRoleRestriction_allowsAccess() {
        String token = generateToken("student@unicauca.edu.co", "STUDENT");
        GatewayFilter gatewayFilter = filter.apply(new AuthenticationFilter.Config());

        MockServerHttpRequest request = MockServerHttpRequest
                .get("/api/format-a/some-endpoint")
                .header("Authorization", "Bearer " + token)
                .build();
        MockServerWebExchange exchange = MockServerWebExchange.from(request);

        AtomicBoolean chainCalled = new AtomicBoolean(false);
        GatewayFilterChain chain = ex -> {
            chainCalled.set(true);
            return Mono.empty();
        };

        gatewayFilter.filter(exchange, chain).block();

        assertTrue(chainCalled.get());
        assertNull(exchange.getResponse().getStatusCode());
    }

    @Test
    void protectedEndpoint_withAuthorizedRole_allowsAccess() {
        String token = generateToken("teacher@unicauca.edu.co", "TEACHER");
        
        AuthenticationFilter.Config config = new AuthenticationFilter.Config();
        config.setAllowedRoles("TEACHER,COORDINATOR");
        GatewayFilter gatewayFilter = filter.apply(config);

        MockServerHttpRequest request = MockServerHttpRequest
                .get("/api/evaluations/some-endpoint")
                .header("Authorization", "Bearer " + token)
                .build();
        MockServerWebExchange exchange = MockServerWebExchange.from(request);

        AtomicBoolean chainCalled = new AtomicBoolean(false);
        GatewayFilterChain chain = ex -> {
            chainCalled.set(true);
            return Mono.empty();
        };

        gatewayFilter.filter(exchange, chain).block();

        assertTrue(chainCalled.get());
        assertNull(exchange.getResponse().getStatusCode());
    }

    @Test
    void protectedEndpoint_withUnauthorizedRole_returnsForbidden() {
        String token = generateToken("student@unicauca.edu.co", "STUDENT");
        
        AuthenticationFilter.Config config = new AuthenticationFilter.Config();
        config.setAllowedRoles("TEACHER,COORDINATOR");
        GatewayFilter gatewayFilter = filter.apply(config);

        MockServerHttpRequest request = MockServerHttpRequest
                .get("/api/evaluations/some-endpoint")
                .header("Authorization", "Bearer " + token)
                .build();
        MockServerWebExchange exchange = MockServerWebExchange.from(request);

        GatewayFilterChain chain = ex -> Mono.empty();

        gatewayFilter.filter(exchange, chain).block();

        assertEquals(HttpStatus.FORBIDDEN, exchange.getResponse().getStatusCode());
    }

    @Test
    void protectedEndpoint_roleMatchingIsCaseInsensitive() {
        String token = generateToken("teacher@unicauca.edu.co", "teacher");
        
        AuthenticationFilter.Config config = new AuthenticationFilter.Config();
        config.setAllowedRoles("TEACHER,COORDINATOR");
        GatewayFilter gatewayFilter = filter.apply(config);

        MockServerHttpRequest request = MockServerHttpRequest
                .get("/api/evaluations/some-endpoint")
                .header("Authorization", "Bearer " + token)
                .build();
        MockServerWebExchange exchange = MockServerWebExchange.from(request);

        AtomicBoolean chainCalled = new AtomicBoolean(false);
        GatewayFilterChain chain = ex -> {
            chainCalled.set(true);
            return Mono.empty();
        };

        gatewayFilter.filter(exchange, chain).block();

        assertTrue(chainCalled.get());
        assertNull(exchange.getResponse().getStatusCode());
    }

    @Test
    void protectedEndpoint_withInvalidToken_returnsUnauthorized() {
        String invalidToken = "invalid.jwt.token";
        
        AuthenticationFilter.Config config = new AuthenticationFilter.Config();
        config.setAllowedRoles("TEACHER");
        GatewayFilter gatewayFilter = filter.apply(config);

        MockServerHttpRequest request = MockServerHttpRequest
                .get("/api/evaluations/some-endpoint")
                .header("Authorization", "Bearer " + invalidToken)
                .build();
        MockServerWebExchange exchange = MockServerWebExchange.from(request);

        GatewayFilterChain chain = ex -> Mono.empty();

        gatewayFilter.filter(exchange, chain).block();

        assertEquals(HttpStatus.UNAUTHORIZED, exchange.getResponse().getStatusCode());
    }

    /**
     * Helper method to generate a valid JWT token for testing
     */
    private String generateToken(String email, String role) {
        SecretKey key = Keys.hmacShaKeyFor(JWT_SECRET.getBytes());
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + 86400000); // 24 hours

        return Jwts.builder()
                .setSubject(email)
                .claim("role", role)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }
}
