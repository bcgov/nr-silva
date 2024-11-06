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
import jakarta.persistence.EntityManagerFactory;
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
  @Mock EntityManagerFactory entityManagerFactory;

  private OpeningSearchRepository openingSearchRepository;

  private OpeningSearchFiltersDto mockFilter(
      List<String> orgUnit,
      List<String> category,
      List<String> statusList,
      Boolean myOpenings,
      Boolean submittedToFrpa,
      String disturbanceDateStart,
      String disturbanceDateEnd,
      String regenDelayDateStart,
      String regenDelayDateEnd,
      String freeGrowingDateStart,
      String freeGrowingDateEnd,
      String updateDateStart,
      String updateDateEnd,
      String cuttingPermitId,
      String cutBlockId,
      String timberMark,
      String mainSearchTerm) {
    return new OpeningSearchFiltersDto(
        orgUnit,
        category,
        statusList,
        myOpenings,
        submittedToFrpa,
        disturbanceDateStart,
        disturbanceDateEnd,
        regenDelayDateStart,
        regenDelayDateEnd,
        freeGrowingDateStart,
        freeGrowingDateEnd,
        updateDateStart,
        updateDateEnd,
        cuttingPermitId,
        cutBlockId,
        timberMark,
        mainSearchTerm);
  }

  private OpeningSearchFiltersDto mockOrgUnit(List<String> orgUnit) {
    return mockFilter(
        orgUnit, null, null, null, null, null, null, null, null, null, null, null, null, null, null,
        null, null);
  }

  private OpeningSearchFiltersDto mockAllFilters() {
    return mockFilter(
        List.of("DCR"),
        List.of("FTML"),
        List.of("APP"),
        true,
        false,
        "2023-01-01",
        "2023-05-31",
        "2020-01-01",
        "2023-12-31",
        "2024-01-01",
        "2024-06-30",
        "2024-01-01",
        "2025-01-01",
        "207",
        "HS",
        "47/206",
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

    when(entityManagerFactory.createEntityManager()).thenReturn(entityManager);

    openingSearchRepository = new OpeningSearchRepository(entityManagerFactory);
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
    String forestFileId = "TFL47";
    String orgUnitCode = null;
    String orgUnitName = null;
    String clientNumber = null;
    String clientLocation = null;
    Timestamp regenDelay = null;
    Timestamp earlyFreeGrowing = null;
    Timestamp lateFreeGrowing = null;
    Timestamp updateTimestamp = Timestamp.valueOf(LocalDateTime.now());
    String userId = "TEST";
    BigDecimal submittedToFrpa108 = new BigDecimal("33");
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
          forestFileId,
          orgUnitCode,
          orgUnitName,
          clientNumber,
          clientLocation,
          regenDelay,
          earlyFreeGrowing,
          lateFreeGrowing,
          updateTimestamp,
          userId,
          submittedToFrpa108,
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

    OpeningSearchResponseDto dto = result.getData().get(0);
    Assertions.assertEquals(openingId, dto.getOpeningId());
    Assertions.assertEquals(openingNumber, dto.getOpeningNumber());
    Assertions.assertEquals(category, dto.getCategory());
    Assertions.assertEquals(status, dto.getStatus());
    Assertions.assertEquals(cuttingPermitId, dto.getCuttingPermitId());
    Assertions.assertEquals(timberMark, dto.getTimberMark());
    Assertions.assertEquals(cutBlockId, dto.getCutBlockId());
    Assertions.assertEquals(openingGrossArea, dto.getOpeningGrossAreaHa());
    Assertions.assertEquals(disturbanceStartDate.toLocalDateTime(), dto.getDisturbanceStartDate());
    Assertions.assertEquals(forestFileId, dto.getForestFileId());
    Assertions.assertEquals(orgUnitCode, dto.getOrgUnitCode());
    Assertions.assertEquals(orgUnitName, dto.getOrgUnitName());
    Assertions.assertEquals(clientNumber, dto.getClientNumber());
    Assertions.assertNull(dto.getRegenDelayDate());
    Assertions.assertNull(dto.getEarlyFreeGrowingDate());
    Assertions.assertNull(dto.getLateFreeGrowingDate());
    Assertions.assertEquals(userId, dto.getEntryUserId());
    Assertions.assertEquals(true, dto.getSubmittedToFrpa());
    Assertions.assertEquals(33L, dto.getSilvaReliefAppId());
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
    String forestFileId = "TFL47";
    String orgUnitCode = null;
    String orgUnitName = null;
    String clientNumber = null;
    String clientLocation = null;
    Timestamp regenDelay = null;
    Timestamp earlyFreeGrowing = null;
    Timestamp lateFreeGrowing = null;
    Timestamp updateTimestamp = Timestamp.valueOf(LocalDateTime.now());
    String userId = "TEST";
    BigDecimal submittedToFrpa = BigDecimal.ZERO;
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
          forestFileId,
          orgUnitCode,
          orgUnitName,
          clientNumber,
          clientLocation,
          regenDelay,
          earlyFreeGrowing,
          lateFreeGrowing,
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

    OpeningSearchResponseDto dto = result.getData().get(0);
    Assertions.assertEquals(openingId, dto.getOpeningId());
    Assertions.assertEquals(openingNumber, dto.getOpeningNumber());
    Assertions.assertEquals(category, dto.getCategory());
    Assertions.assertEquals(status, dto.getStatus());
    Assertions.assertEquals(cuttingPermitId, dto.getCuttingPermitId());
    Assertions.assertEquals(timberMark, dto.getTimberMark());
    Assertions.assertEquals(cutBlockId, dto.getCutBlockId());
    Assertions.assertEquals(openingGrossArea, dto.getOpeningGrossAreaHa());
    Assertions.assertEquals(disturbanceStartDate.toLocalDateTime(), dto.getDisturbanceStartDate());
    Assertions.assertEquals(forestFileId, dto.getForestFileId());
    Assertions.assertEquals(orgUnitCode, dto.getOrgUnitCode());
    Assertions.assertEquals(orgUnitName, dto.getOrgUnitName());
    Assertions.assertEquals(clientNumber, dto.getClientNumber());
    Assertions.assertEquals(clientLocation, dto.getClientLocation());
    Assertions.assertNull(dto.getClientName());
    Assertions.assertNull(dto.getClientAcronym());
    Assertions.assertNull(dto.getRegenDelayDate());
    Assertions.assertNull(dto.getEarlyFreeGrowingDate());
    Assertions.assertNull(dto.getLateFreeGrowingDate());
    Assertions.assertEquals(userId, dto.getEntryUserId());
    Assertions.assertEquals(false, dto.getSubmittedToFrpa());
    Assertions.assertFalse(result.isHasNextPage());
  }

  @Test
  @DisplayName("Search opening query org unit filter should succeed")
  void searchOpeningQuery_orgUnitFilter_shouldSucceed() {
    OpeningSearchFiltersDto filters = mockOrgUnit(List.of("DCR"));

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
    String forestFileId = "TFL47";
    String orgUnitCode = "DCR";
    String orgUnitName = null;
    String clientNumber = null;
    String clientLocation = null;
    Timestamp regenDelay = null;
    Timestamp earlyFreeGrowing = null;
    Timestamp lateFreeGrowing = null;
    Timestamp updateTimestamp = Timestamp.valueOf(LocalDateTime.now());
    String userId = "TEST";
    BigDecimal submittedToFrpa = BigDecimal.ZERO;
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
          forestFileId,
          orgUnitCode,
          orgUnitName,
          clientNumber,
          clientLocation,
          regenDelay,
          earlyFreeGrowing,
          lateFreeGrowing,
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

    OpeningSearchResponseDto dto = result.getData().get(0);
    Assertions.assertEquals(openingId, dto.getOpeningId());
    Assertions.assertEquals(openingNumber, dto.getOpeningNumber());
    Assertions.assertEquals(category, dto.getCategory());
    Assertions.assertEquals(status, dto.getStatus());
    Assertions.assertEquals(cuttingPermitId, dto.getCuttingPermitId());
    Assertions.assertEquals(timberMark, dto.getTimberMark());
    Assertions.assertEquals(cutBlockId, dto.getCutBlockId());
    Assertions.assertEquals(openingGrossArea, dto.getOpeningGrossAreaHa());
    Assertions.assertEquals(disturbanceStartDate.toLocalDateTime(), dto.getDisturbanceStartDate());
    Assertions.assertEquals(forestFileId, dto.getForestFileId());
    Assertions.assertEquals(orgUnitCode, dto.getOrgUnitCode());
    Assertions.assertEquals(orgUnitName, dto.getOrgUnitName());
    Assertions.assertEquals(clientNumber, dto.getClientNumber());
    Assertions.assertEquals(clientLocation, dto.getClientLocation());
    Assertions.assertNull(dto.getClientName());
    Assertions.assertNull(dto.getClientAcronym());
    Assertions.assertNull(dto.getRegenDelayDate());
    Assertions.assertNull(dto.getEarlyFreeGrowingDate());
    Assertions.assertNull(dto.getLateFreeGrowingDate());
    Assertions.assertEquals(userId, dto.getEntryUserId());
    Assertions.assertEquals(false, dto.getSubmittedToFrpa());
    Assertions.assertFalse(result.isHasNextPage());
  }

  @Test
  @DisplayName("Search opening query all filters should succeed")
  void searchOpeningQuery_allFilters_shouldSucceed() {
    OpeningSearchFiltersDto filters = mockAllFilters();

    PaginationParameters pagination = new PaginationParameters(0, 10);

    Integer openingId = 123456789;
    String openingNumber = "589";
    OpeningCategoryEnum category = OpeningCategoryEnum.of("FTML");
    OpeningStatusEnum status = OpeningStatusEnum.of(filters.getStatusList().get(0));
    String cuttingPermitId = "123";
    String timberMark = "EM2184";
    String cutBlockId = "456";
    BigDecimal openingGrossArea = new BigDecimal("11");
    Timestamp disturbanceStartDate = Timestamp.valueOf(LocalDateTime.now());
    String forestFileId = "TFL47";
    String orgUnitCode = "DCR";
    String orgUnitName = "Org Name";
    String clientNumber = "00012797";
    String clientLocation = "00";
    Timestamp regenDelay = null;
    Timestamp earlyFreeGrowing = Timestamp.valueOf(LocalDateTime.now());
    Timestamp lateFreeGrowing = Timestamp.valueOf(LocalDateTime.now());
    Timestamp updateTimestamp = Timestamp.valueOf(LocalDateTime.now());
    String entryUserId = filters.getRequestUserId();
    BigDecimal submittedToFrpa = BigDecimal.ZERO;
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
          forestFileId,
          orgUnitCode,
          orgUnitName,
          clientNumber,
          clientLocation,
          regenDelay,
          earlyFreeGrowing,
          lateFreeGrowing,
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

    OpeningSearchResponseDto dto = result.getData().get(0);
    Assertions.assertEquals(openingId, dto.getOpeningId());
    Assertions.assertEquals(openingNumber, dto.getOpeningNumber());
    Assertions.assertEquals(category, dto.getCategory());
    Assertions.assertEquals(status, dto.getStatus());
    Assertions.assertEquals(cuttingPermitId, dto.getCuttingPermitId());
    Assertions.assertEquals(timberMark, dto.getTimberMark());
    Assertions.assertEquals(cutBlockId, dto.getCutBlockId());
    Assertions.assertEquals(openingGrossArea, dto.getOpeningGrossAreaHa());
    Assertions.assertEquals(disturbanceStartDate.toLocalDateTime(), dto.getDisturbanceStartDate());
    Assertions.assertEquals(forestFileId, dto.getForestFileId());
    Assertions.assertEquals(orgUnitCode, dto.getOrgUnitCode());
    Assertions.assertEquals(orgUnitName, dto.getOrgUnitName());
    Assertions.assertEquals(clientNumber, dto.getClientNumber());
    Assertions.assertEquals(clientLocation, dto.getClientLocation());
    Assertions.assertNull(dto.getRegenDelayDate());
    Assertions.assertEquals(earlyFreeGrowing, Timestamp.valueOf(dto.getEarlyFreeGrowingDate()));
    Assertions.assertEquals(lateFreeGrowing, Timestamp.valueOf(dto.getLateFreeGrowingDate()));
    Assertions.assertEquals(entryUserId, dto.getEntryUserId());
    Assertions.assertEquals(false, dto.getSubmittedToFrpa());
    Assertions.assertFalse(result.isHasNextPage());
  }

  @Test
  @DisplayName("Search opening query no records found should succeed")
  void searchOpeningQuery_noRecordsFound_shouldSucceed() {
    OpeningSearchFiltersDto filters = mockOrgUnit(List.of("AAA"));

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
