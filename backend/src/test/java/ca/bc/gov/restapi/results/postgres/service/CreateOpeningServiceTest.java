package ca.bc.gov.restapi.results.postgres.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import ca.bc.gov.restapi.results.common.exception.NotFoundGenericException;
import ca.bc.gov.restapi.results.common.exception.OpeningCategoryNotFoundException;
import ca.bc.gov.restapi.results.common.projection.CutBlockValidationProjection;
import ca.bc.gov.restapi.results.common.security.LoggedUserHelper;
import ca.bc.gov.restapi.results.common.service.OpenMapsService;
import ca.bc.gov.restapi.results.postgres.dto.CreateOpeningRequestDto;
import ca.bc.gov.restapi.results.postgres.dto.CreateOpeningResponseDto;
import ca.bc.gov.restapi.results.postgres.dto.ExtractedGeoDataDto;
import ca.bc.gov.restapi.results.postgres.dto.MapsheetDto;
import ca.bc.gov.restapi.results.postgres.dto.TenureRequestDto;
import ca.bc.gov.restapi.results.postgres.entity.OrgUnitEntity;
import ca.bc.gov.restapi.results.postgres.entity.code.OpenCategoryCodePostgresEntity;
import ca.bc.gov.restapi.results.postgres.entity.opening.OpeningEntity;
import ca.bc.gov.restapi.results.postgres.repository.CutBlockValidationRepository;
import ca.bc.gov.restapi.results.postgres.repository.OpenCategoryCodePostgresRepository;
import ca.bc.gov.restapi.results.postgres.repository.OpeningGeometryPostgresRepository;
import ca.bc.gov.restapi.results.postgres.repository.OpeningPostgresRepository;
import ca.bc.gov.restapi.results.postgres.repository.OrgUnitPostgresRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIfSystemProperty;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.server.ResponseStatusException;

@EnabledIfSystemProperty(named = "server.primary-db", matches = "postgres")
@ExtendWith(MockitoExtension.class)
@DisplayName("Unit Test | CreateOpeningService")
class CreateOpeningServiceTest {

  @Mock private OpeningSpatialFileService openingSpatialFileService;
  @Mock private OpenMapsService openMapsService;
  @Mock private OpeningPostgresRepository openingRepository;
  @Mock private OpeningGeometryPostgresRepository openingGeometryRepository;
  @Mock private CutBlockValidationRepository cutBlockValidationRepository;
  @Mock private OpenCategoryCodePostgresRepository openCategoryCodeRepository;
  @Mock private OrgUnitPostgresRepository orgUnitRepository;
  @Mock private TenureValidationService tenureValidationService;
  @Mock private LoggedUserHelper loggedUserHelper;
  @Mock private JdbcTemplate jdbcTemplate;
  @Spy private ObjectMapper objectMapper = new ObjectMapper();

  private CreateOpeningService service;

  /** Simple valid GeoJSON FeatureCollection with one polygon feature. */
  private static final String VALID_GEOJSON =
      """
      {
        "type": "FeatureCollection",
        "features": [{
          "type": "Feature",
          "geometry": {
            "type": "Polygon",
            "coordinates": [[
              [-125.0, 50.0], [-124.0, 50.0],
              [-124.0, 51.0], [-125.0, 51.0],
              [-125.0, 50.0]
            ]]
          },
          "properties": {}
        }]
      }
      """;

  private static final MapsheetDto FAKE_MAPSHEET = new MapsheetDto("092", "L", "057", "0", "0");

  @BeforeEach
  void setUp() {
    service =
        new CreateOpeningService(
            openingSpatialFileService,
            openMapsService,
            openingRepository,
            openingGeometryRepository,
            cutBlockValidationRepository,
            openCategoryCodeRepository,
            orgUnitRepository,
            tenureValidationService,
            loggedUserHelper,
            jdbcTemplate,
            objectMapper);
  }

  private ExtractedGeoDataDto buildGeoData() throws Exception {
    JsonNode json = objectMapper.readTree(VALID_GEOJSON);
    return new ExtractedGeoDataDto(new BigDecimal("100.0000"), json);
  }

  private CreateOpeningRequestDto buildDto(List<TenureRequestDto> tenures) {
    return new CreateOpeningRequestDto(
        new BigDecimal("100.0000"),
        new BigDecimal("10.0"),
        "12345678",
        "01",
        "DAS",
        "FTML",
        null,
        tenures);
  }

  private TenureRequestDto primaryTenure() {
    return new TenureRequestDto("TFL001", "CP01", "CB001", true);
  }

  private TenureRequestDto nonPrimaryTenure() {
    return new TenureRequestDto("TFL002", null, "CB002", false);
  }

  /** Set up mocks for steps 1–5 (spatial file + mapsheet). */
  private void mockGeometryAndMapsheetSteps() throws Exception {
    ExtractedGeoDataDto geoData = buildGeoData(); // build before entering when() context
    when(openingSpatialFileService.processOpeningSpatialFile(anyString(), any()))
        .thenReturn(geoData);
    when(openMapsService.getMapsheetForPoint(any(double.class), any(double.class)))
        .thenReturn(FAKE_MAPSHEET);
    when(openingRepository.findNextOpeningNumber(eq("092"), eq("L"), eq("057"), eq("0"), eq("0")))
        .thenReturn(1);
  }

  /** Additionally stub detectDuplicates for tests that reach step 11. */
  private void mockNoDuplicates() {
    when(tenureValidationService.detectDuplicates(any()))
        .thenReturn(java.util.Collections.emptyList());
  }

  @Test
  @DisplayName("Happy path should create opening and return response with new opening ID")
  void createOpening_happyPath_shouldSucceed() throws Exception {
    mockGeometryAndMapsheetSteps();
    mockNoDuplicates();

    // Category + org unit
    when(openCategoryCodeRepository.findById("FTML"))
        .thenReturn(Optional.of(mock(OpenCategoryCodePostgresEntity.class)));
    OrgUnitEntity orgUnit = OrgUnitEntity.builder().orgUnitNo(99L).orgUnitCode("DAS").build();
    when(orgUnitRepository.findByOrgUnitCode("DAS")).thenReturn(Optional.of(orgUnit));

    // Auth
    when(loggedUserHelper.hasRoleMatching(any())).thenReturn(true);

    // Cut block validation
    CutBlockValidationProjection block =
        new CutBlockValidationProjection() {
          public Long getCbSkey() {
            return 42L;
          }

          public String getTimberMark() {
            return "TM001";
          }
        };
    when(cutBlockValidationRepository.findCutBlockByTenureAndClient(
            eq("TFL001"), eq("CP01"), eq("CB001"), eq("12345678"), eq("01")))
        .thenReturn(Optional.of(block));

    // Audit
    when(loggedUserHelper.getAuditUserId()).thenReturn("testuser");
    when(jdbcTemplate.queryForObject(anyString(), eq(Long.class))).thenReturn(10001L);
    when(openingRepository.save(any(OpeningEntity.class))).thenAnswer(i -> i.getArgument(0));

    CreateOpeningResponseDto response =
        service.createOpening(buildDto(List.of(primaryTenure())), "test.zip", new byte[0]);

    assertThat(response).isNotNull();
    assertThat(response.openingId()).isEqualTo(10001L);
  }

  @Test
  @DisplayName("No primary tenure should throw 400 BAD_REQUEST")
  void createOpening_noPrimaryTenure_shouldThrow400() throws Exception {
    mockGeometryAndMapsheetSteps();
    when(openCategoryCodeRepository.findById("FTML"))
        .thenReturn(Optional.of(mock(OpenCategoryCodePostgresEntity.class)));
    OrgUnitEntity orgUnit = OrgUnitEntity.builder().orgUnitNo(99L).orgUnitCode("DAS").build();
    when(orgUnitRepository.findByOrgUnitCode("DAS")).thenReturn(Optional.of(orgUnit));
    when(loggedUserHelper.hasRoleMatching(any())).thenReturn(true);

    assertThatThrownBy(
            () ->
                service.createOpening(
                    buildDto(List.of(nonPrimaryTenure())), "test.zip", new byte[0]))
        .isInstanceOf(ResponseStatusException.class)
        .satisfies(
            ex ->
                assertThat(((ResponseStatusException) ex).getStatusCode())
                    .isEqualTo(HttpStatus.BAD_REQUEST));
  }

  @Test
  @DisplayName("Multiple primary tenures should throw 400 BAD_REQUEST")
  void createOpening_multiplePrimaryTenures_shouldThrow400() throws Exception {
    mockGeometryAndMapsheetSteps();
    when(openCategoryCodeRepository.findById("FTML"))
        .thenReturn(Optional.of(mock(OpenCategoryCodePostgresEntity.class)));
    OrgUnitEntity orgUnit = OrgUnitEntity.builder().orgUnitNo(99L).orgUnitCode("DAS").build();
    when(orgUnitRepository.findByOrgUnitCode("DAS")).thenReturn(Optional.of(orgUnit));
    when(loggedUserHelper.hasRoleMatching(any())).thenReturn(true);

    TenureRequestDto primary2 = new TenureRequestDto("TFL002", null, "CB002", true);

    assertThatThrownBy(
            () ->
                service.createOpening(
                    buildDto(List.of(primaryTenure(), primary2)), "test.zip", new byte[0]))
        .isInstanceOf(ResponseStatusException.class)
        .satisfies(
            ex ->
                assertThat(((ResponseStatusException) ex).getStatusCode())
                    .isEqualTo(HttpStatus.BAD_REQUEST));
  }

  @Test
  @DisplayName("Unauthorized client number should throw 403 FORBIDDEN")
  void createOpening_unauthorizedClient_shouldThrow403() throws Exception {
    mockGeometryAndMapsheetSteps();
    when(openCategoryCodeRepository.findById("FTML"))
        .thenReturn(Optional.of(mock(OpenCategoryCodePostgresEntity.class)));
    OrgUnitEntity orgUnit = OrgUnitEntity.builder().orgUnitNo(99L).orgUnitCode("DAS").build();
    when(orgUnitRepository.findByOrgUnitCode("DAS")).thenReturn(Optional.of(orgUnit));
    when(loggedUserHelper.hasRoleMatching(any())).thenReturn(false);

    assertThatThrownBy(
            () ->
                service.createOpening(buildDto(List.of(primaryTenure())), "test.zip", new byte[0]))
        .isInstanceOf(ResponseStatusException.class)
        .satisfies(
            ex ->
                assertThat(((ResponseStatusException) ex).getStatusCode())
                    .isEqualTo(HttpStatus.FORBIDDEN));
  }

  @Test
  @DisplayName("Category not found should throw OpeningCategoryNotFoundException")
  void createOpening_categoryNotFound_shouldThrow() throws Exception {
    mockGeometryAndMapsheetSteps();
    when(openCategoryCodeRepository.findById("FTML")).thenReturn(Optional.empty());

    assertThatThrownBy(
            () ->
                service.createOpening(buildDto(List.of(primaryTenure())), "test.zip", new byte[0]))
        .isInstanceOf(OpeningCategoryNotFoundException.class);
  }

  @Test
  @DisplayName("OrgUnit not found should throw NotFoundGenericException")
  void createOpening_orgUnitNotFound_shouldThrow() throws Exception {
    mockGeometryAndMapsheetSteps();
    when(openCategoryCodeRepository.findById("FTML"))
        .thenReturn(Optional.of(mock(OpenCategoryCodePostgresEntity.class)));
    when(orgUnitRepository.findByOrgUnitCode("DAS")).thenReturn(Optional.empty());

    assertThatThrownBy(
            () ->
                service.createOpening(buildDto(List.of(primaryTenure())), "test.zip", new byte[0]))
        .isInstanceOf(NotFoundGenericException.class);
  }

  @Test
  @DisplayName("Cut block not found for a tenure should throw 400 BAD_REQUEST")
  void createOpening_cutBlockNotFound_shouldThrow400() throws Exception {
    mockGeometryAndMapsheetSteps();
    mockNoDuplicates();
    when(openCategoryCodeRepository.findById("FTML"))
        .thenReturn(Optional.of(mock(OpenCategoryCodePostgresEntity.class)));
    OrgUnitEntity orgUnit = OrgUnitEntity.builder().orgUnitNo(99L).orgUnitCode("DAS").build();
    when(orgUnitRepository.findByOrgUnitCode("DAS")).thenReturn(Optional.of(orgUnit));
    when(loggedUserHelper.hasRoleMatching(any())).thenReturn(true);
    when(cutBlockValidationRepository.findCutBlockByTenureAndClient(
            any(), any(), any(), any(), any()))
        .thenReturn(Optional.empty());

    assertThatThrownBy(
            () ->
                service.createOpening(buildDto(List.of(primaryTenure())), "test.zip", new byte[0]))
        .isInstanceOf(ResponseStatusException.class)
        .satisfies(
            ex ->
                assertThat(((ResponseStatusException) ex).getStatusCode())
                    .isEqualTo(HttpStatus.BAD_REQUEST));
  }

  @Test
  @DisplayName("Happy path with non-null licenseeOpeningId should set it on the opening entity")
  void createOpening_withLicenseeOpeningId_shouldSucceed() throws Exception {
    mockGeometryAndMapsheetSteps();
    mockNoDuplicates();
    when(openCategoryCodeRepository.findById("FTML"))
        .thenReturn(Optional.of(mock(OpenCategoryCodePostgresEntity.class)));
    OrgUnitEntity orgUnit = OrgUnitEntity.builder().orgUnitNo(99L).orgUnitCode("DAS").build();
    when(orgUnitRepository.findByOrgUnitCode("DAS")).thenReturn(Optional.of(orgUnit));
    when(loggedUserHelper.hasRoleMatching(any())).thenReturn(true);
    CutBlockValidationProjection block =
        new CutBlockValidationProjection() {
          public Long getCbSkey() {
            return 42L;
          }

          public String getTimberMark() {
            return "TM001";
          }
        };
    when(cutBlockValidationRepository.findCutBlockByTenureAndClient(
            any(), any(), any(), any(), any()))
        .thenReturn(Optional.of(block));
    when(loggedUserHelper.getAuditUserId()).thenReturn("testuser");
    when(jdbcTemplate.queryForObject(anyString(), eq(Long.class))).thenReturn(20002L);
    when(openingRepository.save(any(OpeningEntity.class))).thenAnswer(i -> i.getArgument(0));

    CreateOpeningRequestDto dto =
        new CreateOpeningRequestDto(
            new BigDecimal("100.0000"),
            new BigDecimal("10.0"),
            "12345678",
            "01",
            "DAS",
            "FTML",
            " LIC-001 ",
            List.of(primaryTenure()));

    CreateOpeningResponseDto response = service.createOpening(dto, "file.shp", new byte[0]);

    assertThat(response.openingId()).isEqualTo(20002L);
  }

  @Test
  @DisplayName("Spatial file processing failure should propagate exception")
  void createOpening_spatialFileFailure_shouldPropagate() {
    when(openingSpatialFileService.processOpeningSpatialFile(anyString(), any()))
        .thenThrow(new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Infected file"));

    assertThatThrownBy(
            () -> service.createOpening(buildDto(List.of(primaryTenure())), "bad.zip", new byte[0]))
        .isInstanceOf(ResponseStatusException.class)
        .satisfies(
            ex ->
                assertThat(((ResponseStatusException) ex).getStatusCode())
                    .isEqualTo(HttpStatus.UNPROCESSABLE_ENTITY));
  }
}
