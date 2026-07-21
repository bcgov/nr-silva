package ca.bc.gov.restapi.results.common.clamav;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import ca.bc.gov.restapi.results.common.exception.VirusDetectedException;
import java.time.Duration;
import java.util.Optional;
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
  private VirusScanService failOpenService;

  @BeforeEach
  void setup() {
    ClamAvProperties propsFailClosed =
        ClamAvProperties.builder()
            .host("localhost")
            .port(3310)
            .connectTimeout(Duration.ofSeconds(5))
            .readTimeout(Duration.ofSeconds(30))
            .failOpen(false)
            .build();
    failClosedService = new VirusScanService(clamAvClient, propsFailClosed);

    ClamAvProperties propsFailOpen =
        ClamAvProperties.builder()
            .host("localhost")
            .port(3310)
            .connectTimeout(Duration.ofSeconds(5))
            .readTimeout(Duration.ofSeconds(30))
            .failOpen(true)
            .build();
    failOpenService = new VirusScanService(clamAvClient, propsFailOpen);
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
  @DisplayName("Infected file rejection contains signature name")
  void infectedFile_rejectionContainsSignature() {
    when(clamAvClient.scan(any(), any()))
        .thenReturn(ClamAvVerdict.infected("Virus.Win32.Test", "stream: Virus.Win32.Test FOUND"));

    Optional<String> result = failClosedService.check(new byte[] {1}, "evil.bin");

    assertTrue(result.isPresent());
    assertTrue(result.get().contains("Virus.Win32.Test"));
  }

  @Test
  @DisplayName("Scanner error with fail-open=true allows the upload through")
  void scannerError_failOpen_allowsUpload() {
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
  @DisplayName("Scanner error rejection contains error detail")
  void scannerError_rejectionContainsDetail() {
    when(clamAvClient.scan(any(), any())).thenReturn(ClamAvVerdict.error("timeout"));

    Optional<String> result = failClosedService.check(new byte[] {1}, "file.bin");

    assertTrue(result.isPresent());
    assertTrue(result.get().contains("timeout"));
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

  @Test
  @DisplayName("scanOrThrow does not throw when scanner is unavailable (fail-open)")
  void scanOrThrow_unavailableFailOpen_doesNotThrow() {
    when(clamAvClient.scan(any(), any())).thenReturn(ClamAvVerdict.error("connection refused"));

    assertDoesNotThrow(() -> failOpenService.scanOrThrow(new byte[] {1}, "file.bin"));
  }

  @Test
  @DisplayName("onReady() calls ping() and logs when clamd is reachable")
  void onReady_clamavReachable_callsPing() {
    when(clamAvClient.ping()).thenReturn(true);
    when(clamAvClient.host()).thenReturn("localhost");
    when(clamAvClient.port()).thenReturn(3310);

    assertDoesNotThrow(() -> failClosedService.onReady());

    verify(clamAvClient).ping();
  }

  @Test
  @DisplayName("onReady() calls ping() and logs when clamd is unreachable (fail-closed)")
  void onReady_clamavUnreachableFailClosed_callsPing() {
    when(clamAvClient.ping()).thenReturn(false);
    when(clamAvClient.host()).thenReturn("localhost");
    when(clamAvClient.port()).thenReturn(3310);

    assertDoesNotThrow(() -> failClosedService.onReady());

    verify(clamAvClient).ping();
  }

  @Test
  @DisplayName("onReady() calls ping() and logs when clamd is unreachable (fail-open)")
  void onReady_clamavUnreachableFailOpen_callsPing() {
    when(clamAvClient.ping()).thenReturn(false);
    when(clamAvClient.host()).thenReturn("localhost");
    when(clamAvClient.port()).thenReturn(3310);

    assertDoesNotThrow(() -> failOpenService.onReady());

    verify(clamAvClient).ping();
  }

  @Test
  @DisplayName("check() with large file (multiple MB) handles scanning")
  void check_largeFile_scannedSuccessfully() {
    when(clamAvClient.scan(any(), any())).thenReturn(ClamAvVerdict.clean());

    byte[] largeFile = new byte[5 * 1024 * 1024]; // 5MB
    for (int i = 0; i < largeFile.length; i++) {
      largeFile[i] = (byte) (i % 256);
    }

    assertTrue(failClosedService.check(largeFile, "large.bin").isEmpty());
  }

  @Test
  @DisplayName("check() with small file (1 byte) handles scanning")
  void check_singleByte_scannedSuccessfully() {
    when(clamAvClient.scan(any(), any())).thenReturn(ClamAvVerdict.clean());

    assertTrue(failClosedService.check(new byte[] {0x00}, "tiny.bin").isEmpty());
  }

  @Test
  @DisplayName("check() with filename containing special characters")
  void check_specialCharactersInFilename() {
    when(clamAvClient.scan(any(), any())).thenReturn(ClamAvVerdict.clean());

    assertTrue(
        failClosedService.check(new byte[] {1}, "file-with-special_chars.@#$.bin").isEmpty());
  }

  @Test
  @DisplayName("scanOrThrow() with multiple consecutive scans")
  void scanOrThrow_multipleScans_consistent() {
    when(clamAvClient.scan(any(), any())).thenReturn(ClamAvVerdict.clean());

    assertDoesNotThrow(() -> failClosedService.scanOrThrow(new byte[] {1}, "file1.bin"));
    assertDoesNotThrow(() -> failClosedService.scanOrThrow(new byte[] {2}, "file2.bin"));
    assertDoesNotThrow(() -> failClosedService.scanOrThrow(new byte[] {3}, "file3.bin"));
  }

  @Test
  @DisplayName("check() returns correct rejection reason format for infected file")
  void check_infectedRejectionFormat() {
    when(clamAvClient.scan(any(), any()))
        .thenReturn(ClamAvVerdict.infected("Win.Malware.XYZ", "stream: Win.Malware.XYZ FOUND"));

    Optional<String> result = failClosedService.check(new byte[] {1}, "bad.exe");

    assertTrue(result.isPresent());
    assertTrue(result.get().startsWith("Virus detected:"));
  }

  @Test
  @DisplayName("check() returns correct rejection reason format for scanner error")
  void check_errorRejectionFormat() {
    when(clamAvClient.scan(any(), any())).thenReturn(ClamAvVerdict.error("socket timeout"));

    Optional<String> result = failClosedService.check(new byte[] {1}, "file.bin");

    assertTrue(result.isPresent());
    assertTrue(result.get().startsWith("Virus scan unavailable:"));
  }
}
