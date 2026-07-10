# Block 2 — Distributed systems: Kafka, SAGA, gRPC, CDC (Sat, ~9h)

Goal: place an order through the gateway and watch it flow
order → (gRPC) inventory → (Kafka) payment → (Kafka) confirmed/compensated,
then break it on purpose. Finish with Debezium streaming MySQL changes into
Elasticsearch. **This block produces your best interview stories — narrate what
you see out loud as you go.**

Covers: Kafka, SAGA + rollback/compensation, gRPC, MongoDB, DynamoDB, SQS,
Debezium, Elasticsearch, circuit breaker/cascading failure, throttling,
debouncing, CompletableFuture, multithreading, sync/async, switch expressions,
webhooks, design patterns (saga, outbox, strategy, circuit breaker).

## 0. Read first (30 min, seriously — do not skip)
microservices.io → "Pattern: Saga", "Pattern: Transactional outbox",
"Pattern: Circuit Breaker". Chris Richardson's diagrams are exactly what you're
about to build.

## 1. Kafka hands-on tour (~45 min)
Console producer/consumer from inside the container; then two consumers SAME
group-id (messages split — partition assignment) vs DIFFERENT group-ids (both
get everything — pub/sub). Watch rebalancing happen when you kill one. Check
consumer lag in Kafka UI (:8090). Vocabulary you must own: topic, partition,
offset, key→partition hashing, consumer group, rebalance, retention, lag.

## 2. inventory-service (~1.5h) — steps I1-I4 in InventoryServiceApplication.java
`.\gradlew :services:inventory-service:generateProto` first, then implement.
Inspect generated code in `build/generated/source/proto` — THAT's contract-first.

## 3. order-service (~2h) — steps S1-S5 in OrderServiceApplication.java
S5 (CompletableFuture fan-out) is a top-3 Java interview topic — don't skip it.
Time the sequential version vs the parallel version with 2 slow downstream
calls; the numbers make the story.

## 4. payment-service (~1.5h) — steps Y1-Y4 in PaymentServiceApplication.java
```powershell
docker compose --profile aws up -d
docker compose exec localstack awslocal sqs create-queue --queue-name payment-retry
# inspect DynamoDB after a payment:
docker run --rm --network host amazon/aws-cli --endpoint-url http://localhost:8002 --region us-east-1 dynamodb scan --table-name payments
```

## 5. THE SAGA DRILL (~1h) — the centerpiece
1. Happy path: POST an order, follow the events in Kafka UI, status CONFIRMED.
2. Payment failure: make the simulator fail → watch inventory.release fire →
   stock restored → order COMPENSATED. **That is a distributed rollback.**
3. Crash drill: kill payment-service, place an order (stuck PENDING —
   inventory reserved!), restart payment → consumer resumes at its committed
   offset → saga completes. Durable log > RPC.
4. Idempotency drill: restart with `auto-offset-reset: earliest` and no
   idempotency check → double-charge. Add "payment exists?" check → safe.
   At-least-once delivery ⇒ idempotent consumers. Say that sentence in every
   messaging interview.

## 6. Resilience: cascading failure defense (~1h) — S6 + G-filters
Order-service @CircuitBreaker/@Retry+fallback on its product-service call
(config already in application.yml). Kill product-service, fire 15 requests,
watch /actuator/circuitbreakers go CLOSED→OPEN (fail-fast, no thread pile-up)
→ HALF_OPEN → CLOSED on recovery. Then gateway: RequestRateLimiter (Redis
token bucket — hammer it in a loop, get 429s) + CircuitBreaker filter with
fallbackUri.
Concepts to nail: cascading failure anatomy (slow dependency → thread-pool
exhaustion → caller starves → repeat upstream); defenses = timeouts, circuit
breakers, bulkheads, load shedding. **Throttling** (drop above N/sec — you
built it) vs **debouncing** (wait for quiet period — client-side, e.g. search
box; know it as a contrast).

## 7. Debezium CDC → Elasticsearch (~1.5h) — steps D1-D4
```powershell
docker compose --profile cdc --profile search up -d
curl -X POST -H "Content-Type: application/json" --data @infra/debezium/register-products-connector.json http://localhost:8383/connectors
curl http://localhost:8383/connectors/products-connector/status
```
Topic `shoppulse.productdb.products` appears in Kafka UI with a snapshot of
existing rows. Update a product via your API → a change event with `before`/
`after` payloads arrives. Then D3/D4: uncomment kafka+ES deps in
product-service, listener indexes into ES, add
`GET /api/products/search?q=runing` — fuzzy match works, MySQL LIKE can't.
Interview: how Debezium works (binlog tailing as a fake replica), why CDC beats
dual-writes, snapshot vs streaming phase, schema history topic.

## 8. notification-service N1 + webhook (~30 min) — see its class javadoc
Kafka listener on order.confirmed + the signed webhook receiver (payment Y4).
Full pipeline demo: POST order → gateway → saga → notification logs "email sent".

## Interview questions to answer out loud
1. Draw your saga. Why not a distributed transaction/2PC? (blocking, coordinator SPOF, availability — BASE over ACID)
2. Choreography vs orchestration — which did you build, when would you switch? (many steps / visibility / centralized error handling → orchestrator like Temporal)
3. What guarantees ordering in Kafka? (partition + key) Exactly-once vs at-least-once — what did YOU do about duplicates?
4. What happens when a consumer in a group dies mid-message? (rebalance, uncommitted offset redelivered → idempotency again)
5. gRPC vs REST: when and why? (internal S2S, contract-first, streaming, ~5-10x serialization win; browser support/debuggability favor REST)
6. Outbox pattern: what breaks without it? (commit succeeds, publish fails → phantom order)
7. Circuit breaker states + what each protects. What's a bulkhead?
8. SQS visibility timeout? DLQ? Why must every queue consumer be idempotent?
9. CompletableFuture: thenApply vs thenCompose vs thenCombine; which pool runs supplyAsync without an executor and why is that dangerous (shared ForkJoinPool.commonPool starvation)?
10. DynamoDB vs MongoDB vs MySQL — justify each choice IN THIS SYSTEM.

## Resources
- Confluent Developer "Kafka 101" (free course) — watch at 1.5x during breaks
- microservices.io (saga/outbox/CDC) — canonical
- Hussein Nasser: "gRPC crash course" (YouTube)
- Debezium docs: "Tutorial" page (you're literally following it)
- resilience4j docs: CircuitBreaker section (the state-machine diagram)
