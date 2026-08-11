package ca.bc.gov.restapi.results.postgres.endpoint;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import ca.bc.gov.restapi.results.common.clamav.VirusScanService;
import ca.bc.gov.restapi.results.common.exception.VirusDetectedException;
import ca.bc.gov.restapi.results.extensions.AbstractTestContainerIntegrationTest;
import ca.bc.gov.restapi.results.extensions.WithMockJwt;
import ca.bc.gov.restapi.results.postgres.config.OpeningEndpointTestConfig;
import ca.bc.gov.restapi.results.postgres.dto.CreateOpeningResponseDto;
import ca.bc.gov.restapi.results.postgres.service.CreateOpeningService;
import java.nio.charset.StandardCharsets;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIfSystemProperty;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.server.ResponseStatusException;

@EnabledIfSystemProperty(named = "server.primary-db", matches = "postgres")
@DisplayName("Integration Test | Opening Create Endpoint")
@AutoConfigureMockMvc
@WithMockJwt
@Import(OpeningEndpointTestConfig.class)
class OpeningEndpointCreateIntegrationTest extends AbstractTestContainerIntegrationTest {

  @Autowired private MockMvc mockMvc;

  @Autowired private CreateOpeningService createOpeningService;

  @Autowired private VirusScanService virusScanService;

  private static final String VALID_GEOJSON =
      "{\"type\":\"Feature\",\"geometry\":{\"type\":\"Polygon\",\"coordinates\":[[[0,0],[1,0],[1,1],[0,1],[0,0]]]},\"properties\":{}}";

  private static final String VALID_DATA_JSON =
      "{\"openingGrossArea\": 100.5, \"maxAllowablePermAccessPerc\": 25.5, "
          + "\"clientNumber\": \"00001012\", \"clientLocationCode\": \"AB\", "
          + "\"orgUnitCode\": \"DCC\", \"openingCategoryCode\": \"FTML\", "
          + "\"tenures\": [{\"fileId\": \"F001\", \"cutBlock\": \"A1\", \"isPrimary\": true}]}";

  @BeforeEach
  void resetMocks() {
    Mockito.reset(createOpeningService);
    Mockito.reset(virusScanService);
  }

  // ============ HAPPY PATH ============

  @Test
  @DisplayName("Should create opening successfully with valid data and file")
  void createOpening_withValidDataAndFile_shouldReturn201() throws Exception {
    CreateOpeningResponseDto response = new CreateOpeningResponseDto(123L);
    when(createOpeningService.createOpening(any(), anyString(), any(byte[].class)))
        .thenReturn(response);

    MockMultipartFile dataFile =
        new MockMultipartFile(
            "data",
            "data.json",
            MediaType.APPLICATION_JSON_VALUE,
            VALID_DATA_JSON.getBytes(StandardCharsets.UTF_8));

    MockMultipartFile geoFile =
        new MockMultipartFile(
            "file",
            "opening.geojson",
            MediaType.APPLICATION_JSON_VALUE,
            VALID_GEOJSON.getBytes(StandardCharsets.UTF_8));

    mockMvc
        .perform(
            multipart("/api/openings")
                .file(dataFile)
                .file(geoFile)
                .with(csrf().asHeader())
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.openingId").value(123));
  }

  // ============ MISSING/INVALID PARTS ============

  @Test
  @DisplayName("Should return bad request when data part is missing")
  void createOpening_withoutDataPart_shouldReturn400() throws Exception {
    MockMultipartFile geoFile =
        new MockMultipartFile(
            "file",
            "opening.geojson",
            MediaType.APPLICATION_JSON_VALUE,
            VALID_GEOJSON.getBytes(StandardCharsets.UTF_8));

    mockMvc
        .perform(
            multipart("/api/openings")
                .file(geoFile)
                .with(csrf().asHeader())
                .contentType(MediaType.MULTIPART_FORM_DATA))
        .andExpect(status().isBadRequest());
  }

  @Test
  @DisplayName("Should return bad request when file part is missing")
  void createOpening_withoutFilePart_shouldReturn400() throws Exception {
    MockMultipartFile dataFile =
        new MockMultipartFile(
            "data",
            "data.json",
            MediaType.APPLICATION_JSON_VALUE,
            VALID_DATA_JSON.getBytes(StandardCharsets.UTF_8));

    mockMvc
        .perform(
            multipart("/api/openings")
                .file(dataFile)
                .with(csrf().asHeader())
                .contentType(MediaType.MULTIPART_FORM_DATA))
        .andExpect(status().isBadRequest());
  }

  // ============ INVALID JSON ============

  @Test
  @DisplayName("Should return bad request when data JSON is invalid")
  void createOpening_withInvalidJson_shouldReturn400() throws Exception {
    String invalidJson = "{\"invalidField\": \"value\"";

    MockMultipartFile dataFile =
        new MockMultipartFile(
            "data",
            "data.json",
            MediaType.APPLICATION_JSON_VALUE,
            invalidJson.getBytes(StandardCharsets.UTF_8));

    MockMultipartFile geoFile =
        new MockMultipartFile(
            "file",
            "opening.geojson",
            MediaType.APPLICATION_JSON_VALUE,
            VALID_GEOJSON.getBytes(StandardCharsets.UTF_8));

    mockMvc
        .perform(
            multipart("/api/openings")
                .file(dataFile)
                .file(geoFile)
                .with(csrf().asHeader())
                .contentType(MediaType.MULTIPART_FORM_DATA))
        .andExpect(status().isBadRequest());
  }

  // ============ VALIDATION FAILURES ============

  @Test
  @DisplayName("Should return bad request when required field openingGrossArea is missing")
  void createOpening_withMissingOpeningGrossArea_shouldReturn400() throws Exception {
    String invalidData =
        "{\"maxAllowablePermAccessPerc\": 25.5, "
            + "\"clientNumber\": \"00001012\", \"clientLocationCode\": \"AB\", "
            + "\"orgUnitCode\": \"DCC\", \"openingCategoryCode\": \"FTML\", "
            + "\"tenures\": [{\"fileId\": \"F001\", \"cutBlock\": \"A1\", \"isPrimary\": true}]}";

    MockMultipartFile dataFile =
        new MockMultipartFile(
            "data",
            "data.json",
            MediaType.APPLICATION_JSON_VALUE,
            invalidData.getBytes(StandardCharsets.UTF_8));

    MockMultipartFile geoFile =
        new MockMultipartFile(
            "file",
            "opening.geojson",
            MediaType.APPLICATION_JSON_VALUE,
            VALID_GEOJSON.getBytes(StandardCharsets.UTF_8));

    mockMvc
        .perform(
            multipart("/api/openings")
                .file(dataFile)
                .file(geoFile)
                .with(csrf().asHeader())
                .contentType(MediaType.MULTIPART_FORM_DATA))
        .andExpect(status().isBadRequest());
  }

  @Test
  @DisplayName("Should return bad request when tenure list is empty")
  void createOpening_withEmptyTenureList_shouldReturn400() throws Exception {
    String invalidData =
        "{\"openingGrossArea\": 100.5, \"maxAllowablePermAccessPerc\": 25.5, "
            + "\"clientNumber\": \"00001012\", \"clientLocationCode\": \"AB\", "
            + "\"orgUnitCode\": \"DCC\", \"openingCategoryCode\": \"FTML\", "
            + "\"tenures\": []}";

    MockMultipartFile dataFile =
        new MockMultipartFile(
            "data",
            "data.json",
            MediaType.APPLICATION_JSON_VALUE,
            invalidData.getBytes(StandardCharsets.UTF_8));

    MockMultipartFile geoFile =
        new MockMultipartFile(
            "file",
            "opening.geojson",
            MediaType.APPLICATION_JSON_VALUE,
            VALID_GEOJSON.getBytes(StandardCharsets.UTF_8));

    mockMvc
        .perform(
            multipart("/api/openings")
                .file(dataFile)
                .file(geoFile)
                .with(csrf().asHeader())
                .contentType(MediaType.MULTIPART_FORM_DATA))
        .andExpect(status().isBadRequest());
  }

  @Test
  @DisplayName(
      "Should return bad request when tenures contain duplicate fileId + cuttingPermit + cutBlock")
  void createOpening_withDuplicateTenures_shouldReturn400() throws Exception {
    String duplicateTenuresData =
        "{\"openingGrossArea\": 100.5, \"maxAllowablePermAccessPerc\": 25.5, \"clientNumber\":"
            + " \"00001012\", \"orgUnitCode\": \"DCC\", \"openingCategoryCode\": \"FTML\","
            + " \"tenures\": [{\"fileId\": \"F001\", \"cuttingPermit\": \"CP1\", \"cutBlock\":"
            + " \"A1\", \"isPrimary\": true},{\"fileId\": \"F001\", \"cuttingPermit\": \"CP1\","
            + " \"cutBlock\": \"A1\", \"isPrimary\": false}]}";

    MockMultipartFile dataFile =
        new MockMultipartFile(
            "data",
            "data.json",
            MediaType.APPLICATION_JSON_VALUE,
            duplicateTenuresData.getBytes(StandardCharsets.UTF_8));

    MockMultipartFile geoFile =
        new MockMultipartFile(
            "file",
            "opening.geojson",
            MediaType.APPLICATION_JSON_VALUE,
            VALID_GEOJSON.getBytes(StandardCharsets.UTF_8));

    // Mock the service to throw a duplicate tenure exception
    when(createOpeningService.createOpening(any(), anyString(), any(byte[].class)))
        .thenThrow(
            new ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                "Duplicate tenure at indices [0, 1]: fileId=F001, cuttingPermit=CP1, cutBlock=A1"));

    mockMvc
        .perform(
            multipart("/api/openings")
                .file(dataFile)
                .file(geoFile)
                .with(csrf().asHeader())
                .contentType(MediaType.MULTIPART_FORM_DATA))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.detail").value(Matchers.containsString("Duplicate tenure")));
  }

  // ============ FILE VALIDATION ============

  @Test
  @DisplayName("Should return bad request when file size exceeds limit (25MB)")
  void createOpening_withOversizedFile_shouldReturn400() throws Exception {
    // Create a file that exceeds 25MB limit
    byte[] largeContent = new byte[26 * 1024 * 1024 + 1];
    MockMultipartFile dataFile =
        new MockMultipartFile(
            "data",
            "data.json",
            MediaType.APPLICATION_JSON_VALUE,
            VALID_DATA_JSON.getBytes(StandardCharsets.UTF_8));

    MockMultipartFile oversizedFile =
        new MockMultipartFile(
            "file", "large.geojson", MediaType.APPLICATION_JSON_VALUE, largeContent);

    mockMvc
        .perform(
            multipart("/api/openings")
                .file(dataFile)
                .file(oversizedFile)
                .with(csrf().asHeader())
                .contentType(MediaType.MULTIPART_FORM_DATA))
        .andExpect(status().isBadRequest());
  }

  @Test
  @DisplayName("Should return 422 when virus is detected in file")
  void createOpening_withVirusInFile_shouldReturn422() throws Exception {
    doThrow(new VirusDetectedException("Virus detected: Eicar-Test-Signature"))
        .when(virusScanService)
        .scanOrThrow(any(), any());

    MockMultipartFile dataFile =
        new MockMultipartFile(
            "data",
            "data.json",
            MediaType.APPLICATION_JSON_VALUE,
            VALID_DATA_JSON.getBytes(StandardCharsets.UTF_8));

    MockMultipartFile maliciousFile =
        new MockMultipartFile(
            "file",
            "evil.geojson",
            MediaType.APPLICATION_JSON_VALUE,
            VALID_GEOJSON.getBytes(StandardCharsets.UTF_8));

    mockMvc
        .perform(
            multipart("/api/openings")
                .file(dataFile)
                .file(maliciousFile)
                .with(csrf().asHeader())
                .contentType(MediaType.MULTIPART_FORM_DATA))
        .andExpect(status().isUnprocessableEntity());
  }

  @Test
  @DisplayName("Should return 422 when virus scanner is unavailable (fail-closed)")
  void createOpening_withScannerUnavailable_shouldReturn422() throws Exception {
    doThrow(new VirusDetectedException("Virus scan unavailable: connection refused"))
        .when(virusScanService)
        .scanOrThrow(any(), any());

    MockMultipartFile dataFile =
        new MockMultipartFile(
            "data",
            "data.json",
            MediaType.APPLICATION_JSON_VALUE,
            VALID_DATA_JSON.getBytes(StandardCharsets.UTF_8));

    MockMultipartFile geoFile =
        new MockMultipartFile(
            "file",
            "opening.geojson",
            MediaType.APPLICATION_JSON_VALUE,
            VALID_GEOJSON.getBytes(StandardCharsets.UTF_8));

    mockMvc
        .perform(
            multipart("/api/openings")
                .file(dataFile)
                .file(geoFile)
                .with(csrf().asHeader())
                .contentType(MediaType.MULTIPART_FORM_DATA))
        .andExpect(status().isUnprocessableEntity());
  }

  // ============ SECURITY ============

  @Test
  @DisplayName("Should be forbidden without CSRF token")
  void createOpening_withoutCsrf_shouldReturn403() throws Exception {
    MockMultipartFile dataFile =
        new MockMultipartFile(
            "data",
            "data.json",
            MediaType.APPLICATION_JSON_VALUE,
            VALID_DATA_JSON.getBytes(StandardCharsets.UTF_8));

    MockMultipartFile geoFile =
        new MockMultipartFile(
            "file",
            "opening.geojson",
            MediaType.APPLICATION_JSON_VALUE,
            VALID_GEOJSON.getBytes(StandardCharsets.UTF_8));

    mockMvc
        .perform(
            multipart("/api/openings")
                .file(dataFile)
                .file(geoFile)
                .contentType(MediaType.MULTIPART_FORM_DATA))
        .andExpect(status().isForbidden());
  }

  // ============ SERVICE ERRORS ============

  @Test
  @DisplayName("Should return 500 when service encounters an unexpected error")
  void createOpening_whenServiceThrowsError_shouldReturn500() throws Exception {
    when(createOpeningService.createOpening(any(), anyString(), any(byte[].class)))
        .thenThrow(
            new ResponseStatusException(
                org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR, "Database error"));

    MockMultipartFile dataFile =
        new MockMultipartFile(
            "data",
            "data.json",
            MediaType.APPLICATION_JSON_VALUE,
            VALID_DATA_JSON.getBytes(StandardCharsets.UTF_8));

    MockMultipartFile geoFile =
        new MockMultipartFile(
            "file",
            "opening.geojson",
            MediaType.APPLICATION_JSON_VALUE,
            VALID_GEOJSON.getBytes(StandardCharsets.UTF_8));

    mockMvc
        .perform(
            multipart("/api/openings")
                .file(dataFile)
                .file(geoFile)
                .with(csrf().asHeader())
                .contentType(MediaType.MULTIPART_FORM_DATA))
        .andExpect(status().isInternalServerError());
  }
}
