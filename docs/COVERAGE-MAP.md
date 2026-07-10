# Coverage map — every topic from your list → where you learn it

Depth: **BUILD** = write the code yourself · **INTEGRATE** = run + wire + one exercise · **CONCEPT** = talking points anchored to the project.

| Topic | Where | Block | Depth |
|---|---|---|---|
| LLM API | scripts/01 | 3 | BUILD |
| Prompt engineering | scripts/02 | 3 | BUILD |
| Tool calling | scripts/03 (raw!) | 3 | BUILD |
| LangChain internals | scripts/05 (read source) | 3 | BUILD |
| RAG internals | scripts/04 (raw) + 05 | 3 | BUILD |
| Vector databases | Qdrant, scripts/04 | 3 | BUILD |
| Agents | scripts/03 loop + 06 | 3 | BUILD |
| LangGraph | scripts/06 | 3 | BUILD |
| MCP | scripts/07 + .mcp.json | 3 | BUILD |
| Evaluation | scripts/08 RAGAS | 3 | BUILD |
| Observability (LLM) | scripts/08 Langfuse | 3 | INTEGRATE |
| Docker | Dockerfiles, compose | 0/4 | BUILD |
| Kubernetes | kind + k8s/ | 4 | BUILD |
| Helm charts | helm/ | 4 | BUILD |
| Kafka setup + integration | compose + order/payment/notification | 2 | BUILD |
| gRPC | inventory.proto, order↔inventory | 2 | BUILD |
| REST | product-service P3 | 1 | BUILD |
| SOAP | notification N3 | 5 | INTEGRATE |
| Spring Boot Admin | admin-server | 4 | INTEGRATE |
| OSS monitoring for microservices | Prometheus+Grafana+Zipkin | 4 | INTEGRATE |
| Redis cache | product P4 | 1 | BUILD |
| Elasticsearch | product D3/D4 | 2 | INTEGRATE |
| DynamoDB | payment Y1/Y2 (dynamodb-local) | 2 | INTEGRATE |
| MongoDB | inventory I1 | 2 | BUILD |
| MySQL | auth/product/order via JPA | 1 | BUILD |
| Debezium | CDC → ES pipeline | 2 | BUILD |
| Cascading failure | S6 breaker drill + theory | 2 | BUILD |
| Debouncing | contrast w/ throttling | 2 | CONCEPT |
| Throttling | gateway RequestRateLimiter | 2 | BUILD |
| Streams | product P2, drill in Block 5 | 1 | BUILD |
| Records | all DTOs | 1 | BUILD |
| JVM ZGC | Block 4 §5 GC-log comparison | 4 | INTEGRATE |
| Optional | product P2 | 1 | BUILD |
| Predicates | product P2 filters | 1 | BUILD |
| application.properties | every service's application.yml | 1 | BUILD |
| HashiCorp Vault setup | compose + auth V2 | 1 | INTEGRATE |
| SAGA | order S4 + saga drill | 2 | BUILD |
| Rollback / compensation | inventory.release compensation; also `helm rollback`, `kubectl rollout undo` | 2/4 | BUILD |
| Access control (RBAC) | auth A5 @PreAuthorize | 1 | BUILD |
| API gateway | api-gateway G1-G3 | 1 | BUILD |
| Authentication | auth A2-A4 | 1 | BUILD |
| Bearer token generation | auth A3 (JWT) | 1 | BUILD |
| Session management | stateless JWT vs alternatives (A4) | 1 | BUILD |
| Types of cache | P4: cache-aside, L1/L2, TTL + theory | 1 | BUILD |
| @Autowired / DI | constructor injection everywhere + why | 1 | BUILD |
| SHA-256 ("SSH 256") | JWT HMAC-SHA256, webhook HMAC, vs BCrypt | 1/2 | BUILD |
| Hibernate / JPA | product P1/P6 (N+1!) | 1 | BUILD |
| SQS kind of thing | payment Y3 (LocalStack SQS) | 2 | INTEGRATE |
| Types of deployment | Block 4 §6 + rollout drills | 4 | CONCEPT |
| IaaS/PaaS/SaaS/FaaS | Block 4 §6 | 4 | CONCEPT |
| Multithreading | S5 pools + inventory concurrency | 2 | BUILD |
| sync/async | @Async + sync-vs-async endpoint | 2 | BUILD |
| CompletableFuture | order S5 fan-out | 2 | BUILD |
| Swagger | product P5 (springdoc) | 1 | BUILD |
| Eureka-type registry | eureka-server (+ K8s replaces it — Block 4) | 0/1 | BUILD |
| Profiles per env | dev/docker profiles everywhere | 1 | BUILD |
| Load balancing strategies | Block 1 two-instance demo + custom supplier + K8s Service | 1/4 | BUILD |
| SOLID | Block 1 Q9, Block 5 Q36 — from YOUR code | 1/5 | BUILD |
| Interfaces | strategy channels, repos, sealed types | 1/2 | BUILD |
| Design patterns | Block 5 Q37 inventory of ~9 in the codebase | all | BUILD |
| Webhook | payment Y4 → notification N2 (signed) | 2 | BUILD |
| Polling | notification N2 @Scheduled fallback | 2/5 | BUILD |
