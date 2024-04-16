package ca.bc.gov.restapi.results.oracle.dto;

/** This interface represents a org unit record in the database. */
public interface DashboardOrgUnitDto {

  Long getOrgUnitNo();

  String getOrgUnitCode();

  String getOrgUnitName();

  default String toLogString() {
    StringBuilder logDto = new StringBuilder("{");
    logDto.append("orgUnitNo=").append(getOrgUnitNo()).append(", ");
    logDto.append("orgUnitCode='").append(getOrgUnitCode()).append("', ");
    logDto.append("orgUnitName='").append(getOrgUnitName()).append("'}");
    return logDto.toString();
  }
}
