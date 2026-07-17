package ca.bc.gov.restapi.results.common.clamav;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import ca.bc.gov.restapi.results.common.exception.VirusDetectedException;
import java.time.Duration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@DisplayName("Unit Test | VirusScanService")
@ExtendWith(MockitoExtension.class)
class VirusScanServiceTest {

  @Mock private ClamAvClient clamAvClient;

  private VirusScanService failClosedService;

  @BeforeEach
  void setup() {
    ClamAvProperties props =
        ClamAvProperties.builder()
            .host("localhost")
            .port(3310)
            .connectTimeout(Duration.ofSeconds(5))
            .readTimeout(Duration.ofSeconds(30))
            .failOpen(false)
            .build();
    failClosedService = new VirusScanService(clamAvClient, props);
  }

  @Test
  @DisplayName("Clean file returns empty Optional")
  void cleanFile_returnsEmpty() {
    when(clamAvClient.scan(any(), any())).thenReturn(ClamAvVerdict.clean());

    assertTrue(failClosedService.check(new byte[] {1}, "clean.bin").isEmpty());
  }

  @Test
  @DisplayName("Infected file returns a rejection reason")
  void infectedFile_returnsRejection() {
    when(clamAvClient.scan(any(), any()))
        .thenReturn(
            ClamAvVerdict.infected("Eicar-Test-Signature", "stream: Eicar-Test-Signature FOUND"));

    assertTrue(failClosedService.check(new byte[] {1}, "evil.bin").isPresent());
  }

  @Test
  @DisplayName("Scanner error with fail-open=true allows the upload through")
  void scannerError_failOpen_allowsUpload() {
    ClamAvProperties propsOpen =
        ClamAvProperties.builder()
            .host("localhost")
            .port(3310)
            .connectTimeout(Duration.ofSeconds(5))
            .readTimeout(Duration.ofSeconds(30))
            .failOpen(true)
            .build();
    VirusScanService failOpenService = new VirusScanService(clamAvClient, propsOpen);

    when(clamAvClient.scan(any(), any())).thenReturn(ClamAvVerdict.error("connection refused"));

    assertTrue(failOpenService.check(new byte[] {1}, "file.bin").isEmpty());
  }

  @Test
  @DisplayName("Scanner error with fail-open=false rejects the upload")
  void scannerError_failClosed_rejectsUpload() {
    when(clamAvClient.scan(any(), any())).thenReturn(ClamAvVerdict.error("connection refused"));

    assertTrue(failClosedService.check(new byte[] {1}, "file.bin").isPresent());
  }

  @Test
  @DisplayName("scanOrThrow throws VirusDetectedException for an infected file")
  void scanOrThrow_infected_throwsVirusDetectedException() {
    when(clamAvClient.scan(any(), any()))
        .thenReturn(
            ClamAvVerdict.infected("Eicar-Test-Signature", "stream: Eicar-Test-Signature FOUND"));

    assertThrows(
        VirusDetectedException.class,
        () -> failClosedService.scanOrThrow(new byte[] {1}, "evil.bin"));
  }

  @Test
  @DisplayName("scanOrThrow does not throw for a clean file")
  void scanOrThrow_clean_doesNotThrow() {
    when(clamAvClient.scan(any(), any())).thenReturn(ClamAvVerdict.clean());

    assertDoesNotThrow(() -> failClosedService.scanOrThrow(new byte[] {1}, "clean.bin"));
  }

  @Test
  @DisplayName(
      "scanOrThrow throws VirusDetectedException when scanner is unavailable (fail-closed)")
  void scanOrThrow_unavailableFailClosed_throwsVirusDetectedException() {
    when(clamAvClient.scan(any(), any())).thenReturn(ClamAvVerdict.error("connection refused"));

    assertThrows(
        VirusDetectedException.class,
        () -> failClosedService.scanOrThrow(new byte[] {1}, "file.bin"));
  }
}
