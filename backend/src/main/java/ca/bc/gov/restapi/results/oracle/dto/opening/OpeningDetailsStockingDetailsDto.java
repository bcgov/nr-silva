package ca.bc.gov.restapi.results.oracle.dto.opening;

import lombok.With;

@With
public record OpeningDetailsStockingDetailsDto(
    String stockingStandardUnit,
    Long ssid,
    boolean defaultMof,
    boolean manualEntry,
    Long fspId,
    Float netArea,
    Float soilDisturbancePercent,
    OpeningDetailsBecDto bec,
    Long regenDelay,
    Long freeGrowingLate,
    Long freeGrowingEarly,
    String additionalStandards
) {

}
