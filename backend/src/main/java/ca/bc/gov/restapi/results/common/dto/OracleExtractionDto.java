package ca.bc.gov.restapi.results.common.dto;

import ca.bc.gov.restapi.results.oracle.dto.DashboardActionCodeDto;
import ca.bc.gov.restapi.results.oracle.dto.DashboardOpeningDto;
import ca.bc.gov.restapi.results.oracle.dto.DashboardOpeningSubmissionDto;
import ca.bc.gov.restapi.results.oracle.dto.DashboardOrgUnitDto;
import ca.bc.gov.restapi.results.oracle.dto.DashboardResultsAuditDto;
import ca.bc.gov.restapi.results.oracle.dto.DashboardStockingEventDto;
import java.util.List;

/** This recor holds all data extracted from Oracle to be inserted on Postgres */
public record OracleExtractionDto(
    List<DashboardOpeningDto> mainOpenings,
    List<DashboardOpeningSubmissionDto> openingSubmissions,
    List<DashboardResultsAuditDto> resultsAudits,
    List<DashboardStockingEventDto> stockingEvents,
    List<DashboardOrgUnitDto> orgUnits,
    List<DashboardActionCodeDto> actionCodes) {}
