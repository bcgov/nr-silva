package ca.bc.gov.restapi.results.oracle.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * This class contains methods for manual pagination.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PaginationUtil {

  /**
   * Get the last page number, AKA total number of pages.
   *
   * @param totalElements The total number of elements
   * @param pageSize      The page size
   * @return The last page number, representing the total number of pages.
   */
  public static int getLastPage(int totalElements, int pageSize) {
    return (pageSize == 0) ? 0 : (int) Math.ceil((double) totalElements / pageSize);
  }

  /**
   * Get the start index (offset) when creating a sub list from a list.
   *
   * @param currentPage The current position or page
   * @param pageSize    The page size
   * @return The start index
   */
  public static int getStartIndex(int currentPage, int pageSize) {
    return currentPage * pageSize;
  }

  /**
   * Get the last index (offset) when creating a sub list from a list.
   *
   * @param startIndex    The first position
   * @param pageSize      The page size
   * @param totalElements The total number of elements
   * @return The last index
   */
  public static int getEndIndex(int startIndex, int pageSize, int totalElements) {
    return Math.min(startIndex + pageSize, totalElements);
  }

}
