package ca.bc.gov.restapi.results.oracle.dto.opening;

import ca.bc.gov.restapi.results.oracle.SilvaOracleConstants;
import java.util.List;
import java.util.Objects;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.BooleanUtils;
import org.springframework.util.CollectionUtils;

/**
 * This record contains all possible filters when using the Opening Search API.
 */
@Slf4j
@Getter
@ToString
public class OpeningSearchFiltersDto {

  private final List<String> orgUnit;
  private final List<String> category;
  private final List<String> statusList;
  private final Boolean myOpenings;
  private final String submittedToFrpa;
  private final String disturbanceDateStart;
  private final String disturbanceDateEnd;
  private final String regenDelayDateStart;
  private final String regenDelayDateEnd;
  private final String freeGrowingDateStart;
  private final String freeGrowingDateEnd;
  private final String updateDateStart;
  private final String updateDateEnd;
  private final String cuttingPermitId;
  private final String cutBlockId;
  private final String timberMark;
  // Main input, it can be one of Opening ID, Opening Number, Timber Mark ID, or File ID
  private final String mainSearchTerm;
  private final String clientLocationCode;
  private final String clientNumber;

  @Setter
  private String requestUserId;

  /**
   * Creates an instance of the search opening filter dto.
   */
  public OpeningSearchFiltersDto(
      List<String> orgUnit,
      List<String> category,
      List<String> statusList,
      Boolean myOpenings,
      Boolean submittedToFrpa,
      String disturbanceDateStart,
      String disturbanceDateEnd,
      String regenDelayDateStart,
      String regenDelayDateEnd,
      String freeGrowingDateStart,
      String freeGrowingDateEnd,
      String updateDateStart,
      String updateDateEnd,
      String cuttingPermitId,
      String cutBlockId,
      String timberMark,
      String clientLocationCode,
      String clientNumber,
      String mainSearchTerm) {
    this.orgUnit = !CollectionUtils.isEmpty(orgUnit) ? orgUnit : List.of(
        SilvaOracleConstants.NOVALUE);
    this.category = !CollectionUtils.isEmpty(category) ? category : List.of(
        SilvaOracleConstants.NOVALUE);
    this.statusList = !CollectionUtils.isEmpty(statusList) ? statusList : List.of(
        SilvaOracleConstants.NOVALUE);
    this.myOpenings = myOpenings;
    this.submittedToFrpa =
        BooleanUtils
            .toString(
                submittedToFrpa,
                "YES",
                "NO",
                "NO"
            );
    this.disturbanceDateStart =
        Objects.isNull(disturbanceDateStart) ? null : disturbanceDateStart.trim();
    this.disturbanceDateEnd = Objects.isNull(disturbanceDateEnd) ? null : disturbanceDateEnd.trim();
    this.regenDelayDateStart =
        Objects.isNull(regenDelayDateStart) ? null : regenDelayDateStart.trim();
    this.regenDelayDateEnd = Objects.isNull(regenDelayDateEnd) ? null : regenDelayDateEnd.trim();
    this.freeGrowingDateStart =
        Objects.isNull(freeGrowingDateStart) ? null : freeGrowingDateStart.trim();
    this.freeGrowingDateEnd = Objects.isNull(freeGrowingDateEnd) ? null : freeGrowingDateEnd.trim();
    this.updateDateStart = Objects.isNull(updateDateStart) ? null : updateDateStart.trim();
    this.updateDateEnd = Objects.isNull(updateDateEnd) ? null : updateDateEnd.trim();
    this.cuttingPermitId =
        Objects.isNull(cuttingPermitId) ? null : cuttingPermitId.toUpperCase().trim();
    this.cutBlockId = Objects.isNull(cutBlockId) ? null : cutBlockId.toUpperCase().trim();
    this.timberMark = Objects.isNull(timberMark) ? null : timberMark.toUpperCase().trim();
    this.mainSearchTerm =
        Objects.isNull(mainSearchTerm) ? null : mainSearchTerm.toUpperCase().trim();
    this.clientLocationCode =
        Objects.isNull(clientLocationCode) ? null : clientLocationCode.trim();
    this.clientNumber = Objects.isNull(clientNumber) ? null : clientNumber.trim();
  }

  public OpeningSearchFiltersDto(){
    this.orgUnit = List.of(SilvaOracleConstants.NOVALUE);
    this.category = List.of(SilvaOracleConstants.NOVALUE);
    this.statusList = List.of(SilvaOracleConstants.NOVALUE);
    this.myOpenings = null;
    this.submittedToFrpa = "NO";
    this.disturbanceDateStart = null;
    this.disturbanceDateEnd = null;
    this.regenDelayDateStart = null;
    this.regenDelayDateEnd = null;
    this.freeGrowingDateStart = null;
    this.freeGrowingDateEnd = null;
    this.updateDateStart = null;
    this.updateDateEnd = null;
    this.cuttingPermitId = null;
    this.cutBlockId = null;
    this.timberMark = null;
    this.mainSearchTerm = null;
    this.clientLocationCode = null;
    this.clientNumber = null;
    this.requestUserId = null;
  }

  /**
   * Define if a property has value.
   *
   * @param prop The property to be checked.
   * @return True if it has, false otherwise.
   */
  public boolean hasValue(String prop) {
    return switch (prop) {
      case SilvaOracleConstants.ORG_UNIT ->
          !Objects.isNull(this.orgUnit) && !this.orgUnit.isEmpty();
      case SilvaOracleConstants.CATEGORY ->
          !Objects.isNull(this.category) && !this.category.isEmpty();
      case SilvaOracleConstants.STATUS_LIST ->
          !Objects.isNull(this.statusList) && !this.statusList.isEmpty();
      case SilvaOracleConstants.MY_OPENINGS -> !Objects.isNull(this.myOpenings);
      case SilvaOracleConstants.SUBMITTED_TO_FRPA -> !Objects.isNull(this.submittedToFrpa);
      case SilvaOracleConstants.DISTURBANCE_DATE_START ->
          !Objects.isNull(this.disturbanceDateStart);
      case SilvaOracleConstants.DISTURBANCE_DATE_END -> !Objects.isNull(this.disturbanceDateEnd);
      case SilvaOracleConstants.REGEN_DELAY_DATE_START -> !Objects.isNull(this.regenDelayDateStart);
      case SilvaOracleConstants.REGEN_DELAY_DATE_END -> !Objects.isNull(this.regenDelayDateEnd);
      case SilvaOracleConstants.FREE_GROWING_DATE_START ->
          !Objects.isNull(this.freeGrowingDateStart);
      case SilvaOracleConstants.FREE_GROWING_DATE_END -> !Objects.isNull(this.freeGrowingDateEnd);
      case SilvaOracleConstants.UPDATE_DATE_START -> !Objects.isNull(this.updateDateStart);
      case SilvaOracleConstants.UPDATE_DATE_END -> !Objects.isNull(this.updateDateEnd);
      case SilvaOracleConstants.CUTTING_PERMIT_ID -> !Objects.isNull(this.cuttingPermitId);
      case SilvaOracleConstants.CUT_BLOCK_ID -> !Objects.isNull(this.cutBlockId);
      case SilvaOracleConstants.TIMBER_MARK -> !Objects.isNull(this.timberMark);
      case SilvaOracleConstants.MAIN_SEARCH_TERM -> !Objects.isNull(this.mainSearchTerm);
      case SilvaOracleConstants.LOCATION_CODE -> !Objects.isNull(this.clientLocationCode);
      case SilvaOracleConstants.CLIENT_NUMBER -> !Objects.isNull(this.clientNumber);
      default -> {
        log.warn("Prop not found {}", prop);
        yield false;
      }
    };
  }

}
