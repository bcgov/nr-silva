package ca.bc.gov.restapi.results.common.dto.activity;

import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class OpeningActivitySpeciesDto extends OpeningActivityBaseDto {

  private List<OpeningActivitySpeciesDetailsDto> species;

  public OpeningActivitySpeciesDto(OpeningActivityBaseDto base) {
    super(base);
  }

  public long getTotalSpecies() {
    return species != null ? species.size() : 0L;
  }
}
