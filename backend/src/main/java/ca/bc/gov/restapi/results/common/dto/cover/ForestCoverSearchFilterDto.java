package ca.bc.gov.restapi.results.common.dto.cover;

import ca.bc.gov.restapi.results.common.SilvaConstants;
import ca.bc.gov.restapi.results.common.util.StringUtil;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import lombok.Getter;
import lombok.ToString;
import org.springframework.util.CollectionUtils;

/** This class contains all possible filters when using the Forest Cover Search API. */
@Getter
@ToString
public class ForestCoverSearchFilterDto {

  @Schema(type = "integer", format = "int64", nullable = true)
  private final Long openingId;

  @Schema(type = "array", nullable = true)
  private final List<String> stockingStatuses;

  @Schema(type = "array", nullable = true)
  private final List<String> stockingTypes;

  @Schema(type = "array", nullable = true)
  private final List<String> damageAgents;

  @Schema(type = "array", nullable = true)
  private final List<String> openingStatuses;

  @Schema(type = "string")
  private final String fileId;

  @Schema(type = "array", nullable = true)
  private final List<String> orgUnits;

  @Schema(type = "array", nullable = true)
  private final List<String> openingCategories;

  @Schema(type = "string", format = "date", nullable = true)
  private final String updateDateStart;

  @Schema(type = "string", format = "date", nullable = true)
  private final String updateDateEnd;

  /**
   * Creates a no-arg instance with all fields set to null, delegating to the all-args constructor
   * to apply defaults.
   */
  public ForestCoverSearchFilterDto() {
    this(null, null, null, null, null, null, null, null, null, null);
  }

  /** Creates an instance of the forest cover search filter dto. */
  public ForestCoverSearchFilterDto(
      Long openingId,
      List<String> stockingStatuses,
      List<String> stockingTypes,
      List<String> damageAgents,
      List<String> openingStatuses,
      String fileId,
      List<String> orgUnits,
      List<String> openingCategories,
      String updateDateStart,
      String updateDateEnd) {
    this.openingId = openingId;
    this.stockingStatuses =
        !CollectionUtils.isEmpty(stockingStatuses)
            ? StringUtil.toUpperCase(stockingStatuses)
            : List.of(SilvaConstants.NOVALUE);
    this.stockingTypes =
        !CollectionUtils.isEmpty(stockingTypes)
            ? StringUtil.toUpperCase(stockingTypes)
            : List.of(SilvaConstants.NOVALUE);
    this.damageAgents =
        !CollectionUtils.isEmpty(damageAgents)
            ? StringUtil.toUpperCase(damageAgents)
            : List.of(SilvaConstants.NOVALUE);
    this.openingStatuses =
        !CollectionUtils.isEmpty(openingStatuses)
            ? StringUtil.toUpperCase(openingStatuses)
            : List.of(SilvaConstants.NOVALUE);
    this.fileId = StringUtil.nullIfBlank(fileId == null ? null : fileId.trim());
    this.orgUnits =
        !CollectionUtils.isEmpty(orgUnits)
            ? StringUtil.toUpperCase(orgUnits)
            : List.of(SilvaConstants.NOVALUE);
    this.openingCategories =
        !CollectionUtils.isEmpty(openingCategories)
            ? StringUtil.toUpperCase(openingCategories)
            : List.of(SilvaConstants.NOVALUE);
    this.updateDateStart =
        StringUtil.nullIfBlank(updateDateStart == null ? null : updateDateStart.trim());
    this.updateDateEnd =
        StringUtil.nullIfBlank(updateDateEnd == null ? null : updateDateEnd.trim());
  }

  public boolean hasAnyFilter() {
    return openingId != null
        || StringUtil.isFilterSet(stockingStatuses)
        || StringUtil.isFilterSet(stockingTypes)
        || StringUtil.isFilterSet(damageAgents)
        || StringUtil.isFilterSet(openingStatuses)
        || (fileId != null && !fileId.isBlank())
        || StringUtil.isFilterSet(orgUnits)
        || StringUtil.isFilterSet(openingCategories)
        || (updateDateStart != null && !updateDateStart.isBlank())
        || (updateDateEnd != null && !updateDateEnd.isBlank());
  }
}
