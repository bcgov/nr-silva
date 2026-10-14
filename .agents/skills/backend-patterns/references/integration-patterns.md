# External Systems Integration & Anti-Corruption Layer (ACL) Patterns

## Overview

Silva interacts with external legacy databases and external services (e.g., FTA for Cut Blocks/Tenures, Forest Client API, Org Units). To prevent coupling Silva's domain to external schemas or column names, all external integrations follow the **Ports and Adapters (Hexagonal)** pattern with an **Anti-Corruption Layer (ACL)**.

---

## Core Rules for Agents

1. **Never inject external repositories or entities directly into Silva domain services:**
   - ❌ **WRONG:** Injecting `CutBlockPostgresRepository` or `CutBlockOracleRepository` into `TenureValidationService`.
   - ✅ **RIGHT:** Injecting `TenureProvider` interface into `TenureValidationService`.

2. **Define functional contracts, not CRUD table mirrors:**
   - ❌ **WRONG:** `CutBlockEntity findById(Long cbSkey)` (leaks external primary key and table structure).
   - ✅ **RIGHT:** `CutBlockValidationDetails validateCutBlock(fileId, cuttingPermit, cutBlock, clientNumber, openingId)` (expresses business intent and returns clean Silva domain record).

3. **Per-domain provider switching via `@ConditionalOnProperty`:**
   - Every provider has a configuration property: `silva.integrations.<domain>.provider=oracle|api|mock`.
   - Never hardcode the adapter; allow runtime switching.

4. **External Oracle queries use `NamedParameterJdbcTemplate`:**
   - Do NOT create JPA `@Entity` classes for external tables.
   - Use `NamedParameterJdbcTemplate externalOracleJdbcTemplate` with raw SQL and explicit row mappers inside the Oracle adapter.

5. **Provide a Mock adapter for offline testing:**
   - Every `XxxProvider` must have a corresponding `MockXxxAdapter` annotated with `@ConditionalOnProperty(prefix = "silva.integrations.<domain>", name = "provider", havingValue = "mock")`.

---

## 5-Step Integration Pattern

When adding or refactoring an external dependency:

### Step 1: Create Domain Records / Value Objects in `common/dto/`
Define immutable records representing the data Silva actually needs.
```java
package ca.bc.gov.restapi.results.common.dto;

public record CutBlockValidationDetails(
    boolean exists,
    boolean clientIsLicensee,
    boolean availableForOpening,
    Long existingOpeningId,
    String timberMark,
    String status
) {
  public static CutBlockValidationDetails notFound() {
    return new CutBlockValidationDetails(false, false, false, null, null, null);
  }
}
```

### Step 2: Define Provider Interface (Port) in `common/provider/`
Define the functional contract.
```java
package ca.bc.gov.restapi.results.common.provider;

public interface TenureProvider {
  CutBlockValidationDetails validateCutBlock(
      String forestFileId,
      String cuttingPermitId,
      String cutBlockId,
      String clientNumber,
      Long excludedOpeningId);
}
```

### Step 3: Implement Oracle JDBC Adapter in `oracle/provider/`
Encapsulate all external table queries, column names, and joins.
```java
@Component
@RequiredArgsConstructor
@ConditionalOnProperty(
    prefix = "silva.integrations.tenure",
    name = "provider",
    havingValue = "oracle",
    matchIfMissing = true)
public class OracleTenureJdbcAdapter implements TenureProvider {
  private final NamedParameterJdbcTemplate externalOracleJdbcTemplate;

  @Override
  public CutBlockValidationDetails validateCutBlock(...) {
    // Execute SQL query against THE.CUT_BLOCK, etc.
  }
}
```

### Step 4: Implement Mock Adapter in `common/provider/mock/`
Allow local dev and unit/integration testing without Oracle credentials.
```java
@Component
@ConditionalOnProperty(
    prefix = "silva.integrations.tenure",
    name = "provider",
    havingValue = "mock")
public class MockTenureAdapter implements TenureProvider {
  @Override
  public CutBlockValidationDetails validateCutBlock(...) {
    return new CutBlockValidationDetails(true, true, true, null, "47/12K", "ACT");
  }
}
```

### Step 5: Consume in Silva Domain Service
Inject the interface, not a concrete class or repository.
```java
@Service
@RequiredArgsConstructor
public class TenureValidationService {
  private final TenureProvider tenureProvider;

  public TenureValidationResponseDto validateTenures(List<TenureRequestDto> tenures, String clientNumber) {
    for (TenureRequestDto req : tenures) {
      CutBlockValidationDetails details = tenureProvider.validateCutBlock(
          req.fileId(), req.cuttingPermit(), req.cutBlock(), clientNumber, null);
      // Process validation results...
    }
  }
}
```

---

## When an External System Modernizes to REST API

To switch an external dependency from Oracle to a REST API:
1. Implement `RestApiXxxAdapter` implementing `XxxProvider` in `common/provider/` using `RestClient`.
2. Add `@ConditionalOnProperty(prefix = "silva.integrations.<domain>", name = "provider", havingValue = "api")`.
3. Set `SILVA_INTEGRATION_<DOMAIN>_PROVIDER=api` in OpenShift config.
4. **Silva domain services require ZERO code changes.**
