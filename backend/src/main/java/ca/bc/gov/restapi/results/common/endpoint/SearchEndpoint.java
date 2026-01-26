package ca.bc.gov.restapi.results.common.endpoint;

import ca.bc.gov.restapi.results.common.exception.MissingSearchParameterException;
import ca.bc.gov.restapi.results.oracle.SilvaOracleConstants;
import ca.bc.gov.restapi.results.common.dto.opening.OpeningSearchExactFiltersDto;
import ca.bc.gov.restapi.results.common.dto.opening.OpeningSearchResponseDto;
import ca.bc.gov.restapi.results.common.service.OpeningSearchService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController("oracleSearchEndpoint")
@RequestMapping("/api/search")
@RequiredArgsConstructor
@Slf4j
public class SearchEndpoint {

  private final OpeningSearchService openingSearchService;

  /**
   * Exact search for Openings with direct value matching on provided filters.
   *
   * @param openingId Opening ID
   * @param categories Opening category codes filter
   * @param openingStatuses Opening status codes filter
   * @param licenseNumber Licensee number (forest file ID)
   * @param licenseeOpeningId Licensee-provided opening identifier (LICENSEE_OPENING_ID)
   * @param entryDateStart Entry timestamp start date (yyyy-MM-dd). If provided returns records from
   *     this date to present.
   * @param entryDateEnd Entry timestamp end date (yyyy-MM-dd). If provided returns records up to
   *     this date (inclusive).
   * @param cutBlockId Cut block identification filter
   * @param cuttingPermitId Cutting permit identification filter
   * @param timberMark Timber mark filter
   * @param orgUnits Organization unit codes filter
   * @param clientNumbers Client numbers filter
   * @param isCreatedByUser Openings created by the request user
   * @param submittedToFrpa Submitted to FRPA
   * @param mapsheetGrid Mapsheet grid (3-digit code)
   * @param mapsheetLetter Mapsheet letter (A-P or W)
   * @param mapsheetSquare Mapsheet square (alphanumeric)
   * @param mapsheetQuad Mapsheet quadrant (0-4)
   * @param mapsheetSubQuad Mapsheet sub-quadrant (0-4)
   * @param subOpeningNumber Opening number (4-digit), we call it sub opening number here to avoid
   *     confusion
   * @param paginationParameters Pagination settings
   * @return Page of opening search results with exact matching
   */
  @GetMapping("/openings")
  public Page<OpeningSearchResponseDto> openingSearchExact(
      @RequestParam(value = SilvaOracleConstants.OPENING_ID, required = false) Long openingId,
      @RequestParam(value = SilvaOracleConstants.CATEGORIES, required = false)
          List<String> categories,
      @RequestParam(value = SilvaOracleConstants.OPENING_STATUSES, required = false)
          List<String> openingStatuses,
      @RequestParam(value = SilvaOracleConstants.LICENSE_NUMBER, required = false)
          String licenseNumber,
      @RequestParam(value = SilvaOracleConstants.LICENSEE_OPENING_ID, required = false)
          String licenseeOpeningId,
      @RequestParam(value = SilvaOracleConstants.ENTRY_DATE_START, required = false)
          String entryDateStart,
      @RequestParam(value = SilvaOracleConstants.ENTRY_DATE_END, required = false)
          String entryDateEnd,
      @RequestParam(value = SilvaOracleConstants.CUT_BLOCK_ID, required = false) String cutBlockId,
      @RequestParam(value = SilvaOracleConstants.CUTTING_PERMIT_ID, required = false)
          String cuttingPermitId,
      @RequestParam(value = SilvaOracleConstants.TIMBER_MARK, required = false) String timberMark,
      @RequestParam(value = SilvaOracleConstants.ORG_UNITS, required = false) List<String> orgUnits,
      @RequestParam(value = SilvaOracleConstants.CLIENT_NUMBERS, required = false)
          List<String> clientNumbers,
      @RequestParam(value = SilvaOracleConstants.IS_CREATED_BY_USER, required = false)
          Boolean isCreatedByUser,
      @RequestParam(value = SilvaOracleConstants.SUBMITTED_TO_FRPA, required = false)
          Boolean submittedToFrpa,
      @RequestParam(value = SilvaOracleConstants.MAPSHEET_GRID, required = false)
          String mapsheetGrid,
      @RequestParam(value = SilvaOracleConstants.MAPSHEET_LETTER, required = false)
          String mapsheetLetter,
      @RequestParam(value = SilvaOracleConstants.MAPSHEET_SQUARE, required = false)
          String mapsheetSquare,
      @RequestParam(value = SilvaOracleConstants.MAPSHEET_QUAD, required = false)
          String mapsheetQuad,
      @RequestParam(value = SilvaOracleConstants.MAPSHEET_SUB_QUAD, required = false)
          String mapsheetSubQuad,
      @RequestParam(value = SilvaOracleConstants.SUB_OPENING_NUMBER, required = false)
          String subOpeningNumber,
      @ParameterObject Pageable paginationParameters) {

    OpeningSearchExactFiltersDto filtersDto =
        new OpeningSearchExactFiltersDto(
            openingId,
            categories,
            openingStatuses,
            licenseNumber,
            licenseeOpeningId,
            entryDateStart,
            entryDateEnd,
            cutBlockId,
            cuttingPermitId,
            timberMark,
            orgUnits,
            clientNumbers,
            isCreatedByUser,
            submittedToFrpa,
            mapsheetGrid,
            mapsheetLetter != null ? mapsheetLetter.trim().toUpperCase() : null,
            mapsheetSquare,
            mapsheetQuad,
            mapsheetSubQuad,
            subOpeningNumber);

    if (!filtersDto.hasAnyFilter()) {
      throw new MissingSearchParameterException();
    }

    return openingSearchService.openingSearchExact(filtersDto, paginationParameters);
  }
}
