# aerosaga-drone-mission-control
# AeroSaga — Autonomous Drone Mission Control

AeroSaga is a drone delivery mission-control system built to demonstrate a core
idea: long-running, multi-step, real-world processes shouldn't lose their
progress just because a server crashes. Instead of treating a drone mission as
a single, fragile API call, AeroSaga models it as a **durable Temporal
workflow** — one that survives crashes, restarts, and failures, and always
resumes exactly where it left off.

> Built as part of the Infotact Solutions Advanced Full-Stack Java Engineering
> program.

---

## The Problem

A real drone delivery can take 30–45 minutes and involves several sequential
steps — takeoff, navigate to pickup, drop the package, return to base. Typical
web applications are built for short request/response cycles, not long,
interruptible processes. If a normal server crashes mid-mission, all context
about where the drone was is lost.

## The Idea

AeroSaga uses [Temporal.io](https://temporal.io) to make each mission a
**durable workflow**. Temporal — not application memory — tracks a mission's
progress externally. If the backend crashes and restarts, the workflow simply
resumes from its last completed step, with zero manual intervention.

---

## Architecture

```
Client (React dashboard)
        │
        │ REST (X-API-KEY secured) + WebSocket
        ▼
┌───────────────────────────────────────────┐
│              Spring Boot Backend           │
│                                             │
│  controller → service → repository → DB    │
│       │                                     │
│       └──► Temporal WorkflowClient          │
│                    │                        │
└────────────────────┼────────────────────────┘
                      ▼
        ┌─────────────────────────┐
        │   Temporal Server        │
        │ (durable workflow state) │
        └─────────────────────────┘
                      │
                      ▼
        Workflow → Activities → back into
        the same Service/Repository layer
```

---

## Tech Stack

**Backend**
- Java 17, Spring Boot 3.3.4
- Spring Data JPA + PostgreSQL
- Temporal Java SDK (workflow orchestration)
- Spring WebSocket (live telemetry)
- Spring Security (API-key authentication)
- Flyway (versioned database migrations)
- JUnit 5 + Mockito (unit testing)
- Docker & Docker Compose

**Frontend**
- React 19 + Vite
- (planned) CesiumJS / map library for live 3D fleet visualization

---

## Backend Package Structure

| Package | Responsibility |
|---|---|
| `entity` | JPA entities — `Drone`, `Mission` |
| `repository` | Spring Data JPA repositories |
| `service` | Business logic |
| `controller` | REST API endpoints |
| `dto` | Request/response contracts, decoupled from entities |
| `exception` | Centralized error handling (`@ControllerAdvice`) |
| `config` | API-key security, app-wide setup |
| `websocket` | Live telemetry gateway |
| `workflow` | Temporal workflow definitions (the mission "script") |
| `activity` | Real, side-effecting steps a workflow calls |
| `temporal` | Connects the app to the Temporal cluster |

---

## Key Features

- **Crash-resumable missions** — kill the server mid-flight, restart it, and
  the mission continues exactly where it left off.
- **Automatic compensation logic** — a failed package drop automatically
  triggers a return-to-base, instead of failing silently.
- **Configurable failure simulation** — force a mission's drop to fail on
  demand, to reliably demonstrate the compensation logic.
- **Emergency abort & return-home signals** — interrupt a running mission at
  any time, with two distinct outcomes (abort = failure path, return-home =
  normal early completion).
- **Live telemetry** — drone position updates streamed over WebSocket in
  real time.
- **Hardened API** — API-key auth, centralized exception handling, pagination
  and filtering, and versioned database migrations.

---

## Getting Started

### Option 1 — Docker (recommended)

```bash
docker compose build
docker compose up -d
```

This starts Postgres, Temporal's internal database, the Temporal server, the
Temporal Web UI, and the backend — all wired together.

- Backend: `http://localhost:8080`
- Temporal Web UI: `http://localhost:8233`

### Option 2 — Run locally

1. Start Postgres and Temporal (via Docker, or local installs).
2. Update `src/main/resources/application.properties` with your local
   connection details.
3. Run `AeroSagaApplication.java` from your IDE, or:
   ```bash
   mvn spring-boot:run
   ```

---

## API Overview

All endpoints (except `/actuator/health` and `/ws/telemetry/**`) require an
`X-API-KEY` header.

| Method | Endpoint | Description |
|---|---|---|
| `GET` | `/api/drones` | List drones (paginated, filterable by status) |
| `POST` | `/api/drones` | Register a new drone |
| `PATCH` | `/api/drones/{id}/status` | Update drone status |
| `GET` | `/api/missions` | List missions (paginated, filterable by status) |
| `POST` | `/api/missions` | Create a mission — starts a Temporal workflow |
| `GET` | `/api/missions/{id}/status-live` | Query the mission's current workflow step |
| `POST` | `/api/missions/{id}/abort` | Emergency abort |
| `POST` | `/api/missions/{id}/return-home` | Recall the drone (non-failure) |

WebSocket telemetry: `ws://localhost:8080/ws/telemetry`

---

## Testing

```bash
mvn clean test
```

Covers core service-layer logic, including abort-validation rules and the
failure-simulation path.

---

## Roadmap

- [x] Core REST API, database layer, WebSocket telemetry
- [x] Temporal workflow with compensation logic
- [x] API security, exception handling, pagination, migrations
- [x] Abort & return-home signals
- [ ] Frontend live dashboard (real API + WebSocket integration)
- [ ] 3D fleet visualization

---

## Contributors

- Akash Tripathi — Backend (persistence, API, security, WebSocket, workflow integration)
- Dhara — Workflow logic contributions (return-home signal)

---

## Acknowledgements

Project specification: *Infotact Solutions — Advanced Full-Stack Java
Engineering, Vol. II*.
