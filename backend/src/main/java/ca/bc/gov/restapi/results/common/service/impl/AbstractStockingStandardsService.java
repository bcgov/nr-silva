package ca.bc.gov.restapi.results.common.service.impl;

import ca.bc.gov.restapi.results.common.dto.CodeDescriptionDto;
import ca.bc.gov.restapi.results.common.dto.ForestClientDto;
import ca.bc.gov.restapi.results.common.dto.StockingStandardsSearchFilterDto;
import ca.bc.gov.restapi.results.common.dto.StockingStandardsSearchResponseDto;
import ca.bc.gov.restapi.results.common.dto.stockingstandards.StockingStandardsCommentSearchFilterDto;
import ca.bc.gov.restapi.results.common.dto.stockingstandards.StockingStandardsCommentSearchResponseDto;
import ca.bc.gov.restapi.results.common.enums.StockingStandardsCommentLocationCode;
import ca.bc.gov.restapi.results.common.projection.StockingStandardsCommentSearchProjection;
import ca.bc.gov.restapi.results.common.projection.StockingStandardsSearchProjection;
import ca.bc.gov.restapi.results.common.repository.StockingStandardsRepository;
import ca.bc.gov.restapi.results.common.service.ForestClientService;
import ca.bc.gov.restapi.results.common.service.StockingStandardsService;
import ca.bc.gov.restapi.results.common.util.DateUtil;
import ca.bc.gov.restapi.results.common.util.StringUtil;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
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
public abstract class AbstractStockingStandardsService implements StockingStandardsService {

  private static final ZoneId VANCOUVER = ZoneId.of("America/Vancouver");

  protected StockingStandardsRepository stockingStandardsRepository;
  protected ForestClientService forestClientService;

  @Override
  public Page<StockingStandardsSearchResponseDto> stockingStandardsSearch(
      StockingStandardsSearchFilterDto filters, Pageable pagination) {
    DateUtil.validateDateRange(filters.getApprovedDateStart(), filters.getApprovedDateEnd());

    long offset = pagination.getOffset();
    long size = pagination.getPageSize();

    List<StockingStandardsSearchProjection> projections =
        stockingStandardsRepository.stockingStandardsSearch(filters, offset, size);

    long total = 0;
    if (!projections.isEmpty()) {
      Long totalCount = projections.get(0).getTotalCount();
      total = totalCount != null ? totalCount : 0;
    }

    List<String> clientNumbers =
        projections.stream()
            .map(StockingStandardsSearchProjection::getClientNumbers)
            .filter(s -> s != null && !s.isBlank())
            .flatMap(s -> Arrays.stream(s.split(",")))
            .map(String::trim)
            .filter(s -> !s.isBlank())
            .distinct()
            .toList();

    final Map<String, ForestClientDto> clientMap = buildClientMap(clientNumbers);

    List<StockingStandardsSearchResponseDto> responseDtos =
        projections.stream().map(p -> mapToSearchResponse(p, clientMap)).toList();

    return new PageImpl<>(responseDtos, pagination, total);
  }

  @Override
  public Page<StockingStandardsCommentSearchResponseDto> stockingStandardsCommentSearch(
      StockingStandardsCommentSearchFilterDto filters, Pageable pagination) {
    DateUtil.validateDateRange(filters.getUpdateDateStart(), filters.getUpdateDateEnd());

    long offset = pagination.getOffset();
    long size = pagination.getPageSize();

    List<StockingStandardsCommentSearchProjection> projections =
        stockingStandardsRepository.stockingStandardsCommentSearch(filters, offset, size);

    long total = 0;
    if (!projections.isEmpty()) {
      Long totalCount = projections.get(0).getTotalCount();
      total = totalCount != null ? totalCount : 0;
    }

    List<String> clientNumbers =
        projections.stream()
            .map(StockingStandardsCommentSearchProjection::getClientNumbers)
            .filter(s -> s != null && !s.isBlank())
            .flatMap(s -> Arrays.stream(s.split(",")))
            .map(String::trim)
            .filter(s -> !s.isBlank())
            .distinct()
            .toList();

    final Map<String, ForestClientDto> clientMap = buildClientMap(clientNumbers);

    List<StockingStandardsCommentSearchResponseDto> responseDtos =
        projections.stream().map(p -> mapToCommentSearchResponse(p, clientMap)).toList();

    return new PageImpl<>(responseDtos, pagination, total);
  }

  private StockingStandardsCommentSearchResponseDto mapToCommentSearchResponse(
      StockingStandardsCommentSearchProjection projection, Map<String, ForestClientDto> clientMap) {
    List<ForestClientDto> clients = buildClientList(projection.getClientNumbers(), clientMap);
    List<CodeDescriptionDto> orgUnits =
        parseCodeDescriptionList(projection.getOrgUnitCodes(), projection.getOrgUnitNames());
    List<String> fspIds = parseFspIds(projection.getFspIds());
    CodeDescriptionDto status =
        new CodeDescriptionDto(projection.getStatusCode(), projection.getStatusDescription());
    StockingStandardsCommentLocationCode commentLocation =
        StockingStandardsCommentLocationCode.valueOf(projection.getCommentLocation());

    return new StockingStandardsCommentSearchResponseDto(
        projection.getStandardsRegimeId(),
        commentLocation,
        status,
        StringUtil.nullIfBlank(projection.getCommentText()),
        projection.getUpdateTimestamp(),
        projection.getApprovedTimestamp(),
        clients,
        orgUnits,
        fspIds);
  }

  private StockingStandardsSearchResponseDto mapToSearchResponse(
      StockingStandardsSearchProjection projection, Map<String, ForestClientDto> clientMap) {
    List<ForestClientDto> clients = buildClientList(projection.getClientNumbers(), clientMap);
    List<CodeDescriptionDto> orgUnits =
        parseCodeDescriptionList(projection.getOrgUnitCodes(), projection.getOrgUnitNames());
    List<String> fspIds = parseFspIds(projection.getFspIds());
    List<CodeDescriptionDto> preferredSpecies =
        parsePreferredSpecies(
            projection.getPreferredSpeciesCodes(), projection.getPreferredSpeciesNames());

    return new StockingStandardsSearchResponseDto(
        projection.getStandardsRegimeId(),
        StringUtil.nullIfBlank(projection.getStandardsRegimeName()),
        isRegimeExpired(projection.getExpiryDate()),
        StringUtil.nullIfBlank(projection.getStandardsObjective()),
        preferredSpecies,
        fspIds,
        StringUtil.nullIfBlank(projection.getBgcZone()),
        StringUtil.nullIfBlank(projection.getBgcSubZone()),
        StringUtil.nullIfBlank(projection.getBgcVariant()),
        StringUtil.nullIfBlank(projection.getBgcPhase()),
        StringUtil.nullIfBlank(projection.getBecSiteSeries()),
        StringUtil.nullIfBlank(projection.getBecSiteType()),
        StringUtil.nullIfBlank(projection.getBecSeral()),
        orgUnits,
        clients,
        projection.getApprovedDate());
  }

  private boolean isRegimeExpired(LocalDateTime expiryDate) {
    if (expiryDate == null) {
      return false;
    }
    return expiryDate.toLocalDate().isBefore(LocalDate.now(VANCOUVER));
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
        result.add(new CodeDescriptionDto(code, StringUtil.nullIfBlank(name)));
      }
    }
    return result;
  }

  private List<CodeDescriptionDto> parseCodeDescriptionList(String codes, String names) {
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
        result.add(new CodeDescriptionDto(code, StringUtil.nullIfBlank(name)));
      }
    }
    return result;
  }

  private List<String> parseFspIds(String fspIds) {
    if (fspIds == null || fspIds.isBlank()) {
      return List.of();
    }
    return Arrays.stream(fspIds.split(",")).map(String::trim).filter(s -> !s.isBlank()).toList();
  }

  private List<ForestClientDto> buildClientList(
      String clientNumbers, Map<String, ForestClientDto> clientMap) {
    if (clientNumbers == null || clientNumbers.isBlank()) {
      return List.of();
    }
    return Arrays.stream(clientNumbers.split(","))
        .map(String::trim)
        .filter(s -> !s.isBlank())
        .distinct()
        .map(clientMap::get)
        .filter(dto -> dto != null)
        .toList();
  }

  private Map<String, ForestClientDto> buildClientMap(List<String> clientNumbers) {
    if (clientNumbers.isEmpty()) {
      return new HashMap<>();
    }
    List<ForestClientDto> clients =
        forestClientService.searchByClientNumbers(0, clientNumbers.size(), clientNumbers);
    return clients.stream().collect(Collectors.toMap(ForestClientDto::clientNumber, c -> c));
  }
}
