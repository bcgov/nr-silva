package ca.bc.gov.restapi.results.oracle.repository;

import ca.bc.gov.restapi.results.common.SilvaConstants;
import ca.bc.gov.restapi.results.common.pagination.PaginatedResult;
import ca.bc.gov.restapi.results.common.pagination.PaginationParameters;
import ca.bc.gov.restapi.results.oracle.SilvaOracleConstants;
import ca.bc.gov.restapi.results.oracle.dto.OpeningSearchFiltersDto;
import ca.bc.gov.restapi.results.oracle.dto.OpeningSearchResponseDto;
import ca.bc.gov.restapi.results.oracle.enums.OpeningCategoryEnum;
import ca.bc.gov.restapi.results.oracle.enums.OpeningStatusEnum;
import ca.bc.gov.restapi.results.oracle.util.PaginationUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Query;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

/** This class represents the Openings Search repository database access. */
@Slf4j
@Component
public class OpeningSearchRepository {

  private final EntityManager em;

  public OpeningSearchRepository(@Qualifier("oracleEntityManagerFactory") EntityManagerFactory emf) {
    this.em = emf.createEntityManager();
  }

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

    // Limit to 500 records at the database
    query.setMaxResults(SilvaConstants.MAX_PAGE_SIZE);

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
      int index = 0;
      if (obj.getClass().isArray()) {
        Object[] row = (Object[]) obj;
        OpeningSearchResponseDto searchOpeningDto = new OpeningSearchResponseDto();
        if (row.length > index) {
          searchOpeningDto.setOpeningId(getValue(Integer.class, row[index++], "openingId"));
        }

        if (row.length > index) {
          String openingNumber = getValue(String.class, row[index++], "openingNumber");
          if (!Objects.isNull(openingNumber)) {
            searchOpeningDto.setOpeningNumber(openingNumber.trim());
          }
        }

        if (row.length > index) {
          String category = getValue(String.class, row[index++], "category");
          searchOpeningDto.setCategory(OpeningCategoryEnum.of(category));
        }

        if (row.length > index) {
          String status = getValue(String.class, row[index++], "status");
          searchOpeningDto.setStatus(OpeningStatusEnum.of(status));
        }

        if (row.length > index) {
          String cuttingPermitId = getValue(String.class, row[index++], "cuttingPermitId");
          searchOpeningDto.setCuttingPermitId(cuttingPermitId);
        }

        if (row.length > index) {
          String timberMark = getValue(String.class, row[index++], "timberMark");
          searchOpeningDto.setTimberMark(timberMark);
        }

        if (row.length > index) {
          String cutBlockId = getValue(String.class, row[index++], "cutBlockId");
          searchOpeningDto.setCutBlockId(cutBlockId);
        }

        if (row.length > index) {
          BigDecimal openingGrossAreaHa =
              getValue(BigDecimal.class, row[index++], "openingGrossAreaHa");
          searchOpeningDto.setOpeningGrossAreaHa(openingGrossAreaHa);
        }

        if (row.length > index) {
          Timestamp startDate = getValue(Timestamp.class, row[index++], "disturbanceStartDate");
          if (!Objects.isNull(startDate)) {
            searchOpeningDto.setDisturbanceStartDate(startDate.toLocalDateTime());
          }
        }

        if (row.length > index) {
          String forestFileId = getValue(String.class, row[index++], "forestFileId");
          searchOpeningDto.setForestFileId(forestFileId);
        }

        if (row.length > index) {
          String orgUnitCode = getValue(String.class, row[index++], "orgUnitCode");
          searchOpeningDto.setOrgUnitCode(orgUnitCode);
        }

        if (row.length > index) {
          String orgUnitName = getValue(String.class, row[index++], "orgUnitName");
          searchOpeningDto.setOrgUnitName(orgUnitName);
        }

        if (row.length > index) {
          String clientNumber = getValue(String.class, row[index++], "clientNumber");
          searchOpeningDto.setClientNumber(clientNumber);
        }

        if (row.length > index) {
          String clientLocation = getValue(String.class, row[index++], "clientLocation");
          searchOpeningDto.setClientLocation(clientLocation);
        }

        if (row.length > index) {
          Timestamp regenDelayDate = getValue(Timestamp.class, row[index++], "regenDelayDate");
          if (!Objects.isNull(regenDelayDate)) {
            searchOpeningDto.setRegenDelayDate(regenDelayDate.toLocalDateTime());
          }
        }

        if (row.length > index) {
          Timestamp earlyDate = getValue(Timestamp.class, row[index++], "earlyFreeGrowingDate");
          if (!Objects.isNull(earlyDate)) {
            searchOpeningDto.setEarlyFreeGrowingDate(earlyDate.toLocalDateTime());
          }
        }

        if (row.length > index) {
          Timestamp dateDate = getValue(Timestamp.class, row[index++], "lateFreeGrowingDate");
          if (!Objects.isNull(dateDate)) {
            searchOpeningDto.setLateFreeGrowingDate(dateDate.toLocalDateTime());
          }
        }

        if (row.length > index) {
          Timestamp updateTimestamp = getValue(Timestamp.class, row[index++], "updateTimestamp");
          searchOpeningDto.setUpdateTimestamp(updateTimestamp.toLocalDateTime());
        }

        if (row.length > index) {
          String entryUserId = getValue(String.class, row[index++], "entryUserId");
          searchOpeningDto.setEntryUserId(entryUserId);
        }

        if (row.length > index) {
          BigDecimal silvaReliefAppId =
              getValue(BigDecimal.class, row[index++], "submittedToFrpa108");
          boolean submittedApp = silvaReliefAppId.compareTo(BigDecimal.ZERO) > 0;
          searchOpeningDto.setSubmittedToFrpa(submittedApp);
          if (submittedApp) {
            searchOpeningDto.setSilvaReliefAppId(silvaReliefAppId.longValue());
          }
        }

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
    if (filtersDto.hasValue(SilvaOracleConstants.MAIN_SEARCH_TERM)) {
      boolean itsNumeric = filtersDto.getMainSearchTerm().replaceAll("[0-9]", "").isEmpty();
      if (itsNumeric) {
        log.info("Setting mainSearchTerm as numeric filter value");
        // Opening id or  File id
        query.setParameter("openingOrFile", filtersDto.getMainSearchTerm());
      } else {
        log.info("Setting mainSearchTerm as non-numeric filter value");
        // Opening number, Timber Mark, or Forest File Id
        query.setParameter("numberOrTimber", filtersDto.getMainSearchTerm());
      }
    }

    // 1. Org Unit code
    if (filtersDto.hasValue(SilvaOracleConstants.ORG_UNIT)) {
      log.info("Setting orgUnit filter values");
      // No need to set value since the query already dit it. Didn't work set through named param
    }
    // 2. Category code
    if (filtersDto.hasValue(SilvaOracleConstants.CATEGORY)) {
      log.info("Setting category filter values");
      // No need to set value since the query already dit it. Didn't work set through named param
    }
    // 3. Status list codes
    if (filtersDto.hasValue(SilvaOracleConstants.STATUS_LIST)) {

      log.info("Setting statusList filter values");
      // No need to set value since the query already dit it. Didn't work set through named param
    }
    // similarly for openingIds
    if (filtersDto.hasValue(SilvaOracleConstants.OPENING_IDS)) {
      log.info("Setting openingIds filter values");
      // No need to set value since the query already dit it. Didn't work set through
      // named param
    }
    // 4. User entry id
    if (filtersDto.hasValue(SilvaOracleConstants.MY_OPENINGS)) {
      log.info("Setting myOpenings filter value");
      query.setParameter("entryUserId", filtersDto.getRequestUserId());
    }
    // 5. Submitted to FRPA Section 108
    if (filtersDto.hasValue(SilvaOracleConstants.SUBMITTED_TO_FRPA)) {
      log.info("Setting submitted to FRPA filter!");
      // No need to set value since the query already dit it.
    }
    // 6. Disturbance start date
    if (filtersDto.hasValue(SilvaOracleConstants.DISTURBANCE_DATE_START)) {
      log.info("Setting disturbanceDateStart filter value");
      query.setParameter("disturbStartDate", filtersDto.getDisturbanceDateStart());
    }
    // 7. Disturbance end date
    if (filtersDto.hasValue(SilvaOracleConstants.DISTURBANCE_DATE_END)) {
      log.info("Setting disturbanceDateEnd filter value");
      query.setParameter("disturbEndDate", filtersDto.getDisturbanceDateEnd());
    }
    // 8. Regen delay start date
    if (filtersDto.hasValue(SilvaOracleConstants.REGEN_DELAY_DATE_START)) {
      log.info("Setting regenDelayDateStart filter value");
      query.setParameter("regenDelayDateStart", filtersDto.getRegenDelayDateStart());
    }
    // 9. Regen delay end date
    if (filtersDto.hasValue(SilvaOracleConstants.REGEN_DELAY_DATE_END)) {
      log.info("Setting regenDelayDateEnd filter value");
      query.setParameter("regenDelayDateEnd", filtersDto.getRegenDelayDateEnd());
    }
    // 10. Free growing start date
    if (filtersDto.hasValue(SilvaOracleConstants.FREE_GROWING_DATE_START)) {
      log.info("Setting freeGrowingDateStart filter value");
      query.setParameter("freeGrowingDateStart", filtersDto.getFreeGrowingDateStart());
    }
    // 11. Free growing end date
    if (filtersDto.hasValue(SilvaOracleConstants.FREE_GROWING_DATE_END)) {
      log.info("Setting freeGrowingDateEnd filter value");
      query.setParameter("freeGrowingDateEnd", filtersDto.getFreeGrowingDateEnd());
    }
    // 12. Update date start
    if (filtersDto.hasValue(SilvaOracleConstants.UPDATE_DATE_START)) {
      log.info("Setting updateDateStart filter value");
      query.setParameter("updateStartDate", filtersDto.getUpdateDateStart());
    }
    // 13. Update date end
    if (filtersDto.hasValue(SilvaOracleConstants.UPDATE_DATE_END)) {
      log.info("Setting updateDateEnd filter value");
      query.setParameter("updateEndDate", filtersDto.getUpdateDateEnd());
    }
    // 14. Cutting permit id
    if (filtersDto.hasValue(SilvaOracleConstants.CUTTING_PERMIT_ID)) {
      log.info("Setting cuttingPermitId filter value");
      query.setParameter("cuttingPermitId", filtersDto.getCuttingPermitId());
    }
    // 15. Cut block id
    if (filtersDto.hasValue(SilvaOracleConstants.CUT_BLOCK_ID)) {
      log.info("Setting cutBlockId filter value");
      query.setParameter("cutBlockId", filtersDto.getCutBlockId());
    }
    // 16. Timber mark
    if (filtersDto.hasValue(SilvaOracleConstants.TIMBER_MARK)) {
      log.info("Setting timberMark filter value");
      query.setParameter("timberMark", filtersDto.getTimberMark());
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
    builder.append(",cboa.FOREST_FILE_ID AS forestFileId");
    builder.append(",ou.ORG_UNIT_CODE AS orgUnitCode");
    builder.append(",ou.ORG_UNIT_NAME AS orgUnitName");
    builder.append(",res.CLIENT_NUMBER AS clientNumber");
    builder.append(",res.CLIENT_LOCN_CODE AS clientLocation");

    String sql;
    sql = ",ADD_MONTHS(cboa.DISTURBANCE_START_DATE, (COALESCE(SMRG.LATE_OFFSET_YEARS,0)*12))";
    builder.append(sql).append(" AS regenDelayDate");

    sql = ",ADD_MONTHS(cboa.DISTURBANCE_START_DATE, (COALESCE(SMFG.EARLY_OFFSET_YEARS,0)*12))";
    builder.append(sql).append(" AS earlyFreeGrowingDate");

    sql = ",ADD_MONTHS(cboa.DISTURBANCE_START_DATE, (COALESCE(SMFG.LATE_OFFSET_YEARS,0)*12))";
    builder.append(sql).append(" AS lateFreeGrowingDate");

    builder.append(",o.UPDATE_TIMESTAMP AS updateTimestamp");
    builder.append(",o.ENTRY_USERID AS entryUserId");
    builder.append(",COALESCE(sra.SILV_RELIEF_APPLICATION_ID, 0) AS submittedToFrpa108 ");
    builder.append("FROM THE.OPENING o ");
    builder.append("LEFT JOIN THE.CUT_BLOCK_OPEN_ADMIN cboa ON (cboa.OPENING_ID = o.OPENING_ID)");
    builder.append("LEFT JOIN THE.ORG_UNIT ou ON (ou.ORG_UNIT_NO = o.ADMIN_DISTRICT_NO)");
    builder.append("LEFT JOIN the.RESULTS_ELECTRONIC_SUBMISSION res ON (");
    builder.append(" res.RESULTS_SUBMISSION_ID = o.RESULTS_SUBMISSION_ID)");
    builder.append("LEFT JOIN THE.CLIENT_ACRONYM ca ON (ca.CLIENT_NUMBER = res.CLIENT_NUMBER) ");
    builder.append("LEFT JOIN THE.ACTIVITY_TREATMENT_UNIT atu ON (atu.OPENING_ID = o.OPENING_ID)");
    builder.append("LEFT JOIN THE.SILV_RELIEF_APPLICATION sra ON (");
    builder.append(" sra.ACTIVITY_TREATMENT_UNIT_ID = atu.ACTIVITY_TREATMENT_UNIT_ID");
    builder.append(" AND sra.SILV_RELIEF_APPL_STATUS_CODE = 'APP') ");
    builder.append("LEFT JOIN THE.STOCKING_STANDARD_UNIT ssu ON (ssu.OPENING_ID = o.OPENING_ID) ");
    builder.append("LEFT JOIN THE.STOCKING_MILESTONE smrg ON (");
    builder.append(" smrg.STOCKING_STANDARD_UNIT_ID = ssu.STOCKING_STANDARD_UNIT_ID");
    builder.append(" AND SMRG.SILV_MILESTONE_TYPE_CODE = 'RG') ");
    builder.append("LEFT JOIN THE.STOCKING_MILESTONE smfg ON (");
    builder.append(" smfg.STOCKING_STANDARD_UNIT_ID = ssu.STOCKING_STANDARD_UNIT_ID");
    builder.append(" AND smfg.SILV_MILESTONE_TYPE_CODE = 'FG') ");
    builder.append("WHERE 1=1 ");

    /* Filters */
    // List of openings from the openingIds of the filterDto object for the recent openings
    if (filtersDto.hasValue(SilvaOracleConstants.OPENING_IDS)) {
      String openingIds = filtersDto.getOpeningIds().stream().map(String::valueOf).collect(
          Collectors.joining(","));
      log.info("Filter for openingIds detected! openingIds={}", openingIds);
      builder.append(String.format("AND o.OPENING_ID IN (%s) ", openingIds));
    }
    // 0. Main number filter [opening_id, opening_number, timber_mark, file_id]
    // if it's a number, filter by openingId or fileId, otherwise filter by timber mark and opening
    // number
    if (filtersDto.hasValue(SilvaOracleConstants.MAIN_SEARCH_TERM)) {
      log.info("Filter mainSearchTerm detected! mainSearchTerm={}", filtersDto.getMainSearchTerm());
      boolean itsNumeric = filtersDto.getMainSearchTerm().replaceAll("[0-9]", "").isEmpty();
      if (itsNumeric) {
        log.info("Filter mainSearchTerm it's numeric! Looking for Opening ID");
        // Opening id
        builder.append("AND o.OPENING_ID = :openingOrFile ");
      } else {
        log.info(
            "Filter mainSearchTerm NOT numeric! Looking for Opening Number, Timber Mark, or Forest"
                + " File Id");
        // Opening number, Timber Mark, or Forest File Id
        builder.append("AND (");
        builder.append("o.OPENING_NUMBER = :numberOrTimber ");
        builder.append(" or cboa.TIMBER_MARK = :numberOrTimber ");
        builder.append(" or cboa.FOREST_FILE_ID = :numberOrTimber)");
      }
    }

    // 1. Org Unit code
    if (filtersDto.hasValue(SilvaOracleConstants.ORG_UNIT)) {
      String orgUnits = String.join(",", filtersDto.getOrgUnit());
      log.info("Filter orgUnit detected! orgUnit={}", orgUnits);  
      builder.append(String.format("AND ou.ORG_UNIT_CODE IN (%s) ", orgUnits));
    }
    // 2. Category code
    if (filtersDto.hasValue(SilvaOracleConstants.CATEGORY)) {
      String categories = String.join(",", filtersDto.getCategory());
      log.info("Filter category detected! statusList={}", categories);
      builder.append(String.format("AND o.OPEN_CATEGORY_CODE IN (%s) ", categories));
    }
    // 3. Status code
    if (filtersDto.hasValue(SilvaOracleConstants.STATUS_LIST)) {
      String statuses = String.join(",", filtersDto.getStatusList());
      log.info("Filter statusList detected! statusList={}", statuses);
      builder.append(String.format("AND o.OPENING_STATUS_CODE IN (%s) ", statuses));
    }
    // 4. My openings
    if (filtersDto.hasValue(SilvaOracleConstants.MY_OPENINGS)) {
      log.info("Filter myOpenings detected! entryUserId={}", filtersDto.getRequestUserId());
      builder.append("AND o.ENTRY_USERID = :entryUserId ");
    }
    // 5. Submitted to FRPA
    if (filtersDto.hasValue(SilvaOracleConstants.SUBMITTED_TO_FRPA)) {
      Boolean value = filtersDto.getSubmittedToFrpa();
      if (Boolean.TRUE.equals(value)) {
        log.info(
            "Filter submitted to FRPA detected! submitted={}", filtersDto.getSubmittedToFrpa());
        builder.append("AND sra.SILV_RELIEF_APPLICATION_ID IS NOT NULL ");
      }
    }
    // 6. Disturbance start date
    if (filtersDto.hasValue(SilvaOracleConstants.DISTURBANCE_DATE_START)) {
      log.info(
          "Filter disturbanceDateStart detected! date={}", filtersDto.getDisturbanceDateStart());
      builder.append(
          "AND cboa.DISTURBANCE_START_DATE >= to_timestamp(:disturbStartDate, 'YYYY-MM-DD') ");
    }
    // 7. Disturbance end date
    if (filtersDto.hasValue(SilvaOracleConstants.DISTURBANCE_DATE_END)) {
      log.info("Filter disturbanceDateEnd detected! date={}", filtersDto.getDisturbanceDateEnd());
      builder.append(
          "AND cboa.DISTURBANCE_START_DATE <= to_timestamp(:disturbEndDate, 'YYYY-MM-DD') ");
    }
    // 8. Regen delay start date
    if (filtersDto.hasValue(SilvaOracleConstants.REGEN_DELAY_DATE_START)) {
      log.info("Filter regenDelayDateStart detected! date={}", filtersDto.getRegenDelayDateStart());
      builder.append("AND ADD_MONTHS(cboa.DISTURBANCE_START_DATE, ");
      builder.append("COALESCE(SMRG.LATE_OFFSET_YEARS,0)*12) ");
      builder.append("> to_timestamp(:regenDelayDateStart,'YYYY-MM-DD')");
    }
    // 9. Regen delay end date
    if (filtersDto.hasValue(SilvaOracleConstants.REGEN_DELAY_DATE_END)) {
      log.info("Filter regenDelayDateEnd detected! date={}", filtersDto.getRegenDelayDateEnd());
      builder.append("AND ADD_MONTHS(cboa.DISTURBANCE_START_DATE, ");
      builder.append("COALESCE(SMRG.LATE_OFFSET_YEARS,0)*12) ");
      builder.append("< to_timestamp(:regenDelayDateEnd,'YYYY-MM-DD')");
    }
    // 10. Free growing start date
    if (filtersDto.hasValue(SilvaOracleConstants.FREE_GROWING_DATE_START)) {
      log.info(
          "Filter freeGrowingDateStart detected! date={}", filtersDto.getFreeGrowingDateStart());
      builder.append("AND ADD_MONTHS(cboa.DISTURBANCE_START_DATE, ");
      builder.append("COALESCE(SMFG.EARLY_OFFSET_YEARS,0)*12) ");
      builder.append("> to_timestamp(:freeGrowingDateStart,'YYYY-MM-DD')");
    }
    // 11. Free growing end date
    if (filtersDto.hasValue(SilvaOracleConstants.FREE_GROWING_DATE_END)) {
      log.info("Filter freeGrowingDateEnd detected! date={}", filtersDto.getFreeGrowingDateEnd());
      builder.append("AND ADD_MONTHS(cboa.DISTURBANCE_START_DATE, ");
      builder.append("COALESCE(SMFG.LATE_OFFSET_YEARS,0)*12) ");
      builder.append("< to_timestamp(:freeGrowingDateEnd, 'YYYY-MM-DD')");
    }
    // 12. Update date start
    if (filtersDto.hasValue(SilvaOracleConstants.UPDATE_DATE_START)) {
      log.info("Filter updateDateStart detected! date={}", filtersDto.getUpdateDateStart());
      builder.append("AND o.UPDATE_TIMESTAMP >= to_timestamp(:updateStartDate, 'YYYY-MM-DD') ");
    }
    // 13. Update date end
    if (filtersDto.hasValue(SilvaOracleConstants.UPDATE_DATE_END)) {
      log.info("Filter updateDateEnd detected! date={}", filtersDto.getUpdateDateEnd());
      builder.append("AND o.UPDATE_TIMESTAMP <= to_timestamp(:updateEndDate, 'YYYY-MM-DD') ");
    }
    // 14. Cutting permit id
    if (filtersDto.hasValue(SilvaOracleConstants.CUTTING_PERMIT_ID)) {
      log.info(
          "Filter cuttingPermitId detected! cuttingPermitId={}", filtersDto.getCuttingPermitId());
      builder.append("AND cboa.CUTTING_PERMIT_ID = :cuttingPermitId ");
    }
    // 15. Cut block id
    if (filtersDto.hasValue(SilvaOracleConstants.CUT_BLOCK_ID)) {
      log.info("Filter cutBlockId detected! cutBlockId={}", filtersDto.getCutBlockId());
      builder.append("AND cboa.CUT_BLOCK_ID = :cutBlockId ");
    }
    // 16. Timber mark
    if (filtersDto.hasValue(SilvaOracleConstants.TIMBER_MARK)) {
      log.info("Filter timberMark detected! timberMark={}", filtersDto.getTimberMark());
      builder.append("AND cboa.TIMBER_MARK = :timberMark ");
    }

    /* Group by - to avoid duplications */
    builder.append("GROUP BY o.OPENING_ID ");
    builder.append(",o.OPENING_NUMBER ");
    builder.append(",o.OPEN_CATEGORY_CODE ");
    builder.append(",o.OPENING_STATUS_CODE ");
    builder.append(",cboa.CUTTING_PERMIT_ID ");
    builder.append(",cboa.TIMBER_MARK ");
    builder.append(",cboa.CUT_BLOCK_ID ");
    builder.append(",cboa.OPENING_GROSS_AREA ");
    builder.append(",cboa.DISTURBANCE_START_DATE ");
    builder.append(",cboa.FOREST_FILE_ID ");
    builder.append(",ou.ORG_UNIT_CODE ");
    builder.append(",ou.ORG_UNIT_NAME ");
    builder.append(",res.CLIENT_NUMBER ");
    builder.append(",res.CLIENT_LOCN_CODE ");

    sql = ",ADD_MONTHS(cboa.DISTURBANCE_START_DATE, (COALESCE(SMRG.LATE_OFFSET_YEARS, 0) * 12)) ";
    builder.append(sql);

    sql = ",ADD_MONTHS(cboa.DISTURBANCE_START_DATE, (COALESCE(SMFG.EARLY_OFFSET_YEARS, 0) * 12)) ";
    builder.append(sql);

    sql = ",ADD_MONTHS(cboa.DISTURBANCE_START_DATE, (COALESCE(SMFG.LATE_OFFSET_YEARS, 0) * 12)) ";
    builder.append(sql);

    builder.append(",o.UPDATE_TIMESTAMP ");
    builder.append(",o.ENTRY_USERID ");
    builder.append(",COALESCE(sra.SILV_RELIEF_APPLICATION_ID, 0) ");

    // Order by
    builder.append("ORDER BY o.OPENING_ID DESC");

    return builder.toString();
  }
}
