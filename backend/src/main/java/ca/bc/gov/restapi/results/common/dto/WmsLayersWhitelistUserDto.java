package ca.bc.gov.restapi.results.common.dto;

import lombok.Builder;
import lombok.With;
import org.springframework.aot.hint.annotation.RegisterReflectionForBinding;

/**
 * This record represents a user in the whitelist.
 */
@RegisterReflectionForBinding
@Builder
@With
public record WmsLayersWhitelistUserDto(String userName) {

}
