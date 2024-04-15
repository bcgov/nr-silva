package ca.bc.gov.restapi.results.oracle.dto;

import java.time.LocalDateTime;

/** This record holds columns for the results audit table. */
public interface DashboardResultsAuditDto {

  String RESULTS_AUDIT_ACTION_CODE();

  LocalDateTime ACTION_DATE();

  LocalDateTime ENTRY_TIMESTAMP();

  String ENTRY_USERID();

  Long OPENING_ID();

  LocalDateTime ACTION_TIMESTAMP();
}
