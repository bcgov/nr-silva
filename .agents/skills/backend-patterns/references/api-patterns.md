# API Patterns & REST Endpoints

## Search Endpoint Convention

Silva provides search endpoints to filter collections. All search endpoints follow a consistent pattern:

### Structure

```
GET /api/{resource}/search?param1=value1&param2=value2
```

### Example: Search Openings

```java
@RestController
@RequestMapping("/api/openings")
public class OpeningController {
  @Autowired
  private OpeningService service;

  @GetMapping("/search")
  public ResponseEntity<List<OpeningDTO>> search(
    @RequestParam(required = false) String region,
    @RequestParam(required = false) String status,
    @RequestParam(required = false) String name,
    @RequestParam(defaultValue = "0") int page,
    @RequestParam(defaultValue = "20") int size
  ) {
    List<OpeningDTO> results = service.search(region, status, name, page, size);
    return ResponseEntity.ok(results);
  }
}
```

### Query Key Convention (Frontend)

Frontend query keys must match endpoint structure:

| Endpoint | Query Key |
|----------|-----------|
| `GET /api/openings` | `['openings']` |
| `GET /api/openings/123` | `['openings', '123']` |
| `GET /api/openings/search?region=x` | `['openings', 'search', { region: 'x' }]` |
| `GET /api/openings/123/activities` | `['openings', '123', 'activities']` |

---

## Response DTOs & Projections

Always return **DTOs, not entities**. DTOs allow you to control exactly what data is exposed to the client.

### DTO Classes

Keep response DTOs lean and focused:

```java
public class OpeningDTO {
  @JsonProperty("opening_id")
  private String id;

  @JsonProperty("opening_name")
  private String name;

  @JsonProperty("region_code")
  private String regionCode;

  @JsonProperty("status_code")
  private String statusCode;

  // Constructor, getters, Jackson annotations
  public OpeningDTO(String id, String name, String regionCode, String statusCode) {
    this.id = id;
    this.name = name;
    this.regionCode = regionCode;
    this.statusCode = statusCode;
  }
}
```

### Projection DTOs (Partial Data)

For list views that don't need full details, create projection DTOs:

```java
// Lightweight DTO for listing
public class OpeningListDTO {
  @JsonProperty("opening_id")
  private String id;

  @JsonProperty("opening_name")
  private String name;

  @JsonProperty("region_code")
  private String regionCode;

  // Only essential fields
}

// Full DTO for detail view
public class OpeningDetailDTO {
  @JsonProperty("opening_id")
  private String id;

  @JsonProperty("opening_name")
  private String name;

  // ... all fields including complex nested data
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

  // List endpoint — use lightweight projection
  public List<OpeningListDTO> searchOpenings(String region, String status) {
    List<OpeningEntity> entities = repo.findByRegionAndStatus(region, status);
    return mapper.toListDTOs(entities);  // Minimal data
  }

  // Detail endpoint — use full DTO
  public OpeningDetailDTO getOpeningDetail(String id) {
    OpeningEntity entity = repo.findById(id).orElse(null);
    return mapper.toDetailDTO(entity);  // Full data
  }
}
```

---

## Opening Detail Architecture

The **Opening** is Silva's core entity. Its details are composed of **8 sub-services**, each providing specific information:

| Sub-Service | Responsibility |
|-------------|-----------------|
| `OpeningOverviewService` | Basic opening info (name, region, area, status) |
| `OpeningSummaryService` | Aggregated summary statistics (units planned, harvested) |
| `OpeningActivitiesService` | Activities/events related to the opening |
| `OpeningTeamService` | Team members assigned to opening |
| `OpeningMapService` | Geographic/map data (coordinates, GIS boundaries) |
| `OpeningDocumentsService` | Related documents and attachments |
| `OpeningHistoryService` | Change history and audit trail |
| `OpeningComplianceService` | Compliance status and certifications |

### Architecture Pattern

Each sub-service is **independently injected** into the main `OpeningDetailService`:

```java
@Service
public class OpeningDetailService {
  @Autowired
  private OpeningOverviewService overviewService;

  @Autowired
  private OpeningSummaryService summaryService;

  @Autowired
  private OpeningActivitiesService activitiesService;

  @Autowired
  private OpeningTeamService teamService;

  @Autowired
  private OpeningMapService mapService;

  @Autowired
  private OpeningDocumentsService documentsService;

  @Autowired
  private OpeningHistoryService historyService;

  @Autowired
  private OpeningComplianceService complianceService;

  /**
   * Get complete opening details by combining all sub-services
   */
  public OpeningDetailDTO getFullDetails(String openingId) {
    return new OpeningDetailDTO(
      overviewService.getOverview(openingId),
      summaryService.getSummary(openingId),
      activitiesService.getActivities(openingId),
      teamService.getTeam(openingId),
      mapService.getMapData(openingId),
      documentsService.getDocuments(openingId),
      historyService.getHistory(openingId),
      complianceService.getCompliance(openingId)
    );
  }
}
```

### Benefits of This Pattern

- **Modularity:** Each sub-service is independent and testable
- **Scalability:** Easy to add new sub-services
- **Maintainability:** Changes to one sub-service don't affect others
- **Caching:** Each sub-service can have its own caching strategy
- **Pagination:** Large result sets (activities, history) can be paginated per sub-service

---

## REST Endpoint Structure

All endpoints expose data via REST with consistent conventions:

### GET Endpoints

```java
@GetMapping
public ResponseEntity<List<OpeningDTO>> getAll() { /* ... */ }

@GetMapping("/{id}")
public ResponseEntity<OpeningDTO> getById(@PathVariable String id) { /* ... */ }

@GetMapping("/search")
public ResponseEntity<List<OpeningDTO>> search(@RequestParam String region) { /* ... */ }
```

### POST Endpoints

```java
@PostMapping
public ResponseEntity<OpeningDTO> create(@RequestBody CreateOpeningRequest request) { /* ... */ }
```

### PUT Endpoints

```java
@PutMapping("/{id}")
public ResponseEntity<OpeningDTO> update(
  @PathVariable String id,
  @RequestBody UpdateOpeningRequest request
) { /* ... */ }
```

### DELETE Endpoints

```java
@DeleteMapping("/{id}")
public ResponseEntity<Void> delete(@PathVariable String id) { /* ... */ }
```

---

## Pagination & Filtering

Use Spring Data's `Pageable` and `Specification` for complex queries:

```java
@GetMapping
public ResponseEntity<Page<OpeningDTO>> getAll(
  @ParameterObject Pageable pageable,
  @RequestParam(required = false) String region,
  @RequestParam(required = false) String status
) {
  Specification<OpeningEntity> spec = Specification.where(null);

  if (region != null) {
    spec = spec.and((root, query, cb) -> cb.equal(root.get("region"), region));
  }
  if (status != null) {
    spec = spec.and((root, query, cb) -> cb.equal(root.get("status"), status));
  }

  Page<OpeningEntity> page = repo.findAll(spec, pageable);
  Page<OpeningDTO> result = page.map(mapper::toDTO);
  return ResponseEntity.ok(result);
}
```

---

## OpenAPI Documentation

All endpoints must be documented with OpenAPI annotations for automatic API documentation generation:

```java
@GetMapping("/{id}")
@Operation(summary = "Get opening by ID", description = "Retrieve a specific opening")
@ApiResponse(responseCode = "200", description = "Opening found")
@ApiResponse(responseCode = "404", description = "Opening not found")
public ResponseEntity<OpeningDTO> getById(
  @PathVariable @Parameter(description = "Opening ID") String id
) {
  OpeningDTO result = service.getOpening(id);
  return ResponseEntity.ok(result);
}
```
