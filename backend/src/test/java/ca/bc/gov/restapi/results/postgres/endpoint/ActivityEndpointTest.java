package ca.bc.gov.restapi.results.postgres.endpoint;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.AdditionalMatchers.aryEq;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import ca.bc.gov.restapi.results.common.clamav.VirusScanService;
import ca.bc.gov.restapi.results.common.exception.VirusDetectedException;
import ca.bc.gov.restapi.results.postgres.SilvaPostgresConstants;
import ca.bc.gov.restapi.results.postgres.dto.CreateActivityRequestDto;
import ca.bc.gov.restapi.results.postgres.dto.CreateActivityResponseDto;
import ca.bc.gov.restapi.results.postgres.dto.ExtractedGeoDataDto;
import ca.bc.gov.restapi.results.postgres.enums.ActivityStatus;
import ca.bc.gov.restapi.results.postgres.service.CreateActivityService;
import ca.bc.gov.restapi.results.postgres.service.SpatialFileService;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.ArgumentCaptor;
import org.mockito.InOrder;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

/** Unit coverage for activity upload validation, scanning, and delegation. */
@DisplayName("Unit Test | ActivityEndpoint")
class ActivityEndpointTest {

  private static final byte[] FILE_BYTES = {1, 2, 3};
  private static final String DATA =
      """
      {"openingId":1,"status":"PLANNED","baseCode":"HC","fundingSourceCodes":["BCP"],
       "plannedDate":"2999-01-01","plannedArea":1.5}
      """;
  private final SpatialFileService spatialFileService = mock(SpatialFileService.class);
  private final VirusScanService virusScanService = mock(VirusScanService.class);
  private final CreateActivityService createActivityService = mock(CreateActivityService.class);
  private final ActivityEndpoint endpoint =
      new ActivityEndpoint(
          spatialFileService,
          virusScanService,
          createActivityService,
          new ObjectMapper().findAndRegisterModules());

  @Test
  void scansBeforeProcessingAndReturnsTheServiceResult() {
    MockMultipartFile file = new MockMultipartFile("file", "activity.geojson", null, FILE_BYTES);
    ExtractedGeoDataDto expected =
        new ExtractedGeoDataDto(new BigDecimal("4.3"), new ObjectMapper().createObjectNode());
    when(spatialFileService.processSpatialFile(eq("activity.geojson"), aryEq(FILE_BYTES)))
        .thenReturn(expected);

    assertThat(endpoint.uploadActivitySpatial(file)).isSameAs(expected);

    InOrder order = inOrder(virusScanService, spatialFileService);
    order.verify(virusScanService).scanOrThrow(aryEq(FILE_BYTES), eq("activity.geojson"));
    order.verify(spatialFileService).processSpatialFile(eq("activity.geojson"), aryEq(FILE_BYTES));
    order.verifyNoMoreInteractions();
  }

  @Test
  void rejectsNullFile() {
    assertThatThrownBy(() -> endpoint.uploadActivitySpatial(null))
        .isInstanceOfSatisfying(
            ResponseStatusException.class,
            exception -> {
              assertThat(exception.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
              assertThat(exception.getReason()).isEqualTo("Uploaded file is null or empty");
            });
    verifyNoInteractions(virusScanService, spatialFileService);
  }

  @Test
  void rejectsEmptyFile() {
    MockMultipartFile file = new MockMultipartFile("file", new byte[0]);

    assertThatThrownBy(() -> endpoint.uploadActivitySpatial(file))
        .isInstanceOfSatisfying(
            ResponseStatusException.class,
            exception -> assertThat(exception.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST));
    verifyNoInteractions(virusScanService, spatialFileService);
  }

  @Test
  void rejectsOversizedFileBeforeReadingOrScanning() throws IOException {
    MultipartFile file = mock(MultipartFile.class);
    when(file.getSize()).thenReturn(SilvaPostgresConstants.MAX_SPATIAL_FILE_SIZE_BYTES + 1L);

    assertThatThrownBy(() -> endpoint.uploadActivitySpatial(file))
        .isInstanceOfSatisfying(
            ResponseStatusException.class,
            exception -> {
              assertThat(exception.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
              assertThat(exception.getReason()).isEqualTo("File exceeds 25MB size limit");
            });
    verify(file, never()).getBytes();
    verifyNoInteractions(virusScanService, spatialFileService);
  }

  @Test
  void acceptsFileAtTheSizeLimit() throws IOException {
    MultipartFile file = mock(MultipartFile.class);
    when(file.getSize()).thenReturn(SilvaPostgresConstants.MAX_SPATIAL_FILE_SIZE_BYTES.longValue());
    when(file.getBytes()).thenReturn(FILE_BYTES);
    when(file.getOriginalFilename()).thenReturn("activity.geojson");

    endpoint.uploadActivitySpatial(file);

    verify(virusScanService).scanOrThrow(aryEq(FILE_BYTES), eq("activity.geojson"));
    verify(spatialFileService).processSpatialFile(eq("activity.geojson"), aryEq(FILE_BYTES));
  }

  @Test
  void reportsReadFailureWithoutScanningOrProcessing() throws IOException {
    MultipartFile file = mock(MultipartFile.class);
    IOException failure = new IOException("read failed");
    when(file.getBytes()).thenThrow(failure);

    assertThatThrownBy(() -> endpoint.uploadActivitySpatial(file))
        .isInstanceOfSatisfying(
            ResponseStatusException.class,
            exception -> {
              assertThat(exception.getStatusCode())
                  .isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
              assertThat(exception.getReason()).isEqualTo("Failed to read file: read failed");
              assertThat(exception.getCause()).isSameAs(failure);
            });
    verifyNoInteractions(virusScanService, spatialFileService);
  }

  @ParameterizedTest
  @ValueSource(
      strings = {"Virus detected: Eicar-Test-Signature", "Virus scan unavailable: refused"})
  void scanRejectionPreventsProcessing(String reason) {
    MockMultipartFile file = new MockMultipartFile("file", "activity.geojson", null, FILE_BYTES);
    VirusDetectedException failure = new VirusDetectedException(reason);
    doThrow(failure)
        .when(virusScanService)
        .scanOrThrow(aryEq(FILE_BYTES), eq("activity.geojson"));

    assertThatThrownBy(() -> endpoint.uploadActivitySpatial(file)).isSameAs(failure);
    verifyNoInteractions(spatialFileService);
  }

  @Test
  void preservesProcessingErrors() {
    MockMultipartFile file = new MockMultipartFile("file", "activity.geojson", null, FILE_BYTES);
    ResponseStatusException failure =
        new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid geometry");
    when(spatialFileService.processSpatialFile(eq("activity.geojson"), aryEq(FILE_BYTES)))
        .thenThrow(failure);

    assertThatThrownBy(() -> endpoint.uploadActivitySpatial(file)).isSameAs(failure);
    verify(virusScanService).scanOrThrow(aryEq(FILE_BYTES), eq("activity.geojson"));
  }

  @Test
  void createScansTheFileBeforeCreatingTheActivity() {
    MockMultipartFile file = new MockMultipartFile("file", "activity.geojson", null, FILE_BYTES);
    CreateActivityResponseDto expected =
        new CreateActivityResponseDto(10L, 1L, ActivityStatus.PLANNED, "HC");
    when(createActivityService.createActivity(
            any(CreateActivityRequestDto.class), eq("activity.geojson"), aryEq(FILE_BYTES)))
        .thenReturn(expected);

    assertThat(endpoint.createActivity(DATA, file)).isSameAs(expected);

    InOrder order = inOrder(virusScanService, createActivityService);
    order.verify(virusScanService).scanOrThrow(aryEq(FILE_BYTES), eq("activity.geojson"));
    order
        .verify(createActivityService)
        .createActivity(any(CreateActivityRequestDto.class), eq("activity.geojson"), any());
    order.verifyNoMoreInteractions();
  }

  @Test
  void createParsesTheRequestData() {
    MockMultipartFile file = new MockMultipartFile("file", "activity.geojson", null, FILE_BYTES);

    endpoint.createActivity(DATA, file);

    ArgumentCaptor<CreateActivityRequestDto> request =
        ArgumentCaptor.forClass(CreateActivityRequestDto.class);
    verify(createActivityService).createActivity(request.capture(), any(), any());
    assertThat(request.getValue().openingId()).isEqualTo(1L);
    assertThat(request.getValue().status()).isEqualTo(ActivityStatus.PLANNED);
    assertThat(request.getValue().plannedDate()).isEqualTo(LocalDate.of(2999, 1, 1));
    assertThat(request.getValue().plannedArea()).isEqualByComparingTo("1.5");
  }

  @Test
  void createWithoutFileSkipsTheScanner() {
    CreateActivityResponseDto expected =
        new CreateActivityResponseDto(10L, 1L, ActivityStatus.PLANNED, "HC");
    when(createActivityService.createActivity(any(CreateActivityRequestDto.class)))
        .thenReturn(expected);

    assertThat(endpoint.createActivity(DATA, null)).isSameAs(expected);
    verifyNoInteractions(virusScanService, spatialFileService);
  }

  @Test
  void createIgnoresAnUnselectedFilePart() {
    MockMultipartFile noFile = new MockMultipartFile("file", "", null, new byte[0]);

    endpoint.createActivity(DATA, noFile);

    verify(createActivityService).createActivity(any(CreateActivityRequestDto.class));
    verifyNoInteractions(virusScanService);
  }

  @Test
  void createRejectsAnEmptyNamedFile() {
    MockMultipartFile empty = new MockMultipartFile("file", "activity.geojson", null, new byte[0]);

    assertThatThrownBy(() -> endpoint.createActivity(DATA, empty))
        .isInstanceOfSatisfying(
            ResponseStatusException.class,
            e -> assertThat(e.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST));
    verifyNoInteractions(virusScanService, createActivityService);
  }

  @Test
  void createRejectsInvalidJsonBeforeAnythingElse() {
    MockMultipartFile file = new MockMultipartFile("file", "activity.geojson", null, FILE_BYTES);

    assertThatThrownBy(() -> endpoint.createActivity("{not json", file))
        .isInstanceOfSatisfying(
            ResponseStatusException.class,
            e -> {
              assertThat(e.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
              assertThat(e.getReason()).startsWith("Invalid JSON in 'data'");
            });
    verifyNoInteractions(virusScanService, createActivityService);
  }

  @Test
  void createRejectsAnUnknownStatus() {
    String data = DATA.replace("PLANNED", "DONE");

    assertThatThrownBy(() -> endpoint.createActivity(data, null))
        .isInstanceOfSatisfying(
            ResponseStatusException.class,
            e -> assertThat(e.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST));
    verifyNoInteractions(createActivityService);
  }

  @Test
  void createDoesNotSaveWhenTheScannerRejectsTheFile() {
    MockMultipartFile file = new MockMultipartFile("file", "activity.geojson", null, FILE_BYTES);
    VirusDetectedException failure = new VirusDetectedException("Virus detected: Eicar");
    doThrow(failure)
        .when(virusScanService)
        .scanOrThrow(aryEq(FILE_BYTES), eq("activity.geojson"));

    assertThatThrownBy(() -> endpoint.createActivity(DATA, file)).isSameAs(failure);
    verifyNoInteractions(createActivityService);
  }

  @Test
  void createRejectsAnOversizedFileBeforeReadingOrScanning() throws IOException {
    MultipartFile file = mock(MultipartFile.class);
    when(file.isEmpty()).thenReturn(false);
    when(file.getOriginalFilename()).thenReturn("activity.geojson");
    when(file.getSize()).thenReturn(SilvaPostgresConstants.MAX_SPATIAL_FILE_SIZE_BYTES + 1L);

    assertThatThrownBy(() -> endpoint.createActivity(DATA, file))
        .isInstanceOfSatisfying(
            ResponseStatusException.class,
            e -> assertThat(e.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST));
    verify(file, never()).getBytes();
    verifyNoInteractions(virusScanService, createActivityService);
  }
}
