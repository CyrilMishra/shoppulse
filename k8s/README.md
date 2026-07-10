# Kubernetes manifests (Block 4)

Fill the TODOs in product-service/*.yaml, then:

```powershell
kind create cluster --name shoppulse
docker build -t shoppulse/product-service:0.1 -f services/product-service/Dockerfile .
kind load docker-image shoppulse/product-service:0.1 --name shoppulse
kubectl apply -f k8s/product-service/
kubectl get pods -w
kubectl port-forward svc/product-service 28082:8082
```

Note: pods inside kind can't reach your host's docker-compose MySQL/Redis by
`localhost`. Easiest local trick: use `host.docker.internal` in the k8s profile
config (works with Docker Desktop). Production answer: databases run as managed
services (RDS/ElastiCache) or their own charts, addressed by DNS.

## Dockerfile template (write it yourself in services/product-service/Dockerfile)

```dockerfile
# ---- build stage: has JDK + gradle, produces the jar ----
FROM gradle:8.14-jdk17 AS build
WORKDIR /workspace
COPY settings.gradle build.gradle ./
COPY services/product-service ./services/product-service
RUN gradle :services:product-service:bootJar --no-daemon

# ---- runtime stage: JRE only, non-root ----
FROM eclipse-temurin:17-jre
RUN useradd -r appuser
USER appuser
COPY --from=build /workspace/services/product-service/build/libs/*.jar app.jar
EXPOSE 8082
ENTRYPOINT ["java","-jar","/app.jar"]
```

Why multi-stage (interview): build tools never ship to prod, image drops from
~800MB to ~300MB, smaller attack surface. Why layer order matters: COPY of
build files before source maximizes Docker layer-cache hits.
