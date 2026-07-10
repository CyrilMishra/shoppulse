package com.shoppulse.admin;

import de.codecentric.boot.admin.server.config.EnableAdminServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Spring Boot Admin: http://localhost:8087 — live health, metrics, env,
 * loggers (change log levels at RUNTIME — try it), thread dumps, heap dumps
 * for every service it finds in Eureka. Zero config per service beyond
 * exposing actuator endpoints (already done in each application.yml).
 */
@EnableAdminServer
@SpringBootApplication
public class AdminServerApplication {
    public static void main(String[] args) {
        SpringApplication.run(AdminServerApplication.class, args);
    }
}
