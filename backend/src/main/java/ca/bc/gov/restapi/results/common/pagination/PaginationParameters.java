package ca.bc.gov.restapi.results.common.pagination;

import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

/**
 * Pagination parameters to be used in the processing of HTTP GET requests.
 *
 * @param page    The page to be returned. Zero-based, and must be non-negative; defaults to 0
 * @param perPage The maximum number of results in each page. Defaults to 20
 */
public record PaginationParameters(
    @PositiveOrZero(message = "Page number needs to be zero or a positive value")
    Integer page,
    @Positive(message = "Page size needs to be a positive value")
    Integer perPage
) {

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

  public Pageable toPageable(int maxPageSize) {
    return PageRequest.of(page, maxPageSize);
  }

  public Pageable toPageable(Sort sort) {
    return PageRequest.of(page, perPage,sort);
  }
}
