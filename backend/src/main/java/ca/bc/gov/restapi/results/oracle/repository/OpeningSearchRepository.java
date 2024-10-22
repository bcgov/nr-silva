package ca.bc.gov.restapi.results.oracle.repository;

import ca.bc.gov.restapi.results.common.SilvaConstants;
import ca.bc.gov.restapi.results.common.pagination.PaginatedResult;
import ca.bc.gov.restapi.results.common.pagination.PaginationParameters;
import ca.bc.gov.restapi.results.oracle.dto.OpeningSearchFiltersDto;
import ca.bc.gov.restapi.results.oracle.dto.OpeningSearchResponseDto;
import ca.bc.gov.restapi.results.oracle.entity.OpeningSearchProjection;
import ca.bc.gov.restapi.results.oracle.util.PaginationUtil;
import jakarta.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

/**
 * This class represents the Openings Search repository database access.
 */
@Slf4j
@Component
@AllArgsConstructor
public class OpeningSearchRepository {

  private final EntityManager em;
  private final OpeningRepository repository;

  /**
   * Search Opening with filters.
   *
   * @param filtersDto All possible filters to search openings.
   * @param pagination Pagination parameters with pagination settings.
   * @return Paginated result with found records, if any.
   */
  public PaginatedResult<OpeningSearchResponseDto> searchOpeningQuery(
      OpeningSearchFiltersDto filtersDto,
      PaginationParameters pagination
  ) {

    List<OpeningSearchProjection> result =  repository.findOpenings(
        filtersDto,
        pagination.toPageable(SilvaConstants.MAX_PAGE_SIZE).getOffset(),
        pagination.toPageable(SilvaConstants.MAX_PAGE_SIZE).getPageSize()
    );

    int lastPage = PaginationUtil.getLastPage(result.size(), pagination.perPage());

    PaginatedResult<OpeningSearchResponseDto> paginatedResult = new PaginatedResult<>();
    paginatedResult.setPageIndex(pagination.page());
    paginatedResult.setPerPage(pagination.perPage());
    paginatedResult.setTotalPages(lastPage);

    if (result.isEmpty() || pagination.page() > lastPage) {
      log.info("No search openings result for the search given page index and size!");
      paginatedResult.setData(List.of());
      paginatedResult.setTotalPages(result.isEmpty() ? 0 : lastPage);
      paginatedResult.setHasNextPage(false);
      return paginatedResult;
    }

    List<OpeningSearchResponseDto> resultList = result
        .stream()
        .map(resultProjection ->
            OpeningSearchResponseDto
                .builder()
                .openingId(resultProjection.getOpeningId().intValue())
                .openingNumber(resultProjection.getOpeningNumber())
                .category(resultProjection.getFinalCategory())
                .status(resultProjection.getFinalStatus())
                .cuttingPermitId(resultProjection.getCuttingPermitId())
                .timberMark(resultProjection.getTimberMark())
                .cutBlockId(resultProjection.getCutBlockId())
                .openingGrossAreaHa(resultProjection.getOpeningGrossArea())
                .disturbanceStartDate(resultProjection.getDisturbanceStartDate())
                .forestFileId(resultProjection.getForestFileId())
                .orgUnitCode(resultProjection.getOrgUnitCode())
                .orgUnitName(resultProjection.getOrgUnitName())
                .clientNumber(resultProjection.getClientNumber())
                .clientLocation(resultProjection.getClientLocation())
                .regenDelayDate(resultProjection.getRegenDelayDate())
                .earlyFreeGrowingDate(resultProjection.getEarlyFreeGrowingDate())
                .lateFreeGrowingDate(resultProjection.getLateFreeGrowingDate())
                .updateTimestamp(resultProjection.getUpdateTimestamp())
                .entryUserId(resultProjection.getEntryUserId())
                .submittedToFrpa(resultProjection.getSubmittedToFrpa())
                .silvaReliefAppId(resultProjection.getSilvaReliefAppId())
                .build()
        )
        .toList();

    paginatedResult.setData(resultList);
    paginatedResult.setPerPage(resultList.size());
    paginatedResult.setTotalPages(lastPage);
    paginatedResult.setHasNextPage(pagination.page() < lastPage && pagination.page() > 0);

    return paginatedResult;
  }

}
