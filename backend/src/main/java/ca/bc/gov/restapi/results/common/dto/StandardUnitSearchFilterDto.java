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

  @Schema(type = "string", nullable = true)
  private final String standardsUnitId; // VARCHAR2(4)

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
  private final String becSeral;

  @Schema(type = "string", format = "date", nullable = true)
  private final String updateDateStart;

  @Schema(type = "string", format = "date", nullable = true)
  private final String updateDateEnd;

  public StandardUnitSearchFilterDto() {
    this(null, null, null, null, null, null, null, null, null, null, null, null);
  }

  public StandardUnitSearchFilterDto(
      String standardsUnitId,
      List<String> preferredSpecies,
      List<String> orgUnits,
      List<String> clientNumbers,
      String bgcZone,
      String bgcSubZone,
      String bgcVariant,
      String bgcPhase,
      String becSiteSeries,
      String becSeral,
      String updateDateStart,
      String updateDateEnd) {
    this.standardsUnitId = Objects.isNull(standardsUnitId) ? null : standardsUnitId.trim();
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
    this.becSeral = Objects.isNull(becSeral) ? null : becSeral.trim().toUpperCase();
    this.updateDateStart = Objects.isNull(updateDateStart) ? null : updateDateStart.trim();
    this.updateDateEnd = Objects.isNull(updateDateEnd) ? null : updateDateEnd.trim();
  }

  public boolean hasAnyFilter() {
    return (standardsUnitId != null && !standardsUnitId.isBlank())
        || StringUtil.isFilterSet(preferredSpecies)
        || StringUtil.isFilterSet(orgUnits)
        || StringUtil.isFilterSet(clientNumbers)
        || (bgcZone != null && !bgcZone.isBlank())
        || (bgcSubZone != null && !bgcSubZone.isBlank())
        || (bgcVariant != null && !bgcVariant.isBlank())
        || (bgcPhase != null && !bgcPhase.isBlank())
        || (becSiteSeries != null && !becSiteSeries.isBlank())
        || (becSeral != null && !becSeral.isBlank())
        || (updateDateStart != null && !updateDateStart.isBlank())
        || (updateDateEnd != null && !updateDateEnd.isBlank());
  }
}
