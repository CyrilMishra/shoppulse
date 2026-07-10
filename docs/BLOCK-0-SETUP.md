# Block 0 — Setup (Fri, ~1.5h)

Goal: every tool installed, core infra running, one service registered in Eureka.

## 0.1 Install missing tools (PowerShell, admin)

Already on your machine: Docker 29, Gradle 9.3, JDK 21 (`C:\Program Files\Java\jdk-21`), Python 3.13, kubectl, git.
**Gotcha found during scaffold:** your PATH `java` is 1.8. Gradle's toolchain handles compilation
(pinned to 17 in every build.gradle), but set `JAVA_HOME` for the daemon if anything complains:
`$env:JAVA_HOME = 'C:\Program Files\Java\jdk-21'`

```powershell
winget install Kubernetes.kind
winget install Helm.Helm
winget install Ollama.Ollama
```

## 0.2 Start the model downloads NOW (they're GBs — run in background)

```powershell
ollama pull llama3.1:8b        # ~4.7 GB — the chat model
ollama pull nomic-embed-text   # ~270 MB — the embedding model
```

If your laptop struggles with 8b later, `ollama pull qwen2.5:3b` as fallback.

## 0.3 Generate the Gradle wrapper (pin 8.14 — Boot 3.3 doesn't support Gradle 9)

```powershell
cd C:\Users\cyril.mishra\Project\Learning\shoppulse
gradle wrapper --gradle-version 8.14.3
.\gradlew projects        # should list all 9 services
```

## 0.4 Core infra up

```powershell
docker compose up -d
docker compose ps         # all healthy/running?
```

Verify each piece (each command teaches you the tool's client):

```powershell
# MySQL — 3 databases + debezium user exist?
docker compose exec mysql mysql -uroot -pshoppulse -e "SHOW DATABASES;"

# Kafka — create/list a topic from inside the broker container
docker compose exec kafka /opt/kafka/bin/kafka-topics.sh --bootstrap-server localhost:19092 --create --topic hello --partitions 3
docker compose exec kafka /opt/kafka/bin/kafka-topics.sh --bootstrap-server localhost:19092 --list
# then open Kafka UI: http://localhost:8090 — find your topic, 3 partitions

# Redis
docker compose exec redis redis-cli ping        # PONG

# Mongo
docker compose exec mongo mongosh --eval "db.runCommand({ping:1})"

# Vault — open http://localhost:8200 (token: shoppulse-root), then:
docker compose exec vault vault kv put -address=http://127.0.0.1:8200 secret/hello foo=bar
# (export VAULT_TOKEN=shoppulse-root inside the container if it asks)
```

## 0.5 First service running

```powershell
.\gradlew :services:eureka-server:bootRun
```

Open http://localhost:8761 — the Eureka dashboard. Leave it running (one terminal
per service from here on; or run them from IntelliJ, which is nicer).

## 0.6 Python env

```powershell
cd services\ai-support-service
python -m venv .venv
.venv\Scripts\activate
pip install -r requirements.txt
cd ..\..
copy .env.example .env      # fill keys as you get them (Groq/Gemini in Block 3)
```

## Done when
- [ ] `docker compose ps` shows mysql, mongo, redis, kafka, kafka-ui, vault running
- [ ] Eureka dashboard loads at :8761
- [ ] `.\gradlew projects` lists 9 modules
- [ ] ollama pulls finished (`ollama list`)
- [ ] kind, helm installed (`kind version`, `helm version`)

## While things download, watch (pick one)
- Andrej Karpathy — "Intro to Large Language Models" (1h) — best hour of the weekend
- TechWorld with Nana — "Docker Compose in 12 min" if compose feels shaky
