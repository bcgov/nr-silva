package ca.bc.gov.restapi.results.oracle.repository;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import ca.bc.gov.restapi.results.common.pagination.PaginatedResult;
import ca.bc.gov.restapi.results.common.pagination.PaginationParameters;
import ca.bc.gov.restapi.results.oracle.dto.SearchOpeningDto;
import ca.bc.gov.restapi.results.oracle.dto.SearchOpeningFiltersDto;
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

  private SearchOpeningFiltersDto mockFilter(
      String orgUnit,
      String category,
      String status,
      String userId,
      Boolean submittedToFrpa,
      String disturbanceDateStart,
      String disturbanceDateEnd,
      String regenDelayDateStart,
      String regenDelayDateEnd,
      String freeGrowingDateStart,
      String freeGrowingDateEnd,
      String updateDateStart,
      String updateDateEnd,
      String number) {
    return new SearchOpeningFiltersDto(
        orgUnit,
        category,
        status,
        userId,
        submittedToFrpa,
        disturbanceDateStart,
        disturbanceDateEnd,
        regenDelayDateStart,
        regenDelayDateEnd,
        freeGrowingDateStart,
        freeGrowingDateEnd,
        updateDateStart,
        updateDateEnd,
        number);
  }

  private SearchOpeningFiltersDto mockOrgUnit(String orgUnit) {
    return mockFilter(
        orgUnit, null, null, null, null, null, null, null, null, null, null, null, null, null);
  }

  private SearchOpeningFiltersDto mockNumberNumeric(String numberNumeric) {
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
        numberNumeric);
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
  @DisplayName("Search opening query number numeric filter should succeed")
  void searchOpeningQuery_numberFilter_shouldSucceed() {
    SearchOpeningFiltersDto filters = mockNumberNumeric("407");

    PaginationParameters pagination = new PaginationParameters(0, 10);

    Integer openingId = 123456789;
    String openingNumber = "589";
    String category = "FTML";
    String status = "APP";
    String cuttingPermitId = null;
    String timberMark = null;
    String cutBlockId = null;
    BigDecimal grossArea = new BigDecimal("11");
    Timestamp disturbanceStartDate = null;
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
          category,
          status,
          cuttingPermitId,
          timberMark,
          cutBlockId,
          grossArea,
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
    // when(query.setParameter("openingOrFile", "407")).thenReturn(query);

    PaginatedResult<SearchOpeningDto> result =
        openingSearchRepository.searchOpeningQuery(filters, pagination);

    Assertions.assertNotNull(result);
    Assertions.assertEquals(0, result.getPageIndex());
    Assertions.assertEquals(1, result.getPerPage());
    Assertions.assertEquals(1, result.getTotalPages());
    Assertions.assertEquals(1, result.getData().size());
    Assertions.assertEquals(openingId, result.getData().get(0).getOpeningId());
    Assertions.assertFalse(result.isHasNextPage());
  }
}
