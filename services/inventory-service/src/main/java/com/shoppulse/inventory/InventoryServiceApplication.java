package com.shoppulse.inventory;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Stock levels in MongoDB, exposed over gRPC (port 6565) + a small REST facade.
 * Build order (Block 2, steps I1-I4):
 *
 *  I1  StockDocument (@Document "stock": sku, available, reserved) + MongoRepository.
 *      Seed a few SKUs via a CommandLineRunner bean.
 *  I2  Run `gradlew :services:inventory-service:generateProto`, then implement
 *      @GrpcService class InventoryGrpcService extends
 *      InventoryServiceGrpc.InventoryServiceImplBase — override reserveStock:
 *      decrement available, increment reserved, atomic via findAndModify
 *      (interview: how do you avoid overselling under concurrency? optimistic
 *      locking / atomic update / unique reservation doc).
 *  I3  Test with grpcurl (or the REST facade):
 *        grpcurl -plaintext -d '{"sku":"SKU-1","quantity":2,"order_id":"o1"}' \
 *          localhost:6565 inventory.InventoryService/ReserveStock
 *  I4  Kafka listener on "inventory.release" -> put stock back (saga compensation).
 *
 * Mongo vs MySQL talking point: stock is a self-contained document, no joins
 * needed, schema evolves freely -> document store fits. Orders need transactions
 * with an outbox table -> relational fits. Pick per access pattern, not fashion.
 */
@SpringBootApplication
public class InventoryServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(InventoryServiceApplication.class, args);
    }
}
