---
id: ADR-001
title: Anti-Corruption Layer (ACL) for External System Dependencies
status: accepted
date: 2026-10-14
deciders: [Developers]
consulted: []
informed: []
tags: [backend, architecture, database, integration]
related_issues: ["#1511"]
---

# ADR-001: Anti-Corruption Layer (ACL) for External System Dependencies

## Context & Problem Statement

Silva is the modernized forestry management system replacing the legacy RESULTS application. Silva currently operates in two backend modes controlled by `server.primary-db`:
1. **Hybrid Mode (`server.primary-db=oracle`)**: Deployed in production and test environments. Silva uses Oracle for RESULTS business data (read-only for modernization workflows) and PostgreSQL for user-specific state (e.g. favorites).
2. **PostgreSQL-Only Mode (`server.primary-db=postgres`)**: Deployed in test-preview / sandbox environments. Full CRUD operations are enabled, and Silva's domain entities persist directly to PostgreSQL (`silva` schema). The ultimate architectural roadmap is for Silva to run exclusively on PostgreSQL.

However, Silva does not operate in isolation. It has strong dependencies on external applications within the BC Forestry ecosystem (such as Forest Tenures Administration / FTA, Forest Client, and Ministry Organization Units).
- **Current State:** External application data currently resides in the legacy Oracle database (under schemas like `THE`, with tables such as `CUT_BLOCK`, `CUT_BLOCK_CLIENT`, `ORG_UNIT`, etc.). Even in `primary-db=postgres` mode, Silva needs to query and validate against this external Oracle data.
- **Future State:** External applications will modernize and migrate to PostgreSQL and/or publish dedicated REST APIs on their own uncoordinated timelines.
- **The Problem:** Direct database queries coupled to external Oracle table schemas and column names create severe technical debt. If Silva embeds external table schemas directly into its domain logic or Postgres migrations, any change in external systems will trigger massive, risky refactors across Silva's codebase.

We need a modular, decoupled architecture that focuses on **business functionality rather than external field names**, allowing Silva to transition from direct Oracle database reads to external REST APIs seamlessly without impacting core Silva business logic.

---

## Decision

We adopt a **Ports & Adapters (Hexagonal Architecture)** design incorporating an **Anti-Corruption Layer (ACL)**:

1. **Domain-Centric Provider Ports (Interfaces):**
   - Define functional provider interfaces in `ca.bc.gov.restapi.results.common.provider` (e.g. `TenureProvider`, `OrgUnitProvider`).
   - Contracts are structured around **Silva's business questions** (e.g. `isCutBlockAvailableForOpening(...)`, `findTenureLicenseeDetails(...)`) rather than generic CRUD tables.
   - Return clean, Silva-owned domain Value Objects and DTOs (e.g. `ExternalCutBlockDetails`, `ExternalTenureSummary`) in `ca.bc.gov.restapi.results.common.dto`. External column names, table aliases, and legacy quirks never cross this boundary.

2. **Swappable Adapters:**
   - Concrete implementations are decoupled from the core domain:
     - **Oracle JDBC Adapter (`oracle/provider/`):** Queries external Oracle tables directly using lightweight `NamedParameterJdbcTemplate`.
     - **REST API Adapter (`common/provider/`):** Consumes the external system's REST API once it becomes available.
     - **Mock Adapter (`common/provider/mock/`):** Provides deterministic in-memory stubs for unit testing and offline local development.

3. **Per-Domain Configuration Switching via Delegating Providers:**
   - In Spring Boot 3 with GraalVM Native Image compilation, `@ConditionalOnProperty` is evaluated at build time (AOT), which prunes unselected beans.
   - To preserve dynamic runtime switching via OpenShift environment variables (`SILVA_INTEGRATION_<DOMAIN>_PROVIDER = oracle | api | mock`) without rebuilding container images or exploding build arguments, all adapters are compiled into the binary and wrapped by a `@Primary` delegating router.
   - Enables independent, gradual migration as each external system publishes its API.

4. **Lightweight Secondary Oracle Read-Only DataSource:**
   - In `primary-db=postgres` mode with external Oracle enabled (`silva.integrations.external-oracle.enabled=true`), configure a dedicated, lightweight, read-only Oracle connection pool and `NamedParameterJdbcTemplate` (`externalOracleJdbcTemplate`).
   - In `primary-db=oracle` mode, `externalOracleJdbcTemplate` wraps the existing primary `oracleDataSource`, avoiding duplicate connection pools.
   - This keeps external table querying strictly isolated from Silva's primary JPA `EntityManagerFactory`, avoiding multi-dialect JPA complexity and GraalVM reflection issues.

5. **CQRS & System-Wide Read Model Projections:**
   - **Command vs. Query Separation:** Silva applies Command Query Responsibility Segregation (CQRS) across all external domains and legacy boundaries (e.g., Tenures/FTA, Forest Client, Org Units/BCGW, Seedling/SPAR, and any future dependencies identified during discovery).
     - **Command & Mutation Path (Authoritative Validation):** Any state-changing business action, transactional lifecycle rule, or live integrity check must query the external system of record via its domain port (`*Provider`). This ensures authoritative validation against the true source of truth before persisting changes in Silva.
     - **Query & Composite Read Path (Local Read Projections):** Operations that read, filter, aggregate, display, or export data across multiple entities (such as multi-criteria searches, dashboard summaries, GIS/spatial overlays, and bulk reporting) must **never** perform synchronous cross-network API calls or per-row HTTP fetching ($N+1$ latency trap). Instead, Silva maintains **local read model projections and materialized views** in PostgreSQL. These projections are indexed and queried with sub-millisecond local SQL.
   - **Flexible External Data Ownership & Eventual Consistency:**
     - Where an external system owns entities or relationship records that Silva needs to query or join locally (e.g. tenure associations, client profiles, administrative boundaries), Silva persists a local search/read-optimized projection.
     - Synchronization is decoupled from query execution and managed asynchronously via event streaming (Kafka/EventBridge), Change Data Capture (CDC), write-through/outbox lifecycle hooks, or scheduled reconciliation jobs.

6. **Tiered Caching & Resilience:**
   - Reference/lookup data (Org Units, Code tables) is cached in-memory with TTL via Spring Cache / Caffeine.
   - Volatile transactional validations (Cut Block availability, ownership) bypass caching with strict query/HTTP timeouts.
   - Context-aware degradation: Transactional operations fail fast with a descriptive 503/422 domain error, while read-only displays gracefully degrade with fallback indicators.

---

## Consequences

### Positive
- **Zero Coupling to External Schemas:** Core Silva services and controllers interact only with clean Silva domain models, completely insulated from legacy Oracle column names or schema renames.
- **High-Throughput Read & Search Performance:** Adopting CQRS read projections allows multi-criteria searches, composite dashboards, spatial GIS joins, and reporting to execute locally within PostgreSQL with standard indexes, completely shielding Silva read paths from external network latency or downstream service outages.
- **Seamless API Transition:** Switching an external dependency from Oracle DB to REST API requires implementing a new adapter class and toggling a configuration property; zero modifications to Silva domain services.
- **Independent Modernization Timelines:** Each external dependency (Tenures/FTA, Forest Client, Org Units) can migrate at its own pace without blocking Silva or other dependencies.
- **Improved Testability:** Developers can run the full backend locally or in CI without requiring an active Oracle connection by running in PostgreSQL mode with mock providers and `silva.integrations.external-oracle.enabled=false`.
- **Lightweight Runtime Footprint:** Using `NamedParameterJdbcTemplate` for external Oracle queries avoids the overhead and complexity of configuring secondary JPA persistence units.

### Negative / Trade-offs
- **Additional Abstraction Overhead:** Developers must maintain Provider interfaces and DTOs in addition to the concrete adapter.
- **Read-Model Projection Synchronization:** Maintaining local CQRS read projections for external dependencies requires write-through synchronization, CDC, or event ingestion to handle eventual consistency.
- **Dual Connection Pool Management:** In `primary-db=postgres` mode, an extra lightweight Hikari connection pool is maintained for external Oracle access when `oracle` provider is selected.
- **Eventual Consistency & Synchronization:** When external systems transition to APIs or separate databases, distributed consistency rules (e.g. handling orphaned opening references) must be managed at the application level.

---

## Alternatives Considered

1. **Secondary JPA Persistence Unit for External Oracle:**
   - *Rejected:* Maintaining two full JPA persistence units (entities, entity managers, transaction managers) causes substantial complexity, Hibernate dialect conflicts, and GraalVM native image build complications.
2. **Global Integration Mode Toggle (`silva.integrations.mode = oracle | api`):**
   - *Rejected:* Forces all external systems to migrate simultaneously, which is impossible given independent ministry development teams and timelines.
3. **PostgreSQL Foreign Data Wrapper (FDW) / DB-Link:**
   - *Rejected:* Couples PostgreSQL database infrastructure directly to Oracle database credentials, complicates deployment automation in OpenShift, and fails to provide a path to REST APIs.
