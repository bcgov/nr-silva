package ca.bc.gov.restapi.results.oracle.repository;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import ca.bc.gov.restapi.results.common.pagination.PaginatedResult;
import ca.bc.gov.restapi.results.common.pagination.PaginationParameters;
import ca.bc.gov.restapi.results.oracle.dto.OpeningSearchFiltersDto;
import ca.bc.gov.restapi.results.oracle.dto.OpeningSearchResponseDto;
import ca.bc.gov.restapi.results.oracle.enums.OpeningCategoryEnum;
import ca.bc.gov.restapi.results.oracle.enums.OpeningStatusEnum;
import jakarta.persistence.EntityManager;
import jakarta.persistence.FlushModeType;
import jakarta.persistence.LockModeType;
import jakarta.persistence.Parameter;
import jakarta.persistence.Query;
import jakarta.persistence.TemporalType;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
class OpeningSearchRepositoryTest {

  @Mock EntityManager entityManager;

  private OpeningSearchRepository openingSearchRepository;

  private OpeningSearchFiltersDto mockFilter(
      String orgUnit,
      String category,
      String status,
      String entryUserId,
      Boolean submittedToFrpa,
      String disturbanceDateStart,
      String disturbanceDateEnd,
      String regenDelayDateStart,
      String regenDelayDateEnd,
      String freeGrowingDateStart,
      String freeGrowingDateEnd,
      String updateDateStart,
      String updateDateEnd,
      String mainSearchTerm) {
    return new OpeningSearchFiltersDto(
        orgUnit,
        category,
        status,
        entryUserId,
        submittedToFrpa,
        disturbanceDateStart,
        disturbanceDateEnd,
        regenDelayDateStart,
        regenDelayDateEnd,
        freeGrowingDateStart,
        freeGrowingDateEnd,
        updateDateStart,
        updateDateEnd,
        mainSearchTerm);
  }

  private OpeningSearchFiltersDto mockOrgUnit(String orgUnit) {
    return mockFilter(
        orgUnit, null, null, null, null, null, null, null, null, null, null, null, null, null);
  }

  private OpeningSearchFiltersDto mockAllFilters() {
    return mockFilter(
        "DCR",
        "FTML",
        "APP",
        "TEST",
        false,
        "2023-01-01",
        "2023-05-31",
        "2020-01-01",
        "2023-12-31",
        "2024-01-01",
        "2024-06-30",
        "2024-01-01",
        "2025-01-01",
        "407");
  }

  private OpeningSearchFiltersDto mockMainFilter(String mainSearchTerm) {
    return mockFilter(
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        mainSearchTerm);
  }

  private Query mockQuery(List<?> resultList) {
    Query query =
        new Query() {

          @Override
          public List<?> getResultList() {
            return resultList;
          }

          @Override
          public Object getSingleResult() {
            return null;
          }

          @Override
          public int executeUpdate() {
            return 0;
          }

          @Override
          public Query setMaxResults(int maxResult) {
            return this;
          }

          @Override
          public int getMaxResults() {
            return 0;
          }

          @Override
          public Query setFirstResult(int startPosition) {
            return this;
          }

          @Override
          public int getFirstResult() {
            return 0;
          }

          @Override
          public Query setHint(String hintName, Object value) {
            return this;
          }

          @Override
          public Map<String, Object> getHints() {
            return Map.of();
          }

          @Override
          public <T> Query setParameter(Parameter<T> param, T value) {
            return this;
          }

          @Override
          public Query setParameter(
              Parameter<Calendar> param, Calendar value, TemporalType temporalType) {
            return this;
          }

          @Override
          public Query setParameter(Parameter<Date> param, Date value, TemporalType temporalType) {
            return this;
          }

          @Override
          public Query setParameter(String name, Object value) {
            return this;
          }

          @Override
          public Query setParameter(String name, Calendar value, TemporalType temporalType) {
            return this;
          }

          @Override
          public Query setParameter(String name, Date value, TemporalType temporalType) {
            return this;
          }

          @Override
          public Query setParameter(int position, Object value) {
            return this;
          }

          @Override
          public Query setParameter(int position, Calendar value, TemporalType temporalType) {
            return this;
          }

          @Override
          public Query setParameter(int position, Date value, TemporalType temporalType) {
            return this;
          }

          @Override
          public Set<Parameter<?>> getParameters() {
            return Set.of();
          }

          @Override
          public Parameter<?> getParameter(String name) {
            return null;
          }

          @Override
          public <T> Parameter<T> getParameter(String name, Class<T> type) {
            return null;
          }

          @Override
          public Parameter<?> getParameter(int position) {
            return null;
          }

          @Override
          public <T> Parameter<T> getParameter(int position, Class<T> type) {
            return null;
          }

          @Override
          public boolean isBound(Parameter<?> param) {
            return true;
          }

          @Override
          public <T> T getParameterValue(Parameter<T> param) {
            return null;
          }

          @Override
          public Object getParameterValue(String name) {
            return null;
          }

          @Override
          public Object getParameterValue(int position) {
            return null;
          }

          @Override
          public Query setFlushMode(FlushModeType flushMode) {
            return this;
          }

          @Override
          public FlushModeType getFlushMode() {
            return FlushModeType.AUTO;
          }

          @Override
          public Query setLockMode(LockModeType lockMode) {
            return this;
          }

          @Override
          public LockModeType getLockMode() {
            return LockModeType.NONE;
          }

          @Override
          public <T> T unwrap(Class<T> cls) {
            return null;
          }
        };
    return query;
  }

  @BeforeEach
  void setup() {
    openingSearchRepository = new OpeningSearchRepository(entityManager);
  }

  @Test
  @DisplayName("Search opening query main filter numeric should succeed")
  void searchOpeningQuery_mainFilterNumeric_shouldSucceed() {
    OpeningSearchFiltersDto filters = mockMainFilter("407");

    PaginationParameters pagination = new PaginationParameters(0, 10);

    Integer openingId = 123456789;
    String openingNumber = "589";
    OpeningCategoryEnum category = OpeningCategoryEnum.FTML;
    OpeningStatusEnum status = OpeningStatusEnum.APP;
    String cuttingPermitId = null;
    String timberMark = null;
    String cutBlockId = null;
    BigDecimal openingGrossArea = new BigDecimal("11");
    Timestamp disturbanceStartDate = Timestamp.valueOf(LocalDateTime.now());
    Integer fileId = 407;
    String orgUnitCode = null;
    String orgUnitName = null;
    String clientNumber = null;
    String regenTemporary = null;
    String freeGrowTemporary = null;
    Timestamp updateTimestamp = Timestamp.valueOf(LocalDateTime.now());
    String userId = "TEST";
    String submittedToFrpa = "NO";
    List<Object[]> resultList = new ArrayList<>(1);
    resultList.add(
        new Object[] {
          openingId,
          openingNumber,
          category.getCode(),
          status.getCode(),
          cuttingPermitId,
          timberMark,
          cutBlockId,
          openingGrossArea,
          disturbanceStartDate,
          fileId,
          orgUnitCode,
          orgUnitName,
          clientNumber,
          regenTemporary,
          freeGrowTemporary,
          updateTimestamp,
          userId,
          submittedToFrpa,
        });
    Query query = mockQuery(resultList);
    when(entityManager.createNativeQuery(anyString())).thenReturn(query);

    PaginatedResult<OpeningSearchResponseDto> result =
        openingSearchRepository.searchOpeningQuery(filters, pagination);

    Assertions.assertNotNull(result);
    Assertions.assertEquals(0, result.getPageIndex());
    Assertions.assertEquals(1, result.getPerPage());
    Assertions.assertEquals(1, result.getTotalPages());
    Assertions.assertEquals(1, result.getData().size());
    Assertions.assertEquals(openingId, result.getData().get(0).getOpeningId());
    Assertions.assertEquals(openingNumber, result.getData().get(0).getOpeningNumber());
    Assertions.assertEquals(category, result.getData().get(0).getCategory());
    Assertions.assertEquals(status, result.getData().get(0).getStatus());
    Assertions.assertEquals(cuttingPermitId, result.getData().get(0).getCuttingPermitId());
    Assertions.assertEquals(timberMark, result.getData().get(0).getTimberMark());
    Assertions.assertEquals(cutBlockId, result.getData().get(0).getCutBlockId());
    Assertions.assertEquals(openingGrossArea, result.getData().get(0).getOpeningGrossAreaHa());
    Assertions.assertEquals(
        disturbanceStartDate.toLocalDateTime(), result.getData().get(0).getDisturbanceStartDate());
    Assertions.assertEquals(fileId, result.getData().get(0).getFileId());
    Assertions.assertEquals(orgUnitCode, result.getData().get(0).getOrgUnitCode());
    Assertions.assertEquals(orgUnitName, result.getData().get(0).getOrgUnitName());
    Assertions.assertEquals(clientNumber, result.getData().get(0).getClientNumber());
    Assertions.assertEquals(regenTemporary, result.getData().get(0).getRegenDelayDate());
    Assertions.assertEquals(freeGrowTemporary, result.getData().get(0).getFreeGrowingDate());
    Assertions.assertEquals(userId, result.getData().get(0).getEntryUserId());
    Assertions.assertEquals(false, result.getData().get(0).getSubmittedToFrpa());
    Assertions.assertFalse(result.isHasNextPage());
  }

  @Test
  @DisplayName("Search opening query main filter string should succeed")
  void searchOpeningQuery_mainFilterString_shouldSucceed() {
    OpeningSearchFiltersDto filters = mockMainFilter("EM2184");

    PaginationParameters pagination = new PaginationParameters(0, 10);

    Integer openingId = 123456789;
    String openingNumber = "589";
    OpeningCategoryEnum category = OpeningCategoryEnum.FTML;
    OpeningStatusEnum status = OpeningStatusEnum.APP;
    String cuttingPermitId = null;
    String timberMark = "EM2184";
    String cutBlockId = null;
    BigDecimal openingGrossArea = new BigDecimal("11");
    Timestamp disturbanceStartDate = Timestamp.valueOf(LocalDateTime.now());
    ;
    Integer fileId = 407;
    String orgUnitCode = null;
    String orgUnitName = null;
    String clientNumber = null;
    String regenTemporary = null;
    String freeGrowTemporary = null;
    Timestamp updateTimestamp = Timestamp.valueOf(LocalDateTime.now());
    String userId = "TEST";
    String submittedToFrpa = "NO";
    List<Object[]> resultList = new ArrayList<>(1);
    resultList.add(
        new Object[] {
          openingId,
          openingNumber,
          category.getCode(),
          status.getCode(),
          cuttingPermitId,
          timberMark,
          cutBlockId,
          openingGrossArea,
          disturbanceStartDate,
          fileId,
          orgUnitCode,
          orgUnitName,
          clientNumber,
          regenTemporary,
          freeGrowTemporary,
          updateTimestamp,
          userId,
          submittedToFrpa,
        });
    Query query = mockQuery(resultList);
    when(entityManager.createNativeQuery(anyString())).thenReturn(query);

    PaginatedResult<OpeningSearchResponseDto> result =
        openingSearchRepository.searchOpeningQuery(filters, pagination);

    Assertions.assertNotNull(result);
    Assertions.assertEquals(0, result.getPageIndex());
    Assertions.assertEquals(1, result.getPerPage());
    Assertions.assertEquals(1, result.getTotalPages());
    Assertions.assertEquals(1, result.getData().size());
    Assertions.assertEquals(openingId, result.getData().get(0).getOpeningId());
    Assertions.assertEquals(openingNumber, result.getData().get(0).getOpeningNumber());
    Assertions.assertEquals(category, result.getData().get(0).getCategory());
    Assertions.assertEquals(status, result.getData().get(0).getStatus());
    Assertions.assertEquals(cuttingPermitId, result.getData().get(0).getCuttingPermitId());
    Assertions.assertEquals(timberMark, result.getData().get(0).getTimberMark());
    Assertions.assertEquals(cutBlockId, result.getData().get(0).getCutBlockId());
    Assertions.assertEquals(openingGrossArea, result.getData().get(0).getOpeningGrossAreaHa());
    Assertions.assertEquals(
        disturbanceStartDate.toLocalDateTime(), result.getData().get(0).getDisturbanceStartDate());
    Assertions.assertEquals(fileId, result.getData().get(0).getFileId());
    Assertions.assertEquals(orgUnitCode, result.getData().get(0).getOrgUnitCode());
    Assertions.assertEquals(orgUnitName, result.getData().get(0).getOrgUnitName());
    Assertions.assertEquals(clientNumber, result.getData().get(0).getClientNumber());
    Assertions.assertEquals(regenTemporary, result.getData().get(0).getRegenDelayDate());
    Assertions.assertEquals(freeGrowTemporary, result.getData().get(0).getFreeGrowingDate());
    Assertions.assertEquals(userId, result.getData().get(0).getEntryUserId());
    Assertions.assertEquals(false, result.getData().get(0).getSubmittedToFrpa());
    Assertions.assertFalse(result.isHasNextPage());
  }

  @Test
  @DisplayName("Search opening query org unit filter should succeed")
  void searchOpeningQuery_orgUnitFilter_shouldSucceed() {
    OpeningSearchFiltersDto filters = mockOrgUnit("DCR");

    PaginationParameters pagination = new PaginationParameters(0, 10);

    Integer openingId = 123456789;
    String openingNumber = "589";
    OpeningCategoryEnum category = OpeningCategoryEnum.FTML;
    OpeningStatusEnum status = OpeningStatusEnum.SUB;
    String cuttingPermitId = null;
    String timberMark = "EM2184";
    String cutBlockId = null;
    BigDecimal openingGrossArea = new BigDecimal("11");
    Timestamp disturbanceStartDate = Timestamp.valueOf(LocalDateTime.now());
    Integer fileId = 407;
    String orgUnitCode = "DCR";
    String orgUnitName = null;
    String clientNumber = null;
    String regenTemporary = null;
    String freeGrowTemporary = null;
    Timestamp updateTimestamp = Timestamp.valueOf(LocalDateTime.now());
    String userId = "TEST";
    String submittedToFrpa = "NO";
    List<Object[]> resultList = new ArrayList<>(1);
    resultList.add(
        new Object[] {
          openingId,
          openingNumber,
          category.getCode(),
          status.getCode(),
          cuttingPermitId,
          timberMark,
          cutBlockId,
          openingGrossArea,
          disturbanceStartDate,
          fileId,
          orgUnitCode,
          orgUnitName,
          clientNumber,
          regenTemporary,
          freeGrowTemporary,
          updateTimestamp,
          userId,
          submittedToFrpa,
        });
    Query query = mockQuery(resultList);
    when(entityManager.createNativeQuery(anyString())).thenReturn(query);

    PaginatedResult<OpeningSearchResponseDto> result =
        openingSearchRepository.searchOpeningQuery(filters, pagination);

    Assertions.assertNotNull(result);
    Assertions.assertEquals(0, result.getPageIndex());
    Assertions.assertEquals(1, result.getPerPage());
    Assertions.assertEquals(1, result.getTotalPages());
    Assertions.assertEquals(1, result.getData().size());
    Assertions.assertEquals(openingId, result.getData().get(0).getOpeningId());
    Assertions.assertEquals(openingNumber, result.getData().get(0).getOpeningNumber());
    Assertions.assertEquals(category, result.getData().get(0).getCategory());
    Assertions.assertEquals(status, result.getData().get(0).getStatus());
    Assertions.assertEquals(cuttingPermitId, result.getData().get(0).getCuttingPermitId());
    Assertions.assertEquals(timberMark, result.getData().get(0).getTimberMark());
    Assertions.assertEquals(cutBlockId, result.getData().get(0).getCutBlockId());
    Assertions.assertEquals(openingGrossArea, result.getData().get(0).getOpeningGrossAreaHa());
    Assertions.assertEquals(
        disturbanceStartDate.toLocalDateTime(), result.getData().get(0).getDisturbanceStartDate());
    Assertions.assertEquals(fileId, result.getData().get(0).getFileId());
    Assertions.assertEquals(orgUnitCode, result.getData().get(0).getOrgUnitCode());
    Assertions.assertEquals(orgUnitName, result.getData().get(0).getOrgUnitName());
    Assertions.assertEquals(clientNumber, result.getData().get(0).getClientNumber());
    Assertions.assertEquals(regenTemporary, result.getData().get(0).getRegenDelayDate());
    Assertions.assertEquals(freeGrowTemporary, result.getData().get(0).getFreeGrowingDate());
    Assertions.assertEquals(userId, result.getData().get(0).getEntryUserId());
    Assertions.assertEquals(false, result.getData().get(0).getSubmittedToFrpa());
    Assertions.assertFalse(result.isHasNextPage());
  }

  @Test
  @DisplayName("Search opening query all filters should succeed")
  void searchOpeningQuery_allFilters_shouldSucceed() {
    OpeningSearchFiltersDto filters = mockAllFilters();

    PaginationParameters pagination = new PaginationParameters(0, 10);

    Integer openingId = 123456789;
    String openingNumber = "589";
    OpeningCategoryEnum category = OpeningCategoryEnum.of(filters.getCategory());
    OpeningStatusEnum status = OpeningStatusEnum.of(filters.getStatus());
    String cuttingPermitId = "123";
    String timberMark = "EM2184";
    String cutBlockId = "456";
    BigDecimal openingGrossArea = new BigDecimal("11");
    Timestamp disturbanceStartDate = Timestamp.valueOf(LocalDateTime.now());
    Integer fileId = 407;
    String orgUnitCode = filters.getOrgUnit();
    String orgUnitName = "Org Name";
    String clientNumber = "00012797";
    String regenTemporary = "TBD";
    String freeGrowTemporary = "TBD";
    Timestamp updateTimestamp = Timestamp.valueOf(LocalDateTime.now());
    String entryUserId = filters.getEntryUserId();
    String submittedToFrpa = "false";
    List<Object[]> resultList = new ArrayList<>(1);
    resultList.add(
        new Object[] {
          openingId,
          openingNumber,
          category.getCode(),
          status.getCode(),
          cuttingPermitId,
          timberMark,
          cutBlockId,
          openingGrossArea,
          disturbanceStartDate,
          fileId,
          orgUnitCode,
          orgUnitName,
          clientNumber,
          regenTemporary,
          freeGrowTemporary,
          updateTimestamp,
          entryUserId,
          submittedToFrpa,
        });
    Query query = mockQuery(resultList);
    when(entityManager.createNativeQuery(anyString())).thenReturn(query);

    PaginatedResult<OpeningSearchResponseDto> result =
        openingSearchRepository.searchOpeningQuery(filters, pagination);

    Assertions.assertNotNull(result);
    Assertions.assertEquals(0, result.getPageIndex());
    Assertions.assertEquals(1, result.getPerPage());
    Assertions.assertEquals(1, result.getTotalPages());
    Assertions.assertEquals(1, result.getData().size());
    Assertions.assertEquals(openingId, result.getData().get(0).getOpeningId());
    Assertions.assertEquals(openingNumber, result.getData().get(0).getOpeningNumber());
    Assertions.assertEquals(category, result.getData().get(0).getCategory());
    Assertions.assertEquals(status, result.getData().get(0).getStatus());
    Assertions.assertEquals(cuttingPermitId, result.getData().get(0).getCuttingPermitId());
    Assertions.assertEquals(timberMark, result.getData().get(0).getTimberMark());
    Assertions.assertEquals(cutBlockId, result.getData().get(0).getCutBlockId());
    Assertions.assertEquals(openingGrossArea, result.getData().get(0).getOpeningGrossAreaHa());
    Assertions.assertEquals(
        disturbanceStartDate.toLocalDateTime(), result.getData().get(0).getDisturbanceStartDate());
    Assertions.assertEquals(fileId, result.getData().get(0).getFileId());
    Assertions.assertEquals(orgUnitCode, result.getData().get(0).getOrgUnitCode());
    Assertions.assertEquals(orgUnitName, result.getData().get(0).getOrgUnitName());
    Assertions.assertEquals(clientNumber, result.getData().get(0).getClientNumber());
    Assertions.assertEquals(regenTemporary, result.getData().get(0).getRegenDelayDate());
    Assertions.assertEquals(freeGrowTemporary, result.getData().get(0).getFreeGrowingDate());
    Assertions.assertEquals(entryUserId, result.getData().get(0).getEntryUserId());
    Assertions.assertEquals(false, result.getData().get(0).getSubmittedToFrpa());
    Assertions.assertFalse(result.isHasNextPage());
  }

  @Test
  @DisplayName("Search opening query no records found should succeed")
  void searchOpeningQuery_noRecordsFound_shouldSucceed() {
    OpeningSearchFiltersDto filters = mockOrgUnit("AAA");

    PaginationParameters pagination = new PaginationParameters(0, 10);

    List<Object[]> resultList = new ArrayList<>();
    Query query = mockQuery(resultList);
    when(entityManager.createNativeQuery(anyString())).thenReturn(query);

    PaginatedResult<OpeningSearchResponseDto> result =
        openingSearchRepository.searchOpeningQuery(filters, pagination);

    Assertions.assertNotNull(result);
    Assertions.assertEquals(0, result.getPageIndex());
    Assertions.assertEquals(10, result.getPerPage());
    Assertions.assertEquals(0, result.getTotalPages());
    Assertions.assertTrue(result.getData().isEmpty());
    Assertions.assertFalse(result.isHasNextPage());
  }
}
