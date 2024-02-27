package ca.bc.gov.restapi.results.endpoint.pagination;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

/** Holds an API response with pagination information and data. */
@Getter
@Setter
@Schema(description = "Holds an API response with pagination information and data.")
public class PaginatedResult<T> {

  @Schema(description = "Current page index, zero-based.", example = "2")
  private int pageIndex;

  @Schema(description = "The amount of records per page.", example = "15")
  private int perPage;

  @Schema(description = "The amount of pages", example = "3")
  private int totalPages;

  @Schema(description = "Defines if there's more records to fetch", example = "false")
  private boolean hasNextPage;

  @Schema(description = "List of records, or empty list if no records.")
  private List<T> data;
}
