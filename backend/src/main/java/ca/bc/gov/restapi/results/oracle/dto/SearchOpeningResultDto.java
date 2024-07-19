package ca.bc.gov.restapi.results.oracle.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public interface SearchOpeningResultDto {
  
  public Long getOpeningId();

  public String getOpeningNumber();

  public String getCategory();

  public Long getFileId();

  public String getStatus();

  public String getCuttingPermitId();

  public String getTimberMark();

  public String getCutBlockId();

  public BigDecimal getOpeningGrossArea();

  public LocalDateTime getDisturbanceStartDate();

  public String getOrgUnitCode();

  public String getOrgUnitName();

  public String getClientNumber();

  public String getRegenDelayDate();

  public String getFreeGrowingDate();

  public LocalDateTime getUpdateTimestamp();

  public String getEntryUserId();

  public String getSubmittedToFrpa108();

}
