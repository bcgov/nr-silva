package ca.bc.gov.restapi.results.common.service.impl;

import ca.bc.gov.restapi.results.common.dto.CodeDescriptionDto;
import ca.bc.gov.restapi.results.common.dto.ForestClientDto;
import ca.bc.gov.restapi.results.common.dto.StockingStandardsSearchFilterDto;
import ca.bc.gov.restapi.results.common.dto.StockingStandardsSearchResponseDto;
import ca.bc.gov.restapi.results.common.dto.opening.OpeningDetailsBecDto;
import ca.bc.gov.restapi.results.common.dto.stockingstandards.StockingStandardDetailsDto;
import ca.bc.gov.restapi.results.common.dto.stockingstandards.StockingStandardDetailsLayerDto;
import ca.bc.gov.restapi.results.common.dto.stockingstandards.StockingStandardDetailsSpeciesDto;
import ca.bc.gov.restapi.results.common.dto.stockingstandards.StockingStandardsCommentSearchFilterDto;
import ca.bc.gov.restapi.results.common.dto.stockingstandards.StockingStandardsCommentSearchResponseDto;
import ca.bc.gov.restapi.results.common.enums.StockingStandardsCommentLocationCode;
import ca.bc.gov.restapi.results.common.projection.StockingStandardsCommentSearchProjection;
import ca.bc.gov.restapi.results.common.projection.StockingStandardsSearchProjection;
import ca.bc.gov.restapi.results.common.projection.stockingstandards.StockingStandardBecProjection;
import ca.bc.gov.restapi.results.common.projection.stockingstandards.StockingStandardDetailsProjection;
import ca.bc.gov.restapi.results.common.projection.stockingstandards.StockingStandardLayerProjection;
import ca.bc.gov.restapi.results.common.projection.stockingstandards.StockingStandardOrgUnitProjection;
import ca.bc.gov.restapi.results.common.projection.stockingstandards.StockingStandardSpeciesProjection;
import ca.bc.gov.restapi.results.common.repository.StockingStandardsRepository;
import ca.bc.gov.restapi.results.common.service.ForestClientService;
import ca.bc.gov.restapi.results.common.service.StockingStandardsService;
import ca.bc.gov.restapi.results.common.util.DateUtil;
import ca.bc.gov.restapi.results.common.util.StringUtil;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

/** Shared stocking standards search and detail mapping. */
@Slf4j
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class AbstractStockingStandardsService implements StockingStandardsService {

  protected StockingStandardsRepository stockingStandardsRepository;
  protected ForestClientService forestClientService;

  @Override
  public Page<StockingStandardsSearchResponseDto> searchStockingStandards(
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
  public Page<StockingStandardsCommentSearchResponseDto> searchStockingStandardsComments(
      StockingStandardsCommentSearchFilterDto filters, Pageable pagination) {
    DateUtil.validateDateRange(filters.getUpdateDateStart(), filters.getUpdateDateEnd());

    long offset = pagination.getOffset();
    long size = pagination.getPageSize();

    List<StockingStandardsCommentSearchProjection> projections =
        stockingStandardsRepository.searchStockingStandardsComments(filters, offset, size);

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

  @Override
  public Optional<StockingStandardDetailsDto> getStockingStandardDetails(Long stockingStandardId) {
    return Optional.empty();
  }

  protected StockingStandardDetailsDto mapStockingStandardDetails(
      StockingStandardDetailsProjection header,
      List<String> clientNumbers,
      List<StockingStandardOrgUnitProjection> orgUnitProjections,
      List<StockingStandardBecProjection> becProjections,
      List<StockingStandardLayerProjection> layerProjections,
      List<StockingStandardSpeciesProjection> species) {
    List<ForestClientDto> clients =
        buildClientList(String.join(",", clientNumbers), buildClientMap(clientNumbers));
    List<CodeDescriptionDto> orgUnits =
        orgUnitProjections.stream()
            .map(
                orgUnit ->
                    new CodeDescriptionDto(orgUnit.getOrgUnitCode(), orgUnit.getOrgUnitName()))
            .toList();
    List<OpeningDetailsBecDto> becData =
        becProjections.stream()
            .map(
                bec ->
                    new OpeningDetailsBecDto(
                        bec.getBecZoneCode(),
                        bec.getBecSubzoneCode(),
                        bec.getBecVariant(),
                        bec.getBecPhase(),
                        bec.getBecSiteSeries(),
                        bec.getBecSiteType(),
                        bec.getBecSeral()))
            .toList();
    List<StockingStandardDetailsLayerDto> layers =
        layerProjections.stream().map(layer -> mapLayer(layer, species)).toList();

    return new StockingStandardDetailsDto(
        header.getStockingStandardId(),
        header.getStatusCode(),
        header.getCreatedDate(),
        header.getEffectiveDate(),
        header.getExpiryDate(),
        StringUtil.nullIfBlank(header.getObjective()),
        StringUtil.nullIfBlank(header.getName()),
        StringUtil.nullIfBlank(header.getLocation()),
        "Y".equalsIgnoreCase(header.getMinistryDefaultInd()),
        yesNoToBoolean(header.getAlternativeMethodSelected()),
        orgUnits,
        clients,
        becData,
        "Y".equalsIgnoreCase(header.getRegenObligationInd())
            ? "REGEN_OBLIGATION"
            : "STOCKING_REQUIREMENT",
        header.getRegenDelayYears(),
        header.getFreeGrowingLateYears(),
        header.getEarlyYears(),
        header.getLateYears(),
        layers.stream().anyMatch(layer -> "I".equals(layer.layerCode())) ? "SINGLE" : "MULTI",
        StringUtil.nullIfBlank(header.getAdditionalStandards()),
        layers);
  }

  protected StockingStandardDetailsLayerDto mapLayer(
      StockingStandardLayerProjection layer, List<StockingStandardSpeciesProjection> species) {
    List<StockingStandardDetailsSpeciesDto> layerSpecies =
        species.stream()
            .filter(item -> item.getLayerId().equals(layer.getLayerId()))
            .map(
                item ->
                    new StockingStandardDetailsSpeciesDto(
                        item.getSpeciesCode(),
                        mapSpeciesType(item.getSpeciesTypeCode()),
                        item.getMinHeight(),
                        mapMilestone(
                            item.getRegenMilestoneInd(), item.getFreeGrowingMilestoneInd())))
            .toList();
    return new StockingStandardDetailsLayerDto(
        layer.getLayerCode(),
        layer.getMinWellSpacedTrees(),
        layer.getMinPreferredWellSpacedTrees(),
        layer.getMinHorizontalDistanceWellSpacedTrees(),
        layer.getTargetWellSpacedTrees(),
        layer.getMinResidualBasalArea(),
        layer.getMinPostSpacingDensity(),
        layer.getMaxPostSpacingDensity(),
        layer.getMaxConiferous(),
        layer.getHeightRelativeToComp(),
        layer.getHeightRelativeToCompUnitCode(),
        layerSpecies);
  }

  protected String mapSpeciesType(String speciesTypeCode) {
    return switch (speciesTypeCode) {
      case "PRF" -> "PREFERRED";
      case "ACC" -> "ACCEPTABLE";
      case "ECO" -> "ECOLOGICALLY_SUITABLE";
      default -> speciesTypeCode;
    };
  }

  protected String mapMilestone(String regenMilestoneInd, String freeGrowingMilestoneInd) {
    if (regenMilestoneInd == null && freeGrowingMilestoneInd == null) {
      return null;
    }
    if ("Y".equalsIgnoreCase(regenMilestoneInd)
        && "Y".equalsIgnoreCase(freeGrowingMilestoneInd)) {
      return "BOTH";
    }
    return "Y".equalsIgnoreCase(regenMilestoneInd) ? "REGEN" : "FREE_GROWING";
  }

  protected Boolean yesNoToBoolean(String indicator) {
    return indicator == null ? null : "Y".equalsIgnoreCase(indicator);
  }

  private StockingStandardsCommentSearchResponseDto mapToCommentSearchResponse(
      StockingStandardsCommentSearchProjection projection, Map<String, ForestClientDto> clientMap) {
    List<ForestClientDto> clients = buildClientList(projection.getClientNumbers(), clientMap);
    List<CodeDescriptionDto> orgUnits =
        parseCodeDescriptionList(projection.getOrgUnitCodes(), projection.getOrgUnitNames());
    List<String> fspIds = parseFspIds(projection.getFspIds());
    StockingStandardsCommentLocationCode commentLocation =
        StockingStandardsCommentLocationCode.valueOf(projection.getCommentLocation());

    return new StockingStandardsCommentSearchResponseDto(
        projection.getStandardsRegimeId(),
        commentLocation,
        DateUtil.isExpired(projection.getExpiryDate()),
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
    List<String> bgcList = parseBgcList(projection.getBgcList());
    boolean isDefaultStandard = "Y".equalsIgnoreCase(projection.getMofDefaultStandardInd());

    return new StockingStandardsSearchResponseDto(
        projection.getStandardsRegimeId(),
        StringUtil.nullIfBlank(projection.getStandardsRegimeName()),
        DateUtil.isExpired(projection.getExpiryDate()),
        StringUtil.nullIfBlank(projection.getStandardsObjective()),
        preferredSpecies,
        fspIds,
        bgcList,
        isDefaultStandard,
        orgUnits,
        clients,
        projection.getApprovedDate());
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

  protected Map<String, ForestClientDto> buildClientMap(List<String> clientNumbers) {
    if (clientNumbers.isEmpty()) {
      return new HashMap<>();
    }
    List<ForestClientDto> clients =
        forestClientService.searchByClientNumbers(0, clientNumbers.size(), clientNumbers);
    return clients.stream().collect(Collectors.toMap(ForestClientDto::clientNumber, c -> c));
  }

  private List<String> parseBgcList(String raw) {
    if (raw == null || raw.isBlank()) {
      return List.of();
    }
    return Arrays.stream(raw.split("\\|\\|")).map(String::trim).filter(s -> !s.isBlank()).toList();
  }
}
