package ca.bc.gov.restapi.results.common.service.impl;

import ca.bc.gov.restapi.results.common.dto.CodeDescriptionDto;
import ca.bc.gov.restapi.results.common.repository.GenericCodeRepository;
import ca.bc.gov.restapi.results.common.service.CodeService;
import ca.bc.gov.restapi.results.common.util.CodeConverterUtil;
import java.util.List;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public abstract class AbstractCodeService implements CodeService {
  protected abstract GenericCodeRepository<?> getSilvBaseCodeRepository();

  protected abstract GenericCodeRepository<?> getSilvTechniqueCodeRepository();

  protected abstract GenericCodeRepository<?> getSilvMethodCodeRepository();

  protected abstract GenericCodeRepository<?> getSilvObjectiveCodeRepository();

  protected abstract GenericCodeRepository<?> getSilvFundSrceCodeRepository();

  @Override
  public List<CodeDescriptionDto> getAllSilvBaseCode() {
    return CodeConverterUtil.toCodeDescriptionDtos(getSilvBaseCodeRepository().findAll());
  }

  @Override
  public List<CodeDescriptionDto> getAllSilvTechniqueCode() {
    return CodeConverterUtil.toCodeDescriptionDtos(getSilvTechniqueCodeRepository().findAll());
  }

  @Override
  public List<CodeDescriptionDto> getAllSilvMethodCode() {
    return CodeConverterUtil.toCodeDescriptionDtos(getSilvMethodCodeRepository().findAll());
  }

  @Override
  public List<CodeDescriptionDto> getAllSilvObjectiveCode() {
    return CodeConverterUtil.toCodeDescriptionDtos(getSilvObjectiveCodeRepository().findAll());
  }

  @Override
  public List<CodeDescriptionDto> getAllSilvFundSrceCode() {
    return CodeConverterUtil.toCodeDescriptionDtos(getSilvFundSrceCodeRepository().findAll());
  }
}
