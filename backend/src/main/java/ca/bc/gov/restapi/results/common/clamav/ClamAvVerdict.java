package ca.bc.gov.restapi.results.common.clamav;

/**
 * The outcome of a single ClamAV scan. Named {@code ClamAvVerdict} rather than
 * {@code ScanResult} to avoid confusion with the legacy RESULTS application.
 *
 * <ul>
 *   <li>{@link Status#CLEAN} — clamd replied {@code ... OK}; file is safe.</li>
 *   <li>{@link Status#INFECTED} — clamd replied {@code ... FOUND}; a virus signature was
 *       detected. Always rejected regardless of the fail-open policy.</li>
 *   <li>{@link Status#ERROR} — the scanner could not be reached or returned an unexpected
 *       reply. The fail-open policy decides whether to accept or reject.</li>
 * </ul>
 */
public record ClamAvVerdict(Status status, String signature, String detail) {

  /** The three possible outcomes of a ClamAV scan. */
  public enum Status {
    CLEAN,
    INFECTED,
    ERROR
  }

  /** File passed the scan cleanly. */
  public static ClamAvVerdict clean() {
    return new ClamAvVerdict(Status.CLEAN, null, null);
  }

  /**
   * File is infected.
   *
   * @param signature the virus signature name reported by clamd
   * @param raw the full raw reply from clamd
   */
  public static ClamAvVerdict infected(String signature, String raw) {
    return new ClamAvVerdict(Status.INFECTED, signature, raw);
  }

  /**
   * Scanner error or unreachable.
   *
   * @param detail description of the error (exception message or raw reply)
   */
  public static ClamAvVerdict error(String detail) {
    return new ClamAvVerdict(Status.ERROR, null, detail);
  }
}
