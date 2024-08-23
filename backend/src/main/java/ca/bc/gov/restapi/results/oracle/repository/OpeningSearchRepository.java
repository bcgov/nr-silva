package ca.bc.gov.restapi.results.oracle.repository;

import ca.bc.gov.restapi.results.common.pagination.PaginatedResult;
import ca.bc.gov.restapi.results.common.pagination.PaginationParameters;
import ca.bc.gov.restapi.results.oracle.dto.OpeningSearchFiltersDto;
import ca.bc.gov.restapi.results.oracle.dto.OpeningSearchResponseDto;
import ca.bc.gov.restapi.results.oracle.enums.OpeningCategoryEnum;
import ca.bc.gov.restapi.results.oracle.enums.OpeningStatusEnum;
import ca.bc.gov.restapi.results.oracle.util.PaginationUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/** This class represents the Openings Search repository database access. */
@Slf4j
@Component
@AllArgsConstructor
public class OpeningSearchRepository {

  private final EntityManager em;

  /**
   * Search Opening with filters.
   *
   * @param filtersDto All possible filters to search openings.
   * @param pagination Pagination parameters with pagination settings.
   * @return Paginated result with found records, if any.
   */
  public PaginatedResult<OpeningSearchResponseDto> searchOpeningQuery(
      OpeningSearchFiltersDto filtersDto, PaginationParameters pagination) {

    final String sqlQuery = createNativeSqlQuery(filtersDto);
    final Query query = setQueryParameters(filtersDto, sqlQuery);

    // Limit to 100 records at the database
    query.setMaxResults(100);

    List<?> result = query.getResultList();
    int lastPage = PaginationUtil.getLastPage(result.size(), pagination.perPage());

    PaginatedResult<OpeningSearchResponseDto> paginatedResult = new PaginatedResult<>();
    paginatedResult.setPageIndex(pagination.page());
    paginatedResult.setPerPage(pagination.perPage());
    paginatedResult.setTotalPages(lastPage);

    if (result.isEmpty() || pagination.page() > lastPage) {
      log.info("No search openings result for the search given page index and size!");
      paginatedResult.setData(List.of());
      paginatedResult.setTotalPages(result.isEmpty() ? 0 : lastPage);
      paginatedResult.setHasNextPage(false);
      return paginatedResult;
    }

    int startIndex = PaginationUtil.getStartIndex(pagination.page(), pagination.perPage());
    int endIndex = PaginationUtil.getEndIndex(startIndex, pagination.perPage(), result.size());

    List<OpeningSearchResponseDto> resultList =
        buildResultListDto(result.subList(startIndex, endIndex));

    paginatedResult.setData(resultList);
    paginatedResult.setPerPage(resultList.size());
    paginatedResult.setTotalPages(lastPage);
    paginatedResult.setHasNextPage(pagination.page() < lastPage && pagination.page() > 0);

    return paginatedResult;
  }

  private List<OpeningSearchResponseDto> buildResultListDto(List<?> result) {
    List<OpeningSearchResponseDto> resultList = new ArrayList<>();

    for (Object obj : result) {
      if (obj.getClass().isArray()) {
        Object[] row = (Object[]) obj;
        OpeningSearchResponseDto searchOpeningDto = new OpeningSearchResponseDto();
        if (row.length > 0) {
          searchOpeningDto.setOpeningId(getValue(Integer.class, row[0], "openingId"));
        }

        if (row.length > 1) {
          String openingNumber = getValue(String.class, row[1], "openingNumber");
          if (!Objects.isNull(openingNumber)) {
            searchOpeningDto.setOpeningNumber(openingNumber.trim());
          }
        }

        if (row.length > 2) {
          String category = getValue(String.class, row[2], "category");
          searchOpeningDto.setCategory(OpeningCategoryEnum.of(category));
        }

        if (row.length > 3) {
          String status = getValue(String.class, row[3], "status");
          searchOpeningDto.setStatus(OpeningStatusEnum.of(status));
        }

        if (row.length > 4) {
          String cuttingPermitId = getValue(String.class, row[4], "cuttingPermitId");
          searchOpeningDto.setCuttingPermitId(cuttingPermitId);
        }

        if (row.length > 5) {
          String timberMark = getValue(String.class, row[5], "timberMark");
          searchOpeningDto.setTimberMark(timberMark);
        }

        if (row.length > 6) {
          String cutBlockId = getValue(String.class, row[6], "cutBlockId");
          searchOpeningDto.setCutBlockId(cutBlockId);
        }

        if (row.length > 7) {
          BigDecimal openingGrossAreaHa = getValue(BigDecimal.class, row[7], "openingGrossAreaHa");
          searchOpeningDto.setOpeningGrossAreaHa(openingGrossAreaHa);
        }

        if (row.length > 8) {
          Timestamp startDate = getValue(Timestamp.class, row[8], "disturbanceStartDate");
          if (!Objects.isNull(startDate)) {
            searchOpeningDto.setDisturbanceStartDate(startDate.toLocalDateTime());
          }
        }

        if (row.length > 9) {
          Integer fileId = getValue(Integer.class, row[9], "fileId");
          searchOpeningDto.setFileId(fileId);
        }

        if (row.length > 10) {
          String orgUnitCode = getValue(String.class, row[10], "orgUnitCode");
          searchOpeningDto.setOrgUnitCode(orgUnitCode);
        }

        if (row.length > 11) {
          String orgUnitName = getValue(String.class, row[11], "orgUnitName");
          searchOpeningDto.setOrgUnitName(orgUnitName);
        }

        if (row.length > 12) {
          String clientNumber = getValue(String.class, row[12], "clientNumber");
          searchOpeningDto.setClientNumber(clientNumber);
        }

        // HERE
        if (row.length > 13) {
          searchOpeningDto.setRegenDelayDate(getValue(String.class, row[13], "regenTemporary"));
        }

        // HERE
        if (row.length > 14) {
          searchOpeningDto.setFreeGrowingDate(getValue(String.class, row[14], "freeGrowTemporary"));
        }

        if (row.length > 15) {
          Timestamp updateTimestamp = getValue(Timestamp.class, row[15], "updateTimestamp");
          searchOpeningDto.setUpdateTimestamp(updateTimestamp.toLocalDateTime());
        }

        if (row.length > 16) {
          String entryUserId = getValue(String.class, row[16], "entryUserId");
          searchOpeningDto.setEntryUserId(entryUserId);
        }

        if (row.length > 17) {
          BigDecimal silvaReliefAppId = getValue(BigDecimal.class, row[17], "submittedToFrpa108");
          boolean submittedApp = silvaReliefAppId.compareTo(BigDecimal.ZERO) > 0;
          searchOpeningDto.setSubmittedToFrpa(submittedApp);
          if (submittedApp) {
            searchOpeningDto.setSilvaReliefAppId(silvaReliefAppId.longValue());
          }
        }

        // fetch from forestClient API
        searchOpeningDto.setClientAcronym(null);

        resultList.add(searchOpeningDto);
      }
    }

    return resultList;
  }

  private <T> T getValue(Class<T> clazz, Object obj, String name) {
    if (Objects.isNull(obj)) {
      log.debug("{} is null", name);
      return null;
    }
    if (clazz.equals(Integer.class) && obj instanceof Integer intVal) {
      log.debug("Integer {}={}", name, intVal);
      return clazz.cast(obj);
    }
    if (clazz.equals(String.class) && obj instanceof String strVal) {
      log.debug("String {}={}", name, strVal);
      return clazz.cast(obj);
    }
    if (clazz.equals(LocalDateTime.class) && obj instanceof LocalDateTime localDateTime) {
      log.debug("LocalDateTime {}={}", name, localDateTime);
      return clazz.cast(obj);
    }
    if (clazz.equals(BigDecimal.class) && obj instanceof BigDecimal bigDecValue) {
      log.debug("BigDecimal {}={}", name, bigDecValue);
      return clazz.cast(obj);
    }
    if (clazz.equals(Timestamp.class) && obj instanceof Timestamp timestamp) {
      log.debug("Timestamp {}={}", name, timestamp);
      return clazz.cast(obj);
    }
    log.info("Unhandled class {} for {}", obj.getClass().getName(), name);
    return null;
  }

  private Query setQueryParameters(OpeningSearchFiltersDto filtersDto, String nativeQuery) {
    Query query = em.createNativeQuery(nativeQuery);

    // set parameters
    if (filtersDto.hasValue(OpeningSearchFiltersDto.MAIN_SEARCH_TERM)) {
      boolean itsNumeric = filtersDto.getMainSearchTerm().replaceAll("[0-9]", "").isEmpty();
      if (itsNumeric) {
        log.info("Setting mainSearchTerm as numeric filter value");
        // Opening id or  File id
        query.setParameter("openingOrFile", filtersDto.getMainSearchTerm());
      } else {
        log.info("Setting mainSearchTerm as non-numeric filter value");
        // Opening number or Timber Mark
        query.setParameter("numberOrTimber", filtersDto.getMainSearchTerm());
      }
    }

    // 1. Org Unit code
    if (filtersDto.hasValue(OpeningSearchFiltersDto.ORG_UNIT)) {
      log.info("Setting orgUnit filter value");
      query.setParameter("orgUnit", filtersDto.getOrgUnit());
    }
    // 2. Category code
    if (filtersDto.hasValue(OpeningSearchFiltersDto.CATEGORY)) {
      log.info("Setting category filter value");
      query.setParameter("category", filtersDto.getCategory());
    }
    // 3. Status code
    if (filtersDto.hasValue(OpeningSearchFiltersDto.STATUS)) {
      log.info("Setting status filter value");
      query.setParameter("status", filtersDto.getStatus());
    }
    // 4. User entry id
    if (filtersDto.hasValue(OpeningSearchFiltersDto.ENTRY_USER_ID)) {
      log.info("Setting entryUserId filter value");
      query.setParameter("entryUserId", "%" + filtersDto.getEntryUserId() + "%");
    }
    // 5. Submitted to FRPA Section 108
    if (filtersDto.hasValue(OpeningSearchFiltersDto.SUBMITTED_TO_FRPA)) {
      log.info("Setting submitted to FRPA filter!");
      // No need to set value since the query already dit it.
    }
    // 6. Disturbance start date
    if (filtersDto.hasValue(OpeningSearchFiltersDto.DISTURBANCE_DATE_START)) {
      log.info("Setting disturbanceDateStart filter value");
      query.setParameter("disturbStartDate", filtersDto.getDisturbanceDateStart());
    }
    // 7. Disturbance end date
    if (filtersDto.hasValue(OpeningSearchFiltersDto.DISTURBANCE_DATE_END)) {
      log.info("Setting disturbanceDateEnd filter value");
      query.setParameter("disturbEndDate", filtersDto.getDisturbanceDateEnd());
    }
    // 8. Regen delay start date
    if (filtersDto.hasValue(OpeningSearchFiltersDto.REGEN_DELAY_DATE_START)) {
      log.info("Skipping filter regenDelayDateStart value. Filter not ready!");
    }
    // 9. Regen delay end date
    if (filtersDto.hasValue(OpeningSearchFiltersDto.REGEN_DELAY_DATE_END)) {
      log.info("Skipping filter regenDelayDateEnd value. Filter not ready!");
    }
    // 10. Free growing start date
    if (filtersDto.hasValue(OpeningSearchFiltersDto.FREE_GROWING_DATE_START)) {
      log.info("Skipping filter freeGrowingDateStart value. Filter not ready!");
    }
    // 11. Free growing end date
    if (filtersDto.hasValue(OpeningSearchFiltersDto.FREE_GROWING_DATE_END)) {
      log.info("Skipping filter freeGrowingDateEnd value. Filter not ready!");
    }
    // 12. Update date start
    if (filtersDto.hasValue(OpeningSearchFiltersDto.UPDATE_DATE_START)) {
      log.info("Setting updateDateStart filter value");
      query.setParameter("updateStartDate", filtersDto.getUpdateDateStart());
    }
    // 13. Update date end
    if (filtersDto.hasValue(OpeningSearchFiltersDto.UPDATE_DATE_END)) {
      log.info("Setting updateDateEnd filter value");
      query.setParameter("updateEndDate", filtersDto.getUpdateDateEnd());
    }

    return query;
  }

  private String createNativeSqlQuery(OpeningSearchFiltersDto filtersDto) {
    StringBuilder builder = new StringBuilder();
    builder.append("SELECT o.OPENING_ID AS openingId");
    builder.append(",o.OPENING_NUMBER AS openingNumber");
    builder.append(",o.OPEN_CATEGORY_CODE AS category");
    builder.append(",o.OPENING_STATUS_CODE AS status");
    builder.append(",cboa.CUTTING_PERMIT_ID AS cuttingPermitId");
    builder.append(",cboa.TIMBER_MARK AS timberMark");
    builder.append(",cboa.CUT_BLOCK_ID AS cutBlockId");
    builder.append(",cboa.OPENING_GROSS_AREA AS openingGrossArea");
    builder.append(",cboa.DISTURBANCE_START_DATE AS disturbanceStartDate");
    builder.append(",oa.OPENING_ATTACHMENT_FILE_ID AS fileId");
    builder.append(",ou.ORG_UNIT_CODE AS orgUnitCode");
    builder.append(",ou.ORG_UNIT_NAME AS orgUnitName");
    builder.append(",res.CLIENT_NUMBER AS clientNumber");
    builder.append(",'TDB' AS regenDelayDate");
    builder.append(",'TBD' AS freeGrowingDate");
    builder.append(",o.UPDATE_TIMESTAMP AS updateTimestamp");
    builder.append(",o.ENTRY_USERID AS entryUserId");
    builder.append(",COALESCE(sra.SILV_RELIEF_APPLICATION_ID, 0) AS submittedToFrpa108 ");
    builder.append("FROM THE.OPENING o ");
    builder.append("LEFT JOIN THE.OPENING_ATTACHMENT oa ON (oa.OPENING_ID = o.OPENING_ID)");
    builder.append("LEFT JOIN THE.CUT_BLOCK_OPEN_ADMIN cboa ON (cboa.OPENING_ID = o.OPENING_ID)");
    builder.append("LEFT JOIN THE.ORG_UNIT ou ON (ou.ORG_UNIT_NO = o.ADMIN_DISTRICT_NO)");
    builder.append("LEFT JOIN the.RESULTS_ELECTRONIC_SUBMISSION res ON (");
    builder.append(" res.RESULTS_SUBMISSION_ID = o.RESULTS_SUBMISSION_ID)");
    builder.append("LEFT JOIN THE.CLIENT_ACRONYM ca ON (ca.CLIENT_NUMBER = res.CLIENT_NUMBER) ");
    builder.append("LEFT JOIN THE.ACTIVITY_TREATMENT_UNIT atu ON (atu.OPENING_ID = o.OPENING_ID)");
    builder.append("LEFT JOIN THE.SILV_RELIEF_APPLICATION sra ON (");
    builder.append(" sra.ACTIVITY_TREATMENT_UNIT_ID = atu.ACTIVITY_TREATMENT_UNIT_ID");
    builder.append(" AND sra.SILV_RELIEF_APPL_STATUS_CODE = 'APP')");
    builder.append("WHERE 1=1 ");

    /* Filters */
    // 0. Main number filter [opening_id, opening_number, timber_mark, file_id]
    // if it's a number, filter by openingId or fileId, otherwise filter by timber mark and opening
    // number
    if (filtersDto.hasValue(OpeningSearchFiltersDto.MAIN_SEARCH_TERM)) {
      log.info("Filter mainSearchTerm detected! mainSearchTerm={}", filtersDto.getMainSearchTerm());
      boolean itsNumeric = filtersDto.getMainSearchTerm().replaceAll("[0-9]", "").isEmpty();
      if (itsNumeric) {
        log.info("Filter mainSearchTerm it's numeric! Looking for Opening ID or File ID");
        // Opening id or  File id
        builder.append("AND (o.OPENING_ID = :openingOrFile or ");
        builder.append("oa.OPENING_ATTACHMENT_FILE_ID = :openingOrFile) ");
      } else {
        log.info("Filter mainSearchTerm NOT numeric! Looking for Opening Number or Timber Mark");
        // Opening number or Timber Mark
        builder.append(
            "AND (o.OPENING_NUMBER = :numberOrTimber or cboa.TIMBER_MARK = :numberOrTimber) ");
      }
    }

    // 1. Org Unit code
    if (filtersDto.hasValue(OpeningSearchFiltersDto.ORG_UNIT)) {
      log.info("Filter orgUnit detected! orgUnit={}", filtersDto.getOrgUnit());
      builder.append("AND ou.ORG_UNIT_CODE = :orgUnit ");
    }
    // 2. Category code
    if (filtersDto.hasValue(OpeningSearchFiltersDto.CATEGORY)) {
      log.info("Filter category detected! category={}", filtersDto.getCategory());
      builder.append("AND o.OPEN_CATEGORY_CODE = :category ");
    }
    // 3. Status code
    if (filtersDto.hasValue(OpeningSearchFiltersDto.STATUS)) {
      log.info("Filter status detected! status={}", filtersDto.getStatus());
      builder.append("AND o.OPENING_STATUS_CODE = :status ");
    }
    // 4. Entry user id
    if (filtersDto.hasValue(OpeningSearchFiltersDto.ENTRY_USER_ID)) {
      log.info("Filter entryUserId detected! entryUserId={}", filtersDto.getEntryUserId());
      builder.append("AND o.ENTRY_USERID LIKE :entryUserId ");
    }
    // 5. Submitted to FRPA
    if (filtersDto.hasValue(OpeningSearchFiltersDto.SUBMITTED_TO_FRPA)) {
      Boolean value = filtersDto.getSubmittedToFrpa();
      if (Boolean.FALSE.equals(value)) {
        log.info(
            "Filter submitted to FRPA detected! submitted={}", filtersDto.getSubmittedToFrpa());
        builder.append("AND sra.SILV_RELIEF_APPLICATION_ID IS NULL ");
      } else {
        log.info(
            "Filter submitted to FRPA detected! submitted={}", filtersDto.getSubmittedToFrpa());
        builder.append("AND sra.SILV_RELIEF_APPLICATION_ID IS NOT NULL ");
      }
    }
    // 6. Disturbance start date
    if (filtersDto.hasValue(OpeningSearchFiltersDto.DISTURBANCE_DATE_START)) {
      log.info(
          "Filter disturbanceDateStart detected! date={}", filtersDto.getDisturbanceDateStart());
      builder.append(
          "AND cboa.DISTURBANCE_START_DATE >= to_timestamp(:disturbStartDate, 'YYYY-MM-DD') ");
    }
    // 7. Disturbance end date
    if (filtersDto.hasValue(OpeningSearchFiltersDto.DISTURBANCE_DATE_END)) {
      log.info("Filter disturbanceDateEnd detected! date={}", filtersDto.getDisturbanceDateEnd());
      builder.append(
          "AND cboa.DISTURBANCE_START_DATE <= to_timestamp(:disturbEndDate, 'YYYY-MM-DD') ");
    }
    // 8. Regen delay start date
    if (filtersDto.hasValue(OpeningSearchFiltersDto.REGEN_DELAY_DATE_START)) {
      log.info("Filter regenDelayDateStart detected! date={}", filtersDto.getRegenDelayDateStart());
      log.info("Skipping Filter regenDelayDateStart. Filter not ready!");
    }
    // 9. Regen delay end date
    if (filtersDto.hasValue(OpeningSearchFiltersDto.REGEN_DELAY_DATE_END)) {
      log.info("Filter regenDelayDateEnd detected! date={}", filtersDto.getRegenDelayDateEnd());
      log.info("Skipping Filter regenDelayDateEnd. Filter not ready!");
    }
    // 10. Free growing start date
    if (filtersDto.hasValue(OpeningSearchFiltersDto.FREE_GROWING_DATE_START)) {
      log.info(
          "Filter freeGrowingDateStart detected! date={}", filtersDto.getFreeGrowingDateStart());
      log.info("Skipping Filter freeGrowingDateStart. Filter not ready!");
    }
    // 11. Free growing end date
    if (filtersDto.hasValue(OpeningSearchFiltersDto.FREE_GROWING_DATE_END)) {
      log.info("Filter freeGrowingDateEnd detected! date={}", filtersDto.getFreeGrowingDateEnd());
      log.info("Skipping Filter freeGrowingDateEnd. Filter not ready!");
    }
    // 12. Update date start
    if (filtersDto.hasValue(OpeningSearchFiltersDto.UPDATE_DATE_START)) {
      log.info("Filter updateDateStart detected! date={}", filtersDto.getUpdateDateStart());
      builder.append("AND o.UPDATE_TIMESTAMP >= to_timestamp(:updateStartDate, 'YYYY-MM-DD') ");
    }
    // 13. Update date end
    if (filtersDto.hasValue(OpeningSearchFiltersDto.UPDATE_DATE_END)) {
      log.info("Filter updateDateEnd detected! date={}", filtersDto.getUpdateDateEnd());
      builder.append("AND o.UPDATE_TIMESTAMP <= to_timestamp(:updateEndDate, 'YYYY-MM-DD') ");
    }

    // Order by
    builder.append("ORDER BY o.OPENING_ID DESC");

    return builder.toString();
  }
}
