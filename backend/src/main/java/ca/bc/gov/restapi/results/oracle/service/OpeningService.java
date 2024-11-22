package ca.bc.gov.restapi.results.oracle.service;

import ca.bc.gov.restapi.results.common.SilvaConstants;
import ca.bc.gov.restapi.results.common.dto.ForestClientDto;
import ca.bc.gov.restapi.results.common.exception.MaxPageSizeException;
import ca.bc.gov.restapi.results.common.pagination.PaginatedResult;
import ca.bc.gov.restapi.results.common.pagination.PaginationParameters;
import ca.bc.gov.restapi.results.common.provider.ForestClientApiProvider;
import ca.bc.gov.restapi.results.common.security.LoggedUserService;
import ca.bc.gov.restapi.results.oracle.SilvaOracleConstants;
import ca.bc.gov.restapi.results.oracle.dto.OpeningSearchFiltersDto;
import ca.bc.gov.restapi.results.oracle.dto.OpeningSearchResponseDto;
import ca.bc.gov.restapi.results.oracle.dto.RecentOpeningDto;
import ca.bc.gov.restapi.results.oracle.entity.CutBlockOpenAdminEntity;
import ca.bc.gov.restapi.results.oracle.entity.OpeningEntity;
import ca.bc.gov.restapi.results.oracle.entity.SilvicultureSearchProjection;
import ca.bc.gov.restapi.results.oracle.enums.OpeningCategoryEnum;
import ca.bc.gov.restapi.results.oracle.enums.OpeningStatusEnum;
import ca.bc.gov.restapi.results.oracle.repository.OpeningRepository;
import ca.bc.gov.restapi.results.postgres.service.UserOpeningService;
import jakarta.transaction.Transactional;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

/**
 * This class holds methods for fetching and handling {@link OpeningEntity} in general.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class OpeningService {

  private final OpeningRepository openingRepository;
  private final CutBlockOpenAdminService cutBlockOpenAdminService;
  private final LoggedUserService loggedUserService;
  private final ForestClientApiProvider forestClientApiProvider;
  private final UserOpeningService userOpeningService;

  /**
   * Get recent openings given the opening creation date.
   *
   * @param pagination A {@link PaginationParameters} with pagination settings.
   * @return {@link List} of {@link RecentOpeningDto} containing all recent openings.
   */
  public PaginatedResult<RecentOpeningDto> getRecentOpenings(PaginationParameters pagination) {
    log.info(
        "Getting recent openings, user independent, with page index {} and page size {}",
        pagination.page(),
        pagination.perPage());

    if (pagination.perPage() > SilvaConstants.MAX_PAGE_SIZE) {
      throw new MaxPageSizeException(SilvaConstants.MAX_PAGE_SIZE);
    }

    // Openings
    Pageable pageable =
        PageRequest.of(
            pagination.page(), pagination.perPage(), Sort.by("updateTimestamp").descending());
    Page<OpeningEntity> openingPage = openingRepository.findAll(pageable);

    PaginatedResult<RecentOpeningDto> paginatedResult = new PaginatedResult<>();
    paginatedResult.setPageIndex(pagination.page());
    paginatedResult.setPerPage(pagination.perPage());

    if (openingPage.getContent().isEmpty()) {
      log.info("No recent openings given page index and size!");
      paginatedResult.setData(List.of());
      paginatedResult.setTotalPages(0);
      paginatedResult.setHasNextPage(false);
      return paginatedResult;
    }

    // Cut Block Open Admin
    List<Long> openingIds = openingPage.getContent().stream().map(OpeningEntity::getId).toList();
    List<CutBlockOpenAdminEntity> cutBlocks =
        cutBlockOpenAdminService.findAllByOpeningIdIn(openingIds);

    List<RecentOpeningDto> list = createDtoFromEntity(openingPage.getContent(), cutBlocks);
    paginatedResult.setData(list);
    paginatedResult.setTotalPages(openingPage.getTotalPages());
    paginatedResult.setHasNextPage(openingPage.hasNext());

    return paginatedResult;
  }

  /**
   * Search Opening API.
   *
   * @param filtersDto An instance of {@link OpeningSearchFiltersDto} with all possible filters.
   * @param pagination An instance of {@link PaginationParameters} with pagination settings.
   * @return Paginated result with found content.
   */
  @Transactional
  public PaginatedResult<OpeningSearchResponseDto> openingSearch(
      OpeningSearchFiltersDto filtersDto, PaginationParameters pagination) {
    log.info(
        "Search Openings with page index {} and page size {} with filters {}",
        pagination.page(),
        pagination.perPage(),
        filtersDto
    );

    if (pagination.perPage() > SilvaConstants.MAX_PAGE_SIZE_OPENING_SEARCH) {
      throw new MaxPageSizeException(SilvaConstants.MAX_PAGE_SIZE_OPENING_SEARCH);
    }

    // Set the user in the filter, if required
    if (filtersDto.hasValue(SilvaOracleConstants.MY_OPENINGS) && Boolean.TRUE.equals(
        filtersDto.getMyOpenings())) {
      String userId = loggedUserService.getLoggedUserId().replace("@", "\\");
      if (!userId.startsWith("IDIR")) {
        userId = "BCEID" + userId.substring(5);
      }
      filtersDto.setRequestUserId(userId);
    }
    Page<SilvicultureSearchProjection> searchResultPage =
        openingRepository.searchBy(
            filtersDto,
            pagination.toPageable(Sort.by("opening_id").descending())
        );

    PaginatedResult<OpeningSearchResponseDto> result = new PaginatedResult<>();
    result.setTotalItems(searchResultPage.getTotalElements());
    result.setPageIndex(pagination.page());
    result.setPerPage(pagination.perPage());
    result.setTotalPages(searchResultPage.getTotalPages());
    result.setData(searchResultPage
        .get()
        .map(mapToSearchResponse())
        .toList()
    );

    return fetchClientAcronyms(fetchFavorites(result));
  }

  private PaginatedResult<OpeningSearchResponseDto> fetchClientAcronyms(
      PaginatedResult<OpeningSearchResponseDto> result) {
    Map<String, ForestClientDto> forestClientsMap = new HashMap<>();

    List<String> clientNumbers =
        result
            .getData()
            .stream()
            .map(OpeningSearchResponseDto::getClientNumber)
            .filter(StringUtils::isNotBlank)
            .distinct()
            .toList();

    // Forest client API doesn't have a single endpoint to fetch all at once, so we need to do
    // one request per client number :/
    for (String clientNumber : clientNumbers) {
      Optional<ForestClientDto> dto = forestClientApiProvider.fetchClientByNumber(clientNumber);
      dto.ifPresent(forestClientDto -> forestClientsMap.put(clientNumber, forestClientDto));
    }

    result
        .getData()
        .forEach(response -> {
          if (StringUtils.isNotBlank(response.getClientNumber()) && forestClientsMap.containsKey(
              response.getClientNumber())) {
            ForestClientDto client = forestClientsMap.get(response.getClientNumber());
            response.setClientAcronym(client.acronym());
            response.setClientName(client.clientName());
          }
        });

    return result;
  }

  private PaginatedResult<OpeningSearchResponseDto> fetchFavorites(
      PaginatedResult<OpeningSearchResponseDto> pagedResult
  ) {

    List<Long> favourites = userOpeningService.checkForFavorites(
        pagedResult
            .getData()
            .stream()
            .map(OpeningSearchResponseDto::getOpeningId)
            .map(Integer::longValue)
            .toList()
    );

    for (OpeningSearchResponseDto opening : pagedResult.getData()) {
      opening.setFavourite(favourites.contains(opening.getOpeningId().longValue()));
    }

    return pagedResult;
  }

  private List<RecentOpeningDto> createDtoFromEntity(
      List<OpeningEntity> openings, List<CutBlockOpenAdminEntity> cutBlocks) {
    if (openings.size() != cutBlocks.size()) {
      log.warn("Different number of records for the Opening x Cut Block Open Admin relationship");
    }

    List<RecentOpeningDto> recentOpeningDtos = new ArrayList<>();

    for (int i = 0; i < openings.size(); i++) {
      OpeningEntity opening = openings.get(i);

      String forestFileId = "";
      String cuttingPermitId = "";
      String timberMark = "";
      String cuttingBlockId = "";
      BigDecimal openingGrossArea = BigDecimal.ZERO;
      LocalDate disturbanceStartDate = null;

      if (cutBlocks.size() - 1 >= i) {
        CutBlockOpenAdminEntity cutBlockOpenAdmin = cutBlocks.get(i);
        forestFileId = cutBlockOpenAdmin.getForestFileId();
        cuttingPermitId = cutBlockOpenAdmin.getCuttingPermitId();
        timberMark = cutBlockOpenAdmin.getTimberMark();
        cuttingBlockId = cutBlockOpenAdmin.getCutBlockId();
        openingGrossArea = cutBlockOpenAdmin.getOpeningGrossArea();
        disturbanceStartDate = cutBlockOpenAdmin.getDisturbanceEndDate();
      }

      RecentOpeningDto openingDto =
          new RecentOpeningDto(
              opening.getId(),
              forestFileId,
              cuttingPermitId,
              timberMark,
              cuttingBlockId,
              openingGrossArea,
              OpeningStatusEnum.of(opening.getStatus()),
              OpeningCategoryEnum.of(opening.getCategory()),
              disturbanceStartDate,
              opening.getEntryTimestamp(),
              opening.getUpdateTimestamp());

      recentOpeningDtos.add(openingDto);
    }

    return recentOpeningDtos;
  }


  private static Function<SilvicultureSearchProjection, OpeningSearchResponseDto> mapToSearchResponse() {
    return projection ->
        new OpeningSearchResponseDto(
            projection.getOpeningId().intValue(),
            projection.getOpeningNumber(),
            OpeningCategoryEnum.of(projection.getCategory()),
            OpeningStatusEnum.of(projection.getStatus()),
            projection.getCuttingPermitId(),
            projection.getTimberMark(),
            projection.getCutBlockId(),
            projection.getOpeningGrossArea(),
            projection.getDisturbanceStartDate(),
            projection.getOrgUnitCode(),
            projection.getOrgUnitName(),
            projection.getClientNumber(),
            projection.getClientLocation(),
            "",
            "",
            projection.getRegenDelayDate(),
            projection.getEarlyFreeGrowingDate(),
            projection.getLateFreeGrowingDate(),
            projection.getUpdateTimestamp(),
            projection.getEntryUserId(),
            projection.getSubmittedToFrpa108() > 0,
            projection.getForestFileId(),
            projection.getSubmittedToFrpa108(),
            null,
            false
        );
  }

}
