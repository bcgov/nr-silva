package ca.bc.gov.restapi.results.common.dto.activity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.With;

@EqualsAndHashCode(callSuper = true)
@Data
@With
@AllArgsConstructor
public class OpeningActivityJuvenileDto extends OpeningActivityBaseDto {
  private Float targetIntertreeDistance;
  private Float allowableVariationDistance;
  private Long allowableTreePerLot;
  private Float spacingPerHa;

  public OpeningActivityJuvenileDto(OpeningActivityBaseDto base) {
    super(base);
  }

}
