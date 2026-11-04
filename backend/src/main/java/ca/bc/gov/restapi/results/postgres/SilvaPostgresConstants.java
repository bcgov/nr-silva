package ca.bc.gov.restapi.results.postgres;

import ca.bc.gov.restapi.results.common.SilvaConstants;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/** This class holds properties used for the application configuration. */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SilvaPostgresConstants extends SilvaConstants {

  public static final Integer MAX_SPATIAL_FILE_SIZE_BYTES = 25 * 1024 * 1024; // 25 MB

  /** Tolerance for Douglas-Peucker thinning in meters (for EPSG:3005). */
  public static final double THINNING_TOLERANCE_METERS = 2.5;

  /**
   * Approximate tolerance for Douglas-Peucker thinning in degrees (for EPSG:4326). 2.5 meters ≈
   * 0.0000225 degrees at BC latitude (1 degree ≈ 111,000 meters).
   */
  public static final double THINNING_TOLERANCE_DEGREES = 2.5 / 111_000.0;

  /**
   * IMPORTANT business rule: tolerance in metres (EPSG:3005) applied when checking that an
   * activity geometry is within its opening geometry.
   *
   * <p>Two things move an activity away from the opening boundary it was drawn along:
   *
   * <ul>
   *   <li>Spatial files are usually in EPSG:4326 with 5 decimals (about 1.1 m north-south and 0.7 m
   *       east-west in BC) and the activity and opening are rounded independently, which can leave
   *       them about 1.3 m apart.
   *   <li>An opening created in Silva is stored after vertex thinning ({@link
   *       #THINNING_TOLERANCE_METERS}), so its stored boundary can be up to 2.5 m from the one the
   *       user uploaded.
   * </ul>
   *
   * <p>Without a tolerance, valid activities would be rejected as "outside". The opening geometry
   * is therefore buffered by this value before the containment test, so touching the boundary is
   * allowed and anything further out is rejected. The check runs on the activity as uploaded,
   * before its own thinning. 4 m covers both effects (about 3.8 m combined) and is a fifth of the
   * 20 m positional accuracy that RESULTS spatial submissions are held to (RESULTS Information
   * Submission Specifications, section 6.1.3).
   */
  public static final double OPENING_CONTAINMENT_TOLERANCE_METERS = 4.0;
}
