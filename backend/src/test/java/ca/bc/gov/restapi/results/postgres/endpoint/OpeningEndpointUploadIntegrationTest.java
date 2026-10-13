package ca.bc.gov.restapi.results.postgres.endpoint;

import static org.mockito.AdditionalMatchers.aryEq;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import ca.bc.gov.restapi.results.common.clamav.VirusScanService;
import ca.bc.gov.restapi.results.common.exception.VirusDetectedException;
import ca.bc.gov.restapi.results.extensions.AbstractTestContainerIntegrationTest;
import ca.bc.gov.restapi.results.extensions.WithMockJwt;
import ca.bc.gov.restapi.results.postgres.config.OpeningEndpointTestConfig;
import ca.bc.gov.restapi.results.postgres.dto.ExtractedGeoDataDto;
import ca.bc.gov.restapi.results.postgres.service.SpatialFileService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.DispatcherType;
import jakarta.servlet.RequestDispatcher;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIfSystemProperty;
import org.mockito.InOrder;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.server.ResponseStatusException;

@EnabledIfSystemProperty(named = "server.primary-db", matches = "postgres")
@DisplayName("Integration Test | Opening Upload Endpoint")
@AutoConfigureMockMvc
@WithMockJwt
@Import(OpeningEndpointTestConfig.class)
class OpeningEndpointUploadIntegrationTest extends AbstractTestContainerIntegrationTest {

  @Autowired private MockMvc mockMvc;

  @Autowired private SpatialFileService spatialFileService;

  @Autowired private VirusScanService virusScanService;

  @Autowired private ObjectMapper mapper;

  @BeforeEach
  void resetMocks() {
    Mockito.reset(spatialFileService);
    Mockito.reset(virusScanService);
  }

  @Test
  @DisplayName("Should accept upload and return accepted")
  void shouldAcceptUpload() throws Exception {
    ExtractedGeoDataDto dto =
        ExtractedGeoDataDto.builder()
            .geometryArea(new BigDecimal("4.3"))
            .geoJson(mapper.createObjectNode().put("type", "FeatureCollection"))
            .build();
    when(spatialFileService.processSpatialFile(anyString(), any(byte[].class)))
        .thenReturn(dto);

    MockMultipartFile file =
        new MockMultipartFile(
            "file",
            "test.geojson",
            MediaType.APPLICATION_JSON_VALUE,
            "{\"type\":\"FeatureCollection\",\"features\":[]}".getBytes(StandardCharsets.UTF_8));

    mockMvc
        .perform(
            multipart("/api/openings/create/upload")
                .file(file)
                .with(csrf().asHeader())
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isAccepted())
        .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
        .andExpect(content().json(mapper.writeValueAsString(dto)))
        .andExpect(jsonPath("$.geometryArea").value(4.3))
        .andExpect(jsonPath("$.geoJson.type").value("FeatureCollection"));

    InOrder order = inOrder(virusScanService, spatialFileService);
    order.verify(virusScanService).scanOrThrow(aryEq(file.getBytes()), eq("test.geojson"));
    order.verify(spatialFileService).processSpatialFile(eq("test.geojson"), aryEq(file.getBytes()));
    order.verifyNoMoreInteractions();
  }

  @Test
  @DisplayName("Should be forbidden without CSRF")
  void shouldBeForbiddenWithoutCsrf() throws Exception {
    MockMultipartFile file =
        new MockMultipartFile(
            "file",
            "test.geojson",
            MediaType.APPLICATION_JSON_VALUE,
            "{\"type\":\"FeatureCollection\",\"features\":[]}".getBytes(StandardCharsets.UTF_8));

    mockMvc
        .perform(
            multipart("/api/openings/create/upload")
                .file(file)
                .contentType(MediaType.MULTIPART_FORM_DATA))
        .andExpect(status().isForbidden());

    verifyNoInteractions(virusScanService, spatialFileService);
  }

  @Test
  @DisplayName("Should return bad request when service throws")
  void shouldReturnBadRequestWhenServiceThrows() throws Exception {
    when(spatialFileService.processSpatialFile(anyString(), any(byte[].class)))
        .thenThrow(
            new ResponseStatusException(org.springframework.http.HttpStatus.BAD_REQUEST, "bad"));

    MockMultipartFile file =
        new MockMultipartFile(
            "file",
            "test.geojson",
            MediaType.APPLICATION_JSON_VALUE,
            "{\"type\":\"FeatureCollection\",\"features\":[]}".getBytes(StandardCharsets.UTF_8));

    mockMvc
        .perform(
            multipart("/api/openings/create/upload")
                .file(file)
                .with(csrf().asHeader())
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isBadRequest())
        .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_PROBLEM_JSON))
        .andExpect(jsonPath("$.status").value(400))
        .andExpect(jsonPath("$.detail").value("bad"));

    InOrder order = inOrder(virusScanService, spatialFileService);
    order.verify(virusScanService).scanOrThrow(aryEq(file.getBytes()), eq("test.geojson"));
    order.verify(spatialFileService).processSpatialFile(eq("test.geojson"), aryEq(file.getBytes()));
    order.verifyNoMoreInteractions();
  }

  @Test
  @DisplayName("Should return 422 when a virus is detected")
  void shouldReturn422WhenVirusDetected() throws Exception {
    doThrow(new VirusDetectedException("Virus detected: Eicar-Test-Signature"))
        .when(virusScanService)
        .scanOrThrow(any(), any());

    MockMultipartFile file =
        new MockMultipartFile(
            "file", "evil.bin", MediaType.APPLICATION_OCTET_STREAM_VALUE, new byte[] {1, 2, 3});

    mockMvc
        .perform(
            multipart("/api/openings/create/upload")
                .file(file)
                .with(csrf().asHeader())
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isUnprocessableEntity())
        .andExpect(jsonPath("$.detail").value("Virus detected: Eicar-Test-Signature"));

    verifyNoInteractions(spatialFileService);
  }

  @Test
  @DisplayName("Should return 422 when the virus scanner is unavailable (fail-closed)")
  void shouldReturn422WhenScannerUnavailable() throws Exception {
    doThrow(new VirusDetectedException("Virus scan unavailable: connection refused"))
        .when(virusScanService)
        .scanOrThrow(any(), any());

    MockMultipartFile file =
        new MockMultipartFile(
            "file",
            "file.geojson",
            MediaType.APPLICATION_JSON_VALUE,
            "{\"type\":\"FeatureCollection\",\"features\":[]}".getBytes(StandardCharsets.UTF_8));

    mockMvc
        .perform(
            multipart("/api/openings/create/upload")
                .file(file)
                .with(csrf().asHeader())
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isUnprocessableEntity())
        .andExpect(jsonPath("$.detail").value("Virus scan unavailable: connection refused"));

    verifyNoInteractions(spatialFileService);
  }

  @Test
  @DisplayName("Should preserve server error status during the internal error dispatch")
  void shouldPreserveServerErrorStatusDuringErrorDispatch() throws Exception {
    mockMvc
        .perform(
            get("/error")
                .with(
                    request -> {
                      request.setDispatcherType(DispatcherType.ERROR);
                      return request;
                    })
                .requestAttr(RequestDispatcher.ERROR_STATUS_CODE, 500)
                .requestAttr(RequestDispatcher.ERROR_REQUEST_URI, "/api/openings/create/upload"))
        .andExpect(status().isInternalServerError());
  }

  @Test
  @DisplayName("Should continue to deny direct requests to the error endpoint")
  void shouldDenyDirectRequestToErrorEndpoint() throws Exception {
    mockMvc.perform(get("/error")).andExpect(status().isForbidden());
  }
}
