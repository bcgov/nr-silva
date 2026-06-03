package ca.bc.gov.restapi.results.common.dto;

import ca.bc.gov.restapi.results.common.SilvaConstants;
import ca.bc.gov.restapi.results.common.enums.YesNoEnum;
import ca.bc.gov.restapi.results.common.util.StringUtil;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import lombok.Getter;
import lombok.ToString;
import org.springframework.util.CollectionUtils;

/** Filter DTO for the Stocking Standards (Standards Regime) search API. */
@Getter
@ToString
public class StockingStandardsSearchFilterDto {

  @Schema(type = "integer", format = "int64", nullable = true)
  private final Long standardsRegimeId;

  @Schema(type = "array", nullable = true)
  private final List<String> preferredSpecies;

  @Schema(type = "array", nullable = true)
  private final List<String> orgUnits;

  @Schema(type = "array", nullable = true)
  private final List<String> clientNumbers;

  @Schema(type = "string", nullable = true)
  private final String fspId;

  @Schema(type = "string", nullable = true)
  private final String bgcZone;

  @Schema(type = "string", nullable = true)
  private final String bgcSubZone;

  @Schema(type = "string", nullable = true)
  private final String bgcVariant;

  @Schema(type = "string", nullable = true)
  private final String bgcPhase;

  @Schema(type = "string", nullable = true)
  private final String becSiteSeries;

  @Schema(type = "string", nullable = true)
  private final String becSiteType;

  @Schema(type = "string", nullable = true)
  private final String becSeral;

  @Schema(type = "string", format = "date", nullable = true)
  private final String approvedDateStart;

  @Schema(type = "string", format = "date", nullable = true)
  private final String approvedDateEnd;

  @Schema(type = "boolean", nullable = true)
  private final Boolean defaultStandardsInd;

  public StockingStandardsSearchFilterDto() {
    this(null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);
  }

  public StockingStandardsSearchFilterDto(
      Long standardsRegimeId,
      List<String> preferredSpecies,
      List<String> orgUnits,
      List<String> clientNumbers,
      String fspId,
      String bgcZone,
      String bgcSubZone,
      String bgcVariant,
      String bgcPhase,
      String becSiteSeries,
      String becSiteType,
      String becSeral,
      String approvedDateStart,
      String approvedDateEnd,
      Boolean defaultStandardsInd) {
    this.standardsRegimeId = standardsRegimeId;
    this.preferredSpecies =
        !CollectionUtils.isEmpty(preferredSpecies)
            ? StringUtil.toUpperCase(preferredSpecies)
            : List.of(SilvaConstants.NOVALUE);
    this.orgUnits =
        !CollectionUtils.isEmpty(orgUnits)
            ? StringUtil.toUpperCase(orgUnits)
            : List.of(SilvaConstants.NOVALUE);
    this.clientNumbers =
        !CollectionUtils.isEmpty(clientNumbers)
            ? StringUtil.toUpperCase(clientNumbers)
            : List.of(SilvaConstants.NOVALUE);
    this.fspId = StringUtil.nullIfBlank(fspId == null ? null : fspId.trim());
    this.bgcZone = StringUtil.nullIfBlank(bgcZone == null ? null : bgcZone.trim().toUpperCase());
    this.bgcSubZone =
        StringUtil.nullIfBlank(bgcSubZone == null ? null : bgcSubZone.trim().toUpperCase());
    this.bgcVariant =
        StringUtil.nullIfBlank(bgcVariant == null ? null : bgcVariant.trim().toUpperCase());
    this.bgcPhase = StringUtil.nullIfBlank(bgcPhase == null ? null : bgcPhase.trim().toUpperCase());
    this.becSiteSeries =
        StringUtil.nullIfBlank(becSiteSeries == null ? null : becSiteSeries.trim());
    this.becSiteType = StringUtil.nullIfBlank(becSiteType == null ? null : becSiteType.trim());
    this.becSeral = StringUtil.nullIfBlank(becSeral == null ? null : becSeral.trim().toUpperCase());
    this.approvedDateStart =
        StringUtil.nullIfBlank(approvedDateStart == null ? null : approvedDateStart.trim());
    this.approvedDateEnd =
        StringUtil.nullIfBlank(approvedDateEnd == null ? null : approvedDateEnd.trim());
    this.defaultStandardsInd = defaultStandardsInd;
  }

  /**
   * Returns {@link YesNoEnum#YES} value ('Y'), {@link YesNoEnum#NO} value ('N'), or null.
   * Used by Oracle native queries to avoid ORA-01722: Oracle JDBC binds Java {@link Boolean}
   * as numeric 1/0, causing type mismatch in string comparisons.
   */
  public String getDefaultStandardsIndStr() {
    if (defaultStandardsInd == null) return null;
    return (Boolean.TRUE.equals(defaultStandardsInd) ? YesNoEnum.YES : YesNoEnum.NO).value();
  }

  public boolean hasAnyFilter() {
    return standardsRegimeId != null
        || StringUtil.isFilterSet(preferredSpecies)
        || StringUtil.isFilterSet(orgUnits)
        || StringUtil.isFilterSet(clientNumbers)
        || (fspId != null && !fspId.isBlank())
        || (bgcZone != null && !bgcZone.isBlank())
        || (bgcSubZone != null && !bgcSubZone.isBlank())
        || (bgcVariant != null && !bgcVariant.isBlank())
        || (bgcPhase != null && !bgcPhase.isBlank())
        || (becSiteSeries != null && !becSiteSeries.isBlank())
        || (becSiteType != null && !becSiteType.isBlank())
        || (becSeral != null && !becSeral.isBlank())
        || (approvedDateStart != null && !approvedDateStart.isBlank())
        || (approvedDateEnd != null && !approvedDateEnd.isBlank())
        || defaultStandardsInd != null;
  }
}
