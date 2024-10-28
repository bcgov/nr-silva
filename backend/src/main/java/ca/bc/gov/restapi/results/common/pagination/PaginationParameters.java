package ca.bc.gov.restapi.results.common.pagination;

import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

/**
 * Pagination parameters to be used in the processing of HTTP GET requests.
 *
 * @param page    The page to be returned. Zero-based, and must be non-negative; defaults to 0
 * @param perPage The maximum number of results in each page. Defaults to 20
 */
public record PaginationParameters(@PositiveOrZero Integer page, @Positive Integer perPage) {

  /**
   * Build an instance of {@link PaginationParameters}, using the default values for {@code page}
   * and {@code perPage} if they're null.
   */
  public PaginationParameters {
    if (page == null) {
      page = 0;
    }
    if (perPage == null) {
      perPage = 5;
    }
  }
}
