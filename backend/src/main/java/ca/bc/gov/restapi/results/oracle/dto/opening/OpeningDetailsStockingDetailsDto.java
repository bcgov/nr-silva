package ca.bc.gov.restapi.results.oracle.dto.opening;

import lombok.With;

@With
public record OpeningDetailsStockingDetailsDto(
    String stockingStandardUnit,
    Long ssid,
    boolean defaultMof,
    boolean manualEntry,
    Long fspId,
    Long netArea,
    Long soilDisturbancePercent,
    OpeningDetailsBecDto bec,
    Long regenDelay,
    Long freeGrowingLate
) {

}
