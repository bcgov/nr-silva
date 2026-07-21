package ca.bc.gov.restapi.results.common.clamav;

import ca.bc.gov.restapi.results.common.exception.VirusDetectedException;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

/**
 * Policy layer over {@link ClamAvClient}. Decides whether a scan verdict should allow or deny an
 * upload, applies the fail-open/fail-closed policy for scanner errors, emits greppable verdict log
 * lines, and pings clamd at startup.
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class VirusScanService {

  private final ClamAvClient clamAvClient;
  private final ClamAvProperties properties;

  /**
   * Scan {@code data} and return a rejection reason when the upload must be denied, or {@link
   * Optional#empty()} when it should be accepted.
   *
   * @param data raw file bytes
   * @param filename used for logging
   * @return rejection reason, or empty if the file is safe to accept
   */
  public Optional<String> check(byte[] data, String filename) {
    ClamAvVerdict verdict = clamAvClient.scan(data, filename);
    return switch (verdict.status()) {
      case CLEAN -> {
        log.info("clamav scan: '{}' ({} bytes) → CLEAN", filename, data.length);
        yield Optional.empty();
      }
      case INFECTED -> {
        log.warn(
            "clamav scan: '{}' ({} bytes) → INFECTED ({}) — rejecting",
            filename,
            data.length,
            verdict.signature());
        yield Optional.of("Virus detected: " + verdict.signature());
      }
      case ERROR -> {
        if (properties.isFailOpen()) {
          log.warn(
              "clamav scan: '{}' ({} bytes) → UNAVAILABLE ({}) — failing OPEN, allowing",
              filename,
              data.length,
              verdict.detail());
          yield Optional.empty();
        } else {
          log.warn(
              "clamav scan: '{}' ({} bytes) → UNAVAILABLE ({}) — failing CLOSED, rejecting",
              filename,
              data.length,
              verdict.detail());
          yield Optional.of("Virus scan unavailable: " + verdict.detail());
        }
      }
    };
  }

  /**
   * Like {@link #check} but throws {@link VirusDetectedException} (HTTP 422) when the upload is
   * rejected.
   *
   * @param data raw file bytes
   * @param filename used for logging
   * @throws VirusDetectedException when the file must be denied
   */
  public void scanOrThrow(byte[] data, String filename) {
    check(data, filename)
        .ifPresent(
            reason -> {
              throw new VirusDetectedException(reason);
            });
  }

  /** Pings clamd at startup and logs the result so a misconfigured scanner is visible early. */
  @EventListener(ApplicationReadyEvent.class)
  public void onReady() {
    if (clamAvClient.ping()) {
      log.info(
          "ClamAV startup health check: clamd reachable at {}:{} — PONG",
          clamAvClient.host(),
          clamAvClient.port());
    } else {
      log.warn(
          "ClamAV startup health check: clamd unreachable at {}:{} — uploads will {}"
              + " (fail-open={})",
          clamAvClient.host(),
          clamAvClient.port(),
          properties.isFailOpen() ? "be allowed" : "be rejected",
          properties.isFailOpen());
    }
  }
}
