package ca.bc.gov.restapi.results.common.endpoint;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import ca.bc.gov.restapi.results.common.dto.opening.OpeningSearchResponseDto;
import ca.bc.gov.restapi.results.common.service.OpeningSearchService;
import ca.bc.gov.restapi.results.common.service.opening.details.OpeningDetailsService;
import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.server.ResponseStatusException;

@ExtendWith(MockitoExtension.class)
@DisplayName("Unit Test | OpeningEndpoint")
class OpeningEndpointTest {

  @Mock private OpeningDetailsService openingDetailsService;
  @Mock private OpeningSearchService openingSearchService;

  private OpeningEndpoint endpoint;

  @BeforeEach
  void setUp() {
    endpoint = new OpeningEndpoint(openingDetailsService, openingSearchService);
  }

  private HttpServletRequest mockRequest(String sizeParam) {
    HttpServletRequest req = mock(HttpServletRequest.class);
    when(req.getParameter("size")).thenReturn(sizeParam);
    return req;
  }

  // ─── getUserCreatedOpenings ───────────────────────────────────────────────

  @Test
  @DisplayName("getUserCreatedOpenings with valid size should delegate to search service")
  void getUserCreatedOpenings_validSize_shouldReturnPage() {
    Page<OpeningSearchResponseDto> page = new PageImpl<>(List.of(), PageRequest.of(0, 10), 0);
    when(openingSearchService.searchOpeningExact(any(), any())).thenReturn(page);

    Page<OpeningSearchResponseDto> result =
        endpoint.getUserCreatedOpenings(PageRequest.of(0, 10), mockRequest("10"));

    assertThat(result).isNotNull();
  }

  @Test
  @DisplayName("getUserCreatedOpenings with no size param should succeed")
  void getUserCreatedOpenings_noSizeParam_shouldSucceed() {
    Page<OpeningSearchResponseDto> page = new PageImpl<>(List.of(), PageRequest.of(0, 20), 0);
    when(openingSearchService.searchOpeningExact(any(), any())).thenReturn(page);

    Page<OpeningSearchResponseDto> result =
        endpoint.getUserCreatedOpenings(PageRequest.of(0, 20), mockRequest(null));

    assertThat(result).isNotNull();
  }

  @Test
  @DisplayName("getUserCreatedOpenings with empty size param should succeed")
  void getUserCreatedOpenings_emptySizeParam_shouldSucceed() {
    Page<OpeningSearchResponseDto> page = new PageImpl<>(List.of(), PageRequest.of(0, 20), 0);
    when(openingSearchService.searchOpeningExact(any(), any())).thenReturn(page);

    Page<OpeningSearchResponseDto> result =
        endpoint.getUserCreatedOpenings(PageRequest.of(0, 20), mockRequest(""));

    assertThat(result).isNotNull();
  }

  @Test
  @DisplayName("getUserCreatedOpenings with size=0 should throw 400 BAD_REQUEST")
  void getUserCreatedOpenings_sizeZero_shouldThrow400() {
    assertThatThrownBy(
            () -> endpoint.getUserCreatedOpenings(PageRequest.of(0, 10), mockRequest("0")))
        .isInstanceOf(ResponseStatusException.class)
        .satisfies(
            ex ->
                assertThat(((ResponseStatusException) ex).getStatusCode())
                    .isEqualTo(HttpStatus.BAD_REQUEST));
  }

  @Test
  @DisplayName("getUserCreatedOpenings with negative size should throw 400 BAD_REQUEST")
  void getUserCreatedOpenings_negativeSize_shouldThrow400() {
    assertThatThrownBy(
            () -> endpoint.getUserCreatedOpenings(PageRequest.of(0, 10), mockRequest("-1")))
        .isInstanceOf(ResponseStatusException.class)
        .satisfies(
            ex ->
                assertThat(((ResponseStatusException) ex).getStatusCode())
                    .isEqualTo(HttpStatus.BAD_REQUEST));
  }

  @Test
  @DisplayName("getUserCreatedOpenings with size exceeding max should throw 400 BAD_REQUEST")
  void getUserCreatedOpenings_sizeExceedsMax_shouldThrow400() {
    assertThatThrownBy(
            () -> endpoint.getUserCreatedOpenings(PageRequest.of(0, 10), mockRequest("99999")))
        .isInstanceOf(ResponseStatusException.class)
        .satisfies(
            ex ->
                assertThat(((ResponseStatusException) ex).getStatusCode())
                    .isEqualTo(HttpStatus.BAD_REQUEST));
  }

  @Test
  @DisplayName("getUserCreatedOpenings with non-numeric size should throw 400 BAD_REQUEST")
  void getUserCreatedOpenings_nonNumericSize_shouldThrow400() {
    assertThatThrownBy(
            () -> endpoint.getUserCreatedOpenings(PageRequest.of(0, 10), mockRequest("notANumber")))
        .isInstanceOf(ResponseStatusException.class)
        .satisfies(
            ex ->
                assertThat(((ResponseStatusException) ex).getStatusCode())
                    .isEqualTo(HttpStatus.BAD_REQUEST));
  }

  // ─── getAttachmentByGuid ─────────────────────────────────────────────────

  @Test
  @DisplayName("getAttachmentByGuid should return 200 with presigned URL")
  void getAttachmentByGuid_happyPath_shouldReturn200() {
    when(openingDetailsService.generateAttachmentDownloadUrl(eq("abc-guid")))
        .thenReturn("https://example.com/presigned");

    ResponseEntity<String> response = endpoint.getAttachmentByGuid(12345L, "abc-guid");

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertThat(response.getBody()).isEqualTo("https://example.com/presigned");
  }
}
