package ca.bc.gov.restapi.results.common.pagination;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import java.util.Set;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.data.domain.PageRequest;

@DisplayName("Unit Test | Pagination Parameters")
class PaginationParametersTest {

  private static Validator validator;

  @BeforeAll
  static void setUp() {
    ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    validator = factory.getValidator();
  }

  @ParameterizedTest
  @CsvSource({
      "2, 10, 2, 10, 20, 2, 20",
      "1, 10, 1, 10, 15, 1, 15",
  })
  @DisplayName("Should check valid values")
  void shouldCheckValidValues(
      Integer page,
      Integer perPage,
      int expectedPage,
      int expectedPerPage,
      int maxPageSize,
      int expectedPageablePage,
      int expectedPageableSize
  ) {
    PaginationParameters params = new PaginationParameters(page, perPage);
    assertEquals(expectedPage, params.page());
    assertEquals(expectedPerPage, params.perPage());
    assertEquals(PageRequest.of(expectedPageablePage, expectedPageableSize),
        params.toPageable(maxPageSize));
    Set<ConstraintViolation<PaginationParameters>> violations = validator.validate(params);
    assertTrue(violations.isEmpty());
  }

  @ParameterizedTest
  @CsvSource({
      "-1, 5",
      "1, -5"
  })
  @DisplayName("Should check invalid values")
  void shouldCheckInvalidValues(
      Integer page,
      Integer perPage
  ) {
    PaginationParameters params = new PaginationParameters(page, perPage);
    Set<ConstraintViolation<PaginationParameters>> violations = validator.validate(params);
    assertEquals(1, violations.size());
    assertNotNull(violations.iterator().next().getMessage());
  }

  @Test
  @DisplayName("Should check null values")
  void shouldCheckNullValues() {
    PaginationParameters params = new PaginationParameters(null, 5);
    assertEquals(0, params.page());
    assertEquals(5, params.perPage());
    assertEquals(PageRequest.of(0, 5),
        params.toPageable(5));
    Set<ConstraintViolation<PaginationParameters>> violations = validator.validate(params);
    assertTrue(violations.isEmpty());
  }

  @Test
  @DisplayName("Should check null values perPage")
  void shouldCheckNullValuesPerPage() {
    PaginationParameters params = new PaginationParameters(1, null);
    assertEquals(1, params.page());
    assertEquals(5, params.perPage());
    assertEquals(PageRequest.of(1, 5),
        params.toPageable(5));
    Set<ConstraintViolation<PaginationParameters>> violations = validator.validate(params);
    assertTrue(violations.isEmpty());
  }

}