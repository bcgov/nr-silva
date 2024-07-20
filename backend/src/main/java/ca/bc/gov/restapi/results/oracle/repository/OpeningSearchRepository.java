package ca.bc.gov.restapi.results.oracle.repository;

import ca.bc.gov.restapi.results.common.pagination.PaginationParameters;
import ca.bc.gov.restapi.results.oracle.dto.SearchOpeningDto;
import ca.bc.gov.restapi.results.oracle.dto.SearchOpeningFiltersDto;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@AllArgsConstructor
public class OpeningSearchRepository {

  private final EntityManager em;

  public List<SearchOpeningDto> searchOpeningQuery(
      SearchOpeningFiltersDto filtersDto, PaginationParameters pagination) {

    final String sqlQuery = createNativeSqlQuery(filtersDto);
    final Query query = createJpaQuery(filtersDto, sqlQuery);

    // set pagination
    query.setFirstResult(pagination.page());
    query.setMaxResults(pagination.perPage());

    // get list
    List<?> result = query.getResultList();
    log.info("result empty: {}", result.isEmpty());
    log.info("result size: {}", result.size());

    List<SearchOpeningDto> resultList = new ArrayList<>();
    for (Object obj : result) {
      if (obj.getClass().isArray()) {
        Object[] row = (Object[]) obj;
        SearchOpeningDto searchOpeningDto = new SearchOpeningDto();
        // opening Id
        if (row.length > 0 && row[0] instanceof Integer openingId) {
          log.info("openingId={}", openingId);
          searchOpeningDto.setOpeningId((long) openingId);
        }

        // opening number
        if (row.length > 1 && row[1] instanceof String openingNumber) {
          log.info("openingNumber={}", openingNumber);
          searchOpeningDto.setOpeningNumber(openingNumber);
        }
        // searchOpeningDto.setOpeningNumber(openingResult.getOpeningNumber());
        // searchOpeningDto.setCategory(OpeningCategoryEnum.of(openingResult.getCategory()));
        // searchOpeningDto.setStatus(OpeningStatusEnum.of(openingResult.getStatus()));
        // searchOpeningDto.setUpdateTimestamp(openingResult.getUpdateTimestamp());
        // searchOpeningDto.setEntryUserId(openingResult.getEntryUserId());

        // // From Cut Block Opening Admin table
        // searchOpeningDto.setCuttingPermitId(openingResult.getCuttingPermitId());
        // searchOpeningDto.setCutBlockId(openingResult.getCutBlockId());
        // searchOpeningDto.setTimberMark(openingResult.getTimberMark());
        // searchOpeningDto.setGrossAreaHa(openingResult.getOpeningGrossArea());
        // searchOpeningDto.setDisturbanceDate(openingResult.getDisturbanceStartDate().toLocalDate());

        // // From Opening Attachment table
        // searchOpeningDto.setFileId(openingResult.getFileId());

        // // From Org Unit (Admin District)
        // searchOpeningDto.setOrgUnitCode(openingResult.getOrgUnitCode());
        // searchOpeningDto.setOrgUnitName(openingResult.getOrgUnitName());

        // searchOpeningDto.setClientNumber(openingResult.getClientNumber());
        // searchOpeningDto.setClientAcronym(null);
        // searchOpeningDto.setRegenDelayDate(null);
        // searchOpeningDto.setFreeGrowingDate(null);
        // searchOpeningDto.setSubmittedToFrpa(false);

        resultList.add(searchOpeningDto);
      }
    }

    return resultList;
  }

  private Query createJpaQuery(SearchOpeningFiltersDto filtersDto, String nativeQuery) {
    Query query = em.createNativeQuery(nativeQuery);

    // set parameters
    if (filtersDto.hasValue(filtersDto._NUMBER)) {
      boolean itsNumeric = filtersDto.getNumber().replaceAll("[0-9]", "").isEmpty();
      if (itsNumeric) {
        log.info("setting numeric 'number' filter!");
        // Opening id or  File id
        query.setParameter("openingOrFile", filtersDto.getNumber());
      } else {
        log.info("setting non-numeric 'number' filter!");
        // Opening number or Timber Mark
        query.setParameter("numberOrTimber", filtersDto.getNumber());
      }
    }

    // 1. Org Unit code
    if (filtersDto.hasValue(filtersDto._ORG_UNIT)) {
      log.info("setting org unit filter!");
      query.setParameter("orgUnit", filtersDto.getOrgUnit());
    }
    // 2. Category code
    if (filtersDto.hasValue(filtersDto._CATEGORY)) {
      log.info("setting category filter");
      query.setParameter("category", filtersDto.getCategory());
    }
    // 3. Status code
    if (filtersDto.hasValue(filtersDto._STATUS)) {
      log.info("setting status filter");
      query.setParameter("status", filtersDto.getStatus());
    }
    // 4. User entry id
    if (filtersDto.hasValue(filtersDto._USER_ID)) {
      log.info("setting user-id filter");
      query.setParameter("userId", filtersDto.getUserId());
    }
    // 5. Submitted to FRPA
    if (filtersDto.hasValue(filtersDto._SUBMITTED_TO_FRPA)) {
      // log.info("Submitted to FRPA filter detected! submitted=[{}]",
      // filtersDto.getSubmittedToFrpa());
      // builder.append("AND o.ENTRY_USERID = ?5");
    }
    // 6. Disturbance start date
    if (filtersDto.hasValue(filtersDto._DISTURBANCE_DATE_START)) {
      log.info("setting disturbance start date filter");
      query.setParameter("disturbStartDate", filtersDto.getDisturbanceDateStart());
    }
    // 7. Disturbance end date
    if (filtersDto.hasValue(filtersDto._DISTURBANCE_DATE_END)) {
      log.info("setting disturbance end date filter");
      query.setParameter("disturbEndDate", filtersDto.getDisturbanceDateEnd());
    }
    // 8. Regen delay start date
    // 9. Regen delay end date
    // 10. Free growing start date
    // 11. Free growing end date
    // 12. Update date start
    if (filtersDto.hasValue(filtersDto._UPDATE_DATE_END)) {
      log.info("setting update start date filter");
      query.setParameter("updateStartDate", filtersDto.getUpdateDateStart());
    }
    // 13. Update date end
    if (filtersDto.hasValue(filtersDto._UPDATE_DATE_END)) {
      log.info("setting update end date filter");
      query.setParameter("updateEndDate", filtersDto.getUpdateDateEnd());
    }

    return query;
  }

  private String createNativeSqlQuery(SearchOpeningFiltersDto filtersDto) {
    StringBuilder builder = new StringBuilder();
    builder.append("SELECT o.OPENING_ID AS openingId");
    builder.append(",o.OPENING_NUMBER AS openingNumber");
    builder.append(",o.OPEN_CATEGORY_CODE AS category");
    builder.append(",oa.OPENING_ATTACHMENT_FILE_ID AS fileId");
    builder.append(",o.OPENING_STATUS_CODE AS status");
    builder.append(",cboa.CUTTING_PERMIT_ID AS cuttingPermitId");
    builder.append(",cboa.TIMBER_MARK AS timberMark");
    builder.append(",cboa.CUT_BLOCK_ID AS cutBlockId");
    builder.append(",cboa.OPENING_GROSS_AREA AS openingGrossArea");
    builder.append(",cboa.DISTURBANCE_START_DATE AS disturbanceStartDate");
    builder.append(",ou.ORG_UNIT_CODE AS orgUnitCode");
    builder.append(",ou.ORG_UNIT_NAME AS orgUnitName");
    builder.append(",res.CLIENT_NUMBER AS clientNumber");
    builder.append(",'TDB' AS regenDelayDate");
    builder.append(",'TBD' AS freeGrowingDate");
    builder.append(",o.UPDATE_TIMESTAMP AS updateTimestamp");
    builder.append(",o.ENTRY_USERID AS entryUserId");
    builder.append(",'TBD' AS submittedToFrpa108 ");
    builder.append("FROM THE.OPENING o ");
    builder.append("LEFT JOIN THE.OPENING_ATTACHMENT oa ON (oa.OPENING_ID = o.OPENING_ID)");
    builder.append("LEFT JOIN THE.CUT_BLOCK_OPEN_ADMIN cboa ON (cboa.OPENING_ID = o.OPENING_ID)");
    builder.append("LEFT JOIN THE.ORG_UNIT ou ON (ou.ORG_UNIT_NO = o.ADMIN_DISTRICT_NO)");
    builder.append(
        "LEFT JOIN the.RESULTS_ELECTRONIC_SUBMISSION res ON (res.RESULTS_SUBMISSION_ID ="
            + " o.RESULTS_SUBMISSION_ID)");
    builder.append("LEFT JOIN THE.CLIENT_ACRONYM ca ON (ca.CLIENT_NUMBER = res.CLIENT_NUMBER) ");
    builder.append("WHERE 1=1 ");

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
        builder.append("AND (o.OPENING_ID = :openingOrFile or ");
        builder.append("oa.OPENING_ATTACHMENT_FILE_ID = :openingOrFile) ");
      } else {
        log.info("number filter it's NOT numeric!");
        // Opening number or Timber Mark
        builder.append(
            "AND (o.OPENING_NUMBER = :numberOrTimber or cboa.TIMBER_MARK = :numberOrTimber) ");
      }
    }

    // 1. Org Unit code
    if (filtersDto.hasValue(filtersDto._ORG_UNIT)) {
      log.info("org unit filter detected! org unit=[{}]", filtersDto.getOrgUnit());
      builder.append("AND ou.ORG_UNIT_CODE = :orgUnit ");
    }
    // 2. Category code
    if (filtersDto.hasValue(filtersDto._CATEGORY)) {
      log.info("category filter detected! category=[{}]", filtersDto.getCategory());
      builder.append("AND o.OPEN_CATEGORY_CODE = :category ");
    }
    // 3. Status code
    if (filtersDto.hasValue(filtersDto._STATUS)) {
      log.info("status filter detected! status=[{}]", filtersDto.getStatus());
      builder.append("AND o.OPENING_STATUS_CODE = :status ");
    }
    // 4. User entry id
    if (filtersDto.hasValue(filtersDto._USER_ID)) {
      log.info("user-id filter detected! user-id=[{}]", filtersDto.getUserId());
      builder.append("AND o.ENTRY_USERID = :userId ");
    }
    // 5. Submitted to FRPA
    if (filtersDto.hasValue(filtersDto._SUBMITTED_TO_FRPA)) {
      // log.info("Submitted to FRPA filter detected! submitted=[{}]",
      // filtersDto.getSubmittedToFrpa());
      // builder.append("AND o.ENTRY_USERID = ?5");
    }
    // 6. Disturbance start date
    if (filtersDto.hasValue(filtersDto._DISTURBANCE_DATE_START)) {
      log.info(
          "Disturbance start date filter detected! date=[{}]",
          filtersDto.getDisturbanceDateStart());
      builder.append(
          "AND cboa.DISTURBANCE_START_DATE >= to_timestamp(:disturbStartDate, 'YYYY-MM-DD');");
    }
    // 7. Disturbance end date
    if (filtersDto.hasValue(filtersDto._DISTURBANCE_DATE_END)) {
      log.info(
          "Disturbance end date filter detected! date=[{}]", filtersDto.getDisturbanceDateEnd());
      builder.append(
          "AND cboa.DISTURBANCE_START_DATE <= to_timestamp(:disturbEndDate, 'YYYY-MM-DD');");
    }
    // 8. Regen delay start date
    // 9. Regen delay end date
    // 10. Free growing start date
    // 11. Free growing end date
    // 12. Update date start
    if (filtersDto.hasValue(filtersDto._UPDATE_DATE_END)) {
      log.info("Update start date filter detected! date=[{}]", filtersDto.getUpdateDateStart());
      builder.append("AND o.UPDATE_TIMESTAMP <= to_timestamp(:updateStartDate, 'YYYY-MM-DD');");
    }
    // 13. Update date end
    if (filtersDto.hasValue(filtersDto._UPDATE_DATE_END)) {
      log.info("Update end date filter detected! date=[{}]", filtersDto.getUpdateDateEnd());
      builder.append("AND o.UPDATE_TIMESTAMP <= to_timestamp(:updateEndDate, 'YYYY-MM-DD');");
    }

    // Log query
    log.info("Final query: {}", builder.toString());
    return builder.toString();
  }
}
