package ca.bc.gov.restapi.results.common.service.impl;

import ca.bc.gov.restapi.results.common.dto.CodeDescriptionDto;
import ca.bc.gov.restapi.results.common.entity.GenericCodeEntity;
import ca.bc.gov.restapi.results.common.projection.OrgUnitProjection;
import ca.bc.gov.restapi.results.common.repository.GenericCodeRepository;
import ca.bc.gov.restapi.results.common.repository.OrgUnitRepository;
import ca.bc.gov.restapi.results.common.service.CodeService;
import ca.bc.gov.restapi.results.common.util.CodeConverterUtil;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.BooleanUtils;

@Slf4j
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class AbstractCodeService implements CodeService {
  protected GenericCodeRepository<?> silvBaseCodeRepository;
  protected GenericCodeRepository<?> silvTechniqueCodeRepository;
  protected GenericCodeRepository<?> silvMethodCodeRepository;
  protected GenericCodeRepository<?> silvObjectiveCodeRepository;
  protected GenericCodeRepository<?> silvFundSrceCodeRepository;
  protected GenericCodeRepository<?> silvSystemCodeRepository;
  protected GenericCodeRepository<?> silvSystemVariantCodeRepository;
  protected GenericCodeRepository<?> silvCutPhaseCodeRepository;
  protected GenericCodeRepository<?> disturbanceCodeRepository;
  protected GenericCodeRepository<?> openCategoryCodeRepository;
  protected GenericCodeRepository<?> openingStatusCodeRepository;
  protected GenericCodeRepository<?> silvDamageAgentCodeRepository;
  protected GenericCodeRepository<?> stockingStatusCodeRepository;
  protected GenericCodeRepository<?> stockingTypeCodeRepository;
  protected GenericCodeRepository<?> silvTreeSpeciesCodeRepository;
  protected OrgUnitRepository orgUnitRepository;

  private List<CodeDescriptionDto> getCodes(
      GenericCodeRepository<?> repository, boolean effectiveOnly) {
    List<? extends GenericCodeEntity> entities = repository.findAllByOrderByExpiryDateDesc();
    if (effectiveOnly) {
      entities = entities.stream().filter(GenericCodeEntity::isEffective).toList();
    }
    return CodeConverterUtil.toCodeDescriptionDtos(entities);
  }

  @Override
  public List<CodeDescriptionDto> getAllSilvBaseCode(boolean effectiveOnly) {
    return getCodes(silvBaseCodeRepository, effectiveOnly);
  }

  @Override
  public List<CodeDescriptionDto> getAllSilvTechniqueCode(boolean effectiveOnly) {
    return getCodes(silvTechniqueCodeRepository, effectiveOnly);
  }

  @Override
  public List<CodeDescriptionDto> getAllSilvMethodCode(boolean effectiveOnly) {
    return getCodes(silvMethodCodeRepository, effectiveOnly);
  }

  @Override
  public List<CodeDescriptionDto> getAllSilvObjectiveCode(boolean effectiveOnly) {
    return getCodes(silvObjectiveCodeRepository, effectiveOnly);
  }

  @Override
  public List<CodeDescriptionDto> getAllSilvFundSrceCode(boolean effectiveOnly) {
    return getCodes(silvFundSrceCodeRepository, effectiveOnly);
  }

  @Override
  public List<CodeDescriptionDto> getAllSilvSystemCode(boolean effectiveOnly) {
    return getCodes(silvSystemCodeRepository, effectiveOnly);
  }

  @Override
  public List<CodeDescriptionDto> getAllSilvSystemVariantCode(boolean effectiveOnly) {
    return getCodes(silvSystemVariantCodeRepository, effectiveOnly);
  }

  @Override
  public List<CodeDescriptionDto> getAllSilvCutPhaseCode(boolean effectiveOnly) {
    return getCodes(silvCutPhaseCodeRepository, effectiveOnly);
  }

  @Override
  public List<CodeDescriptionDto> getAllDisturbanceCode(boolean effectiveOnly) {
    return getCodes(disturbanceCodeRepository, effectiveOnly);
  }

  @Override
  public List<CodeDescriptionDto> getAllOpenStatusCode(boolean effectiveOnly) {
    return getCodes(openingStatusCodeRepository, effectiveOnly);
  }

  @Override
  public List<CodeDescriptionDto> getAllSilvDamageAgentCode(boolean effectiveOnly) {
    return getCodes(silvDamageAgentCodeRepository, effectiveOnly);
  }

  @Override
  public List<CodeDescriptionDto> getAllStockingStatusCode(boolean effectiveOnly) {
    return getCodes(stockingStatusCodeRepository, effectiveOnly);
  }

  @Override
  public List<CodeDescriptionDto> getAllStockingTypeCode(boolean effectiveOnly) {
    return getCodes(stockingTypeCodeRepository, effectiveOnly);
  }

  @Override
  public List<CodeDescriptionDto> getAllSilvTreeSpeciesCode(boolean effectiveOnly) {
    return getCodes(silvTreeSpeciesCodeRepository, effectiveOnly);
  }

  @Override
  public List<CodeDescriptionDto> findAllCategories(boolean includeExpired) {
    log.info("Getting all open category codes. Include expired: {}", includeExpired);
    // Prefer converting entity results through the shared converter utility when possible.
    List<? extends GenericCodeEntity> entities =
        openCategoryCodeRepository.findAllByOrderByExpiryDateDesc();

    log.info(
        "Found {} open category codes ({}cluding expired)",
        entities.size(),
        BooleanUtils.toString(includeExpired, "in", "ex"));

    if (!includeExpired) {
      List<? extends GenericCodeEntity> filtered =
          entities.stream().filter(e -> !e.isExpired()).toList();
      return CodeConverterUtil.toCodeDescriptionDtos(filtered);
    }

    return CodeConverterUtil.toCodeDescriptionDtos(entities);
  }

  @Override
  public List<CodeDescriptionDto> findAllOrgUnits(boolean districtsOnly) {
    log.info("Getting all org units. Districts only: {}", districtsOnly);
    LocalDate today = LocalDate.now();
    if (districtsOnly) {
      List<CodeDescriptionDto> result =
          orgUnitRepository.findActiveDistricts(today).stream()
              .map(o -> new CodeDescriptionDto(o.getOrgUnitCode(), o.getOrgUnitName()))
              .toList();
      log.info("Found {} district org units", result.size());
      return result;
    }
    List<CodeDescriptionDto> result =
        orgUnitRepository.findAllActive(today).stream()
            .sorted(
                Comparator.<OrgUnitProjection, Integer>comparing(
                        o ->
                            switch (o.getOrgUnitCode().charAt(0)) {
                              case 'D' -> 0;
                              case 'R' -> 1;
                              case 'H' -> 2;
                              default -> 3;
                            })
                    .thenComparing(OrgUnitProjection::getOrgUnitCode))
            .map(o -> new CodeDescriptionDto(o.getOrgUnitCode(), o.getOrgUnitName()))
            .toList();
    log.info("Found {} org units", result.size());
    return result;
  }
}
