# Block 4 — Ops: Docker, K8s, Helm, monitoring, ZGC (Sun morning, ~6h)

Goal: product-service running in a kind cluster via a Helm chart, Grafana
dashboard live, one distributed trace in Zipkin, ZGC logs read.

Covers: Docker, Kubernetes, Helm, Prometheus/Grafana monitoring, Zipkin
tracing, Spring Boot Admin, deployment types, ZGC.

## 1. Dockerize (~1h)
Write `services/product-service/Dockerfile` YOURSELF (multi-stage — this is the
interview-relevant part; a template is in k8s/README.md). Build stage runs
`gradle bootJar`, runtime stage is `eclipse-temurin:17-jre` + non-root user.
Understand: layer caching (dependency layers before source), image size
(JDK-with-sources vs JRE), why non-root.
```powershell
docker build -t shoppulse/product-service:0.1 -f services/product-service/Dockerfile .
docker run --rm -p 18082:8082 -e SPRING_PROFILES_ACTIVE=docker --network shoppulse_default shoppulse/product-service:0.1
```
(Now the `docker` profile from Block 1 pays off — hostnames like `mysql` resolve.)

## 2. Observability stack (~1.5h)
`docker compose --profile obs up -d`
- **Prometheus** :9090 → Status→Targets (your services UP). Run PromQL:
  `rate(http_server_requests_seconds_count[1m])`, then p95:
  `histogram_quantile(0.95, rate(http_server_requests_seconds_bucket[5m]))`.
- **Grafana** :3000 → import dashboard **4701** (JVM Micrometer) → generate
  traffic with a curl loop → watch heap/threads/latency move. Create ONE alert
  rule (p95 > 500ms) so you've touched alerting.
- **Zipkin** :9411 → make a request that crosses gateway→order→product →
  find the trace, see spans + timings. Understand: trace id propagates via
  B3/W3C headers; Micrometer Tracing (Sleuth's successor) instruments it.
- **Spring Boot Admin** :8087 (`.\gradlew :services:admin-server:bootRun`) —
  it auto-discovers everything from Eureka. Party trick: change a logger to
  DEBUG at runtime from the UI.
- Vocabulary: metrics vs logs vs traces (the three pillars); RED method
  (Rate, Errors, Duration); pull vs push.

## 3. Kubernetes with kind (~2h)
```powershell
kind create cluster --name shoppulse
kind load docker-image shoppulse/product-service:0.1 --name shoppulse
kubectl apply -f k8s/product-service/     # deployment, service, configmap — fill the TODOs first
kubectl get pods -w
kubectl port-forward svc/product-service 28082:8082
```
The k8s/ yamls have TODO markers for the parts you must write: liveness vs
readiness probes (use /actuator/health/liveness + /readiness), resource
requests/limits, env from ConfigMap/Secret.
Drills that teach K8s fast:
- `kubectl delete pod <name>` → watch the Deployment self-heal
- `kubectl scale deployment product-service --replicas=3` → Service load-balances (server-side LB — contrast with Eureka client-side!)
- `kubectl rollout restart` / set a bad image → `kubectl rollout undo` (**rolling deployment + rollback, live**)
- break the readiness probe → pod stays out of the Service endpoints (that's what readiness IS)
Note: in K8s you don't need Eureka — Services + DNS + kube-proxy replace it. Say that in interviews.

## 4. Helm (~1h)
```powershell
helm create charts-scratch     # study the generated layout, then:
```
Convert k8s/product-service/*.yaml into `helm/product-service/`:
Chart.yaml, values.yaml (image.tag, replicaCount, resources), templates/ with
`{{ .Values.* }}` substitutions.
```powershell
helm install product ./helm/product-service
helm upgrade product ./helm/product-service --set replicaCount=3
helm history product && helm rollback product 1
```
Helm = parameterized, versioned, rollback-able K8s manifests. Releases,
`--dry-run --debug` to render templates, chart dependencies. Prod: charts in a
repo, deployed by ArgoCD (GitOps — cluster state reconciles to git).

## 5. ZGC (~30 min)
```powershell
.\gradlew :services:product-service:bootRun "-Dorg.gradle.jvmargs=-XX:+UseZGC -Xlog:gc"
```
Fire load, read pause lines (sub-millisecond). Repeat with G1 (`-XX:+UseG1GC`)
and compare. Theory (15 min read): ZGC = concurrent, region-based, colored
pointers + load barriers, pauses <1ms independent of heap size (multi-TB OK);
generational ZGC default since JDK 21. Trade-off vs G1: a few % throughput for
latency. When: latency-sensitive APIs, big heaps.

## 6. Deployment types + *aaS (~20 min talking points, anchored in what you just did)
- **Rolling** — K8s default, you did it. **Recreate** — downtime, `strategy: Recreate`.
- **Blue-green** — two full environments, flip the router/Service selector; instant rollback.
- **Canary** — shift 5%→50%→100% by pod ratio or mesh/ingress weights; watch error rate between shifts.
- **Feature flags** — deploy ≠ release.
- **IaaS** (EC2: you manage OS up) / **CaaS** (EKS: you manage containers) /
  **PaaS** (Heroku/Cloud Run: you push code) / **FaaS** (Lambda: you push functions) /
  **SaaS** (you consume the app). Your kind cluster ≈ self-managed CaaS.

## Interview questions
1. Deployment vs ReplicaSet vs Pod vs Service vs Ingress — one line each.
2. Liveness vs readiness (and what happens when each fails — restart vs endpoint removal).
3. How does a rolling update achieve zero downtime? (maxSurge/maxUnavailable + readiness gates)
4. What does Helm add over kubectl apply? What's a release? How do you roll back?
5. Requests vs limits; what happens at memory limit (OOMKill) vs CPU limit (throttling)?
6. Prometheus pull model — why? What's a histogram metric; how does p95 come out of buckets?
7. Blue-green vs canary — cost vs risk trade-off.
8. ZGC vs G1 — how ZGC gets sub-ms pauses and what it costs.

## Resources
- TechWorld with Nana: "Kubernetes in 4 hours" (watch the concepts sections at 1.5x, skip installs), "Helm in 1 hour", "Prometheus in 20 min"
- kind docs quickstart · Helm docs "Getting Started"
- Inside Java: "Generational ZGC" post (JEP 439 summary)
