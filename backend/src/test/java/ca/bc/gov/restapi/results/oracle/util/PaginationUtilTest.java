package ca.bc.gov.restapi.results.oracle.util;

import ca.bc.gov.restapi.results.common.exception.InvalidSortingFieldException;
import java.util.Map;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Sort;

@DisplayName("Unit Test | Pagination Util")
class PaginationUtilTest {

  @Test
  @DisplayName("should sort using received valid sort")
  void shouldTestResolveSort() {
    // Given
    Sort receivedSort = Sort.by("field1").ascending();
    String defaultSortField = "defaultField";
    Map<String, String> sortableFields = Map.of("field1", "field1", "field2", "field2");

    // When
    Sort result = PaginationUtil.resolveSort(receivedSort, defaultSortField, sortableFields);

    // Then
    Assertions.assertEquals(Sort.by("field1").ascending(), result);
  }

  @Test
  @DisplayName("should sort using default sort")
  void shouldTestResolveSortWithDefault() {
    // Given
    Sort receivedSort = Sort.unsorted();
    String defaultSortField = "defaultField";
    Map<String, String> sortableFields = Map.of("field1", "field1", "field2", "field2");

    // When
    Sort result = PaginationUtil.resolveSort(receivedSort, defaultSortField, sortableFields);

    // Then
    Assertions.assertEquals(Sort.by("defaultField").ascending(), result);
  }

  @Test
  @DisplayName("should throw exception when invalid field is used")
  void shouldTestResolveSortWithInvalidField() {
    // Given
    Sort receivedSort = Sort.by("invalidField").ascending();
    String defaultSortField = "defaultField";
    Map<String, String> sortableFields = Map.of("field1", "field1", "field2", "field2");

    // When
    Assertions.assertThrows(InvalidSortingFieldException.class, () -> {
      PaginationUtil.resolveSort(receivedSort, defaultSortField, sortableFields);
    });
  }
}
