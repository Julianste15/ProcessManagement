package co.unicauca.gateway.config;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.WebTestClient;

/**
 * Verifica que el gateway aplique la allowlist de CORS configurada en
 * application.yml y rechace orígenes no permitidos.
 */
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        properties = {
                "jwt.secret=test-secret-key-0123456789abcdef0123456789",
                "eureka.client.enabled=false",
                "spring.cloud.discovery.enabled=false",
                "CORS_ALLOWED_ORIGINS=http://localhost:8080"
        })
class CorsConfigurationTest {

    private static final String ALLOWED_ORIGIN = "http://localhost:8080";
    private static final String FORBIDDEN_ORIGIN = "http://evil.example";

    @Autowired
    private WebTestClient webTestClient;

    @Test
    @DisplayName("Un origen permitido recibe las cabeceras CORS")
    void allowedOriginGetsCorsHeaders() {
        webTestClient.options()
                .uri("/api/auth/login")
                .header("Origin", ALLOWED_ORIGIN)
                .header("Access-Control-Request-Method", "POST")
                .exchange()
                .expectStatus().isOk()
                .expectHeader().valueEquals("Access-Control-Allow-Origin", ALLOWED_ORIGIN);
    }

    @Test
    @DisplayName("Un origen no permitido es rechazado")
    void forbiddenOriginIsRejected() {
        webTestClient.options()
                .uri("/api/auth/login")
                .header("Origin", FORBIDDEN_ORIGIN)
                .header("Access-Control-Request-Method", "POST")
                .exchange()
                .expectStatus().isForbidden()
                .expectHeader().doesNotExist("Access-Control-Allow-Origin");
    }
}
