package ca.bc.gov.restapi.results.oracle.repository;

import ca.bc.gov.restapi.results.oracle.dto.DashboardActionCodeDto;
import ca.bc.gov.restapi.results.oracle.dto.DashboardOpeningDto;
import ca.bc.gov.restapi.results.oracle.dto.DashboardOpeningSubmissionDto;
import ca.bc.gov.restapi.results.oracle.dto.DashboardOrgUnitDto;
import ca.bc.gov.restapi.results.oracle.dto.DashboardResultsAuditDto;
import ca.bc.gov.restapi.results.oracle.dto.DashboardStockingEventDto;
import ca.bc.gov.restapi.results.oracle.dto.OpeningSearchOracleDto;
import ca.bc.gov.restapi.results.oracle.entity.OpeningEntity;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

/** This interface allows the service to fetch and save data into the database. */
public interface OpeningRepository extends JpaRepository<OpeningEntity, Long> {

  Page<OpeningEntity> findAllByEntryUserId(String entryUserId, Pageable pageable);

  @Query(
      value =
          """
          SELECT op.OPENING_ID AS openingId
            ,op.OPENING_STATUS_CODE AS openingStatusCode
            ,op.ENTRY_USERID AS entryUserId
            ,op.ENTRY_TIMESTAMP AS entryTimestamp
            ,op.UPDATE_TIMESTAMP AS updateTimestamp
            ,op.ADMIN_DISTRICT_NO AS adminDistrictNo
            ,op.RESULTS_SUBMISSION_ID AS resultsSubmissionId
            ,GREATEST(op.ENTRY_TIMESTAMP,op.UPDATE_TIMESTAMP) AS actionTimestamp
          FROM THE.OPENING op
          WHERE op.ENTRY_TIMESTAMP >= ADD_MONTHS(SYSDATE, - ?1)
            OR op.UPDATE_TIMESTAMP >= ADD_MONTHS(SYSDATE, - ?1)
          ORDER BY actionTimestamp DESC
          """,
      nativeQuery = true)
  List<DashboardOpeningDto> findAllDashboardOpenings(Integer months);

  @Query(
      value =
          """
          SELECT res.RESULTS_SUBMISSION_ID AS resultsSubmissionId
            ,res.CLIENT_NUMBER AS clientNumber
          FROM THE.RESULTS_ELECTRONIC_SUBMISSION res
          WHERE res.RESULTS_SUBMISSION_ID IN (?1)
          """,
      nativeQuery = true)
  List<DashboardOpeningSubmissionDto> findAllDashboardOpeningSubmissions(
      List<Long> submissionIdList);

  @Query(
      value =
          """
          SELECT ra.RESULTS_AUDIT_ACTION_CODE AS resultsAuditActionCode
            ,ra.ACTION_DATE AS actionDate
            ,ra.ENTRY_TIMESTAMP AS entryTimestamp
            ,ra.ENTRY_USERID AS entryUserid
            ,ra.OPENING_ID AS openingId
            ,GREATEST(ra.ENTRY_TIMESTAMP,ra.ACTION_DATE) AS actionTimestamp
          FROM THE.RESULTS_AUDIT_EVENT ra
          WHERE ra.ENTRY_TIMESTAMP >= ADD_MONTHS(SYSDATE, - ?1)
            OR ra.ACTION_DATE >= ADD_MONTHS(SYSDATE, - ?1)
          ORDER BY actionTimestamp DESC
        """,
      nativeQuery = true)
  List<DashboardResultsAuditDto> findAllDashboardAuditEvents(Integer months);

  @Query(
      value =
          """
          SELECT seh.RESULTS_AUDIT_ACTION_CODE AS resultsAuditActionCode
            ,seh.ENTRY_USERID AS entryUserid
            ,seh.OPENING_ID AS openingId
            ,seh.ENTRY_TIMESTAMP AS entryTimestamp
            ,seh.AMEND_EVENT_TIMESTAMP AS amendEventTimestamp
            ,GREATEST(seh.ENTRY_TIMESTAMP,seh.AMEND_EVENT_TIMESTAMP) AS actionTimestamp
          FROM THE.STOCKING_EVENT_HISTORY seh
          WHERE seh.ENTRY_TIMESTAMP >= ADD_MONTHS(SYSDATE, - ?1)
            OR seh.AMEND_EVENT_TIMESTAMP  >= ADD_MONTHS(SYSDATE, - ?1)
          ORDER BY actionTimestamp DESC
        """,
      nativeQuery = true)
  List<DashboardStockingEventDto> findAllDashboardStockingEventHistory(Integer months);

  @Query(
      value =
          """
          SELECT ou.ORG_UNIT_NO AS orgUnitNo
            ,ou.ORG_UNIT_CODE AS orgUnitCode
            ,ou.ORG_UNIT_NAME AS orgUnitName
          FROM THE.ORG_UNIT ou
          WHERE ou.ORG_UNIT_NO IN (?1)
        """,
      nativeQuery = true)
  List<DashboardOrgUnitDto> findAllDashboardOrgUnits(List<Long> orgUnitIds);

  @Query(
      value =
          """
          SELECT raac.RESULTS_AUDIT_ACTION_CODE AS resultsAuditActionCode
            ,raac.DESCRIPTION AS description
          FROM THE.RESULTS_AUDIT_ACTION_CODE raac
          WHERE raac.RESULTS_AUDIT_ACTION_CODE IN (?1)
        """,
      nativeQuery = true)
  List<DashboardActionCodeDto> findAllDashboardActionCodes(List<String> codes);

  @Query(
      value =
          """
          SELECT o.OPENING_ID AS openingId
            ,o.OPENING_NUMBER AS openingNumber
            ,o.OPEN_CATEGORY_CODE AS categoryCode
            ,oa.OPENING_ATTACHMENT_FILE_ID AS fileId
            ,o.OPENING_STATUS_CODE AS statusCode
            ,cboa.CUTTING_PERMIT_ID AS cuttingPermitId
            ,cboa.TIMBER_MARK AS timberMark
            ,cboa.CUT_BLOCK_ID AS cutBlockId
            ,cboa.OPENING_GROSS_AREA AS openingGrossArea
            ,cboa.DISTURBANCE_START_DATE AS disturbanceStartDate
            ,ou.ORG_UNIT_CODE AS orgUnitCode
            ,ou.ORG_UNIT_NAME AS orgUnitName
            ,res.CLIENT_NUMBER AS clientNumber
            ,'TDB' AS regenDelayDate
            ,'TBD' AS freeGrowingDate
            ,o.UPDATE_TIMESTAMP AS updateTimestamp
            ,o.ENTRY_USERID AS entryUserId
            ,'TBD' AS submittedToFrpa108
          FROM THE.OPENING o
          LEFT JOIN THE.OPENING_ATTACHMENT oa ON (oa.OPENING_ID = o.OPENING_ID)
          LEFT JOIN THE.CUT_BLOCK_OPEN_ADMIN cboa ON (cboa.OPENING_ID = o.OPENING_ID)
          LEFT JOIN THE.ORG_UNIT ou ON (ou.ORG_UNIT_NO = o.ADMIN_DISTRICT_NO)
          LEFT JOIN the.RESULTS_ELECTRONIC_SUBMISSION res ON (res.RESULTS_SUBMISSION_ID = o.RESULTS_SUBMISSION_ID)
          LEFT JOIN THE.CLIENT_ACRONYM ca ON (ca.CLIENT_NUMBER = res.CLIENT_NUMBER)
          WHERE o.OPENING_ID = '?1'
            OR o.OPENING_NUMBER = '?1'
            OR cboa.TIMBER_MARK = '?1'
            OR oa.OPENING_ATTACHMENT_FILE_ID = '?1'
        """,
      nativeQuery = true)
  Page<OpeningSearchOracleDto> findAllOpeningSearchWithNumber(Pageable pageable, String number);
}
