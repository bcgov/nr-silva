# Database Patterns & Dual-Database Architecture

## Dual-Database Architecture

Silva maintains **two production databases:**
- **Oracle** — Legacy database (RESULTS system, active production data)
- **Postgres** — Target database (new Silva system, migration in progress)

The backend must support both databases **simultaneously** because data migration is incremental. Users switch between databases using runtime configuration flags.

### Runtime Database Switching

Configuration via `application.properties` / `application-{profile}.yml`:

```yaml
# application.yml
spring:
  jpa:
    hibernate:
      ddl-auto: validate
  datasource:
    primary: oracle  # or 'postgres' — switches all database access
```

Database-specific classes are **conditionally registered** using `@ConditionalOnProperty`:

```java
@Entity
@ConditionalOnProperty(
  name = "spring.datasource.primary",
  havingValue = "oracle"
)
public class OpeningEntity {
  // Oracle-specific mapping
  @Column(name = "OPENING_ID")
  private String id;
}

@Entity
@ConditionalOnProperty(
  name = "spring.datasource.primary",
  havingValue = "postgres"
)
public class OpeningEntity {
  // Postgres-specific mapping
  @Column(name = "opening_id")
  private String id;
}
```

---

## Entity → DTO → Mapper Chain

**All entities follow a three-layer structure:**

1. **Entity** — JPA-mapped database model
2. **DTO** — Data Transfer Object (what REST API returns)
3. **Mapper** — Converts entity → DTO

### Example: Opening Entity

```java
// 1. ENTITY - JPA model mapped to database
@Entity
@Table(name = "OPENINGS")
public class OpeningEntity {
  @Id
  private String id;

  @Column(name = "OPENING_NAME")
  private String name;

  @Column(name = "REGION")
  private String region;

  // Getters/setters
}

// 2. DTO - REST API response
public class OpeningDTO {
  private String id;
  private String name;
  private String region;

  // Constructor, getters, Jackson annotations
  @JsonProperty("opening_id")
  private String id;
}

// 3. MAPPER - Converts entity to DTO
@Component
public class OpeningMapper {
  public OpeningDTO toDTO(OpeningEntity entity) {
    if (entity == null) return null;
    return new OpeningDTO(
      entity.getId(),
      entity.getName(),
      entity.getRegion()
    );
  }

  public List<OpeningDTO> toDTOList(List<OpeningEntity> entities) {
    return entities.stream()
      .map(this::toDTO)
      .collect(Collectors.toList());
  }
}
```

### Usage in Service

```java
@Service
public class OpeningService {
  @Autowired
  private OpeningRepository repo;

  @Autowired
  private OpeningMapper mapper;

  public OpeningDTO getOpening(String id) {
    OpeningEntity entity = repo.findById(id).orElse(null);
    return mapper.toDTO(entity);  // Convert entity to DTO
  }

  public List<OpeningDTO> getAllOpenings() {
    List<OpeningEntity> entities = repo.findAll();
    return mapper.toDTOList(entities);  // Convert list
  }
}
```

---

## Code Lookup Table Pattern (6-Step)

Forestry management uses code lookup tables (e.g., status codes, region codes). The 6-step pattern ensures consistency:

### Step 1: Create the Entity

```java
@Entity
@Table(name = "CODE_OPENING_STATUS")
public class OpeningStatusCodeEntity {
  @Id
  @Column(name = "CODE_ID")
  private String id;

  @Column(name = "CODE_DESCRIPTION")
  private String description;

  @Column(name = "EFFECTIVE_DATE")
  private LocalDate effectiveDate;

  // Getters/setters
}
```

### Step 2: Create the DTO

```java
public class OpeningStatusCodeDTO {
  @JsonProperty("code_id")
  private String id;

  @JsonProperty("description")
  private String description;

  @JsonProperty("effective_date")
  private LocalDate effectiveDate;

  // Constructor, getters, setters
}
```

### Step 3: Create the Repository

```java
@Repository
public interface OpeningStatusCodeRepository
    extends JpaRepository<OpeningStatusCodeEntity, String> {
  List<OpeningStatusCodeEntity> findByEffectiveDateLessThanEqual(LocalDate date);
}
```

### Step 4: Create the Mapper

```java
@Component
public class OpeningStatusCodeMapper {
  public OpeningStatusCodeDTO toDTO(OpeningStatusCodeEntity entity) {
    if (entity == null) return null;
    return new OpeningStatusCodeDTO(
      entity.getId(),
      entity.getDescription(),
      entity.getEffectiveDate()
    );
  }

  public List<OpeningStatusCodeDTO> toDTOList(List<OpeningStatusCodeEntity> entities) {
    return entities.stream()
      .map(this::toDTO)
      .collect(Collectors.toList());
  }
}
```

### Step 5: Create the Service

```java
@Service
public class OpeningStatusCodeService {
  @Autowired
  private OpeningStatusCodeRepository repo;

  @Autowired
  private OpeningStatusCodeMapper mapper;

  public List<OpeningStatusCodeDTO> getAllValidCodes() {
    List<OpeningStatusCodeEntity> entities =
      repo.findByEffectiveDateLessThanEqual(LocalDate.now());
    return mapper.toDTOList(entities);
  }

  public OpeningStatusCodeDTO getCode(String id) {
    return mapper.toDTO(repo.findById(id).orElse(null));
  }
}
```

### Step 6: Expose via REST Controller

```java
@RestController
@RequestMapping("/api/codes/opening-status")
public class OpeningStatusCodeController {
  @Autowired
  private OpeningStatusCodeService service;

  @GetMapping
  public ResponseEntity<List<OpeningStatusCodeDTO>> getCodes() {
    return ResponseEntity.ok(service.getAllValidCodes());
  }

  @GetMapping("/{id}")
  public ResponseEntity<OpeningStatusCodeDTO> getCode(@PathVariable String id) {
    return ResponseEntity.ok(service.getCode(id));
  }
}
```

---

## Native SQL Query Patterns

Some queries are too complex for JPA/JPQL, requiring native SQL. Because the backend supports **both Oracle and Postgres**, native queries must be **dialect-specific**.

### Dialect-Specific Queries

Use `@Query(nativeQuery = true)` with SQL that works for **your current database**. Different databases use different syntax:

**Oracle Example:**

```java
@Repository
public interface OpeningRepository extends JpaRepository<OpeningEntity, String> {
  @Query(
    value = "SELECT o.* FROM OPENINGS o " +
            "WHERE o.REGION = :region " +
            "AND UPPER(o.OPENING_NAME) LIKE UPPER(:name) " +
            "ORDER BY o.OPENING_NAME",
    nativeQuery = true
  )
  List<OpeningEntity> searchByRegionAndName(
    @Param("region") String region,
    @Param("name") String name
  );
}
```

**Postgres Example (same query, Postgres syntax):**

```java
@Query(
  value = "SELECT o.* FROM openings o " +
          "WHERE o.region = :region " +
          "AND LOWER(o.opening_name) LIKE LOWER(:name) " +
          "ORDER BY o.opening_name",
  nativeQuery = true
)
```

**Key Differences:**
- **Oracle:** UPPER(), no semicolon at end, schema objects uppercase
- **Postgres:** LOWER() or ::text, no UPPER() for case-insensitive, schema objects lowercase

---

## Null-Check Patterns

**Oracle and Postgres handle NULLs differently.** Always validate before processing.

### Oracle NULL Handling

Oracle treats empty strings (`''`) as NULL. Use `IS NOT NULL` in queries:

```java
@Query(
  value = "SELECT o.* FROM OPENINGS o WHERE o.DESCRIPTION IS NOT NULL",
  nativeQuery = true
)
List<OpeningEntity> findWithDescription();
```

### Postgres NULL Handling

Postgres distinguishes between `NULL` and empty strings. Use explicit NULL checks:

```java
@Query(
  value = "SELECT o.* FROM openings o WHERE o.description IS NOT NULL AND o.description != ''",
  nativeQuery = true
)
List<OpeningEntity> findWithDescription();
```

### Safe Null-Check Pattern in Java

Always check before using fields returned from database queries:

```java
public OpeningDTO getOpeningDetails(String id) {
  OpeningEntity entity = repo.findById(id).orElse(null);
  if (entity == null) {
    return null;  // or throw exception
  }

  // Safe to access fields
  String name = entity.getName();
  if (name == null || name.isEmpty()) {
    name = "Unknown";  // Provide default
  }

  return new OpeningDTO(id, name);
}
```

---

## Conditional Bean Registration

Use `@ConditionalOnProperty` to register different implementations based on active database:

```java
@Configuration
public class DataSourceConfig {

  @Bean
  @ConditionalOnProperty(
    name = "spring.datasource.primary",
    havingValue = "oracle"
  )
  public DataSource oracleDataSource() {
    // Oracle-specific configuration
    return new OracleDataSource();
  }

  @Bean
  @ConditionalOnProperty(
    name = "spring.datasource.primary",
    havingValue = "postgres"
  )
  public DataSource postgresDataSource() {
    // Postgres-specific configuration
    return new PostgresDataSource();
  }
}
```
