package ca.bc.gov.restapi.results.oracle.dto;

import lombok.With;
import org.springframework.aot.hint.annotation.RegisterReflectionForBinding;

@RegisterReflectionForBinding
@With
public record CodeDescriptionDto(
    String code,
    String description
) {

}
