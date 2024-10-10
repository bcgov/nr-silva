package ca.bc.gov.restapi.results.common.dto;

import lombok.Builder;
import lombok.With;

/**
 * This record represents a user in the whitelist.
 */
@Builder
@With
public record WmsLayersWhitelistUserDto(String userName) {

}
