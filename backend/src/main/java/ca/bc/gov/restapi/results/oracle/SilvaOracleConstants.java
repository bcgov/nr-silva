package ca.bc.gov.restapi.results.oracle;

import java.util.Set;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SilvaOracleConstants {

  public static final String ORG_UNIT = "orgUnit";
  public static final String CATEGORY = "category";
  public static final String STATUS_LIST = "statusList";
  public static final String MY_OPENINGS = "myOpenings";
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
  public static final String MAPSHEET_GRID = "mapsheetGrid";
  public static final String MAPSHEET_LETTER = "mapsheetLetter";
  public static final String MAPSHEET_SQUARE = "mapsheetSquare";
  public static final String MAPSHEET_QUAD = "mapsheetQuad";
  public static final String MAPSHEET_SUB_QUAD = "mapsheetSubQuad";
  public static final String OPENING_NUMBER = "openingNumber";
  public static final String LICENSE_NUMBER = "licenseNumber";
  public static final String LICENSEE_OPENING_ID = "licenseeOpeningId";
  public static final String ENTRY_DATE_START = "entryDateStart";
  public static final String ENTRY_DATE_END = "entryDateEnd";
  public static final String CLIENT_NUMBERS = "clientNumbers";
  public static final String IS_CREATED_BY_USER = "isCreatedByUser";
  public static final String OPENING_ID = "openingId";
  public static final String CATEGORIES = "categories";
  public static final String OPENING_STATUSES = "openingStatuses";
  public static final String ORG_UNITS = "orgUnits";

  // Allowed mapsheet grid values for validation
  public static final Set<String> VALID_MAPSHEET_GRID_VALUES =
      Set.of(
          "82", "83", "92", "93", "94", "082", "083", "092", "093", "094", "102", "103", "104",
          "105", "114");

  // Allowed mapsheet letters (A-P and W)
  public static final Set<Character> VALID_MAPSHEET_LETTERS =
      Set.of('A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'W');
}
