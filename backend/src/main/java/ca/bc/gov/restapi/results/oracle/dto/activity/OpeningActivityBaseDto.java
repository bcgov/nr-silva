package ca.bc.gov.restapi.results.oracle.dto.activity;

import ca.bc.gov.restapi.results.common.dto.ForestClientDto;
import ca.bc.gov.restapi.results.common.dto.ForestClientLocationDto;
import lombok.Data;
import lombok.With;

@Data
@With
public class OpeningActivityBaseDto {

  String licenseeActivityId;
  String intraAgencyNumber;
  ForestClientDto activityClient;
  ForestClientLocationDto activityLocation;
  Float plannedAmount;
  Float treatedAmount;
  Float plannedCost;
  Float actualCost;
  Long totalPlanting;

  public OpeningActivityBaseDto() {
  }

  public OpeningActivityBaseDto(
      String licenseeActivityId,
      String intraAgencyNumber,
      ForestClientDto activityClient,
      ForestClientLocationDto activityLocation,
      Float plannedAmount,
      Float treatedAmount,
      Float plannedCost,
      Float actualCost,
      Long totalPlanting
  ) {
    this.licenseeActivityId = licenseeActivityId;
    this.intraAgencyNumber = intraAgencyNumber;
    this.activityClient = activityClient;
    this.activityLocation = activityLocation;
    this.plannedAmount = plannedAmount;
    this.treatedAmount = treatedAmount;
    this.plannedCost = plannedCost;
    this.actualCost = actualCost;
    this.totalPlanting = totalPlanting;
  }

  public OpeningActivityBaseDto(OpeningActivityBaseDto base) {
    this.licenseeActivityId = base.licenseeActivityId;
    this.intraAgencyNumber = base.intraAgencyNumber;
    this.activityClient = base.activityClient;
    this.activityLocation = base.activityLocation;
    this.plannedAmount = base.plannedAmount;
    this.treatedAmount = base.treatedAmount;
    this.plannedCost = base.plannedCost;
    this.actualCost = base.actualCost;
    this.totalPlanting = base.totalPlanting;
  }
}
