package ca.bc.gov.restapi.results.oracle.dto;

import ca.bc.gov.restapi.results.oracle.SilvaOracleConstants;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

/** This record contains all possible filters when using the Opening Search API. */
@Slf4j
@Getter
@ToString
public class OpeningSearchFiltersDto {
  private final String orgUnit;
  private final String category;
  private final List<String> statusList;
  private final Boolean myOpenings;
  private final Boolean submittedToFrpa;
  private final LocalDateTime disturbanceDateStart;
  private final LocalDateTime disturbanceDateEnd;
  private final LocalDateTime regenDelayDateStart;
  private final LocalDateTime regenDelayDateEnd;
  private final LocalDateTime freeGrowingDateStart;
  private final LocalDateTime freeGrowingDateEnd;
  private final LocalDateTime updateDateStart;
  private final LocalDateTime updateDateEnd;
  private final String cuttingPermitId;
  private final String cutBlockId;
  private final String timberMark;
  // Main input, it can be one of Opening ID, Opening Number, Timber Mark ID, or File ID
  private final String mainSearchTerm;

  @Setter
  private String requestUserId;

  /** Creates an instance of the search opening filter dto. */
  public OpeningSearchFiltersDto(
      String orgUnit,
      String category,
      List<String> statusList,
      Boolean myOpenings,
      Boolean submittedToFrpa,
      LocalDateTime disturbanceDateStart,
      LocalDateTime disturbanceDateEnd,
      LocalDateTime regenDelayDateStart,
      LocalDateTime regenDelayDateEnd,
      LocalDateTime freeGrowingDateStart,
      LocalDateTime freeGrowingDateEnd,
      LocalDateTime updateDateStart,
      LocalDateTime updateDateEnd,
      String cuttingPermitId,
      String cutBlockId,
      String timberMark,
      String mainSearchTerm
  ) {
    this.orgUnit = Objects.isNull(orgUnit) ? null : orgUnit.toUpperCase().trim();
    this.category = Objects.isNull(category) ? null : category.toUpperCase().trim();
    this.statusList = Objects.isNull(statusList) || statusList.isEmpty() ? null : statusList;
    this.myOpenings = myOpenings;
    this.submittedToFrpa = submittedToFrpa;
    this.disturbanceDateStart =
        Objects.isNull(disturbanceDateStart) ? null : disturbanceDateStart;
    this.disturbanceDateEnd = Objects.isNull(disturbanceDateEnd) ? null : disturbanceDateEnd;
    this.regenDelayDateStart =
        Objects.isNull(regenDelayDateStart) ? null : regenDelayDateStart;
    this.regenDelayDateEnd = Objects.isNull(regenDelayDateEnd) ? null : regenDelayDateEnd;
    this.freeGrowingDateStart =
        Objects.isNull(freeGrowingDateStart) ? null : freeGrowingDateStart;
    this.freeGrowingDateEnd = Objects.isNull(freeGrowingDateEnd) ? null : freeGrowingDateEnd;
    this.updateDateStart = Objects.isNull(updateDateStart) ? null : updateDateStart;
    this.updateDateEnd = Objects.isNull(updateDateEnd) ? null : updateDateEnd;
    this.cuttingPermitId =
        Objects.isNull(cuttingPermitId) ? null : cuttingPermitId.toUpperCase().trim();
    this.cutBlockId = Objects.isNull(cutBlockId) ? null : cutBlockId.toUpperCase().trim();
    this.timberMark = Objects.isNull(timberMark) ? null : timberMark.toUpperCase().trim();
    this.mainSearchTerm =
        Objects.isNull(mainSearchTerm) ? null : mainSearchTerm.toUpperCase().trim();
  }

  /**
   * Define if a property has value.
   *
   * @param prop The property to be checked.
   * @return True if it has, false otherwise.
   */
  public boolean hasValue(String prop) {
    return switch (prop) {
      case SilvaOracleConstants.ORG_UNIT -> !Objects.isNull(this.orgUnit);
      case SilvaOracleConstants.CATEGORY -> !Objects.isNull(this.category);
      case SilvaOracleConstants.STATUS_LIST -> !this.statusList.isEmpty();
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
      default -> {
        log.warn("Prop not found {}", prop);
        yield false;
      }
    };
  }
}
