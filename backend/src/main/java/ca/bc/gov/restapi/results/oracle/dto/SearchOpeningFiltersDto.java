package ca.bc.gov.restapi.results.oracle.dto;

import java.util.Objects;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

/** This record contains all possible filters when using the Opening Search API. */
@Slf4j
@Getter
public class SearchOpeningFiltersDto {
  private final String orgUnit;
  private final String category;
  private final String status;
  private final String userId;
  private final Boolean submittedToFrpa;
  private final String disturbanceDateStart;
  private final String disturbanceDateEnd;
  private final String regenDelayDateStart;
  private final String regenDelayDateEnd;
  private final String freeGrowingDateStart;
  private final String freeGrowingDateEnd;
  private final String updateDateStart;
  private final String updateDateEnd;
  private final String number;

  public static final String ORG_UNIT = "orgUnit";
  public static final String CATEGORY = "category";
  public static final String STATUS = "status";
  public static final String USER_ID = "userId";
  public static final String SUBMITTED_TO_FRPA = "submittedToFrpa";
  public static final String DISTURBANCE_DATE_START = "disturbanceDateStart";
  public static final String DISTURBANCE_DATE_END = "disturbanceDateEnd";
  public static final String REGEN_DELAY_DATE_START = "regenDelayDateStart";
  public static final String REGEN_DELAY_DATE_END = "regenDelayDateEnd";
  public static final String FREE_GROWING_DATE_START = "freeGrowingDateStart";
  public static final String FREE_GROWING_DATE_END = "freeGrowingDateEnd";
  public static final String UPDATE_DATE_START = "updateDateStart";
  public static final String UPDATE_DATE_END = "updateDateEnd";
  public static final String NUMBER = "number";

  /** Creates an instance of the search opening filter dto. */
  public SearchOpeningFiltersDto(
      String orgUnit,
      String category,
      String status,
      String userId,
      Boolean submittedToFrpa,
      String disturbanceDateStart,
      String disturbanceDateEnd,
      String regenDelayDateStart,
      String regenDelayDateEnd,
      String freeGrowingDateStart,
      String freeGrowingDateEnd,
      String updateDateStart,
      String updateDateEnd,
      String number) {
    this.orgUnit = Objects.isNull(orgUnit) ? null : orgUnit.toUpperCase().trim();
    this.category = Objects.isNull(category) ? null : category.toUpperCase().trim();
    this.status = Objects.isNull(status) ? null : status.toUpperCase().trim();
    this.userId = Objects.isNull(userId) ? null : userId.toUpperCase().trim();
    this.submittedToFrpa = submittedToFrpa;
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
    this.number = Objects.isNull(number) ? null : number.toUpperCase().trim();
  }

  /**
   * Define if a property has value.
   *
   * @param prop The property to be checked.
   * @return True if it has, false otherwise.
   */
  public boolean hasValue(String prop) {
    switch (prop) {
      case ORG_UNIT:
        return !Objects.isNull(this.orgUnit);
      case CATEGORY:
        return !Objects.isNull(this.category);
      case STATUS:
        return !Objects.isNull(this.status);
      case USER_ID:
        return !Objects.isNull(this.userId);
      case SUBMITTED_TO_FRPA:
        return !Objects.isNull(this.submittedToFrpa);
      case DISTURBANCE_DATE_START:
        return !Objects.isNull(this.disturbanceDateStart);
      case DISTURBANCE_DATE_END:
        return !Objects.isNull(this.disturbanceDateEnd);
      case REGEN_DELAY_DATE_START:
        return !Objects.isNull(this.regenDelayDateStart);
      case REGEN_DELAY_DATE_END:
        return !Objects.isNull(this.regenDelayDateEnd);
      case FREE_GROWING_DATE_START:
        return !Objects.isNull(this.freeGrowingDateStart);
      case FREE_GROWING_DATE_END:
        return !Objects.isNull(this.freeGrowingDateEnd);
      case UPDATE_DATE_START:
        return !Objects.isNull(this.updateDateStart);
      case UPDATE_DATE_END:
        return !Objects.isNull(this.updateDateEnd);
      case NUMBER:
        return !Objects.isNull(this.number);
      default: {
        log.warn("Prop not found {}", prop);
        return false;
      }
    }
  }
}
