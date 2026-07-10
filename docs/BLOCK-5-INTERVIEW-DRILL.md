# Block 5 — Gap-fill + interview drill (Sun, ~3-4h)

## 1. Quick builds if not done (cut these first if out of time)
- [ ] SOAP endpoint in notification-service (step N3, 45 min max)
- [ ] Webhook signature verify + polling fallback (step N2)
- [ ] Streams/Optional/Predicate polish: add one endpoint using
      `groupingBy` + `Collectors.averagingDouble` (products per category with
      avg price) so you have a non-trivial streams example that's YOURS.

## 2. The mock interview (90 min — THE highest-ROI block of the weekend)
Retrieval practice converts "I built it" into "I can explain it under pressure."
Pair up (or use Claude: "interview me hard on <topic>, follow up on my answers,
don't accept hand-waving"). Rules: answer OUT LOUD, no notes, every answer must
reference ShopPulse. Fumble → 10 min reading → re-answer → move on.

### Question bank (add your own as you build)

**Java core**
1. Records vs Lombok @Data vs POJO — what does a record actually generate, and when is it wrong (mutability, JPA entities)?
2. Your streams pipeline for products-by-category — walk through it. Intermediate vs terminal ops; what does lazy mean here?
3. Optional.map vs flatMap; three Optional anti-patterns (field, param, .get()).
4. thenApply/thenCompose/thenCombine; how did your order-summary fan-out handle timeout and failure?
5. How would you size the thread pool for that fan-out (CPU vs IO bound)? What happens when the queue fills?
6. synchronized vs ConcurrentHashMap vs AtomicInteger — where would each protect your inventory counter, and why did you push atomicity into Mongo instead?
7. ZGC vs G1 — pauses, mechanism, trade-off.

**Spring**
8. Bean lifecycle + how @Cacheable actually works (proxies! why self-invocation bypasses it).
9. Constructor vs field injection; how do circular dependencies happen and what do they smell of?
10. @Transactional: default propagation, when it silently does nothing (private/self-call), readOnly.
11. Profiles: how ShopPulse config changes dev→docker→k8s without code changes.

**Data**
12. Justify each store in ShopPulse: MySQL orders, Mongo stock, Dynamo payments, Redis cache, ES search, Qdrant vectors. (Polyglot persistence — access patterns drive choice.)
13. N+1 — how you created, detected, fixed it.
14. Index a slow query: what does EXPLAIN show; covering index; why is LIKE '%x%' unindexable and how does ES solve it (inverted index)?
15. Debezium: how it reads changes, snapshot vs streaming, why CDC > dual-write.
16. Redis: data structures beyond strings; persistence (RDB/AOF); what breaks if Redis dies in your setup (nothing — cache-aside degrades to DB. Say it with confidence, it's a design win).

**Distributed**
17. Your saga end-to-end including the compensation path. Why not 2PC?
18. Kafka ordering, consumer groups, rebalancing; your duplicate-payment story and the idempotency fix.
19. Circuit breaker states; your OPEN-breaker demo; bulkhead; load shedding.
20. Throttling vs debouncing; how the Redis token bucket works.
21. Outbox pattern — the exact failure it prevents.
22. gRPC vs REST vs SOAP — one paragraph each, when each wins.
23. Webhook vs polling — you built both; latency/load/reliability trade-offs; how webhook receivers stay safe (signature) and correct (idempotent, retries on sender side).
24. CAP: where do Eureka (AP) and your saga (eventual consistency) sit?

**Security**
25. Full login flow → JWT structure → gateway validation → downstream trust.
26. Refresh tokens; revocation strategies; where does the signing secret live (Vault) and why.
27. BCrypt vs SHA-256 (again — it WILL come up); what's HMAC and where did you use it (JWT signature, webhook signature)?

**AI**
28-34. The question lists at the bottom of scripts 01-08 + BLOCK-3 — do them all out loud.

**Design/architecture**
35. "Design an e-commerce checkout" — you now literally have one. Practice narrating ShopPulse's architecture in 3 minutes with trade-offs.
36. SOLID with examples from YOUR code, not textbook shapes.
37. Name 6 design patterns in ShopPulse and where (Strategy: notification channels; Observer: Kafka listeners; Circuit Breaker; Saga; Outbox; Builder: DTOs; Facade: gateway; Singleton: beans; Template Method: JpaRepository/spring-ws).
38. What would you change to handle 100x traffic? (scale-out stateless services, partition Kafka topics, read replicas, cache hit-rate work, async everything non-critical, CDN)

## 3. Storytelling prep (30 min)
Write down (actually write) your 5 war stories: the OPEN circuit breaker, the
double-charge + idempotency fix, the N+1 explosion, the lost webhook, the RAGAS
metric that dropped when you broke chunking. STAR format. These are worth more
than 20 memorized definitions.

## 4. After the weekend (keep momentum)
Week 2: read "Designing Data-Intensive Applications" ch. 1-9 (the theory behind
everything you just built) · deepen one AI area (build an eval harness in CI) ·
add the outbox table for real · try Temporal for orchestrated saga.
