package ca.bc.gov.restapi.results.common.dto.activity;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class OpeningActivitySurveyDto extends OpeningActivityBaseDto {

  private Long plotsCount;
  private Long surveyMinPlotsPerStratum;

  public OpeningActivitySurveyDto(OpeningActivityBaseDto base) {
    super(base);
  }
}
