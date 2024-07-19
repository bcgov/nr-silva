package ca.bc.gov.restapi.results.oracle.repository;

import ca.bc.gov.restapi.results.common.pagination.PaginationParameters;
import ca.bc.gov.restapi.results.oracle.dto.SearchOpeningFiltersDto;
import ca.bc.gov.restapi.results.oracle.dto.SearchOpeningResultDto;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@AllArgsConstructor
public class OpeningSearchRepository {

  private final EntityManager em;

  public List<SearchOpeningResultDto> searchOpeningQuery(
      SearchOpeningFiltersDto filtersDto, PaginationParameters pagination) {
    // build query
    StringBuilder queryString = new StringBuilder();

    queryString.append("SELECT o.OPENING_ID AS openingId");
    queryString.append(",o.OPENING_NUMBER AS openingNumber");
    queryString.append(",o.OPEN_CATEGORY_CODE AS category");
    queryString.append(",oa.OPENING_ATTACHMENT_FILE_ID AS fileId");
    queryString.append(",o.OPENING_STATUS_CODE AS status");
    queryString.append(",cboa.CUTTING_PERMIT_ID AS cuttingPermitId");
    queryString.append(",cboa.TIMBER_MARK AS timberMark");
    queryString.append(",cboa.CUT_BLOCK_ID AS cutBlockId");
    queryString.append(",cboa.OPENING_GROSS_AREA AS openingGrossArea");
    queryString.append(",cboa.DISTURBANCE_START_DATE AS disturbanceStartDate");
    queryString.append(",ou.ORG_UNIT_CODE AS orgUnitCode");
    queryString.append(",ou.ORG_UNIT_NAME AS orgUnitName");
    queryString.append(",res.CLIENT_NUMBER AS clientNumber");
    queryString.append(",'TDB' AS regenDelayDate");
    queryString.append(",'TBD' AS freeGrowingDate");
    queryString.append(",o.UPDATE_TIMESTAMP AS updateTimestamp");
    queryString.append(",o.ENTRY_USERID AS entryUserId");
    queryString.append(",'TBD' AS submittedToFrpa108");
    queryString.append("FROM THE.OPENING o ");
    queryString.append("LEFT JOIN THE.OPENING_ATTACHMENT oa ON (oa.OPENING_ID = o.OPENING_ID)");
    queryString.append(
        "LEFT JOIN THE.CUT_BLOCK_OPEN_ADMIN cboa ON (cboa.OPENING_ID = o.OPENING_ID)");
    queryString.append("LEFT JOIN THE.ORG_UNIT ou ON (ou.ORG_UNIT_NO = o.ADMIN_DISTRICT_NO)");
    queryString.append(
        "LEFT JOIN the.RESULTS_ELECTRONIC_SUBMISSION res ON (res.RESULTS_SUBMISSION_ID ="
            + " o.RESULTS_SUBMISSION_ID)");
    queryString.append(
        "LEFT JOIN THE.CLIENT_ACRONYM ca ON (ca.CLIENT_NUMBER = res.CLIENT_NUMBER) ");
    queryString.append("WHERE 1=1 ");

    /* Filters */
    // 0. Main number filter [opening_id, opening_number, timber_mark, file_id]
    // if it's a number, filter by openingId or fileId, otherwise filter by timber mark and opening
    // number
    if (filtersDto.hasValue(filtersDto._NUMBER)) {
      log.info("number filter detected! number=[{}]", filtersDto.getNumber());
      boolean itsNumeric = filtersDto.getNumber().replaceAll("[0-9]", "").isEmpty();
      if (itsNumeric) {
        log.info("number filter it's numeric!");
        // Opening id or  File id
        queryString.append(
            "AND (o.OPENING_ID = :openingOrFile or oa.OPENING_ATTACHMENT_FILE_ID ="
                + " :openingOrFile) ");
      } else {
        log.info("number filter it's NOT numeric!");
        // Opening number or Timber Mark
        queryString.append(
            "AND (o.OPENING_NUMBER = :numberOrTimber or cboa.TIMBER_MARK = :numberOrTimber) ");
      }
    }

    // 1. Org Unit code
    if (filtersDto.hasValue(filtersDto._ORG_UNIT)) {
      log.info("org unit filter detected! org unit=[{}]", filtersDto.getOrgUnit());
      queryString.append("AND ou.ORG_UNIT_CODE = ?2");
    }
    // 2. Category code
    if (filtersDto.hasValue(filtersDto._CATEGORY)) {
      log.info("category filter detected! category=[{}]", filtersDto.getCategory());
      queryString.append("AND o.OPEN_CATEGORY_CODE = ?3");
    }
    // 3. Status code
    if (filtersDto.hasValue(filtersDto._STATUS)) {
      log.info("status filter detected! status=[{}]", filtersDto.getStatus());
      queryString.append("AND o.OPENING_STATUS_CODE = ?4");
    }
    // 4. User entry id
    if (filtersDto.hasValue(filtersDto._USER_ID)) {
      log.info("user-id filter detected! user-id=[{}]", filtersDto.getUserId());
      queryString.append("AND o.ENTRY_USERID = ?5");
    }
    // 5. Submitted to FRPA
    if (filtersDto.hasValue(filtersDto._SUBMITTED_TO_FRPA)) {
      log.info(
          "Submitted to FRPA filter detected! submitted=[{}]", filtersDto.getSubmittedToFrpa());
      // queryString.append("AND o.ENTRY_USERID = ?5");
    }
    // 6. Disturbance start date
    // 7. Disturbance end date
    // 8. Regen delay start date
    // 9. Regen delay end date
    // 10. Free growing start date
    // 11. Free growing end date
    // 12. Update date start
    // 13. Update date end

    Query query = em.createNativeQuery(queryString.toString());
    // set pagination
    query.setFirstResult(pagination.page());
    query.setMaxResults(pagination.perPage());

    // set parameters
    if (filtersDto.hasValue(filtersDto._NUMBER)) {
      boolean itsNumeric = filtersDto.getNumber().replaceAll("[0-9]", "").isEmpty();
      if (itsNumeric) {
        // Opening id or  File id
        query.setParameter("openingOrFile", filtersDto.getNumber());
      } else {
        // Opening number or Timber Mark
        queryString.append("AND (o.OPENING_NUMBER = ?1 or cboa.TIMBER_MARK = ?1) ");
        query.setParameter("numberOrTimber", filtersDto.getNumber());
      }
    }

    // get list
    List<SearchOpeningResultDto> resultList = (List<SearchOpeningResultDto>) query.getResultList();

    return resultList;
  }
}
