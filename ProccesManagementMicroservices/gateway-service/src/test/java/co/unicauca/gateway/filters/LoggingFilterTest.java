package co.unicauca.gateway.filters;

import org.junit.jupiter.api.Test;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.mock.http.server.reactive.MockServerHttpRequest;
import org.springframework.mock.web.server.MockServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.concurrent.atomic.AtomicBoolean;

import static org.junit.jupiter.api.Assertions.assertTrue;

class LoggingFilterTest {

    @Test
    void loggingFilter_alwaysCallsChain() {
        LoggingFilter filter = new LoggingFilter();
        GatewayFilter gatewayFilter = filter.apply(new LoggingFilter.Config());

        MockServerHttpRequest request = MockServerHttpRequest
                .get("/api/any")
                .build();
        MockServerWebExchange exchange = MockServerWebExchange.from(request);

        AtomicBoolean chainCalled = new AtomicBoolean(false);
        GatewayFilterChain chain = ex -> {
            chainCalled.set(true);
            return Mono.empty();
        };

        gatewayFilter.filter(exchange, chain).block();

        assertTrue(chainCalled.get());
    }
}
