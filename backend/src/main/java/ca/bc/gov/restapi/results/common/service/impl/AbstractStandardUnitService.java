package ca.bc.gov.restapi.results.common.service.impl;

import ca.bc.gov.restapi.results.common.dto.CodeDescriptionDto;
import ca.bc.gov.restapi.results.common.dto.ForestClientDto;
import ca.bc.gov.restapi.results.common.dto.StandardUnitSearchFilterDto;
import ca.bc.gov.restapi.results.common.dto.StandardUnitSearchResponseDto;
import ca.bc.gov.restapi.results.common.projection.StandardUnitSearchProjection;
import ca.bc.gov.restapi.results.common.repository.StandardUnitRepository;
import ca.bc.gov.restapi.results.common.service.ForestClientService;
import ca.bc.gov.restapi.results.common.service.StandardUnitService;
import ca.bc.gov.restapi.results.common.util.DateUtil;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

@Slf4j
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class AbstractStandardUnitService implements StandardUnitService {

  protected StandardUnitRepository standardUnitRepository;
  protected ForestClientService forestClientService;

  @Override
  public Page<StandardUnitSearchResponseDto> standardUnitSearch(
      StandardUnitSearchFilterDto filters, Pageable pagination) {
    DateUtil.validateDateRange(filters.getUpdateDateStart(), filters.getUpdateDateEnd());

    long offset = pagination.getOffset();
    long size = pagination.getPageSize();

    List<StandardUnitSearchProjection> projections =
        standardUnitRepository.standardUnitSearch(filters, offset, size);

    long total = 0;
    if (!projections.isEmpty()) {
      Long totalCount = projections.get(0).getTotalCount();
      total = totalCount != null ? totalCount : 0;
    }

    // Gather unique client numbers and fetch client information
    List<String> clientNumbers =
        projections.stream()
            .map(StandardUnitSearchProjection::getClientNumber)
            .filter(code -> code != null && !code.isBlank())
            .distinct()
            .toList();

    final Map<String, ForestClientDto> clientMap = buildClientMap(clientNumbers);

    List<StandardUnitSearchResponseDto> responseDtos =
        projections.stream().map(projection -> mapToSearchResponse(projection, clientMap)).toList();

    return new PageImpl<>(responseDtos, pagination, total);
  }

  private StandardUnitSearchResponseDto mapToSearchResponse(
      StandardUnitSearchProjection projection, Map<String, ForestClientDto> clientMap) {
    return new StandardUnitSearchResponseDto(
        projection.getStockingStandardUnitId(),
        projection.getOpeningId(),
        projection.getFileId(),
        projection.getCutBlock(),
        projection.getCuttingPermit(),
        projection.getStandardsUnitId(),
        projection.getNetArea(),
        projection.getRegenDelayYears(),
        projection.getFreeGrowingEarlyYears(),
        projection.getFreeGrowingLateYears(),
        projection.getTotalLayer(),
        projection.getTargetWellSpacedTrees(),
        parsePreferredSpecies(
            projection.getPreferredSpeciesCodes(), projection.getPreferredSpeciesNames()),
        projection.getBgcZone(),
        projection.getBgcSubZone(),
        projection.getBgcVariant(),
        projection.getBgcPhase(),
        projection.getBecSiteSeries(),
        projection.getBecSiteType(),
        projection.getBecSeral(),
        mapCode(projection.getOrgUnitCode(), projection.getOrgUnitName()),
        clientMap.getOrDefault(projection.getClientNumber(), null),
        projection.getUpdateTimestamp());
  }

  private List<CodeDescriptionDto> parsePreferredSpecies(String codes, String names) {
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

  private Map<String, ForestClientDto> buildClientMap(List<String> clientNumbers) {
    Map<String, ForestClientDto> clientMap = new HashMap<>();
    for (String clientNumber : clientNumbers) {
      forestClientService
          .getClientByNumber(clientNumber)
          .ifPresent(dto -> clientMap.put(clientNumber, dto));
    }
    return clientMap;
  }
}
