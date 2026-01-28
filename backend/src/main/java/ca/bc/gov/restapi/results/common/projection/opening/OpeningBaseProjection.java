package ca.bc.gov.restapi.results.common.projection.opening;

import java.time.LocalDateTime;

public interface OpeningBaseProjection {
  Long getId();
  String getStatus();
  String getCategory();
  String getEntryUserId();
  LocalDateTime getUpdateTimestamp();
  LocalDateTime getEntryTimestamp();
}
