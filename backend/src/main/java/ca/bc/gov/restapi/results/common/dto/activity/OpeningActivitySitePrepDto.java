package ca.bc.gov.restapi.results.common.dto.activity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.With;

@EqualsAndHashCode(callSuper = true)
@Data
@With
@AllArgsConstructor
public class OpeningActivitySitePrepDto extends OpeningActivityBaseDto {

  private Long targetSpot;

  public OpeningActivitySitePrepDto(OpeningActivityBaseDto base) {
    super(base);
  }


}
