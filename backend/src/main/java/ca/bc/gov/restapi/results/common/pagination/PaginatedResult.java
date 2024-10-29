package ca.bc.gov.restapi.results.common.pagination;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.With;

/**
 * Holds an API response with pagination information and data.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@With
@Builder
public class PaginatedResult<T> {

  private int pageIndex;
  private int perPage;
  private int totalPages;
  private boolean hasNextPage;
  private List<T> data;
}
