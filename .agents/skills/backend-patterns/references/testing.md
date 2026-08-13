# Integration Testing & Mocking

## TestContainers Setup

**TestContainers** provides real database containers for integration tests. This ensures tests validate against the actual database (Oracle or Postgres) instead of mocks.

### Maven Dependency

```xml
<!-- pom.xml -->
<dependency>
  <groupId>org.testcontainers</groupId>
  <artifactId>testcontainers</artifactId>
  <version>1.19.0</version>
  <scope>test</scope>
</dependency>
<dependency>
  <groupId>org.testcontainers</groupId>
  <artifactId>postgresql</artifactId>
  <version>1.19.0</version>
  <scope>test</scope>
</dependency>
<dependency>
  <groupId>org.testcontainers</groupId>
  <artifactId>oracle-xe</artifactId>
  <version>1.19.0</version>
  <scope>test</scope>
</dependency>
```

### Base Test Configuration

```java
// src/test/java/com/bcgov/silva/BaseIntegrationTest.java
@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
public abstract class BaseIntegrationTest {

  @Container
  static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15")
    .withDatabaseName("silva_test")
    .withUsername("test")
    .withPassword("test");

  @DynamicPropertySource
  static void postgresProperties(DynamicPropertyRegistry registry) {
    registry.add("spring.datasource.url", postgres::getJdbcUrl);
    registry.add("spring.datasource.username", postgres::getUsername);
    registry.add("spring.datasource.password", postgres::getPassword);
  }
}
```

### Usage in Test

```java
@SpringBootTest
@Testcontainers
public class OpeningRepositoryIntegrationTest extends BaseIntegrationTest {

  @Autowired
  private OpeningRepository repo;

  @Test
  void shouldSaveAndRetrieveOpening() {
    // Arrange
    OpeningEntity entity = new OpeningEntity();
    entity.setId("OPENING-001");
    entity.setName("Test Opening");
    entity.setRegion("Region A");

    // Act
    OpeningEntity saved = repo.save(entity);

    // Assert
    assertThat(saved.getId()).isEqualTo("OPENING-001");
    assertThat(saved.getName()).isEqualTo("Test Opening");

    // Verify in database
    Optional<OpeningEntity> retrieved = repo.findById("OPENING-001");
    assertThat(retrieved).isPresent();
    assertThat(retrieved.get().getName()).isEqualTo("Test Opening");
  }
}
```

---

## WireMock for External API Mocking

**WireMock** mocks external API calls (e.g., ForestClient API) without hitting real endpoints.

### Maven Dependency

```xml
<dependency>
  <groupId>com.github.tomakehurst</groupId>
  <artifactId>wiremock-jre8</artifactId>
  <version>2.35.0</version>
  <scope>test</scope>
</dependency>
```

### Setup

```java
@SpringBootTest
public class ForestClientIntegrationTest {

  @RegisterExtension
  static WireMockExtension wm = WireMockExtension.newInstance()
    .options(wireMockConfig().port(8088))
    .build();

  @Test
  void shouldMockForestClientResponse() {
    // Arrange
    wm.stubFor(
      get("/forestClient/operations?region=Region A")
        .willReturn(aResponse()
          .withHeader("Content-Type", "application/json")
          .withBody("{\"operations\": [{\"id\": \"OP-123\", \"name\": \"Test Operation\"}]}")
          .withStatus(200))
    );

    // Act
    List<ForestClientOperation> operations = forestClientService.getOperations("Region A");

    // Assert
    assertThat(operations).hasSize(1);
    assertThat(operations.get(0).getId()).isEqualTo("OP-123");
  }
}
```

---

## Mockito for Unit Testing

**Mockito** creates mock objects for unit tests without requiring external services.

### Basic Mocking

```java
@ExtendWith(MockitoExtension.class)
public class OpeningServiceTest {

  @Mock
  private OpeningRepository repo;

  @Mock
  private OpeningMapper mapper;

  @InjectMocks
  private OpeningService service;

  @Test
  void shouldReturnOpeningDTO() {
    // Arrange
    OpeningEntity entity = new OpeningEntity();
    entity.setId("OPENING-001");
    entity.setName("Test Opening");

    OpeningDTO dto = new OpeningDTO("OPENING-001", "Test Opening", "Region A", "ACTIVE");

    when(repo.findById("OPENING-001")).thenReturn(Optional.of(entity));
    when(mapper.toDTO(entity)).thenReturn(dto);

    // Act
    OpeningDTO result = service.getOpening("OPENING-001");

    // Assert
    assertThat(result).isNotNull();
    assertThat(result.getId()).isEqualTo("OPENING-001");

    // Verify interactions
    verify(repo).findById("OPENING-001");
    verify(mapper).toDTO(entity);
  }
}
```

---

## Database NULL Testing

Database NULL handling differs between Oracle and Postgres. Always test NULL scenarios.

### Postgres NULL Scenario

```java
@SpringBootTest
@Testcontainers
public class OpeningNullHandlingTest {

  @Container
  static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15");

  @Autowired
  private OpeningRepository repo;

  @Test
  void shouldHandleNullDescriptionInPostgres() {
    // Arrange
    OpeningEntity entity = new OpeningEntity();
    entity.setId("OPENING-001");
    entity.setName("Test Opening");
    entity.setDescription(null);  // NULL value

    // Act
    repo.save(entity);
    Optional<OpeningEntity> retrieved = repo.findById("OPENING-001");

    // Assert
    assertThat(retrieved).isPresent();
    assertThat(retrieved.get().getDescription()).isNull();
  }

  @Test
  void shouldFindOpeningsWithoutNullDescription() {
    // Query that filters for non-null descriptions
    List<OpeningEntity> results = repo.findByDescriptionIsNotNull();
    assertThat(results).isEmpty();  // Because we set description to NULL
  }
}
```

### Oracle NULL Scenario

```java
@SpringBootTest
@Testcontainers
public class OracleNullHandlingTest {

  @Container
  static OracleContainer oracle = new OracleContainer("gvenzl/oracle-xe:latest");

  @Autowired
  private OpeningRepository repo;

  @Test
  void shouldTreatEmptyStringAsNullInOracle() {
    // Oracle treats empty string '' as NULL
    OpeningEntity entity = new OpeningEntity();
    entity.setId("OPENING-001");
    entity.setName("Test Opening");
    entity.setDescription("");  // Empty string

    repo.save(entity);
    Optional<OpeningEntity> retrieved = repo.findById("OPENING-001");

    // In Oracle, empty string becomes NULL
    assertThat(retrieved.get().getDescription()).isNull();
  }
}
```

---

## ForestClient API Enrichment

Silva enriches opening data by calling the external **ForestClient API**. Always mock this in tests to avoid external dependencies.

### WireMock Setup for ForestClient

```java
@SpringBootTest
public class OpeningEnrichmentTest {

  @RegisterExtension
  static WireMockExtension wm = WireMockExtension.newInstance()
    .options(wireMockConfig().port(8088))
    .build();

  @Autowired
  private OpeningService service;

  @Test
  void shouldEnrichOpeningWithForestClientData() {
    // Arrange
    wm.stubFor(
      get("/forestClient/clients/CLIENT-123")
        .willReturn(aResponse()
          .withHeader("Content-Type", "application/json")
          .withBody("{\"client_id\": \"CLIENT-123\", \"client_name\": \"Test Client\", \"email\": \"client@test.com\"}")
          .withStatus(200))
    );

    // Act
    OpeningDetailDTO result = service.getOpeningWithEnrichment("OPENING-001");

    // Assert
    assertThat(result.getClientName()).isEqualTo("Test Client");
    assertThat(result.getClientEmail()).isEqualTo("client@test.com");
  }

  @Test
  void shouldHandleForestClientMissingData() {
    // Arrange
    wm.stubFor(
      get("/forestClient/clients/CLIENT-MISSING")
        .willReturn(aResponse()
          .withStatus(404))
    );

    // Act
    OpeningDetailDTO result = service.getOpeningWithEnrichment("OPENING-001");

    // Assert
    assertThat(result.getClientName()).isNull();  // No data available
    // Opening data should still be available
    assertThat(result.getName()).isNotNull();
  }
}
```

---

## GraalVM Native Image Reflection Binding

When compiling with **GraalVM native image**, reflection requires explicit configuration.

### Reflection Configuration

Create `src/main/resources/META-INF/native-image/com.bcgov.silva/silva-backend/reflect-config.json`:

```json
[
  {
    "name": "com.bcgov.silva.entity.OpeningEntity",
    "allDeclaredConstructors": true,
    "allPublicConstructors": true,
    "allDeclaredMethods": true,
    "allDeclaredFields": true
  },
  {
    "name": "com.bcgov.silva.dto.OpeningDTO",
    "allDeclaredConstructors": true,
    "allPublicConstructors": true,
    "allDeclaredMethods": true,
    "allDeclaredFields": true
  }
]
```

### Test for Native Image Compatibility

```java
@SpringBootTest
public class NativeImageTest {

  @Test
  void shouldReflectivelyAccessOpeningEntity() throws Exception {
    Class<?> clazz = Class.forName("com.bcgov.silva.entity.OpeningEntity");

    // Verify all constructors are accessible
    Constructor<?>[] constructors = clazz.getDeclaredConstructors();
    assertThat(constructors).isNotEmpty();

    // Verify all fields are accessible
    Field[] fields = clazz.getDeclaredFields();
    assertThat(fields.length).isGreaterThan(0);
  }
}
```
