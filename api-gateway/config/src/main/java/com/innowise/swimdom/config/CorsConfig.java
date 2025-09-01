package com.innowise.swimdom.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.server.WebFilter;
import reactor.core.publisher.Mono;

/**
 * Configuration class for CORS (Cross-Origin Resource Sharing) settings.
 * Allows configuring allowed origins, methods and headers for CORS requests.
 */
@Configuration
@RequiredArgsConstructor
public class CorsConfig {

    /**
     * Allowed origins, methods and headers.
     */
    @Bean
    public WebFilter corsFilter() {
        return (exchange, chain) -> {
            ServerHttpResponse response = exchange.getResponse();
            HttpHeaders headers = response.getHeaders();
            headers.add("Access-Control-Allow-Origin", "*");
            headers.add("Access-Control-Allow-Methods", "GET, PUT, POST, DELETE, PATCH, OPTIONS");
            headers.add("Access-Control-Allow-Headers", "*");
            ServerHttpRequest request = exchange.getRequest();
            
            if (request.getMethod() == HttpMethod.OPTIONS) {
                response.getHeaders().add("Access-Control-Max-Age", "3600");
                response.setStatusCode(HttpStatus.OK);
                return Mono.empty();
            } else {
                return chain.filter(exchange);
            }
        };
    }
}
