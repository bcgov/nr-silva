package ca.bc.gov.restapi.results.common.service.impl;

import ca.bc.gov.restapi.results.common.dto.CodeDescriptionDto;
import ca.bc.gov.restapi.results.common.dto.cover.ForestCoverSearchFilterDto;
import ca.bc.gov.restapi.results.common.dto.cover.ForestCoverSearchResponseDto;
import ca.bc.gov.restapi.results.common.projection.ForestCoverSearchProjection;
import ca.bc.gov.restapi.results.common.repository.ForestCoverRepository;
import ca.bc.gov.restapi.results.common.service.ForestCoverService;
import ca.bc.gov.restapi.results.common.util.DateUtil;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class AbstractForestCoverService implements ForestCoverService {

  protected final ForestCoverRepository forestCoverRepository;

  @Override
  public Page<ForestCoverSearchResponseDto> forestCoverSearch(
      ForestCoverSearchFilterDto filters, Pageable pagination) {
    DateUtil.validateDateRange(filters.getUpdateDateStart(), filters.getUpdateDateEnd());

    long offset = pagination.getOffset();
    long size = pagination.getPageSize();

    List<ForestCoverSearchProjection> projections =
        forestCoverRepository.forestCoverSearch(filters, offset, size);

    long total = 0;
    if (!projections.isEmpty()) {
      Long totalCount = projections.get(0).getTotalCount();
      total = totalCount != null ? totalCount : 0;
    }

    List<ForestCoverSearchResponseDto> responseDtos =
        projections.stream().map(this::mapToSearchResponse).toList();

    return new PageImpl<>(responseDtos, pagination, total);
  }

  private ForestCoverSearchResponseDto mapToSearchResponse(ForestCoverSearchProjection projection) {
    return new ForestCoverSearchResponseDto(
        projection.getForestCoverId(),
        projection.getPolygonId(),
        projection.getStandardUnitId(),
        parseDamageAgents(projection.getDamageCodes(), projection.getDamageNames()),
        mapCode(projection.getStockingTypeCode(), projection.getStockingTypeName()),
        mapCode(projection.getStockingStatusCode(), projection.getStockingStatusName()),
        projection.getFileId(),
        projection.getOpeningId(),
        mapCode(projection.getOpeningCategoryCode(), projection.getOpeningCategoryName()),
        mapCode(projection.getOrgUnitCode(), projection.getOrgUnitName()),
        projection.getUpdateTimestamp(),
        projection.getRegenDueDate(),
        projection.getFreeGrowingDueDate());
  }

  private List<CodeDescriptionDto> parseDamageAgents(String codes, String names) {
    if (codes == null || codes.isBlank()) {
      return List.of();
    }
    String[] codeArr = codes.split(",");
    String[] nameArr = names != null ? names.split("\\|\\|") : new String[0];
    List<CodeDescriptionDto> result = new ArrayList<>();
    for (int i = 0; i < codeArr.length; i++) {
      String code = codeArr[i].trim();
      String name = i < nameArr.length ? nameArr[i].trim() : null;
      if (!code.isBlank()) {
        result.add(new CodeDescriptionDto(code, name));
      }
    }
    return result;
  }

  private CodeDescriptionDto mapCode(String code, String description) {
    if (code == null || code.isBlank()) {
      return null;
    }
    return new CodeDescriptionDto(code, description);
  }
}
