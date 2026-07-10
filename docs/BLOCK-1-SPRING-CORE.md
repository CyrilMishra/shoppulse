# Block 1 — Spring core: auth, product, gateway (Fri, ~8h)

Goal: `curl` a JWT from auth-service, use it through the gateway to hit a
Redis-cached product endpoint. Every step number below is referenced from TODO
comments in the service source files.

Covers: REST, JPA/Hibernate, MySQL, Redis + cache types, JWT/bearer tokens,
authentication, session management, RBAC, SHA-256 vs BCrypt, Vault, Swagger,
profiles, records, streams, Optional, Predicates, @Autowired vs constructor
injection, API gateway, load balancing, SOLID.

## 1. product-service (~3h) — steps P1-P6 in ProductServiceApplication.java

P1 entity+repo → P2 records/streams/Optional/Predicate service layer → P3 REST
controller → P4 Redis caching → P5 Swagger → P6 the N+1 exercise.

Verify:
```powershell
curl -X POST localhost:8082/api/products -H "Content-Type: application/json" -d '{"sku":"SKU-1","name":"Trail Runner","price":89.99,"category":"shoes"}'
curl localhost:8082/api/products/1        # 1st: SQL in the logs
curl localhost:8082/api/products/1        # 2nd: NO SQL — served from Redis
docker compose exec redis redis-cli KEYS "*"      # see your cache key
# wait 60s (TTL) -> curl again -> SQL is back. You have SEEN cache-aside + TTL.
```
Swagger: http://localhost:8082/swagger-ui.html

**Load-balancing demo (do this now, it's 2 min):** start a second instance
`.\gradlew :services:product-service:bootRun --args='--server.port=9082'`,
watch Eureka show 2 instances of PRODUCT-SERVICE. The gateway will round-robin
them in step G2 — add a log line printing `server.port` in a controller to see
alternation. Strategies: round-robin (default), random, weighted, zone-aware —
custom = provide a `ServiceInstanceListSupplier` bean.

## 2. auth-service (~2.5h) — steps A1-A6 in AuthServiceApplication.java

Verify:
```powershell
curl -X POST localhost:8081/api/auth/register -H "Content-Type: application/json" -d '{"email":"a@b.com","password":"secret123"}'
curl -X POST localhost:8081/api/auth/login -H "Content-Type: application/json" -d '{"email":"a@b.com","password":"secret123"}'
# copy the token, decode it at jwt.io — look at header.payload.signature
```
Look in MySQL: `SELECT password_hash FROM authdb.users;` — a BCrypt hash
(`$2a$10$...` — algorithm, cost factor, salt, hash all encoded in the string).

## 3. api-gateway (~1.5h) — steps G1-G3

G1: uncomment routes in application.yml → `curl localhost:8080/api/products/1`
G2: two product instances → watch round-robin.
G3: JwtAuthenticationFilter (GlobalFilter): skip /api/auth/**, else validate
Bearer token, 401 on failure, forward X-User-Id/X-User-Roles headers downstream.

```powershell
curl localhost:8080/api/products/1                          # 401
curl localhost:8080/api/products/1 -H "Authorization: Bearer <token>"   # 200
```

## 4. Vault — step V2 (~45 min)

```powershell
docker compose exec -e VAULT_TOKEN=shoppulse-root vault vault kv put -address=http://127.0.0.1:8200 secret/auth-service shoppulse.jwt.secret=vault-managed-secret-9876543210 spring.datasource.password=shoppulse
```
Uncomment the vault starter in auth-service build.gradle + the vault block in
its application.yml, DELETE the secret from yml, restart, login still works.
Secrets now come from Vault at boot. Prod parity: AppRole/K8s auth instead of a
root token, dynamic DB credentials, rotation.

## 5. Profiles — step already in auth-service yml (~15 min)
Run with `--spring.profiles.active=docker`, watch it fail to reach `mysql:3306`
(that hostname only resolves inside the compose network) — now you understand
exactly what profiles select and why the docker profile exists.

## Interview questions to answer out loud before moving on
1. Walk through what happens from `curl` to DB row on GET /api/products/1 (filter chain → dispatcher servlet → controller → cache miss → repository → Hibernate session → JDBC).
2. PUT vs POST vs PATCH; why is idempotency important for retries?
3. JWT: what's in it, how is it verified (HMAC-SHA256 signature), why can't you revoke one, what do you do about that (short TTL + refresh + denylist)?
4. Why BCrypt over SHA-256 for passwords? (work factor, salt; SHA-256 is for integrity/signatures — e.g. the JWT signature and your webhook HMAC)
5. Stateless JWT vs sticky sessions vs Redis session store — trade-offs?
6. Cache-aside vs read-through vs write-through vs write-behind; what's cache stampede and 2 fixes (lock/single-flight, jittered TTL)?
7. N+1: what is it, how did YOU detect it (show-sql), 3 fixes (join fetch, @EntityGraph, batch size)?
8. Constructor vs field injection — 3 concrete reasons.
9. Where do SOLID principles show up in what you just wrote? (S: controller/service/repo split; O+D: cache abstraction — you swapped Redis/Caffeine without touching service code; L: JpaRepository contracts; I: small focused interfaces)

## Resources
- Java Brains "Spring Security JWT" playlist (the auth flow, visually)
- Baeldung: "Spring Boot with Redis Cache", "spring-cloud-gateway", "JPA one-to-many"
- Vlad Mihalcea: "N+1 query problem" article
- TechWorld with Nana: "HashiCorp Vault explained" (15 min)
