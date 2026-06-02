package ca.bc.gov.restapi.results.common.projection;

import java.time.LocalDateTime;

public interface StockingStandardsCommentSearchProjection {

  Long getStandardsRegimeId();

  String getCommentLocation();

  LocalDateTime getExpiryDate();

  String getCommentText();

  LocalDateTime getUpdateTimestamp();

  String getOrgUnitCodes();

  String getOrgUnitNames();

  String getClientNumbers();

  String getFspIds();

  LocalDateTime getApprovedTimestamp();

  Long getTotalCount();
}
