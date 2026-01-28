package ca.bc.gov.restapi.results.common.dto.activity;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.With;

@EqualsAndHashCode(callSuper = true)
@Data
@With
@AllArgsConstructor
public class OpeningActivitySpeciesDto extends OpeningActivityBaseDto {

  private List<OpeningActivitySpeciesDetailsDto> species;

  public OpeningActivitySpeciesDto(OpeningActivityBaseDto base) {
    super(base);
  }

  public long getTotalSpecies() {
    return species != null ? species.size() : 0L;
  }

}
