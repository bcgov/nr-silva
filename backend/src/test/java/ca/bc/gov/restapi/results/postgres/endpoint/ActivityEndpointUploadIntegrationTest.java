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
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.anonymous;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import ca.bc.gov.restapi.results.common.clamav.VirusScanService;
import ca.bc.gov.restapi.results.common.exception.VirusDetectedException;
import ca.bc.gov.restapi.results.extensions.AbstractTestContainerIntegrationTest;
import ca.bc.gov.restapi.results.extensions.WithMockJwt;
import ca.bc.gov.restapi.results.postgres.SilvaPostgresConstants;
import ca.bc.gov.restapi.results.postgres.service.SpatialFileService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.nio.charset.StandardCharsets;
import java.util.stream.Stream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIfSystemProperty;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.bean.override.convention.TestBean;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

/** Exercises activity uploads through HTTP using the real shared spatial processor. */
@EnabledIfSystemProperty(named = "server.primary-db", matches = "postgres")
@DisplayName("Integration Test | Activity Upload Endpoint")
@AutoConfigureMockMvc
@WithMockJwt
class ActivityEndpointUploadIntegrationTest extends AbstractTestContainerIntegrationTest {

  private static final String UPLOAD_PATH = "/api/activities/spatial/upload";
  private static final String GEOJSON =
      """
      {"type":"FeatureCollection","crs":{"properties":{"name":"EPSG:3005"}},
       "features":[{"type":"Feature","properties":{},"geometry":{"type":"Polygon",
       "coordinates":[[[850000,1500000],[850100,1500000],[850100,1500100],
                        [850000,1500100],[850000,1500000]]]}}]}
      """;
  private static final String GML =
      """
      <gml:Polygon xmlns:gml="http://www.opengis.net/gml" srsName="EPSG:3005">
        <gml:outerBoundaryIs><gml:LinearRing><gml:coordinates>
          850000,1500000 850100,1500000 850100,1500100 850000,1500100 850000,1500000
        </gml:coordinates></gml:LinearRing></gml:outerBoundaryIs>
      </gml:Polygon>
      """;

  @Autowired private MockMvc mockMvc;
  @MockitoBean private VirusScanService virusScanService;
  @TestBean private SpatialFileService spatialFileService;
  @Autowired private ObjectMapper mapper;

  static SpatialFileService spatialFileService() {
    return new SpatialFileService();
  }

  @BeforeEach
  void clearStartupScannerInteractions() {
    clearInvocations(virusScanService);
  }

  @ParameterizedTest
  @MethodSource("validFiles")
  void processesValidFilesAndReturnsGeometryIn4326(String fileName, String data) throws Exception {
    MockMultipartFile file = file(fileName, data);

    MvcResult response =
        mockMvc
            .perform(upload(file).with(csrf().asHeader()))
            .andExpect(status().isAccepted())
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.geometryArea").value(1.0))
            .andExpect(jsonPath("$.geoJson.type").value("FeatureCollection"))
            .andExpect(jsonPath("$.geoJson.features.length()").value(1))
            .andExpect(jsonPath("$.geoJson.features[0].geometry.type").value("Polygon"))
            .andReturn();

    JsonNode result = mapper.readTree(response.getResponse().getContentAsString());
    JsonNode coordinate = result.at("/geoJson/features/0/geometry/coordinates/0/0");
    assertThat(coordinate.get(0).asDouble()).isBetween(-180.0, 180.0);
    assertThat(coordinate.get(1).asDouble()).isBetween(-90.0, 90.0);
    verify(virusScanService).scanOrThrow(aryEq(file.getBytes()), eq(fileName));
  }

  private static Stream<Arguments> validFiles() {
    return Stream.of(
        Arguments.of("activity.geojson", GEOJSON), Arguments.of("activity.gml", GML));
  }

  @ParameterizedTest
  @MethodSource("invalidFiles")
  void returnsProcessingErrorsAsProblemDetails(String fileName, String data, String detail)
      throws Exception {
    mockMvc
        .perform(upload(file(fileName, data)).with(csrf().asHeader()))
        .andExpect(status().isBadRequest())
        .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_PROBLEM_JSON))
        .andExpect(jsonPath("$.status").value(400))
        .andExpect(jsonPath("$.detail").value(containsString(detail)));
  }

  private static Stream<Arguments> invalidFiles() {
    return Stream.of(
        Arguments.of("activity.txt", GEOJSON, "Unsupported file type"),
        Arguments.of("activity.geojson", "not json", "Invalid GeoJSON file"),
        Arguments.of("activity.gml", "<broken", "Failed to process GML file"),
        Arguments.of(
            "activity.geojson", GEOJSON.replace("EPSG:3005", "EPSG:3857"), "GeoJSON CRS must"),
        Arguments.of(
            "activity.geojson",
            GEOJSON.replace("850000", "-850000").replace("850100", "-850100"),
            "not fully within the Province of BC boundary"));
  }

  @Test
  void rejectsMissingFile() throws Exception {
    mockMvc
        .perform(multipart(UPLOAD_PATH).with(csrf().asHeader()))
        .andExpect(status().isBadRequest());
    verifyNoInteractions(virusScanService);
  }

  @Test
  void rejectsEmptyFileBeforeScanning() throws Exception {
    mockMvc
        .perform(upload(file("activity.geojson", "")).with(csrf().asHeader()))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.detail").value("Uploaded file is null or empty"));
    verifyNoInteractions(virusScanService);
  }

  @Test
  void rejectsOversizedFileBeforeScanning() throws Exception {
    MockMultipartFile file =
        new MockMultipartFile(
            "file",
            "activity.geojson",
            MediaType.APPLICATION_JSON_VALUE,
            new byte[SilvaPostgresConstants.MAX_SPATIAL_FILE_SIZE_BYTES + 1]);

    mockMvc
        .perform(upload(file).with(csrf().asHeader()))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.detail").value("File exceeds 25MB size limit"));
    verifyNoInteractions(virusScanService);
  }

  @ParameterizedTest
  @ValueSource(
      strings = {"Virus detected: Eicar-Test-Signature", "Virus scan unavailable: refused"})
  void scanRejectionTakesPrecedenceOverInvalidFileContent(String reason) throws Exception {
    doThrow(new VirusDetectedException(reason)).when(virusScanService).scanOrThrow(any(), any());

    mockMvc
        .perform(upload(file("activity.geojson", "invalid json")).with(csrf().asHeader()))
        .andExpect(status().isUnprocessableEntity())
        .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_PROBLEM_JSON))
        .andExpect(jsonPath("$.status").value(422))
        .andExpect(jsonPath("$.detail").value(reason));
  }

  @Test
  void requiresCsrf() throws Exception {
    mockMvc
        .perform(upload(file("activity.geojson", GEOJSON)))
        .andExpect(status().isForbidden());
    verifyNoInteractions(virusScanService);
  }

  @Test
  void requiresAuthentication() throws Exception {
    mockMvc
        .perform(
            upload(file("activity.geojson", GEOJSON))
                .with(anonymous())
                .with(csrf().asHeader()))
        .andExpect(status().isUnauthorized());
    verifyNoInteractions(virusScanService);
  }

  private static MockMultipartFile file(String name, String data) {
    return new MockMultipartFile(
        "file",
        name,
        MediaType.APPLICATION_OCTET_STREAM_VALUE,
        data.getBytes(StandardCharsets.UTF_8));
  }

  private static MockHttpServletRequestBuilder upload(MockMultipartFile file) {
    return multipart(UPLOAD_PATH).file(file).accept(MediaType.APPLICATION_JSON);
  }
}
