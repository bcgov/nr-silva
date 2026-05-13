package ca.bc.gov.restapi.results.common.dto.activity;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class OpeningActivityJuvenileDto extends OpeningActivityBaseDto {
  private Float targetIntertreeDistance;
  private Float allowableVariationDistance;
  private Long allowableTreePerLot;
  private Float spacingPerHa;

  public OpeningActivityJuvenileDto(OpeningActivityBaseDto base) {
    super(base);
  }
}
