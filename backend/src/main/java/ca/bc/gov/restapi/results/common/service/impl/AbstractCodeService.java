package ca.bc.gov.restapi.results.common.service.impl;

import ca.bc.gov.restapi.results.common.configuration.SilvaConfiguration;
import ca.bc.gov.restapi.results.common.dto.CodeDescriptionDto;
import ca.bc.gov.restapi.results.common.entity.GenericCodeEntity;
import ca.bc.gov.restapi.results.common.repository.GenericCodeRepository;
import ca.bc.gov.restapi.results.common.repository.OrgUnitRepository;
import ca.bc.gov.restapi.results.common.service.CodeService;
import ca.bc.gov.restapi.results.common.util.CodeConverterUtil;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
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
  protected SilvaConfiguration silvaConfiguration;

  @Override
  public List<CodeDescriptionDto> getAllSilvBaseCode() {
    return CodeConverterUtil.toCodeDescriptionDtos(
        silvBaseCodeRepository.findAllByOrderByExpiryDateDesc());
  }

  @Override
  public List<CodeDescriptionDto> getAllSilvTechniqueCode() {
    return CodeConverterUtil.toCodeDescriptionDtos(
        silvTechniqueCodeRepository.findAllByOrderByExpiryDateDesc());
  }

  @Override
  public List<CodeDescriptionDto> getAllSilvMethodCode() {
    return CodeConverterUtil.toCodeDescriptionDtos(
        silvMethodCodeRepository.findAllByOrderByExpiryDateDesc());
  }

  @Override
  public List<CodeDescriptionDto> getAllSilvObjectiveCode() {
    return CodeConverterUtil.toCodeDescriptionDtos(
        silvObjectiveCodeRepository.findAllByOrderByExpiryDateDesc());
  }

  @Override
  public List<CodeDescriptionDto> getAllSilvFundSrceCode() {
    return CodeConverterUtil.toCodeDescriptionDtos(
        silvFundSrceCodeRepository.findAllByOrderByExpiryDateDesc());
  }

  @Override
  public List<CodeDescriptionDto> getAllSilvSystemCode() {
    return CodeConverterUtil.toCodeDescriptionDtos(
        silvSystemCodeRepository.findAllByOrderByExpiryDateDesc());
  }

  @Override
  public List<CodeDescriptionDto> getAllSilvSystemVariantCode() {
    return CodeConverterUtil.toCodeDescriptionDtos(
        silvSystemVariantCodeRepository.findAllByOrderByExpiryDateDesc());
  }

  @Override
  public List<CodeDescriptionDto> getAllSilvCutPhaseCode() {
    return CodeConverterUtil.toCodeDescriptionDtos(
        silvCutPhaseCodeRepository.findAllByOrderByExpiryDateDesc());
  }

  @Override
  public List<CodeDescriptionDto> getAllDisturbanceCode() {
    return CodeConverterUtil.toCodeDescriptionDtos(
        disturbanceCodeRepository.findAllByOrderByExpiryDateDesc());
  }

  @Override
  public List<CodeDescriptionDto> getAllOpenStatusCode() {
    return CodeConverterUtil.toCodeDescriptionDtos(
        openingStatusCodeRepository.findAllByOrderByExpiryDateDesc());
  }

  @Override
  public List<CodeDescriptionDto> getAllSilvDamageAgentCode() {
    return CodeConverterUtil.toCodeDescriptionDtos(
        silvDamageAgentCodeRepository.findAllByOrderByExpiryDateDesc());
  }

  @Override
  public List<CodeDescriptionDto> getAllStockingStatusCode() {
    return CodeConverterUtil.toCodeDescriptionDtos(
        stockingStatusCodeRepository.findAllByOrderByExpiryDateDesc());
  }

  @Override
  public List<CodeDescriptionDto> getAllStockingTypeCode() {
    return CodeConverterUtil.toCodeDescriptionDtos(
        stockingTypeCodeRepository.findAllByOrderByExpiryDateDesc());
  }

  @Override
  public List<CodeDescriptionDto> getAllSilvTreeSpeciesCode() {
    return CodeConverterUtil.toCodeDescriptionDtos(
        silvTreeSpeciesCodeRepository.findAllByOrderByExpiryDateDesc());
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
  public List<CodeDescriptionDto> findAllOrgUnits() {
    log.info("Getting all org units for the search openings");

    if (Objects.isNull(silvaConfiguration.getOrgUnits())
        || silvaConfiguration.getOrgUnits().isEmpty()) {
      log.warn("No Org Units from the properties file.");
      return List.of();
    }

    LocalDate now = LocalDate.now();
    List<CodeDescriptionDto> orgUnits =
        orgUnitRepository.findAllByOrgUnitCodeIn(silvaConfiguration.getOrgUnits()).stream()
            .map(
                orgUnit -> {
                  boolean expired =
                      orgUnit.getExpiryDate() != null && orgUnit.getExpiryDate().isBefore(now);
                  return new CodeDescriptionDto(
                      orgUnit.getOrgUnitCode(),
                      expired ? orgUnit.getOrgUnitName() + " (Expired)" : orgUnit.getOrgUnitName());
                })
            .toList();

    log.info("Found {} org units by codes", orgUnits.size());
    return orgUnits;
  }
}
