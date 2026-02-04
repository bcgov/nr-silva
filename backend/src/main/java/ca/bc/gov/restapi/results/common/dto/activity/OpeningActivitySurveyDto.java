package ca.bc.gov.restapi.results.common.dto.activity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.With;

@EqualsAndHashCode(callSuper = true)
@Data
@With
@AllArgsConstructor
public class OpeningActivitySurveyDto extends OpeningActivityBaseDto {

  private Long plotsCount;
  private Long surveyMinPlotsPerStratum;

  public OpeningActivitySurveyDto(OpeningActivityBaseDto base) {
    super(base);
  }


}
