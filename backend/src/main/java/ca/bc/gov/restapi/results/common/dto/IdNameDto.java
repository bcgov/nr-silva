package ca.bc.gov.restapi.results.common.dto;

import lombok.With;

@With
public record IdNameDto(
    String id,
    String name
) {

}
