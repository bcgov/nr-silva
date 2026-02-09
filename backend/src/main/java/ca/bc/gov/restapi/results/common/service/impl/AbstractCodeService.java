package ca.bc.gov.restapi.results.common.service.impl;

import ca.bc.gov.restapi.results.common.configuration.SilvaConfiguration;
import ca.bc.gov.restapi.results.common.dto.CodeDescriptionDto;
import ca.bc.gov.restapi.results.common.entity.GenericCodeEntity;
import ca.bc.gov.restapi.results.common.repository.GenericCodeRepository;
import ca.bc.gov.restapi.results.common.repository.OrgUnitRepository;
import ca.bc.gov.restapi.results.common.service.CodeService;
import ca.bc.gov.restapi.results.common.util.CodeConverterUtil;
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
  protected GenericCodeRepository<?> openCategoryCodeRepository;
  protected OrgUnitRepository orgUnitRepository;
  protected SilvaConfiguration silvaConfiguration;

  @Override
  public List<CodeDescriptionDto> getAllSilvBaseCode() {
    return CodeConverterUtil.toCodeDescriptionDtos(silvBaseCodeRepository.findAll());
  }

  @Override
  public List<CodeDescriptionDto> getAllSilvTechniqueCode() {
    return CodeConverterUtil.toCodeDescriptionDtos(silvTechniqueCodeRepository.findAll());
  }

  @Override
  public List<CodeDescriptionDto> getAllSilvMethodCode() {
    return CodeConverterUtil.toCodeDescriptionDtos(silvMethodCodeRepository.findAll());
  }

  @Override
  public List<CodeDescriptionDto> getAllSilvObjectiveCode() {
    return CodeConverterUtil.toCodeDescriptionDtos(silvObjectiveCodeRepository.findAll());
  }

  @Override
  public List<CodeDescriptionDto> getAllSilvFundSrceCode() {
    return CodeConverterUtil.toCodeDescriptionDtos(silvFundSrceCodeRepository.findAll());
  }

  @Override
  public List<CodeDescriptionDto> getAllOpenStatusCode() {
    return CodeConverterUtil.toCodeDescriptionDtos(openCategoryCodeRepository.findAll());
  }

  @Override
  public List<CodeDescriptionDto> findAllCategories(boolean includeExpired) {
    log.info("Getting all open category codes. Include expired: {}", includeExpired);
    // Prefer converting entity results through the shared converter utility when possible.
    List<? extends GenericCodeEntity> entities = openCategoryCodeRepository.findAll();

    log.info(
        "Found {} open category codes ({}cluding expired)",
        entities.size(),
        BooleanUtils.toString(includeExpired, "in", "ex"));

    if (!includeExpired) {
      List<? extends GenericCodeEntity> filtered =
          entities.stream().filter(e -> !e.isExpired()).toList();
      return CodeConverterUtil.toCodeDescriptionDtos(filtered);
    }

    return entities.stream()
        .map(
            ent ->
                new CodeDescriptionDto(
                    ent.getCode(),
                    ent.isExpired() ? ent.getDescription() + " (Expired)" : ent.getDescription()))
        .toList();
  }

  @Override
  public List<CodeDescriptionDto> findAllOrgUnits() {
    log.info("Getting all org units for the search openings");

    if (Objects.isNull(silvaConfiguration.getOrgUnits())
        || silvaConfiguration.getOrgUnits().isEmpty()) {
      log.warn("No Org Units from the properties file.");
      return List.of();
    }

    List<CodeDescriptionDto> orgUnits =
        orgUnitRepository.findAllByOrgUnitCodeIn(silvaConfiguration.getOrgUnits()).stream()
            .map(
                orgUnit ->
                    new CodeDescriptionDto(orgUnit.getOrgUnitCode(), orgUnit.getOrgUnitName()))
            .toList();

    log.info("Found {} org units by codes", orgUnits.size());
    return orgUnits;
  }
}
