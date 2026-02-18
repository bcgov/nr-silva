package ca.bc.gov.restapi.results.common.service;

import ca.bc.gov.restapi.results.common.dto.CodeDescriptionDto;
import java.util.List;

public interface CodeService {
  List<CodeDescriptionDto> getAllSilvBaseCode();

  List<CodeDescriptionDto> getAllSilvTechniqueCode();

  List<CodeDescriptionDto> getAllSilvMethodCode();

  List<CodeDescriptionDto> getAllSilvObjectiveCode();

  List<CodeDescriptionDto> getAllSilvFundSrceCode();

  List<CodeDescriptionDto> findAllCategories(boolean includeExpired);

  List<CodeDescriptionDto> findAllOrgUnits();

  List<CodeDescriptionDto> getAllOpenStatusCode();

  List<CodeDescriptionDto> getAllDisturbanceCode();

  List<CodeDescriptionDto> getAllSilvSystemCode();

  List<CodeDescriptionDto> getAllSilvSystemVariantCode();

  List<CodeDescriptionDto> getAllSilvCutPhaseCode();
}
