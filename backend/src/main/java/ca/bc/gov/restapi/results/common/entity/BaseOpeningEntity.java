package ca.bc.gov.restapi.results.common.entity;

import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@MappedSuperclass
public abstract class BaseOpeningEntity {
  @Id private Long id;

  private String status;
  private String category;
  private String entryUserId;
  private LocalDateTime updateTimestamp;
  private LocalDateTime entryTimestamp;

  private Long geoDistrictNo;
  private Long adminDistrictNo;
  private Long orgUnitNo;
  private BigDecimal maxAllowPermntAccessPct;
  private String licenseeOpeningId;
  private String amendmentInd;
  private String mapsheetGrid;
  private String mapsheetLetter;
  private String mapsheetSquare;
  private String mapsheetQuad;
  private String mapsheetSubQuad;
  private String openingNumber;
  private Integer revisionCount;
  private String updateUserId;
}
