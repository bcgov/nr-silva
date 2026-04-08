package ca.bc.gov.restapi.results.common.dto;

import ca.bc.gov.restapi.results.common.SilvaConstants;
import ca.bc.gov.restapi.results.common.util.StringUtil;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import java.util.Objects;
import lombok.Getter;
import lombok.ToString;
import org.springframework.util.CollectionUtils;

/** This class contains all possible filters when using the Standard Unit Search API. */
@Getter
@ToString
public class StandardUnitSearchFilterDto {

  @Schema(type = "integer", format = "int64", nullable = true)
  private final Long standardsRegimeId;

  @Schema(type = "array", nullable = true)
  private final List<String> preferredSpecies;

  @Schema(type = "array", nullable = true)
  private final List<String> orgUnits;

  @Schema(type = "array", nullable = true)
  private final List<String> clientNumbers;

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
  private final String becSiteType; // aka Site Phase

  @Schema(type = "string", nullable = true)
  private final String becSeral;

  @Schema(type = "string", format = "date", nullable = true)
  private final String updateDateStart;

  @Schema(type = "string", format = "date", nullable = true)
  private final String updateDateEnd;

  public StandardUnitSearchFilterDto() {
    this(null, null, null, null, null, null, null, null, null, null, null, null, null);
  }

  public StandardUnitSearchFilterDto(
      Long standardsRegimeId,
      List<String> preferredSpecies,
      List<String> orgUnits,
      List<String> clientNumbers,
      String bgcZone,
      String bgcSubZone,
      String bgcVariant,
      String bgcPhase,
      String becSiteSeries,
      String becSiteType,
      String becSeral,
      String updateDateStart,
      String updateDateEnd) {
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
    this.bgcZone = Objects.isNull(bgcZone) ? null : bgcZone.trim().toUpperCase();
    this.bgcSubZone = Objects.isNull(bgcSubZone) ? null : bgcSubZone.trim().toUpperCase();
    this.bgcVariant = Objects.isNull(bgcVariant) ? null : bgcVariant.trim().toUpperCase();
    this.bgcPhase = Objects.isNull(bgcPhase) ? null : bgcPhase.trim().toUpperCase();
    this.becSiteSeries = Objects.isNull(becSiteSeries) ? null : becSiteSeries.trim();
    this.becSiteType = Objects.isNull(becSiteType) ? null : becSiteType.trim();
    this.becSeral = Objects.isNull(becSeral) ? null : becSeral.trim().toUpperCase();
    this.updateDateStart = Objects.isNull(updateDateStart) ? null : updateDateStart.trim();
    this.updateDateEnd = Objects.isNull(updateDateEnd) ? null : updateDateEnd.trim();
  }

  public boolean hasAnyFilter() {
    return standardsRegimeId != null
        || StringUtil.isFilterSet(preferredSpecies)
        || StringUtil.isFilterSet(orgUnits)
        || StringUtil.isFilterSet(clientNumbers)
        || (bgcZone != null && !bgcZone.isBlank())
        || (bgcSubZone != null && !bgcSubZone.isBlank())
        || (bgcVariant != null && !bgcVariant.isBlank())
        || (bgcPhase != null && !bgcPhase.isBlank())
        || (becSiteSeries != null && !becSiteSeries.isBlank())
        || (becSiteType != null && !becSiteType.isBlank())
        || (becSeral != null && !becSeral.isBlank())
        || (updateDateStart != null && !updateDateStart.isBlank())
        || (updateDateEnd != null && !updateDateEnd.isBlank());
  }
}
