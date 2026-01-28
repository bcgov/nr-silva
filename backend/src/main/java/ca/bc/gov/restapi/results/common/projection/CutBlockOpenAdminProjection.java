package ca.bc.gov.restapi.results.common.projection;

import java.math.BigDecimal;
import java.time.LocalDate;

public interface CutBlockOpenAdminProjection {
  Long getId();
  String getForestFileId();
  String getCuttingPermitId();
  String getTimberMark();
  String getCutBlockId();
  BigDecimal getOpeningGrossArea();
  LocalDate getDisturbanceStartDate();
  LocalDate getDisturbanceEndDate();
  Long getOpeningId();
}
