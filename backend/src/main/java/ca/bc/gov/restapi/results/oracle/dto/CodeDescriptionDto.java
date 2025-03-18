package ca.bc.gov.restapi.results.oracle.dto;

import lombok.With;

/**
 * The type Code description dto.
 */
@With
public record CodeDescriptionDto(
    String code,
    String description
) {

}
