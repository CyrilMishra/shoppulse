# HANDOFF — moving ShopPulse to another system

Everything needed to pick this project up on a fresh machine and continue the
learning sprint. The repo is fully self-contained: curriculum, infra, build
config, and the AI coach prompt all travel with it.

## 1. What you are holding

| Item | State at handoff (2026-07-10) |
|---|---|
| 9 Java service skeletons | **Compile verified** (`gradlew compileJava` — BUILD SUCCESSFUL, incl. gRPC stub generation) |
| Python ai-support-service | Skeleton + guided scripts 01–08; venv NOT created (per-machine) |
| docker-compose.yml (16 containers, 6 profiles) | Written; not yet started anywhere — Block 0 does that |
| Curriculum | docs/BLOCK-0..5 + COVERAGE-MAP.md — complete |
| Learning code (entities, controllers, saga, agent…) | **NOT written — that is the sprint itself** |
| Gradle wrapper | 8.14.3, committed (no Gradle install needed on new machine) |
| Git | Initial commit contains the pristine scaffold |

The pristine-scaffold commit matters: at any point,
`git diff <initial-commit>` shows exactly what YOU built — useful for review,
and for resetting a service if an experiment goes sideways.

## 2. Transfer to the new system (pick one)

**A. Private GitHub repo (best — free, adds backup + history):**
```powershell
# on THIS machine, inside the repo (needs GitHub CLI: winget install GitHub.cli)
gh auth login
gh repo create shoppulse --private --source . --push
# on the NEW machine
git clone https://github.com/<your-user>/shoppulse
```

**B. Git bundle (offline, single file over USB/drive — full history, no server):**
```powershell
# on THIS machine, inside the repo
git bundle create ..\shoppulse.bundle --all
# on the NEW machine
git clone shoppulse.bundle shoppulse
cd shoppulse; git remote remove origin
```

**C. Plain zip:** zip the folder EXCLUDING `.gradle/`, `build/`, `.venv/`
(already .gitignored — `git archive -o ..\shoppulse.zip HEAD` does this
correctly in one command, but note it omits uncommitted changes).

## 3. New-machine prerequisites

| Tool | Needed for | Install (Windows) |
|---|---|---|
| Docker Desktop (WSL2) | all infra | docker.com |
| JDK 17 or 21 | running services (wrapper handles the build toolchain) | `winget install EclipseAdoptium.Temurin.21.JDK` |
| Python 3.11+ | ai-support-service | `winget install Python.Python.3.12` |
| kind | Block 4 | `winget install Kubernetes.kind` |
| kubectl | Block 4 | `winget install Kubernetes.kubectl` |
| helm | Block 4 | `winget install Helm.Helm` |
| Ollama | Block 3 | `winget install Ollama.Ollama`, then `ollama pull llama3.1:8b` and `ollama pull nomic-embed-text` (start these downloads FIRST — ~5 GB) |
| Gradle | **not needed** — `.\gradlew` wrapper is committed | — |

RAM guidance: core compose profile ~3 GB; everything at once ~10–12 GB.
Use the profiles (see docker-compose.yml header) — never run more than the
current block needs.

## 4. First-run verification on the new machine (15 min)

```powershell
cd shoppulse
$env:JAVA_HOME = '<path to JDK 17/21>'     # only if PATH java is old
.\gradlew projects                          # expect 9 modules
.\gradlew compileJava                       # expect BUILD SUCCESSFUL (first run downloads deps, ~2-5 min)
docker compose up -d
docker compose ps                           # mysql, mongo, redis, kafka, kafka-ui, vault running
.\gradlew :services:eureka-server:bootRun   # http://localhost:8761 loads
cd services\ai-support-service
python -m venv .venv; .venv\Scripts\activate; pip install -r requirements.txt
cd ..\..; copy .env.example .env            # fill LLM keys during Block 3
```

All green → you are exactly where this machine left off. Anything red → the
fix is almost always JAVA_HOME, Docker not started, or a port collision
(port map in README.md).

## 5. Continue the sprint

1. Open `docs/AI-COACH-PROMPT.md`, fill in `<REPO_PATH>` and the
   **Current status** section, paste it as the first message to your AI
   assistant on the new system.
2. Resume at your current block doc (`docs/BLOCK-*.md`). If starting fresh:
   BLOCK-0 → BLOCK-5 in order. Every step number in the docs matches a TODO
   in the service source.
3. Commit as you complete each step (`git commit -m "P4: redis cache-aside on product lookup"`)
   — the commit log becomes your study journal and your git-workflow practice.

## 6. Status tracker (edit this section as you go — it travels with the repo)

- [ ] Block 0 — setup & infra up
- [ ] Block 1 — auth (A1–A6), product (P1–P6), gateway (G1–G3), vault (V2), profiles
- [ ] Block 2 — kafka, inventory (I1–I4), order/saga (S1–S7), payment (Y1–Y4), resilience, CDC (D1–D4), notification (N1)
- [ ] Block 3 — AI scripts 01–08 + agent wired into gateway
- [ ] Block 4 — dockerize, obs stack, kind, helm, ZGC
- [ ] Block 5 — SOAP/webhook gap-fill (N2–N3), mock interview, war stories written

Last worked on: `<block/step>` · Machine: `<name>` · Date: `<date>`
