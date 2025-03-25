package ca.bc.gov.restapi.results.oracle.service;

import ca.bc.gov.restapi.results.common.SilvaConstants;
import ca.bc.gov.restapi.results.common.dto.ForestClientDto;
import ca.bc.gov.restapi.results.common.exception.MaxPageSizeException;
import ca.bc.gov.restapi.results.common.provider.ForestClientApiProvider;
import ca.bc.gov.restapi.results.common.security.LoggedUserService;
import ca.bc.gov.restapi.results.oracle.SilvaOracleConstants;
import ca.bc.gov.restapi.results.oracle.dto.OpeningSearchFiltersDto;
import ca.bc.gov.restapi.results.oracle.dto.OpeningSearchResponseDto;
import ca.bc.gov.restapi.results.oracle.entity.OpeningEntity;
import ca.bc.gov.restapi.results.oracle.entity.SilvicultureSearchProjection;
import ca.bc.gov.restapi.results.oracle.enums.OpeningCategoryEnum;
import ca.bc.gov.restapi.results.oracle.enums.OpeningStatusEnum;
import ca.bc.gov.restapi.results.oracle.repository.OpeningRepository;
import ca.bc.gov.restapi.results.postgres.service.UserOpeningService;
import jakarta.transaction.Transactional;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
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

  @Transactional
  public Page<OpeningSearchResponseDto> openingSearch(
      OpeningSearchFiltersDto filtersDto, Pageable pagination
  ) {
    log.info(
        "Search Openings with page index {} and page size {} with filters {}",
        pagination.getPageNumber(),
        pagination.getPageSize(),
        filtersDto
    );

    if (pagination.getPageSize() > SilvaConstants.MAX_PAGE_SIZE_OPENING_SEARCH) {
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
            List.of(0L),
            ((PageRequest) pagination).withSort(Sort.by("opening_id").descending())
        );

    return parsePageResult(searchResultPage);
  }

  public Page<OpeningSearchResponseDto> parsePageResult(
      Page<SilvicultureSearchProjection> searchResultPage
  ) {
    return fetchClientAcronyms(fetchFavorites(new PageImpl<>(
        searchResultPage.get()
            .map(mapToSearchResponse())
            .toList(),
        searchResultPage.getPageable(),
        searchResultPage.getTotalPages()
    )));
  }

  private Page<OpeningSearchResponseDto> fetchClientAcronyms(
      Page<OpeningSearchResponseDto> result
  ) {
    Map<String, ForestClientDto> forestClientsMap = new HashMap<>();

    List<String> clientNumbers =
        result
            .get()
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
        .getContent()
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

  private Page<OpeningSearchResponseDto> fetchFavorites(
      Page<OpeningSearchResponseDto> pagedResult
  ) {

    List<Long> favourites = userOpeningService.checkForFavorites(
        pagedResult
            .getContent()
            .stream()
            .map(OpeningSearchResponseDto::getOpeningId)
            .toList()
    );

    for (OpeningSearchResponseDto opening : pagedResult.getContent()) {
      opening.setFavourite(favourites.contains(opening.getOpeningId().longValue()));
    }

    return pagedResult;
  }

  private Function<SilvicultureSearchProjection, OpeningSearchResponseDto> mapToSearchResponse() {
    return projection ->
        new OpeningSearchResponseDto(
            projection.getOpeningId(),
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
