package ca.bc.gov.restapi.results.common.dto.activity;

import ca.bc.gov.restapi.results.common.SilvaConstants;
import ca.bc.gov.restapi.results.common.util.StringUtil;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import java.util.Objects;
import lombok.Getter;
import lombok.ToString;
import org.springframework.util.CollectionUtils;

/** This class contains all possible filters when using the Activity Search API. */
@Getter
@ToString
public class ActivitySearchFiltersDto {

  @Schema(type = "array", nullable = true)
  private final List<String> bases;

  @Schema(type = "array", nullable = true)
  private final List<String> techniques;

  @Schema(type = "array", nullable = true)
  private final List<String> methods;

  @Schema(type = "boolean", nullable = true)
  private final Boolean isComplete;

  @Schema(type = "array", nullable = true)
  private final List<String> objectives;

  @Schema(type = "array", nullable = true)
  private final List<String> fundingSources;

  @Schema(type = "array", nullable = true)
  private final List<String> orgUnits;

  @Schema(type = "array", nullable = true)
  private final List<String> openingCategories;

  @Schema(type = "string", nullable = true)
  private final String fileId;

  @Schema(type = "array", nullable = true)
  private final List<String> clientNumbers;

  @Schema(type = "array", nullable = true)
  private final List<String> openingStatuses;

  @Schema(type = "string", nullable = true)
  private final String intraAgencyNumber;

  @Schema(type = "string", format = "date", nullable = true)
  private final String updateDateStart;

  @Schema(type = "string", format = "date", nullable = true)
  private final String updateDateEnd;

  /**
   * Creates a no-arg instance with all fields set to null, delegating to the all-args constructor
   * to apply defaults.
   */
  public ActivitySearchFiltersDto() {
    this(null, null, null, null, null, null, null, null, null, null, null, null, null, null);
  }

  /** Creates an instance of the activity search filter dto. */
  public ActivitySearchFiltersDto(
      List<String> bases,
      List<String> techniques,
      List<String> methods,
      Boolean isComplete,
      List<String> objectives,
      List<String> fundingSources,
      List<String> orgUnits,
      List<String> openingCategories,
      String fileId,
      List<String> clientNumbers,
      List<String> openingStatuses,
      String intraAgencyNumber,
      String updateDateStart,
      String updateDateEnd) {
    this.bases =
        !CollectionUtils.isEmpty(bases)
            ? StringUtil.toUpperCase(bases)
            : List.of(SilvaConstants.NOVALUE);
    this.techniques =
        !CollectionUtils.isEmpty(techniques)
            ? StringUtil.toUpperCase(techniques)
            : List.of(SilvaConstants.NOVALUE);
    this.methods =
        !CollectionUtils.isEmpty(methods)
            ? StringUtil.toUpperCase(methods)
            : List.of(SilvaConstants.NOVALUE);
    this.isComplete = isComplete;
    this.objectives =
        !CollectionUtils.isEmpty(objectives)
            ? StringUtil.toUpperCase(objectives)
            : List.of(SilvaConstants.NOVALUE);
    this.fundingSources =
        !CollectionUtils.isEmpty(fundingSources)
            ? StringUtil.toUpperCase(fundingSources)
            : List.of(SilvaConstants.NOVALUE);
    this.orgUnits =
        !CollectionUtils.isEmpty(orgUnits)
            ? StringUtil.toUpperCase(orgUnits)
            : List.of(SilvaConstants.NOVALUE);
    this.openingCategories =
        !CollectionUtils.isEmpty(openingCategories)
            ? StringUtil.toUpperCase(openingCategories)
            : List.of(SilvaConstants.NOVALUE);
    this.fileId = Objects.isNull(fileId) ? null : fileId.trim();
    this.intraAgencyNumber = Objects.isNull(intraAgencyNumber) ? null : intraAgencyNumber.trim();
    this.clientNumbers =
        !CollectionUtils.isEmpty(clientNumbers) ? clientNumbers : List.of(SilvaConstants.NOVALUE);
    this.openingStatuses =
        !CollectionUtils.isEmpty(openingStatuses)
            ? StringUtil.toUpperCase(openingStatuses)
            : List.of(SilvaConstants.NOVALUE);
    this.updateDateStart = Objects.isNull(updateDateStart) ? null : updateDateStart.trim();
    this.updateDateEnd = Objects.isNull(updateDateEnd) ? null : updateDateEnd.trim();
  }

  public boolean hasAnyFilter() {
    return StringUtil.isFilterSet(bases)
        || StringUtil.isFilterSet(techniques)
        || StringUtil.isFilterSet(methods)
        || isComplete != null
        || StringUtil.isFilterSet(objectives)
        || StringUtil.isFilterSet(fundingSources)
        || StringUtil.isFilterSet(orgUnits)
        || StringUtil.isFilterSet(openingCategories)
        || (fileId != null && !fileId.isBlank())
        || StringUtil.isFilterSet(clientNumbers)
        || StringUtil.isFilterSet(openingStatuses)
        || (intraAgencyNumber != null && !intraAgencyNumber.isBlank())
        || (updateDateStart != null && !updateDateStart.isBlank())
        || (updateDateEnd != null && !updateDateEnd.isBlank());
  }
}
