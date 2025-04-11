package ca.bc.gov.restapi.results.oracle.dto.opening;

import lombok.With;

@With
public record OpeningDetailsTombstoneOverviewDto(
    Long openingId,
    OpeningDetailsTombstoneDto tombstone,
    OpeningDetailsOverviewDto overview
) {

}
