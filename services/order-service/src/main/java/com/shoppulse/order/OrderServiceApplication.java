package com.shoppulse.order;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * The saga orchestrator's home. THE most valuable service for interviews.
 * Build order (Block 2, steps S1-S7):
 *
 *  S1  OrderEntity: id, customerId, sku, qty, amount,
 *      status ENUM: PENDING -> INVENTORY_RESERVED -> CONFIRMED | REJECTED | COMPENSATED.
 *      Use a SWITCH EXPRESSION over the enum for state transitions.
 *  S2  POST /api/orders -> save PENDING -> publish "order.created" to Kafka
 *      (KafkaTemplate, key = orderId so all events for one order hit ONE
 *      partition = ordered. Know why keys matter!).
 *  S3  gRPC call to inventory-service ReserveStock (stubs are generated from
 *      src/main/proto/inventory.proto by `gradlew generateProto`).
 *  S4  SAGA (choreography): listen for "payment.completed" -> CONFIRMED;
 *      "payment.failed" -> publish "inventory.release" (COMPENSATION) -> COMPENSATED.
 *      Then the drill: kill payment-service mid-saga, watch the order stay
 *      PENDING, restart, watch the consumer catch up. That's your interview story.
 *  S5  CompletableFuture: GET /api/orders/{id}/summary fans out to product +
 *      inventory in PARALLEL:
 *        var p = CompletableFuture.supplyAsync(() -> productClient.get(sku), pool);
 *        var s = CompletableFuture.supplyAsync(() -> stockClient.get(sku), pool);
 *        p.thenCombine(s, Summary::new).orTimeout(2, SECONDS).join();
 *      Define your own ThreadPoolTaskExecutor bean — know queue size vs core
 *      vs max pool sizing, and what happens when the queue fills (rejection policy).
 *  S6  Resilience4j @CircuitBreaker + @Retry on the product client, fallback
 *      method. Flip product-service off and watch the breaker OPEN (actuator
 *      /actuator/circuitbreakers). This is your cascading-failure defense.
 *  S7  (Discussion) Outbox pattern: why "DB commit + kafka publish" is a
 *      dual-write bug, and how Debezium on an outbox table fixes it.
 *
 * @EnableAsync gives you @Async for the sync-vs-async comparison endpoint.
 */
@EnableAsync
@SpringBootApplication
public class OrderServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(OrderServiceApplication.class, args);
    }
}
