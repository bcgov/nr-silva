# Backend – Agent Guidelines

Spring Boot 3 backend (`ca.bc.gov.restapi.results`) that connects to **two live databases simultaneously**: Oracle (legacy source-of-truth) and Postgres (migrated target). Both are always wired; a single property selects which is "primary" at runtime.

## Dual-DB Architecture

### Runtime switch
```yaml
server:
  primary-db: postgres   # or "oracle"
```
`@ConditionalOnProperty(prefix = "server", name = "primary-db", havingValue = "postgres|oracle")` gates every repository, service, and any bean that is DB-specific. **Every new DB-specific class must carry this annotation.**

### JPA configuration
| DB | Config class | Package scanned | Persistence unit |
|----|-------------|-----------------|-----------------|
| Postgres | `PostgresJpaConfiguration` | `ca.bc.gov.restapi.results.postgres` | `postgres` (primary) |
| Oracle | `OracleJpaConfiguration` | `ca.bc.gov.restapi.results.oracle` | `oracle` |

The two `EntityManagerFactory` beans are completely separate. Entities/repositories **must** live in the correct package or they will be picked up by the wrong factory.

### Schema conventions
| DB | Schema | Table/column casing | Example |
|----|--------|---------------------|---------|
| Oracle | `THE` | `UPPER_CASE` | `THE.SILV_FUND_SRCE_CODE` / `SILV_FUND_SRCE_CODE` |
| Postgres | `silva` | `snake_case` | `silva.silv_fund_srce_code` / `silv_fund_srce_code` |

## Package Layout

```
common/    – interfaces, DTOs, abstract classes, shared endpoints, shared services
oracle/    – Oracle-specific entities, repositories, services
postgres/  – Postgres-specific entities, repositories, services
```

Endpoints always live in `common/endpoint/` and depend on a **common interface** (e.g. `CodeService`). They never import Oracle or Postgres packages directly.

## Adding a New Code Lookup Table

Follow `SilvFundSrceCode` as the canonical example. All six steps are required:

1. **Oracle entity** – `oracle/entity/code/XxxOracleEntity.java`
   - Extends `AbstractCodeOracleEntity`
   - `@Table(schema = "THE", name = "TABLE_NAME")`
   - `@AttributeOverride(name = "code", column = @Column(name = "TABLE_NAME", length = N))`

2. **Postgres entity** – `postgres/entity/code/XxxPostgresEntity.java`
   - Extends `AbstractCodePostgresEntity`
   - `@Table(schema = "silva", name = "table_name")`
   - `@AttributeOverride(name = "code", column = @Column(name = "table_name", length = N))`

3. **Oracle repository** – `oracle/repository/XxxOracleRepository.java`
   - `extends GenericCodeRepository<XxxOracleEntity>`
   - `@ConditionalOnProperty(..., havingValue = "oracle")`

4. **Postgres repository** – `postgres/repository/XxxPostgresRepository.java`
   - `extends GenericCodeRepository<XxxPostgresEntity>`
   - `@ConditionalOnProperty(..., havingValue = "postgres")`

5. **Service layer** (three files to touch):
   - Add `List<CodeDescriptionDto> getAllXxx()` to `common/service/CodeService.java`
   - Add `protected GenericCodeRepository<?> xxxRepository;` field + `@Override getAllXxx()` body to `common/service/impl/AbstractCodeService.java` (uses `@AllArgsConstructor`, so **field order = constructor order**)
   - Inject new repository in `oracle/service/CodeOracleService.java` and `postgres/service/CodePostgresService.java` — both delegate to `super(...)`

6. **Endpoint** – add `@GetMapping("/xxx")` method to `common/endpoint/CodesEndpoint.java` calling `codeService.getAllXxx()`

## Entity Inheritance Chain

```
GenericCodeEntity          (common/entity – fields: code, description, effectiveDate, expiryDate, updateTimestamp)
  └── AbstractCodeOracleEntity   (oracle – UPPER_CASE @AttributeOverrides, @MappedSuperclass)
        └── XxxOracleEntity      (oracle – supplies @Table + @AttributeOverride for the PK column)
  └── AbstractCodePostgresEntity (postgres – snake_case @AttributeOverrides, @MappedSuperclass)
        └── XxxPostgresEntity    (postgres – supplies @Table + @AttributeOverride for the PK column)
```

`GenericCodeEntity` exposes `isExpired()` and is the type used by `GenericCodeRepository<T>` and `CodeConverterUtil`.

## Key Shared Utilities

| Class | Purpose |
|-------|---------|
| `CodeConverterUtil.toCodeDescriptionDtos(list)` | Converts any `List<? extends GenericCodeEntity>` to `List<CodeDescriptionDto>` |
| `GenericCodeRepository<T>` | JPA base with `findAll()`, `findAllByOrderByExpiryDateDesc()`, `findAllByExpiryDateAfter(LocalDate)` |
| `SilvaConfiguration` | Spring `@ConfigurationProperties("ca.bc.gov.nrs")` — org units, external API addresses, data limits |

## Naming Conventions

- Oracle classes: `XxxOracleEntity`, `XxxOracleRepository`, `XxxOracleService` (or `CodeOracleService` if shared)
- Postgres classes: `XxxPostgresEntity`, `XxxPostgresRepository`, `XxxPostgresService`
- Common interfaces/abstracts: `XxxService` (interface), `AbstractXxxService` (abstract impl)
- Lombok: `@SuperBuilder` + `@With` + `@NoArgsConstructor` on all entities; `@AllArgsConstructor(access = PROTECTED)` on abstract services

---

## Search / Query Endpoints

### Endpoint map

| HTTP | Path | Controller | Notes |
|------|------|-----------|-------|
| GET | `/api/search/openings` | `SearchEndpoint` (common) | Paginated exact-match opening search |
| GET | `/api/search/activities` | `SearchEndpoint` (common) | Paginated activity/silviculture search |
| GET | `/api/search/disturbances` | `SearchEndpoint` (common) | Paginated disturbance search |
| GET | `/api/search/forest-cover` | `SearchEndpoint` (common) | Paginated forest cover search |
| GET | `/api/openings/{id}/tombstone` | `OpeningEndpoint` (common) | Opening summary/overview |
| GET | `/api/openings/{id}/ssu` | `OpeningEndpoint` (common) | Stocking standard units |
| GET | `/api/openings/favourites` | `OpeningEndpoint` (postgres) | User-favourite openings (Postgres only) |

`SearchEndpoint` lives in `common/endpoint/` and calls **common service interfaces only** — it never touches Oracle/Postgres packages directly.

### Guard: at least one filter required

All search endpoints call `filters.hasAnyFilter()` before hitting the DB. If no filters are set, throw `MissingSearchParameterException` (→ HTTP 400). Always implement `hasAnyFilter()` on new filter DTOs.

### Pagination

Use Spring's `@ParameterObject Pageable` on every paginated endpoint. The abstract service enforces `SilvaConstants.MAX_PAGE_SIZE` (500) or `MAX_PAGE_SIZE_OPENING_SEARCH` (2000) — validate with `validatePageSize(pagination)` from `AbstractOpeningSearchService`.

Date range filters (`updateDateStart` / `updateDateEnd`) are validated with `DateUtil.validateDateRange(start, end)`.

### Filter DTOs

Filter objects (e.g. `OpeningSearchExactFiltersDto`, `ActivitySearchFiltersDto`) live in `common/dto/`. They are plain `@Getter` + `@NoArgsConstructor` classes, not records, because `requestUserId` needs a `@Setter` at runtime. Use `SilvaConstants` string constants (not raw strings) for query-parameter names on the endpoint.

### How search queries are executed

Search queries are **not** Spring Data method names or `@Query` on the repository interface. They are implemented as **native SQL** (or JPQL) on the concrete Oracle/Postgres repository class, exposed through a `@NoRepositoryBean` common interface.

Pattern:
```
common/repository/OpeningRepository<T>        ← @NoRepositoryBean interface with method signatures
  oracle/repository/OpeningOracleRepository   ← @Repository + native query impl (Oracle SQL)
  postgres/repository/OpeningPostgresRepository ← @Repository + native query impl (Postgres SQL)
```

The abstract service holds a `protected final OpeningRepository<?>` field and calls it directly — it never knows whether it's Oracle or Postgres. The concrete service (e.g. `OpeningSearchPostgresService`) injects its DB-specific repository and passes it to `super(...)`.

Same pattern applies to `ActivityTreatmentUnitRepository` (used by `ActivityService`) and `ForestCoverRepository` (used by `ForestCoverService`).

### Projection interfaces

Query results are mapped to **projection interfaces** in `common/projection/`, not to entities. Each interface field maps to a SQL column alias. The `getTotalCount()` field on search projections carries the total row count from a `COUNT(*) OVER()` window function in the native query, avoiding a second COUNT query.

Key projections:
| Projection | Used for |
|-----------|---------|
| `SilvicultureSearchProjection` | Opening exact search results |
| `ActivitySearchProjection` | Activity search results |
| `DisturbanceSearchProjection` | Disturbance search results |
| `ForestCoverSearchProjection` | Forest cover search results |
| `OpeningTombstoneProjection` | Opening tombstone/details |

### ForestClient enrichment

After any search that returns client numbers, call `ForestClientApiProvider` (REST client to an external API) to enrich results with client acronyms/names. The abstract services build a `Map<String, ForestClientDto>` keyed by client number and use it during DTO mapping. Do not call the API per-row; batch distinct client numbers once.

### Opening Details – decomposed sub-services

`/api/openings/{id}/*` routes are handled by `OpeningEndpoint` (common) which delegates to `OpeningDetailsService` (interface). The implementation (`AbstractOpeningDetailsService`) orchestrates **eight focused sub-services**, each responsible for one slice of the opening:

| Sub-service interface | Responsibility |
|-----------------------|---------------|
| `OpeningDetailsTombstoneService` | Tombstone / overview |
| `OpeningDetailsStockingService` | SSU stocking standards |
| `OpeningDetailsActivitiesService` | Activities & disturbances tabs |
| `OpeningDetailsTenureService` | Tenure/licence info |
| `OpeningDetailsForestCoverService` | Forest cover list & details |
| `OpeningDetailsAttachmentService` | Attachments metadata |
| `OpeningForestCoverHistoryService` | Forest cover history |
| `OpeningStandardUnitHistoryService` | SSU history |

Each sub-service follows the same Abstract → Postgres/Oracle concrete pattern. The concrete `OpeningDetailsPostgresService` / `OpeningDetailsOracleService` simply call `super(...)` with the injected sub-services — the orchestration logic lives in `AbstractOpeningDetailsService`.

When adding a new opening detail section, add a method to `OpeningDetailsService` interface, implement it in the relevant sub-service abstract (or a new one), and expose it on `OpeningEndpoint`.

### Postgres-only endpoints

Some endpoints only make sense against Postgres (user state, spatial files) and live in `postgres/endpoint/` instead of `common/endpoint/`. They inject Postgres services directly and carry no `@ConditionalOnProperty` (Postgres is the primary/default DB):
- `postgres/endpoint/OpeningEndpoint` — favourites CRUD, spatial file upload
- `postgres/endpoint/UserRecentOpeningEndpoint` — recently viewed openings

---

## Integration Test Guidelines

### ForestClient API — always add WireMock

Any abstract integration test class whose service calls `ForestClientService` (i.e. the service does ForestClient enrichment after a DB query) **must** register a WireMock server on port 10000. Without it, the test will fail at runtime with `ResourceAccess I/O error … http://localhost:10000/clients/…`.

The forest-client API address is configured in `application-default.yml` as `http://localhost:10000`, so every integration test context shares the same port.

Add this block to **every** `Abstract*ServiceIntegrationTest` and `Abstract*EndpointIntegrationTest` that touches a service which does ForestClient enrichment:

```java
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;
import ca.bc.gov.restapi.results.extensions.WiremockLogNotifier;
import com.github.tomakehurst.wiremock.junit5.WireMockExtension;
import org.junit.jupiter.api.extension.RegisterExtension;

@RegisterExtension
static WireMockExtension clientApiStub =
    WireMockExtension.newInstance()
        .options(
            wireMockConfig()
                .port(10000)
                .notifier(new WiremockLogNotifier())
                .asynchronousResponseEnabled(true)
                .stubRequestLoggingDisabled(false))
        .configureStaticDsl(true)
        .build();
```

Services that currently do ForestClient enrichment (and therefore require WireMock in their tests):
- `AbstractActivityService` → `AbstractActivityServiceIntegrationTest` / `AbstractSearchEndpointActivitySearchIntegrationTest`
- `AbstractStandardUnitService` → `AbstractStandardUnitServiceIntegrationTest` / `AbstractSearchEndpointStandardUnitSearchIntegrationTest`
- `AbstractForestCoverService` → `AbstractSearchEndpointForestCoverSearchIntegrationTest`

When adding a new service that calls `ForestClientService`, add it to this list and add the WireMock block to its abstract integration test.

### Postgres native query — null-check pattern

PostgreSQL cannot infer the type of a bare `? IS NULL` parameter and will throw `could not determine data type of parameter $1`. Use the same pattern already established throughout `SilvaPostgresQueryConstants` for all nullable scalar bind parameters:

```sql
-- WRONG – type ambiguous
:#{#filter.someId} IS NULL

-- CORRECT – consistent with the rest of the query file
COALESCE(CAST(:#{#filter.someId} AS text), 'NOVALUE') = 'NOVALUE'
```

For `boolean` parameters where you need the actual typed value in a branch, `CAST(... AS boolean) IS NULL` is acceptable (see `isComplete` filter). For all other nullable scalars, prefer the `COALESCE(...text..., 'NOVALUE')` form.
