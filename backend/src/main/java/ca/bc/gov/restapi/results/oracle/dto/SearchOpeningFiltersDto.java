package ca.bc.gov.restapi.results.oracle.dto;

import java.time.LocalDateTime;
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
  private final LocalDateTime disturbanceDateStart;
  private final LocalDateTime disturbanceDateEnd;
  private final LocalDateTime regenDelayDateStart;
  private final LocalDateTime regenDelayDateEnd;
  private final LocalDateTime freeGrowingDateStart;
  private final LocalDateTime freeGrowingDateEnd;
  private final LocalDateTime updateDateStart;
  private final LocalDateTime updateDateEnd;
  private final String number;

  public final String _ORG_UNIT = "orgUnit";
  public final String _CATEGORY = "category";
  public final String _STATUS = "status";
  public final String _USER_ID = "userId";
  public final String _SUBMITTED_TO_FRPA = "submittedToFrpa";
  public final String _DISTURBANCE_DATE_START = "disturbanceDateStart";
  public final String _DISTURBANCE_DATE_END = "disturbanceDateEnd";
  public final String _REGEN_DELAY_DATE_START = "regenDelayDateStart";
  public final String _REGEN_DELAY_DATE_END = "regenDelayDateEnd";
  public final String _FREE_GROWING_DATE_START = "freeGrowingDateStart";
  public final String _FREE_GROWING_DATE_END = "freeGrowingDateEnd";
  public final String _UPDATE_DATE_START = "updateDateStart";
  public final String _UPDATE_DATE_END = "updateDateEnd";
  public final String _NUMBER = "number";

  /** Creates an instance of the search opening filter dto. */
  public SearchOpeningFiltersDto(
      String orgUnit,
      String category,
      String status,
      String userId,
      Boolean submittedToFrpa,
      LocalDateTime disturbanceDateStart,
      LocalDateTime disturbanceDateEnd,
      LocalDateTime regenDelayDateStart,
      LocalDateTime regenDelayDateEnd,
      LocalDateTime freeGrowingDateStart,
      LocalDateTime freeGrowingDateEnd,
      LocalDateTime updateDateStart,
      LocalDateTime updateDateEnd,
      String number) {
    this.orgUnit = Objects.isNull(orgUnit) ? null : orgUnit.toUpperCase().trim();
    this.category = Objects.isNull(category) ? null : category.toUpperCase().trim();
    this.status = Objects.isNull(status) ? null : status.toUpperCase().trim();
    this.userId = Objects.isNull(userId) ? null : userId.toUpperCase().trim();
    this.submittedToFrpa = submittedToFrpa;
    this.disturbanceDateStart = disturbanceDateStart;
    this.disturbanceDateEnd = disturbanceDateEnd;
    this.regenDelayDateStart = regenDelayDateStart;
    this.regenDelayDateEnd = regenDelayDateEnd;
    this.freeGrowingDateStart = freeGrowingDateStart;
    this.freeGrowingDateEnd = freeGrowingDateEnd;
    this.updateDateStart = updateDateStart;
    this.updateDateEnd = updateDateEnd;
    this.number = Objects.isNull(number) ? null : number.toUpperCase().trim();
  }

  public boolean hasValue(String prop) {
    switch (prop) {
      case _ORG_UNIT:
        return !Objects.isNull(this.orgUnit);
      case _CATEGORY:
        return !Objects.isNull(this.category);
      case _STATUS:
        return !Objects.isNull(this.status);
      case _USER_ID:
        return !Objects.isNull(this.userId);
      case _SUBMITTED_TO_FRPA:
        return !Objects.isNull(this.submittedToFrpa);
      case _DISTURBANCE_DATE_START:
        return !Objects.isNull(this.disturbanceDateStart);
      case _DISTURBANCE_DATE_END:
        return !Objects.isNull(this.disturbanceDateEnd);
      case _REGEN_DELAY_DATE_START:
        return !Objects.isNull(this.regenDelayDateStart);
      case _REGEN_DELAY_DATE_END:
        return !Objects.isNull(this.regenDelayDateEnd);
      case _FREE_GROWING_DATE_START:
        return !Objects.isNull(this.freeGrowingDateStart);
      case _FREE_GROWING_DATE_END:
        return !Objects.isNull(this.freeGrowingDateEnd);
      case _UPDATE_DATE_START:
        return !Objects.isNull(this.updateDateStart);
      case _UPDATE_DATE_END:
        return !Objects.isNull(this.updateDateEnd);
      case _NUMBER:
        return !Objects.isNull(this.number);
      default:
        {
          log.warn("Prop not found {}", prop);
          return false;
        }
    }
  }
}
