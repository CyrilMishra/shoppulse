package com.shoppulse.eureka;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

/**
 * Service registry. Every other service registers here on startup (heartbeats
 * every 30s by default) and discovers peers by application name.
 *
 * Interview points to learn while running this:
 * - client-side discovery (Eureka) vs server-side discovery (K8s Service + DNS)
 * - self-preservation mode: if too many heartbeats go missing at once, Eureka
 *   assumes a network partition and STOPS evicting instances (AP over CP — CAP!)
 * - compare with Consul (CP, health checks) and K8s (registry is etcd + kubelet)
 */
@EnableEurekaServer
@SpringBootApplication
public class EurekaServerApplication {
    public static void main(String[] args) {
        SpringApplication.run(EurekaServerApplication.class, args);
    }
}
