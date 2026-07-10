# ShopPulse — learn-by-building weekend

An e-commerce order platform + AI support copilot, built specifically to cover
~60 backend/AI/infra topics hands-on, entirely local and free. One coherent
system — every topic gets a real integration, not an isolated tutorial.

```
 client ──▶ API GATEWAY (8080: JWT, rate-limit, LB via Eureka)
              │
   ┌──────────┼───────────────┬──────────────────┐
   ▼          ▼               ▼                  ▼
 auth      product         order            ai-support (8000, Python)
 (8081)    (8082)          (8083)           LangGraph agent · RAG(Qdrant)
 JWT,Vault MySQL+JPA       MySQL, SAGA      tools→gateway · MCP · Langfuse
 MySQL     Redis cache     via Kafka        RAGAS evals
           ES search  ◀─┐    │ └─gRPC──▶ inventory (8084, MongoDB, gRPC 6565)
                        │    └─Kafka───▶ payment  (8085, DynamoDB, SQS)
 MySQL ─binlog─▶ Debezium ─▶ Kafka ─────▶ notification (8086: Kafka/webhook/polling/SOAP)

 eureka 8761 · admin 8087 · kafka-ui 8090 · grafana 3000 · prometheus 9090
 zipkin 9411 · vault 8200 · qdrant 6333 · langfuse 3002 · ES 9200 · localstack 4566
```

## Start here

1. **[docs/BLOCK-0-SETUP.md](docs/BLOCK-0-SETUP.md)** — tools + infra up (~1.5h)
2. [docs/BLOCK-1-SPRING-CORE.md](docs/BLOCK-1-SPRING-CORE.md) — auth, product, gateway (Fri, ~8h)
3. [docs/BLOCK-2-DISTRIBUTED.md](docs/BLOCK-2-DISTRIBUTED.md) — Kafka, SAGA, gRPC, CDC (Sat, ~9h)
4. [docs/BLOCK-3-AI-LAYER.md](docs/BLOCK-3-AI-LAYER.md) — LLM→RAG→Agent→MCP→Eval (Sat, ~8h)
5. [docs/BLOCK-4-OPS.md](docs/BLOCK-4-OPS.md) — Docker, K8s, Helm, monitoring, ZGC (Sun, ~6h)
6. [docs/BLOCK-5-INTERVIEW-DRILL.md](docs/BLOCK-5-INTERVIEW-DRILL.md) — mock interview (Sun, ~3h)

[docs/COVERAGE-MAP.md](docs/COVERAGE-MAP.md) maps every topic on the list to its exact spot.

## How the repo teaches

- Each service's `*Application.java` javadoc is that service's **lesson plan**
  (numbered steps like P1-P6); block docs reference the same step numbers and
  hold the verify-commands and interview questions.
- The Python curriculum is `services/ai-support-service/scripts/01..08` — the
  docstrings are the lessons.
- Infra is `docker-compose.yml` with **profiles** (core / cdc / search / ai /
  obs / aws) so you only pay RAM for what the current block needs.
- Skeletons compile-and-run; the learning code is yours to write. Don't paste —
  type, run, break, fix.

## Cheat sheet

```powershell
docker compose up -d                       # core infra
.\gradlew projects                         # list modules
.\gradlew :services:eureka-server:bootRun  # run a service (one terminal each)
.\gradlew :services:inventory-service:generateProto   # gRPC stubs
docker compose --profile obs --profile ai up -d       # add a stack
docker compose down -v                     # nuke everything, fresh start
```

If short on time, cut in this order: SOAP → DynamoDB (concept-only) →
Spring Boot Admin → Helm (raw K8s only) → second LB strategy.
**Never cut:** the saga drill (Block 2 §5) and the mock interview (Block 5 §2).
