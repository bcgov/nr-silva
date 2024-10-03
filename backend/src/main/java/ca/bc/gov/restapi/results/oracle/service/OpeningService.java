package ca.bc.gov.restapi.results.oracle.service;

import ca.bc.gov.restapi.results.common.config.ConstantsConfig;
import ca.bc.gov.restapi.results.common.dto.ForestClientDto;
import ca.bc.gov.restapi.results.common.exception.MaxPageSizeException;
import ca.bc.gov.restapi.results.common.pagination.PaginatedResult;
import ca.bc.gov.restapi.results.common.pagination.PaginationParameters;
import ca.bc.gov.restapi.results.common.security.LoggedUserService;
import ca.bc.gov.restapi.results.oracle.dto.OpeningSearchFiltersDto;
import ca.bc.gov.restapi.results.oracle.dto.OpeningSearchResponseDto;
import ca.bc.gov.restapi.results.oracle.dto.RecentOpeningDto;
import ca.bc.gov.restapi.results.oracle.entity.CutBlockOpenAdminEntity;
import ca.bc.gov.restapi.results.oracle.entity.OpeningEntity;
import ca.bc.gov.restapi.results.oracle.enums.OpeningCategoryEnum;
import ca.bc.gov.restapi.results.oracle.enums.OpeningStatusEnum;
import ca.bc.gov.restapi.results.oracle.repository.OpeningRepository;
import ca.bc.gov.restapi.results.oracle.repository.OpeningSearchRepository;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

/** This class holds methods for fetching and handling {@link OpeningEntity} in general. */
@Slf4j
@Service
@RequiredArgsConstructor
public class OpeningService {

  private final OpeningRepository openingRepository;

  private final CutBlockOpenAdminService cutBlockOpenAdminService;

  private final LoggedUserService loggedUserService;

  private final OpeningSearchRepository openingSearchRepository;

  /**
   * Gets all recent openings for the Home Screen.
   *
   * @param pagination A {@link PaginationParameters} with pagination settings.
   * @return {@link List} of {@link RecentOpeningDto} containing all recent openings for that user.
   */
  public PaginatedResult<RecentOpeningDto> getRecentOpeningsCurrentUser(
      PaginationParameters pagination) {
    log.info(
        "Getting recent openings to logged user with page index {} and page size {}",
        pagination.page(),
        pagination.perPage());

    if (pagination.perPage() > ConstantsConfig.MAX_PAGE_SIZE) {
      throw new MaxPageSizeException(ConstantsConfig.MAX_PAGE_SIZE);
    }

    String entryUserId = loggedUserService.getLoggedUserId();

    // Openings
    Pageable pageable =
        PageRequest.of(
            pagination.page(), pagination.perPage(), Sort.by("updateTimestamp").descending());
    Page<OpeningEntity> openingPage = openingRepository.findAllByEntryUserId(entryUserId, pageable);

    PaginatedResult<RecentOpeningDto> paginatedResult = new PaginatedResult<>();
    paginatedResult.setPageIndex(pagination.page());
    paginatedResult.setPerPage(pagination.perPage());

    if (openingPage.getContent().isEmpty()) {
      log.info("No recent openings for this user given page index and size!");
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

    if (pagination.perPage() > ConstantsConfig.MAX_PAGE_SIZE) {
      throw new MaxPageSizeException(ConstantsConfig.MAX_PAGE_SIZE);
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
  public PaginatedResult<OpeningSearchResponseDto> openingSearch(
      OpeningSearchFiltersDto filtersDto, PaginationParameters pagination) {
    log.info(
        "Search Openings with page index {} and page size {}",
        pagination.page(),
        pagination.perPage());

    if (pagination.perPage() > ConstantsConfig.MAX_PAGE_SIZE_OPENING_SEARCH) {
      throw new MaxPageSizeException(ConstantsConfig.MAX_PAGE_SIZE_OPENING_SEARCH);
    }

    // Set the user in the filter, if required
    if (filtersDto.hasValue(OpeningSearchFiltersDto.MY_OPENINGS)) {
      String userId = loggedUserService.getLoggedUserId().replace("@", "\\");
      if (!userId.startsWith("IDIR")) {
        userId = "BCEID" + userId.substring(5);
      }
      filtersDto.setRequestUserId(userId);
    }

    PaginatedResult<OpeningSearchResponseDto> result =
        openingSearchRepository.searchOpeningQuery(filtersDto, pagination);

    fetchClientAcronyms(result);

    return result;
  }

  private void fetchClientAcronyms(PaginatedResult<OpeningSearchResponseDto> result) {
    List<String> clientNumbersWithDuplicates =
        result.getData().stream()
            .filter(o -> !Objects.isNull(o.getClientNumber()))
            .map(OpeningSearchResponseDto::getClientNumber)
            .toList();

    List<String> clientNumbers = new ArrayList<>(new HashSet<>(clientNumbersWithDuplicates));

    // fetch from forest client
    // Keep going from here

    // create map
    Map<String, ForestClientDto> forestClientsMap = Map.of();

    for (OpeningSearchResponseDto response : result.getData()) {
      ForestClientDto client = forestClientsMap.get(response.getClientNumber());
      if (!Objects.isNull(client)) {
        response.setClientAcronym(client.acronym());
      }
    }
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
}
