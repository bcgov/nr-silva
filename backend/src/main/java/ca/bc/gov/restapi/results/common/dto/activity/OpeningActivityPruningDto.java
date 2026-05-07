package ca.bc.gov.restapi.results.common.dto.activity;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class OpeningActivityPruningDto extends OpeningActivityBaseDto {

  private Float totalStemsPerHa;
  private Float stemsPerHaToPrune;
  private Float targetIntertreeDistance;
  private Float minimumIntertreeDistance;
  private Float heightAboveGround;
  private Float minimumLiveCrown;

  public OpeningActivityPruningDto(OpeningActivityBaseDto base) {
    super(base);
  }
}
