package ca.bc.gov.restapi.results.postgres.service;

import ca.bc.gov.restapi.results.common.dto.CodeDescriptionDto;
import ca.bc.gov.restapi.results.common.dto.ForestClientDto;
import ca.bc.gov.restapi.results.common.dto.opening.OpeningDetailsBecDto;
import ca.bc.gov.restapi.results.common.dto.stockingstandards.StockingStandardDetailsDto;
import ca.bc.gov.restapi.results.common.dto.stockingstandards.StockingStandardDetailsLayerDto;
import ca.bc.gov.restapi.results.common.dto.stockingstandards.StockingStandardDetailsSpeciesDto;
import ca.bc.gov.restapi.results.common.service.ForestClientService;
import ca.bc.gov.restapi.results.common.service.impl.AbstractStockingStandardsService;
import ca.bc.gov.restapi.results.common.util.StringUtil;
import ca.bc.gov.restapi.results.postgres.entity.StandardsRegimeEntity;
import ca.bc.gov.restapi.results.postgres.entity.StandardsRegimeLayerEntity;
import ca.bc.gov.restapi.results.postgres.entity.StandardsRegimeLayerSpeciesEntity;
import ca.bc.gov.restapi.results.postgres.repository.OrgUnitPostgresRepository;
import ca.bc.gov.restapi.results.postgres.repository.StandardsRegimeClientPostgresRepository;
import ca.bc.gov.restapi.results.postgres.repository.StandardsRegimeLayerPostgresRepository;
import ca.bc.gov.restapi.results.postgres.repository.StandardsRegimeLayerSpeciesPostgresRepository;
import ca.bc.gov.restapi.results.postgres.repository.StandardsRegimeOrgUnitPostgresRepository;
import ca.bc.gov.restapi.results.postgres.repository.StandardsRegimeSiteSeriesPostgresRepository;
import ca.bc.gov.restapi.results.postgres.repository.StockingStandardsPostgresRepository;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

/** PostgreSQL implementation of stocking standards search and detail retrieval. */
@Service
@ConditionalOnProperty(prefix = "server", name = "primary-db", havingValue = "postgres")
public class StockingStandardsPostgresService extends AbstractStockingStandardsService {

  private final StockingStandardsPostgresRepository stockingStandardsRepository;
  private final StandardsRegimeOrgUnitPostgresRepository orgUnitLinkRepository;
  private final OrgUnitPostgresRepository orgUnitRepository;
  private final StandardsRegimeClientPostgresRepository clientRepository;
  private final StandardsRegimeSiteSeriesPostgresRepository siteSeriesRepository;
  private final StandardsRegimeLayerPostgresRepository layerRepository;
  private final StandardsRegimeLayerSpeciesPostgresRepository speciesRepository;

  /** Creates the PostgreSQL stocking standards service. */
  public StockingStandardsPostgresService(
      StockingStandardsPostgresRepository stockingStandardsRepository,
      StandardsRegimeOrgUnitPostgresRepository orgUnitLinkRepository,
      OrgUnitPostgresRepository orgUnitRepository,
      StandardsRegimeClientPostgresRepository clientRepository,
      StandardsRegimeSiteSeriesPostgresRepository siteSeriesRepository,
      StandardsRegimeLayerPostgresRepository layerRepository,
      StandardsRegimeLayerSpeciesPostgresRepository speciesRepository,
      ForestClientService forestClientService) {
    super(stockingStandardsRepository, forestClientService);
    this.stockingStandardsRepository = stockingStandardsRepository;
    this.orgUnitLinkRepository = orgUnitLinkRepository;
    this.orgUnitRepository = orgUnitRepository;
    this.clientRepository = clientRepository;
    this.siteSeriesRepository = siteSeriesRepository;
    this.layerRepository = layerRepository;
    this.speciesRepository = speciesRepository;
  }

  @Override
  public Optional<StockingStandardDetailsDto> getStockingStandardDetails(Long stockingStandardId) {
    return stockingStandardsRepository
        .findById(stockingStandardId)
        .map(standardsRegime -> mapStockingStandardDetails(standardsRegime, stockingStandardId));
  }

  private StockingStandardDetailsDto mapStockingStandardDetails(
      StandardsRegimeEntity standardsRegime, Long stockingStandardId) {
    List<StandardsRegimeLayerEntity> layers =
        layerRepository.findByStandardsRegimeIdOrderByStockingLayerCode(stockingStandardId);
    List<Long> layerIds = layers.stream().map(StandardsRegimeLayerEntity::getId).toList();
    Map<Long, List<StandardsRegimeLayerSpeciesEntity>> speciesByLayer =
        layerIds.isEmpty()
            ? Map.of()
            : speciesRepository
                .findByStandardsRegimeLayerIdInOrderBySpeciesOrder(layerIds)
                .stream()
                .collect(
                    Collectors.groupingBy(
                        StandardsRegimeLayerSpeciesEntity::getStandardsRegimeLayerId));
    List<String> clientNumbers =
        clientRepository.findByStandardsRegimeIdOrderByClientNumber(stockingStandardId).stream()
            .map(client -> client.getClientNumber())
            .toList();
    Map<String, ForestClientDto> clientsByNumber = buildClientMap(clientNumbers);
    List<ForestClientDto> clients =
        clientNumbers.stream().map(clientsByNumber::get).filter(client -> client != null).toList();
    List<Long> orgUnitNumbers =
        orgUnitLinkRepository.findByStandardsRegimeId(stockingStandardId).stream()
            .map(link -> link.getOrgUnitNo())
            .toList();
    List<CodeDescriptionDto> orgUnits =
        orgUnitRepository.findAllById(orgUnitNumbers).stream()
            .sorted(Comparator.comparing(orgUnit -> orgUnit.getOrgUnitCode()))
            .map(
                orgUnit ->
                    new CodeDescriptionDto(orgUnit.getOrgUnitCode(), orgUnit.getOrgUnitName()))
            .toList();
    List<OpeningDetailsBecDto> becData =
        siteSeriesRepository.findByStandardsRegimeIdOrderById(stockingStandardId).stream()
            .map(
                bec ->
                    new OpeningDetailsBecDto(
                        bec.getBgcZoneCode(),
                        bec.getBgcSubzoneCode(),
                        bec.getBgcVariant(),
                        bec.getBgcPhase(),
                        bec.getBecSiteSeries(),
                        bec.getBecSiteType(),
                        bec.getBecSeral()))
            .toList();
    List<StockingStandardDetailsLayerDto> detailLayers =
        layers.stream()
            .map(layer -> mapLayer(layer, speciesByLayer.getOrDefault(layer.getId(), List.of())))
            .toList();

    return new StockingStandardDetailsDto(
        standardsRegime.getId(),
        standardsRegime.getStandardsRegimeStatusCode(),
        standardsRegime.getEntryTimestamp(),
        standardsRegime.getEffectiveDate(),
        standardsRegime.getExpiryDate(),
        StringUtil.nullIfBlank(standardsRegime.getStandardsObjective()),
        StringUtil.nullIfBlank(standardsRegime.getStandardsRegimeName()),
        StringUtil.nullIfBlank(standardsRegime.getGeographicDescription()),
        "Y".equalsIgnoreCase(standardsRegime.getMofDefaultStandardInd()),
        yesNoToBoolean(standardsRegime.getAlternativeMethodInd()),
        orgUnits,
        clients,
        becData,
        "Y".equalsIgnoreCase(standardsRegime.getRegenObligationInd())
            ? "REGEN_OBLIGATION"
            : "STOCKING_REQUIREMENT",
        standardsRegime.getRegenDelayOffsetYrs(),
        standardsRegime.getFreeGrowingLateOffsetYrs(),
        standardsRegime.getNoRegenEarlyOffsetYrs(),
        standardsRegime.getNoRegenLateOffsetYrs(),
        detailLayers.stream().anyMatch(layer -> "I".equals(layer.layerCode()))
            ? "SINGLE"
            : "MULTI",
        StringUtil.nullIfBlank(standardsRegime.getAdditionalStandards()),
        detailLayers);
  }

  private StockingStandardDetailsLayerDto mapLayer(
      StandardsRegimeLayerEntity layer, List<StandardsRegimeLayerSpeciesEntity> species) {
    List<StockingStandardDetailsSpeciesDto> detailSpecies =
        species.stream()
            .map(
                item ->
                    new StockingStandardDetailsSpeciesDto(
                        item.getSilvTreeSpeciesCode(),
                        mapSpeciesType(item.getSpeciesTypeCode()),
                        item.getMinHeight(),
                        mapMilestone(
                            item.getRegenMilestoneInd(), item.getFreeGrowingMilestoneInd())))
            .toList();
    return new StockingStandardDetailsLayerDto(
        layer.getStockingLayerCode(),
        layer.getMinStockingStandard(),
        layer.getMinPrefStockingStandard(),
        layer.getMinHorizontalDistance(),
        layer.getTargetStocking(),
        layer.getResidualBasalArea(),
        layer.getMinPostSpacing(),
        layer.getMaxPostSpacing(),
        layer.getMaxConifer(),
        layer.getHghtRelativeToComp(),
        layer.getTreeSizeUnitCode(),
        detailSpecies);
  }
}
