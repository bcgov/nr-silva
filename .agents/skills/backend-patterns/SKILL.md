---
name: backend-patterns
description: Backend coding patterns for Java Spring Boot, dual-database architecture, REST APIs, and testing in Silva
---

# Backend Patterns Skill

Reference guide for **backend coding conventions** in Silva (Java Spring Boot + dual Oracle/Postgres architecture). Use this skill when implementing backend features.

---

## Quick Navigation

**Choose your reference based on task type:**

### 1. **Database/Entity/Model/Query Pattern Tasks**
Load: `db-patterns.md`
- Dual-database architecture overview
- Entity inheritance chain (entity → DTO → mapping)
- Code lookup table 6-step pattern
- Native SQL query patterns for both databases
- Null-check patterns (Postgres vs. Oracle differences)
- Conditional bean registration

**Use when task involves:** "New entity", "database query", "JPA mapping", "nullable columns", "native query", "code lookup", "Postgres null handling", "Oracle compatibility"

---

### 2. **API Endpoint/Search/Service/REST Tasks**
Load: `api-patterns.md`
- Search endpoint conventions
- Response DTOs and projections
- Opening detail service architecture (8 sub-services)
- REST endpoint structure
- Pagination and filtering
- OpenAPI documentation

**Use when task involves:** "New endpoint", "add search", "REST API", "GET /openings", "search endpoint", "filtering", "pagination", "service layer", "DTOs", "response projection"

---

### 3. **Testing/Integration/Mock/Verification Tasks**
Load: `testing.md`
- Integration test setup (TestContainers, WireMock)
- Mockito patterns
- Database testing (Oracle vs. Postgres)
- Null-check test patterns
- ForestClient API enrichment & mocking
- Reflection binding for GraalVM
- Test database configuration

**Use when task involves:** "Write test", "integration test", "mock API", "TestContainers", "WireMock", "Mockito", "database test", "test setup", "null handling", "GraalVM reflection"

---

## Task Classification Decision Tree

**Apply this tree to classify the task before loading references:**

```
1. Does task involve database/entity/query/mapping/nullable columns?
   YES → Load db-patterns.md
   NO  → Go to 2

2. Does task involve API endpoint/search/REST service/DTO/response projection?
   YES → Load api-patterns.md
   NO  → Go to 3

3. Does task involve testing/mocking/integration tests/test setup?
   YES → Load testing.md
   NO  → Use main backend/AGENTS.md for general guidance

Multi-concern tasks:
- Identify PRIMARY concern (database vs. API vs. test)
- Load that reference first
- Mention in response: "Also loaded [secondary reference] for [reason]"
```

---

## Reference Scope Boundaries

### `db-patterns.md` — What's IN / What's OUT

**IN:**
- Dual-database architecture (Oracle legacy + Postgres target)
- Entity inheritance chain (entity → DTO → mapper)
- Code lookup table 6-step pattern
- Native SQL query patterns (dialect-specific)
- Null-check patterns (Postgres vs. Oracle)
- JPA mapping and conditional bean registration
- Runtime database switching (@ConditionalOnProperty)

**OUT:**
- REST endpoint design (see `api-patterns.md`)
- Service layer business logic (see `api-patterns.md`)
- Testing patterns (see `testing.md`)

**Cross-reference note:** "For REST endpoint design, see api-patterns.md. For testing, see testing.md."

---

### `api-patterns.md` — What's IN / What's OUT

**IN:**
- REST endpoint structure and naming
- Search endpoint conventions
- Response DTOs and projections
- Opening detail architecture (8 sub-services)
- Pagination, filtering, sorting
- OpenAPI documentation
- Service layer patterns

**OUT:**
- Database entity design (see `db-patterns.md`)
- Testing and mocking (see `testing.md`)
- Native query patterns (see `db-patterns.md`)

**Cross-reference note:** "For entity design, see db-patterns.md. For testing services, see testing.md."

---

### `testing.md` — What's IN / What's OUT

**IN:**
- Integration test setup (TestContainers, WireMock)
- Mockito patterns
- Database testing (both Oracle and Postgres)
- Null-check test patterns specific to database
- ForestClient API enrichment & mocking
- Reflection binding for GraalVM native image
- Test database configuration
- Spring Boot test annotations

**OUT:**
- Entity design (see `db-patterns.md`)
- Endpoint design (see `api-patterns.md`)

**Cross-reference note:** "For entity/database patterns, see db-patterns.md. For endpoint patterns, see api-patterns.md."

---

## Critical Warning

### Frontend API Folder — Do Not Edit

The **frontend repository contains an auto-generated API client** in `frontend/src/services/OpenApi/**` that is generated from this backend's OpenAPI specification. **Never modify files in that folder.**

- Frontend regenerates this folder when: `npm run generate:openapi` (user action after backend changes)
- Regeneration uses the OpenAPI spec exposed by **this backend**
- **Do NOT commit changes** to `frontend/src/services/OpenApi/**` — they will be overwritten

This warning must remain in the backend AGENTS.md because it's a critical cross-layer safety guideline.

---

## Fallback: When Unsure

If the task straddles categories:
1. Classify PRIMARY concern using decision tree above
2. Load that reference
3. Mention in response which reference you loaded and why
4. Note: "If you need [secondary concern] patterns, load [reference name]"
