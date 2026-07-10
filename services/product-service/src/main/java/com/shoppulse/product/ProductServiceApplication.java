package com.shoppulse.product;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

/**
 * Product catalog: MySQL + JPA, Redis cache, Swagger, Elasticsearch search.
 * Build order (Block 1 steps P1-P6, Block 2 steps D1-D4):
 *
 *  P1  ProductEntity (@Entity, table "products": id, sku, name, description,
 *      price, category, createdAt) + ProductRepository extends JpaRepository.
 *  P2  DTOs as Java RECORDS: record ProductResponse(Long id, String sku, ...) {}
 *      Map entity->record in the service layer using STREAMS:
 *        repo.findAll().stream().filter(...).map(this::toResponse).toList()
 *      Return OPTIONAL from single lookups; controller does .orElseThrow().
 *      Use a PREDICATE<ProductEntity> field for reusable filters and compose
 *      with .and()/.or() — that's your Predicates story.
 *  P3  REST controller: GET/POST/PUT/DELETE /api/products.
 *      Get the semantics right: 201+Location on create, 204 on delete,
 *      PUT is idempotent / POST is not — interviewers love this.
 *  P4  @Cacheable("products") on getById, @CacheEvict on update/delete.
 *      Watch Redis: docker compose exec redis redis-cli MONITOR
 *      This is CACHE-ASIDE. Know read-through/write-through/write-behind too,
 *      and L1 (Caffeine, in-process) vs L2 (Redis, shared) — try both CacheManagers.
 *  P5  Swagger: open http://localhost:8082/swagger-ui.html — annotate one
 *      endpoint with @Operation/@ApiResponse so you've touched the annotations.
 *  P6  Deliberately create an N+1: add a lazy @OneToMany (reviews), loop over
 *      products calling getReviews(), watch show-sql explode, fix with join fetch.
 *      THE classic Hibernate interview question — now you've SEEN it.
 *
 *  D3  (Block 2) Kafka listener on Debezium topic "shoppulse.productdb.products"
 *      -> index into Elasticsearch. D4: GET /api/products/search?q= via ES.
 */
@EnableCaching
@SpringBootApplication
public class ProductServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(ProductServiceApplication.class, args);
    }
}
