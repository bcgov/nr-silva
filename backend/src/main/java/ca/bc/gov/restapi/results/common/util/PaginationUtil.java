package ca.bc.gov.restapi.results.common.util;

import ca.bc.gov.restapi.results.common.exception.InvalidSortingFieldException;
import java.util.Map;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Sort;

/**
 * This class contains methods for manual pagination.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PaginationUtil {

  public static Sort resolveSort(
      Sort receivedSort,
      String defaultSortField,
      Map<String, String> sortableFields
  ) {
    if (receivedSort == null || receivedSort.isUnsorted()) {
      return Sort.by(Sort.Order.asc(defaultSortField));
    }
    return Sort.by(
        receivedSort
            .stream()
            .map(order -> {
              String dbField = sortableFields.get(order.getProperty());
              if (dbField == null) {
                throw new InvalidSortingFieldException(order.getProperty());
              }
              return new Sort.Order(order.getDirection(), dbField);
            })
            .toList()
    );
  }

}
