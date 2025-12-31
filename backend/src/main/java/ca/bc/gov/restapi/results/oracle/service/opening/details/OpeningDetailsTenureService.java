package ca.bc.gov.restapi.results.oracle.service.opening.details;

import ca.bc.gov.restapi.results.oracle.dto.CodeDescriptionDto;
import ca.bc.gov.restapi.results.oracle.dto.SimplePageDto;
import ca.bc.gov.restapi.results.oracle.dto.opening.OpeningDetailsTenureDto;
import ca.bc.gov.restapi.results.oracle.dto.opening.OpeningDetailsTenuresDto;
import ca.bc.gov.restapi.results.common.projection.opening.OpeningTenureProjection;
import ca.bc.gov.restapi.results.oracle.repository.CutBlockOpenAdminRepository;
import ca.bc.gov.restapi.results.oracle.util.PaginationUtil;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class OpeningDetailsTenureService {

  private final CutBlockOpenAdminRepository cutBlockOpenAdminRepository;

  private static final Map<String, String> TENURE_SORT_FIELDS =
      Map.of(
          "fileId", "FOREST_FILE_ID",
          "cutBlock", "CUT_BLOCK_ID",
          "cuttingPermit", "CUTTING_PERMIT_ID",
          "timberMark", "TIMBER_MARK",
          "status", "cb.BLOCK_STATUS_ST",
          "plannedGrossArea", "PLANNED_GROSS_BLOCK_AREA",
          "plannedNetArea", "PLANNED_NET_BLOCK_AREA");

  public OpeningDetailsTenuresDto getOpeningTenures(
      Long openingId, String mainSearchTerm, Pageable pageable) {

    String normalizedSearch =
        Optional.ofNullable(mainSearchTerm).map(String::toUpperCase).orElse(null);

    Page<OpeningDetailsTenureDto> tenuresPage =
        cutBlockOpenAdminRepository
            .findAllTenuresByOpeningId(
                openingId,
                normalizedSearch,
                PageRequest.of(
                    pageable.getPageNumber(),
                    pageable.getPageSize(),
                    PaginationUtil.resolveSort(
                        pageable.getSort(), "CUT_BLOCK_OPEN_ADMIN_ID", TENURE_SORT_FIELDS)))
            .map(mapProjectionToDto());

    OpeningDetailsTenureDto primaryTenure =
        cutBlockOpenAdminRepository
            .findPrimeTenureByOpeningId(openingId)
            .map(mapProjectionToDto())
            .orElse(null);

    long totalUnfiltered =
        mainSearchTerm == null
            ? tenuresPage.getTotalElements()
            : cutBlockOpenAdminRepository
                .findAllTenuresByOpeningId(openingId, null, Pageable.ofSize(1))
                .getTotalElements();

    return new OpeningDetailsTenuresDto(
        primaryTenure,
        tenuresPage.getContent(),
        new SimplePageDto(
            tenuresPage.getSize(),
            tenuresPage.getNumber(),
            tenuresPage.getTotalElements(),
            tenuresPage.getTotalPages()),
        totalUnfiltered);
  }

  private Function<OpeningTenureProjection, OpeningDetailsTenureDto> mapProjectionToDto() {
    return projection ->
        new OpeningDetailsTenureDto(
            projection.getId(),
            projection.getPrimaryTenure(),
            projection.getFileId(),
            projection.getCutBlock(),
            projection.getCuttingPermit(),
            projection.getTimberMark(),
            new CodeDescriptionDto(projection.getStatusCode(), projection.getStatusName()),
            projection.getPlannedGrossArea(),
            projection.getPlannedNetArea());
  }
}
