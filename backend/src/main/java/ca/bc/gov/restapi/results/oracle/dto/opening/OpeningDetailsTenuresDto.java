package ca.bc.gov.restapi.results.oracle.dto.opening;

import ca.bc.gov.restapi.results.oracle.dto.SimplePageDto;
import java.util.List;
import lombok.With;

@With
public record OpeningDetailsTenuresDto(
    OpeningDetailsTenureDto primary,
    List<OpeningDetailsTenureDto> content,
    SimplePageDto page
) {

}
