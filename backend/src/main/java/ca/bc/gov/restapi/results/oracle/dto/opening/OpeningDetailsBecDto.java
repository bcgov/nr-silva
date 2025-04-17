package ca.bc.gov.restapi.results.oracle.dto.opening;

public record OpeningDetailsBecDto(
    String becZoneCode,
    String becSubzoneCode,
    String becVariant,
    String becPhase,
    String becSiteSeries,
    String becSiteType,
    String becSeral
) {

}
