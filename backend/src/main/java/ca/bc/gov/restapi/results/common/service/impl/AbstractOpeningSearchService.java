package ca.bc.gov.restapi.results.common.service.impl;

import ca.bc.gov.restapi.results.common.SilvaConstants;
import ca.bc.gov.restapi.results.common.dto.CodeDescriptionDto;
import ca.bc.gov.restapi.results.common.dto.ForestClientDto;
import ca.bc.gov.restapi.results.common.dto.opening.OpeningSearchExactFiltersDto;
import ca.bc.gov.restapi.results.common.dto.opening.OpeningSearchResponseDto;
import ca.bc.gov.restapi.results.common.entity.BaseCodeEntity;
import ca.bc.gov.restapi.results.common.entity.BaseOpeningEntity;
import ca.bc.gov.restapi.results.common.exception.MaxPageSizeException;
import ca.bc.gov.restapi.results.common.projection.SilvicultureSearchProjection;
import ca.bc.gov.restapi.results.common.provider.ForestClientApiProvider;
import ca.bc.gov.restapi.results.common.repository.OpenCategoryCodeRepository;
import ca.bc.gov.restapi.results.common.repository.OpeningRepository;
import ca.bc.gov.restapi.results.common.repository.OpeningStatusCodeRepository;
import ca.bc.gov.restapi.results.common.security.LoggedUserHelper;
import ca.bc.gov.restapi.results.common.service.OpeningSearchService;
import ca.bc.gov.restapi.results.postgres.service.UserOpeningService;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class AbstractOpeningSearchService implements OpeningSearchService {
  protected final OpeningRepository<? extends BaseOpeningEntity> openingRepository;
  protected final OpenCategoryCodeRepository<? extends BaseCodeEntity> openCategoryCodeRepository;
  protected final OpeningStatusCodeRepository<? extends BaseCodeEntity> openingStatusCodeRepository;
  protected final LoggedUserHelper loggedUserHelper;
  protected final ForestClientApiProvider forestClientApiProvider;
  protected final UserOpeningService userOpeningService;
    
  /**
   * Exact search for openings with direct value matching.
   *
   * @param filtersDto the exact search filter criteria.
   * @param pagination pagination parameters
   * @return Page of opening search results
   */
  @Override
  @Transactional
  public Page<OpeningSearchResponseDto> openingSearchExact(
      OpeningSearchExactFiltersDto filtersDto, Pageable pagination) {
    log.info(
        "Exact search Openings with page index {} and page size {} with filters {}",
        pagination.getPageNumber(),
        pagination.getPageSize(),
        filtersDto);

    validatePageSize(pagination);
    validateEntryDateRange(filtersDto);

    // Validate mapsheet related fields for Opening Number
    validateMapsheetFields(filtersDto);

    if (filtersDto.hasValue(SilvaConstants.IS_CREATED_BY_USER)
        && Boolean.TRUE.equals(filtersDto.getIsCreatedByUser())) {
      filtersDto.setRequestUserId(loggedUserHelper.getLoggedUserId());
    }

    List<SilvicultureSearchProjection> searchContent =
        openingRepository.searchByExact(
            filtersDto, List.of(0L), pagination.getOffset(), pagination.getPageSize());

    long total = searchContent.isEmpty() ? 0 : searchContent.get(0).getTotalCount();
    log.info("Exact search resulted in {}/{} results", searchContent.size(), total);

    Page<SilvicultureSearchProjection> searchResultPage =
        new PageImpl<>(searchContent, pagination, total);

    return parsePageResult(searchResultPage);
  }

  @Override
  public Page<OpeningSearchResponseDto> parsePageResult(
      Page<SilvicultureSearchProjection> searchResultPage) {
    // Load code mappings once before processing results
    final var categoryMap =
        openCategoryCodeRepository.findAll().stream()
            .collect(
                Collectors.toMap(
                    BaseCodeEntity::getCode,
                    e -> new CodeDescriptionDto(e.getCode(), e.getDescription())));

    final var statusMap =
        openingStatusCodeRepository.findAll().stream()
            .collect(
                Collectors.toMap(
                    BaseCodeEntity::getCode,
                    e -> new CodeDescriptionDto(e.getCode(), e.getDescription())));

    return fetchClientAcronyms(
        new PageImpl<>(
            searchResultPage
                .get()
                .map(mapToSearchResponse(categoryMap, statusMap))
                .filter(OpeningSearchResponseDto::isValid)
                .toList(),
            searchResultPage.getPageable(),
            searchResultPage.getTotalElements()));
  }

  private Page<OpeningSearchResponseDto> fetchClientAcronyms(
      Page<OpeningSearchResponseDto> result) {
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
        .forEach(
            response -> {
              if (StringUtils.isNotBlank(response.getClientNumber())
                  && forestClientsMap.containsKey(response.getClientNumber())) {
                ForestClientDto client = forestClientsMap.get(response.getClientNumber());
                response.setClientAcronym(client.acronym());
                response.setClientName(client.clientName());
              }
            });

    return result;
  }

  private Function<SilvicultureSearchProjection, OpeningSearchResponseDto> mapToSearchResponse(
      Map<String, CodeDescriptionDto> categoryMap, Map<String, CodeDescriptionDto> statusMap) {
    return projection -> {
      CodeDescriptionDto categoryDto = null;
      if (projection.getCategory() != null) {
        categoryDto = categoryMap.get(projection.getCategory());
      }

      CodeDescriptionDto statusDto = null;
      if (projection.getStatus() != null) {
        statusDto = statusMap.get(projection.getStatus());
      }

      return new OpeningSearchResponseDto(
          projection.getOpeningId(),
          composedMapsheetKey(projection),
          categoryDto,
          statusDto,
          projection.getLicenseeOpeningId(),
          projection.getCuttingPermitId(),
          projection.getTimberMark(),
          projection.getCutBlockId(),
          projection.getOpeningGrossArea(),
          projection.getDisturbanceGrossArea(),
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
          projection.getEntryTimestamp(),
          projection.getSubmittedToFrpa108() > 0,
          projection.getForestFileId(),
          projection.getSubmittedToFrpa108(),
          null);
    };
  }

  /**
   * Constructs the composed mapsheet key from the projection. If any component is null, replaces it
   * with "--" as a placeholder.
   *
   * @param projection the silviculture search projection
   * @return the composed mapsheet key (e.g., "93O 045 0.0 343" or "93O 045 -- --" if components are
   *     null)
   */
  private String composedMapsheetKey(SilvicultureSearchProjection projection) {
    String mapsheepOpeningId = projection.getMapsheepOpeningId();
    if (mapsheepOpeningId != null && !mapsheepOpeningId.trim().isEmpty()) {
      return mapsheepOpeningId;
    }
    return "--";
  }

  /**
   * Validates if the mapsheet grid value is one of the allowed values.
   *
   * @param gridValue the grid value to validate
   * @return true if valid, false otherwise
   */
  private boolean isValidMapsheetGrid(String gridValue) {
    return SilvaConstants.VALID_MAPSHEET_GRID_VALUES.contains(gridValue);
  }

  /**
   * Validates if the mapsheet letter is between A-P or W.
   *
   * @param letterValue the letter value to validate
   * @return true if valid, false otherwise
   */
  private boolean isValidMapsheetLetter(String letterValue) {
    return letterValue.length() == 1
        && SilvaConstants.VALID_MAPSHEET_LETTERS.contains(letterValue.charAt(0));
  }

  /**
   * Validates if the mapsheet quad is between 0 and 4.
   *
   * @param quadValue the quad value to validate
   * @return true if valid, false otherwise
   */
  private boolean isValidMapsheetQuad(String quadValue) {
    return quadValue.length() == 1 && quadValue.charAt(0) >= '0' && quadValue.charAt(0) <= '4';
  }

  /**
   * Validates if the mapsheet sub-quad is between 0 and 4.
   *
   * @param subQuadValue the sub-quad value to validate
   * @return true if valid, false otherwise
   */
  private boolean isValidMapsheetSubQuad(String subQuadValue) {
    return subQuadValue.length() == 1
        && subQuadValue.charAt(0) >= '0'
        && subQuadValue.charAt(0) <= '4';
  }

  private void validatePageSize(Pageable pagination) {
    if (pagination.getPageSize() > SilvaConstants.MAX_PAGE_SIZE_OPENING_SEARCH) {
      throw new MaxPageSizeException(SilvaConstants.MAX_PAGE_SIZE_OPENING_SEARCH);
    }
  }

  private void validateEntryDateRange(OpeningSearchExactFiltersDto filtersDto) {
    if (filtersDto.getUpdateDateStart() != null && filtersDto.getUpdateDateEnd() != null) {
      try {
        LocalDate start = LocalDate.parse(filtersDto.getUpdateDateStart());
        LocalDate end = LocalDate.parse(filtersDto.getUpdateDateEnd());
        if (end.isBefore(start)) {
          throw new ResponseStatusException(
              HttpStatus.BAD_REQUEST, "End date must be the same or after start date");
        }
      } catch (DateTimeParseException ex) {
        throw new ResponseStatusException(
            HttpStatus.BAD_REQUEST,
            "Invalid date format for updateDateStart/updateDateEnd. Expected yyyy-MM-dd");
      }
    }
  }

  /**
   * Consolidated validation for all mapsheet-related fields.
   *
   * @param filtersDto the DTO containing mapsheet values
   */
  private void validateMapsheetFields(OpeningSearchExactFiltersDto filtersDto) {
    // Validate mapsheet grid
    if (filtersDto.getMapsheetGrid() != null) {
      String gridValue = filtersDto.getMapsheetGrid().trim();
      if (!isValidMapsheetGrid(gridValue)) {
        throw new ResponseStatusException(
            HttpStatus.BAD_REQUEST,
            "Invalid mapsheetGrid value. Must be one of: 82, 83, 92, 93, 94, 082, 083, 092, 093,"
                + " 094, 102, 103, 104, 105, or 114");
      }
    }

    // Validate mapsheet letter
    if (filtersDto.getMapsheetLetter() != null) {
      String letterValue = filtersDto.getMapsheetLetter().trim().toUpperCase();
      if (!isValidMapsheetLetter(letterValue)) {
        throw new ResponseStatusException(
            HttpStatus.BAD_REQUEST, "Invalid mapsheetLetter value. Must be between A-P or W");
      }
    }

    // Validate mapsheet quad
    if (filtersDto.getMapsheetQuad() != null) {
      String quadValue = filtersDto.getMapsheetQuad().trim();
      if (!isValidMapsheetQuad(quadValue)) {
        throw new ResponseStatusException(
            HttpStatus.BAD_REQUEST, "Invalid mapsheetQuad value. Must be between 0 and 4");
      }
    }

    // Validate mapsheet sub-quad
    if (filtersDto.getMapsheetSubQuad() != null) {
      String subQuadValue = filtersDto.getMapsheetSubQuad().trim();
      if (!isValidMapsheetSubQuad(subQuadValue)) {
        throw new ResponseStatusException(
            HttpStatus.BAD_REQUEST, "Invalid mapsheetSubQuad value. Must be between 0 and 4");
      }
    }
  }
}
