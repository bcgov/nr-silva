package ca.bc.gov.restapi.results.postgres.service;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import ca.bc.gov.restapi.results.postgres.SilvaConstants;
import java.nio.charset.StandardCharsets;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.server.ResponseStatusException;

@DisplayName("Unit Test | OpeningSpatialFileService")
class OpeningSpatialFileServiceTest {

  private final OpeningSpatialFileService service = new OpeningSpatialFileService();

  @Test
  @DisplayName("Should throw when file is null or empty")
  void shouldThrowOnNullOrEmpty() {
    MockMultipartFile empty = new MockMultipartFile("file", "", "application/json", new byte[0]);

    assertThatThrownBy(() -> service.processOpeningSpatialFile(empty))
        .isInstanceOf(ResponseStatusException.class);
  }

  @Test
  @DisplayName("Should throw when file exceeds size limit")
  void shouldThrowOnOversize() {
    byte[] big = new byte[SilvaConstants.MAX_OPENING_FILE_SIZE_BYTES + 1];
    MockMultipartFile bigFile =
        new MockMultipartFile("file", "big.geojson", "application/json", big);

    assertThatThrownBy(() -> service.processOpeningSpatialFile(bigFile))
        .isInstanceOf(ResponseStatusException.class);
  }

  @Test
  @DisplayName("Should throw on unsupported extension")
  void shouldThrowOnUnsupportedExtension() {
    MockMultipartFile file =
        new MockMultipartFile(
            "file", "data.txt", "text/plain", "hello".getBytes(StandardCharsets.UTF_8));

    assertThatThrownBy(() -> service.processOpeningSpatialFile(file))
        .isInstanceOf(ResponseStatusException.class);
  }
}
