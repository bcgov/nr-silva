package ca.bc.gov.restapi.results.common.dto;

import lombok.With;
import org.springframework.aot.hint.annotation.RegisterReflectionForBinding;

@RegisterReflectionForBinding
@With
public record IdNameDto(
    String id,
    String name
) {

}
