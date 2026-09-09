package ca.bc.gov.restapi.results.postgres.service;

import ca.bc.gov.restapi.results.common.security.LoggedUserHelper;
import ca.bc.gov.restapi.results.postgres.dto.BecDataDto;
import ca.bc.gov.restapi.results.postgres.dto.CreateStockingStandardRequestDto;
import ca.bc.gov.restapi.results.postgres.dto.CreateStockingStandardResponseDto;
import ca.bc.gov.restapi.results.postgres.dto.StockingLayerDto;
import ca.bc.gov.restapi.results.postgres.dto.StockingSpeciesDto;
import ca.bc.gov.restapi.results.postgres.entity.StandardsRegimeClientEntity;
import ca.bc.gov.restapi.results.postgres.entity.StandardsRegimeEntity;
import ca.bc.gov.restapi.results.postgres.entity.StandardsRegimeLayerEntity;
import ca.bc.gov.restapi.results.postgres.entity.StandardsRegimeLayerSpeciesEntity;
import ca.bc.gov.restapi.results.postgres.entity.StandardsRegimeOrgUnitEntity;
import ca.bc.gov.restapi.results.postgres.entity.StandardsRegimeSiteSeriesEntity;
import ca.bc.gov.restapi.results.postgres.enums.StockingLayerType;
import ca.bc.gov.restapi.results.postgres.enums.StockingSpeciesMilestone;
import ca.bc.gov.restapi.results.postgres.enums.StockingStandardAuthorityType;
import ca.bc.gov.restapi.results.postgres.enums.StockingType;
import ca.bc.gov.restapi.results.postgres.repository.StandardsRegimeClientPostgresRepository;
import ca.bc.gov.restapi.results.postgres.repository.StandardsRegimeLayerPostgresRepository;
import ca.bc.gov.restapi.results.postgres.repository.StandardsRegimeLayerSpeciesPostgresRepository;
import ca.bc.gov.restapi.results.postgres.repository.StandardsRegimeOrgUnitPostgresRepository;
import ca.bc.gov.restapi.results.postgres.repository.StandardsRegimeSiteSeriesPostgresRepository;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/** Service that handles the full lifecycle of creating a new Stocking Standard. */
@Service
@RequiredArgsConstructor
@ConditionalOnProperty(prefix = "server", name = "primary-db", havingValue = "postgres")
public class CreateStockingStandardService {

  private final StockingStandardValidationService validationService;
  private final ca.bc.gov.restapi.results.postgres.repository.StockingStandardsPostgresRepository
      standardsRegimeRepository;
  private final StandardsRegimeOrgUnitPostgresRepository orgUnitLinkRepository;
  private final StandardsRegimeClientPostgresRepository clientLinkRepository;
  private final StandardsRegimeSiteSeriesPostgresRepository siteSeriesRepository;
  private final StandardsRegimeLayerPostgresRepository layerRepository;
  private final StandardsRegimeLayerSpeciesPostgresRepository layerSpeciesRepository;
  private final LoggedUserHelper loggedUserHelper;
  private final JdbcTemplate jdbcTemplate;

  /**
   * Creates a new Stocking Standard (standards_regime) with its org units, clients, BEC data,
   * layers and species, all in a single transaction.
   *
   * @param dto the stocking standard creation request
   * @return a {@link CreateStockingStandardResponseDto} containing the new standard's ID
   */
  @Transactional
  public CreateStockingStandardResponseDto create(CreateStockingStandardRequestDto dto) {
    // Step 1: validate everything up front — zero writes on any validation failure.
    List<Long> orgUnitNos = validationService.validate(dto);

    String auditUserId = loggedUserHelper.getAuditUserId();
    LocalDateTime now = LocalDateTime.now();

    // Step 2: allocate the standards_regime ID and persist the base record.
    Long standardsRegimeId =
        jdbcTemplate.queryForObject("SELECT nextval('silva.standards_regime_id_seq')", Long.class);

    StandardsRegimeEntity standardsRegime =
        StandardsRegimeEntity.builder()
            .id(standardsRegimeId)
            .standardsRegimeName(dto.name() != null ? dto.name().trim() : null)
            .standardsRegimeStatusCode("DFT")
            .standardsObjective(dto.objective().trim())
            .geographicDescription(dto.location() != null ? dto.location().trim() : null)
            .mofDefaultStandardInd(
                dto.authorityType() == StockingStandardAuthorityType.MINISTRY_DEFAULT ? "Y" : "N")
            .alternativeMethodInd(Boolean.TRUE.equals(dto.alternativeMethodSelected()) ? "Y" : "N")
            .regenObligationInd(dto.stockingType() == StockingType.REGEN_OBLIGATION ? "Y" : "N")
            .regenDelayOffsetYrs(dto.regenDelayYears())
            .freeGrowingLateOffsetYrs(dto.freeGrowingYears())
            .noRegenEarlyOffsetYrs(dto.earlyYears())
            .noRegenLateOffsetYrs(dto.lateYears())
            .additionalStandards(
                dto.additionalStandards() != null ? dto.additionalStandards().trim() : null)
            .alternateInfo(dto.alternateInfo() != null ? dto.alternateInfo().trim() : null)
            .entryUserid(auditUserId)
            .entryTimestamp(now)
            .updateUserid(auditUserId)
            .updateTimestamp(now)
            .revisionCount(1)
            .build();
    standardsRegimeRepository.save(standardsRegime);

    // Step 3: org units.
    for (Long orgUnitNo : orgUnitNos) {
      orgUnitLinkRepository.save(
          StandardsRegimeOrgUnitEntity.builder()
              .standardsRegimeId(standardsRegimeId)
              .orgUnitNo(orgUnitNo)
              .entryUserid(auditUserId)
              .entryTimestamp(now)
              .updateUserid(auditUserId)
              .updateTimestamp(now)
              .revisionCount(1)
              .build());
    }

    // Step 4: clients (Operational Plan only).
    if (dto.authorityType() == StockingStandardAuthorityType.OPERATIONAL_PLAN
        && dto.clientNumbers() != null) {
      for (String clientNumber : dto.clientNumbers()) {
        clientLinkRepository.save(
            StandardsRegimeClientEntity.builder()
                .standardsRegimeId(standardsRegimeId)
                .clientNumber(clientNumber.trim())
                .entryUserid(auditUserId)
                .entryTimestamp(now)
                .updateUserid(auditUserId)
                .updateTimestamp(now)
                .revisionCount(1)
                .build());
      }
    }

    // Step 5: BEC data.
    if (dto.becData() != null) {
      for (BecDataDto bec : dto.becData()) {
        Long siteSeriesId =
            jdbcTemplate.queryForObject(
                "SELECT nextval('silva.standard_regime_site_series_id_seq')", Long.class);
        siteSeriesRepository.save(
            StandardsRegimeSiteSeriesEntity.builder()
                .id(siteSeriesId)
                .standardsRegimeId(standardsRegimeId)
                .bgcZoneCode(bec.bgcZoneCode())
                .bgcSubzoneCode(bec.bgcSubzoneCode())
                .bgcVariant(bec.variant())
                .bgcPhase(bec.phase())
                .becSiteSeries(bec.siteSeries())
                .becSiteType(bec.sitePhase())
                .entryUserid(auditUserId)
                .entryTimestamp(now)
                .updateUserid(auditUserId)
                .updateTimestamp(now)
                .revisionCount(1)
                .build());
      }
    }

    // Step 6: layer(s) and their species.
    List<StockingLayerDto> layers =
        dto.layerType() == StockingLayerType.SINGLE ? List.of(dto.singleLayer()) : dto.multiLayers();
    for (StockingLayerDto layer : layers) {
      Long layerId =
          jdbcTemplate.queryForObject(
              "SELECT nextval('silva.standards_regime_layer_id_seq')", Long.class);
      StandardsRegimeLayerEntity layerEntity =
          StandardsRegimeLayerEntity.builder()
              .id(layerId)
              .standardsRegimeId(standardsRegimeId)
              .stockingLayerCode(layer.layerCode())
              .treeSizeUnitCode(layer.heightRelativeToCompUnitCode())
              .residualBasalArea(layer.minResidualBasalArea())
              .minHorizontalDistance(layer.minHorizontalDistance())
              .minPrefStockingStandard(layer.minPreferredWellSpacedTrees())
              .minStockingStandard(layer.minWellSpacedTrees())
              .targetStocking(layer.targetWellSpacedTrees())
              .minPostSpacing(layer.minPostSpacingDensity())
              .maxPostSpacing(layer.maxPostSpacingDensity())
              .maxConifer(layer.maxConiferous())
              .hghtRelativeToComp(layer.heightRelativeToComp())
              .entryUserid(auditUserId)
              .entryTimestamp(now)
              .updateUserid(auditUserId)
              .updateTimestamp(now)
              .revisionCount(1)
              .build();
      layerRepository.save(layerEntity);

      int order = 1;
      for (StockingSpeciesDto species : dto.species()) {
        layerSpeciesRepository.save(
            StandardsRegimeLayerSpeciesEntity.builder()
                .standardsRegimeLayerId(layerId)
                .silvTreeSpeciesCode(species.speciesCode().trim())
                .speciesOrder(order++)
                .speciesTypeCode(species.speciesType().getCode())
                .minHeight(species.minHeight())
                .regenMilestoneInd(
                    species.milestone() == StockingSpeciesMilestone.FREE_GROWING ? "N" : "Y")
                .freeGrowingMilestoneInd(
                    species.milestone() == StockingSpeciesMilestone.REGEN ? "N" : "Y")
                .entryUserid(auditUserId)
                .entryTimestamp(now)
                .updateUserid(auditUserId)
                .updateTimestamp(now)
                .revisionCount(1)
                .build());
      }
    }

    return new CreateStockingStandardResponseDto(standardsRegimeId);
  }
}
