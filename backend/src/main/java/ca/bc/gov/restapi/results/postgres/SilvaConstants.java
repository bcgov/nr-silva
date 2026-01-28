package ca.bc.gov.restapi.results.postgres;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/** This class holds properties used for the application configuration. */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SilvaConstants {

  public static final Integer MAX_OPENING_FILE_SIZE_BYTES = 25 * 1024 * 1024; // 25 MB

  /** Tolerance for Douglas-Peucker thinning in meters (for EPSG:3005). */
  public static final double THINNING_TOLERANCE_METERS = 2.5;

  /**
   * Approximate tolerance for Douglas-Peucker thinning in degrees (for EPSG:4326). 2.5 meters ≈
   * 0.0000225 degrees at BC latitude (1 degree ≈ 111,000 meters).
   */
  public static final double THINNING_TOLERANCE_DEGREES = 2.5 / 111_000.0;

  public static final String ORG_UNIT = "orgUnit";
  public static final String CATEGORY = "category";
  public static final String STATUS_LIST = "statusList";
  public static final String MY_OPENINGS = "myOpenings";
  public static final String MY_OPENINGS_USER_ID = "myOpeningsUserId";
  public static final String SUBMITTED_TO_FRPA = "submittedToFrpa";
  public static final String DISTURBANCE_DATE_START = "disturbanceDateStart";
  public static final String DISTURBANCE_DATE_END = "disturbanceDateEnd";
  public static final String REGEN_DELAY_DATE_START = "regenDelayDateStart";
  public static final String REGEN_DELAY_DATE_END = "regenDelayDateEnd";
  public static final String FREE_GROWING_DATE_START = "freeGrowingDateStart";
  public static final String FREE_GROWING_DATE_END = "freeGrowingDateEnd";
  public static final String UPDATE_DATE_START = "updateDateStart";
  public static final String UPDATE_DATE_END = "updateDateEnd";
  public static final String CUTTING_PERMIT_ID = "cuttingPermitId";
  public static final String CUT_BLOCK_ID = "cutBlockId";
  public static final String TIMBER_MARK = "timberMark";
  public static final String MAIN_SEARCH_TERM = "mainSearchTerm";
  public static final String LOCATION_CODE = "clientLocationCode";
  public static final String CLIENT_NUMBER = "clientNumber";
  public static final String NOVALUE = "NOVALUE";
}
