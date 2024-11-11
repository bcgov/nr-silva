package ca.bc.gov.restapi.results.oracle.dto;

import lombok.With;

@With
public record CodeDescriptionDto(
    String code,
    String description
) {

}
