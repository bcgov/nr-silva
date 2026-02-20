package ca.bc.gov.restapi.results.common.dto.activity;

import ca.bc.gov.restapi.results.common.SilvaConstants;
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

  @Schema(type = "string")
  private final String fileId;

  @Schema(type = "array", nullable = true)
  private final List<String> clientNumbers;

  @Schema(type = "array", nullable = true)
  private final List<String> openingStatuses;

  @Schema(type = "string", format = "date", nullable = true)
  private final String updateDateStart;

  @Schema(type = "string", format = "date", nullable = true)
  private final String updateDateEnd;

  /**
   * Creates a no-arg instance with all fields set to null, delegating to the all-args constructor
   * to apply defaults.
   */
  public ActivitySearchFiltersDto() {
    this(null, null, null, null, null, null, null, null, null, null, null, null, null);
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
      String updateDateStart,
      String updateDateEnd) {
    this.bases = !CollectionUtils.isEmpty(bases) ? bases : List.of(SilvaConstants.NOVALUE);
    this.techniques =
        !CollectionUtils.isEmpty(techniques) ? techniques : List.of(SilvaConstants.NOVALUE);
    this.methods = !CollectionUtils.isEmpty(methods) ? methods : List.of(SilvaConstants.NOVALUE);
    this.isComplete = isComplete;
    this.objectives =
        !CollectionUtils.isEmpty(objectives) ? objectives : List.of(SilvaConstants.NOVALUE);
    this.fundingSources =
        !CollectionUtils.isEmpty(fundingSources) ? fundingSources : List.of(SilvaConstants.NOVALUE);
    this.orgUnits = !CollectionUtils.isEmpty(orgUnits) ? orgUnits : List.of(SilvaConstants.NOVALUE);
    this.openingCategories =
        !CollectionUtils.isEmpty(openingCategories)
            ? openingCategories
            : List.of(SilvaConstants.NOVALUE);
    this.fileId = Objects.isNull(fileId) ? null : fileId.trim();
    this.clientNumbers =
        !CollectionUtils.isEmpty(clientNumbers) ? clientNumbers : List.of(SilvaConstants.NOVALUE);
    this.openingStatuses =
        !CollectionUtils.isEmpty(openingStatuses)
            ? openingStatuses
            : List.of(SilvaConstants.NOVALUE);
    this.updateDateStart = Objects.isNull(updateDateStart) ? null : updateDateStart.trim();
    this.updateDateEnd = Objects.isNull(updateDateEnd) ? null : updateDateEnd.trim();
  }

  public boolean hasAnyFilter() {
    return !SilvaConstants.NOVALUE.equals(bases.get(0))
        || !SilvaConstants.NOVALUE.equals(techniques.get(0))
        || !SilvaConstants.NOVALUE.equals(methods.get(0))
        || isComplete != null
        || !SilvaConstants.NOVALUE.equals(objectives.get(0))
        || !SilvaConstants.NOVALUE.equals(fundingSources.get(0))
        || !SilvaConstants.NOVALUE.equals(orgUnits.get(0))
        || !SilvaConstants.NOVALUE.equals(openingCategories.get(0))
        || (fileId != null && !fileId.isBlank())
        || !SilvaConstants.NOVALUE.equals(clientNumbers.get(0))
        || !SilvaConstants.NOVALUE.equals(openingStatuses.get(0))
        || (updateDateStart != null && !updateDateStart.isBlank())
        || (updateDateEnd != null && !updateDateEnd.isBlank());
  }
}
