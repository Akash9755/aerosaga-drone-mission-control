\# Backend Integration Notes



\## Current Integration Baseline



Dhara's backend currently provides:



\- Spring Boot backend

\- PostgreSQL database

\- JPA entities for Drone and Mission

\- Temporal mission workflow

\- Mission creation

\- Mission state query

\- Mission abort

\- Mission return-home

\- Drone activity implementation

\- Mission status activity

\- Temporal worker



\## Akash Backend Features To Integrate



The following features from Akash's backend need to be integrated carefully:



\- API key security

\- Flyway database migrations

\- Actuator health/metrics

\- Telemetry WebSocket

\- Drone REST APIs

\- Telemetry service

\- Exception handling

\- Pagination/filtering where applicable



\## Important Differences



Dhara package:

com.aerosaga



Akash package:

com.example.aerosaga



Dhara's workflow currently contains additional mission control functionality and

should remain the source of truth for the Temporal mission workflow.



Akash's security, telemetry, Flyway and Actuator functionality should be

integrated without replacing the existing mission workflow.



\## Integration Rules



1\. Do not replace Dhara's working mission workflow with Akash's older workflow.

2\. Preserve the existing PostgreSQL schema/entities unless a migration is required.

3\. Resolve package names consistently to com.aerosaga.

4\. Preserve Temporal task queue configuration.

5\. Integrate Akash's security separately.

6\. Integrate telemetry/WebSocket separately.

7\. Run compilation and application tests after each integration step.

