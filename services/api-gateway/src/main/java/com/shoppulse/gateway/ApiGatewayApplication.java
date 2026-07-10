package com.shoppulse.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Single entry point for all clients. Responsibilities (each is a Block 1/2 task):
 *  1. Route /api/products/** -> product-service, /api/orders/** -> order-service ...
 *     via Eureka (lb://SERVICE-NAME) — client-side load balancing.
 *  2. Validate the JWT bearer token in a GlobalFilter, reject 401 before any
 *     service is touched. (Auth at the edge; services trust the forwarded identity.)
 *  3. Rate limiting with the Redis token-bucket filter (throttling).
 *  4. Circuit breaker per route (cascading-failure defense).
 *
 * TODO Block 1 (G1): declare routes in application.yml
 * TODO Block 1 (G3): write JwtAuthenticationFilter implements GlobalFilter, Ordered
 * TODO Block 2 (S6): add RequestRateLimiter + CircuitBreaker filters to routes
 */
@SpringBootApplication
public class ApiGatewayApplication {
    public static void main(String[] args) {
        SpringApplication.run(ApiGatewayApplication.class, args);
    }
}
