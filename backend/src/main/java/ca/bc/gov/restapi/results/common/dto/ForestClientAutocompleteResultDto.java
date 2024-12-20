package ca.bc.gov.restapi.results.common.dto;

import lombok.With;

@With
public record ForestClientAutocompleteResultDto(
    String id,
    String name,
    String acronym
) {

}
