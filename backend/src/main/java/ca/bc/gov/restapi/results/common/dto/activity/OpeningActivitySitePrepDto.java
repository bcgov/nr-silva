package ca.bc.gov.restapi.results.common.dto.activity;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class OpeningActivitySitePrepDto extends OpeningActivityBaseDto {

  private Long targetSpot;

  public OpeningActivitySitePrepDto(OpeningActivityBaseDto base) {
    super(base);
  }
}
