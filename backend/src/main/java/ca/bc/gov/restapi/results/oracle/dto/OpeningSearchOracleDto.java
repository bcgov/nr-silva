package ca.bc.gov.restapi.results.oracle.dto;

import java.time.LocalDateTime;

/** This interface contains all the columns returned from the search query. */
public interface OpeningSearchOracleDto {

  Long getOpeningId();

  String getOpeningNumber();

  String getCategoryCode();

  Long getFileId();

  String getStatusCode();

  String getCuttingPermitId();

  String getTimberMark();

  String getCutBlockId();

  String getOpeningGrossArea();

  LocalDateTime getDisturbanceStartDate();

  String getOrgUnitCode();

  String getOrgUnitName();

  String getClientNumber();

  String getRegenDelayDate();

  String getFreeGrowingDate();

  LocalDateTime getUpdateTimestamp();

  String getEntryUserId();

  String getSubmittedToFrpa108();

  /** Default method for logging the interface. */
  default String toLogString() {
    StringBuilder logDto = new StringBuilder("{");
    logDto.append("openingId=").append(getOpeningId());
    logDto.append(", openingNumber='").append(getOpeningNumber()).append("'");
    logDto.append(", categoryCode='").append(getCategoryCode()).append("'");
    logDto.append(", fileId=").append(getFileId());
    logDto.append(", statusCode='").append(getStatusCode()).append("'");
    logDto.append(", cuttingPermitId='").append(getCuttingPermitId()).append("'");
    logDto.append(", timberMark='").append(getTimberMark()).append("'");
    logDto.append(", cutBlockId='").append(getCutBlockId()).append("'");
    logDto.append(", openingGrossArea='").append(getOpeningGrossArea()).append("'");
    logDto.append(", disturbanceStartDate='").append(getDisturbanceStartDate()).append("'");
    logDto.append(", orgUnitCode='").append(getOrgUnitCode()).append("'");
    logDto.append(", orgUnitName='").append(getOrgUnitName()).append("'");
    logDto.append(", clientNumber='").append(getClientNumber()).append("'");
    logDto.append(", regenDelayDate='").append(getRegenDelayDate()).append("'");
    logDto.append(", freeGrowingDate='").append(getFreeGrowingDate()).append("'");
    logDto.append(", updateTimestamp='").append(getUpdateTimestamp()).append("'");
    logDto.append(", entryUserId='").append(getEntryUserId()).append("'");
    logDto.append(", submittedToFrpa108='").append(getSubmittedToFrpa108()).append("'");
    logDto.append("'}");
    return logDto.toString();
  }
}
