package ca.bc.gov.restapi.results.oracle.dto.opening;

import ca.bc.gov.restapi.results.oracle.enums.OpeningCategoryEnum;
import ca.bc.gov.restapi.results.oracle.enums.OpeningStatusEnum;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.With;

/**
 * This class represents a record of the opening search api response.
 */
@Data
@Builder
@With
@NoArgsConstructor
@AllArgsConstructor
public class OpeningSearchResponseDto {

  private Integer openingId;
  private String openingNumber;
  private OpeningCategoryEnum category;
  private OpeningStatusEnum status;
  private String cuttingPermitId;
  private String timberMark;
  private String cutBlockId;
  private BigDecimal openingGrossAreaHa;
  private LocalDateTime disturbanceStartDate;
  private String orgUnitCode;
  private String orgUnitName;
  private String clientNumber;
  private String clientLocation;
  private String clientAcronym;
  private String clientName;
  private LocalDateTime regenDelayDate;
  private LocalDateTime earlyFreeGrowingDate;
  private LocalDateTime lateFreeGrowingDate;
  private LocalDateTime updateTimestamp;
  private String entryUserId;
  private Boolean submittedToFrpa;
  private String forestFileId;
  private Long silvaReliefAppId;
  private LocalDateTime lastViewDate;
  private boolean favourite;
}
