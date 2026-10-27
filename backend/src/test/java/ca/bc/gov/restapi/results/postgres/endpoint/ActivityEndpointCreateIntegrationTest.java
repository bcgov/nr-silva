package ca.bc.gov.restapi.results.postgres.endpoint;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.mockito.AdditionalMatchers.aryEq;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.clearInvocations;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import ca.bc.gov.restapi.results.common.clamav.VirusScanService;
import ca.bc.gov.restapi.results.common.exception.VirusDetectedException;
import ca.bc.gov.restapi.results.extensions.AbstractTestContainerIntegrationTest;
import ca.bc.gov.restapi.results.extensions.WithMockJwt;
import ca.bc.gov.restapi.results.postgres.SilvaPostgresConstants;
import ca.bc.gov.restapi.results.postgres.service.SpatialFileService;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIfSystemProperty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.bean.override.convention.TestBean;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMultipartHttpServletRequestBuilder;
import org.springframework.transaction.annotation.Transactional;

@EnabledIfSystemProperty(named = "server.primary-db", matches = "postgres")
@DisplayName("Integration Test | Create activity endpoint")
@AutoConfigureMockMvc
@WithMockJwt(cognitoGroups = {"Submitter_00010003"})
@Transactional
class ActivityEndpointCreateIntegrationTest extends AbstractTestContainerIntegrationTest {

  private static final String CREATE_PATH = "/api/openings/{openingId}/activities";
  // Opening with a prime licence gross area of 79.4 ha
  private static final long OPENING_ID = 1004185L;
  private static final String SQUARE_GEOJSON =
      """
      {"type":"FeatureCollection","crs":{"properties":{"name":"EPSG:3005"}},
       "features":[{"type":"Feature","properties":{},"geometry":{"type":"Polygon",
       "coordinates":[[[850000,1500000],[850100,1500000],[850100,1500100],
                        [850000,1500100],[850000,1500000]]]}}]}
      """;

  @Autowired private MockMvc mockMvc;
  @Autowired private JdbcTemplate jdbcTemplate;
  @MockitoBean private VirusScanService virusScanService;
  @TestBean private SpatialFileService spatialFileService;

  static SpatialFileService spatialFileService() {
    return new SpatialFileService();
  }

  @BeforeEach
  void clearStartupScannerInteractions() {
    clearInvocations(virusScanService);
  }

  private String data(String base, String funding) {
    return """
        {"status":"PLANNED","baseCode":"%s","techniqueCode":"CG",
         "methodCode":"BAGS","objectiveCodes":["A","AC"],"fundingSourceCodes":%s,
         "projectNumber":"PRJ-1","licenseeActivityId":"LIC-1","comment":"a comment",
         "plannedDate":"%s","plannedArea":12.5,"plannedCost":1500}
        """
        .formatted(base, funding, LocalDate.now().plusDays(30));
  }

  private MockMultipartHttpServletRequestBuilder create(long openingId, String data) {
    return multipart(CREATE_PATH, openingId)
        .file(new MockMultipartFile("data", "", MediaType.APPLICATION_JSON_VALUE, data.getBytes()));
  }

  private MockMultipartFile spatialFile() {
    return new MockMultipartFile(
        "file", "activity.geojson", null, SQUARE_GEOJSON.getBytes(StandardCharsets.UTF_8));
  }

  private int activityCount() {
    return jdbcTemplate.queryForObject(
        "SELECT COUNT(*) FROM silva.activity_treatment_unit", Integer.class);
  }

  @Test
  @DisplayName("Creates an activity with its spatial file and returns the new activity")
  void create_withFile_returnsCreated() throws Exception {
    mockMvc
        .perform(
            create(OPENING_ID, data("HC", "[\"BCP\",\"BCT\"]"))
                .file(spatialFile())
                .with(csrf().asHeader()))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.activityId").isNumber())
        .andExpect(jsonPath("$.openingId").value(OPENING_ID))
        .andExpect(jsonPath("$.status").value("PLANNED"))
        .andExpect(jsonPath("$.baseCode").value("HC"));

    verify(virusScanService)
        .scanOrThrow(
            aryEq(SQUARE_GEOJSON.getBytes(StandardCharsets.UTF_8)), eq("activity.geojson"));
    Integer geometries =
        jdbcTemplate.queryForObject(
            "SELECT COUNT(*) FROM silva.activity_treatment_geometry g "
                + "JOIN silva.activity_treatment_unit a "
                + "ON a.activity_treatment_unit_id = g.activity_treatment_unit_id "
                + "WHERE a.opening_id = ? AND a.fia_project_id = 'PRJ-1'",
            Integer.class,
            OPENING_ID);
    assertThat(geometries).isEqualTo(1);
  }

  @Test
  @DisplayName("Creates an activity without a spatial file")
  void create_withoutFile_returnsCreated() throws Exception {
    mockMvc
        .perform(create(OPENING_ID, data("FE", "[\"BCP\"]")).with(csrf().asHeader()))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.baseCode").value("FE"));

    verify(virusScanService, org.mockito.Mockito.never()).scanOrThrow(any(), any());
  }

  @Test
  @DisplayName("Lists every violation together as field: message")
  void create_invalid_returnsEveryViolation() throws Exception {
    int before = activityCount();

    mockMvc
        .perform(create(OPENING_ID, data("PL", "[]")).with(csrf().asHeader()))
        .andExpect(status().isBadRequest())
        .andExpect(
            jsonPath("$.detail").value(containsString("fundingSourceCodes: must not be empty")))
        .andExpect(jsonPath("$.detail").value(containsString("baseCode: must be one of")));

    assertThat(activityCount()).isEqualTo(before);
  }

  @Test
  @DisplayName("Returns the opening gross area when the area is over the tolerance")
  void create_areaOverTolerance_reportsGrossArea() throws Exception {
    String data =
        data("HC", "[\"BCP\"]").replace("\"plannedArea\":12.5", "\"plannedArea\":200");

    mockMvc
        .perform(create(OPENING_ID, data).with(csrf().asHeader()))
        .andExpect(status().isBadRequest())
        .andExpect(
            jsonPath("$.detail")
                .value(
                    containsString(
                        "plannedArea: Planned area (200 ha) cannot exceed 99.25 ha "
                            + "(125% of the Opening's gross area of 79.4 ha)")));
  }

  @Test
  @DisplayName("Returns not found when the opening does not exist")
  void create_unknownOpening_returnsNotFound() throws Exception {
    mockMvc
        .perform(create(-1L, data("HC", "[\"BCP\"]")).with(csrf().asHeader()))
        .andExpect(status().isNotFound());
  }

  @Test
  @WithMockJwt(cognitoGroups = {"Planner_00099999"})
  @DisplayName("Returns forbidden for a user without a role for the opening's clients")
  void create_noRoleForTheOpening_returnsForbidden() throws Exception {
    int before = activityCount();

    mockMvc
        .perform(create(OPENING_ID, data("HC", "[\"BCP\"]")).with(csrf().asHeader()))
        .andExpect(status().isForbidden())
        .andExpect(jsonPath("$.detail").value("Not authorised for this opening"));

    assertThat(activityCount()).isEqualTo(before);
  }

  @Test
  @WithMockJwt(cognitoGroups = {"Planner_00099999"})
  @DisplayName("Returns forbidden before reporting problems with the request")
  void create_noRoleAndInvalidRequest_returnsForbidden() throws Exception {
    mockMvc
        .perform(create(OPENING_ID, data("PL", "[]")).with(csrf().asHeader()))
        .andExpect(status().isForbidden());
  }

  @Test
  @DisplayName("Returns bad request when the opening id in the path is not a number")
  void create_nonNumericOpeningId_returnsBadRequest() throws Exception {
    mockMvc
        .perform(
            multipart(CREATE_PATH, "abc")
                .file(
                    new MockMultipartFile(
                        "data",
                        "",
                        MediaType.APPLICATION_JSON_VALUE,
                        data("HC", "[\"BCP\"]").getBytes()))
                .with(csrf().asHeader()))
        .andExpect(status().isBadRequest());
  }

  @Test
  @DisplayName("Returns bad request when the data part is not valid JSON")
  void create_invalidJson_returnsBadRequest() throws Exception {
    mockMvc
        .perform(create(OPENING_ID, "{not json").with(csrf().asHeader()))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.detail").value(containsString("Invalid JSON in 'data'")));
  }

  @Test
  @DisplayName("Rejects an infected file with 422 and saves nothing")
  void create_infectedFile_returnsUnprocessable() throws Exception {
    int before = activityCount();
    doThrow(new VirusDetectedException("Virus detected: Eicar-Test-Signature"))
        .when(virusScanService)
        .scanOrThrow(any(), eq("activity.geojson"));

    mockMvc
        .perform(
            create(OPENING_ID, data("HC", "[\"BCP\"]"))
                .file(spatialFile())
                .with(csrf().asHeader()))
        .andExpect(status().isUnprocessableEntity());

    assertThat(activityCount()).isEqualTo(before);
  }

  @Test
  @DisplayName("Returns not found, without scanning, for an infected file on an unknown opening")
  void create_unknownOpeningAndInfectedFile_returnsNotFoundWithoutScanning() throws Exception {
    doThrow(new VirusDetectedException("Virus detected: Eicar-Test-Signature"))
        .when(virusScanService)
        .scanOrThrow(any(), eq("activity.geojson"));

    mockMvc
        .perform(
            create(-1L, data("HC", "[\"BCP\"]")).file(spatialFile()).with(csrf().asHeader()))
        .andExpect(status().isNotFound());

    verifyNoInteractions(virusScanService);
  }

  @Test
  @DisplayName("Returns not found, without reading the file, for an oversized file on an unknown opening")
  void create_unknownOpeningAndOversizedFile_returnsNotFound() throws Exception {
    MockMultipartFile oversized =
        new MockMultipartFile(
            "file",
            "big.geojson",
            null,
            new byte[(int) SilvaPostgresConstants.MAX_SPATIAL_FILE_SIZE_BYTES + 1]);

    mockMvc
        .perform(create(-1L, data("HC", "[\"BCP\"]")).file(oversized).with(csrf().asHeader()))
        .andExpect(status().isNotFound());

    verifyNoInteractions(virusScanService);
  }

  @Test
  @WithMockJwt(cognitoGroups = {"Planner_00099999"})
  @DisplayName("Returns forbidden, without scanning, for an infected file without access")
  void create_noRoleAndInfectedFile_returnsForbiddenWithoutScanning() throws Exception {
    doThrow(new VirusDetectedException("Virus detected: Eicar-Test-Signature"))
        .when(virusScanService)
        .scanOrThrow(any(), eq("activity.geojson"));

    mockMvc
        .perform(
            create(OPENING_ID, data("HC", "[\"BCP\"]"))
                .file(spatialFile())
                .with(csrf().asHeader()))
        .andExpect(status().isForbidden());

    verifyNoInteractions(virusScanService);
  }

  @Test
  @DisplayName("Reports an infected file before problems with the request")
  void create_infectedFileAndInvalidRequest_returnsUnprocessable() throws Exception {
    doThrow(new VirusDetectedException("Virus detected: Eicar-Test-Signature"))
        .when(virusScanService)
        .scanOrThrow(any(), eq("activity.geojson"));

    mockMvc
        .perform(create(OPENING_ID, data("PL", "[]")).file(spatialFile()).with(csrf().asHeader()))
        .andExpect(status().isUnprocessableEntity());
  }

  @Test
  @DisplayName("Rejects an invalid spatial file with 400 and saves nothing")
  void create_invalidSpatialFile_returnsBadRequestAndSavesNothing() throws Exception {
    int before = activityCount();
    MockMultipartFile bad =
        new MockMultipartFile(
            "file", "bad.geojson", null, "not a spatial file".getBytes(StandardCharsets.UTF_8));

    mockMvc
        .perform(
            create(OPENING_ID, data("HC", "[\"BCP\"]")).file(bad).with(csrf().asHeader()))
        .andExpect(status().isBadRequest());

    assertThat(activityCount()).isEqualTo(before);
  }
}
