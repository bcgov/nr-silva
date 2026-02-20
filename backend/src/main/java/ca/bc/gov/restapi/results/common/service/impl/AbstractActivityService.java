package ca.bc.gov.restapi.results.common.service.impl;

import ca.bc.gov.restapi.results.common.dto.CodeDescriptionDto;
import ca.bc.gov.restapi.results.common.dto.ForestClientDto;
import ca.bc.gov.restapi.results.common.dto.activity.ActivitySearchFiltersDto;
import ca.bc.gov.restapi.results.common.dto.activity.ActivitySearchResponseDto;
import ca.bc.gov.restapi.results.common.projection.ActivitySearchProjection;
import ca.bc.gov.restapi.results.common.repository.ActivityTreatmentUnitRepository;
import ca.bc.gov.restapi.results.common.service.ActivityService;
import ca.bc.gov.restapi.results.common.service.ForestClientService;
import ca.bc.gov.restapi.results.common.util.DateUtil;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

@Slf4j
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class AbstractActivityService implements ActivityService {

  protected ActivityTreatmentUnitRepository activityTreatmentUnitRepository;
  protected ForestClientService forestClientService;

  @Override
  public Page<ActivitySearchResponseDto> activitySearch(
      ActivitySearchFiltersDto filters, Pageable pagination) {
    DateUtil.validateDateRange(filters.getUpdateDateStart(), filters.getUpdateDateEnd());

    long offset = pagination.getOffset();
    long size = pagination.getPageSize();

    List<ActivitySearchProjection> projections =
        activityTreatmentUnitRepository.activitySearch(filters, offset, size);

    long total = 0;
    if (!projections.isEmpty()) {
      Long totalCount = projections.get(0).getTotalCount();
      total = totalCount != null ? totalCount : 0;
    }

    // Gather unique client numbers and fetch client information
    List<String> clientNumbers =
        projections.stream()
            .map(ActivitySearchProjection::getOpeningClientCode)
            .filter(code -> code != null && !code.isBlank())
            .distinct()
            .collect(Collectors.toList());

    final Map<String, ForestClientDto> clientMap;
    if (!clientNumbers.isEmpty()) {
      List<ForestClientDto> clients =
          forestClientService.searchByClientNumbers(0, clientNumbers.size(), clientNumbers);
      clientMap = clients.stream().collect(Collectors.toMap(ForestClientDto::clientNumber, c -> c));
    } else {
      clientMap = new HashMap<>();
    }

    // Map projections to response DTOs
    var responseDtos =
        projections.stream()
            .map(projection -> mapToSearchResponse(projection, clientMap))
            .collect(Collectors.toList());

    return new PageImpl<>(responseDtos, pagination, total);
  }

  private ActivitySearchResponseDto mapToSearchResponse(
      ActivitySearchProjection projection, Map<String, ForestClientDto> clientMap) {

    return new ActivitySearchResponseDto(
        projection.getActivityId(),
        mapCode(projection.getBaseCode(), projection.getBaseDescription()),
        mapCode(projection.getTechniqueCode(), projection.getTechniqueDescription()),
        mapCode(projection.getMethodCode(), projection.getMethodDescription()),
        projection.getIsComplete() != null && projection.getIsComplete() == 1L,
        mapCode(projection.getFundingSourceCode(), projection.getFundingSourceDescription()),
        projection.getFileId(),
        projection.getCutBlock(),
        projection.getOpeningId(),
        projection.getCuttingPermit(),
        projection.getTreatmentAmountArea(),
        projection.getIntraAgencyNumber(),
        mapCode(projection.getOpeningCategoryCode(), projection.getOpeningCategoryDescription()),
        mapCode(projection.getOrgUnitCode(), projection.getOrgUnitDescription()),
        clientMap.getOrDefault(projection.getOpeningClientCode(), null),
        projection.getUpdateTimestamp());
  }

  private CodeDescriptionDto mapCode(String code, String description) {
    if (code == null || code.isBlank()) {
      return null;
    }
    return new CodeDescriptionDto(code, description);
  }
}
