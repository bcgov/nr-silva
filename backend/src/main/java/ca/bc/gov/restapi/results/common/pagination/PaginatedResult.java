package ca.bc.gov.restapi.results.common.pagination;

import java.util.List;
import lombok.Getter;
import lombok.Setter;

/** Holds an API response with pagination information and data. */
@Getter
@Setter
public class PaginatedResult<T> {

  private int pageIndex;
  private int perPage;
  private int totalPages;
  private boolean hasNextPage;
  private List<T> data;
}
