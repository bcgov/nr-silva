package ca.bc.gov.restapi.results.postgres;

import ca.bc.gov.restapi.results.common.SilvaConstants;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/** This class holds properties used for the application configuration. */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SilvaPostgresConstants extends SilvaConstants {

  public static final Integer MAX_OPENING_FILE_SIZE_BYTES = 25 * 1024 * 1024; // 25 MB

  /** Tolerance for Douglas-Peucker thinning in meters (for EPSG:3005). */
  public static final double THINNING_TOLERANCE_METERS = 2.5;

  /**
   * Approximate tolerance for Douglas-Peucker thinning in degrees (for EPSG:4326). 2.5 meters ≈
   * 0.0000225 degrees at BC latitude (1 degree ≈ 111,000 meters).
   */
  public static final double THINNING_TOLERANCE_DEGREES = 2.5 / 111_000.0;
}
