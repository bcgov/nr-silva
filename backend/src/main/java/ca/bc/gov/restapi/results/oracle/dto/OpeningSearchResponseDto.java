package ca.bc.gov.restapi.results.oracle.dto;

import ca.bc.gov.restapi.results.oracle.enums.OpeningCategoryEnum;
import ca.bc.gov.restapi.results.oracle.enums.OpeningStatusEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/** This class contains all values for the Opening Search API response. */
@Getter
@Setter
@ToString
@EqualsAndHashCode
@Schema(description = "This object contains all values for the Opening Search API response.")
public class OpeningSearchResponseDto {

  private Long openingId;
  private String openingNumber;
  private OpeningCategoryEnum categoryCode;
  private Long fileId;
  private OpeningStatusEnum statusCode;
  private String cuttingPermitId;
  private String timberMark;
  private String cutBlockId;
  private String openingGrossArea;
  private LocalDateTime disturbanceStartDate;
  private String orgUnitCode;
  private String orgUnitName;
  private String clientNumber;
  private String regenDelayDate;
  private String freeGrowingDate;
  private LocalDateTime updateTimestamp;
  private String entryUserId;
  private String submittedToFrpa108;
}
