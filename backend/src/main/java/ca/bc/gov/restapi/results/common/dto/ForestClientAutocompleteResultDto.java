package ca.bc.gov.restapi.results.common.dto;

import lombok.With;

/**
 * The type Forest client autocomplete result dto.
 */
@With
public record ForestClientAutocompleteResultDto(
    String id,
    String name,
    String acronym
) {

}
