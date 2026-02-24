package ca.bc.gov.restapi.results.common.dto.activity;

import ca.bc.gov.restapi.results.common.SilvaConstants;
import ca.bc.gov.restapi.results.common.util.StringUtil;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import java.util.Objects;
import lombok.Getter;
import lombok.ToString;
import org.springframework.util.CollectionUtils;

/** This class contains all possible filters when using the Disturbance Search API. */
@Getter
@ToString
public class DisturbanceSearchFilterDto {

  @Schema(type = "array", nullable = true)
  private final List<String> disturbances;

  @Schema(type = "array", nullable = true)
  private final List<String> silvSystems;

  @Schema(type = "array", nullable = true)
  private final List<String> variants;

  @Schema(type = "array", nullable = true)
  private final List<String> cutPhases;

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
  public DisturbanceSearchFilterDto() {
    this(null, null, null, null, null, null, null, null, null, null, null);
  }

  /** Creates an instance of the disturbance search filter dto. */
  public DisturbanceSearchFilterDto(
      List<String> disturbances,
      List<String> silvSystems,
      List<String> variants,
      List<String> cutPhases,
      List<String> orgUnits,
      List<String> openingCategories,
      String fileId,
      List<String> clientNumbers,
      List<String> openingStatuses,
      String updateDateStart,
      String updateDateEnd) {
    this.disturbances =
        !CollectionUtils.isEmpty(disturbances)
            ? StringUtil.toUpperCase(disturbances)
            : List.of(SilvaConstants.NOVALUE);
    this.silvSystems =
        !CollectionUtils.isEmpty(silvSystems)
            ? StringUtil.toUpperCase(silvSystems)
            : List.of(SilvaConstants.NOVALUE);
    this.variants =
        !CollectionUtils.isEmpty(variants)
            ? StringUtil.toUpperCase(variants)
            : List.of(SilvaConstants.NOVALUE);
    this.cutPhases =
        !CollectionUtils.isEmpty(cutPhases)
            ? StringUtil.toUpperCase(cutPhases)
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
    return !SilvaConstants.NOVALUE.equals(disturbances.get(0))
        || !SilvaConstants.NOVALUE.equals(silvSystems.get(0))
        || !SilvaConstants.NOVALUE.equals(variants.get(0))
        || !SilvaConstants.NOVALUE.equals(cutPhases.get(0))
        || !SilvaConstants.NOVALUE.equals(orgUnits.get(0))
        || !SilvaConstants.NOVALUE.equals(openingCategories.get(0))
        || (fileId != null && !fileId.isBlank())
        || !SilvaConstants.NOVALUE.equals(clientNumbers.get(0))
        || !SilvaConstants.NOVALUE.equals(openingStatuses.get(0))
        || (updateDateStart != null && !updateDateStart.isBlank())
        || (updateDateEnd != null && !updateDateEnd.isBlank());
  }
}
