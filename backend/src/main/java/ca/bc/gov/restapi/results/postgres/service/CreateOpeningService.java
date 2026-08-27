package ca.bc.gov.restapi.results.postgres.service;

import ca.bc.gov.restapi.results.common.exception.NotFoundGenericException;
import ca.bc.gov.restapi.results.common.exception.OpeningCategoryNotFoundException;
import ca.bc.gov.restapi.results.common.security.LoggedUserHelper;
import ca.bc.gov.restapi.results.common.service.OpenMapsService;
import ca.bc.gov.restapi.results.postgres.dto.CreateOpeningRequestDto;
import ca.bc.gov.restapi.results.postgres.dto.CreateOpeningResponseDto;
import ca.bc.gov.restapi.results.postgres.dto.ExtractedGeoDataDto;
import ca.bc.gov.restapi.results.postgres.dto.MapsheetDto;
import ca.bc.gov.restapi.results.postgres.dto.TenureRequestDto;
import ca.bc.gov.restapi.results.postgres.entity.CutBlockEntity;
import ca.bc.gov.restapi.results.postgres.entity.CutBlockOpenAdminEntity;
import ca.bc.gov.restapi.results.postgres.entity.OpeningGeometryEntity;
import ca.bc.gov.restapi.results.postgres.entity.OrgUnitEntity;
import ca.bc.gov.restapi.results.postgres.entity.opening.OpeningEntity;
import ca.bc.gov.restapi.results.postgres.repository.OpenCategoryCodePostgresRepository;
import ca.bc.gov.restapi.results.postgres.repository.OpeningGeometryPostgresRepository;
import ca.bc.gov.restapi.results.postgres.repository.OpeningPostgresRepository;
import ca.bc.gov.restapi.results.postgres.repository.OrgUnitPostgresRepository;
import ca.bc.gov.restapi.results.postgres.util.GeometryReprojectionUtils;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.io.geojson.GeoJsonReader;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

/** Service that handles the full lifecycle of creating a new opening. */
@Slf4j
@Service
@RequiredArgsConstructor
@ConditionalOnProperty(prefix = "server", name = "primary-db", havingValue = "postgres")
public class CreateOpeningService {

  private static final long FEATURE_CLASS_SKEY = 2409L;

  private final OpeningSpatialFileService openingSpatialFileService;
  private final OpenMapsService openMapsService;
  private final OpeningPostgresRepository openingRepository;
  private final OpeningGeometryPostgresRepository openingGeometryRepository;
  private final OpenCategoryCodePostgresRepository openCategoryCodeRepository;
  private final OrgUnitPostgresRepository orgUnitRepository;
  private final TenureValidationService tenureValidationService;
  private final OpeningTenureAssociationService tenureAssociationService;
  private final OpeningTenureAssociationHistoryService tenureAssociationHistoryService;
  private final LoggedUserHelper loggedUserHelper;
  private final JdbcTemplate jdbcTemplate;
  private final ObjectMapper objectMapper;

  /**
   * Creates a new opening by processing the uploaded spatial file, deriving the BCGS mapsheet,
   * validating business rules, and persisting the opening, its geometry, and associated tenures.
   *
   * @param dto the opening creation request containing business fields and tenure details
   * @param fileName the original name of the uploaded spatial file
   * @param fileBytes the raw content of the uploaded spatial file
   * @return a {@link CreateOpeningResponseDto} containing the new opening's ID
   * @throws ResponseStatusException with HTTP 422 if the file is infected or the mapsheet cannot be
   *     derived; HTTP 400 for business rule violations; HTTP 403 if the caller is not authorized
   *     for the supplied client number; HTTP 404 if the category or org unit is not found
   */
  @Transactional
  public CreateOpeningResponseDto createOpening(
      CreateOpeningRequestDto dto, String fileName, byte[] fileBytes) {

    // Step 1: process spatial file (validates, thins, reprojects to EPSG:4326)
    ExtractedGeoDataDto geoData =
        openingSpatialFileService.processOpeningSpatialFile(fileName, fileBytes);

    // Step 2: parse GeoJSON → JTS Geometry in EPSG:4326; get centroid
    Geometry geometry4326 = parseAllGeometries(geoData.geoJson());
    Point centroid4326 = geometry4326.getCentroid();
    double lon = centroid4326.getX();
    double lat = centroid4326.getY();

    // Step 3: reproject 4326 → 3005; compute area and perimeter from projected geometry
    Geometry geometry3005 = reprojectTo3005(geometry4326);
    BigDecimal featureArea =
        BigDecimal.valueOf(geometry3005.getArea() / 10_000.0).setScale(4, RoundingMode.HALF_UP);
    BigDecimal featurePerimeter =
        BigDecimal.valueOf(geometry3005.getLength()).setScale(4, RoundingMode.HALF_UP);

    // Step 4: derive BCGS mapsheet via BC OpenMaps WFS
    MapsheetDto mapsheet = openMapsService.getMapsheetForPoint(lon, lat);

    // Step 5: next opening number within mapsheet tile
    Integer openingNumber =
        openingRepository.findNextOpeningNumber(
            mapsheet.grid(),
            mapsheet.letter(),
            mapsheet.square(),
            mapsheet.quad(),
            mapsheet.subQuad());

    // Step 6: trim string DTO fields handled by record immutability; null-safe trim where needed

    // Step 7: validate opening category code
    openCategoryCodeRepository
        .findById(dto.openingCategoryCode().trim())
        .orElseThrow(OpeningCategoryNotFoundException::new);

    // Step 8: resolve org unit number from code
    OrgUnitEntity orgUnit =
        orgUnitRepository
            .findByOrgUnitCode(dto.orgUnitCode().trim())
            .orElseThrow(() -> new NotFoundGenericException("OrgUnit"));
    Long orgUnitNo = orgUnit.getOrgUnitNo();

    // Step 9: resolve client number for tenure validation (auth delegated to
    // TenureValidationService)
    String clientNumber = dto.clientNumber().trim();

    // Step 10: exactly one primary tenure required
    long primaryCount = dto.tenures().stream().filter(TenureRequestDto::isPrimary).count();
    if (primaryCount == 0) {
      throw new ResponseStatusException(
          HttpStatus.BAD_REQUEST, "Exactly one primary tenure is required; none supplied");
    }
    if (primaryCount > 1) {
      throw new ResponseStatusException(
          HttpStatus.BAD_REQUEST,
          "Exactly one primary tenure is required; " + primaryCount + " supplied");
    }

    // Steps 11+12: validate tenures (field constraints, JWT auth, DB existence, licensee, CBOA dup)
    var tenureValidation = tenureValidationService.validateTenures(dto.tenures(), clientNumber);
    if (!tenureValidation.isValid()) {
      String msg =
          tenureValidation.validationResults().stream()
              .filter(r -> !r.isValid())
              .map(r -> "tenure[" + r.tenureIndex() + "]: " + r.errorMessage())
              .reduce((a, b) -> a + "; " + b)
              .orElse("");
      if (!tenureValidation.duplicateConflicts().isEmpty()) {
        String dups =
            tenureValidation.duplicateConflicts().stream()
                .map(d -> "Duplicate tenure at indices " + d.duplicateIndices() + ": " + d.reason())
                .reduce((a, b) -> a + "; " + b)
                .orElse("");
        msg = msg.isBlank() ? dups : msg + "; " + dups;
      }
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, msg);
    }

    // Step 13: build audit fields
    String auditUserId = loggedUserHelper.getAuditUserId();
    LocalDateTime now = LocalDateTime.now();

    // Step 14: allocate opening ID from sequence (committed immediately regardless of TX outcome)
    Long openingId =
        jdbcTemplate.queryForObject("SELECT nextval('silva.opening_id_seq')", Long.class);

    // Step 15: persist opening
    OpeningEntity opening =
        OpeningEntity.builder()
            .id(openingId)
            .isNew(true)
            .status("SUB")
            .category(dto.openingCategoryCode().trim())
            .geoDistrictNo(orgUnitNo)
            .adminDistrictNo(orgUnitNo)
            .orgUnitNo(orgUnitNo)
            .maxAllowPermntAccessPct(dto.maxAllowablePermAccessPerc())
            .licenseeOpeningId(
                dto.licenseeOpeningId() != null ? dto.licenseeOpeningId().trim() : null)
            .amendmentInd("N")
            .mapsheetGrid(mapsheet.grid())
            .mapsheetLetter(mapsheet.letter())
            .mapsheetSquare(mapsheet.square())
            .mapsheetQuad(mapsheet.quad())
            .mapsheetSubQuad(mapsheet.subQuad())
            .openingNumber(String.valueOf(openingNumber))
            .openingGrossArea(dto.openingGrossArea())
            .revisionCount(1)
            .entryUserId(auditUserId)
            .entryTimestamp(now)
            .updateUserId(auditUserId)
            .updateTimestamp(now)
            .build();
    openingRepository.save(opening);

    // Step 16: persist opening geometry
    OpeningGeometryEntity geometry =
        OpeningGeometryEntity.builder()
            .openingId(openingId)
            .isNew(true)
            .geometry(geometry3005)
            .featureArea(featureArea)
            .featurePerimeter(featurePerimeter)
            .featureClassSkey(FEATURE_CLASS_SKEY)
            .entryUserId(auditUserId)
            .entryTimestamp(now)
            .updateUserId(auditUserId)
            .updateTimestamp(now)
            .revisionCount(1)
            .build();
    openingGeometryRepository.save(geometry);

    // Step 17: associate each tenure, then reconcile CBOA-derived opening data.
    List<TenureRequestDto> tenures = dto.tenures();
    Map<Integer, CutBlockEntity> resolvedBlocks = tenureValidation.resolvedBlocks();
    List<CutBlockOpenAdminEntity> associatedTenures = new ArrayList<>();

    for (int i = 0; i < tenures.size(); i++) {
      TenureRequestDto tenure = tenures.get(i);
      CutBlockEntity block = resolvedBlocks.get(i);
      CutBlockOpenAdminEntity cboa =
          tenureAssociationService.associate(opening, tenure, block, auditUserId, now);
      associatedTenures.add(cboa);
    }
    tenureAssociationService.reconcile(opening, associatedTenures, null, auditUserId, now);
    for (CutBlockOpenAdminEntity cboa : associatedTenures) {
      tenureAssociationHistoryService.record("ASSOCIATED", openingId, cboa, auditUserId);
    }

    // Step 18: return the new opening ID
    return new CreateOpeningResponseDto(openingId);
  }

  private Geometry parseAllGeometries(JsonNode geoJson) {
    try {
      GeoJsonReader reader = new GeoJsonReader();
      JsonNode features = geoJson.get("features");
      if (features == null || features.isEmpty()) {
        throw new ResponseStatusException(
            HttpStatus.BAD_REQUEST, "Spatial file contains no features");
      }
      return reader.read(objectMapper.writeValueAsString(features.get(0).get("geometry")));
    } catch (ResponseStatusException e) {
      throw e;
    } catch (Exception e) {
      log.error("Failed to parse geometry from GeoJSON", e);
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Failed to parse spatial geometry");
    }
  }

  private Geometry reprojectTo3005(Geometry geometry4326) {
    try {
      return GeometryReprojectionUtils.to3005(geometry4326);
    } catch (RuntimeException e) {
      log.error("Failed to reproject geometry to EPSG:3005", e);
      throw new ResponseStatusException(
          HttpStatus.INTERNAL_SERVER_ERROR, "Failed to reproject geometry");
    }
  }
}
